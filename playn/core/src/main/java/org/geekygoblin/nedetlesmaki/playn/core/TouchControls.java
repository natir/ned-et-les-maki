/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.playn.core;

import playn.core.Key;

import im.bci.jnuit.controls.Control;

/**
 *
 * @author pierre
 */
public class TouchControls implements Control {

    private Key move;
    private PlaynTouchControls controls; 
    
    public TouchControls(PlaynTouchControls controls, Key move) {
        this.move = move;
        this.controls = controls;
    }

    @Override
    public String getControllerName() {
        return "Touch";
    }

    @Override
    public String getName() {
       return move.toString();
    }

    @Override
    public float getDeadZone() {
        return 0.0f;
    }

    @Override
    public float getValue() {
        return this.controls.move[move.ordinal()] ? 1.0f : 0.0f;
    }
}
