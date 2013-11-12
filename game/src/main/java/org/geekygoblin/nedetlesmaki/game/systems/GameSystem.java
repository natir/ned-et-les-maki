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

import com.artemis.World;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.ComponentMapper;

import org.geekygoblin.nedetlesmaki.game.components.Pushable;
import org.geekygoblin.nedetlesmaki.game.components.Pusher;
import org.geekygoblin.nedetlesmaki.game.components.Position;
import org.geekygoblin.nedetlesmaki.game.components.Movable;
import org.geekygoblin.nedetlesmaki.game.systems.EntityPosIndexSystem;
import org.geekygoblin.nedetlesmaki.game.utils.PosOperation;

/**
 *
 * @author natir
 */
public class GameSystem {

    @Mapper
    ComponentMapper<Pushable> pushableMapper;
    @Mapper
    ComponentMapper<Pusher> pusherMapper;
    @Mapper
    ComponentMapper<Position> positionMapper;
    @Mapper
    ComponentMapper<Movable> movableMapper;

    private EntityPosIndexSystem index;
    private static GameSystem instance = null;

    private GameSystem(EntityPosIndexSystem index, World w) {
	this.index = index;
	this.pushableMapper = ComponentMapper.getFor(Pushable.class, w);
	this.pusherMapper = ComponentMapper.getFor(Pusher.class, w);
	this.positionMapper = ComponentMapper.getFor(Position.class, w);
	this.movableMapper = ComponentMapper.getFor(Movable.class, w);
    }
    
    public static GameSystem getInstance(EntityPosIndexSystem index, World w) {
	if (instance == null) {
	    instance = new GameSystem(index, w);
	}
	
	return instance;
    }

    public boolean moveEntity(Entity e, Position dirP) {
	/* testMove return the new position
	 * If new position isn't equale to the actuale position :
	 * * move entity
	 * * return true
	 * else run false
	 */

	/*Check if move possible*/
	Position oldP = this.getPosition(e);
	Position newP = testMove(e, dirP);

	if(!PosOperation.equale(newP, this.getPosition(e))) {
	    /*run move*/
	    if(index.moveEntity(oldP.getX(), oldP.getY(), newP.getX(), newP.getY())) {
		e.getComponent(Position.class).setX(newP.getX()*this.getMovable(e));
		e.getComponent(Position.class).setY(newP.getY()*this.getMovable(e));

		return true;
	    }
	    else {
		return false;
	    }
	}
	else {
	    return false;
	}
    }

    public Position testMove(Entity e, Position dirP) {
	/*
	 * If object is one way obtaine this entity
	 * Test if entity can by move :
	 * * e is pusher
	 * * wayOnE is pushable
	 * * If true recursive call one wayOnE and the dirP
	 */
	int nbCase = this.getMovable(e);
	Position oldP = this.getPosition(e);
	Position newP = PosOperation.sum(oldP, PosOperation.multiplication(dirP, nbCase));
	Position freeP = wayFreeTo(oldP, newP);
	Position nextEP = PosOperation.sum(freeP, dirP);

	System.out.printf("Free :\n");
	freeP.print();
	System.out.printf("New :\n");
	newP.print();
	if(!PosOperation.equale(freeP, newP)) {

	    Entity nextE;
	    if(!positionIsVoid(nextEP)) {
		nextE = index.getEntity(nextEP.getX(), nextEP.getY());
	    
		if(this.isPusherEntity(e))
		{	
		    if(this.isPushableEntity(nextE)) {
			if(this.moveEntity(nextE, dirP))
			{
			    /*nextEP maybe change*/
			    return PosOperation.sum(freeP, dirP);
			}
			else {
			    return freeP;
			}
		    }
		    else {
			return freeP;
		    }
		}
		else {
		    return freeP;
		}
	    }
	    else {
		return nextEP;
	    }
	}

	return newP;
    }
    
    public boolean positionIsVoid(Position p) {
	Entity tmpE = index.getEntity(p.getX(), p.getY());
	if(tmpE != null) {
	    return false;
	}
	
	return true;
    }

    public boolean isPushableEntity(Entity e) {
	Pushable p = this.pushableMapper.getSafe(e);

	if(p != null) {
	    if(p.isPushable()) {
		return true;
	    }
	}

	return false;
    }

    public boolean isPusherEntity(Entity e) {
	Pusher p = this.pusherMapper.getSafe(e);

	if(p != null) {
	    if(p.isPusher()) {
		return true;
	    }
	}

	return false;
    }

    public Position getPosition(Entity e) {
	Position p = this.positionMapper.getSafe(e);
	
	if(p != null) {
	    return p;
	}
	
	return new Position(-1, -1);
    }
    
    public int getMovable(Entity e) {
	Movable m = this.movableMapper.getSafe(e);

	if(m != null) {
	    return m.getNbCase();
	}

	return 0;
    }

    public Position wayFreeTo(Position begin, Position end) {
	Position delta = PosOperation.deduction(begin, end);
	System.out.print("Begin End Delta\n");
	begin.print();
	end.print();
	delta.print();
	int base, max;
	if(delta.getX() > 0) {
	    base = begin.getX() - 1;
	    max = end.getX();
	    this.testObjOnWayX(base, max, begin.getY(), -1).print();
	    return this.testObjOnWayX(base, max, begin.getY(), -1);
	}
	else if(delta.getX() < 0) {
	    base = begin.getX();
	    max = end.getX() + 1;
	    this.testObjOnWayX(base, max, begin.getY(), 1).print();
	    return this.testObjOnWayX(base, max, begin.getY(), 1);
	}
	else if(delta.getY() > 0) {
	    base = begin.getY() - 1;
	    max = end.getY();
	    this.testObjOnWayX(base, max, begin.getY(), -1).print();
	    return this.testObjOnWayY(base, max, begin.getX(), -1);
	}
	else {
	    base = begin.getY() + 1;
	    max = end.getY();
	    this.testObjOnWayX(base, max, begin.getY(), -1).print();
	    return this.testObjOnWayY(base, max, begin.getX(), 1);
	}
    }

    private Position testObjOnWayX(int base, int max, int y, int mul) {
	System.out.printf("Test free way X  base %d max %d mul %d \n", base, max, mul);
	Position old = new Position(base, y);

	for(int i = base; i != max; i += 1 * mul) {
	    Entity e = this.index.getEntity(i, y);
	    System.out.printf("Test free way X, [%d, %d] :\n", i, y);
	    System.out.print(e);
	    if(e != null) {
		return old;
	    }
	    else {
		old = new Position(i, y);
	    }
	}
	
	return new Position(max, y);
    }

    private Position testObjOnWayY(int base, int max, int x, int mul) {
	Position old = new Position(x, base);
	
	for(int i = base; i != max; i += 1 * mul) {
	    Entity e = this.index.getEntity(x, i);
	    if(e != null) {
		return old;
	    }
	    else {
		old = new Position(x, i);
	    }
	}
	
	return new Position(x, max);
    }
}
