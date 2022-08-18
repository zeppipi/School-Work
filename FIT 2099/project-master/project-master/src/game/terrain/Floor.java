package game.terrain;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;
import game.enums.Abilities;

/**
 * A class that represents the floor inside a building.
 *
 * @editor Hazael Frans Christian
 * @version 2
 * @see Ground
 */
public class Floor extends Ground {

	public Floor() {
		super('_');
	}

	/**
	 * Only the Player can enter this tile
	 *
	 * @param actor the Actor to check
	 * @return a boolean that determines the actor can or can't enter
	 */
	@Override
	public boolean canActorEnter(Actor actor){
		return actor.hasCapability(Abilities.ENTER_FLOOR);
	}

}
