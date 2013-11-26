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
package org.geekygoblin.nedetlesmaki.game.utils;

import java.util.ArrayList;

import com.artemis.Entity;

import org.geekygoblin.nedetlesmaki.game.constants.AnimationType;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Position;

/**
 *
 * @author natir
*/
public class Mouvement {

    private Entity e;
    private final ArrayList<Position> lP;
    private final ArrayList<AnimationType> lA;

    public Mouvement(Entity e) {
	this.e = e;
	this.lP = new ArrayList();
	this.lA = new ArrayList();
    }

    public void setEntity(Entity e) {
	this.e = e;
    }

    public Mouvement addPosition(Position p) {
	this.lP.add(p);
	return this;
    }

    public Mouvement addAnimation(AnimationType a) {
	this.lA.add(a);
	return this;
    }

    public Entity getEntity() {
	return this.e;
    }

    public ArrayList<Position> getPositionList() {
	return this.lP;
    }

    public ArrayList<AnimationType> getAnimationList() {
	return this.lA;
    }
}
