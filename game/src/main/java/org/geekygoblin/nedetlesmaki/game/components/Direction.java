package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class Direction extends Component {
    
    private String dir;

    public Direction(String dir) {
	this.dir = dir;
    }

    public String getDirection() {
	return this.dir;
    }

    public void setDirection(String dir) {
       this.dir = dir;
    }

}
