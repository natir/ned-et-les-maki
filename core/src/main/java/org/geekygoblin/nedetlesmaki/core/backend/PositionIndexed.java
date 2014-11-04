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

import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.GameObject;

public class PositionIndexed extends Position {

    private final LevelIndex index;

    public PositionIndexed(int x, int y, LevelIndex index) {
        super(x, y);
        this.index = index;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
    }

    public boolean setPosition(int x, int y) {
        Square s = this.index.getSquare(this.x, this.y);

        if (s == null) {
            return false;
        }

        GameObject tmp = s.getGameObject();
        if (tmp == null) {
            return false;
        }
        this.index.getSquare(this.x, this.y).setGameObject(null);
        super.setX(x);
        super.setY(y);
        if (this.index.getSquare(x, y) == null) {
            this.index.setSquare(x, y, new Square());
        }

        Square new_square = this.index.getSquare(x, y);
        if (new_square != null) {
            new_square.setGameObject(tmp);
            return true;
        } else {
            return false;
        }
    }

    public boolean setPosition(Position pos) {
        return this.setPosition(pos.getX(), pos.getY());
    }

    public void reIndex(GameObject go) {
        Square s = this.index.getSquare(this.x, this.y);

        if (s == null) {
            return;
        }

        s.setGameObject(go);
    }
}
