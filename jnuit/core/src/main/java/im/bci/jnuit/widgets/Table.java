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
package im.bci.jnuit.widgets;

import im.bci.jnuit.NuitToolkit;

import java.util.List;

import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Cell;
import com.esotericsoftware.tablelayout.Value;
import im.bci.jnuit.visitors.WidgetVisitor;

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

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }

}
