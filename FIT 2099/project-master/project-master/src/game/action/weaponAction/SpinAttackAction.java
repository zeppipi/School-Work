package game.action.weaponAction;

import edu.monash.fit2099.engine.*;
import game.enums.Abilities;
import game.enums.Status;

import java.util.Random;

/**
 * Class that gives a weapon the ability to so a spin attack
 *
 * @author Yap Choon Seong
 * @version 1
 * @see WeaponAction
 */
public class SpinAttackAction extends WeaponAction {
    /**
     * Random number generator
     */
    protected Random rand = new Random();

    /**
     * Constructor
     *
     * @param weaponItem the weapon item that has capabilities
     */
    public SpinAttackAction(WeaponItem weaponItem) {
        super(weaponItem);
        this.weapon = weaponItem;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        StringBuilder result = new StringBuilder(actor + " performed spin attack");
        int damage = weapon.damage()/2;
        for (Exit exit : map.locationOf(actor).getExits()) {
            Location targetLocation = exit.getDestination();

            if (targetLocation.containsAnActor() && targetLocation.getActor().hasCapability(Status.VULNERABLE)) {
                // check if the attacker missed the attack
                if (!(rand.nextInt(100) <= weapon.chanceToHit())) {
                    result.append(actor).append(" misses ").append(targetLocation.getActor().toString()).append(".");
                }
                else {
                    // attacker attacks
                    targetLocation.getActor().hurt(damage);
                    result.append(System.lineSeparator()).append(actor).append(" ").append(weapon.verb()).append(" ").append(targetLocation.getActor().toString()).append(" for ").append(damage).append(" damage.");

                    // if target dies
                    if (!targetLocation.getActor().isConscious()) {
                        Actions dropActions = new Actions();
                        // drop all items
                        for (Item item : targetLocation.getActor().getInventory())
                            dropActions.add(item.getDropAction(actor));
                        for (Action drop : dropActions)
                            drop.execute(targetLocation.getActor(), map);

                        // if the actor is player, reset game
                        if (targetLocation.getActor().hasCapability(Abilities.RESET)) {
                            result.append(System.lineSeparator()).append(targetLocation.getActor()).append(" is killed, the game will be reset");
                        } else if (targetLocation.getActor().hasCapability(Status.IS_BOSS)) {
                            // if actor is yhorm, output different message
                            result.append(System.lineSeparator()).append(targetLocation.getActor()).append(" is killed, the player wins!");
                        } else if (actor.hasCapability(Status.HOSTILE_TO_ENEMY)) {
                            // if attacker is player, it gains souls and remove dead actor
                            targetLocation.getActor().asSoul().transferSouls(actor.asSoul());
                            result.append(System.lineSeparator()).append(targetLocation.getActor().toString()).append(" is killed.");
                            map.removeActor(targetLocation.getActor());
                        } else {
                            // remove dead player
                            result.append(System.lineSeparator()).append(targetLocation.getActor().toString()).append(" is killed.");
                            map.removeActor(targetLocation.getActor());
                        }
                    }
                }
            }
        }
        return result.toString();
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " performs Spin Attack";
    }
}
