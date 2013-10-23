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

import org.lwjgl.opengl.GL11;

import im.bci.lwjgl.nuit.NuitToolkit;
import im.bci.lwjgl.nuit.utils.TrueTypeFont;

public class Button extends Widget {

    private final String text;
    private final NuitToolkit toolkit;

    public Button(NuitToolkit toolkit, String text) {
        this.toolkit = toolkit;
        this.text = text;
    }

    @Override
    public void draw() {
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        TrueTypeFont font = toolkit.getFont();
        String translatedText = toolkit.getMessage(text);
        GL11.glTranslatef(getX() + getWidth()/2.0f - font.getWidth(translatedText)/3.0f, getY() + getHeight()/2.0f + font.getHeight(translatedText) / 2.0f, 0.0f);
        GL11.glScalef(1, -1, 1);
        font.drawString(translatedText);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    
    @Override
    public void onMouseClick(float mouseX, float mouseY) {
    	this.onOK();
    }
    
    @Override
    public float getMinWidth() {
        return toolkit.getFont().getWidth(toolkit.getMessage(text));
    }
    
    @Override
    public float getMinHeight() {
        return toolkit.getFont().getHeight(toolkit.getMessage(text));
    }
    
}
