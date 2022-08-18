package game.action.attackAction;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;
import game.enums.Abilities;
import game.enums.Status;

/**
 * Special Attack action for attacking other Actors by performing wind slash.
 * Can only be performed on Yhorm the Giant.
 *
 * @author Yap Choon Seong
 * @version 1
 * @see AttackAction
 */
public class WindSlashAction extends AttackAction {
    /**
     * Constructor.
     *
     * @param target    the Actor to attack
     * @param direction the direction of target
     */
    public WindSlashAction(Actor target, String direction) {
        super(target, direction);
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        Weapon weapon = actor.getWeapon();

        // calculate damage & attack
        int damage = weapon.damage() * 2;
        target.hurt(damage);

        // remove weapon's & actor's capability
        for (Item item : actor.getInventory()) {
            if (item.asWeapon() != null) {
                item.removeCapability(Status.FULLY_CHARGED);
            }
        }
        actor.removeCapability(Abilities.WIND_SLASH);

        if (target.isConscious()) {
            // stun it
            target.addCapability(Status.STUN);
            return actor + " slashes " + target + " with the power of wind!";
        } else {
            // if target dead
            String result = "as the final slash lands, Yhorm the Giant crumbles into fine ashes";
            result += System.lineSeparator() + "the remains of the giant has returned to earth...";
            return result;
        }
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " slashes the " + target + " at " + direction;
    }
}
