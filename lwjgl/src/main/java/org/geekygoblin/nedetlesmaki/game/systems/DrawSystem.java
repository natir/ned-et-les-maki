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

import org.geekygoblin.nedetlesmaki.core.systems.IDrawSystem;
import org.geekygoblin.nedetlesmaki.core.systems.MouseArrowSystem;
import im.bci.jnuit.artemis.sprite.SpriteProjector;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;

import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.Sort;
import im.bci.jnuit.lwjgl.LwjglHelper;
import im.bci.jnuit.lwjgl.LwjglNuitFont;
import im.bci.jnuit.animation.IAnimationFrame;
import im.bci.jnuit.animation.IPlay;
import java.util.Comparator;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import im.bci.jnuit.NuitFont;
import im.bci.jnuit.NuitRenderer;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.NamedEntities;
import im.bci.jnuit.artemis.sprite.Sprite;
import org.geekygoblin.nedetlesmaki.core.components.LevelBackground;
import org.geekygoblin.nedetlesmaki.core.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.core.components.ui.DialogComponent;
import org.geekygoblin.nedetlesmaki.core.constants.VirtualResolution;
import im.bci.jnuit.lwjgl.sprite.SpriteBatcher;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geekygoblin.nedetlesmaki.core.components.ui.InGameUI;
import org.geekygoblin.nedetlesmaki.core.utils.Viewport;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import pythagoras.f.Vector;
import pythagoras.f.Vector3;

/**
 *
 * @author devnewton
 */
@Singleton
public class DrawSystem extends EntitySystem implements IDrawSystem {

    private ComponentMapper<LevelBackground> levelBackgroundMapper;
    private ComponentMapper<Sprite> spriteMapper;
    private ComponentMapper<MainMenu> mainMenuMapper;
    private ComponentMapper<DialogComponent> dialogMapper;
    private ComponentMapper<InGameUI> inGameUIMapper;

