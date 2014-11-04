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
public class OrangeMaki extends GameObject {

    private boolean validate;
    private final IAnimationCollection animation;

    public OrangeMaki(PositionIndexed pos, Entity entity, LevelIndex index, IAssets assets) {
        super(pos, entity, index);
        this.animation = assets.getAnimations("animation/maki/maki.json");
    }

    @Override
    public Position moveTo(Position diff, float wait_time) {
        int loop_cpt;
        MoveType last_move = MoveType.NO;
        Position n_pos = Position.sum(this.pos, diff);
        GameObject n_obj = this.index.getGameObject(n_pos);
        Plate c_plate = this.index.getPlate(this.pos);
        Plate n_plate = this.index.getPlate(n_pos);

        this.run_animation(Position.getVoid(), MoveType.WAIT, wait_time); // Juste for wait_time
        for (loop_cpt = 0; n_obj == null; loop_cpt++) { // while no objet move continue
            this.pos.setPosition(n_pos);
            if (n_plate != null && n_plate.getColorType() == ColorType.orange) { // Move to plate
                if (this.validate) { // Actuali is in plate
                    this.run_animation(diff, MoveType.NO, 0.0f);
                    last_move = MoveType.NO;
                    this.index.setPlateValue(c_plate, false);
                } else {
                    this.run_animation(diff, MoveType.VALIDATE, 0.0f);
                    last_move = MoveType.VALIDATE;
                    this.validate = true;
                }
                this.index.setPlateValue(n_plate, true);
            } else { // Didn't move to plate
                if (this.validate) { // Actuali is in plate
                    this.run_animation(diff, MoveType.UNVALIDATE, 0.0f);
                    last_move = MoveType.UNVALIDATE;
                    this.validate = false;
                    this.index.setPlateValue(c_plate, false);
                } else {
                    this.run_animation(diff, MoveType.NO, 0.0f);
                    last_move = MoveType.NO;
                }
            }

            n_pos = Position.sum(this.pos, diff);
            n_obj = this.index.getGameObject(n_pos);
            c_plate = this.index.getPlate(this.pos);
            n_plate = this.index.getPlate(n_pos);
        }

        this.save(new Memento(Position.multiplication(diff, loop_cpt), last_move, null));
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
            updatable.moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.speed);
        } else if (type == MoveType.VALIDATE) {
            updatable.moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.speed)
                    .startAnimation(this.animation.getAnimationByName("maki_orange_one"), PlayMode.ONCE);
        } else if (type == MoveType.UNVALIDATE) {
            updatable.moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.speed)
                    .startAnimation(this.animation.getAnimationByName("maki_orange_out"), PlayMode.ONCE);
        } else if (type == MoveType.WAIT) {
            updatable.waitDuring(wait_time);
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
