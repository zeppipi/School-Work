package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;

/**
 * An action that has the ability to reposition actors
 *
 * @author Hazael Frans Christian
 * @version 1
 * @see Action
 */
public class RepositionAction extends Action {

    /**
     * original position
     */
    private final Location position;

    /**
     * Constructor
     * @param position initial position of the actor
     */
    public RepositionAction(Location position) {
        this.position = position;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        map.moveActor(actor, position);
        return menuDescription(actor);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " returned to its original position";
    }
}
