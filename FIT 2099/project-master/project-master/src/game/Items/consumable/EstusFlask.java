
package game.Items.consumable;

import game.character.player.Player;
import game.enums.Status;
import game.interfaces.Resettable;

/**
 * A class that represent the healing potion named Estus Flask.
 * it can only be carried by the player.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see edu.monash.fit2099.engine.Item
 * @see Potion
 * @see Resettable
 */
public class EstusFlask extends Potion implements Resettable {

    private final int maxCharge = 3;
    private int charge;

    /**
     * Constructor.
     */
    public EstusFlask(Player player) {
        super("Estus Flask", 'E', false, player);
        this.addCapability(Status.REFILLABLE);
        this.charge = maxCharge;
        registerInstance();
    }

    @Override
    public boolean consume() {
        if (this.charge > 0) {
            // heal the Player with 40% of the maximum hit points
            int heal_hp = (int) Math.round(this.player.getMaxHitPoint() * 0.4);
            player.heal(heal_hp);
            this.charge -= 1;
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Estus Flask charges: " + this.charge + "/" + this.maxCharge;
    }

    @Override
    public void resetInstance() {
        // refill estus flask
        this.charge = this.maxCharge;
    }

    @Override
    public boolean isExist() {
        return true;
    }

}