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

import im.bci.jnuit.NuitPreferences;
import im.bci.jnuit.NuitControls;
import im.bci.jnuit.controls.Control;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devnewton
 */
public class LwjglNuitPreferences implements NuitPreferences {

    private final Properties store = new Properties();
    private final String appName;
    private final NuitControls controls;

    public LwjglNuitPreferences(NuitControls controls, String appName) {
        this.controls = controls;
        this.appName = appName;
        load();
    }

    private void load() {
        File userConfigFile = getUserConfigFilePath();
        if (userConfigFile.exists() && userConfigFile.canRead()) {
            try (FileInputStream is = new FileInputStream(userConfigFile)) {
                store.load(is);
            } catch (IOException ex) {
                Logger.getLogger(LwjglNuitPreferences.class.getName()).log(Level.WARNING, "Cannot load config from file " + userConfigFile, ex);
            }
        }
    }

    @Override
    public void saveConfig() {
        File userConfigFile = getUserConfigFilePath();

        if (!userConfigFile.exists()) {
            getUserConfigDirPath().mkdirs();
        }
        try (FileOutputStream os = new FileOutputStream(userConfigFile)) {
            store.store(os, "");

        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot save config to file " + userConfigFile, e);
        }
    }

    public File getUserConfigDirPath() {
        String configDirPath = System.getenv("XDG_CONFIG_HOME");
        if (null == configDirPath) {
            configDirPath = System.getenv("APPDATA");
        }
        if (null == configDirPath) {
            configDirPath = System.getProperty("user.home") + File.separator
                    + ".config";
        }
        return new File(configDirPath, appName);
    }

    private File getUserConfigFilePath() {
        return new File(getUserConfigDirPath(), "config.properties");
    }

    @Override
    public void putBoolean(String name, boolean value) {
        store.setProperty(name, String.valueOf(value));
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        return Boolean.valueOf(getSystemOrStoreProperty(name, String.valueOf(defaultValue)));
    }

    @Override
    public void putInt(String name, int value) {
        store.setProperty(name, String.valueOf(value));
    }

    @Override
    public int getInt(String name, int defaultValue) {
        return Integer.valueOf(getSystemOrStoreProperty(name, String.valueOf(defaultValue)));
    }

    @Override
    public void putControl(String name, Control value) {
        if (null != value) {
            store.setProperty(name + ".controller", value.getControllerName());
            store.setProperty(name + ".control", value.getName());
        } else {
            store.remove(name + ".controller");
            store.remove(name + ".control");
        }
    }

    @Override
    public Control getControl(String name, Control defaultValue) {
        String controllerName = getSystemOrStoreProperty(name + ".controller", null);
        String controlName = getSystemOrStoreProperty(name + ".control", null);
        for (Control control : controls.getPossibleControls()) {
            if (control.getControllerName().equals(controllerName) && control.getName().equals(controlName)) {
                return control;
            }
        }
        return defaultValue;
    }

    private String getSystemOrStoreProperty(String name, String defaultValue) {
        final String systemProperty = System.getProperty("nedetlesmaki."+name);
        if (null != systemProperty) {
            return systemProperty;
        } else {
            return store.getProperty(name, defaultValue);
        }
    }

}
