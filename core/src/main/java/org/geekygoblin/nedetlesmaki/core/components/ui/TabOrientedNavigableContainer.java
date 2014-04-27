/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

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
package org.geekygoblin.nedetlesmaki.core.components.ui;

import im.bci.jnuit.widgets.Button;
import im.bci.jnuit.widgets.Widget;

import im.bci.jnuit.widgets.Container;
import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 * @author devnewton
 */
public class TabOrientedNavigableContainer extends Container {

    private final TreeSet<Widget> leftToRightChildren;
    private final TreeSet<Widget> topToBottomChildren;

    private static class LeftToRightComparator implements Comparator<Widget> {

        @Override
        public int compare(Widget o1, Widget o2) {
            if ((o1.getY() + o1.getHeight()) < o2.getY()) {
                return -1;
            }
            if (o1.getY() > (o2.getY() + o2.getHeight())) {
                return 1;
            }
            return Float.compare(o1.getX(), o2.getX());
        }
    }

    private static class TopToBottomComparator implements Comparator<Widget> {

        @Override
        public int compare(Widget o1, Widget o2) {
            if ((o1.getX() + o1.getWidth()) < o2.getX()) {
                return -1;
            }
            if (o1.getX() > (o2.getX() + o2.getWidth())) {
                return 1;
            }
            return Float.compare(o1.getY(), o2.getY());
        }
    }

    public TabOrientedNavigableContainer() {
        leftToRightChildren = new TreeSet<Widget>(new LeftToRightComparator());
        topToBottomChildren = new TreeSet<Widget>(new TopToBottomComparator());
    }

    @Override
    public Widget findClosestLeftFocusableWidget(Widget widget) {
        leftToRightChildren.clear();
        leftToRightChildren.addAll(getChildren());
        for (Widget w : leftToRightChildren.headSet(widget, false).descendingSet()) {
            if (w.isFocusable()) {
                return w;
            }
        }
        for (Widget w : leftToRightChildren.tailSet(widget, false).descendingSet()) {
            if (w.isFocusable()) {
                return w;
            }
        }
        return null;
    }

    @Override
    public Widget findClosestRightFocusableWidget(Widget widget) {
        leftToRightChildren.clear();
        leftToRightChildren.addAll(getChildren());
        for (Widget w : leftToRightChildren.tailSet(widget, false)) {
            if (w.isFocusable()) {
                return w;
            }
        }
        for (Widget w : leftToRightChildren.headSet(widget, false)) {
            if (w.isFocusable()) {
                return w;
            }
        }
        return null;
    }

    @Override
    public Widget findClosestUpFocusableWidget(Widget widget) {
        topToBottomChildren.clear();
        topToBottomChildren.addAll(getChildren());
        System.out.println("topToBottom");
        for (Widget w : topToBottomChildren) {
            if (w instanceof Button) {
                System.out.println(((Button) w).getText());
            }
        }
        for (Widget w : topToBottomChildren.headSet(widget, false).descendingSet()) {
            if (w.isFocusable()) {
                return w;
            }
        }
        for (Widget w : topToBottomChildren.tailSet(widget, false).descendingSet()) {
            if (w.isFocusable()) {
                return w;
            }
        }
        return null;
    }

    @Override
    public Widget findClosestDownFocusableWidget(Widget widget) {
        topToBottomChildren.clear();
        topToBottomChildren.addAll(getChildren());
        for (Widget w : topToBottomChildren.tailSet(widget, false)) {
            if (w.isFocusable()) {
                return w;
            }
        }
        for (Widget w : topToBottomChildren.headSet(widget, false)) {
            if (w.isFocusable()) {
                return w;
            }
        }
        return null;
    }
}
