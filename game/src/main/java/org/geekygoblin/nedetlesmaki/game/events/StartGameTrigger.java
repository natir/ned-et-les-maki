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
import im.bci.nanim.PlayMode;
import im.bci.tmxloader.TmxLayer;
import im.bci.tmxloader.TmxTileInstance;
import im.bci.tmxloader.TmxTileInstanceEffect;
import java.util.EnumSet;
import java.util.List;
import javax.inject.Inject;
import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.NamedEntities;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.assets.TmxAsset;
import org.geekygoblin.nedetlesmaki.game.components.Level;
import org.geekygoblin.nedetlesmaki.game.components.ZOrder;
import org.geekygoblin.nedetlesmaki.game.components.Position;
import org.geekygoblin.nedetlesmaki.game.components.Boostable;
import org.geekygoblin.nedetlesmaki.game.components.EntityPosIndex;
import org.geekygoblin.nedetlesmaki.game.components.Movable;
import org.geekygoblin.nedetlesmaki.game.components.Pusher;
import org.geekygoblin.nedetlesmaki.game.components.Rooted;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.geekygoblin.nedetlesmaki.game.constants.ZOrders;
import org.geekygoblin.nedetlesmaki.game.systems.UpdateLevelVisualSystem;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 */
public class StartGameTrigger extends Trigger {
    private final Assets assets;
    private final Entity mainMenu;
    private final Entity ingameControls;
    
    @Inject
    public StartGameTrigger(Assets assets, @NamedEntities.MainMenu Entity mainMenu, @NamedEntities.IngameControls Entity ingameControls) {
        this.assets = assets;
        this.mainMenu = mainMenu;
        this.ingameControls = ingameControls;
    }

    @Override
    public void process(Game game) {
        mainMenu.disable();
        ingameControls.enable();

        Entity level = game.createEntity();
        level.addComponent(new Level());
        level.addComponent(new ZOrder(ZOrders.LEVEL));
        game.addEntity(level);

        TmxAsset tmx = assets.getTmx("levels/test.tmx");
        final List<TmxLayer> layers = tmx.getLayers();
        for (int l = 0, n = layers.size(); l < n; ++l) {
            TmxLayer layer = tmx.getLayers().get(l);
            for (int x = 0, lw = layer.getWidth(); x < lw; ++x) {
                for (int y = 0, lh = layer.getHeight(); y < lh; ++y) {
                    final TmxTileInstance tile = layer.getTileAt(x, y);
                    if (null != tile) {
                        createEntityFromTile(tile, game, tmx, x, y, l, layer);
                    }
                }
            }
        }
    }

    private Vector3f tileToScreenPos(TmxAsset tmx, int y, int x, int layer) {
        float tileWidth = tmx.getMap().getTilewidth();
        float tileHeight = tmx.getMap().getTileheight();
        float originX = tmx.getMap().getHeight() * tileWidth / 2;
        return new Vector3f((x - y) * tileWidth / 2 + originX, (x + y) * tileHeight / 2, layer);
    }

    private void createEntityFromTile(final TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        switch (tile.getTile().getProperty("type", "decoration")) {
	case "ned":
	    createNed(tile, game, tmx, x, y, l, layer);
	    break;
	case "green_maki":
	    createGreenMaki(tile, game, tmx, x, y, l, layer);
	    break;
	case "orange_maki":
	    createOrangeMaki(tile, game, tmx, x, y, l, layer);
	    break;
	case "blue_maki":
	    createBlueMaki(tile, game, tmx, x, y, l, layer);
	    break;
	case "box":
	    createBox(tile, game, tmx, x, y, l, layer);
	    break;
	case "rooted_box":
	    createRootedBox(tile, game, tmx, x, y, l, layer);
	    break;
	case "decoration":
	default:
	    createDecoration(tile, game, tmx, x, y, l, layer);
	break;
        }

    }

    private void createDecoration(final TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity decoration = game.createEntity();
        createSprite(tmx, x, y, l, tile, layer, decoration);
        game.addEntity(decoration);
    }

