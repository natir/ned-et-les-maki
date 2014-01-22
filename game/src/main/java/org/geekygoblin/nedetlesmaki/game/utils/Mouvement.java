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
import java.util.Collections;

import com.artemis.Entity;

import org.geekygoblin.nedetlesmaki.game.constants.AnimationType;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Position;

/**
 *
 * @author natir
*/
public class Mouvement {

    private Entity e;
    private final Mouvement_struct ms;
    private final ArrayList<Mouvement_struct> list;

    public Mouvement(Entity e) {
	this.e = e;
        this.ms = new Mouvement_struct();
	this.list = new ArrayList();
    }

    public void setEntity(Entity e) {
	this.e = e;
    }

    public Mouvement setPosition(Position p) {
	this.ms.p = p;
	return this;
    }

    public Mouvement setAnimation(AnimationType a) {
	this.ms.a = a;
	return this;
    }

    public Mouvement setBeforeWait(float bw) {
	this.ms.bw = bw;
	return this;
    }

    public Mouvement saveMouvement() {
        this.list.add(this.ms);
        return this;
    }

    public Entity getEntity() {
	return this.e;
    }

    public Position getPosition(int i) {
	return this.list.get(i).p;
    }

    public AnimationType getAnimation(int i) {
	return this.list.get(i).a;
    }

    public float getBeforeWait(int i) {
        return this.list.get(i).bw;
    }

    public int size() {
        return this.list.size();
    }

    public void reverse() {
        Collections.reverse(this.list);
    }

    private class Mouvement_struct {

        public Position p;
        public AnimationType a;
        public float bw;

        public Mouvement_struct() {
            p = new Position(0, 0);
            a = AnimationType.no;
            bw = 0;
        }
    }
}
