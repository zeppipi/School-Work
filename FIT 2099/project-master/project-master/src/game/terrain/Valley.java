package game.terrain;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;
import game.enums.Abilities;

/**
 * The gorge or endless gap that is dangerous for the Player.
 *
 * @editor Hazael Frans Christian
 * @version 2
 * @see Ground
 */
public class Valley extends Ground {

	public Valley() {
		super('+');
		this.addCapability(Abilities.INSTA_KILL);
	}

	/**
	 * FIXME: At the moment, the Player cannot enter it. It is boring.
	 * @param actor the Actor to check
	 * @return false or actor cannot enter.
	 */
	@Override
	public boolean canActorEnter(Actor actor){
		// only player can pass
		return (actor.hasCapability(Abilities.ENTER_VALLEY));
	}

	@Override
	public void tick(Location location){
		if(location.containsAnActor()){
			location.getActor().hurt(Integer.MAX_VALUE);
		}
	}
}
