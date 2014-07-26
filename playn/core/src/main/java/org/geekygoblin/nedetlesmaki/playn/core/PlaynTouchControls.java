/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.playn.core;

import playn.core.Key;
import playn.core.PlayN;
import playn.core.Touch;
import playn.core.Touch.Listener;

/*
 *
 * @author pierre
 */
public class PlaynTouchControls implements Listener {

    public boolean move[] = new boolean[Key.values().length];
    private Touch.Event begin;
    private Key last;

    public PlaynTouchControls() {
        PlayN.touch().setListener(this);

        this.last = Key.UNKNOWN;
    }

    @Override
    public void onTouchStart(Touch.Event[] events) {
        if (events.length == 1) {
            this.begin = events[0];
        }
    }

    @Override
    public void onTouchMove(Touch.Event[] events) {
        if (events.length == 1) {

            Touch.Event end = events[0];
            float diffX = this.begin.x() - end.x();
            float diffY = this.begin.y() - end.y();

            if (diffX > 0) {
                if (diffY > 0) {
                    this.setupMove(Key.LEFT);
                } else {
                    this.setupMove(Key.DOWN);
                }
            } else {
                if (diffY > 0) {
                    this.setupMove(Key.UP);
                } else {
                    this.setupMove(Key.RIGHT);
                }
            }
        }
    }

    @Override
    public void onTouchEnd(Touch.Event[] events) {
        this.move[this.last.ordinal()] = false;
    }

    @Override
    public void onTouchCancel(Touch.Event[] events) {

    }

    public String getName() {
        return this.last.toString();
    }

    private void setupMove(Key next) {
        if (this.last != next) {
            this.move[this.last.ordinal()] = false;
        }

        this.move[next.ordinal()] = true;
        this.last = next;
    }
}
