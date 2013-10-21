/*
 * Copyright (c) 2013 devnewton <devnewton@bci.im>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'devnewton <devnewton@bci.im>' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.geekygoblin.nedetlesmaki.game.events;

import com.artemis.Entity;
import im.bci.nanim.PlayMode;
import im.bci.tmxloader.TmxLayer;
import im.bci.tmxloader.TmxTileInstance;
import im.bci.tmxloader.TmxTileInstanceEffect;
import java.util.EnumSet;
import java.util.List;
import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.assets.TmxAsset;
import org.geekygoblin.nedetlesmaki.game.components.Level;
import org.geekygoblin.nedetlesmaki.game.components.ZOrder;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.geekygoblin.nedetlesmaki.game.constants.ZOrders;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 */
public class StartGameTrigger extends Trigger {

    @Override
    public void process(Game game) {
        game.getMainMenu().disable();
        game.getIngameControls().enable();
        Entity level = game.createEntity();
        level.addComponent(new Level());
        level.addComponent(new ZOrder(ZOrders.LEVEL));
        game.addEntity(level);

        TmxAsset tmx = game.getAssets().getTmx("levels/test.tmx");
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
        game.setNed(ned);
        createSprite(tmx, x, y, l, tile, layer, ned);
        game.addEntity(ned);
    }
}
