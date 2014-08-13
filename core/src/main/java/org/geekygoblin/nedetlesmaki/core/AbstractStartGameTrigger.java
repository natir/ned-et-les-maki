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
import java.util.List;
import java.util.Random;
import org.geekygoblin.nedetlesmaki.core.components.LevelBackground;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Plate;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.PositionIndexed;
import org.geekygoblin.nedetlesmaki.core.constants.ColorType;
import org.geekygoblin.nedetlesmaki.core.constants.VirtualResolution;
import org.geekygoblin.nedetlesmaki.core.events.IStartGameTrigger;
import org.geekygoblin.nedetlesmaki.core.utils.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.utils.MoveStory;
import org.geekygoblin.nedetlesmaki.core.utils.Square;
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
    protected final MoveStory moveStory;
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

        this.moveStory.cleanStack();
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
        for (int l = 0, n = layers.size(); l < n; ++l) {
            TmxLayer layer = map.getLayers().get(l);
            this.index.initialize(layer.getHeight(), layer.getWidth());
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

    protected AbstractStartGameTrigger(IAssets assets, @NamedEntities.MainMenu Entity mainMenu, @NamedEntities.InGameUI Entity inGameUI, @NamedEntities.IngameControls Entity ingameControls, LevelIndex indexSystem, Random random, MoveStory moveStory) {
        this.assets = assets;
        this.mainMenu = mainMenu;
        this.inGameUI = inGameUI;
        this.ingameControls = ingameControls;
        this.index = indexSystem;
        this.random = random;
        this.moveStory = moveStory;
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
        if ("stairs_close_up".equals(type)) {
            return createStairs(tile, game, x, y, l, layer, 1);
        }
        if ("stairs_close_down".equals(type)) {
            return createStairs(tile, game, x, y, l, layer, 2);
        }
        if ("stairs_close_left".equals(type)) {
            return createStairs(tile, game, x, y, l, layer, 3);
        }
        if ("stairs_close_right".equals(type)) {
            return createStairs(tile, game, x, y, l, layer, 4);
        }
        if ("low_stairs".equals(type)) {
            return createLowStairs(tile, game, x, y, l, layer);
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
        ned.addComponent(new PositionIndexed(x, y, index));
        game.setNed(ned);
        createSprite(x, y, l, tile, ApparitionEffect.NONE, ned);
        game.addEntity(ned);
        index.added(ned, new Position(x, y));
        return ned;
    }

    protected Entity createGreenMaki(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        maki.addComponent(new PositionIndexed(x, y, index));
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(maki, new Position(x, y));
        return maki;
    }

    protected Entity createOrangeMaki(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        maki.addComponent(new PositionIndexed(x, y, index));
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(maki, new Position(x, y));
        return maki;
    }

    protected Entity createBlueMaki(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        maki.addComponent(new PositionIndexed(x, y, index));
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(maki, new Position(x, y));
        return maki;
    }

    protected Entity createGreenMakiOnPlate(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        maki.addComponent(new PositionIndexed(x, y, index));
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(maki, new Position(x, y));
        Entity plate = createPlate(tile, game, x, y, l, layer, ColorType.green, true);
        if (plate != null) {
            game.getManager(GroupManager.class).add(plate, Group.LEVEL);
        }
        return maki;
    }

    protected Entity createOrangeMakiOnPlate(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        maki.addComponent(new PositionIndexed(x, y, index));
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(maki, new Position(x, y));
        Entity plate = createPlate(tile, game, x, y, l, layer, ColorType.orange, true);
        if (plate != null) {
            game.getManager(GroupManager.class).add(plate, Group.LEVEL);
        }
        return maki;
    }

    protected Entity createBlueMakiOnPlate(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
        maki.addComponent(new PositionIndexed(x, y, index));
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        index.added(maki, new Position(x, y));
        Entity plate = createPlate(tile, game, x, y, l, layer, ColorType.blue, true);
        if (plate != null) {
            game.getManager(GroupManager.class).add(plate, Group.LEVEL);
        }
        return maki;
    }

    protected Entity createBox(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity box = game.createEntity();
        box.addComponent(new PositionIndexed(x, y, index));
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, box);
        game.addEntity(box);
        index.added(box, new Position(x, y));
        return box;
    }

    protected Entity createRootedBox(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity box = game.createEntity();
        box.addComponent(new PositionIndexed(x, y, index));
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, box);
        game.addEntity(box);
        index.added(box, new Position(x, y));
        return box;
    }

    protected Entity createWall(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        Entity wall = game.createEntity();
        wall.addComponent(new PositionIndexed(x, y, index));
        createSprite(x, y, l, tile, ApparitionEffect.FROM_BELOW, wall);
        game.addEntity(wall);
        index.added(wall, new Position(x, y));
        return wall;
    }

    protected Entity createPlate(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer, ColorType color, boolean maki) {
        Square s = this.index.getSquare(x, y);
        if (s != null) {
            Entity plate = s.getPlate();
            if (plate != null) {
                plate.getComponent(Plate.class).setMaki(true);
                return null;
            }
        }

        Entity plate = game.createEntity();
        plate.addComponent(new Position(x, y));
        index.addPlate(plate, new Position(x, y));
        game.addEntity(plate);
        game.getManager(GroupManager.class).add(plate, Group.PLATE);
        createSprite(x, y, l, tile, ApparitionEffect.FROM_ABOVE, plate);
        return plate;
    }

    protected Entity createStairs(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer, int dir) {
        Entity stairs = game.createEntity();
        stairs.addComponent(new PositionIndexed(x, y, index));
        createSprite(x, y, l, tile, ApparitionEffect.NONE, stairs);
        game.addEntity(stairs);
        index.added(stairs, new Position(x, y));
        game.getManager(GroupManager.class).add(stairs, Group.STAIRS);
        return stairs;
    }

    protected Entity createLowStairs(TmxTileInstance tile, NedGame game, int x, int y, int l, TmxLayer layer) {
        return this.createWall(tile, game, x, y, l, layer);
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
