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

import java.util.ArrayList;

import com.artemis.Component;

import im.bci.nanim.IAnimation;
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

    public SpritePuppetControls startAnimation(IAnimation animation) {
        updaters.add(new SpriteStartAnimation(sprite, animation));
        return this;
    }

    public SpritePuppetControls stopAnimation() {
        updaters.add(new SpriteStopAnimation(sprite));
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
