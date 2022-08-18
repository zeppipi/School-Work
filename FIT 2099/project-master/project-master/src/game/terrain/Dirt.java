package game.terrain;

import edu.monash.fit2099.engine.Ground;
import game.enums.Abilities;

/**
 * A class that represents bare dirt.
 *
 * @editor Hazael Frans Christian
 * @version 2
 * @see Ground
 */
public class Dirt extends Ground {

	public Dirt() {
		super('.');
		this.addCapability(Abilities.BURNABLE);
	}
}
