package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import game.terrain.Bonfire;

/**
 * Special action to teleport from one bonfire to another.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see Action
 * @see Bonfire
 */
public class TeleportAction extends Action {

    private final Location position;
    private final String destination;

    public TeleportAction(Location position, String destination) {
        this.position = position;
        this.destination = destination;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        map.moveActor(actor, position);
        return actor + " teleported to " + destination;
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " moves to " + destination;
    }
}
