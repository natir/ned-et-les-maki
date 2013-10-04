package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class Rooted extends Component {
    
    private boolean rooted;

    public Rooted(boolean rooted) {
	this.rooted = rooted;
    }

    public boolean isRooted() {
	return this.rooted;
    }

    public void setRooted(boolean rooted) {
       this.rooted = rooted;
    }
}
