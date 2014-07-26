/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

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
import im.bci.jnuit.animation.IAnimationFrame;
import im.bci.jnuit.animation.IPlay;
import im.bci.jnuit.playn.animation.PlaynAnimationImage;
import org.geekygoblin.nedetlesmaki.core.components.LevelBackground;
import playn.core.ImmediateLayer;
import playn.core.PlayN;
import playn.core.Surface;

/**
 *
 * @author devnewton
 */
public class PlaynLevelBackgroundSystem extends EntitySystem implements ImmediateLayer.Renderer {

    private ComponentMapper<LevelBackground> levelBackgroundMapper;
    private ImmediateLayer backgroundGroupLayer;
    private final Bag<Entity> backgrounds = new Bag<Entity>();

    public PlaynLevelBackgroundSystem() {
        super(Aspect.getAspectForOne(LevelBackground.class));
    }
    
    @Override
    protected void initialize() {
        levelBackgroundMapper = world.getMapper(LevelBackground.class);
        backgroundGroupLayer = PlayN.graphics().createImmediateLayer(this);
        PlayN.graphics().rootLayer().add(backgroundGroupLayer);
    }

    @Override
    protected void inserted(final Entity e) {
        backgrounds.add(e);
    }

    @Override
    protected void removed(Entity e) {
        backgrounds.remove(e);
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    public void render(Surface surface) {
        for (Entity e : backgrounds) {
            IPlay play = levelBackgroundMapper.get(e).getBackground();
            if (null != play) {
                final IAnimationFrame currentFrame = play.getCurrentFrame();
                if (null != currentFrame) {
                    PlaynAnimationImage image = (PlaynAnimationImage) currentFrame.getImage();
                    surface.drawImage(image.getImage(), 0, 0, surface.width(), surface.height());
                }
            }
        }

    }

}
