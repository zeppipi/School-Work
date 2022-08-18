package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.enums.Status;

import java.util.Random;

/**
 * Gives an actor the ability to revive themselves
 *
 * @author Hazael Frans Christian
 * @version 1
 * @see Action
 */
public class ReviveAction extends Action {

    private String result;

    @Override
    public String execute(Actor actor, GameMap map) {
        Random random = new Random();

        //50% chance for revive
        int dice = random.nextInt(10);
        if(dice < 5){
            //heal function caps the hitpoints to their max hp, so 1000 would still come up to 100
            actor.heal(100000000);
            actor.addCapability(Status.REVIVED);
            result += actor + " has revived";
        }else{
            result += actor + " has failed to revive";
        }
        return menuDescription(actor);
    }

    @Override
    public String menuDescription(Actor actor) {
        return result.substring(4);
    }
}
