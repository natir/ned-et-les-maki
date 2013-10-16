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
package org.geekygoblin.nedetlesmaki.game.assets;

import im.bci.nanim.IAnimationCollection;
import im.bci.tmxloader.TmxFrame;
import im.bci.tmxloader.TmxLayer;
import im.bci.tmxloader.TmxMap;
import im.bci.tmxloader.TmxTileInstance;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author devnewton
 */
public class TmxAsset {

    private final Assets assets;
    private final TmxMap map;
    private final HashMap<TmxTileInstance, IAnimationCollection> tileAnimations = new HashMap<>();

    public TmxAsset(Assets assets, TmxMap map) {
        this.assets = assets;
        this.map = map;
    }

    public TmxMap getMap() {
        return map;
    }
    
    public IAnimationCollection getTileAnimationCollection(TmxTileInstance tile) {
        IAnimationCollection animationCollection = tileAnimations.get(tile);
        if (null == animationCollection) {
            animationCollection = createAnimationFromTile(tile);
            tileAnimations.put(tile, animationCollection);
        }
        return animationCollection;
    }

    private IAnimationCollection createAnimationFromTile(TmxTileInstance tile) {
        final TmxFrame frame = tile.getTile().getFrame();
        Texture texture = assets.getTexture(frame.getImage().getSource());
        final float width = texture.getWidth();
        final float height = texture.getHeight();
        final float u1 = frame.getX1() / width;
        final float v1 = frame.getY1() / height;
        final float u2 = frame.getX2() / width;
        final float v2 = frame.getY2() / height;
        return new TextureAnimationCollectionWrapper(texture, u1, v1, u2, v2);
    }

    public List<TmxLayer> getLayers() {
        return map.getLayers();
    }
}
