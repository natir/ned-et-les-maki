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
    private final NedGame game;
    private final IAssets assets;
    private final IDrawSystem drawSystem;
    private Sprite target;

    public MouseArrowSystem(NedGame game, IAssets assets, IDrawSystem drawSystem) {
        super(Aspect.getAspectForAll(Sprite.class));
        this.game = game;
        this.drawSystem = drawSystem;
        this.assets = assets;
    }

    @Override
    protected void initialize() {
        spriteMapper = world.getMapper(Sprite.class);
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
    }

    @Override
    protected void process(Entity entity) {
        if (null != mousePos) {
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

        if (null != target) {
            target.setRed(0f);
            target.setGreen(0.5f);
            target.setBlue(1.0f);
            target.setAlpha(1.0f);
        }
    }

    Sprite getSelectedSprite() {
        return nearestSprite;
    }

    public void setTarget(Sprite target) {
        // remove the color from previous target
        if (this.target != null) {
            this.target.setRed(1.0f);
            this.target.setGreen(1.0f);
            this.target.setBlue(1.0f);
            this.target.setAlpha(1.0f);
        }
        this.target = target;
    }
}
