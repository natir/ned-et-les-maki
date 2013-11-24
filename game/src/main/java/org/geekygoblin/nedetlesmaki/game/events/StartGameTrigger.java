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
import im.bci.nanim.PlayMode;
import im.bci.tmxloader.TmxLayer;
import im.bci.tmxloader.TmxTileInstance;
import im.bci.tmxloader.TmxTileInstanceEffect;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.Group;
import org.geekygoblin.nedetlesmaki.game.NamedEntities;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.assets.TmxAsset;
import org.geekygoblin.nedetlesmaki.game.components.Level;
import org.geekygoblin.nedetlesmaki.game.components.ZOrder;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Boostable;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Movable;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Pusher;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Pushable;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Rooted;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Color;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Square;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.BlockOnPlate;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.geekygoblin.nedetlesmaki.game.components.visual.SpritePuppetControls;
import org.geekygoblin.nedetlesmaki.game.constants.ZOrders;
import org.geekygoblin.nedetlesmaki.game.constants.ColorType;
import org.geekygoblin.nedetlesmaki.game.systems.DrawSystem;
import org.geekygoblin.nedetlesmaki.game.systems.SpriteProjector;
import org.geekygoblin.nedetlesmaki.game.manager.EntityIndexManager;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 */
public class StartGameTrigger extends Trigger {

    private final Assets assets;
    private final Entity mainMenu;
    private final Entity ingameControls;
    private final EntityIndexManager indexSystem;
    private String levelName;
    private final Random random;

    @Inject
    public StartGameTrigger(Assets assets, @NamedEntities.MainMenu Entity mainMenu, @NamedEntities.IngameControls Entity ingameControls, EntityIndexManager indexSystem, Random random) {
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

        ArrayList<Entity> entitiesToDelete = new ArrayList<>();
        for (Entity e : game.getManager(GroupManager.class).getEntities(Group.LEVEL)) {
            entitiesToDelete.add(e);
        }
	this.indexSystem.cleanStack();

        Entity level = game.createEntity();
        level.addComponent(new Level());
        level.addComponent(new ZOrder(ZOrders.LEVEL));
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

    private Vector3f tileToPos(TmxAsset tmx, int y, int x, int layer) {
        return new Vector3f(x, y, layer);
    }

    private Entity createEntityFromTile(final TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        switch (tile.getTile().getProperty("type", "decoration")) {
	case "ned":
	    return createNed(tile, game, tmx, x, y, l, layer);
	case "green_maki":
	    return createGreenMaki(tile, game, tmx, x, y, l, layer);
	case "orange_maki":
	    return createOrangeMaki(tile, game, tmx, x, y, l, layer);
	case "blue_maki":
	    return createBlueMaki(tile, game, tmx, x, y, l, layer);
	case "green_maki_on_plate":
	    return createGreenMakiOnPlate(tile, game, tmx, x, y, l, layer);
	case "orange_maki_on_plate":
	    return createOrangeMakiOnPlate(tile, game, tmx, x, y, l, layer);
	case "blue_maki_on_plate":
	    return createBlueMakiOnPlate(tile, game, tmx, x, y, l, layer);
	case "box":
	    return createBox(tile, game, tmx, x, y, l, layer);
	case "rooted_box":
	    return createRootedBox(tile, game, tmx, x, y, l, layer);
	case "wall":
	    return createWall(tile, game, tmx, x, y, l, layer);
	case "green_plate":
	    return createPlate(tile, game, tmx, x, y, l, layer, ColorType.green);
	case "orange_plate":
	    return createPlate(tile, game, tmx, x, y, l, layer, ColorType.orange);
	case "blue_plate":
	    return createPlate(tile, game, tmx, x, y, l, layer, ColorType.blue);
	case "decoration":
	default:
	    return createDecoration(tile, game, tmx, x, y, l, layer);
        }

    }

    private Entity createDecoration(final TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity decoration = game.createEntity();
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_BELOW, decoration).setLabel(x + "," + y);
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
        final Vector3f pos = tileToPos(tmx, x, y, l);
        Vector3f apparitionPos = new Vector3f(pos);
        switch(apparitionEffect) {
            case FROM_ABOVE:
                apparitionPos.translate(0, 0, (1.0f + random.nextFloat()) * DrawSystem.SCREEN_HEIGHT);
                break;
            case FROM_BELOW:
                apparitionPos.translate((2.0f * random.nextFloat() - 1.0f) * tmx.getMap().getWidth(), (2.0f * random.nextFloat() - 1.0f) * tmx.getMap().getWidth(), (1.0f + random.nextFloat() * 2.0f) * -DrawSystem.SCREEN_HEIGHT);
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
        entity.addComponent(new ZOrder(ZOrders.LEVEL));
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
	maki.addComponent(new Color(ColorType.orange));
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
        maki.addComponent(new Color(ColorType.blue));
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
	maki.addComponent(new Color(ColorType.green));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
	game.addEntity(maki);
        indexSystem.added(maki);
	this.createPlate(tile, game, tmx, x, y, l, layer, ColorType.green);
        return maki;
    }

    private Entity createOrangeMakiOnPlate(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();

        maki.addComponent(new Position(x, y));
        maki.addComponent(new Movable(15));
        maki.addComponent(new Pushable(true));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        indexSystem.added(maki);
	this.createPlate(tile, game, tmx, x, y, l, layer, ColorType.orange);
        return maki;
    }

    private Entity createBlueMakiOnPlate(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();

        maki.addComponent(new Position(x, y));
        maki.addComponent(new Movable(15));
        maki.addComponent(new Boostable(3));
        maki.addComponent(new Pusher(false));
        maki.addComponent(new Pushable(true));
        maki.addComponent(new Color(ColorType.green));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, maki);
        game.addEntity(maki);
        indexSystem.added(maki);
	this.createPlate(tile, game, tmx, x, y, l, layer, ColorType.blue);
        return maki;
    }

