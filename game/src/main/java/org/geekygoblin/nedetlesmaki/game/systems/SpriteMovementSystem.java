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
package org.geekygoblin.nedetlesmaki.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.geekygoblin.nedetlesmaki.game.components.visual.SpriteMovement;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author devnewton
 */
public class SpriteMovementSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<SpriteMovement> spriteMovementMapper;
    @Mapper
    ComponentMapper<Sprite> spriteMapper;

    public SpriteMovementSystem() {
        super(Aspect.getAspectForAll(Sprite.class, SpriteMovement.class));
    }

    @Override
    protected void process(Entity entity) {
        Sprite sprite = spriteMapper.get(entity);
        SpriteMovement movement = spriteMovementMapper.get(entity);
        movement.action.update(world.getDelta());
        Vector2f nextPos = movement.path.get(0);
        Vector2f newPos;
        final float progress = movement.action.getProgress();
        if (progress >= 1.0f) {
            newPos = movement.previousPos = nextPos;
            movement.path.remove(0);
            if (movement.path.isEmpty()) {
                entity.removeComponent(movement);
                entity.changedInWorld();
            }
        } else {
            newPos = new Vector2f();
            Vector2f.sub(nextPos, movement.previousPos, newPos);
            newPos.scale(progress);
            newPos.x += movement.previousPos.x;
            newPos.y += movement.previousPos.y;
        }
        sprite.getPosition().x = newPos.x;
        sprite.getPosition().y = newPos.y;
    }
}
