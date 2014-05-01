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
package org.geekygoblin.nedetlesmaki.core;

import javax.inject.Inject;
import javax.inject.Singleton;
import im.bci.jnuit.NuitLocale;
import im.bci.jnuit.NuitTranslator;
import java.util.Locale;

/**
 *
 * @author devnewton
 */
@Singleton
public class NedNuitTranslator extends NuitTranslator {

    @Inject
    public NedNuitTranslator() {
        if (Locale.getDefault().getLanguage().equals(new Locale("fr").getLanguage())) {
            setCurrentLocale(NuitLocale.FRENCH);
        }

        addEnglish();
        addFrench();
        addLevelNumbers();
    }

    private void addLevelNumbers() {
        for (NuitLocale locale : NuitLocale.values()) {
            for (int i = 0; i <= 30; ++i) {
                String index = String.valueOf(i);
                if (i < 10) {
                    index = "0" + index;
                }
                addTranslation(locale, "level." + index + ".name", index);
            }
        }
    }

    private void addEnglish() {
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.start", "START");
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.resume", "RESUME");
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.options", "OPTIONS");
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.extras", "EXTRAS");
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.quit", "QUIT");
        
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.video", "VIDEO");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.audio", "AUDIO");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.game.controls", "GAME CONTROLS");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.menu.controls", "MENU CONTROLS");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.back", "BACK");
        
        addTranslation(NuitLocale.ENGLISH, "extras.menu.button.cutscenes", "CUTSCENES");
        addTranslation(NuitLocale.ENGLISH, "extras.menu.button.artwork", "ARTWORK");
        addTranslation(NuitLocale.ENGLISH, "extras.menu.button.back", "BACK");
        
        addTranslation(NuitLocale.ENGLISH, "cutscenes.menu.button.intro", "INTRODUCTION");
        addTranslation(NuitLocale.ENGLISH, "cutscenes.menu.button.credits", "CREDITS");
        addTranslation(NuitLocale.ENGLISH, "cutscenes.menu.button.back", "BACK");
                
        addTranslation(NuitLocale.ENGLISH, "action.up", "Up");
        addTranslation(NuitLocale.ENGLISH, "action.down", "Down");
        addTranslation(NuitLocale.ENGLISH, "action.left", "Left");
        addTranslation(NuitLocale.ENGLISH, "action.right", "Right");
        addTranslation(NuitLocale.ENGLISH, "action.rewind", "Rewind");
        addTranslation(NuitLocale.ENGLISH, "action.menu", "Menu");

        addTranslation(NuitLocale.ENGLISH, "dialog.intro.01", "");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.02", "Ned pulled round in a world unknown to him. ");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.03", "");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.04", "He began to explore.");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.05", "Doing so, he found out what appeared to be a residential complex.");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.06", "");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.07", "Individuals were not slow to show. ");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.08", "Without further explanation, the indigenous creatures urged him unceremoniously into some tower.");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.09", "");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.10", "Thus, began the adventures of Ned among the spirimonsters.");
        
        addTranslation(NuitLocale.ENGLISH, "dialog.credits.devnewton", "Main developer - http://devnewton.bci.im");
        addTranslation(NuitLocale.ENGLISH, "dialog.credits.natir", "Natir : The man in the dressing gown");
    }

    private void addFrench() {
        addTranslation(NuitLocale.FRENCH, "main.menu.button.start", "DEMARRER");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.resume", "CONTINUER");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.options", "OPTIONS");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.extras", "EXTRAS");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.quit", "QUITTER");
        
        addTranslation(NuitLocale.FRENCH, "options.menu.button.video", "VIDEO");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.audio", "AUDIO");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.game.controls", "CONTROLES DU JEU");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.menu.controls", "CONTROLES DES MENUS");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.back", "RETOUR");
        
        addTranslation(NuitLocale.FRENCH, "extras.menu.button.cutscenes", "CINEMATIQUES");
        addTranslation(NuitLocale.FRENCH, "extras.menu.button.artwork", "ILLUSTRATIONS");
        addTranslation(NuitLocale.FRENCH, "extras.menu.button.back", "RETOUR");
        
        addTranslation(NuitLocale.FRENCH, "cutscenes.menu.button.intro", "INTRODUCTION");
        addTranslation(NuitLocale.FRENCH, "cutscenes.menu.button.credits", "CREDITS");
        addTranslation(NuitLocale.FRENCH, "cutscenes.menu.button.back", "RETOUR");
        
        addTranslation(NuitLocale.FRENCH, "action.up", "Haut");
        addTranslation(NuitLocale.FRENCH, "action.down", "Bas");
        addTranslation(NuitLocale.FRENCH, "action.left", "Gauche");
        addTranslation(NuitLocale.FRENCH, "action.right", "Droite");
        addTranslation(NuitLocale.FRENCH, "action.rewind", "Rembobine");
        addTranslation(NuitLocale.FRENCH, "action.menu", "Menu");

        addTranslation(NuitLocale.FRENCH, "dialog.intro.01", "");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.02", "Ned reprit connaissance dans un monde qui lui était inconnu.");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.03", "");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.04", "Il entreprit de l'explorer.");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.05", "Ce faisant il découvrit ce qui semblait être un complexe résidentiel.");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.06", "");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.07", "Des individus ne tardèrent pas à se montrer.");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.08", "Sans plus d'explication, les créatures indigènes le poussèrent dans la tour sans ménagement.");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.09", "");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.10", "Ainsi débutèrent les aventures de Ned chez les spirimonstres.");
        
        addTranslation(NuitLocale.FRENCH, "dialog.credits.devnewton", "Développeur depuis 1742 - http://devnewton.bci.im");
        addTranslation(NuitLocale.FRENCH, "dialog.credits.natir", "Natir : L'homme a la robe de chambre");
    }
}
