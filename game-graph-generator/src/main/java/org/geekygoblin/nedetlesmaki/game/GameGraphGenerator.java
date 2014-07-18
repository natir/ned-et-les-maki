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
import com.google.inject.grapher.GrapherModule;
import com.google.inject.grapher.InjectorGrapher;
import com.google.inject.grapher.graphviz.GraphvizModule;
import com.google.inject.grapher.graphviz.GraphvizRenderer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author devnewton
 */
public class GameGraphGenerator {

    public static void main(String[] args) throws IOException {
        NedModule module = new NedModule();
        Injector injector = Guice.createInjector(module);
        graph("ned.gv", injector);
        Runtime.getRuntime().exec("dot -Tsvg ned.gv -o ned.svg");
    }

    private static void graph(String filename, Injector nedInjector) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter bout = new PrintWriter(baos);
        Injector injector = Guice.createInjector(new GrapherModule(), new GraphvizModule());
        GraphvizRenderer renderer = injector.getInstance(GraphvizRenderer.class);
        renderer.setOut(bout);
        injector.getInstance(InjectorGrapher.class)
                .of(nedInjector)
                .graph();
        PrintWriter out = new PrintWriter(new File(filename), "UTF-8");
            String s = baos.toString("UTF-8");
        s = fixGrapherBug(s);
        s = hideClassPaths(s);
        out.write(s);

    }

    public static String fixGrapherBug(String s) {
        s = s.replaceAll("style=invis", "style=solid");
        s = s.replaceAll(" margin=(\\S+), ", " margin=\"$1\", ");
        return s;
    }

    private static String hideClassPaths(String s) {
        s = s.replaceAll("style=invis", "style=solid");
        return s;
    }
}
