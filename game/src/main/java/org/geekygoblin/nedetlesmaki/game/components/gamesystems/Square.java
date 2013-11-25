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
package org.geekygoblin.nedetlesmaki.game.components.gamesystems;

import java.util.Iterator;
import java.util.ArrayList;

import com.artemis.Component;
import com.artemis.Entity;

public class Square extends Component {

    private final ArrayList<Entity> array;

    public Square() {
	this.array = new ArrayList();
    }

    public boolean add(Entity e) {
	return this.array.add(e);
    }

    public void remove(Entity del) {
	for(Iterator it = this.array.iterator(); it.hasNext();) {
	    Entity e = (Entity) it.next();
	    
	    if(e.getId() == del.getId()) {
		it.remove();
	    }
	}
    }

    public ArrayList<Entity> getWith(Class componentClass) {
	ArrayList<Entity> ret = new ArrayList();
        for (Entity e : this.array) {
            Object c = e.getComponent(componentClass);
            
            if(c != null) {
                ret.add(e);
            }
        }

	return ret;
    }

    public ArrayList<Entity> getAll() {
	return this.array;
    }
}
