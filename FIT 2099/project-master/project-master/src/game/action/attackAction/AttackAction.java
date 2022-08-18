package game.action.attackAction;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;
import game.action.ChestDropTokenAction;
import game.action.ReviveAction;
import game.enums.Abilities;
import game.enums.Passives;
import game.enums.Status;
import game.enums.Weakness;

/**
 * Special Action for attacking other Actors.
 *
 * @author Hazael Frans Christian
 * @version 2
 * @see Action
 */
public class AttackAction extends Action {

	/**
	 * The Actor that is to be attacked
	 */
	protected Actor target;

	/**
	 * The direction of incoming attack.
	 */
	protected String direction;

	/**
	 * Random number generator
	 */
	protected Random rand = new Random();

	protected int damage;
	protected int hitRate;

	/**
	 * Constructor.
	 * 
	 * @param target the Actor to attack
	 */
	public AttackAction(Actor target, String direction) {
		this.target = target;
		this.direction = direction;
	}

	/**
	 * Critical Strike (Passive): It has a 20% success rate to deal double damage with a normal attack
	 *
	 * @return output string of the passive skill
	 */
	private String CriticalStrike(Actor actor){
		if (rand.nextInt(100) <= 20) {
			this.damage = this.damage * 2;
			return System.lineSeparator() + actor.toString() + " performs critical strike";
		}
		else {
			return System.lineSeparator() + actor.toString() + " fails to perform critical strike";
		}
	}

	/**
	 * Dullness (Passive): attacking enemies that are not weak to Storm Ruler will decrease its effectiveness.
	 * The damage is reduced by half (i.e., it becomes 35), but the hit rate remains 60%.
	 *
	 * @return output string of the passive skill
	 */
	private String Dullness(){
		if (target.hasCapability(Weakness.WEAK_TO_STORM_RULER)) {
			return System.lineSeparator() + "Dullness is not activated on " + target.toString();
		}
		else {
			damage = damage / 2;
			return System.lineSeparator() + "Dullness is activated on " + target.toString();
		}
	}

	/**
	 * Rage mode (Passive): While the holder is in rage mode/ember form,
	 * It increases the holder’s success hit rate by 30% (in other words, the hit rate: 60% + 30% = 90%).
	 *
	 * @return output string of the passive skill
	 */
	private String RageMode(Actor actor){
		if (actor.hasCapability(Status.EMBER_MODE)) {
			int tempHitRate = hitRate;
			hitRate = hitRate + 30;
			return System.lineSeparator() + actor + " is in rage mode, its hit rate is increased from " + tempHitRate + "to" + hitRate;
		}
		else {
			return System.lineSeparator() + actor + " is not in rage mode";
		}
	}

	@Override
	public Action getNextAction() {
		return super.getNextAction();
	}

	@Override
	public String execute(Actor actor, GameMap map) {

		Weapon weapon = actor.getWeapon();

		this.damage = weapon.damage();

		if(actor.hasCapability(Status.MISSED)) {
			this.hitRate = 0;
		}
		else {
			this.hitRate = weapon.chanceToHit();
		}

		String result = "";

		// perform passive skills when attacking
		for (Item item: actor.getInventory()) {
			if (item.asWeapon() != null) {
				if (item.hasCapability(Passives.CRITICAL_STRIKE)) {
					result += CriticalStrike(actor);
				}
				if (item.hasCapability(Passives.DULLNESS)) {
					result += Dullness();
				}
				if (item.hasCapability(Passives.RAGE_MODE)) {
					result += RageMode(actor);
				}
				break;
			}
		}

		// check if the attacker missed the attack
		if (!(rand.nextInt(100) <= this.hitRate)) {
			return actor + " misses " + target + ".";
		}

		// attacker attacks
		target.hurt(this.damage);

		// output result
		result += System.lineSeparator() + actor + " " + weapon.verb() + " " + target + " for " + this.damage + " damage.";

		//for enemies that can revive
		if (target.hasCapability(Abilities.REVIVE) && !target.hasCapability(Status.REVIVED) && !target.isConscious()){
			ReviveAction revive = new ReviveAction();
			result += System.lineSeparator() + revive.execute(target, map);
		}

		//for mimics
		if(target.hasCapability(Abilities.MIMIC) && !target.isConscious()){
			ChestDropTokenAction drops = new ChestDropTokenAction();
			result += System.lineSeparator() + drops.execute(target, map);
		}

		// when the actor being attacked dies
		if (!target.isConscious()) {
			Actions dropActions = new Actions();
			// drop all items
			for (Item item : target.getInventory())
				dropActions.add(item.getDropAction(actor));
			for (Action drop : dropActions)
				drop.execute(target, map);

			// if the actor is player, reset game
			if (target.hasCapability(Abilities.RESET)) {
				result += System.lineSeparator() + target + " is killed, the game will be reset";
			} else if (target.hasCapability(Status.IS_BOSS)) {
				result += System.lineSeparator() + target + " is killed, the player wins!";
			} else {
				// attacker gain souls and remove dead actor
				target.asSoul().transferSouls(actor.asSoul());
				map.removeActor(target);
				result += System.lineSeparator() + target + " is killed.";
			}
		}
		return result;
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " attacks " + target + " at " + direction;
	}
}
