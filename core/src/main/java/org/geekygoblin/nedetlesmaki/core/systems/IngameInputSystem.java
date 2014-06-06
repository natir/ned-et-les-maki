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
package org.geekygoblin.nedetlesmaki.core.systems;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;

import com.artemis.systems.EntityProcessingSystem;

import im.bci.jnuit.controls.Action;
import im.bci.jnuit.artemis.sprite.Sprite;
import im.bci.jnuit.controls.ActionActivatedDetector;

import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.IDefaultControls;
import org.geekygoblin.nedetlesmaki.core.components.Triggerable;
import org.geekygoblin.nedetlesmaki.core.events.ShowMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.components.IngameControls;
import org.geekygoblin.nedetlesmaki.core.manager.EntityIndexManager;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.core.events.ShowLevelMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.utils.MoveStory;

import pythagoras.f.Vector3;

/**
 *
 * @author devnewton
 */
@Singleton
public class IngameInputSystem extends EntityProcessingSystem {

    private final Provider<ShowMenuTrigger> showMenuTrigger;
    private final Provider<ShowLevelMenuTrigger> showLevelMenuTrigger;
    private final EntityIndexManager indexSystem;
    private final GameSystem gameSystem;
    private final ActionActivatedDetector mouseClick;
    private final MoveStory moveStory;
    
    private ComponentMapper<Sprite> spriteMapper;

    @Inject
    public IngameInputSystem(Provider<ShowMenuTrigger> showMenuTrigger, Provider<ShowLevelMenuTrigger> showLevelMenuTrigger, EntityIndexManager indexSystem, MoveStory moveStory, GameSystem gameSystem, IDefaultControls defaultControls) {
        super(Aspect.getAspectForAll(IngameControls.class));
        this.showLevelMenuTrigger = showLevelMenuTrigger;
        this.showMenuTrigger = showMenuTrigger;
        this.indexSystem = indexSystem;
        this.gameSystem = gameSystem;
        this.moveStory = moveStory;
        this.mouseClick = new ActionActivatedDetector(new Action("click", defaultControls.getMouseClickControls()));
    }

    @Override
    protected void initialize() {
        spriteMapper = world.getMapper(Sprite.class);
    }

    @Override
    protected void process(Entity e) {
        if (e.isEnabled()) {
            NedGame game = (NedGame) world;

            IngameControls controls = e.getComponent(IngameControls.class);
            controls.getShowMenu().poll();
            if (controls.getShowMenu().isActivated()) {
                world.addEntity(world.createEntity().addComponent(new Triggerable(showMenuTrigger.get())));
            }
            if (canMoveNed()) {
                controls.getUp().poll();
                controls.getDown().poll();
                controls.getRight().poll();
                controls.getLeft().poll();
                controls.getRewind().poll();
                mouseClick.poll();
                boolean upPressed = controls.getUp().isPressed();
                boolean downPressed = controls.getDown().isPressed();
                boolean leftPressed = controls.getLeft().isPressed();
                boolean rightPressed = controls.getRight().isPressed();
                boolean rewindPressed = controls.getRewind().isPressed();
                Entity ned = game.getNed();
                if (mouseClick.isPressed()) {
                    final Sprite selectedSprite = game.getSystem(MouseArrowSystem.class).getSelectedSprite();
                    if (null != selectedSprite) {
                        Vector3 selectedPosition = selectedSprite.getPosition();

                        Vector3 nedPosition = spriteMapper.get(ned).getPosition();
                        int nedX = Math.round(nedPosition.x);
                        int nedY = Math.round(nedPosition.y);
                        int selectedX = Math.round(selectedPosition.x);
                        int selectedY = Math.round(selectedPosition.y);

                        if (nedX == selectedX) {
                            if (nedY < selectedY) {
                                rightPressed = true;
                            } else if (nedY > selectedY) {
                                leftPressed = true;
                            }
                        } else if (nedY == selectedY) {
                            if (nedX < selectedX) {
                                downPressed = true;
                            } else if (nedX > selectedX) {
                                upPressed = true;
                            }
                        }
                    }
                }
                if (upPressed) {
                    moveStory.addMouvement(gameSystem.moveEntity(ned, new Position(0, -1), 0, false));
                    ned.changedInWorld();
                } else if (downPressed) {
                    moveStory.addMouvement(gameSystem.moveEntity(ned, new Position(0, 1), 0, false));
                    ned.changedInWorld();
                } else if (leftPressed) {
                    moveStory.addMouvement(gameSystem.moveEntity(ned, new Position(-1, 0), 0, false));
                    ned.changedInWorld();
                } else if (rightPressed) {
                    moveStory.addMouvement(gameSystem.moveEntity(ned, new Position(1, 0), 0, false));
                    ned.changedInWorld();
                } else if (rewindPressed) {
                    gameSystem.removeMouv();
                    ned.changedInWorld();
                }
            }
        }
    }

    private boolean canMoveNed() {
        NedGame game = (NedGame) world;
        return null != game.getNed() && world.getSystem(SpritePuppetControlSystem.class).getActives().isEmpty();
    }
}
