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

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.Sort;
import im.bci.jnuit.NuitControls;
import im.bci.jnuit.animation.IAnimationFrame;
import im.bci.jnuit.animation.IPlay;
import im.bci.jnuit.artemis.sprite.Sprite;
import im.bci.jnuit.artemis.sprite.SpriteProjector;
import im.bci.jnuit.controls.Pointer;
import im.bci.jnuit.playn.animation.PlaynAnimationImage;
import java.util.Comparator;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.systems.IDrawSystem;
import playn.core.Color;
import playn.core.ImmediateLayer;
import playn.core.PlayN;
import playn.core.Surface;
import pythagoras.f.Vector;
import pythagoras.f.Vector3;

/**
 *
 * @author devnewton
 */
public class PlaynSpriteSystem extends EntitySystem implements IDrawSystem, ImmediateLayer.Renderer {

    ComponentMapper<Sprite> spriteMapper;
    private final Comparator<Entity> zComparator = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            Sprite s1 = spriteMapper.get(o1);
            Sprite s2 = spriteMapper.get(o2);
            if(s1.getZOrder() < s2.getZOrder()) {
                return -1;
            } else if(s1.getZOrder() > s2.getZOrder()) {
                return 1;
            } else {
                return spriteProjector.compare(s1.getPosition(), s2.getPosition());
            }
        }
    };
    private SpriteProjector spriteProjector;
    private final Bag<Entity> sprites = new Bag<Entity>();
    private ImmediateLayer spriteLayer;
    private final NuitControls controls;
    private final Pointer pointer = new Pointer();

    @Override
    protected void initialize() {
        spriteMapper = world.getMapper(Sprite.class);
        spriteLayer = PlayN.graphics().createImmediateLayer(this);
        PlayN.graphics().rootLayer().add(spriteLayer);
    }

    @Override
    protected void inserted(final Entity e) {
        sprites.add(e);
    }

    @Override
    protected void removed(Entity e) {
        sprites.remove(e);
    }

    public PlaynSpriteSystem(NuitControls controls) {
        super(Aspect.getAspectForOne(Sprite.class));
        this.controls = controls;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        Sort.instance().sort(sprites, zComparator);
        NedGame game = (NedGame) world;
        Entity ned = game.getNed();
        if (null != ned) {
            Sprite nedSprite = spriteMapper.get(ned);
            Vector nedPos = spriteProjector.project(nedSprite.getPosition());
            spriteLayer.setTranslation(-nedPos.x + PlayN.graphics().width() / 2.0f, -nedPos.y + PlayN.graphics().height() / 2.0f);
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
        if (null != spriteProjector) {
            controls.pollPointer(PlayN.graphics().width(), PlayN.graphics().height(), pointer);
            Entity ned = ((NedGame) world).getNed();
            float mouseX = pointer.getX() - PlayN.graphics().width() / 2.0f;
            float mouseY = pointer.getY() - yAdjust * 4.0f - PlayN.graphics().height() / 2.0f;
            if (null != ned) {
                Sprite nedSprite = spriteMapper.get(ned);
                Vector nedPos = spriteProjector.project(nedSprite.getPosition());
                mouseX += nedPos.x;
                mouseY += nedPos.y;
            }
            return spriteProjector.unProject(new Vector(mouseX /*/ spriteGlobalScale*/, mouseY /*/ spriteGlobalScale*/));
        } else {
            return null;
        }
    }

    @Override
    public void hideCursor() {
    }

    @Override
    public void setDefaultCursor() {
    }

    @Override
    public void render(Surface surface) {
        for (Entity e : sprites) {
            Sprite sprite = spriteMapper.get(e);
            final IPlay play = sprite.getPlay();
            if (null != play) {
                final IAnimationFrame currentFrame = play.getCurrentFrame();
                if (null != currentFrame) {
                    PlaynAnimationImage image = (PlaynAnimationImage) currentFrame.getImage();
                    Vector projectedPos = spriteProjector.project(sprite.getPosition());
                    surface.save();
                    surface.rotate(sprite.getRotate());
                    surface.scale(sprite.getScale(), sprite.getScale());
                    surface.setTint(Color.argb((int) (255 * sprite.getAlpha()), (int) (255 * sprite.getRed()), (int) (255 * sprite.getGreen()), (int) (255 * sprite.getBlue())));
                    surface.drawImage(image.getImage(), projectedPos.x, projectedPos.y);
                    surface.setTint(WHITE);
                    surface.restore();
                }
            }
        }

    }
    private static final int WHITE = Color.argb(255, 255, 255, 255);

}
