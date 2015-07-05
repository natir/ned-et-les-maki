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

import java.util.ArrayList;

import im.bci.jnuit.NuitToolkit;
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
import org.geekygoblin.nedetlesmaki.core.events.OneShotTrigger;
import org.geekygoblin.nedetlesmaki.core.events.ShowMenuTrigger;

/**
 *
 * @author devnewton
 */
@Singleton
public class LevelSelector extends Table {

	private final NedGame game;
	private final IAnimationCollection bulleAnimations;
	private final IAnimationCollection shiningFloorAnimations;
	private final NuitToolkit toolkit;
	private final Provider<IStartGameTrigger> startGameTrigger;
	private final Provider<ShowMenuTrigger> showMenuTrigger;
	private final Table buttonsTable;
	private final IAssets assets;

	private enum Tower {
		TOWER_1, TOWER_2;
	}

	private Tower tower;

	@Inject
	public LevelSelector(NedGame game, NuitToolkit toolkit, final IAssets assets, Provider<IStartGameTrigger> startGameTrigger, Provider<ShowMenuTrigger> showMenuTrigger) {
		super(toolkit);
		this.game = game;
		this.toolkit = toolkit;
		this.assets = assets;
		this.startGameTrigger = startGameTrigger;
		this.showMenuTrigger = showMenuTrigger;
		buttonsTable = this;
		buttonsTable.setX(0);
		buttonsTable.setY(0);
		buttonsTable.setWidth(320);
		buttonsTable.setHeight(VirtualResolution.HEIGHT);
		buttonsTable.defaults().expandY().align(TableLayout.CENTER);
		buttonsTable.columnDefaults(0).width(80.0f).pad(20.0f).padLeft(100.0f).spaceRight(50.0f);
		buttonsTable.columnDefaults(1).width(80.0f).pad(20.0f);
		bulleAnimations = assets.getAnimations("animation/bulle/bulle.json");
		shiningFloorAnimations = assets.getAnimations("animation/shinin_floor/shinin_floor.json");
		setupTower1();
	}

	private void toggleTower() {
		clearTower();
		switch (tower) {
		case TOWER_1:
			setupTower2();
			break;
		case TOWER_2:
			setupTower1();
			break;
		}
		buttonsTable.layout();
	}

