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

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.widgets.Button;
import im.bci.jnuit.widgets.Container;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import im.bci.jnuit.background.TexturedBackground;
import org.geekygoblin.nedetlesmaki.game.Game;
import im.bci.jnuit.lwjgl.assets.IAssets;
import org.geekygoblin.nedetlesmaki.game.components.Triggerable;
import org.geekygoblin.nedetlesmaki.game.events.ShowMenuTrigger;
import org.geekygoblin.nedetlesmaki.game.events.StartGameTrigger;

/**
 *
 * @author devnewton
 */
@Singleton
public class LevelSelector extends Container {

    private final Game game;
    private final IAnimationCollection bulleAnimations;
    private final NuitToolkit toolkit;
    private final Provider<StartGameTrigger> startGameTrigger;
    private final Provider<ShowMenuTrigger> showMenuTrigger;

    @Inject
    public LevelSelector(Game game, NuitToolkit toolkit, final IAssets assets, Provider<StartGameTrigger> startGameTrigger, Provider<ShowMenuTrigger> showMenuTrigger) {
        this.game = game;
        this.toolkit = toolkit;
        this.startGameTrigger = startGameTrigger;
        this.showMenuTrigger = showMenuTrigger;
        bulleAnimations = assets.getAnimations("bulle.nanim.gz");
        setBackground(new TexturedBackground(assets.getAnimations("tour.png").getFirst().start(PlayMode.LOOP)));
        setFocusedChild(addButton("level.01.name", "levels/t1/lvl01.tmx", 1087, 938, 1));
        addButton("level.02.name", "levels/t1/lvl02.tmx", 825, 910, -1);
        addButton("level.03.name", "levels/t1/lvl03.tmx", 1087, 881, 1);
        addButton("level.04.name", "levels/t1/lvl04.tmx", 825, 853, -1);
        addButton("level.05.name", "levels/t2/lvl01.tmx", 1087, 825, 1);
        addButton("level.06.name", "levels/t2/lvl02.tmx", 825, 796, -1);
        addButton("level.07.name", "levels/t2/lvl03.tmx", 1087, 768, 1);
        addButton("level.08.name", "levels/t2/lvl04.tmx", 825, 740, -1);
        addButton("level.09.name", "levels/t2/lvl05.tmx", 1087, 711, 1);
        /*addButton("level.10.name", "levels/lvl10.tmx", 550, 506, -1);
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
         addButton("level.29.name", "levels/lvl29.tmx", 725, 107, 1);*/
        addButton("level.30.name", "levels/test.tmx", 825, 117, -1);

        Button backButton = new Button(toolkit, "options.menu.button.back") {

            @Override
            public void onOK() {
                LevelSelector.this.onCancel();
            }

        };
        backButton.setMustDrawFocus(false);
        backButton.setBackground(new TexturedBackground(assets.getAnimations("portail.nanim.gz").getAnimationByName("normal").start(PlayMode.LOOP)));
        backButton.setFocusedBackground(new TexturedBackground(assets.getAnimations("portail.nanim.gz").getAnimationByName("survol").start(PlayMode.LOOP)));
        backButton.setX(555);
        backButton.setY(406);
        backButton.setWidth(195);
        backButton.setHeight(267);
        this.add(backButton);
    }

    private class LevelSelectorButton extends Button {

        private final String levelName;

        public LevelSelectorButton(NuitToolkit toolkit, String text, String levelName) {
            super(toolkit, text);
            setMustDrawFocus(false);
            this.levelName = levelName;
        }

        @Override
        public void onOK() {
            onStartGame(levelName);
        }
    }

    private LevelSelectorButton addButton(String label, String levelName, int x, int y, int orientation) {
        LevelSelectorButton button = new LevelSelectorButton(toolkit, label, levelName);
        final TexturedBackground background = new TexturedBackground(bulleAnimations.getAnimationByName("bulle").start(PlayMode.LOOP));
        button.setBackground(background);
        final TexturedBackground focusedBackground = new TexturedBackground(bulleAnimations.getAnimationByName("bulle_selectionnee").start(PlayMode.LOOP));
        button.setFocusedBackground(focusedBackground);
        button.setWidth(button.getMinWidth() * 1.8f);
        button.setHeight(button.getMinHeight());
        if (orientation < 0) {
            button.setX(x - button.getWidth());
        } else {
            button.setX(x);
            background.setMirrorX(true);
            focusedBackground.setMirrorX(true);
        }
        button.setY(y - button.getHeight() / 2);
        this.add(button);
        return button;
    }

    private void onStartGame(String levelName) {
        game.addEntity(game.createEntity().addComponent(new Triggerable(startGameTrigger.get().withLevelName(levelName))));
    }

    @Override
    public void onCancel() {
        game.addEntity(game.createEntity().addComponent(new Triggerable(showMenuTrigger.get())));
    }

}
