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

import org.geekygoblin.nedetlesmaki.core.IMainLoop;
import org.geekygoblin.nedetlesmaki.core.NamedEntities;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import com.artemis.Entity;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import im.bci.jnuit.NuitPreferences;
import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.controls.Action;
import im.bci.jnuit.lwjgl.LwjglHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import im.bci.jnuit.lwjgl.assets.IAssets;
import org.geekygoblin.nedetlesmaki.core.components.IngameControls;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author devnewton
 */
@Singleton
public class MainLoop implements IMainLoop {

    private final NuitPreferences preferences;
    private final Logger logger;
    private boolean closeRequested;
    private NuitToolkit toolkit;
    private NedGame game;
    private IAssets assets;
    private IngameControls ingameControls;

    @Inject
    public MainLoop(Logger logger, NuitPreferences preferences) {
        this.logger = logger;
        this.preferences = preferences;
        setVideoMode();
        initGamepads();
    }

    @Inject
    public void init(IAssets assets, NuitToolkit toolkit, NedGame game, @NamedEntities.MainMenu Entity mainMenu, IngameControls ingameControls) {
        this.toolkit = toolkit;
        this.game = game;
        this.assets = assets;
        this.ingameControls = ingameControls;
        assets.setIcon("icon.png");
        setControls();
        mainMenu.enable();
    }

    @Override
    public void setCloseRequested(boolean closeRequested) {
        this.closeRequested = closeRequested;
    }

    @Override
    public boolean isCloseRequested() {
        return closeRequested || Display.isCloseRequested();
    }

    public void tick() {
        game.setDelta(1.0f / 60.0f);
        game.process();
        Display.update(false);
        Display.sync(60);
        Display.processMessages();
        Mouse.poll();
        Keyboard.poll();
        Controllers.poll();
    }

    public void close() {
        saveControlsPreferences();
        saveVideoModePreferences();
        preferences.saveConfig();
        assets.clearAll();
    }

    private void setVideoMode() {
        try {
            int width = preferences.getInt("video.width", -1);
            int height = preferences.getInt("video.height", -1);
            boolean fullscreen = preferences.getBoolean("video.fullscreen", true);
            if (width < 0 || height < 0) {
                width = Display.getDesktopDisplayMode().getWidth();
                height = Display.getDesktopDisplayMode().getHeight();
            }
            DisplayMode mode = LwjglHelper.findBestDisplayMode(width, height);
            if (fullscreen) {
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
        } catch (LWJGLException ex) {
            throw new RuntimeException("Cannot set video mode", ex);
        }
    }

    private void initGamepads() {
        try {
            Controllers.create();
        } catch (LWJGLException ex) {
            logger.log(Level.SEVERE, "Cannot init gamepad support", ex);
        }
    }

    private void setControls() {
        loadControlsForAction(toolkit.getMenuOK());
        loadControlsForAction(toolkit.getMenuCancel());
        loadControlsForAction(toolkit.getMenuUp());
        loadControlsForAction(toolkit.getMenuDown());
        loadControlsForAction(toolkit.getMenuLeft());
        loadControlsForAction(toolkit.getMenuRight());

        loadControlsForAction(ingameControls.getUp().getAction());
        loadControlsForAction(ingameControls.getDown().getAction());
        loadControlsForAction(ingameControls.getLeft().getAction());
        loadControlsForAction(ingameControls.getRight().getAction());
        loadControlsForAction(ingameControls.getRewind().getAction());
        loadControlsForAction(ingameControls.getShowMenu().getAction());
    }

    private void loadControlsForAction(Action action) {
        String name = action.getName();
        action.setMainControl(preferences.getControl(name + ".main", action.getMainControl()));
        action.setAlternativeControl(preferences.getControl(name + ".alternative", action.getAlternativeControl()));
    }

    private void saveVideoModePreferences() {
        preferences.putInt("video.width", Display.getDisplayMode().getWidth());
        preferences.putInt("video.height", Display.getDisplayMode().getHeight());
        preferences.putBoolean("video.fullscreen", Display.isFullscreen());

    }

    private void saveControlsPreferences() {
        saveControlsForAction(toolkit.getMenuOK());
        saveControlsForAction(toolkit.getMenuCancel());
        saveControlsForAction(toolkit.getMenuUp());
        saveControlsForAction(toolkit.getMenuDown());
        saveControlsForAction(toolkit.getMenuLeft());
        saveControlsForAction(toolkit.getMenuRight());

        saveControlsForAction(ingameControls.getUp().getAction());
        saveControlsForAction(ingameControls.getDown().getAction());
        saveControlsForAction(ingameControls.getLeft().getAction());
        saveControlsForAction(ingameControls.getRight().getAction());
        saveControlsForAction(ingameControls.getRewind().getAction());
        saveControlsForAction(ingameControls.getShowMenu().getAction());
    }

    private void saveControlsForAction(Action action) {
        String name = action.getName();
        preferences.putControl(name + ".main", action.getMainControl());
        preferences.putControl(name + ".alternative", action.getAlternativeControl());
    }

}
