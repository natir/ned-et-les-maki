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

import com.artemis.Component;
import com.artemis.Entity;

import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Plate;
import org.geekygoblin.nedetlesmaki.game.constants.ColorType;

public class Square extends Component {

    private Entity e;
    private Plate p;

    public Square() {
	this.e = null;
	this.p = new Plate(new Color(ColorType.no));
    }
    
    public Square(Entity e) {
	this.e = e;
	this.p = new Plate(new Color(ColorType.no));
    }

    public Square(Entity e, Plate p) {
	this.e = e;
	this.p = p;
    }

    public Square(Square c) {
	if(c != null) {
	    this.e = c.getEntity();
	    this.p = c.getPlate();
	}
	else {
	    this.e = null;
	    this.p = new Plate(new Color(ColorType.no));
	}
    }

    public Entity getEntity() {
	return this.e;
    }

    public void setEntity(Entity e) {
	this.e = e;
    }

    public Plate getPlate() {
	return this.p;
    }

    public void setPlate(Plate p) {
	this.p = p;
    }
}
