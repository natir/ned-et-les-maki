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

public class Case extends Component {
    
    private Entity e;
    private boolean p;

    public Case() {
	this.e = null;
	this.p = false;
    }
    
    public Case(Entity e) {
	this.e = e;
	this.p = false;
    }

    public Case(Entity e, boolean p) {
	this.e = e;
	this.p = p;
    }

    public Case(Case c) {
	if(c != null) {
	    this.e = c.getEntity();
	    this.p = c.plateInThis();
	}
	else {
	    this.e = null;
	    this.p = false;  
	}
    }

    public Entity getEntity() {
	return this.e;
    }

    public void setEntity(Entity e) {
	this.e = e;
    }

    public boolean plateInThis() {
	return this.p;
    }

    public void setPlate(boolean p) {
	this.p = p;
    }
}
