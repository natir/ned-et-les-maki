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
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.manager.EntityIndexManager;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.components.Triggerable;
import org.geekygoblin.nedetlesmaki.game.components.IngameControls;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;

/**
 *
 * @author devnewton
 */
@Singleton
public class IngameInputSystem extends EntityProcessingSystem {
    private final Assets assets;
    private final Provider<ShowMenuTrigger> showMenuTrigger;
    private final EntityIndexManager indexSystem;
    private final GameSystem gameSystem;

    @Inject
    public IngameInputSystem(Assets assets, Provider<ShowMenuTrigger> showMenuTrigger, EntityIndexManager indexSystem, GameSystem gameSystem) {
        super(Aspect.getAspectForAll(IngameControls.class));
        this.assets = assets;
        this.showMenuTrigger = showMenuTrigger;
	this.indexSystem = indexSystem;
	this.gameSystem = gameSystem;
    }

    @Mapper
    ComponentMapper<Sprite> spriteMapper;

    @Override
    protected void process(Entity e) {
        if (e.isEnabled()) {
	    Game game = (Game) world;

            IngameControls controls = e.getComponent(IngameControls.class);
            controls.getShowMenu().poll();
            if (controls.getShowMenu().isActivated()) {
                world.addEntity(world.createEntity().addComponent(new Triggerable(showMenuTrigger.get())));
            }
            if (canMoveNed()) {
		controls.getUp().poll();
		controls.getDown().poll();
		controls.getRight().poll();
		controls.getLeft().poll();
                if (controls.getUp().isActivated()) {
                    Entity ned = game.getNed();

        	    indexSystem.addMouvement(gameSystem.moveEntity(ned, new Position(0, -1)));
		    System.out.print("Ned :");
		    ned.getComponent(Position.class).print();
		    gameSystem.printIndex();

		    ned.changedInWorld();
		}
		else if (controls.getDown().isActivated()) {
                    Entity ned = game.getNed();
		    
        	    indexSystem.addMouvement(gameSystem.moveEntity(ned, new Position(0, 1)));

		    System.out.print("Ned :");
		    ned.getComponent(Position.class).print();
		    gameSystem.printIndex();

		    ned.changedInWorld();
		}
		else if (controls.getLeft().isActivated()) {
		    Entity ned = game.getNed();
		    
        	    indexSystem.addMouvement(gameSystem.moveEntity(ned, new Position(-1, 0)));
		
        	    System.out.print("Ned :");
		    ned.getComponent(Position.class).print();
		    gameSystem.printIndex();

                    ned.changedInWorld();
		}
		else if (controls.getRight().isActivated()) {
		    Entity ned = game.getNed();

		    indexSystem.addMouvement(gameSystem.moveEntity(ned, new Position(1, 0)));	    
        	    System.out.print("Ned :");
		    ned.getComponent(Position.class).print();
		    gameSystem.printIndex();

                    ned.changedInWorld();
		}
            }
        }
    }

    private boolean canMoveNed() {
        return world.getSystem(SpritePuppetControlSystem.class).getActives().isEmpty();
    }
}
