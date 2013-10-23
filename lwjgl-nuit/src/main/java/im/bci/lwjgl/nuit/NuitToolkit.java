/*
 * Copyright (c) 2013 devnewton <devnewton@bci.im>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'devnewton <devnewton@bci.im>' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import java.util.ResourceBundle;

public class NuitToolkit extends Toolkit<Widget, Table> implements AutoCloseable {

    private ActionActivatedDetector menuUp, menuDown, menuLeft, menuRight, menuOK, menuCancel;
    private TrueTypeFont font;
    private Vector2f oldMousePos;
    private Boolean oldIsMouseButtonDown;

    public NuitToolkit() {
        menuUp = new ActionActivatedDetector(new Action("nuit.action.menu.up", new KeyControl(Keyboard.KEY_UP)));
        menuDown = new ActionActivatedDetector(new Action("nuit.action.menu.down", new KeyControl(Keyboard.KEY_DOWN)));
        menuLeft = new ActionActivatedDetector(new Action("nuit.action.menu.left", new KeyControl(Keyboard.KEY_LEFT)));
        menuRight = new ActionActivatedDetector(new Action("nuit.action.menu.right", new KeyControl(Keyboard.KEY_RIGHT)));
        menuOK = new ActionActivatedDetector(new Action("nuit.action.menu.ok", new KeyControl(Keyboard.KEY_RETURN)));
        menuCancel = new ActionActivatedDetector(new Action("nuit.action.menu.cancel", new KeyControl(Keyboard.KEY_ESCAPE)));
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
}
