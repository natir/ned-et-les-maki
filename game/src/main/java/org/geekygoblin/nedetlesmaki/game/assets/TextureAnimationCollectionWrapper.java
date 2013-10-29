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
