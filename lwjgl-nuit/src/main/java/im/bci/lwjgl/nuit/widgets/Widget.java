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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class Widget {

    public List<Widget> getChildren() {
        return children;
    }
    private float x, y, width, height;
    private List<Widget> children = new ArrayList<>();

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

    public void draw() {
        drawChildren();
    }

    protected void drawChildren() {
        for (Widget child : children) {
            child.draw();
        }
    }

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
                    float lenghtSquared = new Vector2f(w.getCenterX() - widget.getCenterX(), w.getCenterY() - widget.getCenterY()).lengthSquared();
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
                    float lenghtSquared = new Vector2f(w.getCenterX() - widget.getCenterX(), w.getCenterY() - widget.getCenterY()).lengthSquared();
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
                    float lenghtSquared = new Vector2f(w.getCenterX() - widget.getCenterX(), w.getCenterY() - widget.getCenterY()).lengthSquared();
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
                    float lenghtSquared = new Vector2f(w.getCenterX() - widget.getCenterX(), w.getCenterY() - widget.getCenterY()).lengthSquared();
                    if (null == closestLeftChild || lenghtSquared < closestLeftChildLengthSquared) {
                        closestLeftChildLengthSquared = lenghtSquared;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
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
}
