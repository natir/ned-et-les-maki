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
package org.geekygoblin.nedetlesmaki.core.components.ui;

import im.bci.jnuit.NuitToolkit;
import javax.inject.Inject;
import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.focus.NullFocusCursor;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import im.bci.jnuit.widgets.Dialogue;

/**
 *
 * @author devnewton
 */
public class NedDialogue extends Dialogue {

    @Inject
    public NedDialogue(NuitToolkit toolkit, IAssets assets) {
        super(toolkit);
        IAnimationCollection dialogAnimations = assets.getAnimations("animation/dialog_ui/dialog_ui.json");

        nextButton.setText("");
        nextButton.setBackground(new TexturedBackground(dialogAnimations.getAnimationByName("next").start(PlayMode.LOOP)));
        nextButton.setFocusedBackground(new TexturedBackground(dialogAnimations.getAnimationByName("next_focused").start(PlayMode.LOOP)));
        nextButton.setFocusCursor(NullFocusCursor.INSTANCE);
       
        previousButton.setText("");
        previousButton.setBackground(new TexturedBackground(dialogAnimations.getAnimationByName("previous").start(PlayMode.LOOP)));
        previousButton.setFocusedBackground(new TexturedBackground(dialogAnimations.getAnimationByName("previous_focused").start(PlayMode.LOOP)));
        previousButton.setFocusCursor(NullFocusCursor.INSTANCE);
    }
}
