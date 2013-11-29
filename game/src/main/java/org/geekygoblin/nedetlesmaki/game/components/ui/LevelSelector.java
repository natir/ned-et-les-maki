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
package org.geekygoblin.nedetlesmaki.game.components.ui;

import im.bci.lwjgl.nuit.NuitToolkit;
import im.bci.lwjgl.nuit.widgets.Button;
import im.bci.lwjgl.nuit.widgets.Container;
import im.bci.lwjgl.nuit.widgets.Widget;
import im.bci.nanim.IAnimationCollection;
import im.bci.nanim.IAnimationFrame;
import im.bci.nanim.IPlay;
import im.bci.nanim.PlayMode;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.components.Triggerable;
import org.geekygoblin.nedetlesmaki.game.events.StartGameTrigger;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
@Singleton
public class LevelSelector extends Container {

    private final Game game;
    private final IAnimationCollection bulleAnimations;
    private final NuitToolkit toolkit;
    private final Assets assets;
    private final Provider<StartGameTrigger> startGameTrigger;

    @Inject
    public LevelSelector(Game game, NuitToolkit toolkit, Assets assets, Provider<StartGameTrigger> startGameTrigger) {
        this.game = game;
        this.toolkit = toolkit;
        this.assets = assets;
        this.startGameTrigger = startGameTrigger;
        bulleAnimations = assets.getAnimations("bulle.nanim.gz");
        setFocusedChild(addButton("level.01.name", "levels/lvl01.tmx", 725, 695, 1));
        addButton("level.02.name", "levels/lvl02.tmx", 550, 674, -1);
        addButton("level.03.name", "levels/lvl03.tmx", 725, 653, 1);
        addButton("level.04.name", "levels/lvl04.tmx", 550, 632, -1);
        addButton("level.05.name", "levels/lvl05.tmx", 725, 611, 1);
        addButton("level.06.name", "levels/lvl06.tmx", 550, 590, -1);
        addButton("level.07.name", "levels/lvl07.tmx", 725, 569, 1);
        addButton("level.08.name", "levels/lvl08.tmx", 550, 548, -1);
        addButton("level.09.name", "levels/lvl09.tmx", 725, 527, 1);
        addButton("level.10.name", "levels/lvl10.tmx", 550, 506, -1);
        addButton("level.11.name", "levels/lvl11.tmx", 725, 485, 1);
        addButton("level.12.name", "levels/lvl12.tmx", 550, 464, -1);
        addButton("level.13.name", "levels/lvl13.tmx", 725, 443, 1);
        addButton("level.14.name", "levels/lvl14.tmx", 550, 422, -1);
        addButton("level.15.name", "levels/lvl15.tmx", 725, 401, 1);
        addButton("level.16.name", "levels/lvl16.tmx", 550, 380, -1);
        addButton("level.17.name", "levels/lvl17.tmx", 725, 359, 1);
        addButton("level.18.name", "levels/lvl18.tmx", 550, 338, -1);
        addButton("level.19.name", "levels/lvl19.tmx", 725, 317, 1);
        addButton("level.20.name", "levels/lvl20.tmx", 550, 296, -1);
        addButton("level.21.name", "levels/lvl21.tmx", 725, 275, 1);
        addButton("level.22.name", "levels/lvl22.tmx", 550, 254, -1);
        addButton("level.23.name", "levels/lvl23.tmx", 725, 233, 1);
        addButton("level.24.name", "levels/lvl24.tmx", 550, 212, -1);
        addButton("level.25.name", "levels/lvl25.tmx", 725, 191, 1);
        addButton("level.26.name", "levels/lvl26.tmx", 550, 170, -1);
        addButton("level.27.name", "levels/lvl27.tmx", 725, 149, 1);
        addButton("level.28.name", "levels/lvl28.tmx", 550, 129, -1);
        addButton("level.29.name", "levels/lvl29.tmx", 725, 107, 1);
        addButton("level.30.name", "levels/test.tmx", 550, 87, -1);
    }

    @Override
    public void draw() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, assets.getTexture("tour.png").getId());
        float x1 = 0;
        float x2 = getWidth();
        float y1 = 0;
        float y2 = getHeight();
        float u1 = 0;
        float v1 = 1;
        float u2 = 1;
        float v2 = 0;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x1, y2);
        GL11.glTexCoord2f(u2, v1);
        GL11.glVertex2f(x2, y2);
        GL11.glTexCoord2f(u2, v2);
        GL11.glVertex2f(x2, y1);
        GL11.glTexCoord2f(u1, v2);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
        super.draw();
    }

    @Override
    protected void drawFocus(Widget focused) {
        //NOTHING
    }

    @Override
    protected final void setFocusedChild(Widget focusedChild) {
        Widget oldFocusedChild = getFocusedChild();
        if (oldFocusedChild instanceof LevelSelectorButton) {
            ((LevelSelectorButton) oldFocusedChild).backgroundAnimationPlay = bulleAnimations.getAnimationByName("bulle").start(PlayMode.LOOP);
        }
        if (focusedChild instanceof LevelSelectorButton) {
            ((LevelSelectorButton) focusedChild).backgroundAnimationPlay = bulleAnimations.getAnimationByName("bulle_selectionnee").start(PlayMode.LOOP);
        }
        super.setFocusedChild(focusedChild);
    }

    private class LevelSelectorButton extends Button {

        IPlay backgroundAnimationPlay;
        private final int orientation;
        private final String levelName;

        public LevelSelectorButton(NuitToolkit toolkit, String text, String levelName, int orientation) {
            super(toolkit, text);
            this.orientation = orientation;
            this.levelName = levelName;
        }

        @Override
        public void onOK() {
            onStartGame(levelName);
        }

        @Override
        public void update() {
            backgroundAnimationPlay.update((long) (game.getDelta() * 1000L));
            super.update();
        }

        @Override
        public void draw() {
            final IAnimationFrame currentFrame = backgroundAnimationPlay.getCurrentFrame();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentFrame.getImage().getId());
            float x1 = getX();
            float x2 = x1 + getWidth();
            float y1 = getY();
            float y2 = y1 + getHeight();
            float u1;
            float u2;
            if (orientation < 0) {
                u1 = currentFrame.getU1();
                u2 = currentFrame.getU2();
            } else {
                u1 = currentFrame.getU2();
                u2 = currentFrame.getU1();
            }
            float v1 = currentFrame.getV2();
            float v2 = currentFrame.getV1();
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(u1, v1);
            GL11.glVertex2f(x1, y2);
            GL11.glTexCoord2f(u2, v1);
            GL11.glVertex2f(x2, y2);
            GL11.glTexCoord2f(u2, v2);
            GL11.glVertex2f(x2, y1);
            GL11.glTexCoord2f(u1, v2);
            GL11.glVertex2f(x1, y1);
            GL11.glEnd();
            super.draw();
        }

    }

    private LevelSelectorButton addButton(String label, String levelName, int x, int y, int orientation) {
        LevelSelectorButton button = new LevelSelectorButton(toolkit, label, levelName, orientation);
        button.backgroundAnimationPlay = bulleAnimations.getAnimationByName("bulle").start(PlayMode.LOOP);
        button.setWidth(button.getMinWidth() * 1.8f);
        button.setHeight(button.getMinHeight());
        if (orientation < 0) {
            button.setX(x - button.getWidth());
        } else {
            button.setX(x);
        }
        button.setY(y - button.getHeight() / 2);
        this.add(button);
        return button;
    }

    private void onStartGame(String levelName) {
        game.addEntity(game.createEntity().addComponent(new Triggerable(startGameTrigger.get().withLevelName(levelName))));
    }
}
