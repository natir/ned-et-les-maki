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
package org.geekygoblin.nedetlesmaki.game.events;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import im.bci.jnuit.animation.PlayMode;
import im.bci.tmxloader.TmxLayer;
import im.bci.tmxloader.TmxTileInstance;
import im.bci.tmxloader.TmxTileInstanceEffect;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import com.google.inject.Inject;
import im.bci.jnuit.artemis.sprite.IsometricSpriteProjector;
import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.Group;
import org.geekygoblin.nedetlesmaki.game.NamedEntities;
import im.bci.jnuit.lwjgl.assets.IAssets;
import im.bci.jnuit.lwjgl.assets.TmxAsset;
import org.geekygoblin.nedetlesmaki.game.components.LevelBackground;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Boostable;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Movable;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Pusher;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Pushable;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Rooted;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Color;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.StopOnPlate;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.BlockOnPlate;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Destroyable;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Destroyer;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Plate;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Stairs;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Square;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.CatchNed;
import im.bci.jnuit.artemis.sprite.Sprite;
import im.bci.jnuit.artemis.sprite.SpritePuppetControls;
import org.geekygoblin.nedetlesmaki.game.constants.ColorType;
import org.geekygoblin.nedetlesmaki.game.constants.VirtualResolution;
import org.geekygoblin.nedetlesmaki.game.systems.DrawSystem;
import org.geekygoblin.nedetlesmaki.game.manager.EntityIndexManager;
import pythagoras.f.Vector3;

/**
 *
 * @author devnewton natir
 */
public class StartGameTrigger extends Trigger {

    private final IAssets assets;
    private final Entity mainMenu;
    private final Entity ingameControls;
    private final EntityIndexManager indexSystem;
    private String levelName;
    private final Random random;

    @Inject
    public StartGameTrigger(IAssets assets, @NamedEntities.MainMenu Entity mainMenu, @NamedEntities.IngameControls Entity ingameControls, EntityIndexManager indexSystem, Random random) {
        this.assets = assets;
        this.mainMenu = mainMenu;
        this.ingameControls = ingameControls;
        this.indexSystem = indexSystem;
        this.random = random;
    }

    public StartGameTrigger withLevelName(String levelName) {
        this.levelName = levelName;
        return this;
    }

    @Override
    public void process(Game game) {
        mainMenu.disable();
        ingameControls.enable();

        final GroupManager groupManager = game.getManager(GroupManager.class);

        ArrayList<Entity> entitiesToDelete = new ArrayList<Entity>();
        for (Entity e : game.getManager(GroupManager.class).getEntities(Group.LEVEL)) {
            entitiesToDelete.add(e);
        }
        this.indexSystem.cleanIndex();
        this.indexSystem.cleanStack();

        Entity level = game.createEntity();
        level.addComponent(new LevelBackground(assets.getAnimations("background.png").getFirst().start(PlayMode.ONCE)));
        groupManager.add(level, Group.LEVEL);
        game.addEntity(level);

        TmxAsset tmx = assets.getTmx(levelName);
        createProjector(game, tmx);
        final List<TmxLayer> layers = tmx.getLayers();
        for (int l = 0, n = layers.size(); l < n; ++l) {
            TmxLayer layer = tmx.getLayers().get(l);
            for (int y = 0, lh = layer.getHeight(); y < lh; ++y) {
                for (int x = 0, lw = layer.getWidth(); x < lw; ++x) {
                    final TmxTileInstance tile = layer.getTileAt(x, y);
                    if (null != tile) {
                        Entity entity = createEntityFromTile(tile, game, tmx, x, y, l, layer);
                        groupManager.add(entity, Group.LEVEL);
                    }
                }
            }
        }

        for (Entity e : entitiesToDelete) {
            e.deleteFromWorld();
        }
        assets.clearUseless();
    }

    private Vector3 tileToPos(TmxAsset tmx, int y, int x, int layer) {
        return new Vector3(x, y, layer);
    }

