package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class Indexed extends Component {

    private boolean index;

    public Indexed(boolean index) {
	this.index = index;
    }

    public boolean isIndexed() {
	return this.index;
    }

    public void setIndexed(boolean index) {
       this.index = index;
    }
}

