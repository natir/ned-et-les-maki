/*
 * Copyright (c) 2013 devnewton <devnewton@bci.im>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'devnewton <devnewton@bci.im>' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.geekygoblin.nedetlesmaki.game.components.visual;

import im.bci.timed.OneShotTimedAction;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author devnewton
 *
 */
class SpriteMoveTo extends SpriteControl {
	private final Vector3f to;
	private final float duration;
	private final Sprite sprite;
	private Vector3f from;
	private OneShotTimedAction action;
	
	SpriteMoveTo(Sprite sprite, Vector3f to, float duration) {
		this.sprite = sprite;
		this.to = to;
		this.duration = duration;
	}

	@Override
	public void update(float elapsedTime) {
		if(null == action) {
			this.from = new Vector3f(sprite.getPosition());
			action = new OneShotTimedAction(duration);
		}		
		action.update(elapsedTime);
        Vector3f newPos;
        final float progress = action.getProgress();
        if (progress >= 1.0f) {
            newPos = to;
        } else {
            newPos = new Vector3f();
            Vector3f.sub(to, from, newPos);
            newPos.scale(progress);
            newPos.x += from.x;
            newPos.y += from.y;
            newPos.z += from.z;
        }
        sprite.getPosition().x = newPos.x;
        sprite.getPosition().y = newPos.y;
        sprite.getPosition().y = newPos.y;
	}

	@Override
	public boolean isFinished() {
		return action.getProgress() >= 1.0f;
	}
	

}
