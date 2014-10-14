/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geekygoblin.nedetlesmaki.core.components.gamesystems;

import com.artemis.Entity;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.backend.Position;
import org.geekygoblin.nedetlesmaki.core.backend.PositionIndexed;

/**
 *
 * @author pierre
 */
public class Wall extends GameObject{

    public Wall(PositionIndexed pos, Entity entity, LevelIndex index) {
        super(pos, entity, index);
    }

    @Override
    public boolean moveTo(Position diff) {
        return false;
    }

    @Override
    public void save(Memento m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Memento undo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
