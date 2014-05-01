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

    public PlaynDefaultControls(PlaynNuitControls controls) {
        this.controls = controls;
    }

    @Override
    public Control[] getMouseClickControls() {
        return new Control[]{};
    }

    @Override
    public Control[] getUp() {
        return new Control[]{new KeyControl(controls, Key.UP)};
    }

    @Override
    public Control[] getDown() {
        return new Control[]{new KeyControl(controls, Key.DOWN)};
    }

    @Override
    public Control[] getLeft() {
        return new Control[]{new KeyControl(controls, Key.LEFT)};
    }

    @Override
    public Control[] getRight() {
        return new Control[]{new KeyControl(controls, Key.RIGHT)};
    }

    @Override
    public Control[] getRewind() {
        return new Control[]{new KeyControl(controls, Key.BACK)};
    }

    @Override
    public Control[] getMenu() {
        return new Control[]{new KeyControl(controls, Key.ESCAPE)};
    }

}
