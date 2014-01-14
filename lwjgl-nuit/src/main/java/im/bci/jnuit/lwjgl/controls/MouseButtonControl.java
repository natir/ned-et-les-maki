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
package im.bci.jnuit.lwjgl.controls;

import im.bci.jnuit.controls.Control;
import org.lwjgl.input.Mouse;

public class MouseButtonControl implements Control {

    private final int button;

    public MouseButtonControl(int button) {
        this.button = button;
    }

    @Override
    public String getName() {
        return Mouse.getButtonName(button);
    }

    @Override
    public float getDeadZone() {
        return 0.1f;
    }

    @Override
    public float getValue() {
        return Mouse.isButtonDown(button) ? 1.0f : 0.0f;
    }

    @Override
    public String getControllerName() {
        return "Mouse";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.button;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MouseButtonControl other = (MouseButtonControl) obj;
        if (this.button != other.button) {
            return false;
        }
        return true;
    }

}
