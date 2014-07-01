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
package org.geekygoblin.nedetlesmaki.core.systems;

import com.artemis.Entity;
import com.artemis.utils.ImmutableBag;
import im.bci.jnuit.artemis.sprite.Sprite;
import java.util.ArrayList;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Color;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Plate;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.PositionIndexed;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Pusher;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Square;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Stairs;
import org.geekygoblin.nedetlesmaki.core.constants.AnimationType;
import org.geekygoblin.nedetlesmaki.core.constants.ColorType;
import org.geekygoblin.nedetlesmaki.core.manager.EntityIndexManager;
import org.geekygoblin.nedetlesmaki.core.utils.Mouvement;
import org.geekygoblin.nedetlesmaki.core.utils.MoveStory;
import org.geekygoblin.nedetlesmaki.core.utils.PosOperation;

/**
 *
 * @author natir
 */
@Singleton
public class GameSystem {

    private final EntityIndexManager index;
    private final MoveStory move;
    public boolean end;

    @Inject
    public GameSystem(EntityIndexManager index, MoveStory move) {
        this.index = index;
        this.end = false;
        this.move = move;
    }

    public ArrayList<Mouvement> moveEntity(Entity e, Position dirP, float baseBefore, boolean nedPush) {
        Entity nedEntity = this.index.getNed();

        Position oldP = this.index.getPositionIndexed(e);

        ArrayList<Mouvement> mouv = new ArrayList();

        float destroyBefore = baseBefore;

        for (int i = 0; i != this.index.getMovable(e); i++) {
            float animTime = this.calculateAnimationTime(0.4f, i);
            Position newP = PosOperation.sum(oldP, dirP);

            if (i > this.index.getBoost(e) - 1) {
                e.getComponent(Pusher.class).setPusher(true);
            }

            if (this.index.positionIsVoid(newP)) {
                Square s = index.getSquare(newP.getX(), newP.getY());
                if (this.testStopOnPlate(e, s)) {
                    mouv.addAll(this.runValideMove(dirP, e, false, baseBefore, animTime, i, this.index.isBoosted(e), nedPush));
                    if (this.index.getBoost(e) != 20) {
                        e.getComponent(Pusher.class).setPusher(false);
                    }

                    return mouv;
                }
                if (!this.testBlockedPlate(e, s)) {
                    mouv.addAll(runValideMove(dirP, e, false, baseBefore, animTime, i, this.index.isBoosted(e), nedPush));
                    baseBefore = 0;
                }
            } else {
                if (this.index.isStairs(newP) && e.equals(nedEntity)) {
                    Stairs s = index.getStairs(this.index.getEntity(newP.getX(), newP.getY()));
                    if (dirP.getX() == 0 && dirP.getY() > 0 && s.getDir() == 1) {
                        mouv.addAll(nedMoveOnStairs(dirP, e, animTime));
                    } else if (dirP.getX() == 0 && dirP.getY() < 0 && s.getDir() == 2) {
                        mouv.addAll(nedMoveOnStairs(dirP, e, animTime));
                    } else if (dirP.getX() > 0 && dirP.getY() == 0 && s.getDir() == 3) {
                        mouv.addAll(nedMoveOnStairs(dirP, e, animTime));
                    } else if (dirP.getX() < 0 && dirP.getY() == 0 && s.getDir() == 4) {
                        mouv.addAll(nedMoveOnStairs(dirP, e, animTime));
                    }

                    if (!mouv.isEmpty()) {
                        this.end = true;
                    }
                }
                if (this.index.isPusherEntity(e)) {
                    Entity nextE = index.getSquare(newP.getX(), newP.getY()).getEntity();
                    if (this.index.isPushableEntity(nextE)) {
                        if (this.index.isDestroyer(e)) {
                            if (this.index.isDestroyable(nextE)) {
                                mouv.addAll(destroyMove(nextE, dirP, destroyBefore + this.beforeTime(0.4f, i), animTime));
                                mouv.addAll(runValideMove(dirP, e, true, 0.0f, animTime, i, this.index.isBoosted(e), nedPush));
                            } else {
                                ArrayList<Mouvement> recMouv = this.moveEntity(nextE, dirP, baseBefore + this.beforeTime(0.4f, i), e == nedEntity);
                                if (!recMouv.isEmpty()) {
                                    mouv.addAll(recMouv);
                                    mouv.addAll(runValideMove(dirP, e, true, 0.0f, animTime, i, this.index.isBoosted(e), nedPush));
                                }
                            }
                        } else {
                            ArrayList<Mouvement> recMouv = this.moveEntity(nextE, dirP, baseBefore + this.beforeTime(0.4f, i), e == nedEntity);
                            if (!recMouv.isEmpty()) {
                                mouv.addAll(recMouv);
                                if (!this.index.isCatchNed(nextE)) {
                                    mouv.addAll(runValideMove(dirP, e, true, baseBefore, animTime, i, this.index.isBoosted(e), nedPush));
                                    if (recMouv.size() > 2) {
                                        mouv.add(new Mouvement(nedEntity).setAnimation(this.nedWaitBoostChoice(dirP)).saveMouvement());
                                    }
                                }
                            }
                        }
                    }
                }

                if (this.index.isBoosted(e)) {
                    if (mouv.get(mouv.size() - 1).getAnimation(0) != AnimationType.maki_blue_one && mouv.get(mouv.size() - 2).getAnimation(0) != AnimationType.maki_blue_one) {
                         mouv.add(new Mouvement(e).setAnimation(this.getMakiAnimation(true, -1, dirP, ColorType.blue)).saveMouvement());   
                    }
                }

                if (this.index.nedIsCatched(e) && nedPush) {
                    mouv.add(new Mouvement(nedEntity).setAnimation(this.getFlyAnimation(-1, dirP)).saveMouvement());
                }

                if (this.index.getBoost(e) != 20) {
                    e.getComponent(Pusher.class).setPusher(false);
                }

                if (mouv.size() == 3 && this.isBeginFly(mouv.get(1).getAnimation(0)) && this.isEndFly(mouv.get(2).getAnimation(0))) {
                    mouv.remove(2);
                    mouv.remove(1);
                    mouv.add(new Mouvement(nedEntity).setPosition(dirP).setAnimation(this.getNedAnimation(dirP, 0, true, false)).setAnimationTime(this.calculateAnimationTime(0.4f, 0)).saveMouvement());
                }

                if (mouv.size() == 1 && this.isEndFly(mouv.get(0).getAnimation(0))) {
                    mouv.remove(0);
                }

                return mouv;
            }
        }

        if (this.index.isBoosted(e)) {
            mouv.add(new Mouvement(e).setAnimation(this.getMakiAnimation(true, -1, dirP, ColorType.blue)).saveMouvement());
        }

        if (this.index.nedIsCatched(e)) {
            mouv.add(new Mouvement(this.index.getNed()).setAnimation(this.getFlyAnimation(-1, dirP)).setAnimationTime(this.calculateAnimationTime(baseBefore, 2)).saveMouvement());
        }

        if (mouv.size() == 3 && this.isBeginFly(mouv.get(1).getAnimation(0)) && this.isEndFly(mouv.get(2).getAnimation(0))) {
            mouv.remove(2);
            mouv.remove(1);
            mouv.add(new Mouvement(nedEntity).setPosition(dirP).setAnimation(this.getNedAnimation(dirP, 0, true, false)).setAnimationTime(this.calculateAnimationTime(0.4f, 0)).saveMouvement());
        }

        if (mouv.size() == 1 && this.isEndFly(mouv.get(0).getAnimation(0))) {
            mouv.remove(0);
        }

        return mouv;
    }

