package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class Boostable extends Component {
    
    private int nbCase;

    public Boostable(int nbCase) {
	this.nbCase = nbCase;
    }

    public int getNbCase() {
	return this.nbCase;
    }

    public void setNbCase(int nbCase) {
       this.nbCase = nbCase;
    }
}
