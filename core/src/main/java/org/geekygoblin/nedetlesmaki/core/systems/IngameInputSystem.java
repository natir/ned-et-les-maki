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
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Ned;
import org.geekygoblin.nedetlesmaki.core.events.ShowMenuTrigger;
import org.geekygoblin.nedetlesmaki.core.components.IngameControls;
import org.geekygoblin.nedetlesmaki.core.backend.Position;
import org.geekygoblin.nedetlesmaki.core.components.ui.InGameUI;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Path;
import org.geekygoblin.nedetlesmaki.core.events.IStartGameTrigger;
import org.geekygoblin.nedetlesmaki.core.events.ShowLevelMenuTrigger;

import pythagoras.f.Vector;
import pythagoras.f.Vector3;

/**
 *
 * @author devnewton
 */
@Singleton
public class IngameInputSystem extends EntityProcessingSystem {

    private final Provider<ShowMenuTrigger> showMenuTrigger;
    private final Provider<ShowLevelMenuTrigger> showLevelMenuTrigger;
    private final Provider<IStartGameTrigger> startGameTrigger;
    private final ActionActivatedDetector mouseClick;

    private ComponentMapper<Sprite> spriteMapper;
    private ComponentMapper<Ned> nedMapper;
    private final InGameUI inGameUI;

    private Path nedPath;

    @Inject
    public IngameInputSystem(Provider<ShowMenuTrigger> showMenuTrigger, Provider<ShowLevelMenuTrigger> showLevelMenuTrigger, Provider<IStartGameTrigger> startGameTrigger, IDefaultControls defaultControls, InGameUI inGameUI) {
        super(Aspect.getAspectForAll(IngameControls.class));
        this.showMenuTrigger = showMenuTrigger;
        this.showLevelMenuTrigger = showLevelMenuTrigger;
        this.mouseClick = new ActionActivatedDetector(new Action("click", defaultControls.getMouseClickControls()));
        this.inGameUI = inGameUI;
        this.startGameTrigger = startGameTrigger;
    }

    @Override
    protected void initialize() {
        spriteMapper = world.getMapper(Sprite.class);
        nedMapper = world.getMapper(Ned.class);
    }

    @Override
    protected void process(Entity e) {
        if (e.isEnabled()) {
            NedGame game = (NedGame) world;

            IngameControls controls = e.getComponent(IngameControls.class);
            controls.getShowMenu().poll();
            if (controls.getShowMenu().isActivated() || inGameUI.getShowMenu().pollActivation()) {
                world.addEntity(world.createEntity().addComponent(new Triggerable(showLevelMenuTrigger.get())));
            }
            if (canMoveNed()) {
                Entity ned = game.getNed();

                if (this.nedMapper.getSafe(ned).isEnd()) {
                    world.addEntity(world.createEntity().addComponent(new Triggerable(showLevelMenuTrigger.get())));
                }

                if (controls.getRewind().isPressed() || inGameUI.getRewind().pollActivation()) {
                    this.nedMapper.getSafe(ned).undo();
                    ned.changedInWorld();

                    // disable pathfinding when rewind is used
                    nedPath = null;
                } else if (inGameUI.getReset().pollActivation()) {
                    game.addEntity(world.createEntity().addComponent(new Triggerable(startGameTrigger.get().withLevelName(game.getCurrentLevel()))));
                } else {
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

                    // the user has made some action, stop following the current path
                    if (mouseClick.isPressed() || upPressed || downPressed || leftPressed || rightPressed) {
                        nedPath = null;
                    }

                    // search a path on click
                    if (!inGameUI.isMouseHoverHoverAButton() && mouseClick.isPressed()) {
                        final Sprite selectedSprite = game.getSystem(MouseArrowSystem.class).getSelectedSprite();
                        game.getSystem(MouseArrowSystem.class).setTarget(selectedSprite);
                        if (null != selectedSprite) {
                            Vector3 selectedPosition = selectedSprite.getPosition();
                            Vector3 nedPosition = spriteMapper.get(ned).getPosition();

                            nedPath = new AStar(this.nedMapper.getSafe(ned).getIndex(), new Vector(nedPosition.x, nedPosition.y), new Vector(selectedPosition.x, selectedPosition.y)).getPath();
                        }
                    }

                    // make Ned follow the current path
                    if (nedPath == null || nedPath.isEmpty()) {
                        nedPath = null;
                        game.getSystem(MouseArrowSystem.class).setTarget(null);
                    } else {
                        Vector3 nedPosition = spriteMapper.get(ned).getPosition();
                        Vector nextPosition = nedPath.popStep();
                        if (nedPath.isEmpty()) {
                            ned.removeComponent(Path.class);
                            ned.changedInWorld();
                        }

                        int nedX = Math.round(nedPosition.x);
                        int nedY = Math.round(nedPosition.y);
                        int nextX = Math.round(nextPosition.x);
                        int nextY = Math.round(nextPosition.y);

                        if (nedX == nextX) {
                            if (nedY < nextY) {
                                rightPressed = true;
                            } else if (nedY > nextY) {
                                leftPressed = true;
                            }
                        } else if (nedY == nextY) {
                            if (nedX < nextX) {
                                downPressed = true;
                            } else if (nedX > nextX) {
                                upPressed = true;
                            }
                        }
                    }

                    if (upPressed) {
                        this.nedMapper.getSafe(ned).moveTo(Position.getUp(), 0.0f);
                        ned.changedInWorld();
                    } else if (downPressed) {
                        this.nedMapper.getSafe(ned).moveTo(Position.getDown(), 0.0f);
                        ned.changedInWorld();
                    } else if (leftPressed) {
                        this.nedMapper.getSafe(ned).moveTo(Position.getLeft(), 0.0f);
                        ned.changedInWorld();
                    } else if (rightPressed) {
                        this.nedMapper.getSafe(ned).moveTo(Position.getRight(), 0.0f);
                        ned.changedInWorld();
                    }
                }
            }
        }
    }

    private boolean canMoveNed() {
        NedGame game = (NedGame) world;
        return null != game.getNed() && world.getSystem(SpritePuppetControlSystem.class).getActives().isEmpty();
    }
}
