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

import im.bci.lwjgl.nuit.NuitToolkit;
import im.bci.lwjgl.nuit.utils.TrueTypeFont;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;

/**
 *
 * @author devnewton
 */
@Singleton
public class NedToolkit extends NuitToolkit {

    private final Assets assets;
    private static final String[] messagesBundles = new String[]{"messages", "nuit_messages"};

    @Inject
    public NedToolkit(Assets assets) {
        this.assets = assets;
    }

    @Override
    protected TrueTypeFont createFont() {
        return assets.getFont("prout");
    }

    @Override
    public String getMessage(String key) {
        for (String bundleName : messagesBundles) {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        }
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "No translation for {0}", key);
        return key;
    }
}
