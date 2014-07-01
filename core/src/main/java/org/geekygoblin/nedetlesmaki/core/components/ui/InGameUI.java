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
package org.geekygoblin.nedetlesmaki.core.components.ui;

import com.artemis.Component;
import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.controls.Action;
import im.bci.jnuit.controls.ActionActivatedDetector;
import im.bci.jnuit.focus.NullFocusCursor;
import im.bci.jnuit.widgets.Container;
import im.bci.jnuit.widgets.Root;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.geekygoblin.nedetlesmaki.core.IAssets;

/**
 *
 * @author devnewton
 */
@Singleton
public class InGameUI extends Component {

    private final Root root;
    private final ActionActivatedDetector reset, rewind, showMenu;

    @Inject
    public InGameUI(NuitToolkit toolkit, IAssets assets) {
        root = new Root(toolkit) {

            @Override
            public void onDown() {
            }

            @Override
            public void onLeft() {
            }

            @Override
            public void onOK() {
            }

            @Override
            public void onRight() {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onUp() {
            }

        };
        Container container = new Container();
        InGameButton rewindButton = new InGameButton(toolkit, "");
        rewindButton.setFocusCursor(NullFocusCursor.INSTANCE);
        final IAnimationCollection buttonAnimations = assets.getAnimations("ingame_buttons.json");
        rewindButton.setBackground(new TexturedBackground(buttonAnimations.getAnimationByName("rewind_button").start(PlayMode.LOOP)));
        rewindButton.setFocusedBackground(new TexturedBackground(buttonAnimations.getAnimationByName("rewind_button_focused").start(PlayMode.LOOP)));
        rewindButton.setX(1695);
        rewindButton.setY(275);
        rewindButton.setWidth(150);
        rewindButton.setHeight(150);
        container.add(rewindButton);
        rewind = new ActionActivatedDetector(new Action("rewind", rewindButton));

        InGameButton resetButton = new InGameButton(toolkit, "");
        resetButton.setFocusCursor(NullFocusCursor.INSTANCE);
        resetButton.setBackground(new TexturedBackground(buttonAnimations.getAnimationByName("reset_button").start(PlayMode.LOOP)));
        resetButton.setFocusedBackground(new TexturedBackground(buttonAnimations.getAnimationByName("reset_button_focused").start(PlayMode.LOOP)));
        resetButton.setX(1695);
        resetButton.setY(75);
        resetButton.setWidth(150);
        resetButton.setHeight(150);
        container.add(resetButton);
        reset = new ActionActivatedDetector(new Action("reset", resetButton));

        InGameButton showMenuButton = new InGameButton(toolkit, "");
        showMenuButton.setFocusCursor(NullFocusCursor.INSTANCE);
        showMenuButton.setBackground(new TexturedBackground(buttonAnimations.getAnimationByName("show_menu_button").start(PlayMode.LOOP)));
        showMenuButton.setFocusedBackground(new TexturedBackground(buttonAnimations.getAnimationByName("show_menu_button_focused").start(PlayMode.LOOP)));
        showMenuButton.setX(75);
        showMenuButton.setY(75);
        showMenuButton.setWidth(150);
        showMenuButton.setHeight(150);
        container.add(showMenuButton);
        showMenu = new ActionActivatedDetector(new Action("show_menu", showMenuButton));

        root.add(container);
    }

    public ActionActivatedDetector getReset() {
        return reset;
    }

    public ActionActivatedDetector getRewind() {
        return rewind;
    }

    public ActionActivatedDetector getShowMenu() {
        return showMenu;
    }

    public void update(float delta) {
        root.update(delta);
    }

    public Root getRoot() {
        return root;
    }
}
