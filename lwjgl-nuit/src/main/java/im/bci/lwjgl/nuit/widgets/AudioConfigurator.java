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
package im.bci.lwjgl.nuit.widgets;

import java.util.ArrayList;
import java.util.List;

import im.bci.lwjgl.nuit.NuitToolkit;

public class AudioConfigurator extends Table {

    public static class Volume {

        int level;

        Volume(int level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return level + "%";
        }
    }

    public AudioConfigurator(NuitToolkit toolkit) {
        super(toolkit);
        List<Volume> possibleVolumes = new ArrayList<>();
        for (int l = 0; l <= 100; l += 10) {
            possibleVolumes.add(new Volume(l));
        }

        defaults().expand();
        cell(new Label(toolkit, "nuit.audio.configurator.music.volume"));
        cell(new Select<Volume>(toolkit, possibleVolumes) {
            @Override
            public void onOK() {
                super.onOK();
                changeMusicVolume(getSelected().level / 100.0f);
            }
        });
        row();
        cell(new Label(toolkit, "nuit.audio.configurator.effects.volume"));
        cell(new Select<Volume>(toolkit, possibleVolumes) {
            @Override
            public void onOK() {
                super.onOK();
                changeEffectVolume(getSelected().level / 100.0f);
            }
        });
        row();
        cell(new Button(toolkit, "nuit.audio.configurator.back") {
            @Override
            public void onOK() {
                closeAudioSettings();
            }
        }).colspan(2);
    }

    protected void changeEffectVolume(float f) {
    }

    protected void changeMusicVolume(float f) {
    }

    protected void closeAudioSettings() {
    }

}
