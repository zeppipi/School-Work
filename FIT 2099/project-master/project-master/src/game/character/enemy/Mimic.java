package game.character.enemy;

import edu.monash.fit2099.engine.*;
import game.action.DieAction;
import game.action.ChestMimicAction;
import game.enums.Abilities;
import game.enums.Status;
import game.interfaces.Resettable;

/**
 * An enemy made out of a chest
 *
 * @author Hazael Frans Christian
 * @version 1
 * @see Enemies
 */
public class Mimic extends Enemies implements Resettable {

    /**
     * Constructor.
     *
     * @param location location they spawned in
     */
    public Mimic(Location location) {
        super("Chest", '?', 100, 200, location);//100
    }

    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        if(!this.hasCapability(Status.TO_BE_REPOSITIONED)){
            if(hasCapability(Status.OPENED)){
                return new DieAction();
            }else if(hasCapability(Abilities.MIMIC)){
                super.name = "Mimic";
                super.displayChar = 'M';
                return super.playTurn(actions, lastAction, map, display);
            }else{
                return new DoNothingAction();
            }
        }
        return super.playTurn(actions, lastAction, map, display);
    }

    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        if(hasCapability(Abilities.MIMIC)){
            return super.getAllowableActions(otherActor, direction, map);
        }else{
            Actions actions = new Actions();
            actions.add(new ChestMimicAction(this));
            return actions;
        }
    }

    @Override
    public String toString(){
        if(!hasCapability(Abilities.MIMIC)){
            return "Chest glistens with treasure. It";
        }else{
            return super.toString();
        }
    }

    @Override
    public void resetInstance() {
        removeCapability(Abilities.MIMIC);
        removeCapability(Status.OPENED);
        super.name = "Chest";
        super.displayChar = '?';
        super.resetInstance();
    }

    @Override
    protected IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(55, "kick!");
    }

    @Override
    public boolean isExist(){
        return true;
    }

}
