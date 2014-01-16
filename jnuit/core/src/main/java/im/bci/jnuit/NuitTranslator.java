/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

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
package im.bci.jnuit;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author devnewton
 */
public class NuitTranslator {

    private final EnumMap<NuitLocale, Map<String, String>> translations = new EnumMap<NuitLocale, Map<String, String>>(NuitLocale.class);
    private NuitLocale currentLocale = NuitLocale.ENGLISH;

    public NuitTranslator() {
        for (NuitLocale l : NuitLocale.values()) {
            translations.put(l, new HashMap<String, String>());
        }
        addEnglish();
        addFrench();
    }

    public String getMessage(String key) {
        String message = translations.get(currentLocale).get(key);
        if (null == message) {
            for (NuitLocale l : NuitLocale.values()) {
                message = translations.get(currentLocale).get(key);
                if (null != message) {
                    break;
                }
            }
        }
        if (null == message) {
            message = key;
        }
        return message;
    }

    public void addTranslation(NuitLocale locale, String key, String message) {
        translations.get(locale).put(key, message);
    }

    public void setCurrentLocale(NuitLocale currentLocale) {
        this.currentLocale = currentLocale;
    }

    public NuitLocale getCurrentLocale() {
        return currentLocale;
    }

    private void addEnglish() {
        addTranslation(NuitLocale.ENGLISH, "nuit.action.menu.up", "Up");
        addTranslation(NuitLocale.ENGLISH, "nuit.action.menu.down", "Down");
        addTranslation(NuitLocale.ENGLISH, "nuit.action.menu.left", "Left");
        addTranslation(NuitLocale.ENGLISH, "nuit.action.menu.right", "Right");
        addTranslation(NuitLocale.ENGLISH, "nuit.action.menu.ok", "OK");
        addTranslation(NuitLocale.ENGLISH, "nuit.action.menu.cancel", "Cancel");
        addTranslation(NuitLocale.ENGLISH, "nuit.video.configurator.mode", "Mode");
        addTranslation(NuitLocale.ENGLISH, "nuit.video.configurator.fullscreen", "Fullscreen");
        addTranslation(NuitLocale.ENGLISH, "nuit.video.configurator.apply", "Apply");
        addTranslation(NuitLocale.ENGLISH, "nuit.video.configurator.back", "Back");
        addTranslation(NuitLocale.ENGLISH, "nuit.audio.configurator.music.volume", "Music volume");
        addTranslation(NuitLocale.ENGLISH, "nuit.audio.configurator.effects.volume", "Effects volume");
        addTranslation(NuitLocale.ENGLISH, "nuit.audio.configurator.back", "Back");
        addTranslation(NuitLocale.ENGLISH, "nuit.controls.configurator.action", "Action");
        addTranslation(NuitLocale.ENGLISH, "nuit.controls.configurator.control", "Control");
        addTranslation(NuitLocale.ENGLISH, "nuit.controls.configurator.alternative", "Alternative");
        addTranslation(NuitLocale.ENGLISH, "nuit.controls.configurator.back", "Back");
        addTranslation(NuitLocale.ENGLISH, "nuit.controls.configurator.resets", "Resets");
        addTranslation(NuitLocale.ENGLISH, "nuit.controls.configurator.defaults", "Defaults");
        addTranslation(NuitLocale.ENGLISH, "nuit.controls.configurator.press.key", "Press a key...");
        addTranslation(NuitLocale.ENGLISH, "nuit.controls.configurator.press.key.again", "Press again...");
        addTranslation(NuitLocale.ENGLISH, "nuit.toggle.yes", "Yes");
        addTranslation(NuitLocale.ENGLISH, "nuit.toggle.no", "No");
    }

    private void addFrench() {
        addTranslation(NuitLocale.FRENCH, "nuit.action.menu.up", "Haut");
        addTranslation(NuitLocale.FRENCH, "nuit.action.menu.down", "Bas");
        addTranslation(NuitLocale.FRENCH, "nuit.action.menu.left", "Gauche");
        addTranslation(NuitLocale.FRENCH, "nuit.action.menu.right", "Droite");
        addTranslation(NuitLocale.FRENCH, "nuit.action.menu.ok", "OK");
        addTranslation(NuitLocale.FRENCH, "nuit.action.menu.cancel", "Annuler");
        addTranslation(NuitLocale.FRENCH, "nuit.video.configurator.mode", "Mode");
        addTranslation(NuitLocale.FRENCH, "nuit.video.configurator.fullscreen", "Plein écran");
        addTranslation(NuitLocale.FRENCH, "nuit.video.configurator.apply", "Appliquer");
        addTranslation(NuitLocale.FRENCH, "nuit.video.configurator.back", "Retour");
        addTranslation(NuitLocale.FRENCH, "nuit.audio.configurator.music.volume", "Volume de la musique");
        addTranslation(NuitLocale.FRENCH, "nuit.audio.configurator.effects.volume", "Volume des effets");
        addTranslation(NuitLocale.FRENCH, "nuit.audio.configurator.back", "Retour");
        addTranslation(NuitLocale.FRENCH, "nuit.controls.configurator.action", "Action");
        addTranslation(NuitLocale.FRENCH, "nuit.controls.configurator.control", "Contrôle");
        addTranslation(NuitLocale.FRENCH, "nuit.controls.configurator.alternative", "Alternative");
        addTranslation(NuitLocale.FRENCH, "nuit.controls.configurator.back", "Retour");
        addTranslation(NuitLocale.FRENCH, "nuit.controls.configurator.resets", "Réinitialiser");
        addTranslation(NuitLocale.FRENCH, "nuit.controls.configurator.defaults", "Défauts");
        addTranslation(NuitLocale.FRENCH, "nuit.controls.configurator.press.key", "Appuyez...");
        addTranslation(NuitLocale.FRENCH, "nuit.controls.configurator.press.key.again", "Réappuyez...");
        addTranslation(NuitLocale.FRENCH, "nuit.toggle.yes", "Oui");
        addTranslation(NuitLocale.FRENCH, "nuit.toggle.no", "Non");
    }

}
