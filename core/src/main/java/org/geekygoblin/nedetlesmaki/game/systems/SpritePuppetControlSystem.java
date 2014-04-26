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
package org.geekygoblin.nedetlesmaki.game.systems;

import java.util.ArrayList;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

import im.bci.jnuit.artemis.sprite.SpritePuppetControls;
import im.bci.jnuit.artemis.sprite.SpriteControl;

/**
 *
 * @author devnewton
 */
public class SpritePuppetControlSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<SpritePuppetControls> spriteMapper;

    public SpritePuppetControlSystem() {
        super(Aspect.getAspectForAll(SpritePuppetControls.class));
    }

    @Override
    protected void process(Entity entity) {
        SpritePuppetControls updatable = spriteMapper.get(entity);
        ArrayList<SpriteControl> updaters = updatable.getUpdaters();
        if(!updaters.isEmpty()) {
                SpriteControl updater = updaters.get(0);
                updater.update(world.getDelta());
                if(updater.isFinished()) {
                    updaters.remove(0);
                }
        } else {
            entity.removeComponent(updatable);
            entity.changedInWorld();
        }
    }
}
