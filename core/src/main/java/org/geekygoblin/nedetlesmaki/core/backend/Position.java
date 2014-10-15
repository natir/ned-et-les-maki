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
package org.geekygoblin.nedetlesmaki.core.backend;

/**
 *
 * @author natir
 */
public class Position {

    protected int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static Position sum(Position p1, Position p2) {
        return new Position(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }

    public static Position deduction(Position p1, Position p2) {
        return new Position(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    public static Position multiplication(Position p1, int mul) {
        return new Position(p1.getX() * mul, p1.getY() * mul);
    }

    public static Position getUp() {
        return new Position(0, -1);
    }

    public static Position getDown() {
        return new Position(0, 1);
    }

    public static Position getLeft() {
        return new Position(-1, 0);
    }

    public static Position getRight() {
        return new Position(1, 0);
    }

    public static Position getVoid() {
        return new Position(0, 0);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.x;
        hash = 43 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        final Position other = (Position) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

}
