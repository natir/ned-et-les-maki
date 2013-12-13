/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;
import java.util.ArrayList;
import java.util.List;
import org.geekygoblin.nedetlesmaki.game.events.Trigger;

/**
 *
 * @author devnewton
 */
public final class TriggerableWhenRemoved extends Component {

    private final List<Trigger> triggers = new ArrayList<>();

    public TriggerableWhenRemoved(Trigger trigger) {
        this.triggers.add(trigger);
    }
    
    public TriggerableWhenRemoved add(Trigger trigger) {
        this.triggers.add(trigger);
        return this;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }
}