    private boolean isBeginFly(AnimationType a) {
        return a.equals(AnimationType.fly_start_down) || a.equals(AnimationType.fly_start_left) || a.equals(AnimationType.fly_start_right) || a.equals(AnimationType.fly_start_up);
    }

    private boolean isEndFly(AnimationType a) {
        return a.equals(AnimationType.fly_stop_down) || a.equals(AnimationType.fly_stop_left) || a.equals(AnimationType.fly_stop_right) || a.equals(AnimationType.fly_stop_up);
    }

    private ArrayList<Mouvement> runValideMove(Position diff, Entity e, boolean push, float bw, float aT, int pas, boolean boosted, boolean pusherIsNed) {
        Position oldP = new Position(this.index.getPositionIndexed(e).getX(), this.index.getPositionIndexed(e).getY());
        Position newP = PosOperation.sum(oldP, diff);

        ArrayList<Mouvement> m = new ArrayList();

        if (this.index.getEntity(newP.getX(), newP.getY()) == null) {
            index.getPositionIndexed(e).setPosition(newP.getX(), newP.getY());
            if (e == this.index.getNed()) {
                m.add(new Mouvement(e).setPosition(diff).setAnimation(this.getNedAnimation(diff, pas, push, boosted)).setBeforeWait(bw).setAnimationTime(aT).saveMouvement());
            } else {
                if (makiMoveWithPlate(newP, e, true)) {
                    if (actualIsColorPlate(oldP, e)) {
                        this.index.getSquare(oldP.getX(), oldP.getY()).getPlate().getComponent(Plate.class).setMaki(false);
                        this.index.getSquare(newP.getX(), newP.getY()).getPlate().getComponent(Plate.class).setMaki(true);
                        m.addAll(makiPlateMove(oldP, newP, e, true, aT, bw, false));
                    } else {
                        m.addAll(makiPlateMove(oldP, newP, e, true, aT, bw, false));
                    }

                    m.addAll(this.tryPlate());
                } else if (makiMoveWithPlate(oldP, e, false)) {
                    m.addAll(makiPlateMove(oldP, newP, e, false, aT, bw, actualIsColorPlate(oldP, e)));

                    m.addAll(this.tryPlate());
                } else {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(this.getMakiAnimation(boosted, pas, diff, this.index.getColorType(e))).setBeforeWait(bw).setAnimationTime(aT).saveMouvement());
                }

                Entity ned = this.index.getNed();
                if (this.index.isCatchNed(e) && pusherIsNed) {
                    ned.getComponent(PositionIndexed.class).setPosition(newP.getX() - diff.getX(), newP.getY() - diff.getY());
                    if (pusherIsNed) {
                        m.add(new Mouvement(ned).setPosition(diff).setAnimation(this.getFlyAnimation(pas, diff)).setBeforeWait(bw).setAnimationTime(aT).saveMouvement());

                        this.index.getCatchNed(e).nedCatched(true);
                    }
                }
            }

            return m;
        }

