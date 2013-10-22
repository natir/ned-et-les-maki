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
package org.geekygoblin.nedetlesmaki.game;

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
public class Preferences {

    private final Properties store = new Properties();
    private static final String appName = "nedetlesmaki";
    
    public Preferences() {
        load();
    }

    public void load() {
        File userConfigFile = getUserConfigFilePath();
        if(userConfigFile.exists() && userConfigFile.canRead()) {
            try(FileInputStream is = new FileInputStream(userConfigFile)) {
                store.load(is);
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.WARNING, "Cannot load config from file " + userConfigFile, ex);
            }
        }
    }

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

    public void putInt(String name, int value) {
        store.setProperty(name, String.valueOf(value));
    }

    public void putBoolean(String name, boolean value) {
        store.setProperty(name, String.valueOf(value));
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return Boolean.valueOf(store.getProperty(name, String.valueOf(defaultValue)));
    }
    
    public int getInt(String name, int defaultValue) {
        return Integer.valueOf(store.getProperty(name, String.valueOf(defaultValue)));
    }

}
