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
public class Stairs extends GameObject{

    private int dir;
    private boolean open;
    
    public Stairs(PositionIndexed pos, Entity entity, LevelIndex index) {
        super(pos, entity, index);
        this.open = false;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public boolean isOpen() {
        return true;
    }
    
    public void setOpen(boolean open) {
        this.open = open;
    }
}
