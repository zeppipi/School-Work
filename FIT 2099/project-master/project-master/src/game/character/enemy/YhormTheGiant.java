package game.character.enemy;

import edu.monash.fit2099.engine.*;
import game.action.DieAction;
import game.action.EmberAction;
import game.action.attackAction.WindSlashAction;
import game.Items.CinderOfLord;
import game.enums.Abilities;
import game.enums.Status;
import game.enums.Weakness;
import game.weapon.YhormGreatMachete;

/**
 * Class to represent one of the Lord of Cinders, Yhorm the Giant
 *
 * @author Hazael Frans Christian
 * @version 1
 * @see Enemies
 * @see LordOfCinder
 */
public class YhormTheGiant extends LordOfCinder {

    /**
     * Constructor.
     */
    public YhormTheGiant(Location location) {
        super("Yhorm the Giant", 'Y', 500, 5000, location);
        this.addItemToInventory(new YhormGreatMachete());
        this.addItemToInventory(new CinderOfLord(Abilities.TRADE_FOR_YHORM_WEAPON));
        this.addCapability(Weakness.WEAK_TO_STORM_RULER);
        this.addCapability(Status.IS_BOSS);
    }

    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display){
        // if not conscious, the player wins!
        if (!this.isConscious()) {
            return new DieAction();
        }

        // activate ember form
        if (this.hitPoints < this.maxHitPoints/2 && !this.hasCapability(Status.EMBER_MODE)){
            this.addCapability(Status.EMBER_MODE);
            return new EmberAction();
        }

        return super.playTurn(actions, lastAction, map, display);
    }

    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions actions = super.getAllowableActions(otherActor, direction, map);

        // allow fully charged storm ruler to perform slash
        if (otherActor.hasCapability(Abilities.WIND_SLASH)) {
            actions.add(new WindSlashAction(this, direction));
        }
        return actions;
    }

    @Override
    public void resetInstance() {
        super.resetInstance();
        if (this.hasCapability(Status.EMBER_MODE)) {
            this.removeCapability(Status.EMBER_MODE);
        }
    }
}
