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

import org.lwjgl.opengl.GL11;

import im.bci.lwjgl.nuit.NuitToolkit;
import im.bci.lwjgl.nuit.utils.TrueTypeFont;

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
        for(T value : possibleValues) {
            minWidth = Math.max(toolkit.getFont().getWidth(value.toString()), minWidth);
        }
        return minWidth;
    }
    
    @Override
    public float getMinHeight() {
        float minHeight = 0.0f;
        for(T value : possibleValues) {
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
        if(this.selected<0) {
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
    public void draw() {
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        TrueTypeFont font = toolkit.getFont();
        String text = possibleValues.get(selected).toString();
        GL11.glTranslatef(getX() + getWidth() / 2.0f - font.getWidth(text) / 4.0f, getY() + getHeight() / 2.0f + font.getHeight(text) / 2.0f, 0.0f);
        GL11.glScalef(1, -1, 1);
        font.drawString(text);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
