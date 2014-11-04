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
import pythagoras.f.Vector3;

/**
 *
 * @author pierre
 */
public class Box extends GameObject {

    private final IAnimationCollection animation;

    public Box(PositionIndexed pos, Entity entity, LevelIndex index, IAssets assets) {
        super(pos, entity, index);
        this.animation = assets.getAnimations("animation/box/box.json");
    }

    @Override
    public Position moveTo(Position diff, float wait_time) {
        Position n_pos = Position.sum(this.pos, diff);
        GameObject n_obj = this.index.getGameObject(n_pos);
        Plate n_plate = this.index.getPlate(n_pos);

        if (n_obj == null && n_plate == null) {
            this.pos.setPosition(n_pos);
            this.save(new Memento(diff, MoveType.NO, n_obj));
            this.run_animation(diff, MoveType.NO, wait_time);
        } else {
            this.save(new Memento(Position.getVoid(), MoveType.NO, null));
        }

        return this.pos;
    }

    public Position destroyMove(Position diff, float wait_time) {
        Position current = new Position(this.pos);
        Position next = this.moveTo(diff, wait_time);
        Memento m = (Memento) this.guard.pullSavedStates();

        if (next.equals(current)) {
            this.save(new Memento(m.getDiff(), MoveType.BOOM, null));
            run_animation(Position.getVoid(), MoveType.BOOM, wait_time);
        } else {
            this.save(new Memento(m.getDiff(), MoveType.DESTROY, null));
            run_animation(Position.getVoid(), MoveType.DESTROY, wait_time);
        }

        this.index.deleted(this.pos);
        return this.pos;
    }

    @Override
    public void undo() {
        Memento m = (Memento) this.guard.pullSavedStates();

        if (m == null) {
            return;
        }

        Position n_pos = Position.sum(this.pos, Position.multiplication(m.getDiff(), -1));
        this.pos.setPosition(n_pos);

        if (m.getType() == MoveType.DESTROY || m.getType() == MoveType.BOOM) {
            this.run_animation(Position.multiplication(m.getDiff(), -1), MoveType.UNDESTROY, 0.0f);
            this.pos.reIndex(this);
        } else {
            this.run_animation(Position.multiplication(m.getDiff(), -1), MoveType.NO, 0.0f);
        }

        if (m.getNext() != null) {
            m.getNext().undo();
        }
    }

    private void run_animation(Position diff, MoveType type, float wait_time) {
        Sprite sprite = this.entity.getComponent(Sprite.class);
        SpritePuppetControls updatable = this.entity.getComponent(SpritePuppetControls.class);

        if (updatable
                == null) {
            updatable = new SpritePuppetControls(sprite);
        }

        if (type == MoveType.NO) { // No animation
            if (diff.equals(Position.getUp())) {
                updatable.waitDuring(wait_time)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base);
            } else if (diff.equals(Position.getDown())) {
                updatable.waitDuring(wait_time)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base);
            } else if (diff.equals(Position.getRight())) {
                updatable.waitDuring(wait_time)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base);
            } else if (diff.equals(Position.getLeft())) {
                updatable.waitDuring(wait_time)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base);
            }
        } else if (type == MoveType.BOOM) {
            updatable.waitDuring(wait_time)
                    .startAnimation(this.animation.getAnimationByName("box_boom"), PlayMode.ONCE);
        } else if (type == MoveType.DESTROY) {
            updatable.startAnimation(this.animation.getAnimationByName("destroy"), PlayMode.ONCE);
        } else if (type == MoveType.UNDESTROY) {
            updatable.startAnimation(this.animation.getAnimationByName("create"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base);
        }

        this.entity.addComponent(updatable);

        this.entity.changedInWorld();
    }
}
