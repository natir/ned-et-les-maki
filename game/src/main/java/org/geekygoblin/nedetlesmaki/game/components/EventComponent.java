/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;
import org.geekygoblin.nedetlesmaki.game.events.Event;

/**
 *
 * @author devnewton
 */
public class EventComponent extends Component {

    private Event event;

    public EventComponent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
