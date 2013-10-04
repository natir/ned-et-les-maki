/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;
import org.geekygoblin.nedetlesmaki.game.events.Trigger;

/**
 *
 * @author devnewton
 */
public class Triggerable extends Component {

    private Trigger trigger;

    public Triggerable(Trigger trigger) {
        this.trigger = trigger;
    }

    public Trigger getTrigger() {
        return trigger;
    }
}