        return m;
    }

    private AnimationType getNedAnimation(Position diff, int pas, boolean push, boolean boosted) {
        if (diff.getX() > 0) {
            if (push) {
                return AnimationType.ned_push_right;
            } else {
                return AnimationType.ned_right;
            }
        } else if (diff.getX() < 0) {
            if (push) {
                return AnimationType.ned_push_left;
            } else {
                return AnimationType.ned_left;
            }
        } else if (diff.getY() > 0) {
            if (push) {
                return AnimationType.ned_push_down;
            } else {
                return AnimationType.ned_down;
            }
        } else if (diff.getY() < 0) {
            if (push) {
                return AnimationType.ned_push_up;
            } else {
                return AnimationType.ned_up;
            }
        } else {
            return this.getMakiAnimation(boosted, pas, diff, ColorType.no);
        }
    }

    private ArrayList<Mouvement> nedMoveOnStairs(Position diff, Entity e, float aT) {

        ArrayList<Mouvement> m = new ArrayList();

        if (this.index.getStairs(this.index.getAllStairs().get(0)).isOpen()) {
            if (e == this.index.getNed()) {
                if (diff.getX() > 0) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.ned_mount_stairs_right).setAnimationTime(aT).saveMouvement());
                } else if (diff.getX() < 0) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.ned_mount_stairs_left).setAnimationTime(aT).saveMouvement());
                } else if (diff.getY() > 0) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.ned_mount_stairs_down).setAnimationTime(aT).saveMouvement());
                } else if (diff.getY() < 0) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.ned_mount_stairs_up).setAnimationTime(aT).saveMouvement());
                } else {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.no).setAnimationTime(aT).saveMouvement());
                }
            } else {
                if (m.isEmpty()) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.no).setAnimationTime(aT).saveMouvement());

                }
            }
        }
        return m;
    }

    private ArrayList<Mouvement> makiPlateMove(Position oldP, Position newP, Entity e, boolean getOne, float aT, float bT, boolean actualIsPlate) {
        ArrayList<Mouvement> m = new ArrayList();
        Position diff = PosOperation.deduction(newP, oldP);

        Square obj;

        if (getOne) {
            obj = index.getSquare(newP.getX(), newP.getY());
        } else {
            obj = index.getSquare(oldP.getX(), oldP.getY());
        }

        if (obj == null) {
            return m;
        }

        Entity plate = obj.getPlate();

        if (plate == null) {
            return m;
        }

        Color plateC = this.index.getColor(plate);
        Color makiC = this.index.getColor(e);

        if (plateC.getColor() == makiC.getColor()) {
            if (plateC.getColor() == ColorType.green) {
                if (getOne && !actualIsPlate) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.maki_green_one).setAnimationTime(aT).setBeforeWait(bT).saveMouvement());
                } else if (!getOne && actualIsPlate) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.maki_green_out).setAnimationTime(aT).setBeforeWait(bT).saveMouvement());
                } else {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.no).setAnimationTime(aT).setBeforeWait(bT).saveMouvement());
                }
            } else if (plateC.getColor() == ColorType.orange) {
                if (getOne && !actualIsPlate) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.maki_orange_one).setAnimationTime(aT).setBeforeWait(bT).saveMouvement());
                } else if (!getOne && actualIsPlate) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.maki_orange_out).setAnimationTime(aT).setBeforeWait(bT).saveMouvement());
                } else {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.no).setAnimationTime(aT).setBeforeWait(bT).saveMouvement());
                }
            } else if (plateC.getColor() == ColorType.blue) {
                if (getOne && !actualIsPlate) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.maki_blue_one).setAnimationTime(aT).setBeforeWait(bT).saveMouvement());
                } else if (!getOne && actualIsPlate) {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.maki_blue_out).setAnimationTime(aT).setBeforeWait(bT).saveMouvement());
                } else {
                    m.add(new Mouvement(e).setPosition(diff).setAnimation(AnimationType.no).setAnimationTime(aT).setBeforeWait(bT).saveMouvement());
                }
            }
        }

        return m;
    }

    public ArrayList<Mouvement> destroyMove(Entity e, Position diff, float bT, float aT) {
        ArrayList<Mouvement> preM = this.moveEntity(e, diff, bT, false);

        if (preM.isEmpty()) {
            preM.add(new Mouvement(e).setPosition(new Position(0, 0)).setAnimation(AnimationType.box_boom).setBeforeWait(bT).setAnimationTime(aT).saveMouvement());
        } else {
            preM.add(new Mouvement(e).setPosition(new Position(0, 0)).setAnimation(AnimationType.box_destroy).setBeforeWait(bT).setAnimationTime(aT).saveMouvement());
        }
        this.index.disabled(e);
        return preM;
    }

    private AnimationType getMakiAnimation(boolean boosted, int pas, Position diff, ColorType makiColor) {

        if (boosted) {
            if (diff.getX() > 0) {
                if (pas < 0) {
                    return AnimationType.boost_stop_right;
                } else if (pas == 3) {
                    return AnimationType.boost_start_right;
                } else if (pas > 3) {
                    return AnimationType.boost_loop_right;
                }
            } else if (diff.getX() < 0) {
                if (pas < 0) {
                    return AnimationType.boost_stop_left;
                } else if (pas == 3) {
                    return AnimationType.boost_start_left;
                } else if (pas > 3) {
                    return AnimationType.boost_loop_left;
                }
            } else if (diff.getY() > 0) {
                if (pas < 0) {
                    return AnimationType.boost_stop_down;
                } else if (pas == 3) {
                    return AnimationType.boost_start_down;
                } else if (pas > 3) {
                    return AnimationType.boost_loop_down;
                }
            } else if (diff.getY() < 0) {
                if (pas < 0) {
                    return AnimationType.boost_stop_up;
                } else if (pas == 3) {
                    return AnimationType.boost_start_up;
                } else if (pas > 3) {
                    return AnimationType.boost_loop_up;
                }
            }
        }

        if (makiColor == ColorType.orange) {
            return AnimationType.maki_orange_no;
        } else {
            return AnimationType.no;
        }
    }

    private AnimationType getFlyAnimation(int pas, Position diff) {

        if (diff.getX() > 0) {
            if (pas < 0) {
                return AnimationType.fly_stop_right;
            } else if (pas == 0) {
                return AnimationType.fly_start_right;
            } else if (pas > 0) {
                return AnimationType.fly_loop_right;
            }
        } else if (diff.getX() < 0) {
            if (pas < 0) {
                return AnimationType.fly_stop_left;
            } else if (pas == 0) {
                return AnimationType.fly_start_left;
            } else if (pas > 0) {
                return AnimationType.fly_loop_left;
            }
        } else if (diff.getY() > 0) {
            if (pas < 0) {
                return AnimationType.fly_stop_down;
            } else if (pas == 0) {
                return AnimationType.fly_start_down;
            } else if (pas > 0) {
                return AnimationType.fly_loop_down;
            }
        } else if (diff.getY() < 0) {
            if (pas < 0) {
                return AnimationType.fly_stop_up;
            } else if (pas == 0) {
                return AnimationType.fly_start_up;
            } else if (pas > 0) {
                return AnimationType.fly_loop_up;
            }
        }

        return AnimationType.no;
    }

    public boolean makiMoveWithPlate(Position newP, Entity e, boolean value) {
        Square s = this.index.getSquare(newP.getX(), newP.getY());

        if (s == null) {
            return false;
        }

        Entity plate = s.getPlate();

        if (plate == null) {
            return false;
        }

        if (this.index.getColor(e) == null) {
            return false;
        }

        if (this.index.getColorType(e) == this.index.getColorType(plate)) {
            plate.getComponent(Plate.class).setMaki(value);
            return true;
        }

        return false;
    }

    private boolean actualIsColorPlate(Position nextnextP, Entity maki) {
        Square s = this.index.getSquare(nextnextP.getX(), nextnextP.getY());

        if (s == null) {
            return false;
        }

        Entity plate = s.getPlate();
        if (plate == null) {
            return false;
        }

        if (this.index.getColor(maki) == null) {
            return false;
        }

        if (this.index.getColorType(maki) == this.index.getColorType(plate)) {
            return true;
        }

        return false;
    }

    private boolean testStopOnPlate(Entity eMove, Square obj) {
        if (obj == null) {
            return false;

        }

        Entity plate = obj.getPlate();

        if (plate == null) {
            return false;
        }

        Plate p = this.index.getPlate(plate);
        boolean block = this.index.stopOnPlate(eMove);

        if (!block) {
            return false;
        }

        if (p.isPlate()) {
            if (block) {
                if (this.index.getColorType(plate) == this.index.getColorType(eMove) && this.index.getColorType(eMove) != ColorType.orange) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean testBlockedPlate(Entity eMove, Square obj) {
        if (obj == null) {
            return false;

        }

        Entity plate = obj.getPlate();

        if (plate == null) {
            return false;
        }

        Plate p = plate.getComponent(Plate.class);
        boolean block = this.index.isBlockOnPlate(eMove);

        if (!block) {
            return false;
        }

        if (p.isPlate()) {
            if (block) {
                return true;
            }
        }

        return false;
    }

    private ArrayList<Mouvement> tryPlate() {
        ImmutableBag<Entity> plateGroup = this.index.getAllPlate();

        ImmutableBag<Entity> stairsGroup = this.index.getAllStairs();
        Entity stairs = stairsGroup.get(0);
        Stairs stairsS = this.index.getStairs(stairs);
        boolean baseIs = stairsS.isOpen();
        boolean plateIsUnvalide = true;

        for (int i = 0; i != plateGroup.size(); i++) {
            Entity plateE = plateGroup.get(i);

            Plate plate = this.index.getPlate(plateE);

            if (!plate.haveMaki()) {
                plateIsUnvalide = false;
                return this.tryPlateReturn(baseIs, plateIsUnvalide, stairs, stairsS);
            }
        }

        return this.tryPlateReturn(baseIs, plateIsUnvalide, stairs, stairsS);
    }

    private ArrayList<Mouvement> tryPlateReturn(boolean baseEtat, boolean newEtat, Entity entity, Stairs stairs) {
        if (baseEtat == newEtat) {
            return new ArrayList<Mouvement>();
        } else if (newEtat) {
            stairs.setStairs(true);
            return stairsAnimation(entity, stairs, true);
        } else {
            stairs.setStairs(false);
            return stairsAnimation(entity, stairs, false);
        }
    }

    private ArrayList<Mouvement> stairsAnimation(Entity stairs, Stairs stairsS, boolean open) {
        ArrayList<Mouvement> tmpm = new ArrayList();

        switch (stairsS.getDir()) {
            case 1:
                if (open) {
                    tmpm.add(new Mouvement(stairs).setAnimation(AnimationType.stairs_open_up).setAnimationTime(0.4f).saveMouvement());
                } else {
                    tmpm.add(new Mouvement(stairs).setAnimation(AnimationType.stairs_close_up).setAnimationTime(0.4f).saveMouvement());
                }
                break;
            case 2:
                if (open) {
                    tmpm.add(new Mouvement(stairs).setAnimation(AnimationType.stairs_open_down).setAnimationTime(0.4f).saveMouvement());
                } else {
                    tmpm.add(new Mouvement(stairs).setAnimation(AnimationType.stairs_close_down).setAnimationTime(0.4f).saveMouvement());
                }
                break;
            case 3:
                if (open) {
                    tmpm.add(new Mouvement(stairs).setAnimation(AnimationType.stairs_open_left).setAnimationTime(0.4f).saveMouvement());
                } else {
                    tmpm.add(new Mouvement(stairs).setAnimation(AnimationType.stairs_close_left).setAnimationTime(0.4f).saveMouvement());
                }
                break;
            case 4:
                if (open) {
                    tmpm.add(new Mouvement(stairs).setAnimation(AnimationType.stairs_open_right).setAnimationTime(0.4f).saveMouvement());
                } else {
                    tmpm.add(new Mouvement(stairs).setAnimation(AnimationType.stairs_close_right).setAnimationTime(0.4f).saveMouvement());
                }
                break;
            default:
                if (open) {
                    tmpm.add(new Mouvement(stairs).setAnimation(AnimationType.stairs_open_up).setAnimationTime(0.4f).saveMouvement());
                } else {
                    tmpm.add(new Mouvement(stairs).setAnimation(AnimationType.stairs_close_up).setAnimationTime(0.4f).saveMouvement());
                }
        }

        return tmpm;
    }

    AnimationType nedWaitBoostChoice(Position diff) {

        if (diff.getX() > 0) {
            return AnimationType.ned_waits_boost_right;
        } else if (diff.getX() < 0) {
            return AnimationType.ned_waits_boost_left;
        } else if (diff.getY() > 0) {
            return AnimationType.ned_waits_boost_down;
        } else if (diff.getY() < 0) {
            return AnimationType.ned_waits_boost_up;
        }

        return AnimationType.no;
    }

    public boolean endOfLevel() {
        ImmutableBag<Entity> stairsGroup = this.index.getAllStairs();

        Entity ned = this.index.getNed();
        Sprite nedS = this.index.getSprite(ned);

        if (nedS.getPlay().getName().length() > 9) {
            if (nedS.getPlay().getName().substring(0, 9).equals("ned_mount") && nedS.getPlay().isStopped()) {
                return true;
            }
        }

        return false;
    }

    public void removeMouv() {
        boolean reCall = false;

        ArrayList<Mouvement> head = this.move.pop();

        if (head == null) {
            return;
        }

        Collections.reverse(head);

        ArrayList<Mouvement> rm = new ArrayList<Mouvement>();

        for (int i = 0; i != head.size(); i++) {
            for (int j = 0; j != head.get(i).size(); j++) {
                Position headP = head.get(i).getPosition(j);

                if (headP == null) {
                    return;
                }

                Position diff = PosOperation.deduction(new Position(0, 0), headP);
                Position current = this.index.getPositionIndexed(head.get(i).getEntity());

                if (current == null) {
                    return;
                }

                AnimationType invertAnim = this.invertAnimation(head.get(i).getAnimation(j));

                rm.add(new Mouvement(head.get(i).getEntity()).setAnimation(invertAnim).setPosition(diff).setAnimationTime(head.get(i).getAnimationTime(j)).saveMouvement());

                if (invertAnim == AnimationType.maki_blue_out || invertAnim == AnimationType.maki_orange_out || invertAnim == AnimationType.maki_green_out) {
                    this.index.getSquare(current.getX(), current.getY()).getPlate().getComponent(Plate.class).setMaki(false);
                }

                this.index.getEntity(current.getX(), current.getY()).getComponent(PositionIndexed.class).setPosition(current.getX() + diff.getX(), current.getY() + diff.getY());

                if (invertAnim == AnimationType.stairs_open_down || invertAnim == AnimationType.stairs_open_up || invertAnim == AnimationType.stairs_open_left || invertAnim == AnimationType.stairs_open_right) {
                    reCall = true;
                }
            }
        }

        Collections.reverse(rm);

        this.move.setRemove(rm);

        if (reCall) {
            this.removeMouv();
        }

        this.tryPlate();
    }

    private AnimationType invertAnimation(AnimationType base) {
        if (base == AnimationType.no) {
            return AnimationType.no;
        } else if (base == AnimationType.ned_right) {
            return AnimationType.ned_right;
        } else if (base == AnimationType.ned_left) {
            return AnimationType.ned_left;
        } else if (base == AnimationType.ned_down) {
            return AnimationType.ned_down;
        } else if (base == AnimationType.ned_up) {
            return AnimationType.ned_up;
        } else if (base == AnimationType.ned_push_right) {
            return AnimationType.ned_push_right;
        } else if (base == AnimationType.ned_push_left) {
            return AnimationType.ned_push_left;
        } else if (base == AnimationType.ned_push_down) {
            return AnimationType.ned_push_down;
        } else if (base == AnimationType.ned_push_up) {
            return AnimationType.ned_push_up;
        } else if (base == AnimationType.box_destroy) {
            return AnimationType.box_create;
        } else if (base == AnimationType.box_create) {
            return AnimationType.box_destroy;
        } else if (base == AnimationType.box_boom) {
            return AnimationType.box_create;
        } else if (base == AnimationType.maki_green_one) {
            return AnimationType.maki_green_out;
        } else if (base == AnimationType.maki_orange_one) {
            return AnimationType.maki_orange_out;
        } else if (base == AnimationType.maki_blue_one) {
            return AnimationType.maki_blue_out;
        } else if (base == AnimationType.maki_green_out) {
            return AnimationType.maki_green_one;
        } else if (base == AnimationType.maki_orange_out) {
            return AnimationType.maki_orange_one;
        } else if (base == AnimationType.maki_blue_out) {
            return AnimationType.maki_blue_one;
        } else if (base == AnimationType.disable_entity) {
            return AnimationType.disable_entity;
        } else if (base == AnimationType.stairs_close_up) {
            return AnimationType.stairs_open_up;
        } else if (base == AnimationType.stairs_close_down) {
            return AnimationType.stairs_open_down;
        } else if (base == AnimationType.stairs_close_left) {
            return AnimationType.stairs_open_left;
        } else if (base == AnimationType.stairs_close_right) {
            return AnimationType.stairs_open_right;
        } else if (base == AnimationType.stairs_open_up) {
            return AnimationType.stairs_close_up;
        } else if (base == AnimationType.stairs_open_down) {
            return AnimationType.stairs_close_down;
        } else if (base == AnimationType.stairs_open_left) {
            return AnimationType.stairs_close_left;
        } else if (base == AnimationType.stairs_open_right) {
            return AnimationType.stairs_close_right;
        } else if (base == AnimationType.boost_loop_up) {
            return AnimationType.no;
        } else if (base == AnimationType.boost_loop_down) {
            return AnimationType.no;
        } else if (base == AnimationType.boost_loop_left) {
            return AnimationType.no;
        } else if (base == AnimationType.boost_loop_right) {
            return AnimationType.no;
        } else if (base == AnimationType.boost_start_up) {
            return AnimationType.no;
        } else if (base == AnimationType.boost_start_down) {
            return AnimationType.no;
        } else if (base == AnimationType.boost_start_left) {
            return AnimationType.no;
        } else if (base == AnimationType.boost_start_right) {
            return AnimationType.no;
        } else if (base == AnimationType.boost_stop_up) {
            return AnimationType.no;
        } else if (base == AnimationType.boost_stop_down) {
            return AnimationType.no;
        } else if (base == AnimationType.boost_stop_left) {
            return AnimationType.no;
        }

        return base;
    }

    float calculateAnimationTime(float base, int mul) {
        if (mul > 0) {
            return base / 3f;
        } else {
            return base;
        }
    }

    float beforeTime(float base, int mul) {
        float ret = 0;

        for (int i = 0; i != mul; i++) {
            ret += this.calculateAnimationTime(base, i);
        }

        return ret;
    }

    public ArrayList<Mouvement> findPath(Position next, Entity e) {
        int dir[] = new int[4];
        Position actual = this.index.getPosition(e);
        Position diff = PosOperation.deduction(next, actual);
        ArrayList<Mouvement> ret = new ArrayList<Mouvement>();

        //If diff is juste one make nothing
        if ((diff.getX() == -1 && diff.getY() == 0) || (diff.getX() == 1 && diff.getY() == 0) || (diff.getX() == 0 && diff.getY() == -1) || (diff.getX() == 0 && diff.getY() == 1)) {
            this.moveEntity(e, diff, 0, false);
        } else {
            for (actual = this.index.getPosition(e); actual != null && PosOperation.equale(actual, next); actual = this.index.getPosition(e)) {
                dir[0] = this.weightOfPos(PosOperation.sum(actual, new Position(-1, 0)), diff);
                dir[1] = this.weightOfPos(PosOperation.sum(actual, new Position(1, 0)), diff);
                dir[2] = this.weightOfPos(PosOperation.sum(actual, new Position(0, -1)), diff);
                dir[3] = this.weightOfPos(PosOperation.sum(actual, new Position(0, 1)), diff);

                int min = indexMin(dir);

                if (min > 100) {
                    ret.addAll(this.moveEntity(e, diff, 0, false));
                    return ret;
                } else {
                    ret.addAll(this.moveEntity(e, diff, 0, false));
                }
            }
        }

        return ret;
    }

    private int weightOfPos(Position p, Position diff) {
        if (this.index.getEntity(p.getX(), p.getY()) == null) {
            return diff.getX() + diff.getY();
        } else {
            return diff.getX() + diff.getY() + 100; // just strong value
        }
    }

    private int indexMin(int[] tab) {
        int begin, end;

        begin = tab[0] < tab[1] ? 0 : 1;
        end = tab[2] < tab[3] ? 2 : 3;

        return tab[begin] < tab[end] ? begin : end;
    }
}
