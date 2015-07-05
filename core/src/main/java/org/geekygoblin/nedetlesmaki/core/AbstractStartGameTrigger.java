/*
 The MIT License (MIT)

 Copyright (c) 2013 devnewton <devnewton@bci.im>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package org.geekygoblin.nedetlesmaki.core;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.artemis.sprite.Sprite;
import im.bci.jnuit.artemis.sprite.SpritePuppetControls;
import im.bci.tmxloader.TmxLayer;
import im.bci.tmxloader.TmxMap;
import im.bci.tmxloader.TmxTileInstance;
import im.bci.tmxloader.TmxTileInstanceEffect;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.geekygoblin.nedetlesmaki.core.components.LevelBackground;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Plate;
import org.geekygoblin.nedetlesmaki.core.backend.Position;
import org.geekygoblin.nedetlesmaki.core.backend.PositionIndexed;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.*;
import org.geekygoblin.nedetlesmaki.core.constants.ColorType;
import org.geekygoblin.nedetlesmaki.core.constants.VirtualResolution;
import org.geekygoblin.nedetlesmaki.core.events.IStartGameTrigger;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.backend.Square;
import pythagoras.f.Vector3;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
public abstract class AbstractStartGameTrigger implements IStartGameTrigger {

    protected final IAssets assets;
    protected final Entity mainMenu;
    protected final Entity inGameUI;
    protected final Entity ingameControls;
    protected final LevelIndex index;
    protected String levelName;
    protected final Random random;
    protected TmxMap map;
    private State state = State.CLEAN;

    protected abstract IAnimationCollection getTileAnimationCollection(TmxTileInstance tile);

    private State clean(NedGame game) {
        game.setNed(null);
        mainMenu.disable();
        ingameControls.enable();
        inGameUI.enable();
        ArrayList<Entity> entitiesToDelete = new ArrayList<Entity>();
        for (Entity e : game.getManager(GroupManager.class).getEntities(Group.LEVEL)) {
            entitiesToDelete.add(e);
        }

        for (Entity e : entitiesToDelete) {
            e.deleteFromWorld();
        }
        return State.LOAD;
    }

    protected abstract State load(NedGame game);

    private State build(NedGame game) {
        GroupManager groupManager = game.getManager(GroupManager.class);
        Entity level = game.createEntity();
        level.addComponent(new LevelBackground(assets.getAnimations("background.png").getFirst().start(PlayMode.ONCE)));
        groupManager.add(level, Group.LEVEL);
        game.addEntity(level);

        createProjector(game);
        final List<TmxLayer> layers = map.getLayers();
        this.index.initialize(map.getHeight(), map.getWidth());
        for (int l = 0, n = layers.size(); l < n; ++l) {
            TmxLayer layer = map.getLayers().get(l);
            for (int y = 0, lh = layer.getHeight(); y < lh; ++y) {
                for (int x = 0, lw = layer.getWidth(); x < lw; ++x) {
                    final TmxTileInstance tile = layer.getTileAt(x, y);
                    if (null != tile) {
                        Entity entity = createEntityFromTile(tile, game, x, y, l, layer);
                        groupManager.add(entity, Group.LEVEL);
                    }
                }
            }
        }
        assets.clearUseless();
        return State.END;
    }

    private enum ApparitionEffect {

        FROM_ABOVE,
        FROM_BELOW,
        NONE
    }

    protected enum State {

        CLEAN,
        LOAD,
        BUILD,
        END
    }

    protected AbstractStartGameTrigger(IAssets assets, @NamedEntities.MainMenu Entity mainMenu, @NamedEntities.InGameUI Entity inGameUI, @NamedEntities.IngameControls Entity ingameControls, LevelIndex indexSystem, Random random) {
        this.assets = assets;
        this.mainMenu = mainMenu;
        this.inGameUI = inGameUI;
        this.ingameControls = ingameControls;
        this.index = indexSystem;
        this.random = random;
    }

    @Override
    public IStartGameTrigger withLevelName(String levelName) {
        this.levelName = levelName;
        return this;
    }

    @Override
    public void process(NedGame game) {
        switch (state) {
            case CLEAN:
                state = clean(game);
                break;
            case LOAD:
                state = load(game);
                break;
            case BUILD:
                state = build(game);
                break;
            case END:
                break;
        }
    }

    protected Vector3 tileToPos(int y, int x, int layer) {
        return new Vector3(x, y, layer);
    }

    protected Entity createEntityFromTile(final TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        String type = tile.getTile().getProperty("type", "decoration");
        if ("ned".equals(type)) {
            return createNed(tile, game, x, y, l, layer);
        }
        if ("green_maki".equals(type)) {
            return createGreenMaki(tile, game, x, y, l, layer);
        }
        if ("orange_maki".equals(type)) {
            return createOrangeMaki(tile, game, x, y, l, layer);
        }
        if ("blue_maki".equals(type)) {
            return createBlueMaki(tile, game, x, y, l, layer);
        }
        if ("green_maki_on_plate".equals(type)) {
            return createGreenMakiOnPlate(tile, game, x, y, l, layer);
        }
        if ("orange_maki_on_plate".equals(type)) {
            return createOrangeMakiOnPlate(tile, game, x, y, l, layer);
        }
        if ("blue_maki_on_plate".equals(type)) {
            return createBlueMakiOnPlate(tile, game, x, y, l, layer);
        }
        if ("box".equals(type)) {
            return createBox(tile, game, x, y, l, layer);
        }
        if ("rooted_box".equals(type)) {
            return createRootedBox(tile, game, x, y, l, layer);
        }
        if ("wall".equals(type)) {
            return createWall(tile, game, x, y, l, layer);
        }
        if ("green_plate".equals(type)) {
            return createPlate(tile, game, x, y, l, layer, ColorType.green, false);
        }
        if ("orange_plate".equals(type)) {
            return createPlate(tile, game, x, y, l, layer, ColorType.orange, false);
        }
        if ("blue_plate".equals(type)) {
            return createPlate(tile, game, x, y, l, layer, ColorType.blue, false);
        }
        if ("dark_stairs_close_up".equals(type)) {
            return createStairs(tile, game, x, y, l, layer, Position.getUp());
        }
        if ("dark_stairs_close_down".equals(type)) {
            return createStairs(tile, game, x, y, l, layer, Position.getDown());
        }
        if ("dark_stairs_close_left".equals(type)) {
            return createStairs(tile, game, x, y, l, layer, Position.getLeft());
        }
        if ("dark_stairs_close_right".equals(type)) {
            return createStairs(tile, game, x, y, l, layer, Position.getRight());
        }
        if ("low_stairs".equals(type)) {
            return createLowStairs(tile, game, x, y, l, layer);
        }
        if ("green_stele_out".equals(type)) {
            return createStone(tile, game, x, y, l, layer, ColorType.green, true);
        }
        if ("orange_stele_out".equals(type)) {
            return createStone(tile, game, x, y, l, layer, ColorType.orange, true);
        }
        if ("blue_stele_out".equals(type)) {
            return createStone(tile, game, x, y, l, layer, ColorType.blue, true);
        }
        return createDecoration(tile, game, x, y, l, layer);
    }

    protected Entity createDecoration(final TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity decoration = game.createEntity();
        createSprite(x, y, l, tile, ApparitionEffect.FROM_BELOW, decoration);
        game.addEntity(decoration);
        return decoration;
    }

    private Sprite createSprite(int x, int y, int l, final TmxTileInstance tile, ApparitionEffect apparitionEffect, Entity entity) {
        Sprite sprite = new Sprite();
        final Vector3 pos = tileToPos(x, y, l);
        Vector3 apparitionPos = new Vector3(pos);
        switch (apparitionEffect) {
            case FROM_ABOVE:
                apparitionPos.addLocal(0, 0, (1.0f + random.nextFloat()) * VirtualResolution.HEIGHT);
                break;
            case FROM_BELOW:
                apparitionPos.addLocal((2.0f * random.nextFloat() - 1.0f) * map.getWidth(), (2.0f * random.nextFloat() - 1.0f) * map.getWidth(), (1.0f + random.nextFloat() * 2.0f) * -VirtualResolution.HEIGHT);
                break;
            case NONE:
            default:
                break;
        }
        sprite.setPosition(apparitionPos);
        sprite.setWidth(tile.getTile().getFrame().getX2() - tile.getTile().getFrame().getX1());
        sprite.setHeight(tile.getTile().getFrame().getY2() - tile.getTile().getFrame().getY1());
        sprite.setPlay(getTileAnimationCollection(tile).getFirst().start(PlayMode.LOOP));
        final EnumSet<TmxTileInstanceEffect> effect = tile.getEffect();
        sprite.setMirrorX(effect.contains(TmxTileInstanceEffect.FLIPPED_HORIZONTALLY));
        sprite.setMirrorX(effect.contains(TmxTileInstanceEffect.FLIPPED_VERTICALLY));
        entity.addComponent(sprite);
        entity.addComponent(new SpritePuppetControls(sprite).moveTo(pos, 2.0f));
        return sprite;
    }

    protected Entity createNed(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity ned = game.createEntity();
        Ned obj = new Ned(new PositionIndexed(x, y, index), ned, index, assets);
        ned.addComponent(obj);
        game.setNed(ned);
        createSprite(x, y, l, tile, ApparitionEffect.NONE, ned);
        game.addEntity(ned);
        index.added(obj, new Position(x, y));
        index.setNed(ned);
        return ned;
    }

    protected Entity createGreenMaki(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        GreenMaki obj = new GreenMaki(new PositionIndexed(x, y, index), maki, index, assets);
        maki.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(obj, new Position(x, y));
        return maki;
    }

    protected Entity createOrangeMaki(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        OrangeMaki obj = new OrangeMaki(new PositionIndexed(x, y, index), maki, index, assets);
        maki.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(obj, new Position(x, y));
        return maki;
    }

    protected Entity createBlueMaki(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        BlueMaki obj = new BlueMaki(new PositionIndexed(x, y, index), maki, index, assets);
        maki.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(obj, new Position(x, y));
        return maki;
    }

    protected Entity createGreenMakiOnPlate(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        GreenMaki obj = new GreenMaki(new PositionIndexed(x, y, index), maki, index, assets);
        obj.setValidate(true);
        maki.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(obj, new Position(x, y));
        Entity plate = createPlate(tile, game, x, y, l, layer, ColorType.green, true);
        if (plate != null) {
            game.getManager(GroupManager.class).add(plate, Group.LEVEL);
        }
        return maki;
    }

    protected Entity createOrangeMakiOnPlate(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        OrangeMaki obj = new OrangeMaki(new PositionIndexed(x, y, index), maki, index, assets);
        obj.setValidate(true);
        maki.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(obj, new Position(x, y));
        Entity plate = createPlate(tile, game, x, y, l, layer, ColorType.orange, true);
        if (plate != null) {
            game.getManager(GroupManager.class).add(plate, Group.LEVEL);
        }
        return maki;
    }

    protected Entity createBlueMakiOnPlate(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        BlueMaki obj = new BlueMaki(new PositionIndexed(x, y, index), maki, index, assets);
        obj.setValidate(true);
        maki.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(obj, new Position(x, y));
        Entity plate = createPlate(tile, game, x, y, l, layer, ColorType.blue, true);
        if (plate != null) {
            game.getManager(GroupManager.class).add(plate, Group.LEVEL);
        }
        return maki;
    }

    protected Entity createBox(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity box = game.createEntity();
        Box obj = new Box(new PositionIndexed(x, y, index), box, index, assets);
        box.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, box);
        game.addEntity(box);
        index.added(obj, new Position(x, y));
        return box;
    }

    protected Entity createRootedBox(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity box = game.createEntity();
        RootBox obj = new RootBox(new PositionIndexed(x, y, index), box, index, assets);
        box.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, box);
        game.addEntity(box);
        index.added(obj, new Position(x, y));
        return box;
    }

    protected Entity createWall(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity wall = game.createEntity();
        Wall obj = new Wall(new PositionIndexed(x, y, index), wall, index);
        wall.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_BELOW, wall);
        game.addEntity(wall);
        index.added(obj, new Position(x, y));
        return wall;
    }

    protected Entity createPlate(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer, ColorType color, boolean maki) {
        Square s = this.index.getSquare(x, y);
        if (s != null) {
            Plate plate = s.getPlate();
            if (plate != null) {
                plate.setMaki(true);
                return null;
            }
        }

        Entity plateEntity = game.createEntity();
        Plate plate = new Plate(false, color);
        index.addPlate(plate, new Position(x, y));
        game.addEntity(plateEntity);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, plateEntity);
        return plateEntity;
    }

    protected Entity createStairs(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer, Position dir) {
        Entity stairs = game.createEntity();
        Stairs obj = new Stairs(new PositionIndexed(x, y, index), stairs, index, assets, false, dir);
        stairs.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.NONE, stairs);
        game.addEntity(stairs);
        index.added(obj, new Position(x, y));
        index.setStairs(obj);
        return stairs;
    }

    protected Entity createLowStairs(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        return this.createWall(tile, game, x, y, l, layer);
    }

    protected Entity createStone(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer, ColorType c, boolean pushed) {
        Entity stone = game.createEntity();
        Stone obj = new Stone(new PositionIndexed(x, y, index), stone, index, assets);
        obj.setColorType(c);
        stone.addComponent(obj);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, stone);
        game.addEntity(stone);
        index.addStone(obj, new Position(x, y));
        return stone;
    }

    protected abstract void createProjector(NedGame game);

    @Override
    public float getProgress() {
        switch (state) {
            case CLEAN:
                return 0f;
            case LOAD:
                return 0.1f;
            case BUILD:
                return 0.5f;
            case END:
                return 1f;
            default:
                return 0f;
        }
    }
}
