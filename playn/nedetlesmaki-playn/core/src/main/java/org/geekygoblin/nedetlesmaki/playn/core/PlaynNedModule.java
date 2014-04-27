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

import org.geekygoblin.nedetlesmaki.core.IDefaultControls;
import org.geekygoblin.nedetlesmaki.core.IMainLoop;
import org.geekygoblin.nedetlesmaki.core.NamedEntities;
import org.geekygoblin.nedetlesmaki.core.NedNuitTranslator;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import com.artemis.Entity;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import im.bci.jnuit.NuitToolkit;

import java.util.Random;

import com.google.inject.Singleton;
import im.bci.jnuit.NuitAudio;
import im.bci.jnuit.NuitControls;
import im.bci.jnuit.NuitDisplay;
import im.bci.jnuit.NuitFont;
import im.bci.jnuit.NuitPreferences;
import im.bci.jnuit.NuitRenderer;
import im.bci.jnuit.NuitTranslator;
import im.bci.jnuit.playn.PlaynNuitAudio;
import im.bci.jnuit.playn.PlaynNuitDisplay;
import im.bci.jnuit.playn.PlaynNuitFont;
import im.bci.jnuit.playn.PlaynNuitPreferences;
import im.bci.jnuit.playn.controls.PlaynNuitControls;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import org.geekygoblin.nedetlesmaki.core.NedNuitToolkit;

import org.geekygoblin.nedetlesmaki.core.components.IngameControls;
import org.geekygoblin.nedetlesmaki.core.components.ui.CutScenes;
import org.geekygoblin.nedetlesmaki.core.components.ui.DialogComponent;
import org.geekygoblin.nedetlesmaki.core.components.ui.LevelSelector;
import org.geekygoblin.nedetlesmaki.core.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.core.events.HideMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.events.IStartGameTrigger;
import org.geekygoblin.nedetlesmaki.core.events.ShowLevelMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.events.ShowMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.systems.IngameInputSystem;
import org.geekygoblin.nedetlesmaki.core.systems.UpdateLevelVisualSystem;
import org.geekygoblin.nedetlesmaki.core.systems.GameSystem;
import org.geekygoblin.nedetlesmaki.core.manager.EntityIndexManager;
import org.geekygoblin.nedetlesmaki.playn.core.events.PlaynStartGameTrigger;
import playn.core.Font;

/**
 *
 * @author devnewton
 */
public class PlaynNedModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IDefaultControls.class).to(PlaynDefaultControls.class);
        bind(NuitRenderer.class).to(NedPlaynNuitRenderer.class);
        bind(NuitTranslator.class).to(NedNuitTranslator.class);
        bind(NuitControls.class).to(PlaynNuitControls.class).in(Singleton.class);
        bind(NuitDisplay.class).to(PlaynNuitDisplay.class).in(Singleton.class);
        bind(NuitToolkit.class).to(NedNuitToolkit.class);
        bind(LevelSelector.class);
        bind(IngameInputSystem.class);
        bind(IMainLoop.class).to(PlaynMainLoop.class);
        bind(DialogComponent.class);
        bind(GameSystem.class);
        //TODO bind(DrawSystem.class);
        bind(UpdateLevelVisualSystem.class);
        bind(EntityIndexManager.class);
        bind(ShowMenuTrigger.class);
        bind(HideMenuTrigger.class);
        bind(ShowLevelMenuTrigger.class);
        bind(CutScenes.class);
        bind(Random.class).toInstance(new Random(42));
        bind(IAssets.class).to(PlaynAssets.class);
        bind(NedGame.class).to(PlaynGame.class);
        bind(IStartGameTrigger.class).to(PlaynStartGameTrigger.class);
        bind(NuitAudio.class).to(PlaynNuitAudio.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public NuitPreferences createPreferences(NuitControls controls) {
        return new PlaynNuitPreferences(controls, "nedetlesmaki");
    }

    @Provides
    @NamedEntities.MainMenu
    @Singleton
    public Entity createMainMenu(NedGame game, MainMenu mainMenuComponent) {
        Entity mainMenu = game.createEntity();
        mainMenu.addComponent(mainMenuComponent);
        game.addEntity(mainMenu);
        return mainMenu;
    }

    @Provides
    @NamedEntities.DefaultFont
    @Singleton
    public NuitFont createDefaultFont(IAssets assets) {
        return new PlaynNuitFont("Arial", Font.Style.BOLD, 32, true);
    }

    @Provides
    @NamedEntities.IngameControls
    @Singleton
    public Entity createIngameControls(NedGame game, IngameControls ingameControlsComponent) {
        Entity ingameControls = game.createEntity();
        ingameControls.addComponent(ingameControlsComponent);
        ingameControls.disable();
        game.addEntity(ingameControls);
        return ingameControls;
    }
}
