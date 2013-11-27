/*
 * Copyright © 2013, Pierre Marijon <pierre@marijon.fr>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * The Software is provided "as is", without warranty of any kind, express or 
 * implied, including but not limited to the warranties of merchantability, 
 * fitness for a particular purpose and noninfringement. In no event shall the 
 * authors or copyright holders X be liable for any claim, damages or other 
 * liability, whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other dealings in the
 * Software.
 */
package org.geekygoblin.nedetlesmaki.game.systems;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Iterator;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;

import im.bci.nanim.IAnimationCollection;

import org.geekygoblin.nedetlesmaki.game.manager.EntityIndexManager;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Plate;
import org.geekygoblin.nedetlesmaki.game.components.visual.Sprite;
import org.geekygoblin.nedetlesmaki.game.components.visual.SpritePuppetControls;
import org.geekygoblin.nedetlesmaki.game.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.game.constants.AnimationType;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.utils.Mouvement;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author natir
 */
public class UpdateLevelVisualSystem extends VoidEntitySystem {

    @Mapper
    ComponentMapper<Sprite> spriteMapper;
    @Mapper
    ComponentMapper<Plate> plateMapper;

    private final Assets assets;
    private int nbIndexSaved;
    private final EntityIndexManager index;

    @Inject
    public UpdateLevelVisualSystem(Assets assets, EntityIndexManager indexSystem) {
        this.assets = assets;
        this.nbIndexSaved = 0;
        this.index = indexSystem;
    }

    @Override
    protected void processSystem() {

        if (index.sizeOfStack() != nbIndexSaved) {
            nbIndexSaved = index.sizeOfStack();
            ArrayList<Mouvement> change = this.index.getChangement();

            if (change != null) {
                for (int i = 0; i != change.size(); i++) {
                    
                    ArrayList<Position> tmpLP = change.get(i).getPositionList();
                    ArrayList<AnimationType> tmpLA = change.get(i).getAnimationList();
                    for (Iterator itP = tmpLP.iterator(), itA = tmpLA.iterator(); itP.hasNext() || itA.hasNext();) {
                        this.moveSprite(change.get(i).getEntity(), (Position) itP.next(), (AnimationType) itA.next());
                    }
                }
            }
        }
    }

    private void moveSprite(Entity e, Position diff, AnimationType a) {

        Sprite sprite = e.getComponent(Sprite.class);
        Vector3f pos = sprite.getPosition();
        SpritePuppetControls updatable = new SpritePuppetControls(sprite);
        Position p = new Position((int) diff.getY(), (int) diff.getX());
        
        IAnimationCollection nedAnim = this.assets.getAnimations("ned.nanim");
        IAnimationCollection makiAnim = this.assets.getAnimations("maki.nanim");
        
        if (a == AnimationType.no) {
            updatable.moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.ned_right) {
            updatable.startAnimation(nedAnim.getAnimationByName("walk_right"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.ned_left) {
            updatable.startAnimation(nedAnim.getAnimationByName("walk_left"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.ned_down) {
            updatable.startAnimation(nedAnim.getAnimationByName("walk_down"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.ned_up) {
            updatable.startAnimation(nedAnim.getAnimationByName("walk_up"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.ned_push_right) {
            updatable.startAnimation(nedAnim.getAnimationByName("push_right"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.ned_push_left) {
            updatable.startAnimation(nedAnim.getAnimationByName("push_left"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.ned_push_down) {
            updatable.startAnimation(nedAnim.getAnimationByName("push_down"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.ned_push_up) {
            updatable.startAnimation(nedAnim.getAnimationByName("push_up"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.box_destroy) {
            e.removeComponent(sprite);
        } else if (a == AnimationType.maki_green_one) {
            updatable.startAnimation(makiAnim.getAnimationByName("maki_green_one"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.maki_orange_one) {
            updatable.startAnimation(makiAnim.getAnimationByName("maki_orange_one"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.maki_blue_one) {
            updatable.startAnimation(makiAnim.getAnimationByName("maki_blue_one"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.maki_green_out) {
            updatable.startAnimation(makiAnim.getAnimationByName("maki_green_out"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.maki_orange_out) {
            updatable.startAnimation(makiAnim.getAnimationByName("maki_orange_out"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        } else if (a == AnimationType.maki_blue_out) {
            updatable.startAnimation(makiAnim.getAnimationByName("maki_blue_out"))
                    .moveTo(new Vector3f(p.getX(), p.getY(), pos.z), 0.5f)
                    .stopAnimation();
        }

        e.addComponent(updatable);
        e.changedInWorld();
    }

    private boolean canMove() {
        return world.getSystem(SpritePuppetControlSystem.class).getActives().isEmpty();
    }
}
