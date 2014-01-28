/*
 The MIT License (MIT)

 Copyright (c) 2013 Pierre Marijon <pierre@marijon.fr>

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
package org.geekygoblin.nedetlesmaki.game.components.visual;

import im.bci.jnuit.timed.OneShotTimedAction;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 *
 */
class SpriteMoveToRelative extends SpriteControl {

    private final Vector3f diff;
    private final float duration;
    private final Sprite sprite;
    private Vector3f from;
    private OneShotTimedAction action;

    SpriteMoveToRelative(Sprite sprite, Vector3f diff, float duration) {
        this.sprite = sprite;
        this.diff = diff;
        this.duration = duration;
    }

    @Override
    public void update(float elapsedTime) {
        final OneShotTimedAction a = getAction();
        a.update(elapsedTime);
        Vector3f newPos;
        final float progress = a.getProgress();
       if (progress >= 1.0f) {
            newPos = new Vector3f();
            Vector3f.add(diff, from, newPos);
        } else {
            newPos = new Vector3f();
            Vector3f.add(diff, from, newPos);
            newPos.scale(progress);
            newPos.x += from.x;
            newPos.y += from.y;
            newPos.z += from.z;
        }
        sprite.getPosition().x = newPos.x;
        sprite.getPosition().y = newPos.y;
        sprite.getPosition().z = newPos.z;
    }

    private OneShotTimedAction getAction() {
        if (null == action) {
            this.from = new Vector3f(sprite.getPosition());
            action = new OneShotTimedAction(duration);
        }
        return action;
    }

    @Override
    public boolean isFinished() {
        return getAction().getProgress() >= 1.0f;
    }

}
