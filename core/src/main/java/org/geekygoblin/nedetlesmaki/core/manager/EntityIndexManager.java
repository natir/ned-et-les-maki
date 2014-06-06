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

import java.util.Stack;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.artemis.Entity;
import com.artemis.EntityManager;
import com.artemis.ComponentMapper;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;

import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.Group;
import org.geekygoblin.nedetlesmaki.core.utils.Mouvement;
import org.geekygoblin.nedetlesmaki.core.constants.ColorType;
import im.bci.jnuit.artemis.sprite.Sprite;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.BlockOnPlate;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Boostable;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Color;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Destroyable;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Destroyer;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.PositionIndexed;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Square;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Movable;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Plate;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Pushable;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Pusher;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Stairs;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.StopOnPlate;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Rooted;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.CatchNed;

/**
 *
 * @author natir
 */
@Singleton
public class EntityIndexManager extends EntityManager {

    private Square[][] index;

    ComponentMapper<Pushable> pushableMapper;
    ComponentMapper<Pusher> pusherMapper;
    ComponentMapper<Position> positionMapper;
    ComponentMapper<PositionIndexed> positionIndexedMapper;
    ComponentMapper<Movable> movableMapper;
    ComponentMapper<Plate> plateMapper;
    ComponentMapper<Color> colorMapper;
    ComponentMapper<Boostable> boostMapper;
    ComponentMapper<BlockOnPlate> blockOnPlateMapper;
    ComponentMapper<StopOnPlate> stopOnPlateMapper;
    ComponentMapper<Destroyer> destroyerMapper;
    ComponentMapper<Destroyable> destroyableMapper;
    ComponentMapper<Stairs> stairsMapper;
    ComponentMapper<Rooted> rootedMapper;
    ComponentMapper<CatchNed> catchMapper;
    ComponentMapper<Sprite> spriteMapper;

    @Inject
    public EntityIndexManager() {
        super();
        this.index = new Square[15][15];
    }

    @Override
    protected void initialize() {
        super.initialize();
        pushableMapper = world.getMapper(Pushable.class);
        pusherMapper = world.getMapper(Pusher.class);
        positionMapper = world.getMapper(Position.class);
        positionIndexedMapper = world.getMapper(PositionIndexed.class);
        movableMapper = world.getMapper(Movable.class);
        plateMapper = world.getMapper(Plate.class);
        colorMapper = world.getMapper(Color.class);
        boostMapper = world.getMapper(Boostable.class);
        blockOnPlateMapper = world.getMapper(BlockOnPlate.class);
        stopOnPlateMapper = world.getMapper(StopOnPlate.class);
        destroyerMapper = world.getMapper(Destroyer.class);
        destroyableMapper = world.getMapper(Destroyable.class);
        stairsMapper = world.getMapper(Stairs.class);
        rootedMapper = world.getMapper(Rooted.class);
        catchMapper = world.getMapper(CatchNed.class);
        spriteMapper = world.getMapper(Sprite.class);
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

    public boolean isStairs(Position newP) {
        Square s = this.getSquare(newP.getX(), newP.getY());

        Entity stairsEntity = s.getEntity();

        if (stairsEntity.getComponent(Stairs.class) == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isPushableEntity(Entity e) {
        Pushable p = this.pushableMapper.getSafe(e);

        if (p != null) {
            if (p.isPushable()) {
                return p.isPushable();
            }
        }

        return false;
    }

    public boolean isPusherEntity(Entity e) {
        Pusher p = this.pusherMapper.getSafe(e);

        if (p != null) {
            if (p.isPusher()) {
                return p.isPusher();
            }
        }

        return false;
    }

    public boolean isDestroyer(Entity e) {
        Destroyer d = this.destroyerMapper.getSafe(e);

        if (d != null) {
            if (d.destroyer()) {
                return true;
            }
        }

        return false;
    }

    public boolean isRooted(Entity e) {
        return this.rootedMapper.has(e);
    }

    public boolean isDestroyable(Entity e) {
        Destroyable d = this.destroyableMapper.getSafe(e);

        if (d != null) {
            if (d.destroyable()) {
                return true;
            }
        }

        return false;
    }

    public boolean stopOnPlate(Entity e) {
        StopOnPlate p = stopOnPlateMapper.getSafe(e);

        if (p == null) {
            return false;
        }

        return p.stop();
    }

    public boolean isBlockOnPlate(Entity e) {
        BlockOnPlate p = blockOnPlateMapper.getSafe(e);

        if (p == null) {
            return false;
        }

        return p.block();
    }

    public boolean isBoostable(Entity e) {
        Boostable b = boostMapper.getSafe(e);

        if (b == null) {
            return false;
        }

        return b.getNbCase() != 20;
    }

    public boolean isBoosted(Entity e) {
        boolean boostable = this.isBoostable(e);
        boolean pusher = this.isPusherEntity(e);

        return boostable && pusher;
    }

    public boolean isCatchNed(Entity e) {
        CatchNed b = catchMapper.getSafe(e);

        if (b == null) {
            return false;
        }

        return b.catchNed();
    }

    public boolean nedIsCatched(Entity e) {
        CatchNed b = catchMapper.getSafe(e);

        if (b == null) {
            return false;
        }

        return b.nedIsCatch();
    }

    public int getMovable(Entity e) {
        Movable m = this.movableMapper.getSafe(e);

        if (m != null) {
            return m.getNbCase();
        }

        return 0;
    }

    public int getBoost(Entity e) {
        Boostable b = this.boostMapper.getSafe(e);

        if (b != null) {
            return b.getNbCase();
        }

        return 20;
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

    public Color getColor(Entity e) {
        Color c = this.colorMapper.getSafe(e);
        if (c == null) {
            return null;
        }

        return c;
    }

    public ColorType getColorType(Entity e) {
        Color c = this.getColor(e);

        if (c == null) {
            return ColorType.no;
        } else {
            return c.getColor();
        }
    }

    public Plate getPlate(Entity e) {
        Plate p = plateMapper.getSafe(e);

        if (p == null) {
            return null;
        }

        return p;
    }

    public Stairs getStairs(Entity e) {

        Stairs st = this.stairsMapper.getSafe(e);

        if (st == null) {
            return null;
        }

        return st;
    }

    public Rooted getRooted(Entity e) {

        Rooted st = this.rootedMapper.getSafe(e);

        if (st == null) {
            return null;
        }

        return st;
    }

    public CatchNed getCatchNed(Entity e) {

        CatchNed st = this.catchMapper.getSafe(e);

        if (st == null) {
            return null;
        }

        return st;
    }

    public Sprite getSprite(Entity e) {
        return this.spriteMapper.getSafe(e);
    }
}
