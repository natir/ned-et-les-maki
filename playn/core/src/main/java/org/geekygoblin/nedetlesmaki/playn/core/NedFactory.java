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
import im.bci.jnuit.NuitAudio;
import im.bci.jnuit.NuitFont;
import im.bci.jnuit.NuitPreferences;
import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.NuitTranslator;
import im.bci.jnuit.playn.PlaynNuitAudio;
import im.bci.jnuit.playn.PlaynNuitDisplay;
import im.bci.jnuit.playn.PlaynNuitFont;
import im.bci.jnuit.playn.PlaynNuitPreferences;
import im.bci.jnuit.playn.PlaynNuitRenderer;
import im.bci.jnuit.playn.controls.PlaynNuitControls;
import java.util.Random;
import javax.inject.Provider;
import org.geekygoblin.nedetlesmaki.core.IDefaultControls;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.NedNuitToolkit;
import org.geekygoblin.nedetlesmaki.core.NedNuitTranslator;
import org.geekygoblin.nedetlesmaki.core.components.IngameControls;
import org.geekygoblin.nedetlesmaki.core.components.ui.CutScenes;
import org.geekygoblin.nedetlesmaki.core.components.ui.InGameUI;
import org.geekygoblin.nedetlesmaki.core.components.ui.LevelSelector;
import org.geekygoblin.nedetlesmaki.core.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.core.events.HideMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.events.IStartGameTrigger;
import org.geekygoblin.nedetlesmaki.core.events.ShowLevelMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.events.ShowMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.systems.IngameInputSystem;
import org.geekygoblin.nedetlesmaki.playn.core.events.PlaynStartGameTrigger;
import org.geekygoblin.nedetlesmaki.playn.core.systems.PlaynInGameUISystem;
import org.geekygoblin.nedetlesmaki.playn.core.systems.PlaynMainMenuSystem;
import org.geekygoblin.nedetlesmaki.playn.core.systems.PlaynSpriteSystem;
import playn.core.Font;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
public class NedFactory {

    private final PlaynMainLoop mainLoop;
    private Entity mainMenu;
    private final Entity ingameControls;
    private final PlaynAssets assets;
    private final LevelIndex indexedManager;
    private final Random random;
    private final Entity inGameUI;

    public NedFactory() {
        this.random = new Random(42);
        PlaynNuitControls controls = new PlaynNuitControls();
        PlaynTouchControls touch = new PlaynTouchControls();
        PlaynNuitDisplay display = new PlaynNuitDisplay();
        NuitFont font = new PlaynNuitFont("Arial", Font.Style.BOLD, 32, true);
        NuitTranslator translator = new NedNuitTranslator();
        PlaynNuitRenderer renderer = new NedPlaynNuitRenderer(translator, font);
        NuitAudio audio = new PlaynNuitAudio();
        NuitToolkit toolkit = new NedNuitToolkit(display, translator, font, renderer, controls, audio);
        Provider<ShowLevelMenuTrigger> showLevelMenuTrigger = new Provider<ShowLevelMenuTrigger>() {

            @Override
            public ShowLevelMenuTrigger get() {
                return new ShowLevelMenuTrigger(mainMenu, ingameControls);
            }
        };
        Provider<ShowMenuTrigger> showMenuTrigger = new Provider<ShowMenuTrigger>() {

            @Override
            public ShowMenuTrigger get() {
                return new ShowMenuTrigger(mainMenu, inGameUI, ingameControls);
            }
        };
        Provider<IStartGameTrigger> startGameTrigger = new Provider<IStartGameTrigger>() {

            @Override
            public IStartGameTrigger get() {
                return new PlaynStartGameTrigger(assets, mainMenu, inGameUI, ingameControls, indexedManager, random);
            }
        };

        assets = new PlaynAssets();
        indexedManager = new LevelIndex();
        IDefaultControls defaultControls = new PlaynDefaultControls(controls, touch);
        InGameUI inGameUIComponent = new InGameUI(toolkit, assets);
        IngameInputSystem ingameInputSystem = new IngameInputSystem(showMenuTrigger, showLevelMenuTrigger, startGameTrigger,defaultControls, inGameUIComponent);
        PlaynSpriteSystem drawSystem = new PlaynSpriteSystem(controls);
        PlaynMainMenuSystem mainMenuSystem = new PlaynMainMenuSystem(renderer);
        PlaynInGameUISystem inGameUISystem = new PlaynInGameUISystem(renderer);

        NedGame game = new PlaynGame(toolkit, ingameInputSystem, assets, drawSystem, mainMenuSystem,inGameUISystem);

        mainLoop = new PlaynMainLoop(game);
        LevelSelector levelSelector = new LevelSelector(game, toolkit, assets, startGameTrigger, showMenuTrigger);
        Provider<HideMenuTrigger> hideMenuTrigger = new Provider<HideMenuTrigger>() {

            @Override
            public HideMenuTrigger get() {
                return new HideMenuTrigger(mainMenu, inGameUI, ingameControls);
            }
        };
        CutScenes cutscenes = new CutScenes(assets);
        NuitPreferences preferences = new PlaynNuitPreferences(controls, "nedetlesmaki");

        //TODO à retirer quand on aura fixer l'issue #132 Poids des images
        preferences.putBoolean("cutscenes.intro.seen", true);

        ingameControls = game.createEntity();
        IngameControls ingameControlsComponent = new IngameControls(defaultControls);
        ingameControls.addComponent(ingameControlsComponent);
        ingameControls.disable();
        game.addEntity(ingameControls);

        MainMenu mainMenuComponent = new MainMenu(mainLoop, game, toolkit, assets, levelSelector, hideMenuTrigger, ingameControlsComponent, cutscenes, preferences);
        mainMenu = game.createEntity();
        mainMenu.addComponent(mainMenuComponent);
        game.addEntity(mainMenu);

        inGameUI = game.createEntity();
        inGameUI.addComponent(inGameUIComponent);
        game.addEntity(inGameUI);
        inGameUI.disable();
    }

    public PlaynMainLoop getMainLoop() {
        return mainLoop;
    }

}
