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

import java.util.List;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.visitors.WidgetVisitor;

public class Select<T> extends Widget {

    private final NuitToolkit toolkit;
    private final List<T> possibleValues;
    private int selected;
    private int oldSelected;
    private boolean suckFocus;

    public Select(NuitToolkit toolkit, List<T> possibleValues) {
        this.toolkit = toolkit;
        this.possibleValues = possibleValues;
    }

    @Override
    public float getMinWidth() {
        float minWidth = 0.0f;
        for (T value : possibleValues) {
            minWidth = Math.max(toolkit.getFont().getWidth(value.toString()), minWidth);
        }
        return minWidth;
    }

    @Override
    public float getMinHeight() {
        float minHeight = 0.0f;
        for (T value : possibleValues) {
            minHeight = Math.max(toolkit.getFont().getHeight(value.toString()), minHeight);
        }
        return minHeight;
    }

    @Override
    public boolean isFocusWhore() {
        return true;
    }

    @Override
    public void suckFocus() {
        suckFocus = true;
        oldSelected = selected;
    }

    @Override
    public boolean isSuckingFocus() {
        return suckFocus;
    }

    public T getSelected() {
        return possibleValues.get(selected);
    }

    public void setSelected(T value) {
        this.selected = possibleValues.indexOf(value);
        if (this.selected < 0) {
            this.selected = 0;
        }
    }

    @Override
    public void onLeft() {
        --selected;
        if (selected < 0) {
            selected = possibleValues.size() - 1;
        }
    }

    @Override
    public void onRight() {
        ++selected;
        if (selected >= possibleValues.size()) {
            selected = 0;
        }
    }

    @Override
    public void onOK() {
        suckFocus = false;
    }

    @Override
    public void onMouseClick(float mouseX, float mouseY) {
        onRight();
    }

    @Override
    public void onCancel() {
        selected = oldSelected;
        suckFocus = false;
    }

    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }
}
