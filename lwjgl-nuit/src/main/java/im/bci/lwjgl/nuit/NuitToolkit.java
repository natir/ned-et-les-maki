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
package im.bci.lwjgl.nuit;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import im.bci.lwjgl.nuit.controls.Action;
import im.bci.lwjgl.nuit.controls.ActionActivatedDetector;
import im.bci.lwjgl.nuit.controls.KeyControl;
import im.bci.lwjgl.nuit.utils.LwjglHelper;
import im.bci.lwjgl.nuit.utils.TrueTypeFont;
import im.bci.lwjgl.nuit.widgets.Root;
import im.bci.lwjgl.nuit.widgets.Table;
import im.bci.lwjgl.nuit.widgets.Widget;

import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Cell;
import com.esotericsoftware.tablelayout.Toolkit;
import im.bci.lwjgl.nuit.controls.ControlActivatedDetector;
import im.bci.lwjgl.nuit.controls.GamepadAxisControl;
import im.bci.lwjgl.nuit.controls.GamepadButtonControl;
import im.bci.lwjgl.nuit.controls.MouseButtonControl;
import im.bci.lwjgl.nuit.widgets.ControlsConfigurator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class NuitToolkit extends Toolkit<Widget, Table> implements AutoCloseable {

    private ActionActivatedDetector menuUp, menuDown, menuLeft, menuRight, menuOK, menuCancel;
    private TrueTypeFont font;
    private Vector2f oldMousePos;
    private Boolean oldIsMouseButtonDown;
    private List<ControlActivatedDetector> possibleControls;

    public NuitToolkit() {
        menuUp = new ActionActivatedDetector(new Action("nuit.action.menu.up", new KeyControl(Keyboard.KEY_UP)));
        menuDown = new ActionActivatedDetector(new Action("nuit.action.menu.down", new KeyControl(Keyboard.KEY_DOWN)));
        menuLeft = new ActionActivatedDetector(new Action("nuit.action.menu.left", new KeyControl(Keyboard.KEY_LEFT)));
        menuRight = new ActionActivatedDetector(new Action("nuit.action.menu.right", new KeyControl(Keyboard.KEY_RIGHT)));
        menuOK = new ActionActivatedDetector(new Action("nuit.action.menu.ok", new KeyControl(Keyboard.KEY_RETURN)));
        menuCancel = new ActionActivatedDetector(new Action("nuit.action.menu.cancel", new KeyControl(Keyboard.KEY_ESCAPE)));
        initPossibleControls();
    }

    public String getMessage(String key) {
        return ResourceBundle.getBundle("nuit_messages").getString(key);
    }

    protected TrueTypeFont createFont() {
        Font f;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Boxy-Bold.ttf")) {
            f = Font.createFont(Font.TRUETYPE_FONT, is);
            f = f.deriveFont(Font.PLAIN, 48);
        } catch (IOException | FontFormatException e) {
            f = new Font("monospaced", Font.BOLD, 24);
        }
        return new TrueTypeFont(f, true, new char[0], new HashMap<Character, BufferedImage>());
    }

    public TrueTypeFont getFont() {
        if (null == font) {
            font = createFont();
        }
        return font;
    }

    public Action getMenuUp() {
        return menuUp.getAction();
    }

    public Action getMenuDown() {
        return menuDown.getAction();
    }

    public Action getMenuLeft() {
        return menuLeft.getAction();
    }

    public Action getMenuRight() {
        return menuRight.getAction();
    }

    public Action getMenuOK() {
        return menuOK.getAction();
    }

    public Action getMenuCancel() {
        return menuCancel.getAction();
    }

    @Override
    public Cell<Widget, Table> obtainCell(BaseTableLayout<Widget, Table> layout) {
        Cell<Widget, Table> cell = new Cell<>();
        cell.setLayout(layout);
        return cell;
    }

    @Override
    public void freeCell(Cell<Widget, Table> cell) {
    }

    @Override
    public void addChild(Widget parent, Widget child) {
        parent.add(child);
    }

    @Override
    public void removeChild(Widget parent, Widget child) {
        parent.remove(child);
    }

    @Override
    public float getMinWidth(Widget widget) {
        return widget.getMinWidth();
    }

    @Override
    public float getMinHeight(Widget widget) {
        return widget.getMinHeight();
    }

    @Override
    public float getPrefWidth(Widget widget) {
        return widget.getPreferredWidth();
    }

    @Override
    public float getPrefHeight(Widget widget) {
        return widget.getPreferredHeight();
    }

    @Override
    public float getMaxWidth(Widget widget) {
        return widget.getMaxWidth();
    }

    @Override
    public float getMaxHeight(Widget widget) {
        return widget.getMaxHeight();
    }

    @Override
    public float getWidth(Widget widget) {
        return widget.getWidth();
    }

    @Override
    public float getHeight(Widget widget) {
        return widget.getHeight();
    }

    @Override
    public void clearDebugRectangles(BaseTableLayout<Widget, Table> layout) {
        // TODO Auto-generated method stub
    }

    @Override
    public void addDebugRectangle(BaseTableLayout<Widget, Table> layout, Debug type, float x, float y, float w, float h) {
        // TODO Auto-generated method stub
    }

    public void update(Root root) {
        menuUp.poll();
        menuDown.poll();
        menuLeft.poll();
        menuRight.poll();
        menuOK.poll();
        menuCancel.poll();

        if (menuUp.isActivated()) {
            root.onUp();
        }
        if (menuDown.isActivated()) {
            root.onDown();
        }
        if (menuLeft.isActivated()) {
            root.onLeft();
        }
        if (menuRight.isActivated()) {
            root.onRight();
        }
        if (menuOK.isActivated()) {
            root.onOK();
        }
        if (menuCancel.isActivated()) {
            root.onCancel();
        }

        float mouseX = Mouse.getX() * root.getWidth() / LwjglHelper.getWidth();
        float mouseY = root.getHeight() - (Mouse.getY() * root.getHeight() / LwjglHelper.getHeight());
        if (null == oldMousePos) {
            oldMousePos = new Vector2f();
        } else {
            if (mouseX != oldMousePos.getX() || mouseY != oldMousePos.getY()) {
                root.onMouseMove(mouseX, mouseY);
            }
        }
        oldMousePos.set(mouseX, mouseY);

        boolean isMouseButtonDown = Mouse.isButtonDown(0);
        if (isMouseButtonDown && Boolean.FALSE == oldIsMouseButtonDown) {
            root.onMouseClick(mouseX, mouseY);
        }
        oldIsMouseButtonDown = isMouseButtonDown;
    }

    public void resetInputPoll() {
        menuUp.reset();
        menuDown.reset();
        menuLeft.reset();
        menuRight.reset();
        menuOK.reset();
        menuCancel.reset();
    }

    @Override
    public void close() throws Exception {
        if (null != font) {
            font.deleteFontTexture();
        }
    }

    private void initPossibleControls() {
        possibleControls  = new ArrayList<>();
        for (int c = 0; c < Controllers.getControllerCount(); ++c) {
            Controller pad = Controllers.getController(c);
            for (int a = 0; a < pad.getAxisCount(); ++a) {
                getPossibleControls().add(new ControlActivatedDetector(new GamepadAxisControl(pad, a, true)));
                getPossibleControls().add(new ControlActivatedDetector(new GamepadAxisControl(pad, a, false)));
            }
            for (int b = 0; b < pad.getButtonCount(); ++b) {
                getPossibleControls().add(new ControlActivatedDetector(new GamepadButtonControl(pad, b)));
            }
        }
        for (Field field : Keyboard.class.getFields()) {
            String name = field.getName();
            if (name.startsWith("KEY_")) {
                try {
                    int key = field.getInt(null);
                    getPossibleControls().add(new ControlActivatedDetector(new KeyControl(key)));
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    Logger.getLogger(ControlsConfigurator.class.getName()).log(Level.SEVERE, "error retrieving key", e);
                }
            }
        }
        for (int m = 0; m < Mouse.getButtonCount(); ++m) {
            getPossibleControls().add(new ControlActivatedDetector(new MouseButtonControl(m)));
        }
    }

    public List<ControlActivatedDetector> getPossibleControls() {
        return possibleControls;
    }

    public void setPossibleControls(List<ControlActivatedDetector> possibleControls) {
        this.possibleControls = possibleControls;
    }

}
