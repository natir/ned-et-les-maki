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
package im.bci.jnuit;

import im.bci.jnuit.controls.Action;
import im.bci.jnuit.controls.ActionActivatedDetector;
import im.bci.jnuit.widgets.Root;
import im.bci.jnuit.widgets.Table;
import im.bci.jnuit.widgets.Widget;

import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Cell;
import com.esotericsoftware.tablelayout.Toolkit;
import im.bci.jnuit.controls.Control;
import im.bci.jnuit.controls.Pointer;
import im.bci.jnuit.display.VideoResolution;
import java.util.List;

public class NuitToolkit extends Toolkit<Widget, Table> {

    private final NuitFont font;
    private final NuitTranslator translator;
    private final NuitRenderer renderer;
    private final NuitControls controls;

    private final ActionActivatedDetector menuUp, menuDown, menuLeft, menuRight, menuOK, menuCancel;
    private final Pointer pointer = new Pointer();
    private float oldPointerX, oldPointerY;
    private Boolean oldIsMouseButtonDown;
    private final NuitDisplay display;

    public NuitToolkit(NuitDisplay display, NuitControls controls, NuitTranslator translator, NuitFont font, NuitRenderer renderer) {
        this.font = font;
        this.translator = translator;
        this.renderer = renderer;
        this.controls = controls;
        this.display = display;
        menuUp = new ActionActivatedDetector(new Action("nuit.action.menu.up", controls.getDefaultMenuUpControls()));
        menuDown = new ActionActivatedDetector(new Action("nuit.action.menu.down", controls.getDefaultMenuDownControls()));
        menuLeft = new ActionActivatedDetector(new Action("nuit.action.menu.left", controls.getDefaultMenuLeftControls()));
        menuRight = new ActionActivatedDetector(new Action("nuit.action.menu.right", controls.getDefaultMenuRightControls()));
        menuOK = new ActionActivatedDetector(new Action("nuit.action.menu.ok", controls.getDefaultMenuOkControls()));
        menuCancel = new ActionActivatedDetector(new Action("nuit.action.menu.cancel", controls.getDefaultMenuCancelControls()));
    }

    public NuitFont getFont() {
        return font;
    }

    public String getMessage(String key) {
        return translator.getMessage(key);
    }

    public NuitRenderer getRenderer() {
        return renderer;
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

        controls.pollPointer(root, pointer);
        if (pointer.getX() != oldPointerX || pointer.getY() != oldPointerY) {
            root.onMouseMove(pointer.getX(), pointer.getY());
        }
        oldPointerX = pointer.getX();
        oldPointerY = pointer.getY();
        boolean isMouseButtonDown = pointer.isDown();
        if (isMouseButtonDown && Boolean.FALSE == oldIsMouseButtonDown) {
            root.onMouseClick(pointer.getX(), pointer.getY());
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

    public List<Control> getPossibleControls() {
        return controls.getPossibleControls();
    }

    public List<VideoResolution> listResolutions() {
        return display.listResolutions();
    }

    public void changeResolution(VideoResolution resolution, boolean fullscreen) {
        display.changeResolution(resolution, fullscreen);
    }

    public VideoResolution getResolution() {
        return display.getResolution();
    }

    public boolean isFullscreen() {
        return display.isFullscreen();
    }

}
