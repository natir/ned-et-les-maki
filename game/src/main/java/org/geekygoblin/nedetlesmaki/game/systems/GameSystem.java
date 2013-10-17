package org.geekygoblin.nedetlesmaki.game.systems;

import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.ComponentMapper;

import org.geekygoblin.nedetlesmaki.game.components.Pushable;
import org.geekygoblin.nedetlesmaki.game.components.Pusher;
import org.geekygoblin.nedetlesmaki.game.components.Position;
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

    private EntityPosIndexSystem index;

    public GameSystem(EntityPosIndexSystem index) {
	this.index = index;
    }

    public boolean moveEntity(Entity e, Position newP) {
	
	/*New pos is void*/
	if(positionIsVoid(newP)) {
	    Position oldP = this.positionMapper.get(e);
	    index.saveWorld();
	    index.moveEntity(oldP.getX(), oldP.getY(), newP.getX(), newP.getY());
	    return true;
	}
	else {
	    if(this.isPusherEntity(e)) {
		Entity otherE = this.index.getEntity(newP.getX(), newP.getY());
		if(!otherE.isActive()) {
		    return false;
		}
		if(this.isPushableEntity(otherE)) {
		    return this.moveEntity(otherE, PosOperation.sum(PosOperation.deduction(positionMapper.get(e), newP), newP));
		}
	    }
	}
	
	return false;
    }

    public boolean positionIsVoid(Position p) {
	Entity tmpE = index.getEntity(p.getX(), p.getY());
	if(!tmpE.isActive()) {
	    return false;
	}
	
	return true;
    }

    public boolean isPushableEntity(Entity e) {
	Pushable p = this.pushableMapper.getSafe(e);
	if(p != null) {
	    if(this.pushableMapper.getSafe(e).isPushable()) {
		return true;
	    }
	}

	return false;
    }

    public boolean isPusherEntity(Entity e) {
	Pusher p = this.pusherMapper.getSafe(e);
	if(p != null) {
	    if(this.pusherMapper.getSafe(e).isPusher()) {
		return true;
	    }
	}

	return false;
    }
}
