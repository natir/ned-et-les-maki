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
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.Sort;
import im.bci.jnuit.lwjgl.LwjglHelper;
import im.bci.jnuit.lwjgl.LwjglNuitFont;
import im.bci.jnuit.animation.IAnimationFrame;
import im.bci.jnuit.animation.IAnimationImage;
import im.bci.jnuit.animation.IPlay;
import java.util.Comparator;
import com.google.inject.Inject;
import org.geekygoblin.nedetlesmaki.game.Game;
import im.bci.jnuit.lwjgl.assets.IAssets;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.geekygoblin.nedetlesmaki.game.components.Level;
import org.geekygoblin.nedetlesmaki.game.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.game.components.ZOrder;
import org.geekygoblin.nedetlesmaki.game.components.ui.DialogComponent;
import org.geekygoblin.nedetlesmaki.game.constants.VirtualResolution;
import org.geekygoblin.nedetlesmaki.game.utils.Viewport;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 */
public class DrawSystem extends EntitySystem {

    @Mapper
    ComponentMapper<ZOrder> zOrderMapper;
    @Mapper
    ComponentMapper<Sprite> spriteMapper;
    @Mapper
    ComponentMapper<MainMenu> mainMenuMapper;
    @Mapper
    ComponentMapper<DialogComponent> dialogMapper;
    @Mapper
    ComponentMapper<Level> levelMapper;
    private final Comparator<Entity> zComparator = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            int result = Integer.compare(zOrderMapper.get(o1).getZ(), zOrderMapper.get(o2).getZ());
            if (result == 0) {
                Sprite s1 = spriteMapper.get(o1);
                Sprite s2 = spriteMapper.get(o2);
                result = Integer.compare(null != s1 ? 1 : 0, null != s2 ? 1 : 0);
                if (result == 0 && null != s1 && null != s2) {
                    result = spriteProjector.compare(s1.getPosition(), s2.getPosition());
                }
            }
            return result;
        }
    };
    private SpriteProjector spriteProjector;
    private final IAssets assets;
    private final Viewport viewPort = new Viewport();
    private static final float spriteGlobalScale = 2.0f;
    private final Bag<Entity> entitiesSortedByZ = new Bag<>();

    @Override
    protected void inserted(Entity e) {
        entitiesSortedByZ.add(e);
    }

    @Override
    protected void removed(Entity e) {
        entitiesSortedByZ.remove(e);
    }

    @Inject
    public DrawSystem(IAssets assets) {
        super(Aspect.getAspectForAll(ZOrder.class).one(Level.class, MainMenu.class, DialogComponent.class, Sprite.class));
        this.assets = assets;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        GL11.glClearColor(99f / 255f, 201f / 255f, 183f / 255f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
/*        List<Entity> entititesSortedByZ = new ArrayList<>(entities.size());
        for (int i = 0, n = entities.size(); i < n; ++i) {
            final Entity e = entities.get(i);
            if (e.isEnabled()) {
                entititesSortedByZ.add(e);
            }
        }
        Collections.sort(entititesSortedByZ, zComparator);*/
        Sort.instance().sort(entitiesSortedByZ, zComparator);

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_TRANSFORM_BIT | GL11.GL_HINT_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_SCISSOR_BIT | GL11.GL_LINE_BIT | GL11.GL_TEXTURE_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        updateViewPort();
        GL11.glViewport(viewPort.x, viewPort.y, viewPort.width, viewPort.height);
        GLU.gluOrtho2D(-VirtualResolution.WIDTH / 2.0f, VirtualResolution.WIDTH / 2.0f, VirtualResolution.HEIGHT / 2.0f, -VirtualResolution.HEIGHT / 2.0f);
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

        for (Entity e : entitiesSortedByZ) {
            MainMenu mainMenu = mainMenuMapper.getSafe(e);
            if (null != mainMenu) {
                mainMenu.draw();
            }
            DialogComponent dialog = dialogMapper.getSafe(e);
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

        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        IPlay backgroundAnimationPlay = level.getBackground();
        backgroundAnimationPlay.update((long) (world.getDelta() * 1000L));
        final IAnimationFrame currentFrame = backgroundAnimationPlay.getCurrentFrame();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentFrame.getImage().getId());
        float x1 = -VirtualResolution.WIDTH / 2.0f;
        float x2 = VirtualResolution.WIDTH / 2.0f;
        float y1 = VirtualResolution.HEIGHT / 2.0f;
        float y2 = -VirtualResolution.HEIGHT / 2.0f;
        float u1 = currentFrame.getU1();
        float u2 = currentFrame.getU2();

        float v1 = currentFrame.getV2();
        float v2 = currentFrame.getV1();
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
        GL11.glPopMatrix();
    }

    private void drawSprite(Sprite sprite) {
        Vector2f pos = spriteProjector.project(sprite.getPosition());
        final IPlay play = sprite.getPlay();
        if (null != play) {
            GL11.glPushMatrix();
            GL11.glScalef(spriteGlobalScale, spriteGlobalScale, 1.0f);
            GL11.glTranslatef(pos.getX(), pos.getY(), 0.0f);
            GL11.glRotatef(sprite.getRotate(), 0, 0, 1.0f);
            GL11.glScalef(sprite.getScale(), sprite.getScale(), 1);
            final IAnimationFrame frame = play.getCurrentFrame();
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
        }
        if (null != sprite.getLabel()) {
            GL11.glPushMatrix();
            GL11.glScalef(spriteGlobalScale, spriteGlobalScale, 1.0f);
            GL11.glTranslatef(pos.getX(), pos.getY(), 0.0f);
            GL11.glScalef(0.5f, -0.5f, 1f);
            GL11.glEnable(GL11.GL_BLEND);
            LwjglNuitFont font = (LwjglNuitFont)assets.getFont("prout");
            font.drawString(sprite.getLabel(), LwjglNuitFont.Align.CENTER);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public void setSpriteProjector(SpriteProjector spriteProjector) {
        this.spriteProjector = spriteProjector;
    }

    public SpriteProjector getSpriteProjector() {
        return spriteProjector;
    }

    public Vector3f getMouseSpritePos(int yAdjust) {
        if (null != spriteProjector) {
            float mouseX = (Mouse.getX() - viewPort.x) * VirtualResolution.WIDTH / viewPort.width - VirtualResolution.WIDTH / 2.0f;
            float mouseY = VirtualResolution.HEIGHT - ((Mouse.getY() + yAdjust - viewPort.y) * VirtualResolution.HEIGHT / viewPort.height) - VirtualResolution.HEIGHT / 2.0f;
            Entity ned = ((Game) world).getNed();
            if (null != ned) {
                Sprite nedSprite = spriteMapper.get(ned);
                Vector2f nedPos = spriteProjector.project(nedSprite.getPosition());
                mouseX += nedPos.x;
                mouseY += nedPos.y;
            }
            return spriteProjector.unProject(new Vector2f(mouseX / spriteGlobalScale, mouseY / spriteGlobalScale));
        } else {
            return null;
        }
    }

    private void updateViewPort() {
        final float aspect = (float) VirtualResolution.WIDTH / (float) VirtualResolution.HEIGHT;
        int screenWidth = LwjglHelper.getWidth();
        int screenHeight = LwjglHelper.getHeight();
        viewPort.width = screenWidth;
        viewPort.height = (int) (screenWidth / aspect);
        if (viewPort.height > screenHeight) {
            viewPort.height = screenHeight;
            viewPort.width = (int) (screenHeight * aspect);
        }
        viewPort.x = (screenWidth - viewPort.width) / 2;
        viewPort.y = (screenHeight - viewPort.height) / 2;
    }

}
