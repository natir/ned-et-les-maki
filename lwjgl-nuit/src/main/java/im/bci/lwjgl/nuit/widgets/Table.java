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
package im.bci.lwjgl.nuit.widgets;

import im.bci.lwjgl.nuit.NuitToolkit;

import java.util.List;

import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Cell;
import com.esotericsoftware.tablelayout.Value;

public class Table extends Container {

    private final TableLayout layout;

    public Cell<Widget, Table> cell(Widget widget) {
        add(widget);
        return layout.add(widget);
    }

    public Cell<Widget, Table> row() {
        return layout.row();
    }

    public Cell<Widget, Table> columnDefaults(int column) {
        return layout.columnDefaults(column);
    }

    public void clear() {
        layout.clear();
    }

    public Cell<Widget, Table> defaults() {
        return layout.defaults();
    }

    public BaseTableLayout<Widget, Table> pad(Value<Widget, Table> top, Value<Widget, Table> left, Value<Widget, Table> bottom, Value<Widget, Table> right) {
        return layout.pad(top, left, bottom, right);
    }

    public BaseTableLayout<Widget, Table> pad(float pad) {
        return layout.pad(pad);
    }

    public BaseTableLayout<Widget, Table> pad(float top, float left, float bottom, float right) {
        return layout.pad(top, left, bottom, right);
    }

    public BaseTableLayout<Widget, Table> padTop(float padTop) {
        return layout.padTop(padTop);
    }

    public BaseTableLayout<Widget, Table> padLeft(float padLeft) {
        return layout.padLeft(padLeft);
    }

    public BaseTableLayout<Widget, Table> padBottom(float padBottom) {
        return layout.padBottom(padBottom);
    }

    public BaseTableLayout<Widget, Table> padRight(float padRight) {
        return layout.padRight(padRight);
    }

    public BaseTableLayout<Widget, Table> align(int align) {
        return layout.align(align);
    }

    public BaseTableLayout<Widget, Table> center() {
        return layout.center();
    }

    public BaseTableLayout<Widget, Table> top() {
        return layout.top();
    }

    public BaseTableLayout<Widget, Table> left() {
        return layout.left();
    }

    public BaseTableLayout<Widget, Table> bottom() {
        return layout.bottom();
    }

    public BaseTableLayout<Widget, Table> right() {
        return layout.right();
    }

    public Table(NuitToolkit toolkit) {
        layout = new TableLayout(toolkit);
        layout.setTable(this);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        layout();
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        layout();
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        layout();
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        layout();
    }

    public void layout() {
        layout.layout(getX(), getY(), getWidth(), getHeight());
        List<Cell<Widget, Table>> cells = layout.getCells();
        for (int i = 0, n = cells.size(); i < n; i++) {
            Cell<Widget, Table> c = cells.get(i);
            if (c.getIgnore()) {
                continue;
            }
            Widget cellWidget = c.getWidget();
            cellWidget.setX(c.getWidgetX());
            cellWidget.setY(c.getWidgetY());
            cellWidget.setWidth(c.getWidgetWidth());
            cellWidget.setHeight(c.getWidgetHeight());
        }
    }

}
