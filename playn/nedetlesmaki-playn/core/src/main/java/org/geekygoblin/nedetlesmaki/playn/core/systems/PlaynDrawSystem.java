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
package org.geekygoblin.nedetlesmaki.playn.core.systems;

import org.geekygoblin.nedetlesmaki.core.systems.IDrawSystem;
import im.bci.jnuit.artemis.sprite.SpriteProjector;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;

import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.Sort;
import java.util.Comparator;
import im.bci.jnuit.NuitFont;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.NamedEntities;
import im.bci.jnuit.artemis.sprite.Sprite;
import org.geekygoblin.nedetlesmaki.core.components.LevelBackground;
import org.geekygoblin.nedetlesmaki.core.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.core.components.ui.DialogComponent;
import org.geekygoblin.nedetlesmaki.core.utils.Viewport;
import pythagoras.f.Vector;
import pythagoras.f.Vector3;

/**
 *
 * @author devnewton
 */
public class PlaynDrawSystem extends EntitySystem implements IDrawSystem {

    ComponentMapper<LevelBackground> levelBackgroundMapper;
    ComponentMapper<Sprite> spriteMapper;
    ComponentMapper<MainMenu> mainMenuMapper;
    ComponentMapper<DialogComponent> dialogMapper;
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
    private static final float spriteGlobalScale = 1.5f;
    private final Bag<Entity> backgrounds = new Bag<Entity>();
    private final Bag<Entity> sprites = new Bag<Entity>();
    private final Bag<Entity> uis = new Bag<Entity>();

    @Override
    protected void initialize() {
        levelBackgroundMapper = world.getMapper(LevelBackground.class);
        spriteMapper = world.getMapper(Sprite.class);
        mainMenuMapper = world.getMapper(MainMenu.class);
        dialogMapper = world.getMapper(DialogComponent.class);
    }

    @Override
    protected void inserted(final Entity e) {
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

    public PlaynDrawSystem(@NamedEntities.DefaultFont NuitFont font) {
        super(Aspect.getAspectForOne(LevelBackground.class, MainMenu.class, DialogComponent.class, Sprite.class));

    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        Sort.instance().sort(sprites, zComparator);

        NedGame game = (NedGame) world;
        Entity ned = game.getNed();
        if (null != ned) {
            Sprite nedSprite = spriteMapper.get(ned);
            Vector nedPos = spriteProjector.project(nedSprite.getPosition());
//TODO            GL11.glTranslatef(-nedPos.x, -nedPos.y, 0.0f);
        }

        for (Entity e : backgrounds) {
            LevelBackground level = levelBackgroundMapper.getSafe(e);
            if (null != level) {
                //  drawLevel(level);
            }
        }

    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    public void setSpriteProjector(SpriteProjector spriteProjector) {
        this.spriteProjector = spriteProjector;
    }

    public SpriteProjector getSpriteProjector() {
        return spriteProjector;
    }

    @Override
    public Vector3 getMouseSpritePos(int yAdjust) {
        /*        if (null != spriteProjector) {
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
         }*/
        return null;
    }

    @Override
    public void hideCursor() {
    }

    @Override
    public void setDefaultCursor() {
    }

}
