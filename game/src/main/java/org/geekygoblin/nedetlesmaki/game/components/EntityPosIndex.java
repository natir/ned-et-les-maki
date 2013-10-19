package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 * 
 * @author natir
 */
public class EntityPosIndex extends Component {
    
    private int[][] index;

    public EntityPosIndex() {
	this.index = new int[15][15];
    }

    public int getEntityWithPos(int x, int y) {
	return index[x][y];
    }

    public void setEntityWithPos(int x, int y, int eId) {
	index[x][y] = eId;
    }
}
