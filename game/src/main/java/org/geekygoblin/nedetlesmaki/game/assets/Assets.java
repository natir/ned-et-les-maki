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

import de.matthiasmann.twl.utils.PNGDecoder;
import im.bci.lwjgl.nuit.utils.IconLoader;
import im.bci.lwjgl.nuit.utils.LwjglHelper;
import im.bci.lwjgl.nuit.utils.TrueTypeFont;
import im.bci.nanim.NanimationCollection;
import im.bci.nanim.NanimParser.Nanim;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public class Assets implements AutoCloseable {

    private VirtualFileSystem vfs;
    private final HashMap<String/* name */, TextureWeakReference> textures = new HashMap<>();
    private final ReferenceQueue<Texture> texturesReferenceQueue = new ReferenceQueue<>();
    private final HashMap<String/* name */, AnimationCollectionWeakReference> animations = new HashMap<>();
    private final ReferenceQueue<NanimationCollection> animationsReferenceQueue = new ReferenceQueue<>();
    private final HashMap<String/* name */, TrueTypeFontWeakReference> fonts = new HashMap<>();
    private final ReferenceQueue<TrueTypeFont> fontsReferenceQueue = new ReferenceQueue<>();
    private final TmxAssetLoader tmxLoader;

    public Assets(VirtualFileSystem vfs) {
        this.vfs = vfs;
        tmxLoader = new TmxAssetLoader(this);
    }

    public void setVfs(VirtualFileSystem vfs) {
        clearAll();
        this.vfs = vfs;
    }

    public TmxAsset getTmx(String name) {
        try {
            return tmxLoader.load(name);
        } catch (JAXBException | IOException e) {
            throw new RuntimeException("Cannot load map " + name, e);
        }
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

    public NanimationCollection getAnimations(String name) {
        AnimationCollectionWeakReference animRef = animations.get(name);
        if (null != animRef) {
            NanimationCollection anim = animRef.get();
            if (null != anim) {
                return anim;
            } else {
                animations.remove(name);
            }
        }
        try (InputStream is = vfs.open(name)) {
            NanimationCollection anim = new NanimationCollection(Nanim.parseFrom(is));
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

        for (TrueTypeFontWeakReference ref : fonts.values()) {
            ref.delete();
        }
        fonts.clear();
    }

    InputStream open(String name) throws FileNotFoundException {
        return vfs.open(name);
    }

    private void putAnim(String name, NanimationCollection anim) {
        animations.put(name, new AnimationCollectionWeakReference(name, anim, animationsReferenceQueue));
    }

    private void putTexture(String name, Texture texture) {
        textures.put(name, new TextureWeakReference(name, texture, texturesReferenceQueue));
    }

    private void putFont(String name, TrueTypeFont font) {
        fonts.put(name, new TrueTypeFontWeakReference(name, font, fontsReferenceQueue));
    }

    public TrueTypeFont getFont(String name) {
        TrueTypeFontWeakReference fontRef = fonts.get(name);
        if (fontRef != null) {
            TrueTypeFont font = fontRef.get();
            if (font != null) {
                return font;
            } else {
                fonts.remove(name);
            }
        }
        Font f;
        try (InputStream is = vfs.open(name)) {
            f = Font.createFont(Font.TRUETYPE_FONT, is);
            f = f.deriveFont(Font.PLAIN, 48);
        } catch (IOException | FontFormatException e) {
            f = new Font("monospaced", Font.BOLD, 24);
        }
        TrueTypeFont font = new TrueTypeFont(f, true, new char[0], new HashMap<Character, BufferedImage>()) {

            @Override
            public void deleteFontTexture() {
                //NOTHING
            }
            
        };
        putFont(name, font);
        return font;
    }

    public InputStream getIcon(String name) throws FileNotFoundException {
        return vfs.open(name);
    }

    public void setIcon() {
        try (InputStream is = vfs.open("icon.png")) {
            IconLoader.setIcon(is);
        } catch (IOException ex) {
            Logger.getLogger(Assets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
