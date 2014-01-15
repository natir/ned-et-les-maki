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
package org.geekygoblin.nedetlesmaki.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
            for (int i = 0; i < 30; ++i) {
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
        addTranslation(NuitLocale.ENGLISH, "main.menu.button.quit", "QUIT");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.video", "VIDEO");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.audio", "AUDIO");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.controls", "CONTROLS");
        addTranslation(NuitLocale.ENGLISH, "options.menu.button.back", "BACK");

        addTranslation(NuitLocale.ENGLISH, "dialog.button.next", "Next");
        addTranslation(NuitLocale.ENGLISH, "dialog.button.previous", "Previous");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.reveil.1", "Zzz...");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.reveil.2", "I'm tired.");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.tour_au_loin.1", "What?");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.tour_au_loin.2", "A tower?");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.pied_de_la_tour.1", "?");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.pied_de_la_tour.2", "Help us!");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.pied_de_la_tour.3", "But...");
        addTranslation(NuitLocale.ENGLISH, "dialog.intro.dans_la_tour.1", "And now?");
    }

    private void addFrench() {
        addTranslation(NuitLocale.FRENCH, "main.menu.button.start", "DEMARRER");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.resume", "CONTINUER");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.options", "OPTIONS");
        addTranslation(NuitLocale.FRENCH, "main.menu.button.quit", "QUITTER");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.video", "VIDEO");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.audio", "AUDIO");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.controls", "CONTROLES");
        addTranslation(NuitLocale.FRENCH, "options.menu.button.back", "RETOUR");

        addTranslation(NuitLocale.FRENCH, "dialog.button.next", "Suite");
        addTranslation(NuitLocale.FRENCH, "dialog.button.previous", "Retour");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.reveil.1", "Zzz...");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.reveil.2", "C'est dur de se lever!");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.tour_au_loin.1", "Mais?");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.tour_au_loin.2", "Une tour?");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.pied_de_la_tour.1", "?");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.pied_de_la_tour.2", "Aidez-nous!");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.pied_de_la_tour.3", "Mais...");
        addTranslation(NuitLocale.FRENCH, "dialog.intro.dans_la_tour.1", "Et maintenant que faire?");
    }
}
