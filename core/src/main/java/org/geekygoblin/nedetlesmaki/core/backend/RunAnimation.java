/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.core.backend;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.systems.VoidEntitySystem;
import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.artemis.sprite.Sprite;
import im.bci.jnuit.artemis.sprite.SpritePuppetControls;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import org.geekygoblin.nedetlesmaki.core.components.Triggerable;
import org.geekygoblin.nedetlesmaki.core.constants.AnimationType;
import org.geekygoblin.nedetlesmaki.core.events.ShowLevelMenuTrigger;
import pythagoras.f.Vector3;

import org.geekygoblin.nedetlesmaki.core.components.gamesystems.GameObject;

/**
 *
 * @author pierre
 */
@Singleton
public class RunAnimation {

    private IAssets assets;
    private ComponentMapper<SpritePuppetControls> control;

    @Inject
    public RunAnimation(World w, IAssets assets) {
        this.control = w.getMapper(SpritePuppetControls.class);
        this.assets = assets;
    }

    public void moveTo(GameObject go) {
        Entity e = go.getEntity();
        Position p = go.getPos();
        Sprite sprite = e.getComponent(Sprite.class);
        SpritePuppetControls updatable = this.control.getSafe(e);
        if (updatable == null) {
            updatable = new SpritePuppetControls(sprite);
        }

        updatable
                .moveTo(new Vector3((float) p.getX(), (float) p.getY(), 0.0f), 0);
    }
}
