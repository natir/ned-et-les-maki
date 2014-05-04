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
package org.geekygoblin.nedetlesmaki.core.events;

import com.artemis.Entity;
import javax.inject.Inject;
import org.geekygoblin.nedetlesmaki.core.NedGame;
import org.geekygoblin.nedetlesmaki.core.NamedEntities;
import org.geekygoblin.nedetlesmaki.core.components.ui.MainMenu;

/**
 *
 * @author devnewton
 */
public class ShowMenuTrigger extends OneShotTrigger {
    private final Entity mainMenu;
    private final Entity ingameControls;
    
    @Inject
    public ShowMenuTrigger(@NamedEntities.MainMenu Entity mainMenu,@NamedEntities.IngameControls Entity ingameControls) {
        this.mainMenu = mainMenu;
        this.ingameControls = ingameControls;
    }

    @Override
    public void process(NedGame game) {
        mainMenu.getComponent(MainMenu.class).show();
        mainMenu.enable();
        ingameControls.disable();
    }

    
}
