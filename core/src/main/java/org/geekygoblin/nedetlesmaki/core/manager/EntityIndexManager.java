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
package org.geekygoblin.nedetlesmaki.core.manager;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.artemis.Entity;
import com.artemis.EntityManager;
import com.artemis.ComponentMapper;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;

import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.Group;

import org.geekygoblin.nedetlesmaki.core.utils.Square;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.PositionIndexed;

/**
 *
 * @author natir
 */
@Singleton
public class EntityIndexManager extends EntityManager {

    private Square[][] index;

    ComponentMapper<Position> positionMapper;
    ComponentMapper<PositionIndexed> positionIndexedMapper;

    @Inject
    public EntityIndexManager() {
        super(15 * 15);
        this.index = new Square[15][15];
    }

    @Override
    protected void initialize() {
        super.initialize();
        positionMapper = world.getMapper(Position.class);
        positionIndexedMapper = world.getMapper(PositionIndexed.class);
    }

    @Override
    public void added(Entity e) {
        Position p = this.getPositionIndexed(e);

        if (p != null) {
            if (this.index[p.getX()][p.getY()] == null) {
                this.index[p.getX()][p.getY()] = new Square();
            }

            this.index[p.getX()][p.getY()].setEntity(e);
            super.added(e);
        }
    }

    @Override
    public void deleted(Entity e) {
        Position p = this.getPositionIndexed(e);

        if (p != null) {
            Square s = this.index[p.getX()][p.getY()];
            if (s != null) {
                s.setEntity(null);
            }

            super.deleted(e);
        }
    }

    @Override
    public void disabled(Entity e) {
        Position p = this.getPositionIndexed(e);

        if (p != null) {
            Square s = this.index[p.getX()][p.getY()];
            if (s != null) {
                s.setEntity(null);
            }

            super.disabled(e);
        }
    }

    @Override
    public void enabled(Entity e) {
        Position p = this.getPositionIndexed(e);

        if (p != null) {
            if (this.index[p.getX()][p.getY()] == null) {
                this.index[p.getX()][p.getY()] = new Square();
            }

            e.enable();
            this.index[p.getX()][p.getY()].setEntity(e);
            super.enabled(e);
        }
    }

    public void addPlate(Entity e) {
        Position p = this.getPosition(e);

        if (p != null) {
            if (this.index[p.getX()][p.getY()] == null) {
                this.index[p.getX()][p.getY()] = new Square();
            }

            this.index[p.getX()][p.getY()].setPlate(e);
            super.added(e);
        }
    }

    public Entity getEntity(int x, int y) {

        Square test = this.getSquare(x, y);

        if (test != null) {
            return test.getEntity();
        } else {
            return null;
        }
    }

    public Square getSquare(int x, int y) {

        if (x > 14 || x < 0 || y > 14 || y < 0) {
            return null;
        }

        Square test = index[x][y];

        if (test != null) {
            return test;
        } else {
            return null;
        }
    }

    public void setSquare(int x, int y, Square s) {
        index[x][y] = s;
    }

    public void cleanIndex() {
        this.index = new Square[15][15];
    }

    public ImmutableBag<Entity> getAllPlate() {
        return world.getManager(GroupManager.class).getEntities(Group.PLATE);
    }

    public ImmutableBag<Entity> getAllStairs() {
        return world.getManager(GroupManager.class).getEntities(Group.STAIRS);
    }

    public Entity getNed() {
        return ((NedGame) world).getNed();
    }

    //Utills 
    public boolean positionIsVoid(Position p) {
        Square s = this.getSquare(p.getX(), p.getY());

        if (s != null) {
            return s.getEntity() == null;
        }

        return true;
    }

    public Position getPosition(Entity e) {
        Position p = this.positionMapper.getSafe(e);

        if (p != null) {
            return p;
        }

        return null;
    }

    public PositionIndexed getPositionIndexed(Entity e) {
        PositionIndexed p = this.positionIndexedMapper.getSafe(e);

        if (p != null) {
            return p;
        }

        return null;
    }
}
