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
package im.bci.jnuit.lwjgl.assets;

import im.bci.jnuit.lwjgl.animation.NanimationCollection;
import im.bci.jnuit.lwjgl.animation.NanimationImage;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public class AnimationCollectionWeakReference extends WeakReference<NanimationCollection> {

    IntBuffer textureIds;
    String name;
    private static final Logger logger = Logger.getLogger(AnimationCollectionWeakReference.class.getName());

    AnimationCollectionWeakReference(String name, NanimationCollection animations, ReferenceQueue<NanimationCollection> queue) {
        super(animations, queue);
        this.name = name;
        List<Integer> ids = new ArrayList<>();
        for (NanimationImage image : animations.getImages().values()) {
            ids.add(image.getId());
        }
        ByteBuffer temp = ByteBuffer.allocateDirect(ids.size() * Integer.SIZE);
        temp.order(ByteOrder.nativeOrder());
        textureIds = temp.asIntBuffer();
        for (Integer id : ids) {
            textureIds.put(id);
        }
    }

    void delete() {
        if (null != textureIds) {
            logger.log(Level.FINE, "Unload animation {0}", name);
            GL11.glDeleteTextures(textureIds);
            textureIds = null;
        }
    }
}
