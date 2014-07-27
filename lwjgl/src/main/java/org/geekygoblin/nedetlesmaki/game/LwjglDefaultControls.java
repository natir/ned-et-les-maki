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
package org.geekygoblin.nedetlesmaki.game;

import org.geekygoblin.nedetlesmaki.core.IDefaultControls;
import com.google.inject.Singleton;
import im.bci.jnuit.controls.Control;
import im.bci.jnuit.lwjgl.controls.KeyControl;
import im.bci.jnuit.lwjgl.controls.MouseButtonControl;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
@Singleton
public class LwjglDefaultControls implements IDefaultControls {

    @Override
    public Control[] getMouseClickControls() {
        return new Control[]{new MouseButtonControl(0)};
    }

    @Override
    public Control[] getUp() {
        return new Control[]{new KeyControl(Keyboard.KEY_UP)};
    }

    @Override
    public Control[] getDown() {
        return new Control[]{new KeyControl(Keyboard.KEY_DOWN)};
    }

    @Override
    public Control[] getLeft() {
        return new Control[]{new KeyControl(Keyboard.KEY_LEFT)};
    }

    @Override
    public Control[] getRight() {
        return new Control[]{new KeyControl(Keyboard.KEY_RIGHT)};
    }

    @Override
    public Control[] getRewind() {
        return new Control[]{new KeyControl(Keyboard.KEY_BACK), new MouseButtonControl(1)};
    }

    @Override
    public Control[] getMenu() {
        return new Control[]{new KeyControl(Keyboard.KEY_ESCAPE)};
    }

}
