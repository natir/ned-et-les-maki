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

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;

public class LwjglHelper {

    private static class Methods {

        final Method setResizableMethod, wasResizedMethod, getWidthMethod, getHeightMethod;

        Methods() throws NoSuchMethodException, SecurityException {
            setResizableMethod = Display.class.getMethod("setResizable", boolean.class);
            wasResizedMethod = Display.class.getMethod("wasResized");
            getWidthMethod = Display.class.getMethod("getWidth");
            getHeightMethod = Display.class.getMethod("getHeight");
        }

    }
    private static Methods methods;

    public static void setResizable(boolean b) {
        try {
            if (null == methods) {
                methods = new Methods();
            }
            if (null != methods.setResizableMethod) {
                methods.setResizableMethod.invoke(null, b);
            }
        } catch (Exception e) {
        }
    }

    public static boolean wasResized() {
        try {
            if (null == methods) {
                methods = new Methods();
            }
            if (null != methods.wasResizedMethod) {
                return Boolean.TRUE.equals(methods.wasResizedMethod.invoke(null));
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static int getWidth() {
        try {
            if (null == methods) {
                methods = new Methods();
            }
            if (null != methods.getWidthMethod) {
                return (int) methods.getWidthMethod.invoke(null);
            }
        } catch (Exception e) {
        }
        return Display.getDisplayMode().getWidth();
    }

    public static int getHeight() {
        try {
            if (null == methods) {
                methods = new Methods();
            }
            if (null != methods.getHeightMethod) {
                return (int) methods.getHeightMethod.invoke(null);
            }
        } catch (Exception e) {
        }
        return Display.getDisplayMode().getHeight();
    }

    public static void setupGLTextureParams() {
        if (GLContext.getCapabilities().OpenGL12) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        } else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        }
        setupGLTextureQualityParams();
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
                GL11.GL_MODULATE);
    }

    public static void setupGLTextureQualityParams() {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    }

    public static DisplayMode findBestDisplayMode(int width, int height) {
        DisplayMode desktopMode = Display.getDesktopDisplayMode();
        if (width == desktopMode.getWidth() && height == desktopMode.getHeight()) {
            return desktopMode;
        }
        DisplayMode bestMode = null;
        try {
            for (DisplayMode m : Display.getAvailableDisplayModes()) {
                if (width == m.getWidth() && height == m.getHeight()) {
                    if (null == bestMode
                            || (m.isFullscreenCapable() && !bestMode.isFullscreenCapable())
                            || (m.getBitsPerPixel() > bestMode.getBitsPerPixel())
                            || m.getFrequency() > bestMode.getFrequency()) {
                        bestMode = m;
                    }
                }
            }
            if (null != bestMode) {
                return bestMode;
            }
        } catch (LWJGLException ex) {
            Logger.getLogger(LwjglHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new DisplayMode(width, height);
    }
}
