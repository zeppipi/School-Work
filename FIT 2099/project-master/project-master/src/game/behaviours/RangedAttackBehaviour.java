package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.action.attackAction.AttackAction;
import game.enums.Abilities;
import game.enums.Passives;
import game.enums.Status;
import game.interfaces.Behaviour;

/**
 * Class that gives an actor the ability to perform ranged attack other actors
 *
 * @author Yap Choon Seong
 * @version 1
 * @see Action
 * @see Behaviour
 */
public class RangedAttackBehaviour extends Action implements Behaviour {
    /**
     * The target actor being attacked
     */
    private Actor target;

    /**
     * Constructor
     *
     * @param target the targeted actor
     */
    public RangedAttackBehaviour(Actor target) {
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
     * @param map   the GameMap containing the Actor
     * @return nothing or something to attack
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        if (!map.contains(target) || !map.contains(actor))
            return null;

        Location here = map.locationOf(actor);
        Location there = map.locationOf(target);

        int currentDistance = distance(here, there);
        if (currentDistance <= 3) {
            NumberRange xs, ys;
            if (here.x() == there.x() || here.y() == there.y()) {
                xs = new NumberRange(Math.min(here.x(), there.x()), Math.abs(here.x() - there.x()) + 1);
                ys = new NumberRange(Math.min(here.y(), there.y()), Math.abs(here.y() - there.y()) + 1);

                for (int x : xs) {
                    for (int y : ys) {
                        if (map.at(x, y).getGround().blocksThrownObjects()) {
                            actor.addCapability(Status.MISSED);
                            return new AttackAction(target, ", missed...");
                        }
                    }
                }
            }
            return new AttackAction(target, ", is shooting...");
        }
        return null;
    }

    /**
     * Compute the Manhattan distance between two locations.
     *
     * @param a the first location
     * @param b the first location
     * @return the number of steps between a and b if you only move in the four cardinal directions.
     */
    private int distance(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }
}

