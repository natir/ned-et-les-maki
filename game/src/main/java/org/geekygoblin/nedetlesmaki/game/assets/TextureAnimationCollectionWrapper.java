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
package org.geekygoblin.nedetlesmaki.game.assets;

import im.bci.nanim.IAnimation;
import im.bci.nanim.IAnimationCollection;
import im.bci.nanim.IAnimationFrame;
import im.bci.nanim.IAnimationImage;
import im.bci.nanim.IPlay;
import im.bci.nanim.PlayMode;

/**
 *
 * @author devnewton
 */
public class TextureAnimationCollectionWrapper implements IAnimationCollection, IAnimation, IAnimationFrame, IAnimationImage {

    private final Texture texture;
    private final Play play;
    private final float u1, v1, u2, v2;

    @Override
    public long getDuration() {
        return Long.MAX_VALUE;
    }

    @Override
    public IAnimationImage getImage() {
        return this;
    }

    @Override
    public float getU1() {
        return u1;
    }

    @Override
    public float getU2() {
        return u2;
    }

    @Override
    public float getV1() {
        return v1;
    }

    @Override
    public float getV2() {
        return v2;
    }

    @Override
    public int getId() {
        return texture.getId();
    }

    @Override
    public boolean hasAlpha() {
        return texture.hasAlpha();
    }

    public class Play implements IPlay {

        @Override
        public String getName() {
            return TextureAnimationCollectionWrapper.this.getName();
        }

        @Override
        public void start(PlayMode mode) {
        }

        @Override
        public void stop() {
        }

        @Override
        public boolean isStopped() {
            return true;
        }

        @Override
        public void update(long elapsedTime) {
        }

        @Override
        public IAnimationFrame getCurrentFrame() {
            return TextureAnimationCollectionWrapper.this;
        }
    }

    public TextureAnimationCollectionWrapper(Texture texture, float u1, float v1, float u2, float v2) {
        this.texture = texture;
        this.u1 = u1;
        this.v1 = v1;
        this.u2 = u2;
        this.v2 = v2;
        play = new Play();
    }

    @Override
    public IAnimation getAnimationByName(String name) {
        return this;
    }

    @Override
    public IAnimation getFirst() {
        return this;
    }

    @Override
    public String getName() {
        return String.valueOf(texture.getId());
    }

    @Override
    public IPlay start(PlayMode mode) {
        return play;
    }

    @Override
    public void stop(IPlay play) {
    }
}
