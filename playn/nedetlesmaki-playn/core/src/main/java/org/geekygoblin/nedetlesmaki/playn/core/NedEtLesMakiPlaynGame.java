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
package org.geekygoblin.nedetlesmaki.playn.core;

import com.artemis.Entity;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import im.bci.jnuit.NuitAudio;
import im.bci.jnuit.NuitFont;
import im.bci.jnuit.NuitRenderer;
import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.NuitTranslator;
import im.bci.jnuit.playn.PlaynNuitAudio;
import im.bci.jnuit.playn.PlaynNuitDisplay;
import im.bci.jnuit.playn.PlaynNuitFont;
import im.bci.jnuit.playn.PlaynNuitRenderer;
import im.bci.jnuit.playn.controls.PlaynNuitControls;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import org.geekygoblin.nedetlesmaki.core.IDefaultControls;
import org.geekygoblin.nedetlesmaki.core.IMainLoop;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.NedNuitToolkit;
import org.geekygoblin.nedetlesmaki.core.NedNuitTranslator;
import org.geekygoblin.nedetlesmaki.core.events.ShowLevelMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.events.ShowMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.manager.EntityIndexManager;
import org.geekygoblin.nedetlesmaki.core.systems.GameSystem;
import org.geekygoblin.nedetlesmaki.core.systems.IngameInputSystem;
import org.geekygoblin.nedetlesmaki.core.systems.UpdateLevelVisualSystem;
import org.geekygoblin.nedetlesmaki.game.systems.PlaynDrawSystem;
import org.geekygoblin.nedetlesmaki.game.systems.PlaynMainMenuSystem;
import playn.core.Font;
import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;

public class NedEtLesMakiPlaynGame extends Game.Default {

    private IMainLoop mainLoop;

    public NedEtLesMakiPlaynGame() {
        super(1000 / 60);
    }

    @Override
    public void init() {

        /*PlaynNedModule module = new PlaynNedModule();
         Injector injector = Guice.createInjector(module);
         mainLoop = injector.getInstance(IMainLoop.class);
         */
        mainLoop = new NedFactory().getMainLoop();
    }

    @Override
    public void update(int delta) {
        mainLoop.tick();
    }

    @Override
    public void paint(float alpha) {
        // the background automatically paints itself, so no need to do anything here!
    }
}
