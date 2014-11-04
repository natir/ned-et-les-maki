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
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.artemis.sprite.Sprite;
import im.bci.jnuit.artemis.sprite.SpritePuppetControls;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import org.geekygoblin.nedetlesmaki.core.backend.LevelIndex;
import org.geekygoblin.nedetlesmaki.core.backend.Position;
import org.geekygoblin.nedetlesmaki.core.backend.PositionIndexed;
import org.geekygoblin.nedetlesmaki.core.constants.AnimationTime;
import org.geekygoblin.nedetlesmaki.core.constants.ColorType;
import pythagoras.f.Vector3;

/**
 *
 * @author pierre
 */
public class GreenMaki extends GameObject {

    private boolean validate;
    private final IAnimationCollection animation;

    public GreenMaki(PositionIndexed pos, Entity entity, LevelIndex index, IAssets assets) {
        super(pos, entity, index);

        this.animation = assets.getAnimations("animation/maki/maki.json");
    }

    @Override
    public Position moveTo(Position diff, float wait_time) {
        Position n_pos = Position.sum(this.pos, diff);
        GameObject n_obj = this.index.getGameObject(n_pos);
        Plate c_plate = this.index.getPlate(this.pos);
        Plate n_plate = this.index.getPlate(n_pos);

        if (n_obj == null) {
            this.pos.setPosition(n_pos);
            if (n_plate != null && n_plate.getColorType() == ColorType.green) { // Move to plate
                if (this.validate) { // Actuali is in plate
                    this.run_animation(diff, MoveType.NO, wait_time);
                    this.index.setPlateValue(c_plate, false);
                    this.save(new Memento(diff, MoveType.NO, null));
                } else {
                    this.run_animation(diff, MoveType.VALIDATE, wait_time);
                    this.validate = true;
                    this.save(new Memento(diff, MoveType.VALIDATE, null));
                }
                this.index.setPlateValue(n_plate, true);
            } else { // Didn't move to plate
                if (this.validate) { // Actuali is in plate
                    this.run_animation(diff, MoveType.UNVALIDATE, wait_time);
                    this.validate = false;
                    this.index.setPlateValue(c_plate, false);
                    this.save(new Memento(diff, MoveType.UNVALIDATE, n_obj));
                } else {
                    this.run_animation(diff, MoveType.NO, wait_time);
                    this.save(new Memento(diff, MoveType.NO, n_obj));
                }
            }
            return this.pos;
        }

        return this.pos;
    }

    @Override
    public void undo() {
        Memento m = (Memento) this.guard.pullSavedStates();

        if (m == null) {
            return;
        }

        Position n_pos = Position.sum(this.pos, Position.multiplication(m.getDiff(), -1));
        MoveType type = m.getType();

        Plate c_plate = this.index.getPlate(this.pos);
        Plate n_plate = this.index.getPlate(n_pos);

        if (type == MoveType.VALIDATE) {
            this.index.setPlateValue(c_plate, false);
            this.run_animation(Position.multiplication(m.getDiff(), -1), MoveType.UNVALIDATE, 0.0f);
            this.validate = false;
        } else if (type == MoveType.UNVALIDATE) {
            this.index.setPlateValue(n_plate, true);
            this.run_animation(Position.multiplication(m.getDiff(), -1), MoveType.VALIDATE, 0.0f);
            this.validate = true;
        } else {
            this.run_animation(Position.multiplication(m.getDiff(), -1), type, 0.0f);
        }

        this.pos.setPosition(n_pos);

        if (m.getNext() != null) {
            m.getNext().undo();
        }
    }

    private void run_animation(Position diff, MoveType type, float wait_time) {
        Sprite sprite = this.entity.getComponent(Sprite.class);
        SpritePuppetControls updatable = this.entity.getComponent(SpritePuppetControls.class);

        if (updatable == null) {
            updatable = new SpritePuppetControls(sprite);
        }

        if (type == MoveType.NO) {
            updatable.waitDuring(wait_time)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base);
        } else if (type == MoveType.VALIDATE) {
            updatable.waitDuring(wait_time)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                    .startAnimation(this.animation.getAnimationByName("maki_green_one"), PlayMode.ONCE);
        } else if (type == MoveType.UNVALIDATE) {
            updatable.waitDuring(wait_time)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                    .startAnimation(this.animation.getAnimationByName("maki_green_out"), PlayMode.ONCE);
        }

        this.entity.addComponent(updatable);
        this.entity.changedInWorld();
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }
}
