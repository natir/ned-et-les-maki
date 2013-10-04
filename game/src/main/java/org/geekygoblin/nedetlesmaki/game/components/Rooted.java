package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class Rooted extends Component {
    
    private bool rooted;

    public Rooted(bool rooted) {
	this.rooted = rooted;
    }

    public isRooted() {
	return this.rooted;
    }

    public int setRooted(bool rooted) {
       this.rooted = rooted;
    }
}
