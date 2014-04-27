/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.core.components;

import com.artemis.Component;
import java.util.ArrayList;
import java.util.List;
import org.geekygoblin.nedetlesmaki.core.events.Trigger;

/**
 *
 * @author devnewton
 */
public final class TriggerableWhenRemoved extends Component {

    private final List<Trigger> triggers = new ArrayList<Trigger>();

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
