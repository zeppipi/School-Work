package game.action.weaponAction;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.WeaponAction;
import edu.monash.fit2099.engine.WeaponItem;
import game.enums.Abilities;
import game.enums.Status;

/**
 * Class that gives a weapon the ability to charge its attack
 *
 * @author Yap Choon Seong
 * @version 1
 * @see WeaponAction
 */
public class ChargeWeaponAction extends WeaponAction {

    /**
     * Constructor
     *
     * @param weaponItem the weapon item that has capabilities
     */
    public ChargeWeaponAction(WeaponItem weaponItem) {
        super(weaponItem);
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        this.weapon.addCapability(Abilities.CHARGE);
        // disarm actor
        if (actor.hasCapability(Status.HOSTILE_TO_ENEMY)) {
            actor.removeCapability(Status.HOSTILE_TO_ENEMY);
        }
        return actor + " charges the " + weapon.toString();
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " charges the " + weapon.toString();
    }
}
