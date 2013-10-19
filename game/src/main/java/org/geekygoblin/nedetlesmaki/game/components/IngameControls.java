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
package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;
import im.bci.lwjgl.nuit.controls.Action;
import im.bci.lwjgl.nuit.controls.ActionActivatedDetector;
import im.bci.lwjgl.nuit.controls.KeyControl;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author devnewton
 */
public class IngameControls extends Component{
    private ActionActivatedDetector up, down, left, right, showMenu, dance;
    
    public IngameControls() {
        up = new ActionActivatedDetector(new Action("up", new KeyControl(Keyboard.KEY_UP)));
        down = new ActionActivatedDetector(new Action("down", new KeyControl(Keyboard.KEY_DOWN)));
        left = new ActionActivatedDetector(new Action("left", new KeyControl(Keyboard.KEY_LEFT)));
        right = new ActionActivatedDetector(new Action("right", new KeyControl(Keyboard.KEY_RIGHT)));
        showMenu = new ActionActivatedDetector(new Action("menu", new KeyControl(Keyboard.KEY_ESCAPE)));
        dance = new ActionActivatedDetector(new Action("dance", new KeyControl(Keyboard.KEY_SPACE)));
        
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

    public ActionActivatedDetector getShowMenu() {
        return showMenu;
    }

    public void setShowMenu(ActionActivatedDetector showMenu) {
        this.showMenu = showMenu;
    }
    
    public ActionActivatedDetector getDance() {
		return dance;
	}
    
    public void setDance(ActionActivatedDetector dance) {
		this.dance = dance;
	}
    
}
