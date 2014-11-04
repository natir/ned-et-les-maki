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
import im.bci.jnuit.NuitControls;
import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.focus.NullFocusCursor;
import im.bci.jnuit.widgets.Container;
import im.bci.jnuit.widgets.Root;
import im.bci.jnuit.widgets.Widget;
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
    private final InGameButton reset, rewind, showMenu;
    private final Container container;

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
        container = new Container() {

            @Override
            public void onMouseMove(float mouseX, float mouseY) {
                setFocusedChild(null);
                super.onMouseMove(mouseX, mouseY);
            }

            @Override
            public boolean isFocusSucked() {
                return false;
            }

            @Override
            protected Widget getTopLeftFocusableChild() {
                return null;
            }

        };
        rewind = new InGameButton(toolkit, "");
        rewind.setFocusCursor(NullFocusCursor.INSTANCE);
        final IAnimationCollection buttonAnimations = assets.getAnimations("animation/ingame_buttons/ingame_buttons.json");
        rewind.setBackground(new TexturedBackground(buttonAnimations.getAnimationByName("rewind_button").start(PlayMode.LOOP)));
        rewind.setFocusedBackground(new TexturedBackground(buttonAnimations.getAnimationByName("rewind_button_focused").start(PlayMode.LOOP)));
        rewind.setX(1695);
        rewind.setY(275);
        rewind.setWidth(150);
        rewind.setHeight(150);
        container.add(rewind);

        reset = new InGameButton(toolkit, "");
        reset.setFocusCursor(NullFocusCursor.INSTANCE);
        reset.setBackground(new TexturedBackground(buttonAnimations.getAnimationByName("reset_button").start(PlayMode.LOOP)));
        reset.setFocusedBackground(new TexturedBackground(buttonAnimations.getAnimationByName("reset_button_focused").start(PlayMode.LOOP)));
        reset.setX(1695);
        reset.setY(75);
        reset.setWidth(150);
        reset.setHeight(150);
        container.add(reset);

        showMenu = new InGameButton(toolkit, "");
        showMenu.setFocusCursor(NullFocusCursor.INSTANCE);
        showMenu.setBackground(new TexturedBackground(buttonAnimations.getAnimationByName("show_menu_button").start(PlayMode.LOOP)));
        showMenu.setFocusedBackground(new TexturedBackground(buttonAnimations.getAnimationByName("show_menu_button_focused").start(PlayMode.LOOP)));
        showMenu.setX(75);
        showMenu.setY(75);
        showMenu.setWidth(150);
        showMenu.setHeight(150);
        container.add(showMenu);

        root.add(container);
    }

    public InGameButton getReset() {
        return reset;
    }

    public InGameButton getRewind() {
        return rewind;
    }

    public InGameButton getShowMenu() {
        return showMenu;
    }

    public boolean isMouseHoverHoverAButton() {
        return null != container.getFocusedChild();
    }

    public void update(float delta) {
        root.update(delta);
    }

    public Root getRoot() {
        return root;
    }
}
