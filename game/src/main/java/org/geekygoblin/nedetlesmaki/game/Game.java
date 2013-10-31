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
import javax.inject.Inject;
import javax.inject.Singleton;
import org.geekygoblin.nedetlesmaki.game.components.EntityPosIndex;
import org.geekygoblin.nedetlesmaki.game.systems.DrawSystem;
import org.geekygoblin.nedetlesmaki.game.systems.IngameInputSystem;
import org.geekygoblin.nedetlesmaki.game.systems.TriggerSystem;
import org.geekygoblin.nedetlesmaki.game.systems.MainMenuSystem;
import org.geekygoblin.nedetlesmaki.game.systems.SpriteAnimateSystem;
import org.geekygoblin.nedetlesmaki.game.systems.SpritePuppetControlSystem;
import org.lwjgl.LWJGLException;

/**
 *
 * @author devnewton
 */
@Singleton
public class Game extends World {

    private final Entity entityPosIndex;
    private Entity ned;

    @Inject
    public Game(NuitToolkit toolkit, IngameInputSystem ingameInputSystem) throws LWJGLException {
        setSystem(ingameInputSystem);
        setSystem(new SpriteAnimateSystem());
        setSystem(new SpritePuppetControlSystem());
        setSystem(new DrawSystem());
        setSystem(new TriggerSystem());
        setSystem(new MainMenuSystem());
        setManager(new GroupManager());
        initialize();

	entityPosIndex = createEntity();
        entityPosIndex.addComponent(new EntityPosIndex());
	addEntity(entityPosIndex);
    }

    public Entity getEntityPosIndex() {
        return entityPosIndex;
    }

    public void setNed(Entity ned) {
        this.ned = ned;
    }

    public Entity getNed() {
        return ned;
    }
}
