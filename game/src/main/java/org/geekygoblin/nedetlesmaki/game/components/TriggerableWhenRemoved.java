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
public class TriggerableWhenRemoved extends Component {

    private Trigger trigger;

    public TriggerableWhenRemoved(Trigger trigger) {
        this.trigger = trigger;
    }

    public Trigger getTrigger() {
        return trigger;
    }
}