    private Entity createEntityFromTile(final TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        String type = tile.getTile().getProperty("type", "decoration");
        if ("ned".equals(type)) {
            return createNed(tile, game, tmx, x, y, l, layer);
        }
        if ("green_maki".equals(type)) {
            return createGreenMaki(tile, game, tmx, x, y, l, layer);
        }
        if ("orange_maki".equals(type)) {
            return createOrangeMaki(tile, game, tmx, x, y, l, layer);
        }
        if ("blue_maki".equals(type)) {
            return createBlueMaki(tile, game, tmx, x, y, l, layer);
        }
        if ("green_maki_on_plate".equals(type)) {
            return createGreenMakiOnPlate(tile, game, tmx, x, y, l, layer);
        }
        if ("orange_maki_on_plate".equals(type)) {
            return createOrangeMakiOnPlate(tile, game, tmx, x, y, l, layer);
        }
        if ("blue_maki_on_plate".equals(type)) {
            return createBlueMakiOnPlate(tile, game, tmx, x, y, l, layer);
        }
        if ("box".equals(type)) {
            return createBox(tile, game, tmx, x, y, l, layer);
        }
        if ("rooted_box".equals(type)) {
            return createRootedBox(tile, game, tmx, x, y, l, layer);
        }
        if ("wall".equals(type)) {
            return createWall(tile, game, tmx, x, y, l, layer);
        }
        if ("green_plate".equals(type)) {
            return createPlate(tile, game, tmx, x, y, l, layer, ColorType.green, false);
        }
        if ("orange_plate".equals(type)) {
            return createPlate(tile, game, tmx, x, y, l, layer, ColorType.orange, false);
        }
        if ("blue_plate".equals(type)) {
            return createPlate(tile, game, tmx, x, y, l, layer, ColorType.blue, false);
        }
        if ("stairs_close_up".equals(type)) {
            return createStairs(tile, game, tmx, x, y, l, layer, 1);
        }
        if ("stairs_close_down".equals(type)) {
            return createStairs(tile, game, tmx, x, y, l, layer, 2);
        }
        if ("stairs_close_left".equals(type)) {
            return createStairs(tile, game, tmx, x, y, l, layer, 3);
        }
        if ("stairs_close_right".equals(type)) {
            return createStairs(tile, game, tmx, x, y, l, layer, 4);
        }
        if ("low_stairs".equals(type)) {
            return createLowStairs(tile, game, tmx, x, y, l, layer);
        }
        return createDecoration(tile, game, tmx, x, y, l, layer);

    }

