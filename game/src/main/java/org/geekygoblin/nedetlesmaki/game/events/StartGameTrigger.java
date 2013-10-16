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
import im.bci.nanim.IAnimationCollection;
import im.bci.nanim.NanimationCollection;
import im.bci.nanim.PlayMode;
import im.bci.tmxloader.TmxLayer;
import im.bci.tmxloader.TmxMap;
import im.bci.tmxloader.TmxTileInstance;
import im.bci.tmxloader.TmxTileInstanceEffect;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.assets.Texture;
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
        for(int l = 0, n = layers.size(); l<n; ++l) {
            TmxLayer layer = tmx.getLayers().get(l);
            for(int x=0, lw=layer.getWidth(); x<lw; ++x) {
                for(int y=0, lh=layer.getHeight(); y<lh; ++y) {
                    final TmxTileInstance tile = layer.getTileAt(x, y);
                    final EnumSet<TmxTileInstanceEffect> effect = tile.getEffect();
                    Entity decoration = game.createEntity();
                    Sprite sprite = new Sprite();
                    sprite.setPosition(new Vector3f(x, y, l));
                    sprite.setWidth(tmx.getMap().getTilewidth());
                    sprite.setHeight(tmx.getMap().getTileheight());
                    sprite.setPlay(tmx.getTileAnimationCollection(tile).getFirst().start(PlayMode.LOOP));
                    sprite.setMirrorX(effect.contains(TmxTileInstanceEffect.FLIPPED_HORIZONTALLY));
                    sprite.setMirrorX(layer.getTileAt(x, y).getEffect().contains(TmxTileInstanceEffect.FLIPPED_VERTICALLY));
                    decoration.addComponent(sprite);
                    game.addEntity(decoration);
                }                
            }
        }
    }
}
