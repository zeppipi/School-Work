package game.weapon;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.PickUpItemAction;
import game.action.weaponAction.ChargeWeaponAction;
import game.action.SwapWeaponAction;
import game.enums.Abilities;
import game.enums.Passives;
import game.enums.Status;

import java.util.List;

/**
 * Class that represents the storm ruler weapon
 *
 * @author Yap Choon Seong
 * @version 1
 * @see GameWeaponItem
 */
public class StormRuler extends GameWeaponItem {

    private int charge;
    /**
     * Constructor.
     *
     */
    public StormRuler() {
        super("Storm Ruler", '7', 70, "hits", 60, 2000);
        this.addCapability(Passives.CRITICAL_STRIKE);
        this.addCapability(Passives.DULLNESS);
        this.allowableActions.add(new ChargeWeaponAction(this));
    }

    @Override
    public List<Action> getAllowableActions() {
        // if fully charged, remove all allowable action
        if (this.hasCapability(Status.FULLY_CHARGED)) {
            this.allowableActions.clear();
        } else {
            // if not fully charged, allow it to charge
            this.allowableActions.clear();
            this.allowableActions.add(new ChargeWeaponAction(this));
        }
        return super.getAllowableActions();
    }



    @Override
    public void tick(Location currentLocation, Actor actor) {
        super.tick(currentLocation, actor);
        // if the player charge the storm ruler, increase the charge
        if (this.hasCapability(Abilities.CHARGE)) {
            this.charge += 1;
            this.removeCapability(Abilities.CHARGE);
            // set the status to charging
            if (!this.hasCapability(Status.CHARGING)) {
                this.addCapability(Status.CHARGING);
            }
            // if storm ruler charged for 3 turns
            if (this.charge == 3) {
                this.charge = 0;
                this.removeCapability(Status.CHARGING);
                this.addCapability(Status.FULLY_CHARGED);
                actor.addCapability(Abilities.WIND_SLASH);
            }
        }
    }

    @Override
    public String toString() {
        if (this.hasCapability(Status.CHARGING)) {
            return this.name + "(Charging)";
        } else if (this.hasCapability(Status.FULLY_CHARGED)) {
            return this.name + "(Fully Charge)";
        } else {
            return super.toString();
        }
    }

    @Override
    public PickUpItemAction getPickUpAction(Actor actor) {
        return new SwapWeaponAction(this);
    }
}
