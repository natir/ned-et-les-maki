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
package im.bci.jnuit.lwjgl;

import im.bci.jnuit.NuitRenderer;
import im.bci.jnuit.NuitTranslator;
import im.bci.jnuit.background.Background;
import im.bci.jnuit.background.ColoredBackground;
import im.bci.jnuit.background.NullBackground;
import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.border.ColoredBorder;
import im.bci.jnuit.border.NullBorder;
import im.bci.jnuit.visitors.BackgroundVisitor;
import im.bci.jnuit.visitors.BorderVisitor;
import im.bci.jnuit.visitors.WidgetVisitor;
import im.bci.jnuit.widgets.AudioConfigurator;
import im.bci.jnuit.widgets.Button;
import im.bci.jnuit.widgets.Container;
import im.bci.jnuit.widgets.ControlsConfigurator;
import im.bci.jnuit.widgets.Label;
import im.bci.jnuit.widgets.NullWidget;
import im.bci.jnuit.widgets.Root;
import im.bci.jnuit.widgets.Select;
import im.bci.jnuit.widgets.Stack;
import im.bci.jnuit.widgets.Table;
import im.bci.jnuit.widgets.Toggle;
import im.bci.jnuit.widgets.VideoConfigurator;
import im.bci.jnuit.widgets.Widget;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public abstract class LwjglNuitRenderer implements WidgetVisitor, BackgroundVisitor, NuitRenderer {

    private final NuitTranslator translator;
    private final TrueTypeFont font;
    private final TopBorderRenderer topBorderRenderer = new TopBorderRenderer();
    private final BottomBorderRenderer bottomBorderRenderer = new BottomBorderRenderer();
    private final LeftBorderRenderer leftBorderRenderer = new LeftBorderRenderer();
    private final RightBorderRenderer rightBorderRenderer = new RightBorderRenderer();

    private class TopBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glLineWidth(border.getSize());
            GL11.glColor4f(border.getRed(), border.getGreen(), border.getBlue(), border.getAlpha());
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(widget.getX(), widget.getY());
            GL11.glVertex2f(widget.getX() + widget.getWidth(), widget.getY());
            GL11.glEnd();
            GL11.glColor4f(1f, 1f, 1f, 1f);
            GL11.glLineWidth(1.0f);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

    }

    private class BottomBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
             GL11.glLineWidth(border.getSize());
             GL11.glColor4f(border.getRed(), border.getGreen(), border.getBlue(), border.getAlpha());
             GL11.glBegin(GL11.GL_LINES);
             GL11.glVertex2f(widget.getX(), widget.getY() + widget.getHeight());
             GL11.glVertex2f(widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight());
             GL11.glEnd();
             GL11.glColor4f(1f, 1f, 1f, 1f);
             GL11.glLineWidth(1.0f);
             GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

    }

    private class LeftBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glLineWidth(border.getSize());
            GL11.glColor4f(border.getRed(), border.getGreen(), border.getBlue(), border.getAlpha());
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(widget.getX(), widget.getY());
            GL11.glVertex2f(widget.getX(), widget.getY() + widget.getHeight());
            GL11.glEnd();
            GL11.glColor4f(1f, 1f, 1f, 1f);
            GL11.glLineWidth(1.0f);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

    }

    private class RightBorderRenderer implements BorderVisitor {

        @Override
        public void visit(Widget widget, NullBorder border) {
        }

        @Override
        public void visit(Widget widget, ColoredBorder border) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glLineWidth(border.getSize());
            GL11.glColor4f(border.getRed(), border.getGreen(), border.getBlue(), border.getAlpha());
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(widget.getX() + widget.getWidth(), widget.getY());
            GL11.glVertex2f(widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight());
            GL11.glEnd();
            GL11.glColor4f(1f, 1f, 1f, 1f);
            GL11.glLineWidth(1.0f);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

    }

    public LwjglNuitRenderer(NuitTranslator translator, TrueTypeFont font) {
        this.translator = translator;
        this.font = font;
    }

    @Override
    public void render(Root root) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_TRANSFORM_BIT | GL11.GL_HINT_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_SCISSOR_BIT | GL11.GL_LINE_BIT | GL11.GL_TEXTURE_BIT);
        GL11.glViewport(0, 0, LwjglHelper.getWidth(), LwjglHelper.getHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(root.getX(), root.getWidth(), root.getHeight(), root.getY(), -1.0, 1.0);
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
        drawBackgroundAndBorder(root);
        drawStack(root);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void drawBackgroundAndBorder(Widget widget) {
        widget.getBackground().accept(widget, this);
        widget.getTopBorder().accept(widget, topBorderRenderer);
        widget.getBottomBorder().accept(widget, bottomBorderRenderer);
        widget.getLeftBorder().accept(widget, leftBorderRenderer);
        widget.getRightBorder().accept(widget, rightBorderRenderer);
    }

    @Override
    public void visit(Button widget) {
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        String translatedText = translator.getMessage(widget.getText());
        GL11.glTranslatef(widget.getX() + widget.getWidth() / 2.0f /*- font.getWidth(translatedText)/2.0f*/, widget.getY() + widget.getHeight() / 2.0f + font.getHeight(translatedText) / 2.0f, 0.0f);
        GL11.glScalef(1, -1, 1);
        font.drawString(translatedText, TrueTypeFont.Align.CENTER);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    @Override
    public void visit(Widget widget, ColoredBackground background) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(background.getRed(), background.getGreen(), background.getBlue(), background.getAlpha());
        GL11.glRectf(widget.getX(), widget.getY(), widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public void visit(Widget widget, NullBackground background) {
    }

    @Override
    public void visit(Widget widget, TexturedBackground background) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getBackgroundTextureId(background));
        float x1 = widget.getX();
        float x2 = widget.getX() + widget.getWidth();
        float y1 = widget.getY();
        float y2 = widget.getY() + widget.getHeight();
        float u1 = background.getU1();
        float v1 = background.getV1();
        float u2 = background.getU2();
        float v2 = background.getV2();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u1, v2);
        GL11.glVertex2f(x1, y2);
        GL11.glTexCoord2f(u2, v2);
        GL11.glVertex2f(x2, y2);
        GL11.glTexCoord2f(u2, v1);
        GL11.glVertex2f(x2, y1);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
    }

    @Override
    public void visit(Container widget) {
        drawContainer(widget);
    }

    @Override
    public void visit(Table widget) {
        drawContainer(widget);
    }

    private void drawContainer(Container widget) {
        Widget focused = widget.getFocusedChild();
        for (Widget child : widget.getChildren()) {
            Background background = child.getBackground();
            if (focused == child && null != child.getFocusedBackground()) {
                background = child.getFocusedBackground();
            }
            drawBackgroundAndBorder(child);
            child.accept(this);
        }
        if (null != focused) {
            drawFocus(widget, focused);
        }
    }

    private void drawFocus(Container container, Widget focused) {
        if (focused.mustDrawFocus()) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glLineWidth(2.0f);
            if (container.isFocusSucked()) {
                GL11.glColor3f(0.5f, 0.5f, 0.5f);
            }
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2f(focused.getX(), focused.getY());
            GL11.glVertex2f(focused.getX() + focused.getWidth(), focused.getY());
            GL11.glVertex2f(focused.getX() + focused.getWidth(), focused.getY() + focused.getHeight());
            GL11.glVertex2f(focused.getX(), focused.getY() + focused.getHeight());
            GL11.glVertex2f(focused.getX(), focused.getY());
            GL11.glEnd();
            GL11.glColor3f(1, 1, 1);
            GL11.glLineWidth(1.0f);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    @Override
    public void visit(ControlsConfigurator widget) {
        drawContainer(widget);
    }

    @Override
    public void visit(ControlsConfigurator.ControlConfigurator widget) {
        String text = widget.getText();

        if (null != text) {
            GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glPushMatrix();
            GL11.glTranslatef(widget.getX() + widget.getWidth() / 2.0f - font.getWidth(text) / 4.0f, widget.getY() + widget.getHeight() / 2.0f + font.getHeight(text) / 2.0f, 0.0f);
            GL11.glScalef(1, -1, 1);
            font.drawString(text);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }

    @Override
    public void visit(Label widget) {
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        String translatedText = translator.getMessage(widget.getText());
        GL11.glTranslatef(widget.getX() + widget.getWidth() / 2.0f - font.getWidth(translatedText) / 4.0f, widget.getY() + widget.getHeight() / 2.0f + font.getHeight(translatedText) / 2.0f, 0.0f);
        GL11.glScalef(1, -1, 1);
        font.drawString(translatedText);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    @Override
    public void visit(NullWidget widget) {
        for (Widget child : widget.getChildren()) {
            drawBackgroundAndBorder(child);
            child.accept(this);
        }
    }

    @Override
    public void visit(Select widget) {
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        String text = String.valueOf(widget.getSelected());
        GL11.glTranslatef(widget.getX() + widget.getWidth() / 2.0f - font.getWidth(text) / 4.0f, widget.getY() + widget.getHeight() / 2.0f + font.getHeight(text) / 2.0f, 0.0f);
        GL11.glScalef(1, -1, 1);
        font.drawString(text);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    @Override
    public void visit(Stack widget) {
        drawStack(widget);
    }

    private void drawStack(Stack widget) {
        Widget child = widget.getFocusedChild();
        if (null != child) {
            drawBackgroundAndBorder(child);
            child.accept(this);
        }
    }

    @Override
    public void visit(Toggle widget) {
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        String text = widget.getText();
        GL11.glTranslatef(widget.getX() + widget.getWidth() / 2.0f - font.getWidth(text) / 4.0f, widget.getY() + widget.getHeight() / 2.0f + font.getHeight(text) / 2.0f, 0.0f);
        GL11.glScalef(1, -1, 1);
        font.drawString(text);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    @Override
    public void visit(AudioConfigurator widget) {
        drawContainer(widget);
    }

    @Override
    public void visit(VideoConfigurator widget) {
        drawContainer(widget);
    }

    protected abstract int getBackgroundTextureId(TexturedBackground background);

}