    private void createSprite(TmxAsset tmx, int x, int y, int l, final TmxTileInstance tile, TmxLayer layer, Entity decoration) {
        Sprite sprite = new Sprite();
        sprite.setPosition(tileToScreenPos(tmx, x, y, l));
        sprite.setWidth(tile.getTile().getFrame().getX2() - tile.getTile().getFrame().getX1());
        sprite.setHeight(tile.getTile().getFrame().getY2() - tile.getTile().getFrame().getY1());
        sprite.setPlay(tmx.getTileAnimationCollection(tile).getFirst().start(PlayMode.LOOP));
        final EnumSet<TmxTileInstanceEffect> effect = tile.getEffect();
        sprite.setMirrorX(effect.contains(TmxTileInstanceEffect.FLIPPED_HORIZONTALLY));
        sprite.setMirrorX(effect.contains(TmxTileInstanceEffect.FLIPPED_VERTICALLY));
        decoration.addComponent(sprite);
        decoration.addComponent(new ZOrder(ZOrders.LEVEL));
    }

    private void createNed(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity ned = game.createEntity();
	EntityPosIndex index = game.getEntityPosIndex();
	ned.addComponent(new Position(x-2, y-2));
	ned.addComponent(new Pusher(true));
	ned.addComponent(new Movable(1));
        game.setNed(ned);
        createSprite(tmx, x, y, l, tile, layer, ned);
        game.addEntity(ned);
	index.setEntityWithPos(ned.getComponent(Position.class).getX(), ned.getComponent(Position.class).getY(), ned);
    }

    private void createGreenMaki(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
	EntityPosIndex index = game.getEntityPosIndex();
	maki.addComponent(new Position(x-2, y-2));
	maki.addComponent(new Movable(1));
        createSprite(tmx, x, y, l, tile, layer, maki);
        game.addEntity(maki);
	index.setEntityWithPos(maki.getComponent(Position.class).getX(), maki.getComponent(Position.class).getY(), maki);
    }

    private void createOrangeMaki(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
	EntityPosIndex index = game.getEntityPosIndex();
	maki.addComponent(new Position(x-2, y-2));
	maki.addComponent(new Movable(15));
        createSprite(tmx, x, y, l, tile, layer, maki);
        game.addEntity(maki);
	index.setEntityWithPos(maki.getComponent(Position.class).getX(), maki.getComponent(Position.class).getY(), maki);
    }

    private void createBlueMaki(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity maki = game.createEntity();
	EntityPosIndex index = game.getEntityPosIndex();
	maki.addComponent(new Position(x-2, y-2));
	maki.addComponent(new Movable(15));
	maki.addComponent(new Boostable(3));
	maki.addComponent(new Pusher(false));
        createSprite(tmx, x, y, l, tile, layer, maki);
        game.addEntity(maki);
	index.setEntityWithPos(maki.getComponent(Position.class).getX(), maki.getComponent(Position.class).getY(), maki);
    }

    private void createBox(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity box = game.createEntity();
	EntityPosIndex index = game.getEntityPosIndex();
	box.addComponent(new Position(x-2, y-2));
	box.addComponent(new Movable(1));
        createSprite(tmx, x, y, l, tile, layer, box);
        game.addEntity(box);
	index.setEntityWithPos(box.getComponent(Position.class).getX(), box.getComponent(Position.class).getY(), box);
    }
    
    private void createRootedBox(TmxTileInstance tile, Game game, TmxAsset tmx, int x, int y, int l, TmxLayer layer) {
        Entity box = game.createEntity();
	EntityPosIndex index = game.getEntityPosIndex();
	box.addComponent(new Position(x-2, y-2));
	box.addComponent(new Movable(1));
	box.addComponent(new Rooted(true));
        createSprite(tmx, x, y, l, tile, layer, box);
        game.addEntity(box);
	index.setEntityWithPos(box.getComponent(Position.class).getX(), box.getComponent(Position.class).getY(), box);
    }

}
