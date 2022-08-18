package game.character.player;

import edu.monash.fit2099.engine.*;
import game.action.attackAction.AttackAction;
import game.Items.consumable.EstusFlask;
import game.action.ResetAction;
import game.enums.Abilities;
import game.enums.LastBonfire;
import game.enums.Status;
import game.interfaces.Resettable;
import game.interfaces.Soul;
import game.Items.token.TokenOfSouls;
import game.terrain.Bonfire;
import game.weapon.BroadSword;


/**
 * Class representing the Player.
 *
 * @editor Shin Yung Xin
 * @version 2
 * @see Actor
 * @see Soul
 * @see Resettable
 * @see ResetAction
 */
public class Player extends Actor implements Soul, Resettable {

	private final Menu menu = new Menu();
	/**
	 * The value of soul that the player is currently holding
	 */
	protected int soul;
	/**
	 * The last location of the player before current turn
	 */
	private Location lastLocation;
	/**
	 * The bonfire to respawn the player
	 */
	private Location respawnLocation;
	/**
	 * The soul of token that has previously dropped by the player
	 * null if not available
	 */
	private TokenOfSouls droppedToken;

	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting number of hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints, Location initialLocation) {
		super(name, displayChar, hitPoints);
		this.soul = 0;
		this.respawnLocation = initialLocation;
		this.droppedToken = null;
		// add estus flask
		this.addItemToInventory(new EstusFlask(this));
		// by default the player holds broadsword
		this.addItemToInventory(new BroadSword());
		// add default capability
		this.addCapability(Status.HOSTILE_TO_ENEMY);
		this.addCapability(Abilities.TRADABLE);
		this.addCapability(Abilities.REST);
		this.addCapability(Abilities.RESET);
		this.addCapability(Status.VULNERABLE);
		this.addCapability(Abilities.ENTER_FLOOR);
		this.addCapability(Abilities.ENTER_VALLEY);
		// register as soul instance
		registerInstance();
	}

	@Override
	public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
		Actions actions = new Actions();
		// it can be attacked by any enemy!
		actions.add(new AttackAction(this,direction));
		return actions;
	}

	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		// player died lmao
		if(!isConscious()){
			// update its respawn location to the last interacted bonfire
			this.updateRespawnPosition();
			// create a token of souls and update droppedToken
			this.updateDroppedToken();
			return new ResetAction(this.lastLocation, this.respawnLocation, this.droppedToken);
		}

		// update last location
		this.lastLocation = map.locationOf(this);

		// print player's status
		this.printStatus(display);

		// Handle multi-turn Actions
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();

		// restore player's HOSTILE_TO_ENEMY status after disarmed for one round
		if (!this.hasCapability(Status.HOSTILE_TO_ENEMY)) {
			this.addCapability(Status.HOSTILE_TO_ENEMY);
		}

		// return/print the console menu
		return menu.showMenu(this, actions, display);
	}

	@Override
	public void resetInstance() {
		// restore full hp
		this.hitPoints = maxHitPoints;
	}

	@Override
	public boolean isExist() {
		return this.hitPoints>0;
	}


	@Override
	public void transferSouls(Soul soulObject) {
		soulObject.addSouls(this.soul);
		this.soul = 0;
	}

	@Override
	public boolean subtractSouls(int souls) {
		this.soul -= souls ;
		return true;
	}

	@Override
	public int getSouls() {
		return this.soul;
	}

	@Override
	public boolean addSouls(int souls) {
		this.soul += souls ;
		return true;
	}

	/**
	 * getter for the max hit points of player
	 * @return max hit point of the player instance
	 */
	public int getMaxHitPoint() {
		return this.maxHitPoints;
	}

	/**
	 * Display status of player at each round
	 *
	 * @param display object that manage the I/O for the system, responsible to display message on console
	 */
	private void printStatus(Display display) {
		// print player's HP
		display.println(this + " (" + this.hitPoints + "/" + this.maxHitPoints + ")");

		// print Soul
		display.println("Remaining Souls: " + this.soul);

		// print weapon
		display.println("Weapon: "+ this.getWeapon().toString() + " [Damage: " + this.getWeapon().damage() + " | Hit Rate: " + this.getWeapon().chanceToHit() + "]");

		// print estus flask
		for (Item item: this.inventory) {
			if (item.hasCapability(Status.DRINKABLE)) {
				// print player's Estus Flask status
				display.println(item.toString());
			}
		}
	}

	/**
	 * update player's respawn location to the last interacted bonfire
	 */
	private void updateRespawnPosition() {
		for (LastBonfire lastBonfire : LastBonfire.values()) {
			if (this.hasCapability(lastBonfire)) {
				this.respawnLocation = Bonfire.lastBonfireLocation(lastBonfire);
			}
		}
	}

	/**
	 * Create a new token and update droppedToken
	 */
	private void updateDroppedToken() {
		if (this.droppedToken != null) {
			this.droppedToken.removeCapability(Status.UPDATED);
		}

		TokenOfSouls newToken = new TokenOfSouls();
		this.transferSouls(newToken.asSoul());
		this.droppedToken = newToken;
	}

}
