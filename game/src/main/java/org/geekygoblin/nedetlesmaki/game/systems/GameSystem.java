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

import java.util.ArrayList;
import java.util.Iterator;

import com.artemis.Entity;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;

import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Pushable;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Pusher;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Movable;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.BlockOnPlate;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Square;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Plate;
import org.geekygoblin.nedetlesmaki.game.manager.EntityIndexManager;
import org.geekygoblin.nedetlesmaki.game.utils.PosOperation;
import org.geekygoblin.nedetlesmaki.game.utils.Mouvement;
import org.geekygoblin.nedetlesmaki.game.constants.AnimationType;
import org.geekygoblin.nedetlesmaki.game.Game;

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
    @Mapper
    ComponentMapper<BlockOnPlate> blockOnPlateMapper;

    
    @Inject
    public GameSystem(EntityIndexManager index) {
	this.index = index;
    }

    @Override
    protected void processSystem() {}
    
    public ArrayList<Mouvement> moveEntity(Entity e, Position dirP) {
	System.out.print("\n\n\nmoveEntity call : ");
	System.out.print(e);
	System.out.print(" ");
	dirP.print();

	ArrayList<Mouvement> v = new ArrayList<Mouvement>();

	/*Check if move possible*/
	Position oldP = this.getPosition(e);
	Position newP = this.testMove(e, dirP);
	Position nextP = PosOperation.sum(newP, dirP);

	ArrayList<Mouvement> tmp = this.testNextEntityMovable(newP, e, dirP);
	if(tmp != null) {
	    v.addAll(tmp);
	}

	if(!PosOperation.equale(newP, this.getPosition(e))) {
            /*run move*/
	    if(index.moveEntity(oldP.getX(), oldP.getY(), newP.getX(), newP.getY())) {
		Mouvement mouv;
		Position diff = PosOperation.deduction(newP, oldP);

		mouv = new Mouvement(e).addPosition(diff).addAnimation(AnimationType.no);

		if(e == ((Game) this.world).getNed()) {
		    if(diff.getX() > 0) {
			mouv = new Mouvement(e).addPosition(diff).addAnimation(AnimationType.ned_right);
		    }
		    else if(diff.getX() < 0) {
			mouv = new Mouvement(e).addPosition(diff).addAnimation(AnimationType.ned_left);
		    }
		    else if(diff.getY() > 0) {
			mouv = new Mouvement(e).addPosition(diff).addAnimation(AnimationType.ned_down);
		    }
		    else if(diff.getY() < 0) {
			mouv = new Mouvement(e).addPosition(diff).addAnimation(AnimationType.ned_up);
		    }
		}
	        
		v.add(mouv);
		e.getComponent(Position.class).setX(newP.getX());
		e.getComponent(Position.class).setY(newP.getY());

		return v;
	    }
	    else {
		return null;
	    }
	}
	else {
	    return null;
	}
    }

    public Position testMove(Entity e, Position dirP) {
	System.out.print("testMove call : ");
	System.out.print(e);
	System.out.print(" ");
	dirP.print();
	
	int nbCase = this.getMovable(e);
	Position oldP = this.getPosition(e);
	Position newP = PosOperation.sum(oldP, PosOperation.multiplication(dirP, nbCase));

	int trueX = newP.getX();
	int trueY = newP.getY();

	if(newP.getX() >= 14) { newP.setX(14); }
	if(newP.getX() <= 0) { newP.setX(0); }
	if(newP.getY() >= 14) { newP.setY(14); }
	if(newP.getY() <= 0) { newP.setY(0); }

	Position freeP = wayFreeTo(oldP, newP, e);
	Position nextEP = PosOperation.sum(freeP, dirP);

	if(!PosOperation.equale(freeP, newP)) {
	    return freeP;
	}

	return newP;
    }

    public Position wayFreeTo(Position begin, Position end, Entity eMove) {
	System.out.print("wayFreeTo call : ");
	begin.print();
	System.out.print(" ");
	end.print();
	System.out.print(eMove);
	System.out.print("\n");

	Position delta = PosOperation.deduction(begin, end);
	int base, max;
	
	if(delta.getX() > 0) {
	    base = begin.getX();
	    max = end.getX();
	    return this.testObjOnWayX(base, max, begin.getY(), -1, eMove);
	}
	else if(delta.getX() < 0) {
	    base = begin.getX();
	    max = end.getX();
	    return this.testObjOnWayX(base, max, begin.getY(), 1, eMove);
	}
	else if(delta.getY() > 0) {
	    base = begin.getY();
	    max = end.getY();
	    return this.testObjOnWayY(base, max, begin.getX(), -1, eMove);
	}
	else {
	    base = begin.getY();
	    max = end.getY();
	    return this.testObjOnWayY(base, max, begin.getX(), 1, eMove);
	}
    }

    public ArrayList<Mouvement> testNextEntityMovable(Position nextP, Entity cE, Position dirP) {
	
	System.out.print("testNextEntityMovablel : ");
	nextP.print();
	System.out.print(" ");
	System.out.print(cE);
	System.out.print(" ");
	dirP.print();
	System.out.print("\n");

	if(!positionIsVoid(nextP)) {
	    Entity nextE = index.getEntity(nextP.getX(), nextP.getY()).get(0);
	    
	    if(this.isPusherEntity(cE))
	    {	
		if(this.isPushableEntity(nextE)) {
		    return this.moveEntity(nextE, dirP);
		}
		else {
		    return null;
		}
	    }
	    else {
		return null;
	    }
	}
	else {
	    return null;
	}
    }

    private boolean testBlockedPlate(Entity eMove, Square obj) {
	System.out.print("testBlockedPlate call : ");
	System.out.print(eMove);
	System.out.print(" ");
	System.out.print(obj);
	System.out.print("\n");
	
	ArrayList<Entity> array = obj.getWith(Plate.class);
	
	if(array.size() == 0) {
	    System.out.print("Array equale to 0\n");
	    return false;
	}

	Entity plate = obj.getWith(Plate.class).get(0);
	Plate p = plate.getComponent(Plate.class);
	BlockOnPlate b = blockOnPlateMapper.getSafe(eMove);

	if(b == null)
	{
	    return false;
	}

	if(p.isPlate()) {
		if(b.block()) {
		    return true;
		}
	}
	
	return false;
    }

    private Position testObjOnWayX(int base, int max, int y, int mul, Entity eMove) {
	System.out.print("testObjOnWayX : ");
	System.out.printf("base %d max %d y %d mul %d ", base, max, y, mul);
	System.out.print(eMove);
	System.out.print("\n");

	Position old = new Position(base, y);

	for(int i = base; i != max + (1 * mul) && i != 15 && i != 0; i += 1 * mul) {
	    Square s = this.index.getSquare(i, y);
	    
	    if(s != null) {
		System.out.printf("S isn't null x %d", i);
		ArrayList<Entity> array = s.getWith(Position.class);
		
		if(array.size() != 0) {
		    System.out.print("Array size is 0");
		    if(this.testBlockedPlate(eMove, s)) {
			return old;
		    }
		    else {
			old = new Position(i, y);
		    }
		}
		else {
		    old = new Position(i, y);
		}
	    }
	    else {
		old = new Position(i, y);
	    }
	}
	
	return new Position(max, y);
    }

    private Position testObjOnWayY(int base, int max, int x, int mul, Entity eMove) {
	System.out.print("testObjOnWayY : ");
	System.out.printf("base %d max %d x %d mul %d ", base, max, x, mul);
	System.out.print(eMove);
	System.out.print("\n");

	Position old = new Position(x, base);

	for(int i = base; i != max + (1 * mul) && i != 15 && i != 0; i += 1 * mul) {
	    Square s = this.index.getSquare(x, i);
	
	    if(s != null) {
	        System.out.printf("S isn't null x %d", i);
	        ArrayList<Entity> array = s.getWith(Position.class);
		
		if(array.size() != 0) {
		    System.out.print("Array size is 0\n");
		    if(this.testBlockedPlate(eMove, s)) {
			return old;
		    }
		    else {
			old = new Position(x, i);
		    }
		}
		else {
		    old = new Position(x, i);
		}
	    }
	    else {
		old = new Position(x, i);
	    }    
	}
	
	return new Position(x, max);
    }

    public boolean positionIsVoid(Position p) {
	ArrayList<Entity> tmpE = index.getEntity(p.getX(), p.getY());
	
	if(tmpE != null) {
	    if(tmpE.size() != 0) {
		return false;
	    }
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

     public void printIndex() {
	 for(int i = 0; i != 15; i++) {
	     for(int j = 0; j != 15; j++) {
		 Square s = index.getSquare(i, j);
		 if(s != null) {
		     ArrayList<Entity> array = s.getAll();
		     for(Iterator it = array.iterator(); it.hasNext();) {
			 Entity e = (Entity) it.next(); 
			 System.out.printf("[%d, %d] : ", i, j);
			 System.out.print(e);
			 System.out.print("\n");
		     }
		 }
	     }
	 }
     }
}
