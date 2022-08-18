package game.character.enemy;


import edu.monash.fit2099.engine.*;
import game.action.DieAction;
import game.behaviours.WanderBehaviour;
import game.enums.Status;
import game.interfaces.Resettable;

import java.util.Random;

/**
 * An undead minion.
 *
 * @editor Hazael Frans Christian
 * @version 2
 * @see Enemies
 */
public class Undead extends Enemies implements Resettable {
	// Will need to change this to a collection if Undead gets additional Behaviours.

	/** 
	 * Constructor.
	 * All Undeads are represented by an 'u' and have 30 hit points.
	 *
	 * @param name the name of this Undead
	 */
	public Undead(String name) {
		super(name, 'u', 50, 50, null);
		behaviours.add(new WanderBehaviour());
	}

	/**
	 * Override Enemies' playTurn to add the instant death dice
	 * @see edu.monash.fit2099.engine.Actor#playTurn(Actions, Action, GameMap, Display)
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		if (this.isConscious()) {
			// before looping through their behaviours, should determine if they should die
			Random random = new Random();

			if(!hasCapability(Status.ATTACKED)){
				// 10 sided die
				int number = 1;
				// random bounds = random.nextInt(max - nim) + min
				int dice = random.nextInt(11-1) + 1;
				if (number == dice){
					// too bad, the undead must die
					this.kill();
				}
			}
		}

		// if undead is not conscious, send it to death
		if (!this.isConscious()) {
			return new DieAction();
		}

		return super.playTurn(actions, lastAction, map, display);
	}

	@Override
	public void resetInstance() {
		// kill undead
		this.kill();
	}

	@Override
	public boolean isExist() {
		return this.hitPoints > 0;
	}

	/**
	 * kill the undead instantly.
	 */
	private void kill() {
		this.hurt(this.hitPoints);
	}

	@Override
	protected IntrinsicWeapon getIntrinsicWeapon() {
		return new IntrinsicWeapon(20, "hoot!");
	}

}
