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

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.animation.IAnimation;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.background.TexturedBackground;
import im.bci.jnuit.focus.NullFocusCursor;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import im.bci.jnuit.widgets.Button;
import im.bci.jnuit.widgets.Container;
import im.bci.jnuit.widgets.Root;
import im.bci.jnuit.widgets.Widget;


/**
 *
 * @author devnewton
 */
public class ExtrasMenu extends Container {
    
    public ExtrasMenu(NuitToolkit toolkit, final Root root, final Widget mainMenu, IAssets assets, CutScenes cutscenes) {
        
        final CutScenesMenu cutscenesMenu = new CutScenesMenu(toolkit, root, this, assets, cutscenes);
        root.add(cutscenesMenu);
        
        setBackground(new TexturedBackground(assets.getAnimations("menu_extras.png").getFirst().start(PlayMode.LOOP)));
        IAnimation buttonClassicBackgroundAnimation = assets.getAnimations("animation/menu_buttons/menu_buttons.json").getAnimationByName("2_normal");
        IAnimation buttonClassicFocusedBackgroundAnimation = assets.getAnimations("animation/menu_buttons/menu_buttons.json").getAnimationByName("2_survol");
        IAnimation buttonSmallBackgroundAnimation = assets.getAnimations("animation/menu_buttons/menu_buttons.json").getAnimationByName("1_normal");
        IAnimation buttonSmallFocusedBackgroundAnimation = assets.getAnimations("animation/menu_buttons/menu_buttons.json").getAnimationByName("1_survol");

        final Button cutscenesButton = new Button(toolkit, "extras.menu.button.cutscenes") {
            @Override
            public void onOK() {
                root.show(cutscenesMenu);
            }
        };
        cutscenesButton.setX(237);
        cutscenesButton.setY(426);
        cutscenesButton.setWidth(396);
        cutscenesButton.setHeight(92);
        cutscenesButton.setBackground(new TexturedBackground(buttonSmallBackgroundAnimation.start(PlayMode.LOOP)));
        cutscenesButton.setFocusedBackground(new TexturedBackground(buttonSmallFocusedBackgroundAnimation.start(PlayMode.LOOP)));
        cutscenesButton.setFocusCursor(NullFocusCursor.INSTANCE);
        add(cutscenesButton);

        final Button artworkButton = new Button(toolkit, "extras.menu.button.artwork") {
            @Override
            public void onOK() {
                root.show(mainMenu);
            }
        };
        artworkButton.setX(237);
        artworkButton.setY(547);
        artworkButton.setWidth(396);
        artworkButton.setHeight(92);
        artworkButton.setBackground(new TexturedBackground(buttonSmallBackgroundAnimation.start(PlayMode.LOOP)));
        artworkButton.setFocusedBackground(new TexturedBackground(buttonSmallFocusedBackgroundAnimation.start(PlayMode.LOOP)));
        artworkButton.setFocusCursor(NullFocusCursor.INSTANCE);
        add(artworkButton);

        final Button backButton = new Button(toolkit, "extras.menu.button.back") {
            @Override
            public void onOK() {
                root.show(mainMenu);
            }
        };
        backButton.setX(237);
        backButton.setY(700);
        backButton.setWidth(396);
        backButton.setHeight(92);
        backButton.setBackground(new TexturedBackground(buttonSmallBackgroundAnimation.start(PlayMode.LOOP)));
        backButton.setFocusedBackground(new TexturedBackground(buttonSmallFocusedBackgroundAnimation.start(PlayMode.LOOP)));
        backButton.setFocusCursor(NullFocusCursor.INSTANCE);
        add(backButton);
    }

}
