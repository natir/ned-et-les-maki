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

import com.artemis.Entity;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.backend.PositionIndexed;
import org.geekygoblin.nedetlesmaki.core.constants.ColorType;

/**
 *
 * @author pierre
 */
public class BlueMaki extends GameObject {

    private boolean boosted;
    
    public BlueMaki(PositionIndexed pos, Entity entity, LevelIndex index) {
        super(pos, entity, index);
        this.boosted = false;
    }

    @Override
    public boolean stopOnPlate() {
        return true;
    }

    @Override
    public ColorType getColorType() {
        return ColorType.blue;
    }

    @Override
    public boolean isDestroyer() {
        return boosted;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isPusher() {
        return boosted;
    }

    @Override
    public boolean isBoosted() {
        return boosted;
    }

    @Override
    public void setPusher(boolean b) {
        this.boosted = true;
    }

    @Override
    public int getBoost() {
        return 3;
    }

    @Override
    public int getMovable() {
        return 200;
    }

}
