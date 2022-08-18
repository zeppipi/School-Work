package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.Items.consumable.Potion;

/**
 * Special Action for consuming Potion.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see Action
 * @see Potion
 */
public class ConsumeAction extends Action {

    /**
     * The Potion that is to be consumed.
     */
    protected Potion potion;

    /**
     * Constructor
     * @param potion potion to be consumed
     */
    public ConsumeAction(Potion potion) {
        this.potion = potion;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        boolean status = this.potion.consume();
        if (status){
            return actor + " consumed " + this.potion.toString();
        } else {
            return actor + " unable to consume " + this.potion.toString() + ", please refill charges";
        }
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " consumes " + this.potion.getName();
    }
}
