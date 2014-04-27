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
package org.geekygoblin.nedetlesmaki.core.utils;

import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Position;

/**
 *
 * @author natir
*/
public class PosOperation {

    public static Position sum(Position p1, Position p2) {
	return new Position(p1.getX()+p2.getX(), p1.getY()+p2.getY());
    }

    public static Position deduction(Position p1, Position p2) {
	return new Position(p1.getX()-p2.getX(), p1.getY()-p2.getY());
    }

    public static Position multiplication(Position p1, int mul) {
	return new Position(p1.getX()*mul, p1.getY()*mul);
    }

    public static boolean equale(Position p1, Position p2) {
	if(p1.getX() != p2.getX()) {
	    return false;
	}
	else if(p1.getY() != p2.getY()) {
	    return false;
	}
	else {
	    return true;
	}
    }
}
