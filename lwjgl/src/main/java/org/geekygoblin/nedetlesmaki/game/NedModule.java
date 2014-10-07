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
package org.geekygoblin.nedetlesmaki.game;

import org.geekygoblin.nedetlesmaki.core.NedNuitToolkit;
import org.geekygoblin.nedetlesmaki.core.IDefaultControls;
import org.geekygoblin.nedetlesmaki.core.IMainLoop;
import org.geekygoblin.nedetlesmaki.core.NamedEntities;
import org.geekygoblin.nedetlesmaki.core.NedNuitTranslator;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import im.bci.jnuit.lwjgl.LwjglNuitPreferences;
import com.artemis.Entity;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import im.bci.jnuit.NuitToolkit;

import java.io.File;
import java.util.Random;

import com.google.inject.Singleton;
import im.bci.jnuit.NuitAudio;
import im.bci.jnuit.NuitControls;
import im.bci.jnuit.NuitDisplay;
import im.bci.jnuit.NuitFont;
import im.bci.jnuit.NuitLocale;
import im.bci.jnuit.NuitPreferences;
import im.bci.jnuit.NuitRenderer;
import im.bci.jnuit.NuitTranslator;
import im.bci.jnuit.lwjgl.LwjglNuitControls;
import im.bci.jnuit.lwjgl.LwjglNuitDisplay;

import im.bci.jnuit.lwjgl.assets.VirtualFileSystem;
import im.bci.jnuit.lwjgl.audio.OpenALNuitAudio;
import java.util.Locale;
import org.geekygoblin.nedetlesmaki.core.components.IngameControls;
import org.geekygoblin.nedetlesmaki.core.components.ui.CutScenes;
import org.geekygoblin.nedetlesmaki.core.components.ui.DialogComponent;
import org.geekygoblin.nedetlesmaki.core.components.ui.InGameUI;
import org.geekygoblin.nedetlesmaki.core.components.ui.LevelSelector;
import org.geekygoblin.nedetlesmaki.core.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.core.events.HideMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.events.IStartGameTrigger;
import org.geekygoblin.nedetlesmaki.core.events.ShowLevelMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.events.ShowMenuTrigger;
import org.geekygoblin.nedetlesmaki.game.events.StartGameTrigger;
import org.geekygoblin.nedetlesmaki.core.systems.IngameInputSystem;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.game.systems.DrawSystem;

/**
 *
 * @author devnewton
 */
public class NedModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IDefaultControls.class).to(LwjglDefaultControls.class);
        bind(NuitRenderer.class).to(NedNuitRenderer.class);
        bind(NuitControls.class).to(LwjglNuitControls.class).in(Singleton.class);
        bind(NuitDisplay.class).to(LwjglNuitDisplay.class).in(Singleton.class);
        bind(NuitToolkit.class).to(NedNuitToolkit.class);
        bind(LevelSelector.class);
        bind(IngameInputSystem.class);
        bind(DialogComponent.class);
        bind(DrawSystem.class);
        bind(LevelIndex.class);
        bind(ShowMenuTrigger.class);
        bind(HideMenuTrigger.class);
        bind(ShowLevelMenuTrigger.class);
        bind(CutScenes.class);
        bind(Random.class).toInstance(new Random(42));
        bind(im.bci.jnuit.lwjgl.assets.IAssets.class).to(LwjglAssets.class);
        bind(org.geekygoblin.nedetlesmaki.core.IAssets.class).to(LwjglAssets.class);
        bind(NedGame.class).to(LwjglGame.class);
        bind(IStartGameTrigger.class).to(StartGameTrigger.class);
        bind(IMainLoop.class).to(MainLoop.class);
        bind(NuitAudio.class).to(OpenALNuitAudio.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public NuitTranslator createTranslator() {
        NedNuitTranslator translator = new NedNuitTranslator();
        if (Locale.getDefault().getLanguage().equals(new Locale("fr").getLanguage())) {
            translator.setCurrentLocale(NuitLocale.FRENCH);
        }
        return translator;
    }

    @Provides
    @Singleton
    public NuitPreferences createPreferences(NuitControls controls) {
        return new LwjglNuitPreferences(controls, "nedetlesmaki");
    }

    @Provides
    @Singleton
    public VirtualFileSystem createVfs() {
        File currentDir = NormalLauncher.getApplicationDir();
        for (;;) {
            File assetsDir = new File(currentDir, "assets");
            if (assetsDir.exists()) {
                return new VirtualFileSystem(assetsDir);
            }
            currentDir = currentDir.getParentFile();
            if (null == currentDir) {
                throw new RuntimeException("Cannot find assets directory");
            }
        }
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
    @NamedEntities.InGameUI
    @Singleton
    public Entity createInGameUI(NedGame game, InGameUI inGameUIComponent) {
        Entity inGameUI = game.createEntity();
        inGameUI.addComponent(inGameUIComponent);
        game.addEntity(inGameUI);
        return inGameUI;
    }

    @Provides
    @NamedEntities.DefaultFont
    @Singleton
    public NuitFont createDefaultFont(im.bci.jnuit.lwjgl.assets.IAssets assets) {
        return assets.getFont("ProFontWindows.ttf,32,bold,antialiasing");
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
