package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;
import com.artemis.Entity;

/**
 * 
 * @author natir
 */
public class EntityPosIndex extends Component {
    
    private Entity[][] index;

    public EntityPosIndex() {
	this.index = new Entity[15][15];
    }

    public Entity getEntityWithPos(int x, int y) {
	return index[x][y];
    }

    public void setEntityWithPos(int x, int y, Entity eId) {
	index[x][y] = eId;
    }
}