	private void setupTower1() {
		tower = Tower.TOWER_1;
		setBackground(new TexturedBackground(assets.getAnimations("tower1.png").getFirst().start(PlayMode.LOOP)));
		buttonsTable.setFocusedChild(addButton("tower.02.level.01.name", "levels/t2/lvl01.tmx", 791, 540, 334, 116, "t1groundFloor"));
		addButton("tower.01.level.02.name", "levels/t1/lvl02.tmx", 791, 517, 334, 95, "t1firstFloor");
		buttonsTable.row();
		addButton("tower.01.level.03.name", "levels/t1/lvl03.tmx", 791, 494, 334, 95, "t1midFloors");
		addButton("tower.01.level.04.name", "levels/t1/lvl04.tmx", 791, 471, 334, 95, "t1midFloors");
		buttonsTable.row();
		addButton("tower.01.level.05.name", "levels/t1/lvl05.tmx", 791, 448, 334, 95, "t1midFloors");
		addButton("tower.01.level.06.name", "levels/t1/lvl06.tmx", 791, 425, 334, 95, "t1midFloors");
		buttonsTable.row();
		addButton("tower.01.level.07.name", "levels/t1/lvl07.tmx", 791, 402, 334, 95, "t1midFloors");
		addButton("tower.01.level.08.name", "levels/t1/lvl08.tmx", 791, 379, 334, 95, "t1midFloors");
		buttonsTable.row();
		addButton("tower.01.level.09.name", "levels/t1/lvl09.tmx", 791, 356, 334, 95, "t1midFloors");
		addButton("tower.01.level.10.name", "levels/t1/lvl10.tmx", 791, 333, 334, 95, "t1lastFloor");
		buttonsTable.row();

		IAnimation buttonSmallBackgroundAnimation = assets.getAnimations("animation/menu_buttons/menu_buttons.json").getAnimationByName("1_normal");
		IAnimation buttonSmallFocusedBackgroundAnimation = assets.getAnimations("animation/menu_buttons/menu_buttons.json").getAnimationByName("1_survol");

		final NullWidget portail = new NullWidget();
		portail.setBackground(new TexturedBackground(assets.getAnimations("animation/portail/portail.json").getAnimationByName("normal").start(PlayMode.LOOP)));
		portail.setFocusedBackground(new TexturedBackground(assets.getAnimations("animation/portail/portail.json").getAnimationByName("survol").start(PlayMode.LOOP)));
		portail.setX(526);
		portail.setY(383);
		portail.setWidth(131);
		portail.setHeight(199);
		buttonsTable.add(portail);

		ButtonWithBuddyAnimatableWidget backButton = new ButtonWithBuddyAnimatableWidget(toolkit, "options.menu.button.back") {
			@Override
			public void onOK() {
				LevelSelector.this.onCancel();
			}
		};
		backButton.setBuddy(portail);
		backButton.setFocusCursor(NullFocusCursor.INSTANCE);
		backButton.setBackground(new TexturedBackground(buttonSmallBackgroundAnimation.start(PlayMode.LOOP)));
		backButton.setFocusedBackground(new TexturedBackground(buttonSmallFocusedBackgroundAnimation.start(PlayMode.LOOP)));
		buttonsTable.cell(backButton).fill();

		ButtonWithBuddyAnimatableWidget toggleTowerButton = new ButtonWithBuddyAnimatableWidget(toolkit, "levelselector.button.toggle.tower") {
			@Override
			public void onOK() {
				game.addEntity(game.createEntity().addComponent(new Triggerable(new OneShotTrigger() {
					
					@Override
					public void process(NedGame game) {
						LevelSelector.this.toggleTower();
					}
				})));
			}
		};
		toggleTowerButton.setBuddy(portail);
		toggleTowerButton.setFocusCursor(NullFocusCursor.INSTANCE);
		toggleTowerButton.setBackground(new TexturedBackground(buttonSmallBackgroundAnimation.start(PlayMode.LOOP)));
		toggleTowerButton.setFocusedBackground(new TexturedBackground(buttonSmallFocusedBackgroundAnimation.start(PlayMode.LOOP)));
		buttonsTable.cell(toggleTowerButton).fill();
		buttonsTable.cell(new NullWidget()).expand();
	}

