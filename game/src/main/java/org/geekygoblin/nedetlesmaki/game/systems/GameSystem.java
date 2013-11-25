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

	/*Check if move possible*/
	Position oldP = this.getPosition(e);
	
	ArrayList<Mouvement> mouv = new ArrayList<Mouvement>();

	for(int i = 0; i != this.getMovable(e); i++) {
	    System.out.printf("get in boucle, %d\n", i);
	    Position newP = PosOperation.sum(oldP, dirP);

	    if(this.positionIsVoid(PosOperation.sum(oldP, dirP))) {
		System.out.print("Position is void\n");
		Square s = index.getSquare(newP.getX(), newP.getY());
		
		if(!this.testBlockedPlate(e, s)) {
		    if(!PosOperation.equale(newP, this.getPosition(e))) {
			System.out.print("\nCurrent oldPos : ");
			oldP.print();
			System.out.print("\n\n");
			if(index.moveEntity(oldP.getX(), oldP.getY(), newP.getX(), newP.getY())) {
			    Position diff = PosOperation.deduction(newP, oldP);
			    Mouvement m = new Mouvement(e).addPosition(diff).addAnimation(AnimationType.no);

			    if(e == ((Game) this.world).getNed()) {
				if(diff.getX() > 0) {
				    m = new Mouvement(e).addPosition(diff).addAnimation(AnimationType.ned_right);
				}
				else if(diff.getX() < 0) {
				    m = new Mouvement(e).addPosition(diff).addAnimation(AnimationType.ned_left);
				}
				else if(diff.getY() > 0) {
				    m = new Mouvement(e).addPosition(diff).addAnimation(AnimationType.ned_down);
				}
				else if(diff.getY() < 0) {
				    m = new Mouvement(e).addPosition(diff).addAnimation(AnimationType.ned_up);
				}
			    }

			    mouv.add(m);

			    e.getComponent(Position.class).setX(newP.getX());
			    e.getComponent(Position.class).setY(newP.getY());

			    System.out.printf("\nMouv Array : %d", mouv.size());  
			    System.out.print("\n\n");
			    // oldP = PosOperation.deduction(oldP, dirP);
			    // System.out.print("\nChange oldPos : ");
			    // oldP.print();
			    // System.out.print("\n\n");
			}
		    }
		}
	    }
	    else {
		if(this.isPusherEntity(e))
		{	
		    Entity nextE = index.getEntity(newP.getX(), newP.getY()).get(0);
		    if(this.isPushableEntity(nextE)) {
			mouv.addAll(this.moveEntity(nextE, dirP));
		    }
		}
		
		return mouv;
	    }
	}

	return mouv;
    }

    private boolean testBlockedPlate(Entity eMove, Square obj) {
	System.out.print("testBlockedPlate call : ");
	System.out.print(eMove);
	System.out.print(" ");
	System.out.print(obj);
	System.out.print("\n");
	
	if(obj == null) {
	    return false;
	}

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

    public boolean positionIsVoid(Position p) {
	ArrayList<Entity> tmpE = index.getEntity(p.getX(), p.getY());
	System.out.print("Arrray : ");
	System.out.print(tmpE);
	System.out.print("\n");
	if(tmpE != null) {
	    System.out.print("Ouups\n");
	    if(tmpE.size() != 0) {
		System.out.printf("Size : %d\n", tmpE.size());
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
