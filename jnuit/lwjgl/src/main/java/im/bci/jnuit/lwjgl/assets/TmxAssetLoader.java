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

import im.bci.tmxloader.TmxLoader;
import im.bci.tmxloader.TmxMap;
import im.bci.tmxloader.TmxTileset;

/**
 *
 * @author devnewton
 */
public class TmxAssetLoader extends TmxLoader {

    private final IAssets assets;
    private String parentDir;

    public TmxAssetLoader(IAssets assets) {
        this.assets = assets;
    }

    public TmxAsset loadTmx(String name) {
        parentDir = name.substring(0, name.lastIndexOf('/') + 1);
        TmxMap map = load(assets.getText(name));
        adjustImagesSource(map);
        return new TmxAsset(assets, map);
    }

    @Override
    protected String openExternalTileset(String source) {
        return assets.getText(parentDir + source);

    }
    /* public static void main(String[] args) throws Exception {
     TmxFileLoader f = new TmxFileLoader();
     TmxMap lol = f.load(new File("/home/tralala/dev/ned-et-les-maki/game/data/levels/test.tmx"));
     System.out.print(lol);
     }*/

    private void adjustImagesSource(TmxMap map) {
        for (TmxTileset tileset : map.getTilesets()) {
            tileset.getImage().setSource(parentDir + tileset.getImage().getSource());
        }
    }
}
