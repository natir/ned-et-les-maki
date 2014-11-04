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

import javax.inject.Inject;
import javax.inject.Singleton;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.background.ColoredBackground;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import org.geekygoblin.nedetlesmaki.core.constants.VirtualResolution;

/**
 *
 * @author devnewton
 */
@Singleton
public class CutScenes {

    private final IAssets assets;

    @Inject
    public CutScenes(IAssets assets) {
        this.assets = assets;
    }

    public void createCredits(NedDialogue dialog) {
        assets.clearUseless();
        IAnimationCollection animations = assets.getAnimations("animation/devnewton/devnewton.json");
        IAnimationCollection natir_credit = assets.getAnimations("animation/natir/natir.json");
        dialog.setBackground(new ColoredBackground(0, 0, 0, 1));
        dialog.addTirade(animations.getAnimationByName("devnewton").start(PlayMode.ONCE), (VirtualResolution.WIDTH - 512) / 2, (VirtualResolution.HEIGHT - 128) / 2, 512, 128, "dialog.credits.devnewton");
        dialog.addTirade(natir_credit.getAnimationByName("natir").start(PlayMode.ONCE), (VirtualResolution.WIDTH - 600) / 2, (VirtualResolution.HEIGHT - 360) / 2, 600, 360, "dialog.credits.natir");
    }

    public void createIntro(NedDialogue dialog) {
        assets.clearUseless();
        IAnimationCollection animations = assets.getAnimations("animation/intro/intro.json");
        dialog.addTirade(animations.getAnimationByName("01").start(PlayMode.LOOP), "dialog.intro.01");
        dialog.addTirade(animations.getAnimationByName("02").start(PlayMode.LOOP), "dialog.intro.02");
        dialog.addTirade(animations.getAnimationByName("03").start(PlayMode.LOOP), "dialog.intro.03");
        dialog.addTirade(animations.getAnimationByName("04").start(PlayMode.LOOP), "dialog.intro.04");
        dialog.addTirade(animations.getAnimationByName("05").start(PlayMode.LOOP), "dialog.intro.05");
        dialog.addTirade(animations.getAnimationByName("06").start(PlayMode.LOOP), "dialog.intro.06");
        dialog.addTirade(animations.getAnimationByName("07").start(PlayMode.LOOP), "dialog.intro.07");
        dialog.addTirade(animations.getAnimationByName("08").start(PlayMode.LOOP), "dialog.intro.08");
        dialog.addTirade(animations.getAnimationByName("09").start(PlayMode.LOOP), "dialog.intro.09");
        dialog.addTirade(animations.getAnimationByName("10").start(PlayMode.LOOP), "dialog.intro.10");
    }
}
