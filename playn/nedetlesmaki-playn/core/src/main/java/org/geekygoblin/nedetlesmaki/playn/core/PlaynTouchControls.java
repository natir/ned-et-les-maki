/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.playn.core;

import playn.core.Touch;
import playn.core.Touch.Listener;

import org.geekygoblin.nedetlesmaki.playn.core.TouchMove;

/*
 *
 * @author pierre
 */
public class PlaynTouchControls implements Listener {

    public boolean move[] = new boolean[TouchMove.values().length];
    private double lastStart;
    private String name; 
    
    @Override
    public void onTouchStart(Touch.Event[] events) {
        if (events.length > 0) {
            if (events[0].time() - events[events.length - 1].time() < 1000) {
                move[TouchMove.REWIND.ordinal()] = true;
                this.name = "Rewind";
            }
        } else if (events.length == 0) {
            if (events[0].time() - this.lastStart < 1000) {
                move[TouchMove.REWIND.ordinal()] = true;
                this.name = "Rewind";
            }

            this.lastStart = events[0].time();
        }
    }

    @Override
    public void onTouchMove(Touch.Event[] events) {
        if (events.length > 1) {
            Touch.Event begin = events[0];
            Touch.Event end = events[events.length - 1];

            if (begin.x() - end.x() > 0) {
                if (begin.y() - end.y() > 0) {
                    this.move[TouchMove.LEFT.ordinal()] = true;
                    this.name = "Left";
                } else {
                    this.move[TouchMove.UP.ordinal()] = true;
                    this.name = "Up";
                }
            } else {
                if (begin.y() - end.y() > 0) {
                    this.move[TouchMove.DOWN.ordinal()] = true;
                    this.name = "Down";
                } else {
                    this.move[TouchMove.RIGHT.ordinal()] = true;
                    this.name = "Right";
                }
            }
        }
    }

    @Override
    public void onTouchEnd(Touch.Event[] events) {
        resetValue();
    }

    @Override
    public void onTouchCancel(Touch.Event[] events) {
        resetValue();
    }

    public String getName() {
        return this.name;
    }
    
    private void resetValue() {
        for (boolean tmp : this.move) {
            tmp = false;
        }
    }
}
