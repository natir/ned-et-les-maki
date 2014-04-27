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
package org.geekygoblin.nedetlesmaki.core.components;

import org.geekygoblin.nedetlesmaki.core.IDefaultControls;
import com.artemis.Component;
import im.bci.jnuit.controls.Action;
import im.bci.jnuit.controls.ActionActivatedDetector;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 *
 * @author devnewton
 */
@Singleton
public class IngameControls extends Component {

    private ActionActivatedDetector up, down, left, right, rewind, showMenu;

    @Inject
    public IngameControls(IDefaultControls defaultControls) {
        up = new ActionActivatedDetector(new Action("action.up", defaultControls.getUp()));
        down = new ActionActivatedDetector(new Action("action.down", defaultControls.getDown()));
        left = new ActionActivatedDetector(new Action("action.left", defaultControls.getLeft()));
        right = new ActionActivatedDetector(new Action("action.right", defaultControls.getRight()));
        rewind = new ActionActivatedDetector(new Action("action.rewind", defaultControls.getRewind()));
        showMenu = new ActionActivatedDetector(new Action("action.menu", defaultControls.getMenu()));
    }

    public ActionActivatedDetector getUp() {
        return up;
    }

    public void setUp(ActionActivatedDetector up) {
        this.up = up;
    }

    public ActionActivatedDetector getDown() {
        return down;
    }

    public void setDown(ActionActivatedDetector down) {
        this.down = down;
    }

    public ActionActivatedDetector getLeft() {
        return left;
    }

    public void setLeft(ActionActivatedDetector left) {
        this.left = left;
    }

    public ActionActivatedDetector getRight() {
        return right;
    }

    public void setRight(ActionActivatedDetector right) {
        this.right = right;
    }

    public ActionActivatedDetector getRewind() {
        return rewind;
    }

    public void setRewind(ActionActivatedDetector r) {
        this.rewind = r;
    }

    public ActionActivatedDetector getShowMenu() {
        return showMenu;
    }

    public void setShowMenu(ActionActivatedDetector showMenu) {
        this.showMenu = showMenu;
    }

}
