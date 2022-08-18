package game.action;

import edu.monash.fit2099.engine.*;
import game.enums.Abilities;
import game.terrain.Fire;

/**
 * Action to print out the message that Yhorm the Giant has activated ember mode
 *
 * @author Hazael Frans Christian
 * @version 1
 * @see Action
 */
public class EmberAction extends Action {
    @Override
    public String execute(Actor actor, GameMap map) {
        Location here = map.locationOf(actor);

        // burn surrounding
        for (Exit exit : here.getExits()) {
            if (exit.getDestination().getGround().hasCapability(Abilities.BURNABLE)) {
                exit.getDestination().setGround(new Fire());
            }
        }

        return actor + " HAS ACTIVATED EMBER MODE";
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor.toString() + " burns surrounding.";
    }
}