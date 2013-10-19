package org.geekygoblin.nedetlesmaki.game.components.visual;

import im.bci.timed.OneShotTimedAction;

import org.lwjgl.util.vector.Vector3f;

public class SpriteMoveTo extends SpriteUpdater {
	private final Vector3f from, to;
	private final OneShotTimedAction action;
	private final Sprite sprite;
	
	public SpriteMoveTo(Sprite sprite, Vector3f from, Vector3f to, float duration) {
		this.sprite = sprite;
		this.from = from;
		this.to = to;
		this.action = new OneShotTimedAction(duration);
	}

	@Override
	public void update(float elapsedTime) {
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
