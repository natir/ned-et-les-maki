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
package im.bci.jnuit.lwjgl.animation;

import im.bci.jnuit.animation.IAnimationFrame;

/**
 *
 * @author devnewton
 */
public class NanimationFrame implements IAnimationFrame {

    private NanimationImage image;
    private long duration;//milliseconds
    long endTime;//milliseconds
    float u1 = 0;
    float v1 = 0;
    float u2 = 1;
    float v2 = 1;

    public NanimationFrame(NanimationImage image, long duration) {
        this.image = image;
        this.duration = duration;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public NanimationImage getImage() {
        return image;
    }

    @Override
    public float getU1() {
        return u1;
    }

    @Override
    public float getV1() {
        return v1;
    }

    @Override
    public float getU2() {
        return u2;
    }

    @Override
    public float getV2() {
        return v2;
    }
}
