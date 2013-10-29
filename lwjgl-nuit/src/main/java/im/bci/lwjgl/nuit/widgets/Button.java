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
