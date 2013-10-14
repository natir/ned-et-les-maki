package org.geekygoblin.nedetlesmaki.game.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

import org.geekygoblin.nedetlesmaki.game.components.EntityPosIndex;
import org.geekygoblin.nedetlesmaki.game.components.Indexed;
import org.geekygoblin.nedetlesmaki.game.components.Position;

/**
 *
 * @author natir
 */
public class EntityPosIndexSystem extends EntityProcessingSystem {
    
    private Entity index;

    public EntityPosIndexSystem()
	{
	    super(Aspect.getAspectForAll(Position.class, Indexed.class));
	}

    public void setIndex(Entity index)
	{
	    this.index = index;
	}

    @Override
    protected void process(Entity e) {
	Position p = e.getComponent(Position.class);
	this.index.getComponent(EntityPosIndex.class).setEntityWithPos(p.getX(), p.getY(), e.getId());
    }
}
