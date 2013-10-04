package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class Movable extends Component {
    
    private int nbCase;

    public Movable(int nbCase) {
	this.nbCase = nbCase;
    }

    public int getNbCase() {
	return this.nbCase;
    }

    public int setNbCase(int nbCase) {
       this.nbCase = nbCase;
    }
}
