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
package org.geekygoblin.nedetlesmaki.game;

import im.bci.lwjgl.nuit.controls.Action;
import im.bci.lwjgl.nuit.utils.LwjglHelper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.assets.VirtualFileSystem;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Main {

    static void setupLibraryPath() {
        if (System.getProperty("javawebstart.version") != null) {
            return;
        }

        String libraryPath = System.getProperty("java.library.path");
        if (libraryPath != null && libraryPath.contains("natives")) {
            return;
        }

        try {
            File nativeDir = new File(getApplicationDir(), "natives");
            if (nativeDir.exists() && nativeDir.isDirectory() && nativeDir.list().length > 0) {
                String nativePath = nativeDir.getCanonicalPath();
                System.setProperty("org.lwjgl.librarypath", nativePath);
                System.setProperty("net.java.games.input.librarypath", nativePath);
            }
        } catch (IOException e) {
            Logger.getLogger(Main.class.getName()).log(Level.WARNING, "error", e);
        }
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "Cannot find 'natives' library folder, try system libraries");
    }

    public static File getApplicationDir() throws IOException {
        try {
            return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException uriEx) {
            Logger.getLogger(Main.class.getName()).log(Level.WARNING, "Cannot find application directory, try current", uriEx);
            return new File(".");
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, Exception {

        try {
            setupLibraryPath();
        } catch (Throwable e) {
            handleError(e, "Unexpected error during startup. Check your java version and your opengl driver.\n");
            return;
        }

        try {
            setVideoMode();
            initGamepads();

            File applicationDir = getApplicationDir();
            VirtualFileSystem vfs = new VirtualFileSystem(new File(applicationDir, "data"), new File(applicationDir.getParentFile(), "data"));
            try (Assets assets = new Assets(vfs)) {
                assets.setIcon();
                Game game = new Game(assets);
                setControls(game);
                while (!game.isCloseRequested()) {
                    game.setDelta(1.0f / 60.0f);
                    game.process();
                    Display.update(false);
                    Display.sync(60);
                    Display.processMessages();
                    Mouse.poll();
                    Keyboard.poll();
                    Controllers.poll();
                }
                saveControlsPreferences(game);
                saveVideoModePreferences();
            }
        } catch (Throwable e) {
            handleError(e, "Unexpected error during execution.\n");
        }
    }

    public static void handleError(Throwable e, final String defaultMessage) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, defaultMessage, e);
        JOptionPane.showMessageDialog(null, defaultMessage + "\n" + e.getMessage() + (e.getCause() != null ? "\nCause: " + e.getCause().getMessage() : ""), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void saveVideoModePreferences() {
        Preferences pref = Game.getPreferences();
        pref.putInt("video.width", Display.getDisplayMode().getWidth());
        pref.putInt("video.height", Display.getDisplayMode().getHeight());
        pref.putBoolean("video.fullscreen", Display.isFullscreen());
        pref.saveConfig();

    }

    private static void setVideoMode() throws LWJGLException {
        Preferences pref = Game.getPreferences();
        DisplayMode mode = new DisplayMode(pref.getInt("video.width", 800), pref.getInt("video.height", 600));
        if (pref.getBoolean("video.fullscreen", false)) {
            Display.setDisplayModeAndFullscreen(mode);
        } else {
            Display.setFullscreen(false);
            Display.setDisplayMode(mode);
        }
        LwjglHelper.setResizable(true);
        Display.setTitle("Ned et les maki");
        if (!Display.isCreated()) {
            Display.create();
        }
    }

    private static void initGamepads() {
        try {
            Controllers.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Cannot init gamepad support", ex);
        }
    }

    private static void setControls(Game game) {
        loadControlsForAction(game.getToolkit().getMenuOK());
        loadControlsForAction(game.getToolkit().getMenuCancel());
        loadControlsForAction(game.getToolkit().getMenuUp());
        loadControlsForAction(game.getToolkit().getMenuDown());
        loadControlsForAction(game.getToolkit().getMenuLeft());
        loadControlsForAction(game.getToolkit().getMenuRight());
    }
    
    private static void loadControlsForAction(Action action) {
        String name = action.getName();
        action.setMainControl(Game.getPreferences().getControl(name + ".main", action.getMainControl()));
        action.setAlternativeControl(Game.getPreferences().getControl(name + ".alternative", action.getAlternativeControl()));
    }

    private static void saveControlsPreferences(Game game) {
        saveControlsForAction(game.getToolkit().getMenuOK());
        saveControlsForAction(game.getToolkit().getMenuCancel());
        saveControlsForAction(game.getToolkit().getMenuUp());
        saveControlsForAction(game.getToolkit().getMenuDown());
        saveControlsForAction(game.getToolkit().getMenuLeft());
        saveControlsForAction(game.getToolkit().getMenuRight());
    }

    private static void saveControlsForAction(Action action) {
        String name = action.getName();
        Game.getPreferences().putControl(name + ".main", action.getMainControl());
        Game.getPreferences().putControl(name + ".alternative", action.getAlternativeControl());
    }
}
