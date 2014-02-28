/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package org.geekygoblin.nedetlesmaki.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import im.bci.jnuit.animation.IAnimation;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.lwjgl.assets.IAssets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 */
public class MouseArrowSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Sprite> spriteMapper;
    private Vector3f mousePos;
    private float nearestSpriteDistance;
    private Sprite nearestSprite;
    private Entity arrow;
    private final Game game;
    private final IAssets assets;

    public MouseArrowSystem(Game game, IAssets assets) {
        super(Aspect.getAspectForAll(Sprite.class));
        this.game = game;
        this.assets = assets;
    }

    @Override
    protected void begin() {
        if (null != nearestSprite) {
            nearestSprite.setColor((Color) Color.WHITE);
            nearestSprite = null;
        }
        nearestSpriteDistance = Float.MAX_VALUE;
        mousePos = world.getSystem(DrawSystem.class).getMouseSpritePos(10);
        if (null != mousePos) {
            if (null == arrow) {
                arrow = world.createEntity();
                final Sprite arrowSprite = new Sprite();
                arrowSprite.setWidth(56);
                arrowSprite.setHeight(57);
                arrowSprite.setZOrder(1);
                arrow.addComponent(arrowSprite);
                world.addEntity(arrow);
            }
            final Sprite arrowSprite = spriteMapper.get(arrow);
            arrowSprite.setPosition(new Vector3f(mousePos));
        }
    }

    @Override
    protected void process(Entity entity) {
        if (null != mousePos && entity != arrow) {
            Sprite sprite = spriteMapper.get(entity);
            Vector3f v = new Vector3f();
            Vector3f.sub(mousePos, sprite.getPosition(), v);
            float distance = v.lengthSquared();
            if (distance < nearestSpriteDistance) {
                nearestSprite = sprite;
                nearestSpriteDistance = distance;
            }
        }
    }

    @Override
    protected void end() {
        if (null != nearestSprite) {
            nearestSprite.setColor((Color) Color.ORANGE);
        }
        if (null != arrow) {
            updateArrowPlay(spriteMapper.get(arrow));
        }
    }

    Sprite getSelectedSprite() {
        return nearestSprite;
    }

    private void updateArrowPlay(Sprite sprite) {
        IAnimation animation = null;
        if (null != nearestSprite) {
            Vector3f selectedPosition = nearestSprite.getPosition();
            Vector3f nedPosition = spriteMapper.get(game.getNed()).getPosition();
            int nedX = Math.round(nedPosition.x);
            int nedY = Math.round(nedPosition.y);
            int selectedX = Math.round(selectedPosition.x);
            int selectedY = Math.round(selectedPosition.y);

            if (nedX == selectedX) {
                if (nedY < selectedY) {
                    animation = assets.getAnimations("arrow.nanim.gz").getAnimationByName("arrow_right");
                } else if (nedY > selectedY) {
                    animation = assets.getAnimations("arrow.nanim.gz").getAnimationByName("arrow_left");
                }
            } else if (nedY == selectedY) {
                if (nedX < selectedX) {
                    animation = assets.getAnimations("arrow.nanim.gz").getAnimationByName("arrow_down");
                } else if (nedX > selectedX) {
                    animation = assets.getAnimations("arrow.nanim.gz").getAnimationByName("arrow_up");
                }
            }
        }

        if (null == animation) {
            if (null != sprite.getPlay()) {
                try {
                    Mouse.setNativeCursor(null);
                    sprite.setPlay(null);
                } catch (LWJGLException ex) {
                    Logger.getLogger(MouseArrowSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            if (null == sprite.getPlay() || sprite.getPlay().getName().equals(animation.getName())) {
                sprite.setPlay(animation.start(PlayMode.LOOP));
                try {
                    Cursor emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
                    Mouse.setNativeCursor(emptyCursor);
                } catch (LWJGLException ex) {
                    Logger.getLogger(MouseArrowSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
