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

    public EntityPosIndex(EntityPosIndex clone) {
	this.index = new Entity[15][15];
	for(int i = 0; i != 15; i++) {
	    for(int j = 0; j != 15; j++) {
		Entity e = clone.getEntityWithPos(i, j);
		this.index[i][j] = e;
	    }
	}
    }

    public Entity getEntityWithPos(int x, int y) {
	return this.index[x][y];
    }

    public boolean setEntityWithPos(int x, int y, Entity eId) {
	this.index[x][y] = eId;
	return true;
    }

    public Entity[][] getIndex() {
	return this.index;
    }

    public EntityPosIndex clone() {
        EntityPosIndex result = new EntityPosIndex(this);
	return result;
    }
}
