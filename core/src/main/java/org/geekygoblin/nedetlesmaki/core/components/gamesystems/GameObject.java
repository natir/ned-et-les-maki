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
package org.geekygoblin.nedetlesmaki.core.components.gamesystems;

import com.artemis.Component;
import com.artemis.Entity;
import java.util.Stack;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.backend.Position;
import org.geekygoblin.nedetlesmaki.core.backend.PositionIndexed;

/**
 *
 * @author pierre
 */
public abstract class GameObject extends Component {

    protected enum MoveType {

        // All
        NO,
        // Maki
        VALIDATE,
        UNVALIDATE,
        //Box
        BOOM,
        DESTROY,
        UNDESTROY,
        //Ned
        WALK,
        FLY,
        MOUNT,
        //Ned and Stone
        PUSH,
        //Stairs
        OPEN,
        CLOSE,
        //Orange Maki and Ned
        WAIT,
        //Stone
        OUT,
    }

    protected Entity entity;
    protected PositionIndexed pos;
    protected LevelIndex index;
    protected GameObjectGuard guard;

    public GameObject(PositionIndexed pos, Entity entity, LevelIndex index) {
        this.pos = pos;
        this.entity = entity;
        this.index = index;
        this.guard = new GameObjectGuard();
    }

    public PositionIndexed getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos.setPosition(pos);
    }

    public Entity getEntity() {
        return entity;
    }

    public abstract Position moveTo(Position diff, float wait_time);

    public void save(Memento m) {
        if (m != null) {
            this.guard.addSavedStates(m);
        }
    }

    public abstract void undo();

    protected static class Memento {

        private Position diff;
        private MoveType type;
        private GameObject next;

        public Memento(Position diff, MoveType type, GameObject next) {
            this.diff = diff;
            this.type = type;
            this.next = next;
        }

        public Position getDiff() {
            return diff;
        }

        public void setDiff(Position diff) {
            this.diff = diff;
        }

        public MoveType getType() {
            return type;
        }

        public void setType(MoveType type) {
            this.type = type;
        }

        public GameObject getNext() {
            return next;
        }

        public void setNext(GameObject next) {
            this.next = next;
        }
    }

    protected class GameObjectGuard {

        private final Stack savedStates = new Stack();

        public void addSavedStates(Object m) {
            savedStates.add(m);
        }

        public Object pullSavedStates() {
            if (!savedStates.empty()) {
                return this.savedStates.pop();
            } else {
                return null;
            }
        }
    }
}
