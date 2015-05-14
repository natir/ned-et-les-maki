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
package org.geekygoblin.nedetlesmaki.core.components.ui;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.widgets.Button;
import im.bci.jnuit.widgets.NullWidget;
import im.bci.jnuit.widgets.Table;
import im.bci.jnuit.widgets.TableLayout;
import im.bci.jnuit.widgets.Widget;
import im.bci.jnuit.animation.IAnimation;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.focus.NullFocusCursor;

import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import org.geekygoblin.nedetlesmaki.core.components.Triggerable;
import org.geekygoblin.nedetlesmaki.core.constants.VirtualResolution;
import org.geekygoblin.nedetlesmaki.core.events.IStartGameTrigger;
import org.geekygoblin.nedetlesmaki.core.events.ShowMenuTrigger;

/**
 *
 * @author devnewton
 */
@Singleton
public class LevelSelector extends Table {

    private final NedGame game;
    private final IAnimationCollection bulleAnimations;
    private final NuitToolkit toolkit;
    private final Provider<IStartGameTrigger> startGameTrigger;
    private final Provider<ShowMenuTrigger> showMenuTrigger;
    private final Table buttonsTable;
    private NullWidget portail;
    private Button backButton;
    private TexturedBackground portailFocusedBackground;
    private TexturedBackground portailBackground;

    @Inject
    public LevelSelector(NedGame game, NuitToolkit toolkit, final IAssets assets, Provider<IStartGameTrigger> startGameTrigger, Provider<ShowMenuTrigger> showMenuTrigger) {
        super(toolkit);
        this.game = game;
        this.toolkit = toolkit;
        this.startGameTrigger = startGameTrigger;
        this.showMenuTrigger = showMenuTrigger;
        buttonsTable = this;
        buttonsTable.setX(0);
        buttonsTable.setY(0);
        buttonsTable.setWidth(320);
        buttonsTable.setHeight(VirtualResolution.HEIGHT);
        buttonsTable.defaults().expandY().align(TableLayout.CENTER);
        buttonsTable.columnDefaults(0).width(80.0f).pad(20.0f);
        buttonsTable.columnDefaults(1).width(80.0f).pad(20.0f);
        bulleAnimations = assets.getAnimations("animation/bulle/bulle.json");
        setBackground(new TexturedBackground(assets.getAnimations("tour.png").getFirst().start(PlayMode.LOOP)));
        buttonsTable.setFocusedChild(addButton("level.01.name", "levels/t1/lvl01.tmx"));
        addButton("level.02.name", "levels/t1/lvl02.tmx");
        buttonsTable.row();
        addButton("level.03.name", "levels/t1/lvl03.tmx");
        addButton("level.04.name", "levels/t1/lvl04.tmx");
        buttonsTable.row();
        addButton("level.05.name", "levels/t1/lvl05.tmx");
        addButton("level.06.name", "levels/t1/lvl06.tmx");
        buttonsTable.row();
        addButton("level.07.name", "levels/t1/lvl07.tmx");
        addButton("level.08.name", "levels/t2/lvl01.tmx");
        buttonsTable.row();
        addButton("level.09.name", "levels/t2/lvl02.tmx");
        addButton("level.10.name", "levels/t2/lvl03.tmx");
        buttonsTable.row();
        addButton("level.11.name", "levels/t2/lvl04.tmx");
        addButton("level.12.name", "levels/t2/lvl05.tmx");
        buttonsTable.row();
        addButton("level.13.name", "levels/t2/lvlAA.tmx");
        addButton("level.14.name", "levels/t2/lvlAB.tmx");
        buttonsTable.row();
        addButton("level.29.name", "levels/test2020.tmx");
        addButton("level.30.name", "levels/test.tmx");
        buttonsTable.row();

        IAnimation buttonSmallBackgroundAnimation = assets.getAnimations("animation/menu_buttons/menu_buttons.json").getAnimationByName("1_normal");
        IAnimation buttonSmallFocusedBackgroundAnimation = assets.getAnimations("animation/menu_buttons/menu_buttons.json").getAnimationByName("1_survol");

        backButton = new Button(toolkit, "options.menu.button.back") {
            @Override
            public void onOK() {
                LevelSelector.this.onCancel();
            }
        };
        backButton.setFocusCursor(NullFocusCursor.INSTANCE);
        backButton.setBackground(new TexturedBackground(buttonSmallBackgroundAnimation.start(PlayMode.LOOP)));
        backButton.setFocusedBackground(new TexturedBackground(buttonSmallFocusedBackgroundAnimation.start(PlayMode.LOOP)));
        buttonsTable.cell(backButton).colspan(2).fill();
        buttonsTable.cell(new NullWidget()).expand();

        portail = new NullWidget();
        portailBackground = new TexturedBackground(assets.getAnimations("animation/portail/portail.json").getAnimationByName("normal").start(PlayMode.LOOP));
        portailFocusedBackground = new TexturedBackground(assets.getAnimations("animation/portail/portail.json").getAnimationByName("survol").start(PlayMode.LOOP));
        portail.setBackground(portailBackground);
        portail.setX(526);
        portail.setY(383);
        portail.setWidth(131);
        portail.setHeight(199);
        buttonsTable.add(portail);

    }

    @Override
    public void setFocusedChild(Widget focusedChild) {
        super.setFocusedChild(focusedChild);
        if (null != portail) {
            if (focusedChild == backButton) {
                portail.setBackground(portailFocusedBackground);
            } else {
                portail.setBackground(portailBackground);
            }
        }
    }

    private class LevelSelectorButton extends Button {

        private final String levelName;

        public LevelSelectorButton(NuitToolkit toolkit, String text, String levelName) {
            super(toolkit, text);
            setFocusCursor(NullFocusCursor.INSTANCE);
            this.levelName = levelName;
        }

        @Override
        public void onOK() {
            onStartGame(levelName);
        }
    }

    private LevelSelectorButton addButton(String label, String levelName) {
        LevelSelectorButton button = new LevelSelectorButton(toolkit, label, levelName);
        final TexturedBackground background = new TexturedBackground(bulleAnimations.getAnimationByName("bulle").start(PlayMode.LOOP));
        button.setBackground(background);
        final TexturedBackground focusedBackground = new TexturedBackground(bulleAnimations.getAnimationByName("bulle_selectionnee").start(PlayMode.LOOP));
        button.setFocusedBackground(focusedBackground);
        buttonsTable.cell(button);
        return button;
    }

    private void onStartGame(String levelName) {
        game.setCurrentLevel(levelName);
        game.addEntity(game.createEntity().addComponent(new Triggerable(startGameTrigger.get().withLevelName(levelName))));
    }

    @Override
    public void onCancel() {
        game.addEntity(game.createEntity().addComponent(new Triggerable(showMenuTrigger.get())));
    }

}