    private Entity createBox(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity box = game.createEntity();

        box.addComponent(new Position(x, y));
        box.addComponent(new Movable(1));
        box.addComponent(new Pushable(true));
        box.addComponent(new Color(ColorType.no));
        box.addComponent(new BlockOnPlate(true));
        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, box);
        game.addEntity(box);
        indexSystem.added(box);
        return box;
    }

    private Entity createRootedBox(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity box = game.createEntity();

        box.addComponent(new Position(x, y));
        box.addComponent(new Movable(1));
        box.addComponent(new Rooted(true));
        box.addComponent(new Pushable(true));
        box.addComponent(new Color(ColorType.no));
        box.addComponent(new BlockOnPlate(true));
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

    private Entity createPlate(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer, ColorType color) {
        Square s = indexSystem.getSquare(x, y);
	
	if(s == null) {
	    s = new Square();
        }
	if(s.getEntity() == null) {
	    s.setEntity(game.createEntity());

	    game.addEntity(s.getEntity());
	    indexSystem.added(s.getEntity());
	}

	s.getPlate().setColor(color);
	s.getPlate().setPlate(true);

        createSprite(tmx, x, y, l, tile, ApparitionEffect.FROM_ABOVE, s.getEntity());

        return s.getEntity();
    }

    private void createProjector(Game game, TmxAsset tmx) {
        final float tileWidth = tmx.getMap().getTilewidth();
        final float tileHeight = tmx.getMap().getTileheight();
        game.getSystem(DrawSystem.class).setSpriteProjector(new IsometricSpriteProjector(tileWidth, tileHeight));
    }

    private static class IsometricSpriteProjector implements SpriteProjector {

        private final float tileWidth;
        private final float tileHeight;

        public IsometricSpriteProjector(float tileWidth, float tileHeight) {
            this.tileWidth = tileWidth;
            this.tileHeight = tileHeight;
        }

        @Override
        public Vector2f project(Vector3f pos) {
            return new Vector2f(-(pos.x - pos.y) * tileWidth / 2.0f, (pos.x + pos.y) * tileHeight / 2.0f - pos.z);
        }

            @Override
            public int compare(Vector3f v1, Vector3f v2) {
                float d1 = v1.x + v1.y + v1.z;
                float d2 = v2.x + v2.y + v2.z;
                if (Math.abs(d1 - d2) < 0.1f) {
                    return Float.compare(d1, d2);
                } else {
                    return 0;
                }
            }
    }
}
