package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class StopOnPlate extends Component {
    
    private boolean stop;

    public StopOnPlate(boolean stop) {
	this.stop = stop;
    }

    public boolean stop() {
	return this.stop;
    }

    public void setStop(boolean stop) {
       this.stop = stop;
    }
}
