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
public class BlueMaki extends GameObject {

    private boolean validate;
    private final IAnimationCollection animation;

    public BlueMaki(PositionIndexed pos, Entity entity, LevelIndex index, IAssets assets) {
        super(pos, entity, index);
        this.animation = assets.getAnimations("animation/maki/maki.json");
    }

    @Override
    public Position moveTo(Position diff, float wait_time) {
        boolean stop = false;
        int loop_cpt;
        GameObject last_obj_push = null;
        Position n_pos = Position.sum(this.pos, diff);
        GameObject n_obj = this.index.getGameObject(n_pos);
        Plate c_plate = this.index.getPlate(this.pos);
        Plate n_plate = this.index.getPlate(n_pos);
        MoveType type = MoveType.NO;

        for (loop_cpt = 0; !stop; loop_cpt++) { // while stop is false

            if (n_obj != null) { // If next pos have object
                Position n_obj_pos = new Position(n_obj.getPos());
                if (loop_cpt > 2) {
                    if (n_obj instanceof Box) {
                        n_obj_pos = ((Box) n_obj).destroyMove(diff, this.compute_animation_time(wait_time, loop_cpt));
                        n_pos = Position.sum(n_pos, diff);
                    } else {
                        n_obj.moveTo(diff, this.compute_animation_time(wait_time, loop_cpt));
                    }

                    if (!n_obj_pos.equals(n_obj.getPos())) {
                        n_pos = Position.sum(n_pos, diff);
                    }
                }
                last_obj_push = n_obj;
                stop = true;
            } else {
                n_pos = Position.sum(n_pos, diff);
            }
            if (n_plate != null && n_plate.getColorType() == ColorType.blue && !n_plate.haveMaki()) { // Move to plate
                if (this.validate && c_plate != null) { // Actuali is in plate
                    this.index.setPlateValue(c_plate, false);
                    type = MoveType.NO;
                } else { // Actuali isn't in plate
                    this.validate = true;
                    type = MoveType.VALIDATE;
                }
                this.index.setPlateValue(n_plate, true);
                stop = true;
            } else { // Didn't move to plate
                if (this.validate && !diff.equals(Position.getVoid()) && c_plate != null) { // Actuali is in plate
                    this.validate = false;
                    this.index.setPlateValue(c_plate, false);
                    type = MoveType.UNVALIDATE;
                }
            }

            //Realocation
            n_obj = this.index.getGameObject(n_pos);
            c_plate = this.index.getPlate(this.pos);
            n_plate = this.index.getPlate(n_pos);
        }

        n_pos = Position.deduction(n_pos, diff);
        diff = Position.deduction(n_pos, this.pos);
        this.pos.setPosition(n_pos);
        this.run_animation(diff, type, wait_time);
        this.save(new Memento(diff, type, last_obj_push));

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
        
        /*Memento m = (Memento) this.guard.pullSavedStates();

        if (m == null) {
            return;
        }

        Position n_pos = Position.sum(this.pos, Position.multiplication(m.getDiff(), -1));
        this.pos.setPosition(n_pos);
        this.run_animation(Position.multiplication(m.getDiff(), -1), m.getType(), 0.0f);

        if (m.getNext() != null) {
            m.getNext().undo();
        }*/
    }

    private void run_animation(Position diff, MoveType type, float wait_time) {
        Sprite sprite = this.entity.getComponent(Sprite.class);
        SpritePuppetControls updatable = this.entity.getComponent(SpritePuppetControls.class);

        if (updatable
                == null) {
            updatable = new SpritePuppetControls(sprite);
        }

        if (diff.getX() == 0 && diff.getY() < -2) {
            updatable.waitDuring(wait_time)
                    .moveToRelative(new Vector3(-2, 0, 0), AnimationTime.base * 2)
                    .startAnimation(this.animation.getAnimationByName("boost_start_up"), PlayMode.ONCE)
                    .startAnimation(this.animation.getAnimationByName("boost_loop_up"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(diff.getY() + 2, 0, 0), AnimationTime.speed * diff.getY() * -1)
                    .startAnimation(this.animation.getAnimationByName("boost_stop_up"), PlayMode.ONCE);
        } else if (diff.getX() == 0 && diff.getY() > 2) {
            updatable.waitDuring(wait_time)
                    .moveToRelative(new Vector3(2, 0, 0), AnimationTime.base * 2)
                    .startAnimation(this.animation.getAnimationByName("boost_start_down"), PlayMode.ONCE)
                    .startAnimation(this.animation.getAnimationByName("boost_loop_down"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(diff.getY() - 2, 0, 0), AnimationTime.speed * diff.getY())
                    .startAnimation(this.animation.getAnimationByName("boost_stop_down"), PlayMode.ONCE);
        } else if (diff.getX() > 2 && diff.getY() == 0) {
            updatable.waitDuring(wait_time)
                    .moveToRelative(new Vector3(0, 2, 0), AnimationTime.base * 2)
                    .startAnimation(this.animation.getAnimationByName("boost_start_right"), PlayMode.ONCE)
                    .startAnimation(this.animation.getAnimationByName("boost_loop_right"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(0, diff.getX() - 2, 0), AnimationTime.speed * diff.getX())
                    .startAnimation(this.animation.getAnimationByName("boost_stop_right"), PlayMode.ONCE);
        } else if (diff.getX() < -2 && diff.getY() == 0) {
            updatable.waitDuring(wait_time)
                    .moveToRelative(new Vector3(0, -2, 0), AnimationTime.base * 2)
                    .startAnimation(this.animation.getAnimationByName("boost_start_left"), PlayMode.ONCE)
                    .startAnimation(this.animation.getAnimationByName("boost_loop_left"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(0, diff.getX() + 2, 0), AnimationTime.speed * diff.getX() * -1)
                    .startAnimation(this.animation.getAnimationByName("boost_stop_left"), PlayMode.ONCE);
        } else {
            updatable.waitDuring(wait_time)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base);
        }

        if (type == MoveType.VALIDATE) {
            updatable.startAnimation(this.animation.getAnimationByName("maki_blue_one"), PlayMode.ONCE);
        } else if (type == MoveType.UNVALIDATE) {
            updatable.startAnimation(this.animation.getAnimationByName("maki_blue_out"), PlayMode.ONCE);
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

    private float compute_animation_time(float wait_time, int loop_cpt) {
        return wait_time + AnimationTime.base * 2 + AnimationTime.speed * (loop_cpt - 1);
    }
}
