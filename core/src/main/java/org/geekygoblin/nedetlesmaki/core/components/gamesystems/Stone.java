/*
 * Copyright © 2015S, Pierre Marijon <pierre@marijon.fr>
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
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.artemis.sprite.Sprite;
import im.bci.jnuit.artemis.sprite.SpritePuppetControls;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.backend.Position;
import org.geekygoblin.nedetlesmaki.core.backend.PositionIndexed;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.GameObject;
import org.geekygoblin.nedetlesmaki.core.constants.ColorType;

/**
 *
 * @author pierre
 */
public class Stone extends GameObject {

    private final IAnimationCollection animation;
    private ColorType color;
    private boolean pushed;

    public Stone(PositionIndexed pos, Entity entity, LevelIndex index, IAssets assets) {
        super(pos, entity, index);

        this.animation = assets.getAnimations("animation/steles/steles.json");
        this.pushed = false;
    }

    public ColorType getColorType() {
        return color;
    }

    public void setColorType(ColorType c) {
        this.color = c;
    }

    public boolean isPushed() {
        return this.pushed;
    }

    public void setState(boolean new_state) {
        if (new_state == this.pushed) {
            return;
        }

        this.pushed = new_state;

        if (this.pushed) {
            this.run_animations(MoveType.PUSH);
        } else {
            this.run_animations(MoveType.OUT);
        }
    }

    @Override
    public Position moveTo(Position diff, float wait_time) {
        return this.pos;
    }

    @Override
    public void undo() {
    }

    private void run_animations(MoveType type) {
        Sprite sprite = this.entity.getComponent(Sprite.class);
        SpritePuppetControls updatable = this.entity.getComponent(SpritePuppetControls.class);

        if (updatable == null) {
            updatable = new SpritePuppetControls(sprite);
        }

        if (type == MoveType.OUT) {
            if (color == ColorType.green) {
                updatable.startAnimation(this.animation.getAnimationByName("stele_green_out"), PlayMode.ONCE);
            } else if (color == ColorType.orange) {
                updatable.startAnimation(this.animation.getAnimationByName("stele_orange_out"), PlayMode.ONCE);
            } else {
                updatable.startAnimation(this.animation.getAnimationByName("stele_blue_out"), PlayMode.ONCE);
            }
        } else if (type == MoveType.PUSH) {
            if (color == ColorType.green) {
                updatable.startAnimation(this.animation.getAnimationByName("stele_green_pushed"), PlayMode.ONCE);
            } else if (color == ColorType.orange) {
                updatable.startAnimation(this.animation.getAnimationByName("stele_orange_pushed"), PlayMode.ONCE);
            } else {
                updatable.startAnimation(this.animation.getAnimationByName("stele_blue_pushed"), PlayMode.ONCE);
            }
        }

        this.entity.addComponent(updatable);
        this.entity.changedInWorld();
    }
}
