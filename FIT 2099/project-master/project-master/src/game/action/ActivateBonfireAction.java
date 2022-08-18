package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.enums.LastBonfire;
import game.enums.Status;
import game.terrain.Bonfire;

/**
 * Special action to activate a bonfire.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see Action
 * @see Bonfire
 */
public class ActivateBonfireAction extends Action{

    private final Bonfire bonfire;
    private final Enum<LastBonfire> lastBonfireEnum;

    public ActivateBonfireAction(Bonfire bonfire, Enum<LastBonfire> lastBonfireEnum) {
        this.bonfire = bonfire;
        this.lastBonfireEnum = lastBonfireEnum;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        // update player's latest bonfire
        for (LastBonfire lastBonfire : LastBonfire.values()) {
            if (actor.hasCapability(lastBonfire)) {
                actor.removeCapability(lastBonfire);
            }
            actor.addCapability(this.lastBonfireEnum);
        }

        // remove "not_activated" status of the bonfire
        this.bonfire.removeCapability(Status.NOT_ACTIVATED);

        return actor + " lighted up the " + this.bonfire + "'s Bonfire";
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " lights the " + this.bonfire + "'s Bonfire";
    }
}
