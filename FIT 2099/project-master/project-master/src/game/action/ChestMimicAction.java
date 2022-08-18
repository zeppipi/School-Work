package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.enums.Abilities;
import game.enums.Status;

/**
 * Class that represents the dice roll if the chest should just drop their tokens or become a mimic
 *
 * @author Hazael Frans Chrsitian
 * @version 1
 * @see game.character.enemy.Mimic
 */
public class ChestMimicAction extends Action {
    private final Actor actor;

    /**
     * Constructor
     *
     * @param actor actor to edit their statuses and abilities
     */
    public ChestMimicAction(Actor actor) {
        this.actor = actor;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        int dice = (int)(Math.random() * 2 + 1);    //get random number

        if(dice == 1){
            this.actor.addCapability(Abilities.MIMIC);     //make into mimic
            return "Chest turns out to be a Mimic!";
        }else{
            this.actor.addCapability(Status.OPENED);
            Action dropTokens = new ChestDropTokenAction();
            return dropTokens.execute(this.actor, map);    //drop the tokens
        }
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " open the chest";
    }
}
