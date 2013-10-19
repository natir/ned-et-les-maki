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

import org.geekygoblin.nedetlesmaki.game.events.ShowMenuTrigger;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import im.bci.nanim.NanimationCollection;

import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.components.Triggerable;
import org.geekygoblin.nedetlesmaki.game.components.IngameControls;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 */
public class IngameInputSystem extends EntityProcessingSystem {

    public IngameInputSystem() {
        super(Aspect.getAspectForAll(IngameControls.class));
    }
    
    @Mapper
    ComponentMapper<Sprite> spriteMapper;

    @Override
    protected void process(Entity e) {
        if (e.isEnabled()) {
            IngameControls controls = e.getComponent(IngameControls.class);
            controls.getShowMenu().poll();
            if (controls.getShowMenu().isActivated()) {
                world.addEntity(world.createEntity().addComponent(new Triggerable(new ShowMenuTrigger())));
            }
            controls.getDance().poll();
            if(controls.getDance().isActivated()) {
                Game game = (Game)world;
                Entity ned = game.getNed();
                Sprite sprite = spriteMapper.get(ned);
                NanimationCollection anims = game.getAssets().getAnimations("ned.nanim");
                Vector3f pos = sprite.getPosition();
                sprite.startAnimation(anims.getAnimationByName("walk_up"))
                        .moveTo(new Vector3f(pos.x + 50.0f, pos.y - 40.0f,  pos.z), 1.0f)
                        .startAnimation(anims.getAnimationByName("walk_right"))
                        .moveTo(new Vector3f(pos.x + 100.0f, pos.y,  pos.z), 1.0f)
                        .startAnimation(anims.getAnimationByName("walk_down"))
                        .moveTo(new Vector3f(pos.x + 50.0f, pos.y + 40,  pos.z), 1.0f)
                        .startAnimation(anims.getAnimationByName("dance"))
                        .waitDuring(2.0f)
                        .startAnimation(anims.getAnimationByName("walk_left"))
                        .moveTo(new Vector3f(pos), 1.0f)
                        .startAnimation(anims.getAnimationByName("walk_down"))
                        .stopAnimation();
            }
        }
    }
}
