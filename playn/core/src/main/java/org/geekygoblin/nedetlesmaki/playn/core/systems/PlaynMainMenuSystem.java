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

import com.artemis.systems.EntityProcessingSystem;
import im.bci.jnuit.playn.PlaynNuitRenderer;
import org.geekygoblin.nedetlesmaki.core.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.playn.core.components.ui.PlaynLayerOwner;
import playn.core.ImmediateLayer;
import playn.core.PlayN;
import playn.core.Surface;

/**
 *
 * @author devnewton <devnewton@bci.im>
 */
public class PlaynMainMenuSystem extends EntityProcessingSystem {

    private final PlaynNuitRenderer nuitRenderer;

    private ComponentMapper<PlaynLayerOwner> layerOwnerMapper;
    protected ComponentMapper<MainMenu> mainMenuMapper;

    @Override
    protected void initialize() {
        layerOwnerMapper = world.getMapper(PlaynLayerOwner.class);
        mainMenuMapper = world.getMapper(MainMenu.class);
    }

    @Override
    protected void process(Entity e) {
        if (e.isEnabled()) {
            mainMenuMapper.get(e).update();
        }
    }

    public PlaynMainMenuSystem(PlaynNuitRenderer nuitRenderer) {
        super(Aspect.getAspectForOne(MainMenu.class));
        this.nuitRenderer = nuitRenderer;
    }

    @Override
    protected void inserted(final Entity e) {
        ImmediateLayer layer = PlayN.graphics().createImmediateLayer(PlayN.graphics().width(), PlayN.graphics().height(), new ImmediateLayer.Renderer() {

            @Override
            public void render(Surface surface) {
                nuitRenderer.setSurface(surface);
                nuitRenderer.render(mainMenuMapper.get(e).getRoot());
            }
        });
        e.addComponent(new PlaynLayerOwner(layer));
        PlayN.graphics().rootLayer().add(layer);
    }

    @Override
    protected void removed(Entity e) {
        PlayN.graphics().rootLayer().remove(layerOwnerMapper.get(e).getLayer());
    }
    
}
