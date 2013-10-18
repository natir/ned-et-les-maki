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
package im.bci.lwjgl.nuit.widgets;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import im.bci.lwjgl.nuit.NuitToolkit;
import im.bci.lwjgl.nuit.utils.LwjglHelper;
import java.util.TreeSet;

public class VideoConfigurator extends Table {

    private Select<VideoResolution> mode;
    private Toggle fullscreen;

    private static class VideoResolution implements Comparable<VideoResolution> {

        private int width, height;

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
            if (this.height != other.height) {
                return false;
            }
            return true;
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
        cell(new Label(toolkit, "Mode"));
        mode = new Select<>(toolkit, getDisplayModes());
        cell(mode);
        row();
        cell(new Label(toolkit, "Fullscreen"));
        fullscreen = new Toggle(toolkit);
        cell(fullscreen);
        row();
        cell(new Button(toolkit, "Apply") {
            @Override
            public void onOK() {
                changeVideoSettings();
                closeVideoSettings();
            }
        }).colspan(2);
        row();
        cell(new Button(toolkit, "Back") {
            @Override
            public void onOK() {
                closeVideoSettings();
            }
        }).colspan(2);
    }

    protected void changeVideoSettings() {
        VideoResolution chosenResolution = mode.getSelected();
        DisplayMode chosenMode = new DisplayMode(chosenResolution.getWidth(), chosenResolution.getHeight());
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
}
