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
package im.bci.jnuit.lwjgl;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.Display;

public class IconLoader {

    public static void setIcon(InputStream is) {
        try {
            int nbIcons = Display.setIcon(IconLoader.loadIcon(is));
            Logger.getLogger(IconLoader.class.getName()).log(Level.INFO, "nbIcons = {0}", nbIcons);
        } catch (Exception ex) {
            Logger.getLogger(IconLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ByteBuffer[] loadIcon(InputStream is) throws IOException {
        BufferedImage image = ImageIO.read(is);
        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = loadIconInstance(image, 128);
        buffers[1] = loadIconInstance(image, 32);
        buffers[2] = loadIconInstance(image, 16);
        return buffers;
    }

    private static BufferedImage scale(BufferedImage source, int width, int height) {
        BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(source, 0, 0, width, height, null);
        g.dispose();

        return buf;
    }

    private static ByteBuffer loadIconInstance(BufferedImage image, int dimension) {
        return readImageAsByteBuffer(scale(image, dimension, dimension));
    }

    private static byte[] getRGBAPixels(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        byte[] pixels = new byte[w * h * 4];
        int pixelIndex = 0;
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                int rgba = image.getRGB(x, y);
                byte a = (byte) ((rgba >> 24) & 0xff);
                byte r = (byte) ((rgba >> 16) & 0xff);
                byte g = (byte) ((rgba >> 8) & 0xff);
                byte b = (byte) (rgba & 0xff);
                pixels[pixelIndex++] = r;
                pixels[pixelIndex++] = g;
                pixels[pixelIndex++] = b;
                pixels[pixelIndex++] = a;
            }
        }
        return pixels;
    }

    private static ByteBuffer readImageAsByteBuffer(BufferedImage image) {
        return ByteBuffer.wrap(getRGBAPixels(image));
    }
}