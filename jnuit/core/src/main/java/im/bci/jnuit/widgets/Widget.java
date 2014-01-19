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

import im.bci.jnuit.background.Background;
import im.bci.jnuit.background.NullBackground;
import im.bci.jnuit.border.Border;
import im.bci.jnuit.border.NullBorder;
import im.bci.jnuit.visitors.WidgetVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class Widget {

    private float x, y, width, height;
    private Background background = NullBackground.INSTANCE;
    private Background focusedBackground = null;
    private Border topBorder = NullBorder.INSTANCE, bottomBorder = NullBorder.INSTANCE, leftBorder = NullBorder.INSTANCE, rightBorder = NullBorder.INSTANCE;
    private boolean mustDrawFocus = true;
    private final List<Widget> children = new ArrayList<>();

    public Border getTopBorder() {
        return topBorder;
    }

    public void setTopBorder(Border topBorder) {
        this.topBorder = topBorder;
    }

    public Border getBottomBorder() {
        return bottomBorder;
    }

    public void setBottomBorder(Border bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    public Border getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(Border leftBorder) {
        this.leftBorder = leftBorder;
    }

    public Border getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(Border rightBorder) {
        this.rightBorder = rightBorder;
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public Background getFocusedBackground() {
        return focusedBackground;
    }

    public void setFocusedBackground(Background focusedBackground) {
        this.focusedBackground = focusedBackground;
    }

    public List<Widget> getChildren() {
        return children;
    }

    public boolean isFocusable() {
        return true;
    }

    public boolean isFocusWhore() {
        return false;
    }

    public void onLeft() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onLeft();
        }
    }

    public void onRight() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onRight();
        }
    }

    public void onUp() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onUp();
        }
    }

    public void onDown() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onDown();
        }
    }

    public void onOK() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onOK();
        }
    }

    public void onCancel() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.onCancel();
        }
    }

    public Widget getFocusedChild() {
        return null;
    }

    public abstract void accept(WidgetVisitor visitor);

    public void add(Widget child) {
        children.remove(child);
        children.add(child);
    }

    public void remove(Widget child) {
        children.remove(child);
    }

    public float getMinWidth() {
        return 0;
    }

    public float getMinHeight() {
        return 0;
    }

    public float getPreferredWidth() {
        return 0;
    }

    public float getPreferredHeight() {
        return 0;
    }

    public float getMaxWidth() {
        return 0;
    }

    public float getMaxHeight() {
        return 0;
    }

    public float getCenterX() {
        return x + width / 2.0f;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getCenterY() {
        return y + height / 2.0f;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Widget findClosestLeftFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {

            float closestLeftChildLengthSquared = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterX() < widget.getCenterX()) {
                    float lenghtSquared = distanceSquared(w, widget);
                    if (null == closestLeftChild || lenghtSquared < closestLeftChildLengthSquared) {
                        closestLeftChildLengthSquared = lenghtSquared;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }

    public Widget findClosestRightFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {

            float closestLeftChildLengthSquared = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterX() > widget.getCenterX()) {
                    float lenghtSquared = distanceSquared(w, widget);
                    if (null == closestLeftChild || lenghtSquared < closestLeftChildLengthSquared) {
                        closestLeftChildLengthSquared = lenghtSquared;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }

    public Widget findClosestUpFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {

            float closestLeftChildLengthSquared = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterY() < widget.getCenterY()) {
                    float lenghtSquared = distanceSquared(w, widget);
                    if (null == closestLeftChild || lenghtSquared < closestLeftChildLengthSquared) {
                        closestLeftChildLengthSquared = lenghtSquared;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }

    public Widget findClosestDownFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {

            float closestLeftChildLengthSquared = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterY() > widget.getCenterY()) {
                    float lenghtSquared = distanceSquared(w, widget);
                    if (null == closestLeftChild || lenghtSquared < closestLeftChildLengthSquared) {
                        closestLeftChildLengthSquared = lenghtSquared;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }

    private static float distanceSquared(Widget w1, Widget w2) {
        float dx = w1.getCenterX() - w2.getCenterX();
        float dy = w1.getCenterY() - w2.getCenterY();
        return dx * dx + dy * dy;
    }

    protected Widget getTopLeftFocusableChild() {
        return Collections.min(getFocusableChildren(), new Comparator<Widget>() {
            @Override
            public int compare(Widget w1, Widget w2) {
                int result = Float.compare(w1.getCenterY(), w2.getCenterY());
                if (result == 0) {
                    result = Float.compare(w1.getCenterX(), w2.getCenterX());
                }
                return result;
            }
        });
    }

    private List<Widget> getFocusableChildren() {
        List<Widget> result = new ArrayList<>();
        for (Widget w : getChildren()) {
            if (w.isFocusable()) {
                result.add(w);
            }
        }
        return result;
    }

    public void suckFocus() {
    }

    public boolean isSuckingFocus() {
        return false;
    }

    public void update() {
        for (Widget child : children) {
            child.update();
        }
    }

    public void onMouseMove(float mouseX, float mouseY) {
        for (Widget child : children) {
            if (mouseX >= child.getX() && mouseX <= (child.getX() + child.getWidth()) && mouseY >= child.getY() && mouseY <= (child.getY() + child.getHeight())) {
                child.onMouseMove(mouseX, mouseY);
            }
        }
    }

    public void onMouseClick(float mouseX, float mouseY) {
        for (Widget child : children) {
            if (mouseX >= child.getX() && mouseX <= (child.getX() + child.getWidth()) && mouseY >= child.getY() && mouseY <= (child.getY() + child.getHeight())) {
                child.onMouseClick(mouseX, mouseY);
            }
        }
    }

    public void onShow() {
    }

    public boolean mustDrawFocus() {
        return mustDrawFocus;
    }

    public void setMustDrawFocus(boolean drawFocus) {
        this.mustDrawFocus = drawFocus;
    }
}
