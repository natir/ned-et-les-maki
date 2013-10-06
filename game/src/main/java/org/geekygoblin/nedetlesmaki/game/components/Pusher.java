package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class Pusher extends Component {
    
    private boolean push;

    public Pusher(boolean push) {
	this.push = push;
    }

    public boolean isPusher() {
	return this.push;
    }

    public void setPusher(boolean push) {
       this.push = push;
    }
}
