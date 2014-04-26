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

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import org.geekygoblin.nedetlesmaki.game.Group;
import im.bci.jnuit.artemis.sprite.Sprite;

/**
 *
 * @author devnewton
 */
public class DebugSpriteSystem extends EntitySystem {

    @Mapper
    ComponentMapper<Sprite> spriteMapper;

    public DebugSpriteSystem() {
        super(Aspect.getAspectForAll(Sprite.class));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> ib) {
        for (Entity entity : ib) {
            if (world.getManager(GroupManager.class).isInGroup(entity, Group.DEBUG)) {
                final Sprite sprite = entity.getComponent(Sprite.class);
                sprite.setLabel(sprite.getPosition().x + "," + sprite.getPosition().y + "," + sprite.getPosition().z);
            }
        }
    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }

}
