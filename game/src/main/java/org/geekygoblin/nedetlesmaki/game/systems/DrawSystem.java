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
package org.geekygoblin.nedetlesmaki.game.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import im.bci.lwjgl.nuit.utils.LwjglHelper;
import java.util.Comparator;
import java.util.TreeSet;
import org.geekygoblin.nedetlesmaki.game.components.Level;
import org.geekygoblin.nedetlesmaki.game.components.MainMenu;
import org.geekygoblin.nedetlesmaki.game.components.ZOrder;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author devnewton
 */
public class DrawSystem extends EntitySystem {

    private static final Comparator<Entity> zComparator = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            return Integer.compare(o1.getComponent(ZOrder.class).getZ(), o2.getComponent(ZOrder.class).getZ());
        }
    };

    public DrawSystem() {
        super(Aspect.getAspectForAll(ZOrder.class).one(Level.class, MainMenu.class));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        TreeSet<Entity> entititesSortedByZ = new TreeSet<>(zComparator);
        for (int i = 0, n = entities.size(); i < n; ++i) {
            final Entity e = entities.get(i);
            if(e.isEnabled()) {
                entititesSortedByZ.add(e);
            }
        }

        for (Entity e : entititesSortedByZ) {
            MainMenu mainMenu = e.getComponent(MainMenu.class);
            if(null != mainMenu) {
                mainMenu.draw();
            }
            Level level = e.getComponent(Level.class);
            if(null != level) {
                drawLevel(level);
            }
        }

    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    private void drawLevel(Level level) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_TRANSFORM_BIT | GL11.GL_HINT_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_SCISSOR_BIT | GL11.GL_LINE_BIT | GL11.GL_TEXTURE_BIT);
        GL11.glViewport(0, 0, LwjglHelper.getWidth(), LwjglHelper.getHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 1280, 800, 0, -1.0, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        GL11.glBegin(GL11.GL_POINTS);
        for(int x = 0; x<level.getWidth(); ++x) {
            for(int y = 0; y<level.getWidth(); ++y) {
                GL11.glVertex2f(x * 10, y * 10);
            }
        }
        GL11.glEnd();

        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glPopAttrib();

    }
}
