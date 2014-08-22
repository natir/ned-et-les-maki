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

import javax.inject.Inject;
import javax.inject.Singleton;

import org.geekygoblin.nedetlesmaki.core.backend.Position;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;

import org.geekygoblin.nedetlesmaki.core.constants.GameObjectType;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.GameObject;

/**
 *
 * @author pierre
 */
@Singleton
public class Backend {

    private LevelIndex index;

    @Inject
    public Backend(LevelIndex index) {
        this.index = index;
    }

    public void moveGameObject(GameObject go, Position relative) {
        this.runMoveByType(go, relative);
    }

    private void runMoveByType(GameObject go, Position relative) {

        if (go.getType().equals(GameObjectType.NED)) {
            this.moveNed(go, relative);
        } else if (go.getType().equals(GameObjectType.GREEN_MAKI)) {
            this.moveGreenMaki(go, relative);
        } else if (go.getType().equals(GameObjectType.ORANGE_MAKI)) {
            this.moveOrangeMaki(go, relative);
        } else if (go.getType().equals(GameObjectType.BLUE_MAKI)) {
            this.moveBlueMaki(go, relative);
        } else if (go.getType().equals(GameObjectType.BOX)) {
            this.moveBox(go, relative);
        } else if (go.getType().equals(GameObjectType.ROOT_BOX)) {
            this.moveRootBox(go, relative);
        }
    }

    private void moveObjectTo(GameObject go, Position pos) {
        go.getPos().setPosition(pos);
    }

    private void moveNed(GameObject go, Position relative) {

        Position newPos = Position.sum(go.getPos(), relative);

        if (this.index.positionIsVoid(newPos)) {
            this.moveObjectTo(go, newPos);
        } else {
            this.moveGameObject(this.index.getGameObject(newPos), relative);

            if (this.index.positionIsVoid(newPos)) {
                this.moveObjectTo(go, newPos);
            }
        }
    }

    private void moveGreenMaki(GameObject go, Position relative) {

        Position newPos = Position.sum(go.getPos(), relative);

        if (this.index.positionIsVoid(newPos)) {
            this.moveObjectTo(go, newPos);
        } else {
            this.moveGameObject(this.index.getGameObject(newPos), relative);

            if (this.index.positionIsVoid(newPos)) {
                this.moveObjectTo(go, newPos);
            }
        }
    }

    private void moveOrangeMaki(GameObject go, Position relative) {

        for(Position newPos = Position.sum(go.getPos(), relative); this.index.positionIsVoid(newPos); newPos = Position.sum(go.getPos(), relative)) {
            this.moveObjectTo(go, newPos);
        }
    }

    private void moveBlueMaki(GameObject go, Position relative) {

        for(Position newPos = Position.sum(go.getPos(), relative); this.index.positionIsVoid(newPos); newPos = Position.sum(go.getPos(), relative)) {
            this.moveObjectTo(go, newPos);
        }
    }

    private void moveBox(GameObject go, Position relative) {

        Position newPos = Position.sum(go.getPos(), relative);

        if (this.index.positionIsVoid(newPos)) {
            this.moveObjectTo(go, newPos);
        } else {
            this.moveGameObject(this.index.getGameObject(newPos), relative);

            if (this.index.positionIsVoid(newPos)) {
                this.moveObjectTo(go, newPos);
            }
        }
    }

    private void moveRootBox(GameObject go, Position relative) {

        Position newPos = Position.sum(go.getPos(), relative);

        if (this.index.positionIsVoid(newPos)) {
            this.moveObjectTo(go, newPos);
        } else {
            this.moveGameObject(this.index.getGameObject(newPos), relative);

            if (this.index.positionIsVoid(newPos)) {
                this.moveObjectTo(go, newPos);
            }
        }
    }
}
