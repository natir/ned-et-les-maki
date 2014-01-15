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
package org.geekygoblin.nedetlesmaki.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import im.bci.jnuit.NuitTranslator;
import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.lwjgl.LwjglNuitRenderer;
import im.bci.jnuit.lwjgl.TrueTypeFont;
import im.bci.nanim.IAnimationImage;
import org.geekygoblin.nedetlesmaki.game.assets.IAssets;

/**
 *
 * @author devnewton
 */
@Singleton
public class NedNuitRenderer extends LwjglNuitRenderer {

    private final IAssets assets;

    @Inject
    public NedNuitRenderer(NuitTranslator translator, @NamedEntities.DefaultFont TrueTypeFont font, IAssets assets) {
        super(translator, font);
        this.assets = assets;
    }

    @Override
    protected int getBackgroundTextureId(TexturedBackground background) {
        Object texture = background.getTexture();
        if (texture instanceof String) {
            return assets.getTexture((String) texture).getId();
        } else if (texture instanceof IAnimationImage) {
            IAnimationImage image = (IAnimationImage) texture;
            return image.getId();
        } else {
            throw new RuntimeException("Unknow texture background type: " + background.getClass().getName());
        }
    }

}
