/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author pierre
 */
public class Stairs extends GameObject {

    private Position dir;
    private boolean open;
    private final IAnimationCollection animation;

    public Stairs(PositionIndexed pos, Entity entity, LevelIndex index, IAssets assets, boolean open, Position dir) {
        super(pos, entity, index);
        this.open = open;
        this.animation = assets.getAnimations("animation/stairs/stairs.json");
        this.dir = dir;
    }

    @Override
    public Position moveTo(Position diff, float wait_time) {
        return this.pos;
    }

    @Override
    public void undo() {
    }

    public Position getDir() {
        return dir;
    }

    public void setDir(Position dir) {
        this.dir = dir;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;

        if (open) {
            this.run_animation(MoveType.OPEN);
        } else {
            this.run_animation(MoveType.CLOSE);
        }
    }

    private void run_animation(MoveType type) {
        Sprite sprite = this.entity.getComponent(Sprite.class);
        SpritePuppetControls updatable = this.entity.getComponent(SpritePuppetControls.class);

        if (updatable
                == null) {
            updatable = new SpritePuppetControls(sprite);
        }

        if (type == MoveType.OPEN) {
            if (dir.equals(Position.getUp())) {
                updatable.startAnimation(this.animation.getAnimationByName("dark_stairs_up_open"), PlayMode.ONCE);
            } else if (dir.equals(Position.getDown())) {
                updatable.startAnimation(this.animation.getAnimationByName("dark_stairs_down_open"), PlayMode.ONCE);
            } else if (dir.equals(Position.getLeft())) {
                updatable.startAnimation(this.animation.getAnimationByName("dark_stairs_left_open"), PlayMode.ONCE);
            } else if (dir.equals(Position.getRight())) {
                updatable.startAnimation(this.animation.getAnimationByName("dark_stairs_right_open"), PlayMode.ONCE);
            }
        } else if (type == MoveType.CLOSE) {
            if (dir.equals(Position.getUp())) {
                updatable.startAnimation(this.animation.getAnimationByName("dark_stairs_up_close"), PlayMode.ONCE);
            } else if (dir.equals(Position.getDown())) {
                updatable.startAnimation(this.animation.getAnimationByName("dark_stairs_down_close"), PlayMode.ONCE);
            } else if (dir.equals(Position.getLeft())) {
                updatable.startAnimation(this.animation.getAnimationByName("dark_stairs_left_close"), PlayMode.ONCE);
            } else if (dir.equals(Position.getRight())) {
            }
        }

        this.entity.addComponent(updatable);

        this.entity.changedInWorld();
    }
}
