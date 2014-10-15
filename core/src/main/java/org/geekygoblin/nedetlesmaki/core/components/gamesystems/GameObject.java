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
import java.util.ArrayList;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.backend.Position;
import org.geekygoblin.nedetlesmaki.core.backend.PositionIndexed;

/**
 *
 * @author pierre
 */
public abstract class GameObject extends Component {

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

    public abstract Position moveTo(Position diff);

    public abstract void save(Memento m);

    public abstract Memento undo();

    protected static class Memento {

        private String state;

        public Memento(String stateToSave) {

        }

        public String getSavedState() {
            return state;
        }
    }

    protected class GameObjectGuard {

        private final ArrayList savedStates = new ArrayList();

        public void addSavedStates(Object m) {
            savedStates.add(m);
        }

        public Object getSavedStates(int index) {
            return this.savedStates.get(index);
        }
    }
}
