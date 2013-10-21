package org.geekygoblin.nedetlesmaki.game.components;

import com.artemis.Component;

/**
 *
 * @author natir
 */
public class BlockOnPlate extends Component {
    
    private boolean block;

    public BlockOnPlate(boolean block) {
	this.block = block;
    }

    public boolean block() {
	return this.block;
    }

    public void setBlock(boolean stop) {
       this.block = stop;
    }
}
