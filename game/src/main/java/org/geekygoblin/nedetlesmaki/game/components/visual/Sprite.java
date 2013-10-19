/*
 * Copyright (c) 2013 devnewton <devnewton@bci.im>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'devnewton <devnewton@bci.im>' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.geekygoblin.nedetlesmaki.game.components.visual;

import java.util.ArrayList;

import com.artemis.Component;

import im.bci.nanim.IPlay;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author devnewton
 */
public final class Sprite extends Component {

    private IPlay play;
    private Vector3f position;
    private float width, height;
    private float scale = 1.0f, rotate = 0.0f;
    private boolean mirrorX, mirrorY;
    private Color color = (Color) Color.WHITE;
    private ArrayList<SpriteUpdater> updaters = new ArrayList<>();
    
    public void addUpdater(SpriteUpdater u) {
    	updaters.add(u);    	
    }
    
    public ArrayList<SpriteUpdater> getUpdaters() {
		return updaters;
	}

    public boolean isMirrorX() {
        return mirrorX;
    }

    public void setMirrorX(boolean mirrorX) {
        this.mirrorX = mirrorX;
    }

    public boolean isMirrorY() {
        return mirrorY;
    }

    public void setMirrorY(boolean mirrorY) {
        this.mirrorY = mirrorY;
    }

    public IPlay getPlay() {
        return play;
    }

    public void setPlay(IPlay play) {
        this.play = play;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
