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
package org.geekygoblin.nedetlesmaki.core.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;

import com.artemis.systems.EntityProcessingSystem;
import im.bci.jnuit.animation.IAnimation;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import im.bci.jnuit.artemis.sprite.Sprite;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import pythagoras.f.Vector3;

/**
 *
 * @author devnewton
 */
public class MouseArrowSystem extends EntityProcessingSystem {

    private ComponentMapper<Sprite> spriteMapper;
    private Vector3 mousePos;
    private float nearestSpriteDistance;
    private Sprite nearestSprite;
    private Entity arrow;
    private final NedGame game;
    private final IAssets assets;
    private final IDrawSystem drawSystem;

    public MouseArrowSystem(NedGame game, IAssets assets, IDrawSystem drawSystem) {
        super(Aspect.getAspectForAll(Sprite.class));
        this.game = game;
        this.drawSystem = drawSystem;
        this.assets = assets;
    }

    @Override
    protected void initialize() {
        spriteMapper = world.getMapper(Sprite.class);
        arrowAnimations = assets.getAnimations("animation/arrow/arrow.json");
    }

    @Override
    protected void begin() {
        if (null != nearestSprite) {
            nearestSprite.setRed(1.0f);
            nearestSprite.setGreen(1.0f);
            nearestSprite.setBlue(1.0f);
            nearestSprite.setAlpha(1.0f);
            nearestSprite = null;
        }
        nearestSpriteDistance = Float.MAX_VALUE;
        mousePos = drawSystem.getMouseSpritePos(10);
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
            arrowSprite.setPosition(new Vector3(mousePos));
        }
    }

    @Override
    protected void process(Entity entity) {
        if (null != mousePos && entity != arrow) {
            Sprite sprite = spriteMapper.get(entity);
            Vector3 v = mousePos.subtract(sprite.getPosition());
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
            nearestSprite.setRed(1.0f);
            nearestSprite.setGreen(0.5f);
            nearestSprite.setBlue(0f);
            nearestSprite.setAlpha(1.0f);
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
            Vector3 selectedPosition = nearestSprite.getPosition();
            Vector3 nedPosition = spriteMapper.get(game.getNed()).getPosition();
            int nedX = Math.round(nedPosition.x);
            int nedY = Math.round(nedPosition.y);
            int selectedX = Math.round(selectedPosition.x);
            int selectedY = Math.round(selectedPosition.y);

            if (nedX == selectedX) {
                if (nedY < selectedY) {
                    animation = arrowAnimations.getAnimationByName("arrow_right");
                } else if (nedY > selectedY) {
                    animation = assets.getAnimations("animation/arrow/arrow.json").getAnimationByName("arrow_left");
                }
            } else if (nedY == selectedY) {
                if (nedX < selectedX) {
                    animation = assets.getAnimations("animation/arrow/arrow.json").getAnimationByName("arrow_down");
                } else if (nedX > selectedX) {
                    animation = assets.getAnimations("animation/arrow/arrow.json").getAnimationByName("arrow_up");
                }
            }
        }

        if (null == animation) {
            if (null != sprite.getPlay()) {
                sprite.setPlay(null);
                drawSystem.setDefaultCursor();
            }
        } else {
            if (null == sprite.getPlay() || sprite.getPlay().getName().equals(animation.getName())) {
                sprite.setPlay(animation.start(PlayMode.LOOP));
                drawSystem.hideCursor();
            }
        }

    }
    private IAnimationCollection arrowAnimations;
}
