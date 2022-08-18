package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.action.attackAction.AttackAction;
import game.interfaces.Behaviour;

/**
 * Class that gives an actor the ability to attack other actors
 *
 * @author Hazael Frans Christian
 * @version 1
 * @see Action
 * @see Behaviour
 */
public class AttackBehaviour extends Action implements Behaviour {
    /**
     * The target actor being attacked
     */
    private Actor target;

    /**
     * Constructor
     *
     * @param target the targeted actor
     */
    public AttackBehaviour(Actor target) {
        this.target = target;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        return null;
    }

    @Override
    public String menuDescription(Actor actor) {
        return null;
    }

    /**
     * Method that will execute the AttackAction method, by looping through their adjacent squares
     * and try to find the player in it
     *
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return nothing or something to attack
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        if(!map.contains(target) || !map.contains(actor))
            return null;

        for(Exit exit: map.locationOf(actor).getExits()){
            Location destination = exit.getDestination();

            if(destination.getActor() == target) {
                return new AttackAction(target, ", is attacking...");
            }

        }
        return null;
    }
}
