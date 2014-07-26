/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

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
package org.geekygoblin.nedetlesmaki.playn.core;

import org.geekygoblin.nedetlesmaki.core.IDefaultControls;
import im.bci.jnuit.controls.Control;
import im.bci.jnuit.playn.controls.KeyControl;
import im.bci.jnuit.playn.controls.PlaynNuitControls;
import playn.core.Key;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
public class PlaynDefaultControls implements IDefaultControls {

    private final PlaynNuitControls controls;
    private final PlaynTouchControls touch;

    public PlaynDefaultControls(PlaynNuitControls controls, PlaynTouchControls touch) {
        this.controls = controls;
        this.touch = touch;
    }

    @Override
    public Control[] getMouseClickControls() {
        return new Control[]{controls.getClickControl()};
    }

    @Override
    public Control[] getUp() {
        return new Control[]{new KeyControl(controls, Key.UP), new TouchControls(touch, Key.UP)};
    }

    @Override
    public Control[] getDown() {
        return new Control[]{new KeyControl(controls, Key.DOWN), new TouchControls(touch, Key.DOWN)};
    }

    @Override
    public Control[] getLeft() {
        return new Control[]{new KeyControl(controls, Key.LEFT), new TouchControls(touch, Key.LEFT)};
    }

    @Override
    public Control[] getRight() {
        return new Control[]{new KeyControl(controls, Key.RIGHT), new TouchControls(touch, Key.RIGHT)};
    }

    @Override
    public Control[] getRewind() {
        return new Control[]{new KeyControl(controls, Key.BACK), new TouchControls(touch, Key.BACK)};
    }

    @Override
    public Control[] getMenu() {
        return new Control[]{new KeyControl(controls, Key.ESCAPE), new TouchControls(touch, Key.BACK)};
    }

}
