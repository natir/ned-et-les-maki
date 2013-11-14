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
package org.geekygoblin.nedetlesmaki.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import im.bci.lwjgl.nuit.utils.LwjglHelper;
import im.bci.lwjgl.nuit.utils.TrueTypeFont;
import im.bci.nanim.IAnimationFrame;
import im.bci.nanim.IAnimationImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.geekygoblin.nedetlesmaki.game.components.Level;
import org.geekygoblin.nedetlesmaki.game.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.game.components.ZOrder;
import org.geekygoblin.nedetlesmaki.game.components.ui.Dialog;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author devnewton
 */
public class DrawSystem extends EntitySystem {

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 800;

    @Mapper
    ComponentMapper<ZOrder> zOrderMapper;
    @Mapper
    ComponentMapper<Sprite> spriteMapper;
    @Mapper
    ComponentMapper<MainMenu> mainMenuMapper;
    @Mapper
    ComponentMapper<Dialog> dialogMapper;
    @Mapper
    ComponentMapper<Level> levelMapper;
    private final Comparator<Entity> zComparator = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            int result = Integer.compare(zOrderMapper.get(o1).getZ(), zOrderMapper.get(o2).getZ());
            if (result == 0) {
                Sprite s1 = spriteMapper.get(o1);
                Sprite s2 = spriteMapper.get(o2);
                if (null != s1 && null != s2) {
                    result = spriteProjector.compare(s1.getPosition(), s2.getPosition());
                }
            }
            return result;
        }
    };
    private SpriteProjector spriteProjector;
    private final Assets assets;

    @Inject
    public DrawSystem(Assets assets) {
        super(Aspect.getAspectForAll(ZOrder.class).one(Level.class, MainMenu.class, Dialog.class, Sprite.class));
        this.assets = assets;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        List<Entity> entititesSortedByZ = new ArrayList<>(entities.size());
        for (int i = 0, n = entities.size(); i < n; ++i) {
            final Entity e = entities.get(i);
            if (e.isEnabled()) {
                entititesSortedByZ.add(e);
            }
        }
        Collections.sort(entititesSortedByZ, zComparator);

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_TRANSFORM_BIT | GL11.GL_HINT_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_SCISSOR_BIT | GL11.GL_LINE_BIT | GL11.GL_TEXTURE_BIT);
        GL11.glViewport(0, 0, LwjglHelper.getWidth(), LwjglHelper.getHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        //
        final float aspect = (float) SCREEN_WIDTH / (float) SCREEN_HEIGHT;
        int screenWidth = LwjglHelper.getWidth();
        int screenHeight = LwjglHelper.getHeight();
        int viewWidth = screenWidth;
        int viewHeight = (int) (screenWidth / aspect);
        if (viewHeight > screenHeight) {
            viewHeight = screenHeight;
            viewWidth = (int) (screenHeight * aspect);
        }
        int vportX = (screenWidth - viewWidth) / 2;
        int vportY = (screenHeight - viewHeight) / 2;
        GL11.glViewport(vportX, vportY, viewWidth, viewHeight);
        GLU.gluOrtho2D(-SCREEN_WIDTH / 2.0f, SCREEN_WIDTH / 2.0f, SCREEN_HEIGHT / 2.0f, -SCREEN_HEIGHT / 2.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        Game game = (Game) world;
        Entity ned = game.getNed();
        if (null != ned) {
            Sprite nedSprite = spriteMapper.get(ned);
            Vector2f nedPos = spriteProjector.project(nedSprite.getPosition());
            GL11.glTranslatef(-nedPos.x, -nedPos.y, 0.0f);
        }

        for (Entity e : entititesSortedByZ) {
            MainMenu mainMenu = mainMenuMapper.getSafe(e);
            if (null != mainMenu) {
                mainMenu.draw();
            }
            Dialog dialog = dialogMapper.getSafe(e);
            if (null != dialog) {
                dialog.draw();
            }
            Level level = levelMapper.getSafe(e);
            if (null != level) {
                drawLevel(level);
            }
            Sprite sprite = spriteMapper.getSafe(e);
            if (null != sprite) {
                drawSprite(sprite);
            }
        }
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glPopAttrib();

    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    private void drawLevel(Level level) {

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_POINTS);

        GL11.glColor3f(1f, 0f, 0f);
        GL11.glVertex2f(100, 0);
        GL11.glColor3f(0f, 1f, 0f);
        GL11.glVertex2f(0, 100);
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glVertex2f(0, 0);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);

    }

    private void drawSprite(Sprite sprite) {
        GL11.glPushMatrix();
        Vector2f pos = spriteProjector.project(sprite.getPosition());
        GL11.glTranslatef(pos.getX(), pos.getY(), 0.0f);
        GL11.glRotatef(sprite.getRotate(), 0, 0, 1.0f);
        GL11.glScalef(sprite.getScale(), sprite.getScale(), 1);
        final IAnimationFrame frame = sprite.getPlay().getCurrentFrame();
        final IAnimationImage image = frame.getImage();
        if (image.hasAlpha()) {
            GL11.glEnable(GL11.GL_BLEND);
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, image.getId());

        final float u1, u2;
        if (sprite.isMirrorX()) {
            u1 = frame.getU2();
            u2 = frame.getU1();
        } else {
            u1 = frame.getU1();
            u2 = frame.getU2();
        }

        final float v1, v2;
        if (sprite.isMirrorY()) {
            v1 = frame.getV1();
            v2 = frame.getV2();
        } else {
            v1 = frame.getV2();
            v2 = frame.getV1();
        }
        final Color color = sprite.getColor();
        GL11.glColor4ub(color.getRedByte(), color.getGreenByte(), color.getBlueByte(), color.getAlphaByte());
        float x1 = -sprite.getWidth() / 2.0f;
        float x2 = sprite.getWidth() / 2.0f;
        float y1 = -sprite.getHeight() / 2.0f;
        float y2 = sprite.getHeight() / 2.0f;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x1, y2);
        GL11.glTexCoord2f(u2, v1);
        GL11.glVertex2f(x2, y2);
        GL11.glTexCoord2f(u2, v2);
        GL11.glVertex2f(x2, y1);
        GL11.glTexCoord2f(u1, v2);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
        GL11.glColor3f(1f, 1f, 1f);
        if (image.hasAlpha()) {
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glPopMatrix();
        if (null != sprite.getLabel()) {
            GL11.glPushMatrix();
            GL11.glTranslatef(pos.getX(), pos.getY(), 0.0f);
            GL11.glScalef(0.5f, -0.5f, 1f);
            GL11.glEnable(GL11.GL_BLEND);
            assets.getFont("prout").drawString(sprite.getLabel(), TrueTypeFont.Align.CENTER);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public void setSpriteProjector(SpriteProjector spriteProjector) {
        this.spriteProjector = spriteProjector;
    }
}
