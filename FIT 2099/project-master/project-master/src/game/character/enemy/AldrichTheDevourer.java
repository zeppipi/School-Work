package game.character.enemy;

import edu.monash.fit2099.engine.*;
import game.action.DieAction;
import game.behaviours.FollowBehaviour;
import game.behaviours.RangedAttackBehaviour;
import game.Items.CinderOfLord;
import game.enums.Abilities;
import game.enums.Passives;
import game.enums.Status;
import game.weapon.DarkmoonLongbow;

/**
 * Class that represents the second Lord of Cinder, Alderich the Deveourer
 *
 * @author Hazael Frans Christian
 * @version 1
 * @see Enemies
 * @see LordOfCinder
 */
public class AldrichTheDevourer extends LordOfCinder{
    protected Actor target;

    /**
     * Constructor
     */
    public AldrichTheDevourer(Location location, Actor target) {
        super("Aldrich the Devourer", 'Z', 350, 5000, location);
        this.addItemToInventory(new DarkmoonLongbow());
        this.addItemToInventory(new CinderOfLord(Abilities.TRADE_FOR_ALDRICH_WEAPON));
        this.addCapability(Status.IS_BOSS);
        this.target = target;
    }

    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display){
        // if not conscious, the player wins!
        if (!this.isConscious()) {
            return new DieAction();
        }

        if (this.hasCapability(Passives.RANGED)) {
            behaviours.clear();
            behaviours.add(new RangedAttackBehaviour(target));
            behaviours.add(new FollowBehaviour(target));
        }

        return super.playTurn(actions, lastAction, map, display);
    }
}
