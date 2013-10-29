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
import com.artemis.World;
import com.artemis.managers.GroupManager;
import im.bci.lwjgl.nuit.NuitToolkit;
import im.bci.lwjgl.nuit.controls.Action;
import im.bci.lwjgl.nuit.utils.TrueTypeFont;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.components.IngameControls;
import org.geekygoblin.nedetlesmaki.game.components.EntityPosIndex;
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
    private final Entity mainMenu, ingameControls, entityPosIndex;
    private Entity ned;
    private boolean closeRequested;
    private static final Preferences preferences = new Preferences();
    private static final String[] messagesBundles = new String[]{"messages", "nuit_messages"};

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

           /* @Override
            protected TrueTypeFont createFont() {
                return Game.this.assets.getFont("Boxy-Bold.ttf");
            }*/

            @Override
            public String getMessage(String key) {
                for (String bundleName : messagesBundles) {
                    ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
                    if (bundle.containsKey(key)) {
                        return bundle.getString(key);
                    }
                }
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "No translation for {0}", key);
                return key;
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

	entityPosIndex = createEntity();
        entityPosIndex.addComponent(new EntityPosIndex());
	addEntity(entityPosIndex);
    }

    public Entity getMainMenu() {
        return mainMenu;
    }

    public Entity getIngameControls() {
        return ingameControls;
    }

    public Entity getEntityPosIndex() {
        return entityPosIndex;
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
