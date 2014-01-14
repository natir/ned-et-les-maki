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
package im.bci.jnuit.widgets;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.lwjgl.LwjglHelper;
import im.bci.jnuit.visitors.WidgetVisitor;
import java.util.TreeSet;

public class VideoConfigurator extends Table {

    private final Select<VideoResolution> mode;
    private final Toggle fullscreen;

    private static class VideoResolution implements Comparable<VideoResolution> {

        private final int width, height;

        public VideoResolution(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 71 * hash + this.width;
            hash = 71 * hash + this.height;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final VideoResolution other = (VideoResolution) obj;
            if (this.width != other.width) {
                return false;
            }
            return this.height == other.height;
        }

        @Override
        public int compareTo(VideoResolution o) {
            return Integer.compare(width * height, o.width * o.height);
        }

        @Override
        public String toString() {
            return width + " x " + height;
        }
    }

    public VideoConfigurator(NuitToolkit toolkit) throws LWJGLException {
        super(toolkit);
        defaults().expand();
        cell(new Label(toolkit, "nuit.video.configurator.mode"));
        mode = new Select<>(toolkit, getDisplayModes());
        cell(mode);
        row();
        cell(new Label(toolkit, "nuit.video.configurator.fullscreen"));
        fullscreen = new Toggle(toolkit);
        cell(fullscreen);
        row();
        cell(new Button(toolkit, "nuit.video.configurator.apply") {
            @Override
            public void onOK() {
                changeVideoSettings();
                closeVideoSettings();
            }
        }).colspan(2);
        row();
        cell(new Button(toolkit, "nuit.video.configurator.back") {
            @Override
            public void onOK() {
                closeVideoSettings();
            }
        }).colspan(2);
    }

    protected void changeVideoSettings() {
        VideoResolution chosenResolution = mode.getSelected();
        DisplayMode chosenMode = LwjglHelper.findBestDisplayMode(chosenResolution.getWidth(), chosenResolution.getHeight());
        try {
            if (fullscreen.isEnabled()) {
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

    protected void closeVideoSettings() {
    }

    @Override
    public void onShow() {
        mode.setSelected(new VideoResolution(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight()));
        fullscreen.setEnabled(Display.isFullscreen());
    }

    private List<VideoResolution> getDisplayModes() throws LWJGLException {
        TreeSet<VideoResolution> resolutions = new TreeSet<>();
        for (DisplayMode m : Display.getAvailableDisplayModes()) {
            resolutions.add(new VideoResolution(m.getWidth(), m.getHeight()));
        }
        return new ArrayList<>(resolutions);
    }
    
        @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }
}