    private final Comparator<Entity> zComparator = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            Sprite s1 = spriteMapper.get(o1);
            Sprite s2 = spriteMapper.get(o2);
            int result = Integer.compare(s1.getZOrder(), s2.getZOrder());
            if (result == 0) {
                result = spriteProjector.compare(s1.getPosition(), s2.getPosition());
            }
            return result;
        }
    };
    private SpriteProjector spriteProjector;
    private final Viewport viewPort = new Viewport();
    private static final float spriteGlobalScale = 1.0f;
    private final Bag<Entity> backgrounds = new Bag<Entity>();
    private final Bag<Entity> sprites = new Bag<Entity>();
    private final Bag<Entity> uis = new Bag<Entity>();

    private final SpriteBatcher spriteBatcher = new SpriteBatcher();
    private final LwjglNuitFont font;
    private final NuitRenderer nuitRenderer;

    @Override
    protected void initialize() {
        levelBackgroundMapper = world.getMapper(LevelBackground.class);
        spriteMapper = world.getMapper(Sprite.class);
        mainMenuMapper = world.getMapper(MainMenu.class);
        dialogMapper = world.getMapper(DialogComponent.class);
        inGameUIMapper = world.getMapper(InGameUI.class);
    }

    @Override
    protected void inserted(Entity e) {
        if (levelBackgroundMapper.has(e)) {
            backgrounds.add(e);
        } else if (spriteMapper.has(e)) {
            sprites.add(e);
        } else {
            uis.add(e);
        }
    }

    @Override
    protected void removed(Entity e) {
        backgrounds.remove(e);
        sprites.remove(e);
        uis.remove(e);
    }

    @Inject
    public DrawSystem(@NamedEntities.DefaultFont NuitFont font, NuitRenderer nuitRenderer) {
        super(Aspect.getAspectForOne(LevelBackground.class, MainMenu.class, InGameUI.class, DialogComponent.class, Sprite.class));
        this.font = (LwjglNuitFont) font;
        this.nuitRenderer = nuitRenderer;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        GL11.glClearColor(99f / 255f, 201f / 255f, 183f / 255f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        Sort.instance().sort(sprites, zComparator);

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

        NedGame game = (NedGame) world;
        Entity ned = game.getNed();
        if (null != ned) {
            Sprite nedSprite = spriteMapper.get(ned);
            Vector nedPos = spriteProjector.project(nedSprite.getPosition());
            GL11.glTranslatef(-nedPos.x, -nedPos.y, 0.0f);
        }

        for (Entity e : backgrounds) {
            LevelBackground level = levelBackgroundMapper.getSafe(e);
            if (null != level) {
                drawLevel(level);
            }
        }

        GL11.glPushMatrix();
        GL11.glScalef(spriteGlobalScale, spriteGlobalScale, 1.0f);
        spriteBatcher.begin();
        for (Entity e : sprites) {
            spriteBatcher.draw(spriteMapper.getSafe(e));
        }
        spriteBatcher.end();
        for (Entity e : sprites) {
            drawSpriteLabel(spriteMapper.getSafe(e));
        }
        GL11.glPopMatrix();

        for (Entity e : uis) {
            MainMenu mainMenu = mainMenuMapper.getSafe(e);
            if (null != mainMenu) {
                nuitRenderer.render(mainMenu.getRoot());
            }
            DialogComponent dialog = dialogMapper.getSafe(e);
            if (null != dialog) {
                nuitRenderer.render(dialog.getRoot());
            }
            InGameUI inGameUI = inGameUIMapper.getSafe(e);
            if (null != inGameUI) {
                nuitRenderer.render(inGameUI.getRoot());
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

    private void drawLevel(LevelBackground level) {

        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        IPlay backgroundAnimationPlay = level.getBackground();
        backgroundAnimationPlay.update((long) (world.getDelta() * 1000L));
        final IAnimationFrame currentFrame = backgroundAnimationPlay.getCurrentFrame();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, (Integer) currentFrame.getImage().getId());
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

    private void drawSpriteLabel(Sprite sprite) {
        Vector pos = spriteProjector.project(sprite.getPosition());
        if (null != sprite.getLabel()) {
            GL11.glPushMatrix();
            GL11.glScalef(spriteGlobalScale, spriteGlobalScale, 1.0f);
            GL11.glTranslatef(pos.x, pos.y, 0.0f);
            GL11.glScalef(0.5f, -0.5f, 1f);
            GL11.glEnable(GL11.GL_BLEND);
            font.drawString(sprite.getLabel(), LwjglNuitFont.Align.CENTER);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public void setSpriteProjector(SpriteProjector spriteProjector) {
        this.spriteProjector = spriteProjector;
        this.spriteBatcher.setSpriteProjector(spriteProjector);
    }

    public SpriteProjector getSpriteProjector() {
        return spriteProjector;
    }

    @Override
    public Vector3 getMouseSpritePos(int yAdjust) {
        if (null != spriteProjector) {
            float mouseX = (Mouse.getX() - viewPort.x) * VirtualResolution.WIDTH / viewPort.width - VirtualResolution.WIDTH / 2.0f;
            float mouseY = VirtualResolution.HEIGHT - ((Mouse.getY() + yAdjust - viewPort.y) * VirtualResolution.HEIGHT / viewPort.height) - VirtualResolution.HEIGHT / 2.0f;
            Entity ned = ((NedGame) world).getNed();
            if (null != ned) {
                Sprite nedSprite = spriteMapper.get(ned);
                Vector nedPos = spriteProjector.project(nedSprite.getPosition());
                mouseX += nedPos.x;
                mouseY += nedPos.y;
            }
            return spriteProjector.unProject(new Vector(mouseX / spriteGlobalScale, mouseY / spriteGlobalScale));
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

    @Override
    public void hideCursor() {
        try {
            Cursor emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
            Mouse.setNativeCursor(emptyCursor);
        } catch (LWJGLException ex) {
            Logger.getLogger(MouseArrowSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setDefaultCursor() {
        try {
            Mouse.setNativeCursor(null);
        } catch (LWJGLException ex) {
            Logger.getLogger(MouseArrowSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
