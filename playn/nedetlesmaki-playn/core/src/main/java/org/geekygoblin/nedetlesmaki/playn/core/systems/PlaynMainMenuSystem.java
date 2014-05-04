/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.playn.core.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;

import com.artemis.systems.EntityProcessingSystem;
import im.bci.jnuit.NuitRenderer;
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
