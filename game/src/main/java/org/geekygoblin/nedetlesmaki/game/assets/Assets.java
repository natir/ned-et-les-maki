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

import de.matthiasmann.twl.utils.PNGDecoder;
import im.bci.lwjgl.nuit.utils.LwjglHelper;
import im.bci.nanim.AnimationCollection;
import im.bci.nanim.NanimParser.Nanim;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public class Assets implements AutoCloseable {

    private VirtualFileSystem vfs;
    private HashMap<String/* name */, TextureWeakReference> textures = new HashMap<>();
    private ReferenceQueue<Texture> texturesReferenceQueue = new ReferenceQueue<>();
    private HashMap<String/* name */, AnimationCollectionWeakReference> animations = new HashMap<>();
    private ReferenceQueue<AnimationCollection> animationsReferenceQueue = new ReferenceQueue<>();
    

    Assets(VirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public void setVfs(VirtualFileSystem vfs) {
        clearAll();
        this.vfs = vfs;
    }

    public Texture getTexture(String name) {
        TextureWeakReference textureRef = textures.get(name);
        if (textureRef != null) {
            Texture texture = textureRef.get();
            if (texture != null) {
                return texture;
            } else {
                textures.remove(name);
            }
        }
        try {
            Texture texture = loadPngTexture(name);
            putTexture(name, texture);
            return texture;
        } catch (Exception e) {
            throw new RuntimeException("Cannot load texture " + name, e);
        }
    }

    AnimationCollection getAnimations(String name) {
        AnimationCollectionWeakReference animRef = animations.get(name);
        if (null != animRef) {
            AnimationCollection anim = animRef.get();
            if (null != anim) {
                return anim;
            } else {
                animations.remove(name);
            }
        }
        try (InputStream is = vfs.open(name)) {
            AnimationCollection anim = new AnimationCollection(Nanim.parseFrom(is));
            putAnim(name, anim);
            return anim;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load animation " + name, e);
        }
    }

    public Texture grabScreenToTexture() {
        int maxSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        Texture texture = new Texture(Math.min(maxSize, LwjglHelper.getWidth()), Math.min(maxSize, LwjglHelper.getHeight()), false);
        putTexture("!screenCapture_" + new Date().getTime(), texture);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
        LwjglHelper.setupGLTextureParams();
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, texture.getWidth(), texture.getHeight(), 0);
        return texture;
    }

    @Override
    public void close() throws Exception {
        clearAll();
    }

    private Texture loadPngTexture(String name) throws FileNotFoundException, IOException {
        try (InputStream is = vfs.open(name)) {
            PNGDecoder decoder = new PNGDecoder(is);
            int bpp;
            PNGDecoder.Format format;
            int pixelFormat;
            int texWidth = decoder.getWidth();
            int texHeight = decoder.getHeight();
            boolean hasAlpha = decoder.hasAlpha();
            if (hasAlpha) {
                bpp = 4;
                format = PNGDecoder.Format.RGBA;
                pixelFormat = GL11.GL_RGBA;
            } else {
                bpp = 3;
                format = PNGDecoder.Format.RGB;
                pixelFormat = GL11.GL_RGB;
            }

            int stride = bpp * texWidth;
            ByteBuffer buffer = ByteBuffer.allocateDirect(stride * texHeight);
            decoder.decode(buffer, stride, format);
            buffer.flip();
            Texture texture = new Texture(texWidth, texHeight, hasAlpha);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            LwjglHelper.setupGLTextureParams();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, pixelFormat, texWidth,
                    texHeight, 0, pixelFormat, GL11.GL_UNSIGNED_BYTE, buffer);
            return texture;
        }



    }

    public void clearUseless() {
        TextureWeakReference tex;
        while ((tex = (TextureWeakReference) texturesReferenceQueue.poll()) != null) {
            tex.delete();
        }
        
        AnimationCollectionWeakReference ani;
        while ((ani = (AnimationCollectionWeakReference) animationsReferenceQueue.poll()) != null) {
            ani.delete();
        }
    }

    private void clearAll() {
        for (TextureWeakReference ref : textures.values()) {
            ref.delete();
        }
        textures.clear();
        
        for (AnimationCollectionWeakReference ref : animations.values()) {
            ref.delete();
        }
        animations.clear();
    }

    private void putAnim(String name, AnimationCollection anim) {
        animations.put(name, new AnimationCollectionWeakReference(name, anim, animationsReferenceQueue));
    }

    private void putTexture(String name, Texture texture) {
        textures.put(name, new TextureWeakReference(name, texture, texturesReferenceQueue));
    }
}
