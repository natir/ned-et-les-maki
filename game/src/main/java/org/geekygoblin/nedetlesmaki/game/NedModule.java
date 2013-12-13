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

import com.artemis.Entity;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import im.bci.lwjgl.nuit.NuitToolkit;
import im.bci.nanim.IAnimationCollection;
import im.bci.nanim.PlayMode;

import java.io.File;
import java.util.Random;

import com.google.inject.Singleton;

import org.geekygoblin.nedetlesmaki.game.assets.GarbageCollectedAssets;
import org.geekygoblin.nedetlesmaki.game.assets.IAssets;
import org.geekygoblin.nedetlesmaki.game.assets.VirtualFileSystem;
import org.geekygoblin.nedetlesmaki.game.components.IngameControls;
import org.geekygoblin.nedetlesmaki.game.components.TriggerableWhenRemoved;
import org.geekygoblin.nedetlesmaki.game.components.ZOrder;
import org.geekygoblin.nedetlesmaki.game.components.ui.Dialog;
import org.geekygoblin.nedetlesmaki.game.components.ui.LevelSelector;
import org.geekygoblin.nedetlesmaki.game.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.game.constants.ZOrders;
import org.geekygoblin.nedetlesmaki.game.events.HideMenuTrigger;
import org.geekygoblin.nedetlesmaki.game.events.ShowLevelMenuTrigger;
import org.geekygoblin.nedetlesmaki.game.events.ShowMenuTrigger;
import org.geekygoblin.nedetlesmaki.game.events.Trigger;
import org.geekygoblin.nedetlesmaki.game.systems.IngameInputSystem;
import org.geekygoblin.nedetlesmaki.game.systems.UpdateLevelVisualSystem;
import org.geekygoblin.nedetlesmaki.game.systems.GameSystem;
import org.geekygoblin.nedetlesmaki.game.manager.EntityIndexManager;
import org.geekygoblin.nedetlesmaki.game.systems.DrawSystem;

/**
 *
 * @author devnewton
 */
public class NedModule extends AbstractModule {

    @Override
    protected void configure() {
        File applicationDir = Main.getApplicationDir();
        bind(VirtualFileSystem.class).toInstance(new VirtualFileSystem(new File(applicationDir, "data"), new File(applicationDir.getParentFile(), "data")));

        bind(Preferences.class);
        bind(IAssets.class).to(GarbageCollectedAssets.class);
        bind(NuitToolkit.class).to(NedToolkit.class);
        bind(LevelSelector.class);
        bind(IngameInputSystem.class);
        bind(MainLoop.class);
        bind(Dialog.class);
	bind(GameSystem.class);
        bind(DrawSystem.class);
	bind(UpdateLevelVisualSystem.class);
	bind(EntityIndexManager.class);
        bind(ShowMenuTrigger.class);
        bind(HideMenuTrigger.class);
        bind(ShowLevelMenuTrigger.class);
        bind(Random.class).toInstance(new Random(42));
    }

    @Provides
    @NamedEntities.MainMenu
    @Singleton
    public Entity createMainMenu(Game game, MainMenu mainMenuComponent) {
        Entity mainMenu = game.createEntity();
        mainMenu.addComponent(mainMenuComponent);
        mainMenu.addComponent(new ZOrder(ZOrders.MENU));
        game.addEntity(mainMenu);
        return mainMenu;
    }

    @Provides
    @NamedEntities.IngameControls
    @Singleton
    public Entity createIngameControls(Game game, IngameControls ingameControlsComponent) {
        Entity ingameControls = game.createEntity();
        ingameControls.addComponent(ingameControlsComponent);
        ingameControls.disable();
        game.addEntity(ingameControls);
        return ingameControls;
    }

    @Provides
    @NamedEntities.Intro
    public Entity createIntro(Game game, final IAssets assets, Dialog dialogComponent, ShowMenuTrigger showMenuTrigger) {
        IAnimationCollection animations = assets.getAnimations("intro.nanim.gz");
        dialogComponent.addTirade(animations.getAnimationByName("reveil").start(PlayMode.LOOP), "dialog.intro.reveil.1", "dialog.intro.reveil.2");
        dialogComponent.addTirade(animations.getAnimationByName("tour_au_loin").start(PlayMode.LOOP), "dialog.intro.tour_au_loin.1", "dialog.intro.tour_au_loin.2");
        dialogComponent.addTirade(animations.getAnimationByName("pied_de_la_tour").start(PlayMode.LOOP), "dialog.intro.pied_de_la_tour.1", "dialog.intro.pied_de_la_tour.2", "dialog.intro.pied_de_la_tour.3");
        dialogComponent.addTirade(animations.getAnimationByName("dans_la_tour").start(PlayMode.LOOP), "dialog.intro.dans_la_tour.1");
        Entity intro = game.createEntity();
        intro.addComponent(dialogComponent);
        intro.addComponent(new ZOrder(ZOrders.DIALOG));
        intro.addComponent(new TriggerableWhenRemoved(showMenuTrigger).add(new Trigger() {

            @Override
            public void process(Game game) {

                assets.forceAnimationUnload("intro.nanim.gz");
            }
        }));
        game.addEntity(intro);
        return intro;
    }

    // @Provides
    // @NamedEntities.EntityPosIndex
    // @Singleton
    // public Entity createEntityPosIndex(Game game) {
    //     Entity entityPosIndex = game.createEntity();
    //     entityPosIndex.addComponent(new EntityPosIndex());
    //     game.addEntity(entityPosIndex);
    //     return entityPosIndex;
    // }
}
