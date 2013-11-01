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

import im.bci.lwjgl.nuit.NuitToolkit;
import im.bci.lwjgl.nuit.utils.LwjglHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class Root extends Stack {

    private NuitToolkit toolkit;
    private Color backgroundColor = new Color(0, 0, 0, 127);

    public Root(NuitToolkit tk) {
        this.toolkit = tk;
        setWidth(1280);
        setHeight(800);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void draw() {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_TRANSFORM_BIT | GL11.GL_HINT_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_SCISSOR_BIT | GL11.GL_LINE_BIT | GL11.GL_TEXTURE_BIT);
        GL11.glViewport(0, 0, LwjglHelper.getWidth(), LwjglHelper.getHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(getX(), getWidth(), getHeight(), getY(), -1.0, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        drawBackground();
        super.draw();

        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    protected void drawBackground() {
        if (null != backgroundColor) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4ub(backgroundColor.getRedByte(), backgroundColor.getGreenByte(), backgroundColor.getBlueByte(), backgroundColor.getAlphaByte());
            GL11.glRectf(getX(), getY(), getWidth(), getHeight());
            GL11.glColor4ub(Color.WHITE.getRedByte(), Color.WHITE.getGreenByte(), Color.WHITE.getBlueByte(), Color.WHITE.getAlphaByte());
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    @Override
    public void update() {
        toolkit.update(this);
        for (Widget child : getChildren()) {
            child.update();
        }
    }
}
