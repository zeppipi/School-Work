package game.character.enemy;

import edu.monash.fit2099.engine.*;
import game.action.attackAction.AttackAction;
import game.behaviours.FollowBehaviour;
import game.action.RepositionAction;
import game.behaviours.AttackBehaviour;
import game.enums.Status;
import game.interfaces.Behaviour;
import game.interfaces.Resettable;
import game.interfaces.Soul;

import java.util.ArrayList;

/**
 * A class that groups up all enemies, all enemies hold and implements the same classes, so their
 * implementations are done here, if any changes are needed for that specific enemy, they can
 * simply override these
 *
 * @author Hazael Frans Christian
 * @version 1
 * @see Actor
 * @see Resettable
 * @see Soul
 */
public abstract class Enemies extends Actor implements Resettable, Soul {

    /**
     * List of behaviours
     */
    protected ArrayList<Behaviour> behaviours;

    /**
     * Initial position of the enemy
     */
    protected Location initialLocation;

    /**
     * Amount of souls
     */
    protected int souls;


    /**
     * Constructor.
     *
     * @param name        the name of the Actor
     * @param displayChar the character that will represent the Actor in the display
     * @param hitPoints   the Actor's starting hit points
     */
    public Enemies(String name, char displayChar, int hitPoints, int souls, Location location) {
        super(name, displayChar, hitPoints);
        this.souls = souls;
        this.initialLocation = location;
        this.behaviours = new ArrayList<>();
        this.addCapability(Status.VULNERABLE);
        registerInstance();
    }

    /**
     * Method that makes all allowable actions for the actor
     *
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return a list of actions
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions actions = new Actions();
        // it can be attacked only by the HOSTILE opponent, and this action will not attack the HOSTILE enemy back.
        if(otherActor.hasCapability(Status.HOSTILE_TO_ENEMY)) {
            addCapability(Status.ATTACKED);
            // add new behaviours
            behaviours.clear();
            behaviours.add(new AttackBehaviour(otherActor));
            behaviours.add(new FollowBehaviour(otherActor));
            // return the attack action
            actions.add(new AttackAction(this,direction));
        }
        return actions;
    }

    /**
     * Method that determines what the actor will do in their turn
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return an action to do
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        // if game reset, reposition enemy onto their initial location
        if (this.hasCapability(Status.TO_BE_REPOSITIONED)) {
            this.removeCapability(Status.TO_BE_REPOSITIONED);
            return new RepositionAction(this.initialLocation);
        }

        // if enemy is stunned, do nothing for one round
        else if (this.hasCapability(Status.STUN)) {
            this.removeCapability(Status.STUN);
            return new DoNothingAction();
        }

        // loop through all behaviours
        for(game.interfaces.Behaviour Behaviour : behaviours) {
            Action action = Behaviour.getAction(this, map);
            if (action != null)
                return action;
        }
        return new DoNothingAction();
    }

    @Override
    public void transferSouls(Soul soulObject) {
        soulObject.addSouls(this.souls);
    }

    @Override
    public boolean isExist() {
        return this.hitPoints > 0;
    }

    @Override
    public void resetInstance() {
        this.hitPoints = this.maxHitPoints;
        // remove all behaviour
        this.behaviours.clear();
        // allow actor to reset location in its own turn
        this.addCapability(Status.TO_BE_REPOSITIONED);
    }

    @Override
    public int getSouls() {
        return this.souls;
    }

    @Override
    public String toString() {
        StringBuilder weaponName = new StringBuilder();
        for (Item item: this.inventory){
            if (item.asWeapon() != null) {
                weaponName.append(item);
            }
        }
        if (weaponName.toString().equals("")) {
            weaponName.append("No weapon");
        }
        return this.name + " (" + this.hitPoints + "/" + this.maxHitPoints + ") (" + weaponName + ")"; }
}

