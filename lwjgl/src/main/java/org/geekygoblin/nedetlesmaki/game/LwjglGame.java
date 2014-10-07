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
package org.geekygoblin.nedetlesmaki.game;

import org.geekygoblin.nedetlesmaki.core.IAssets;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import im.bci.jnuit.NuitToolkit;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.systems.DebugSpriteSystem;
import org.geekygoblin.nedetlesmaki.core.systems.DialogSystem;
import org.geekygoblin.nedetlesmaki.game.systems.DrawSystem;
import org.geekygoblin.nedetlesmaki.core.systems.IngameInputSystem;
import org.geekygoblin.nedetlesmaki.core.systems.TriggerSystem;
import org.geekygoblin.nedetlesmaki.game.systems.MainMenuSystem;
import im.bci.jnuit.artemis.sprite.SpriteAnimateSystem;
import org.geekygoblin.nedetlesmaki.core.systems.MouseArrowSystem;
import org.geekygoblin.nedetlesmaki.core.systems.SpritePuppetControlSystem;
import org.geekygoblin.nedetlesmaki.game.systems.InGameUISystem;

/**
 *
 * @author devnewton
 */
@Singleton
public class LwjglGame extends NedGame {

    private Entity ned;

    @Inject
    public LwjglGame(NuitToolkit toolkit, DrawSystem drawSystem, IngameInputSystem ingameInputSystem, LevelIndex indexedManager, IAssets assets) {
        setSystem(ingameInputSystem);
        setSystem(new SpriteAnimateSystem());
        setSystem(new SpritePuppetControlSystem());
        setSystem(new MouseArrowSystem(this, assets, drawSystem));
        setSystem(drawSystem);
        setSystem(new TriggerSystem());
        setSystem(new MainMenuSystem());
        setSystem(new InGameUISystem());
        setSystem(new DialogSystem());
        setSystem(new DebugSpriteSystem());;
        setManager(new GroupManager());

        initialize();
    }

    @Override
    public void setNed(Entity ned) {
        this.ned = ned;
    }

    @Override
    public Entity getNed() {
        return ned;
    }
}
