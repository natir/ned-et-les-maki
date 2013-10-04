package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class StopOnPlate extends Component {
    
    private bool stop;

    public Rooted(bool stop) {
	this.stop = stop;
    }

    public stop() {
	return this.stop;
    }

    public int setStop(bool stop) {
       this.stop = stop;
    }
}
