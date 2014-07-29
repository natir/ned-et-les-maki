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

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class NormalLauncher {

    private static final Logger logger = Logger.getLogger(NormalLauncher.class.getName());

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
            logger.log(Level.INFO, "Computed 'natives' folder: {0}", nativeDir);
            if (nativeDir.exists() && nativeDir.isDirectory() && nativeDir.list().length > 0) {
                String nativePath = nativeDir.getCanonicalPath();
                System.setProperty("org.lwjgl.librarypath", nativePath);
                System.setProperty("net.java.games.input.librarypath", nativePath);
                return;
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "error", e);
        }
        logger.log(Level.INFO, "Cannot find 'natives' library folder, try system libraries");
    }

    public static File getApplicationDir() {
        try {
            return new File(NormalLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException uriEx) {
            logger.log(Level.WARNING, "Cannot find application directory, try current", uriEx);
            return new File(".");
        }
    }

    public static void launch(String[] args) throws IOException, ClassNotFoundException, Exception {
        configureLogging();

        try {
            setupLibraryPath();
        } catch (Throwable e) {
            handleError(e, "Unexpected error during startup. Check your java version and your opengl driver.\n");
            return;
        }

        try {
            NedModule module = new NedModule();
            Injector injector = Guice.createInjector(module);
            MainLoop mainLoop = injector.getInstance(MainLoop.class);
            while (!mainLoop.isCloseRequested()) {
                mainLoop.tick();
            }
            mainLoop.close();
        } catch (Throwable e) {
            handleError(e, "Unexpected error during execution.\n");
        }
    }

    private static void configureLogging() throws SecurityException, IOException {
        LogManager.getLogManager().readConfiguration(NormalLauncher.class.getClassLoader().getResourceAsStream("logging.properties"));
    }

    public static void handleError(Throwable e, final String defaultMessage) {
        logger.log(Level.SEVERE, defaultMessage, e);
        JOptionPane.showMessageDialog(null, defaultMessage + "\n" + e.getMessage() + (e.getCause() != null ? "\nCause: " + e.getCause().getMessage() : ""), "Error", JOptionPane.ERROR_MESSAGE);
    }

}
