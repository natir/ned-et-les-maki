/*
 * Copyright (c) 2013 devnewton <devnewton@bci.im>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'devnewton <devnewton@bci.im>' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.geekygoblin.nedetlesmaki.game;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import im.bci.lwjgl.nuit.NuitToolkit;
import im.bci.lwjgl.nuit.utils.TrueTypeFont;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.components.IngameControls;
import org.geekygoblin.nedetlesmaki.game.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.game.components.ZOrder;
import org.geekygoblin.nedetlesmaki.game.constants.ZOrders;
import org.geekygoblin.nedetlesmaki.game.systems.DrawSystem;
import org.geekygoblin.nedetlesmaki.game.systems.IngameInputSystem;
import org.geekygoblin.nedetlesmaki.game.systems.TriggerSystem;
import org.geekygoblin.nedetlesmaki.game.systems.MainMenuSystem;
import org.geekygoblin.nedetlesmaki.game.systems.SpriteAnimateSystem;
import org.geekygoblin.nedetlesmaki.game.systems.SpritePuppetControlSystem;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

/**
 *
 * @author devnewton
 */
public class Game extends World {

    private final Assets assets;
    private final NuitToolkit toolkit;
    private final Entity mainMenu, ingameControls;
    private Entity ned;
    private boolean closeRequested;
    private static final Preferences preferences = new Preferences();

    public Game(Assets assets) throws LWJGLException {
        this.assets = assets;
        setSystem(new IngameInputSystem());
        setSystem(new SpriteAnimateSystem());
        setSystem(new SpritePuppetControlSystem());
        setSystem(new DrawSystem());
        setSystem(new TriggerSystem());
        setSystem(new MainMenuSystem());
        setManager(new GroupManager());
        initialize();
        
        toolkit = new NuitToolkit() {

            @Override
            protected TrueTypeFont createFont() {
                return Game.this.assets.getFont("Boxy-Bold.ttf");
            }            
        };

        mainMenu = createEntity();
        mainMenu.addComponent(new MainMenu(this, toolkit));
        mainMenu.addComponent(new ZOrder(ZOrders.MENU));
        addEntity(mainMenu);

        ingameControls = createEntity();
        ingameControls.addComponent(new IngameControls());
        ingameControls.disable();
        addEntity(ingameControls);
    }

    public Entity getMainMenu() {
        return mainMenu;
    }

    public Entity getIngameControls() {
        return ingameControls;
    }

    public Assets getAssets() {
        return assets;
    }

    public void setCloseRequested(boolean closeRequested) {
        this.closeRequested = closeRequested;
    }

    public boolean isCloseRequested() {
        return closeRequested || Display.isCloseRequested();
    }
    
    public static Preferences getPreferences() {
        return preferences;
    }

    public void setNed(Entity ned) {
        this.ned = ned;
    }

    public Entity getNed() {
        return ned;
    }

    public NuitToolkit getToolkit() {
        return toolkit;
    }
}
