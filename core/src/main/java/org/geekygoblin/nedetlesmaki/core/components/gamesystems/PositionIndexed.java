/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.core.components.gamesystems;

import org.geekygoblin.nedetlesmaki.core.utils.Square;
import com.artemis.Entity;

import org.geekygoblin.nedetlesmaki.core.manager.EntityIndexManager;

/**
 *
 * @author pierre
 */
public class PositionIndexed extends Position {

    private final EntityIndexManager index;

    public PositionIndexed(int x, int y, EntityIndexManager index) {
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

    public void setPosition(int x, int y) {
        Square s = this.index.getSquare(this.x, this.y);
        
        if(s == null) { return;}
        
        Entity tmp = s.getEntity();
        this.index.getSquare(this.x, this.y).setEntity(null);
        super.setX(x);
        super.setY(y);
        if(this.index.getSquare(x, y) == null)
        {
            this.index.setSquare(x, y, new Square());
        }
        this.index.getSquare(this.x, this.y).setEntity(tmp);
    }
}
