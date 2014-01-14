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

import im.bci.jnuit.visitors.WidgetVisitor;
import java.util.List;

public class Stack extends Widget {

    public void show(Widget w) {
        add(w);
        w.onShow();
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        for (Widget child : getChildren()) {
            child.setX(x);
        }
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        for (Widget child : getChildren()) {
            child.setY(y);
        }
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);

        for (Widget child : getChildren()) {
            child.setWidth(width);
        }
    }

    @Override
    public void add(Widget child) {
        super.add(child);
        child.setX(getX());
        child.setY(getY());
        child.setWidth(getWidth());
        child.setHeight(getHeight());
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        for (Widget child : getChildren()) {
            child.setHeight(height);
        }
    }

    @Override
    public Widget getFocusedChild() {
        List<Widget> children = getChildren();
        int size = children.size();
        if (size > 0) {
            return children.get(size - 1);
        } else {
            return null;
        }
    }

    @Override
    public void onMouseMove(float mouseX, float mouseY) {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onMouseMove(mouseX, mouseY);
        }
    }

    @Override
    public void onMouseClick(float mouseX, float mouseY) {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onMouseClick(mouseX, mouseY);
        }
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }
}
