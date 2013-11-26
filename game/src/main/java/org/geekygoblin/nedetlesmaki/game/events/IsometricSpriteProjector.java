/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geekygoblin.nedetlesmaki.game.events;

import org.geekygoblin.nedetlesmaki.game.systems.SpriteProjector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author bcolombi
 */
public class IsometricSpriteProjector implements SpriteProjector {
    private final float tileWidth;
    private final float tileHeight;

    public IsometricSpriteProjector(float tileWidth, float tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    @Override
    public Vector2f project(Vector3f pos) {
        return new Vector2f(-(pos.x - pos.y) * tileWidth / 2.0f, (pos.x + pos.y) * tileHeight / 2.0f - pos.z);
    }

    @Override
    public int compare(Vector3f v1, Vector3f v2) {
        float d1 = v1.x + v1.y + v1.z;
        float d2 = v2.x + v2.y + v2.z;
        if (Math.abs(d1 - d2) > 0.1f) {
            return Float.compare(d1, d2);
        } else {
            return 0;
        }
    }
    
}
