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
package org.geekygoblin.nedetlesmaki.game.components.ui;

import com.artemis.Component;
import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.widgets.Root;
import com.google.inject.Inject;
import im.bci.jnuit.NuitRenderer;
import org.geekygoblin.nedetlesmaki.game.IAssets;
import org.geekygoblin.nedetlesmaki.game.Game;

/**
 *
 * @author devnewton
 */
public class DialogComponent extends Component {

    private final Root root;
    private final Game game;
    private final NuitRenderer nuitRenderer;
    private final NedDialogue dialog;

    @Inject
    public DialogComponent(NuitToolkit toolkit, NuitRenderer nuitRenderer, Game game, IAssets assets, NedDialogue dialog) {
        this.game = game;
        root = new Root(toolkit);
        this.nuitRenderer = nuitRenderer;
        root.add(dialog);
        this.dialog = dialog;
    }

    public void update() {
        final float delta = game.getDelta();
        root.update(delta);
    }

    public void draw() {
        nuitRenderer.render(root);
    }

    public boolean isFinished() {
      return dialog.isFinished();
    }
}
