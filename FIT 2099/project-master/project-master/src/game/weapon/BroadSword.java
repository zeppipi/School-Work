package game.weapon;

import game.enums.Passives;

/**
 * Class that represents the Broadsword weapon
 *
 * @author Yap Choon Seong
 * @version 1
 * @see GameWeaponItem
 */
public class BroadSword extends GameWeaponItem {

    /**
     * Constructor.
     */
    public BroadSword() {
        super("Broad Sword", 'B', 30, "hits" , 80, 500);
        this.addCapability(Passives.CRITICAL_STRIKE);
    }

}
