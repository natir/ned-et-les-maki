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
package im.bci.jnuit.lwjgl;

import im.bci.jnuit.NuitControls;
import im.bci.jnuit.controls.Control;
import im.bci.jnuit.controls.NullControl;
import im.bci.jnuit.controls.Pointer;
import im.bci.jnuit.lwjgl.controls.GamepadAxisControl;
import im.bci.jnuit.lwjgl.controls.GamepadButtonControl;
import im.bci.jnuit.lwjgl.controls.KeyControl;
import im.bci.jnuit.lwjgl.controls.MouseButtonControl;
import im.bci.jnuit.widgets.ControlsConfigurator;
import im.bci.jnuit.widgets.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 *
 * @author devnewton
 */
public class LwjglNuitControls implements NuitControls {

    @Override
    public List<Control> getPossibleControls() {
        List<Control> possibleControls = new ArrayList<>();
        for (int c = 0; c < Controllers.getControllerCount(); ++c) {
            Controller pad = Controllers.getController(c);
            for (int a = 0; a < pad.getAxisCount(); ++a) {
                possibleControls.add(new GamepadAxisControl(pad, a, true));
                possibleControls.add(new GamepadAxisControl(pad, a, false));
            }
            for (int b = 0; b < pad.getButtonCount(); ++b) {
                possibleControls.add(new GamepadButtonControl(pad, b));
            }
        }
        for (Field field : Keyboard.class.getFields()) {
            String name = field.getName();
            if (name.startsWith("KEY_")) {
                try {
                    int key = field.getInt(null);
                    possibleControls.add(new KeyControl(key));
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    Logger.getLogger(ControlsConfigurator.class.getName()).log(Level.SEVERE, "error retrieving key", e);
                }
            }
        }
        for (int m = 0; m < Mouse.getButtonCount(); ++m) {
            possibleControls.add(new MouseButtonControl(m));
        }
        return possibleControls;
    }

    @Override
    public Control[] getDefaultMenuUpControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(Keyboard.KEY_UP);
        controls[1] = NullControl.INSTANCE;
        for (int c = 0; c < Controllers.getControllerCount(); ++c) {
            Controller pad = Controllers.getController(c);
            if (pad.getAxisCount() >= 2) {
                controls[1] = new GamepadAxisControl(pad, 1, true);
            }
        }
        return controls;
    }

    @Override
    public Control[] getDefaultMenuDownControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(Keyboard.KEY_DOWN);
        controls[1] = NullControl.INSTANCE;
        for (int c = 0; c < Controllers.getControllerCount(); ++c) {
            Controller pad = Controllers.getController(c);
            if (pad.getAxisCount() >= 2) {
                controls[1] = new GamepadAxisControl(pad, 1, false);
            }
        }
        return controls;
    }

    @Override
    public Control[] getDefaultMenuLeftControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(Keyboard.KEY_LEFT);
        controls[1] = NullControl.INSTANCE;
        for (int c = 0; c < Controllers.getControllerCount(); ++c) {
            Controller pad = Controllers.getController(c);
            if (pad.getAxisCount() >= 1) {
                controls[1] = new GamepadAxisControl(pad, 0, false);
            }
        }
        return controls;
    }

    @Override
    public Control[] getDefaultMenuRightControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(Keyboard.KEY_RIGHT);
        controls[1] = NullControl.INSTANCE;
        for (int c = 0; c < Controllers.getControllerCount(); ++c) {
            Controller pad = Controllers.getController(c);
            if (pad.getAxisCount() >= 1) {
                controls[1] = new GamepadAxisControl(pad, 0, true);
            }
        }
        return controls;
    }

    @Override
    public Control[] getDefaultMenuOkControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(Keyboard.KEY_RETURN);
        controls[1] = NullControl.INSTANCE;
        for (int c = 0; c < Controllers.getControllerCount(); ++c) {
            Controller pad = Controllers.getController(c);
            if (pad.getButtonCount() >= 1) {
                controls[1] = new GamepadButtonControl(pad, 0);
            }
        }
        return controls;
    }

    @Override
    public Control[] getDefaultMenuCancelControls() {
        Control[] controls = new Control[2];
        controls[0] = new KeyControl(Keyboard.KEY_ESCAPE);
        controls[1] = NullControl.INSTANCE;
        for (int c = 0; c < Controllers.getControllerCount(); ++c) {
            Controller pad = Controllers.getController(c);
            if (pad.getButtonCount() >= 2) {
                controls[1] = new GamepadButtonControl(pad, 1);
            }
        }
        return controls;
    }

    @Override
    public void pollPointer(Root root, Pointer pointer) {
        pointer.setX(Mouse.getX() * root.getWidth() / LwjglHelper.getWidth());
        pointer.setY(root.getHeight() - (Mouse.getY() * root.getHeight() / LwjglHelper.getHeight()));
        pointer.setDown(Mouse.isButtonDown(0));
    }

}
