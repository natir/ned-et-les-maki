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

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.background.Background;
import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.widgets.Button;
import im.bci.nanim.IAnimationCollection;
import im.bci.nanim.IAnimationFrame;
import im.bci.nanim.IPlay;
import im.bci.nanim.PlayMode;
import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.assets.IAssets;

/**
 *
 * @author devnewton
 */
public class AnimatedButton extends Button {

    private final IPlay background;
    private final IPlay focusedBackground;
    private final Game game;

    public AnimatedButton(Game game, NuitToolkit toolkit, String text, IAssets assets, String nanimName, String normalAnimation, String focusedAnimationName) {
        super(toolkit, text);
        this.game = game;
        final IAnimationCollection animations = assets.getAnimations(nanimName);
        background = animations.getAnimationByName(normalAnimation).start(PlayMode.LOOP);
        focusedBackground = animations.getAnimationByName(focusedAnimationName).start(PlayMode.LOOP);
        setMustDrawFocus(false);
    }

    @Override
    public void update() {

        long elapsedTime = (long) (game.getDelta() * 1000L);

        background.update(elapsedTime);
        final IAnimationFrame backgroundFrame = background.getCurrentFrame();
        this.setBackground(new TexturedBackground(backgroundFrame.getImage(), backgroundFrame.getU1(), backgroundFrame.getV1(), backgroundFrame.getU2(), backgroundFrame.getV2()));

        focusedBackground.update(elapsedTime);
        final IAnimationFrame focusedFrame = focusedBackground.getCurrentFrame();
        setFocusedBackground(new TexturedBackground(focusedFrame.getImage(), focusedFrame.getU1(), focusedFrame.getV1(), focusedFrame.getU2(), focusedFrame.getV2()));
    }
}
