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
import com.google.inject.Inject;
import com.google.inject.Singleton;
import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.lwjgl.assets.IAssets;
import org.geekygoblin.nedetlesmaki.game.manager.EntityIndexManager;
import org.geekygoblin.nedetlesmaki.game.systems.DebugSpriteSystem;
import org.geekygoblin.nedetlesmaki.game.systems.DialogSystem;
import org.geekygoblin.nedetlesmaki.game.systems.DrawSystem;
import org.geekygoblin.nedetlesmaki.game.systems.IngameInputSystem;
import org.geekygoblin.nedetlesmaki.game.systems.TriggerSystem;
import org.geekygoblin.nedetlesmaki.game.systems.MainMenuSystem;
import org.geekygoblin.nedetlesmaki.game.systems.SpriteAnimateSystem;
import org.geekygoblin.nedetlesmaki.game.systems.SpritePuppetControlSystem;
import org.geekygoblin.nedetlesmaki.game.systems.TriggerWhenRemovedSystem;
import org.geekygoblin.nedetlesmaki.game.systems.UpdateLevelVisualSystem;
import org.geekygoblin.nedetlesmaki.game.systems.GameSystem;
import org.geekygoblin.nedetlesmaki.game.systems.MouseArrowSystem;
import org.lwjgl.LWJGLException;

/**
 *
 * @author devnewton
 */
@Singleton
public class Game extends World {

    private Entity ned;

    @Inject
    public Game(NuitToolkit toolkit, DrawSystem drawSystem, IngameInputSystem ingameInputSystem, UpdateLevelVisualSystem updateLevelVisualSystem, EntityIndexManager indexedManager, IAssets assets) throws LWJGLException {
        setManager(indexedManager);
        setSystem(ingameInputSystem);
        setSystem(updateLevelVisualSystem);
        setSystem(new SpriteAnimateSystem());
        setSystem(new SpritePuppetControlSystem());
        setSystem(new MouseArrowSystem(this, assets));
        setSystem(drawSystem);
        setSystem(new TriggerSystem());
        setSystem(new TriggerWhenRemovedSystem());
        setSystem(new MainMenuSystem());
        setSystem(new DialogSystem());
        setSystem(new DebugSpriteSystem());
        setManager(new GroupManager());

        initialize();
    }

    public void setNed(Entity ned) {
        this.ned = ned;
    }

    public Entity getNed() {
        return ned;
    }
}
