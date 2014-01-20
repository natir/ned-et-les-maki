/*
The MIT License (MIT)

Copyright (c) 2013 devnewton <devnewton@bci.im>

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

import java.util.ArrayList;

import com.artemis.Component;

import im.bci.jnuit.animation.IAnimation;
import im.bci.jnuit.animation.PlayMode;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 */
public final class SpritePuppetControls extends Component {

    private final ArrayList<SpriteControl> updaters;
    private final Sprite sprite;

    public SpritePuppetControls(Sprite sprite) {
        this.sprite = sprite;
        this.updaters = new ArrayList<>();
    }

    public SpritePuppetControls moveTo(Vector3f pos, float duration) {
        updaters.add(new SpriteMoveTo(sprite, pos, duration));
        return this;
    }

    public SpritePuppetControls startAnimation(IAnimation animation, PlayMode mode) {
        updaters.add(new SpriteStartAnimation(sprite, animation, mode));
        return this;
    }
    
    public SpritePuppetControls startAnimation(IAnimation animation) {
        updaters.add(new SpriteStartAnimation(sprite, animation, PlayMode.LOOP));
        return this;
    }

    public SpritePuppetControls stopAnimation() {
        updaters.add(new SpriteStopAnimation(sprite));
        return this;
    }
    
    public SpritePuppetControls waitAnimation() {
        updaters.add(new SpriteWaitAnimation(sprite));
        return this;
    }

    public SpritePuppetControls waitDuring(float duration) {
        updaters.add(new SpriteWait(duration));
        return this;
    }

    public ArrayList<SpriteControl> getUpdaters() {
        return updaters;
    }

}
