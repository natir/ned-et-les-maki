/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

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

import im.bci.jnuit.NuitDisplay;
import im.bci.jnuit.display.VideoResolution;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author devnewton
 */
public class LwjglNuitDisplay implements NuitDisplay {

    @Override
    public List<VideoResolution> listResolutions() {
        try {
            TreeSet<VideoResolution> resolutions = new TreeSet<>();
            for (DisplayMode m : Display.getAvailableDisplayModes()) {
                resolutions.add(new VideoResolution(m.getWidth(), m.getHeight()));
            }
            return new ArrayList<>(resolutions);
        } catch (LWJGLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void changeResolution(VideoResolution chosenResolution, boolean fullscreen) {
        DisplayMode chosenMode = LwjglHelper.findBestDisplayMode(chosenResolution.getWidth(), chosenResolution.getHeight());
        try {
            if (fullscreen) {
                Display.setDisplayModeAndFullscreen(chosenMode);
            } else {
                Display.setFullscreen(false);
                Display.setDisplayMode(chosenMode);
            }
            LwjglHelper.setResizable(true);
            if (!Display.isCreated()) {
                Display.create();
            }
            Display.setVSyncEnabled(true);
        } catch (LWJGLException e) {
            throw new RuntimeException("Unable to create display");
        }
    }

    @Override
    public VideoResolution getResolution() {
        return new VideoResolution(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
    }

    @Override
    public boolean isFullscreen() {
        return Display.isFullscreen();
    }
}
