package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.enums.Abilities;
import game.enums.LastBonfire;
import game.reset.ResetManager;

/**
 * Special action to perform rest at Bonfire.
 * Can only be called and created by the player when it rests at Bonfire.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see Action
 */
public class RestAction extends Action {

    /**
     * Name of Bonfire
     */
    private final String bonfire;
    private final Enum<LastBonfire> lastBonfireEnum;

    public RestAction(String bonfire, Enum<LastBonfire> lastBonfireEnum) {
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

        // only actor with REST capability can call this method
        if (!actor.hasCapability(Abilities.REST)) {
            return null;
        }

        // reset all resettable instances
        ResetManager resetManager = ResetManager.getInstance();
        resetManager.run();

        // return string
        return actor + " took a good rest!";
    }

    @Override
    public String menuDescription(Actor actor) {
        if (actor.hasCapability(Abilities.REST)) {
            return actor + " rest at " + this.bonfire + "'s Bonfire";
        }
        return null;
    }
}