    private Entity createDecoration(final TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity decoration = game.createEntity();
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_BELOW, decoration);
        game.addEntity(decoration);
        return decoration;
    }

    private enum ApparitionEffect {

        FROM_ABOVE,
        FROM_BELOW,
        NONE
    }

    private Sprite createSprite(TmxAsset tmx, int x, int y, int l, final TmxTileInstance tile, ApparitionEffect apparitionEffect, Entity entity) {
        Sprite sprite = new Sprite();
        final Vector3 pos = tileToPos(tmx, x, y, l);
        Vector3 apparitionPos = new Vector3(pos);
        switch (apparitionEffect) {
            case FROM_ABOVE:
                apparitionPos.addLocal(0, 0, (1.0f + random.nextFloat()) * VirtualResolution.HEIGHT);
                break;
            case FROM_BELOW:
                apparitionPos.addLocal((2.0f * random.nextFloat() - 1.0f) * tmx.getMap().getWidth(), (2.0f * random.nextFloat() - 1.0f) * tmx.getMap().getWidth(), (1.0f + random.nextFloat() * 2.0f) * -VirtualResolution.HEIGHT);
                break;
            case NONE:
            default:
                break;
        }
        sprite.setPosition(apparitionPos);
        sprite.setWidth(tile.getTile().getFrame().getX2() - tile.getTile().getFrame().getX1());
        sprite.setHeight(tile.getTile().getFrame().getY2() - tile.getTile().getFrame().getY1());
        sprite.setPlay(tmx.getTileAnimationCollection(tile).getFirst().start(PlayMode.LOOP));
        final EnumSet<TmxTileInstanceEffect> effect = tile.getEffect();
        sprite.setMirrorX(effect.contains(TmxTileInstanceEffect.FLIPPED_HORIZONTALLY));
        sprite.setMirrorX(effect.contains(TmxTileInstanceEffect.FLIPPED_VERTICALLY));
        entity.addComponent(sprite);
        entity.addComponent(new SpritePuppetControls(sprite).moveTo(pos, 2.0f));
        return sprite;
    }

    private Entity createNed(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity ned = game.createEntity();
        ned.addComponent(new Position(x, y));
        ned.addComponent(new Pusher(true));
        ned.addComponent(new Movable(1));
        game.setNed(ned);
        createSprite(tmx, x, y, l, tile, ApparitionEffect.NONE, ned);
        game.addEntity(ned);
        indexSystem.added(ned);
        return ned;
    }

    private Entity createGreenMaki(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();

        maki.addComponent(new Position(x, y));
        maki.addComponent(new Movable(1));
        maki.addComponent(new Pushable(true));
        maki.addComponent(new StopOnPlate(true));
        maki.addComponent(new Color(ColorType.green));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        indexSystem.added(maki);
        return maki;
    }

    private Entity createOrangeMaki(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();

        maki.addComponent(new Position(x, y));
        maki.addComponent(new Movable(15));
        maki.addComponent(new Pushable(true));
        maki.addComponent(new StopOnPlate(true));
        maki.addComponent(new Color(ColorType.orange));
        maki.addComponent(new CatchNed(true));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        indexSystem.added(maki);
        return maki;
    }

    private Entity createBlueMaki(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();

        maki.addComponent(new Position(x, y));
        maki.addComponent(new Movable(15));
        maki.addComponent(new Boostable(3));
        maki.addComponent(new Pusher(false));
        maki.addComponent(new Pushable(true));
        maki.addComponent(new StopOnPlate(true));
        maki.addComponent(new Color(ColorType.blue));
        maki.addComponent(new Destroyer(true));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        indexSystem.added(maki);
        return maki;
    }

    private Entity createGreenMakiOnPlate(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();

        maki.addComponent(new Position(x, y));
        maki.addComponent(new Movable(1));
        maki.addComponent(new Pushable(true));
        maki.addComponent(new StopOnPlate(true));
        maki.addComponent(new Color(ColorType.green));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        indexSystem.added(maki);

        Entity plate = createPlate(tile, game, tmx, x, y, l, layer, ColorType.green, true);
        if (plate != null) {
            game.getManager(GroupManager.class).add(plate, Group.LEVEL);
        }

        return maki;
    }

    private Entity createOrangeMakiOnPlate(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();

        maki.addComponent(new Position(x, y));
        maki.addComponent(new Movable(15));
        maki.addComponent(new Pushable(true));
        maki.addComponent(new StopOnPlate(true));
        maki.addComponent(new Color(ColorType.orange));
        maki.addComponent(new CatchNed(true));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        indexSystem.added(maki);

        Entity plate = createPlate(tile, game, tmx, x, y, l, layer, ColorType.orange, true);
        if (plate != null) {
            game.getManager(GroupManager.class).add(plate, Group.LEVEL);
        }

        return maki;
    }

    private Entity createBlueMakiOnPlate(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();

        maki.addComponent(new Position(x, y));
        maki.addComponent(new Movable(15));
        maki.addComponent(new Boostable(3));
        maki.addComponent(new Pusher(false));
        maki.addComponent(new Pushable(true));
        maki.addComponent(new StopOnPlate(true));
        maki.addComponent(new Color(ColorType.blue));
        maki.addComponent(new Destroyer(true));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        indexSystem.added(maki);

        Entity plate = createPlate(tile, game, tmx, x, y, l, layer, ColorType.blue, true);
        if (plate != null) {
            game.getManager(GroupManager.class).add(plate, Group.LEVEL);
        }

        return maki;
    }

    private Entity createBox(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity box = game.createEntity();

        box.addComponent(new Position(x, y));
        box.addComponent(new Movable(1));
        box.addComponent(new Pushable(true));
        box.addComponent(new Color(ColorType.no));
        box.addComponent(new BlockOnPlate(true));
        box.addComponent(new Destroyable(true));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, box);
        game.addEntity(box);
        indexSystem.added(box);
        return box;
    }

    private Entity createRootedBox(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity box = game.createEntity();

        box.addComponent(new Position(x, y));
        box.addComponent(new Movable(1));
        box.addComponent(new Rooted(new Position(x, y)));
        box.addComponent(new Pushable(true));
        box.addComponent(new Color(ColorType.no));
        box.addComponent(new BlockOnPlate(true));
        box.addComponent(new Destroyable(true));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, box);
        game.addEntity(box);
        indexSystem.added(box);
        return box;
    }

    private Entity createWall(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity wall = game.createEntity();

        wall.addComponent(new Position(x, y));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_BELOW, wall);
        game.addEntity(wall);
        indexSystem.added(wall);

        return wall;
    }

    private Entity createPlate(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer, ColorType color, boolean maki) {
        Square s = this.indexSystem.getSquare(x, y);
        if (s != null) {
            ArrayList<Entity> plateInSquare = s.getWith(Plate.class);
            if (!plateInSquare.isEmpty()) {
                plateInSquare.get(0).getComponent(Plate.class).setMaki(true);
                return null;
            }
        }

        Entity plate = game.createEntity();
        plate.addComponent(new Color(color));
        plate.addComponent(new Plate(true, maki));
        plate.addComponent(new Position(x, y));

        indexSystem.added(plate);
        game.addEntity(plate);
        game.getManager(GroupManager.class).add(plate, Group.PLATE);

        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, plate);

        return plate;
    }

    private Entity createStairs(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer, int dir) {
        Entity stairs = game.createEntity();

        stairs.addComponent(new Position(x, y));
        stairs.addComponent(new Stairs(false, dir));

        createSprite(tmx, x, y, l, tile, ApparitionEffect.NONE, stairs);

        game.addEntity(stairs);
        indexSystem.added(stairs);
        game.getManager(GroupManager.class).add(stairs, Group.STAIRS);

        return stairs;
    }

    private Entity createLowStairs(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        return this.createWall(tile, game, tmx, x, y, l, layer);
    }

    private void createProjector(Game game, TmxAsset tmx) {
        final float tileWidth = tmx.getMap().getTilewidth();
        final float tileHeight = tmx.getMap().getTileheight();
        game.getSystem(DrawSystem.class).setSpriteProjector(new IsometricSpriteProjector(tileWidth, tileHeight));
    }
}
