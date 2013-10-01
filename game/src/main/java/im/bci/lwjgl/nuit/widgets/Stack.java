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
    public void draw() {
        Widget child = getFocusedChild();
        if (null != child) {
            child.draw();
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

}