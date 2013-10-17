package org.geekygoblin.nedetlesmaki.game.systems;

import java.util.Stack;

import com.artemis.Aspect;
import com.artemis.Entity;

import org.geekygoblin.nedetlesmaki.game.components.EntityPosIndex;
import org.geekygoblin.nedetlesmaki.game.components.Indexed;
import org.geekygoblin.nedetlesmaki.game.components.Position;

/**
 *
 * @author natir
 */
public class EntityPosIndexSystem {
    
    private Entity index;
    private Stack<Entity> oldIndex;

    public EntityPosIndexSystem(Entity index) {
	    this.index = index;
	    this.oldIndex = new Stack<Entity>();
	}

    public boolean saveWorld() {
	this.oldIndex.add(this.index);
	return true;
    }

    public boolean addEntity(int x, int y, Entity eId) {
        index.getComponent(EntityPosIndex.class).setEntityWithPos(x, y, eId);
	return true;
    }

    public boolean removeEntity(int x, int y) {
	
	index.getComponent(EntityPosIndex.class).getEntityWithPos(x, y).deleteFromWorld();
	return true;
    }

    public Entity getEntity(int x, int y) {
	return index.getComponent(EntityPosIndex.class).getEntityWithPos(x, y);
    }

    public boolean moveEntity(int x1, int y1, int x2, int y2) {
	Entity tmpE = index.getComponent(EntityPosIndex.class).getEntityWithPos(x1, y1);
	this.removeEntity(x1, y1);
	this.addEntity(x2, y2, tmpE);
	
	return true;
    }

    public Entity getLastWorld() {
	return this.oldIndex.peek();
    }

    public Entity getThisWorld() {
	return this.index;
    }
}
