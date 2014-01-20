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
package im.bci.jnuit.lwjgl.assets;

import im.bci.jnuit.animation.IAnimationCollection;
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

    private final IAssets assets;
    private final TmxMap map;
    private final HashMap<TmxTileInstance, IAnimationCollection> tileAnimations = new HashMap<>();

    public TmxAsset(IAssets assets, TmxMap map) {
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
        final String textureName = frame.getImage().getSource();
        ITexture texture = assets.getTexture(textureName);
        final float width = texture.getWidth();
        final float height = texture.getHeight();
        final float u1 = frame.getX1() / width;
        final float v1 = frame.getY1() / height;
        final float u2 = frame.getX2() / width;
        final float v2 = frame.getY2() / height;
        return new TextureAnimationCollectionWrapper(assets, textureName, u1, v1, u2, v2);
    }

    public List<TmxLayer> getLayers() {
        return map.getLayers();
    }
}
