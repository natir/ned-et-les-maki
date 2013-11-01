/*
 * Copyright © 2013, Pierre Marijon <pierre@marijon.fr>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * The Software is provided "as is", without warranty of any kind, express or 
 * implied, including but not limited to the warranties of merchantability, 
 * fitness for a particular purpose and noninfringement. In no event shall the 
 * authors or copyright holders X be liable for any claim, damages or other 
 * liability, whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other dealings in the
 * Software.
 */

package org.geekygoblin.nedetlesmaki.game.systems;

import javax.inject.Inject;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;

import im.bci.nanim.IAnimationCollection;

import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.components.EntityPosIndex;
import org.geekygoblin.nedetlesmaki.game.systems.EntityPosIndexSystem;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.geekygoblin.nedetlesmaki.game.components.visual.SpritePuppetControls;
import org.geekygoblin.nedetlesmaki.game.components.EntityPosIndex;
import org.geekygoblin.nedetlesmaki.game.components.Position;
import org.geekygoblin.nedetlesmaki.game.utils.PosOperation;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author natir
 */
public class UpdateLevelVisualSystem extends VoidEntitySystem {

    @Mapper
    ComponentMapper<Sprite> spriteMapper;

    private final Assets assets;
    private Game game;
    private int nbIndexSaved;
    private EntityPosIndexSystem index;

    @Inject
    public UpdateLevelVisualSystem(Assets assets) {
	this.assets = assets;
	this.nbIndexSaved = 0;
    }

    @Override
    protected void processSystem() {
	game = (Game) world;
	this.index = EntityPosIndexSystem.getInstance(game.getEntityPosIndex());
        EntityPosIndex old = this.index.getLastWorld();
        
	if(old != null)
	{
	    for(int i = 0; i != 15; i++) {
		for(int j = 0; j != 15; j++) {
		    Entity oE = old.getEntityWithPos(i, j);
		    if(oE != null) {
		        Position diff = PosOperation.deduction(oE.getComponent(Position.class), new Position(i, j));
			Game game = (Game) world;

			if(diff.getX() != 0 || diff.getY() != 0) {
			    this.moveSprite(oE, diff);
			    this.index.saveWorld();
			}
		    }
		}
	    }
	}
    }
    
    private void moveSprite(Entity e, Position diff) {
	Sprite sprite = e.getComponent(Sprite.class);
	IAnimationCollection anims = this.assets.getAnimations("ned.nanim");
	Vector3f pos = sprite.getPosition();
	SpritePuppetControls updatable = new SpritePuppetControls(sprite);
	
	if(diff.getX() > 0) {
	    System.out.print("Move up");
	    updatable.startAnimation(anims.getAnimationByName("walk_up"))
		.moveTo(new Vector3f(pos.x + 25.0f, pos.y - 10.0f, pos.z), 0.5f)
		.stopAnimation();
	    e.addComponent(updatable);
	    e.changedInWorld();
	}
	else if(diff.getX() < 0) {
	    System.out.print("Move down");
	    updatable.startAnimation(anims.getAnimationByName("walk_down"))
		.moveTo(new Vector3f(pos.x - 25.0f, pos.y + 25, pos.z), 0.5f)
        	.stopAnimation();
	    e.addComponent(updatable);
	    e.changedInWorld();
	}
	else if(diff.getY() > 0) {
	    System.out.print("Move left");
	    updatable.startAnimation(anims.getAnimationByName("walk_left"))
		.moveTo(new Vector3f(pos.x - 25.0f, pos.y + 25, pos.z), 0.5f)
	        .stopAnimation();
	    e.addComponent(updatable);
	    e.changedInWorld();
	}
	else if(diff.getY() < 0) {
	    System.out.print("Move right");
	    updatable.startAnimation(anims.getAnimationByName("walk_right"))
		.moveTo(new Vector3f(pos.x + 25.0f, pos.y - 25, pos.z), 0.5f)
		.stopAnimation();
	    e.addComponent(updatable);
	    e.changedInWorld();
	}
    }
}
