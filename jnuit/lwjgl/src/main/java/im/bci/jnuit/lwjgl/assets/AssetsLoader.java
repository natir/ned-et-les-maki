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

import de.matthiasmann.twl.utils.PNGDecoder;
import im.bci.jnuit.lwjgl.IconLoader;
import im.bci.jnuit.lwjgl.LwjglHelper;
import im.bci.jnuit.lwjgl.TrueTypeFont;
import im.bci.jnuit.lwjgl.animation.NanimationCollection;
import im.bci.jnuit.lwjgl.animation.NanimParser.Nanim;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.Scanner;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public class AssetsLoader {

    private VirtualFileSystem vfs;
    private static final Logger logger = Logger.getLogger(AssetsLoader.class.getName());

    public AssetsLoader(VirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public void setVfs(VirtualFileSystem vfs) {
        this.vfs = vfs;
    }

    public Texture loadTexture(String name) {
        try {
            logger.log(Level.FINE, "Load texture {0}", name);
            return loadPngTexture(name);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load texture " + name, e);
        }
    }

    public NanimationCollection loadAnimations(String name) {
        try (InputStream vfsInputStream= vfs.open(name)) {
            logger.log(Level.FINE, "Load animation {0}", name);
            InputStream is;
            if(name.endsWith(".gz")) {
                is = new GZIPInputStream(vfsInputStream);
            } else {
                is = vfsInputStream;
            }
            NanimationCollection anim = new NanimationCollection(Nanim.parseFrom(is));
            return anim;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load animation " + name, e);
        }
    }

    public Texture grabScreenToTexture() {
        int maxSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        Texture texture = new Texture(Math.min(maxSize, LwjglHelper.getWidth()), Math.min(maxSize, LwjglHelper.getHeight()), false);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
        LwjglHelper.setupGLTextureParams();
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, texture.getWidth(), texture.getHeight(), 0);
        return texture;
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

    public String loadText(String name) {
        try(InputStream is = vfs.open(name); Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\Z")) {
            return s.next();
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new RuntimeException("Cannot find text file: " + name, ex);
        } catch(IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new RuntimeException("Cannot load text file: " + name, ex);
        }
    }

    public TrueTypeFont loadFont(String name) {
        logger.log(Level.FINE, "Load font {0}", name);
        Font f;
        try (InputStream is = vfs.open(name)) {
            f = Font.createFont(Font.TRUETYPE_FONT, is);
            f = f.deriveFont(Font.PLAIN, 48);
        } catch (IOException | FontFormatException e) {
            f = new Font("monospaced", Font.BOLD, 24);
        }
        TrueTypeFont font = new FontAsset(f, true, new char[0], new HashMap<Character, BufferedImage>());
        font.setCorrection(false);
        return font;
    }

    public void setIcon() {
        try (InputStream is = vfs.open("icon.png")) {
            IconLoader.setIcon(is);
        } catch (IOException ex) {
            Logger.getLogger(AssetsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class FontAsset extends TrueTypeFont {

        public FontAsset(Font font, boolean antiAlias, char[] additionalChars, Map<Character, BufferedImage> specialCharacters) {
            super(font, antiAlias, additionalChars, specialCharacters);
        }

        @Override
        public void deleteFontTexture() {
            //NOTHING
        }
    }
}
