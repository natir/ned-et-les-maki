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

import com.artemis.Entity;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.GameObject;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Plate;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Stairs;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Wall;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Stone;
import org.geekygoblin.nedetlesmaki.core.constants.ColorType;

/**
 *
 * @author natir
 */
@Singleton
public class LevelIndex {

    private Square[][] index;
    private int height, width;
    private Entity ned;
    private Stairs stairs;
    private final ArrayList<Plate> allPlate;
    private final ArrayList<Plate> greenPlate;
    private final ArrayList<Plate> orangePlate;
    private final ArrayList<Plate> bluePlate;
    private final ArrayList<Stone> greenStone;
    private final ArrayList<Stone> orangeStone;
    private final ArrayList<Stone> blueStone;

    @Inject
    public LevelIndex() {
        this.height = -1;
        this.width = -1;
        this.allPlate = new ArrayList<Plate>();
        this.greenPlate = new ArrayList<Plate>();
        this.orangePlate = new ArrayList<Plate>();
        this.bluePlate = new ArrayList<Plate>();
        this.greenStone = new ArrayList<Stone>();
        this.orangeStone = new ArrayList<Stone>();
        this.blueStone = new ArrayList<Stone>();
    }

    public void initialize(int height, int width) {
        this.height = height;
        this.width = width;
        this.index = new Square[this.height][this.width];
    }

    public void added(GameObject e, Position p) {
        if (p != null) {
            if (this.index[p.getX()][p.getY()] == null) {
                this.index[p.getX()][p.getY()] = new Square();
            }

            this.index[p.getX()][p.getY()].setGameObject(e);
        }
    }

    public void deleted(Position p) {
        if (p != null) {
            Square s = this.index[p.getX()][p.getY()];
            if (s != null) {
                s.setGameObject(null);
            }
        }
    }

    public void enabled(GameObject e, Position p) {
        if (p != null) {
            if (this.index[p.getX()][p.getY()] == null) {
                this.index[p.getX()][p.getY()] = new Square();
            }

            this.index[p.getX()][p.getY()].setGameObject(e);
        }
    }

    public void addPlate(Plate e, Position p) {
        if (p != null) {
            if (this.index[p.getX()][p.getY()] == null) {
                this.index[p.getX()][p.getY()] = new Square();
            }

            this.index[p.getX()][p.getY()].setPlate(e);
            this.allPlate.add(e);

            if (e.getColorType() == ColorType.green) {
                this.greenPlate.add(e);
            } else if (e.getColorType() == ColorType.orange) {
                this.orangePlate.add(e);
            } else {
                this.bluePlate.add(e);
            }
        }
    }

    public void addStone(Stone e, Position p) {
        this.added(e, p);
        if (e.getColorType() == ColorType.green) {
            this.greenStone.add(e);
        } else if (e.getColorType() == ColorType.orange) {
            this.orangeStone.add(e);
        } else if (e.getColorType() == ColorType.blue) {
            this.blueStone.add(e);
        }
    }

    public GameObject getGameObject(int x, int y) {
        if (this.outOfBounds(x, y)) {
            return new Wall(new PositionIndexed(x, y, null), null, null);
        }

        Square test = this.getSquare(x, y);

        if (test != null) {
            return test.getGameObject();
        } else {
            return null;
        }
    }

    public GameObject getGameObject(Position pos) {
        return this.getGameObject(pos.getX(), pos.getY());
    }

    public Plate getPlate(int x, int y) {

        Square test = this.getSquare(x, y);

        if (test != null) {
            return test.getPlate();
        } else {
            return null;
        }
    }

    public Plate getPlate(Position pos) {
        return this.getPlate(pos.getX(), pos.getY());
    }

    public Square getSquare(int x, int y) {

        if (x > this.height - 1 || x < 0 || y > this.width - 1 || y < 0) {
            return null;
        }

        Square test = index[x][y];

        if (test != null) {
            return test;
        } else {
            return null;
        }
    }

    public Square getSquare(Position pos) {
        return this.getSquare(pos.getX(), pos.getY());
    }

    public void setSquare(int x, int y, Square s) {
        if (this.outOfBounds(x, y)) {
            return;
        }

        index[x][y] = s;
    }

    public boolean positionIsVoid(Position p) {
        Square s = this.getSquare(p.getX(), p.getY());

        if (s != null) {
            return s.getGameObject() == null;
        }

        return true;
    }

    public void setNed(Entity ned) {
        this.ned = ned;
    }

    public Entity getNed() {
        return this.ned;
    }

    public void setStairs(Stairs s) {
        this.stairs = s;
    }

    public Stairs getStairs() {
        return this.stairs;
    }

    public ArrayList<Plate> getAllPlate() {
        return this.allPlate;
    }

    public void setPlateValue(Plate p, boolean b) {
        if (p != null) {
            p.setMaki(b);

            this.checkStone(this.greenPlate, this.greenStone);
            this.checkStone(this.orangePlate, this.orangeStone);
            this.checkStone(this.bluePlate, this.blueStone);

            if (this.checkEndLevel()) {
                this.stairs.setOpen(true);
            } else if (this.stairs.isOpen()) {
                this.stairs.setOpen(false);
            }
        }
    }

    private boolean checkEndLevel() {
        return this.checkPlateGroup(this.allPlate);
    }

    private void checkStone(ArrayList<Plate> plate, ArrayList<Stone> stone) {
        if (this.checkPlateGroup(plate)) {
            for (Stone s : stone) {
                s.setState(true);
                this.deleted(s.getPos());
            }
        } else {
            for (Stone s : stone) {
                s.setState(false);
                this.enabled(s, s.getPos());
            }
        }
    }

    private boolean checkPlateGroup(ArrayList<Plate> list_plate) {
        for (int i = 0; i < list_plate.size(); i++) {
            if (!list_plate.get(i).haveMaki()) {
                return false;
            }
        }

        return true;
    }

    private boolean outOfBounds(int x, int y) {
        if (x > this.height - 1 || x < 0 || y > this.width - 1 || y < 0) {
            return true;
        }
        return false;
    }
}
