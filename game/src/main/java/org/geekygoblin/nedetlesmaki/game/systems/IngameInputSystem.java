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

import org.geekygoblin.nedetlesmaki.game.events.ShowMenuTrigger;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import im.bci.nanim.NanimationCollection;

import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.utils.PosOperation;
import org.geekygoblin.nedetlesmaki.game.systems.GameSystem;
import org.geekygoblin.nedetlesmaki.game.components.Position;
import org.geekygoblin.nedetlesmaki.game.components.Triggerable;
import org.geekygoblin.nedetlesmaki.game.components.EntityPosIndex;
import org.geekygoblin.nedetlesmaki.game.components.IngameControls;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.geekygoblin.nedetlesmaki.game.components.visual.SpritePuppetControls;
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
	    Game game = (Game) world;
	    GameSystem gsystem = GameSystem.getInstance(EntityPosIndexSystem.getInstance(game.getEntityPosIndex()), game);
            IngameControls controls = e.getComponent(IngameControls.class);
            controls.getShowMenu().poll();
            if (controls.getShowMenu().isActivated()) {
                world.addEntity(world.createEntity().addComponent(new Triggerable(new ShowMenuTrigger())));
            }
            if (canMoveNed()) {
		controls.getUp().poll();
		controls.getDown().poll();
		controls.getRight().poll();
		controls.getLeft().poll();
                controls.getDance().poll();
                if (controls.getDance().isActivated()) {
                    Entity ned = game.getNed();
                    Sprite sprite = spriteMapper.get(ned);
                    NanimationCollection anims = game.getAssets().getAnimations("ned.nanim");
                    Vector3f pos = sprite.getPosition();
                    SpritePuppetControls updatable = new SpritePuppetControls(sprite);
                    updatable.startAnimation(anims.getAnimationByName("walk_up"))
                            .moveTo(new Vector3f(pos.x + 50.0f, pos.y - 40.0f, pos.z), 1.0f)
                            .startAnimation(anims.getAnimationByName("walk_right"))
                            .moveTo(new Vector3f(pos.x + 100.0f, pos.y, pos.z), 1.0f)
                            .startAnimation(anims.getAnimationByName("walk_down"))
                            .moveTo(new Vector3f(pos.x + 50.0f, pos.y + 40, pos.z), 1.0f)
                            .startAnimation(anims.getAnimationByName("dance"))
                            .waitDuring(2.0f)
                            .startAnimation(anims.getAnimationByName("walk_left"))
                            .moveTo(new Vector3f(pos), 1.0f)
                            .startAnimation(anims.getAnimationByName("walk_down"))
                            .stopAnimation();
                    ned.addComponent(updatable);
                    ned.changedInWorld();
                }
		else if (controls.getUp().isActivated()) {
                    Entity ned = game.getNed();
		    
		    gsystem.moveEntity(ned, PosOperation.sum(ned.getComponent(Position.class), new Position(1, 0)));
		}
		else if (controls.getDown().isActivated()) {
                    Entity ned = game.getNed();
		    
		    gsystem.moveEntity(ned, PosOperation.sum(ned.getComponent(Position.class), new Position(-1, 0)));
		}
		else if (controls.getLeft().isActivated()) {
		    Entity ned = game.getNed();
		    
		    gsystem.moveEntity(ned, PosOperation.sum(ned.getComponent(Position.class), new Position(0, 1)));
		}
		else if (controls.getRight().isActivated()) {
		     Entity ned = game.getNed();
		    
		    gsystem.moveEntity(ned, PosOperation.sum(ned.getComponent(Position.class), new Position(0, -1)));
		}
            }
        }
    }

    private boolean canMoveNed() {
        return world.getSystem(SpritePuppetControlSystem.class).getActives().isEmpty();
    }
}
