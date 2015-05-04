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
public class Ned extends GameObject {

    private boolean end;
    private final IAnimationCollection animation;

    public Ned(PositionIndexed pos, Entity entity, LevelIndex index, IAssets assets) {
        super(pos, entity, index);
        this.end = false;
        this.animation = assets.getAnimations("animation/ned/ned.json");
    }

    @Override
    public Position moveTo(Position diff, float wait_time) {
        Position n_pos = Position.sum(this.pos, diff);
        GameObject n_obj = this.index.getGameObject(n_pos);

        if (n_obj == null) {
            this.pos.setPosition(n_pos);
            this.run_animation(diff, MoveType.WALK, 0.0f, false);
            this.save(new Memento(diff, MoveType.WALK, null));
            return this.pos;
        } else {
            Position n_move_to = n_obj.moveTo(diff, 0.0f);
            if (!n_move_to.equals(n_pos)) {
                
                if (n_obj instanceof GreenMaki || n_obj instanceof Box || n_obj instanceof RootBox) { //PushGreen Maki, Box or RootedBox
                
                    this.pos.setPosition(n_pos);
                    this.run_animation(diff, MoveType.PUSH, 0.0f, false);
                    this.save(new Memento(diff, MoveType.PUSH, n_obj));
                    return this.pos;
                
                } else if (n_obj instanceof OrangeMaki) { //Push OrangeMaki
                
                    Position fly_final_pos = Position.deduction(n_move_to, diff);
                    
                    if (Position.deduction(fly_final_pos, this.pos).equals(diff)) {
                        this.run_animation(diff, MoveType.PUSH, 0.0f, false);
                    } else {
                        this.run_animation(Position.deduction(fly_final_pos, this.pos), MoveType.FLY, 0.0f, false);
                    }
                    
                    this.pos.setPosition(fly_final_pos);
                    this.save(new Memento(Position.sum(Position.deduction(fly_final_pos, n_pos), diff), MoveType.FLY, n_obj));
                   
                    return this.pos;
                } else if (n_obj instanceof BlueMaki) { //Push BlueMaki
                    this.run_animation(diff, MoveType.WAIT, 0.0f, false);
                    this.save(new Memento(Position.getVoid(), MoveType.NO, n_obj));
                    return this.pos;
                }
            } else if (n_obj instanceof Stairs && ((Stairs) n_obj).isOpen()) { // Walk on stairs
                Stairs s = (Stairs) n_obj;
                if (diff.equals(Position.multiplication(s.getDir(), -1))) {
                    this.run_animation(diff, MoveType.MOUNT, 0.0f, false);
                    this.end = true;
                }

                return this.pos;
            }
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
        this.pos.setPosition(n_pos);
        this.run_animation(Position.multiplication(m.getDiff(), -1), m.getType(), 0.0f, true);

        if (m.getNext() != null) {
            m.getNext().undo();
        }
    }

    private void run_animation(Position diff, MoveType type, float wait_time, boolean undo) {
        Sprite sprite = this.entity.getComponent(Sprite.class);
        SpritePuppetControls updatable = this.entity.getComponent(SpritePuppetControls.class);

        if (updatable == null) {
            updatable = new SpritePuppetControls(sprite);
        }

        if (type == MoveType.WALK) { // Walk animation
            if (diff.equals(Position.getUp())) {
                updatable.startAnimation(this.animation.getAnimationByName("walk_up"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            } else if (diff.equals(Position.getDown())) {
                updatable.startAnimation(this.animation.getAnimationByName("walk_down"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            } else if (diff.equals(Position.getRight())) {
                updatable.startAnimation(this.animation.getAnimationByName("walk_right"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            } else if (diff.equals(Position.getLeft())) {
                updatable.startAnimation(this.animation.getAnimationByName("walk_left"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            }
        } else if (type == MoveType.PUSH) { // Push animation
            if (diff.equals(Position.getUp()) && undo) {
                updatable.startAnimation(this.animation.getAnimationByName("push_down"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            } else if (diff.equals(Position.getDown()) && undo) {
                updatable.startAnimation(this.animation.getAnimationByName("push_up"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            } else if (diff.equals(Position.getRight()) && undo) {
                updatable.startAnimation(this.animation.getAnimationByName("push_left"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            } else if (diff.equals(Position.getLeft()) && undo) {
                updatable.startAnimation(this.animation.getAnimationByName("push_right"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            } else if (diff.equals(Position.getUp())) {
                updatable.startAnimation(this.animation.getAnimationByName("push_up"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            } else if (diff.equals(Position.getDown())) {
                updatable.startAnimation(this.animation.getAnimationByName("push_down"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            } else if (diff.equals(Position.getRight())) {
                updatable.startAnimation(this.animation.getAnimationByName("push_right"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            } else if (diff.equals(Position.getLeft())) {
                updatable.startAnimation(this.animation.getAnimationByName("push_left"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.base)
                        .stopAnimation();
            }
        } else if (type == MoveType.WAIT) { // Wait animation
            if (diff.equals(Position.getUp())) {
                updatable.startAnimation(this.animation.getAnimationByName("ned_waits_boost_start_up"), PlayMode.ONCE)
                        .startAnimation(this.animation.getAnimationByName("ned_waits_boost_stop_up"), PlayMode.ONCE);
            } else if (diff.equals(Position.getDown())) {
                updatable.startAnimation(this.animation.getAnimationByName("ned_waits_boost_start_down"), PlayMode.ONCE)
                        .startAnimation(this.animation.getAnimationByName("ned_waits_boost_stop_down"), PlayMode.ONCE);
            } else if (diff.equals(Position.getRight())) {
                updatable.startAnimation(this.animation.getAnimationByName("ned_waits_boost_start_right"), PlayMode.ONCE)
                        .startAnimation(this.animation.getAnimationByName("ned_waits_boost_stop_right"), PlayMode.ONCE);
            } else if (diff.equals(Position.getLeft())) {
                updatable.startAnimation(this.animation.getAnimationByName("ned_waits_boost_start_left"), PlayMode.ONCE)
                        .startAnimation(this.animation.getAnimationByName("ned_waits_boost_stop_left"), PlayMode.ONCE);
            }
        } else if (type == MoveType.FLY) { // Fly animation
            if (diff.getX() == 0 && diff.getY() < 0) {
                updatable.startAnimation(this.animation.getAnimationByName("fly_start_up"), PlayMode.ONCE)
                        .startAnimation(this.animation.getAnimationByName("fly_loop_up"), PlayMode.LOOP)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.speed * diff.getY() * -1)
                        .startAnimation(this.animation.getAnimationByName("fly_stop_up"), PlayMode.ONCE);
            } else if (diff.getX() == 0 && diff.getY() > 0) {
                updatable.startAnimation(this.animation.getAnimationByName("fly_start_down"), PlayMode.ONCE)
                        .startAnimation(this.animation.getAnimationByName("fly_loop_down"), PlayMode.LOOP)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.speed * diff.getY())
                        .startAnimation(this.animation.getAnimationByName("fly_stop_down"), PlayMode.ONCE);
            } else if (diff.getX() > 0 && diff.getY() == 0) {
                updatable.startAnimation(this.animation.getAnimationByName("fly_start_right"), PlayMode.ONCE)
                        .startAnimation(this.animation.getAnimationByName("fly_loop_right"), PlayMode.LOOP)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.speed * diff.getX())
                        .startAnimation(this.animation.getAnimationByName("fly_stop_right"), PlayMode.ONCE);
            } else if (diff.getX() < 0 && diff.getY() == 0) {
                updatable.startAnimation(this.animation.getAnimationByName("fly_start_left"), PlayMode.ONCE)
                        .startAnimation(this.animation.getAnimationByName("fly_loop_left"), PlayMode.LOOP)
                        .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), AnimationTime.speed * diff.getX() * -1)
                        .startAnimation(this.animation.getAnimationByName("fly_stop_left"), PlayMode.ONCE);
            }
        } else if (type == MoveType.MOUNT) { // Mount Stairs animiation
            if (diff.equals(Position.getUp())) {
                updatable.startAnimation(this.animation.getAnimationByName("ned_mount_up"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY() - 0.4f, diff.getX() - 0.2f, 1), AnimationTime.base);
            } else if (diff.equals(Position.getDown())) {
                updatable.startAnimation(this.animation.getAnimationByName("ned_mount_down"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY() + 0.2f, diff.getX() - 0.4f, 1), AnimationTime.base);
            } else if (diff.equals(Position.getRight())) {
                updatable.startAnimation(this.animation.getAnimationByName("ned_mount_right"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY() - 0.2f, diff.getX() + 0.2f, 1), AnimationTime.base);
            } else if (diff.equals(Position.getLeft())) {
                updatable.startAnimation(this.animation.getAnimationByName("ned_mount_left"), PlayMode.ONCE)
                        .moveToRelative(new Vector3(diff.getY() - 0.2f, diff.getX() - 0.4f, 1), AnimationTime.base);
            }
        }

        this.entity.addComponent(updatable);

        this.entity.changedInWorld();
    }

    public boolean isEnd() {
        return end;
    }
}
