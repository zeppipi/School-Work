package game.weapon;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import game.action.EmberAction;
import game.enums.Abilities;
import game.enums.Status;

/**
 * Class that represents Yhorm the Giant's weapon
 *
 * @author Yap Choon Seong
 * @version 1
 * @see GameWeaponItem
 */
public class YhormGreatMachete extends GameWeaponItem {
    /**
     * Constructor.
     *
     */
    public YhormGreatMachete() {
        super("Yhorm’s Great Machete", 'M', 95, "strike" , 60, 0);
        this.addCapability(Abilities.YHORM_WEAPON);
        this.allowableActions.add(new EmberAction());
    }

    @Override
    public void tick(Location currentLocation, Actor actor) {
        // if holder in ember form, activate rage mode of machete
        if (actor.hasCapability(Status.EMBER_MODE)) {
            this.addCapability(Status.EMBER_MODE);
        } else {
            // if holder not in ember form anymore, deactivate rage mode
            if (this.hasCapability(Status.EMBER_MODE)) {
                this.removeCapability(Status.EMBER_MODE);
            }
        }
        super.tick(currentLocation, actor);
    }
}
