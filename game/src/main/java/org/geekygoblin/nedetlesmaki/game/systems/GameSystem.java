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
import javax.inject.Singleton;

import com.artemis.Entity;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;

import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Pushable;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Pusher;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Movable;
import org.geekygoblin.nedetlesmaki.game.manager.EntityIndexManager;
import org.geekygoblin.nedetlesmaki.game.utils.PosOperation;

/**
 *
 * @author natir
 */
@Singleton
public class GameSystem extends VoidEntitySystem {

    private EntityIndexManager index;

    @Mapper
    ComponentMapper<Pushable> pushableMapper;
    @Mapper
    ComponentMapper<Pusher> pusherMapper;
    @Mapper
    ComponentMapper<Position> positionMapper;
    @Mapper
    ComponentMapper<Movable> movableMapper;

    
    @Inject
    public GameSystem(EntityIndexManager index) {
	this.index = index;
    }

    @Override
    protected void processSystem() {}
    
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

	// System.out.print("Entity : ");
	// System.out.print(e);
	// System.out.print("\n");
	
	// this.printIndex();

	if(!PosOperation.equale(newP, this.getPosition(e))) {
	    /*run move*/
	    if(index.moveEntity(oldP.getX(), oldP.getY(), newP.getX(), newP.getY())) {
		e.getComponent(Position.class).setX(newP.getX());
		e.getComponent(Position.class).setY(newP.getY());

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

	int trueX = newP.getX();
	int trueY = newP.getY();
    
	// System.out.print("Diff P : ");
	// dirP.print();

	if(newP.getX() >= 14) { newP.setX(14); }
	if(newP.getX() <= 0) { newP.setX(0); }
	if(newP.getY() >= 14) { newP.setY(14); }
	if(newP.getY() <= 0) { newP.setY(0); }

	Position freeP = wayFreeTo(oldP, newP);
	Position nextEP = PosOperation.sum(freeP, dirP);

	if(!PosOperation.equale(freeP, newP)) {
	    if(!positionIsVoid(nextEP)) {
		Entity nextE = index.getEntity(nextEP.getX(), nextEP.getY());

		if(this.isPusherEntity(e))
		{	
		    if(this.isPushableEntity(nextE)) {
			if(this.moveEntity(nextE, dirP))
			{
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
	int base, max;
	
	if(delta.getX() > 0) {
	    base = begin.getX();
	    max = end.getX();
	    return this.testObjOnWayX(base, max, begin.getY(), -1);
	}
	else if(delta.getX() < 0) {
	    base = begin.getX();
	    max = end.getX();
	    return this.testObjOnWayX(base, max, begin.getY(), 1);
	}
	else if(delta.getY() > 0) {
	    base = begin.getY();
	    max = end.getY();
	    return this.testObjOnWayY(base, max, begin.getX(), -1);
	}
	else {
	    base = begin.getY();
	    max = end.getY();
	    return this.testObjOnWayY(base, max, begin.getX(), 1);
	}
    }

    private Position testObjOnWayX(int base, int max, int y, int mul) {
	Position old = new Position(base, y);

	for(int i = base; i != max && i != 15 && i != 0; i += 1 * mul) {
	    Entity e = this.index.getEntity(i, y);
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
	
	for(int i = base; i != max && i != 15 && i != 0; i += 1 * mul) {
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

    private void printIndex() {
	for(int i = 0; i != 15; i++) {
	    for(int j = 0; j != 15; j++) {
		Entity e = index.getEntity(i, j);
		if(e != null) {
		    System.out.printf("[%d, %d] : ", i, j);
		    System.out.print(e);
		    System.out.print("\n");
		}
	    }
	}
    }
}
