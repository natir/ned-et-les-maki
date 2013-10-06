package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class Pushable extends Component {

    private boolean pushable;

    public Pushable(boolean push) {
	this.pushable = push;
    }

    public boolean isPushable() {
	return this.pushable;
    }

    public void setPushable(boolean push) {
       this.pushable = push;
    }
}