	private void setupTower2() {
		tower = Tower.TOWER_2;
		setBackground(new TexturedBackground(assets.getAnimations("tower2.png").getFirst().start(PlayMode.LOOP)));
		buttonsTable.setFocusedChild(addButton("tower.02.level.01.name", "levels/t2/lvl01.tmx", 871, 851, 174, 76, "t2groundFloor"));
		addButton("tower.02.level.02.name", "levels/t2/lvl02.tmx", 871, 828, 174, 55, "tmidFloors");
		buttonsTable.row();
		addButton("tower.02.level.03.name", "levels/t2/lvl03.tmx", 871, 805, 174, 55, "tmidFloors");
		addButton("tower.02.level.04.name", "levels/t2/lvl04.tmx", 871, 782, 174, 55, "tmidFloors");
		buttonsTable.row();
		addButton("tower.02.level.05.name", "levels/t2/lvl05.tmx", 871, 759, 174, 55, "tmidFloors");
		addButton("tower.02.level.06.name", "levels/t2/lvl06.tmx", 871, 736, 174, 55, "tmidFloors");
		buttonsTable.row();
		addButton("tower.02.level.07.name", "levels/t2/lvl07.tmx", 871, 690, 174, 55, "tmidFloors");
		addButton("tower.02.level.08.name", "levels/t2/lvl01.tmx", 871, 667, 174, 55, "tmidFloors");
		buttonsTable.row();
		addButton("tower.02.level.09.name", "levels/t2/lvl02.tmx", 871, 644, 174, 55, "tmidFloors");
		addButton("tower.02.level.10.name", "levels/t2/lvl03.tmx", 871, 621, 174, 55, "tmidFloors");
		buttonsTable.row();
		addButton("tower.02.level.11.name", "levels/t2/lvl04.tmx", 871, 598, 174, 55, "tmidFloors");
		addButton("tower.02.level.12.name", "levels/t2/lvl05.tmx", 871, 575, 174, 55, "tmidFloors");
		buttonsTable.row();
		addButton("tower.02.level.13.name", "levels/t2/lvlAA.tmx", 871, 552, 174, 55, "tmidFloors");
		addButton("tower.02.level.14.name", "levels/t2/lvlAB.tmx", 871, 529, 174, 55, "tmidFloors");
		buttonsTable.row();
		addButton("tower.02.level.29.name", "levels/test2020.tmx", 871, 207, 174, 55, "tmidFloors");
		addButton("tower.02.level.30.name", "levels/test.tmx", 871, 184, 174, 63, "t2lastFloor");
		buttonsTable.row();

		IAnimation buttonSmallBackgroundAnimation = assets.getAnimations("animation/menu_buttons/menu_buttons.json").getAnimationByName("1_normal");
		IAnimation buttonSmallFocusedBackgroundAnimation = assets.getAnimations("animation/menu_buttons/menu_buttons.json").getAnimationByName("1_survol");

		final NullWidget portail = new NullWidget();
		portail.setBackground(new TexturedBackground(assets.getAnimations("animation/portail/portail.json").getAnimationByName("normal").start(PlayMode.LOOP)));
		portail.setFocusedBackground(new TexturedBackground(assets.getAnimations("animation/portail/portail.json").getAnimationByName("survol").start(PlayMode.LOOP)));
		portail.setX(526);
		portail.setY(383);
		portail.setWidth(131);
		portail.setHeight(199);
		buttonsTable.add(portail);

		ButtonWithBuddyAnimatableWidget backButton = new ButtonWithBuddyAnimatableWidget(toolkit, "options.menu.button.back") {
			@Override
			public void onOK() {
				LevelSelector.this.onCancel();
			}
		};
		backButton.setBuddy(portail);
		backButton.setFocusCursor(NullFocusCursor.INSTANCE);
		backButton.setBackground(new TexturedBackground(buttonSmallBackgroundAnimation.start(PlayMode.LOOP)));
		backButton.setFocusedBackground(new TexturedBackground(buttonSmallFocusedBackgroundAnimation.start(PlayMode.LOOP)));
		buttonsTable.cell(backButton).fill();

		ButtonWithBuddyAnimatableWidget toggleTowerButton = new ButtonWithBuddyAnimatableWidget(toolkit, "levelselector.button.toggle.tower") {
			@Override
			public void onOK() {
				game.addEntity(game.createEntity().addComponent(new Triggerable(new OneShotTrigger() {
					
					@Override
					public void process(NedGame game) {
						LevelSelector.this.toggleTower();
					}
				})));			}
		};
		toggleTowerButton.setBuddy(portail);
		toggleTowerButton.setFocusCursor(NullFocusCursor.INSTANCE);
		toggleTowerButton.setBackground(new TexturedBackground(buttonSmallBackgroundAnimation.start(PlayMode.LOOP)));
		toggleTowerButton.setFocusedBackground(new TexturedBackground(buttonSmallFocusedBackgroundAnimation.start(PlayMode.LOOP)));
		buttonsTable.cell(toggleTowerButton).fill();
		buttonsTable.cell(new NullWidget()).expand();
	}

	private void clearTower() {
		buttonsTable.clear();
		ArrayList<Widget> oldChildren = new ArrayList<Widget>(getChildren());
		for (Widget w : oldChildren) {
			buttonsTable.remove(w);
		}
	}

	private class LevelSelectorButton extends ButtonWithBuddyAnimatableWidget {

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

	private LevelSelectorButton addButton(String label, String levelName, int buddyX, int buddyY, int buddyW, int buddyH, String buddyAnimation) {
		LevelSelectorButton button = addButton(label, levelName);
		NullWidget buddy = new NullWidget();
		buddy.setX(buddyX);
		buddy.setY(buddyY);
		buddy.setWidth(buddyW);
		buddy.setHeight(buddyH);
		buddy.setFocusedBackground(new TexturedBackground(shiningFloorAnimations.getAnimationByName(buddyAnimation).start(PlayMode.LOOP)));
		add(buddy);
		button.setBuddy(buddy);
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
