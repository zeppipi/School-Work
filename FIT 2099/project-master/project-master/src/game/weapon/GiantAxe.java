package game.weapon;

import game.action.weaponAction.SpinAttackAction;
import game.enums.Abilities;

/**
 * Class that represents a Giant Axe
 *
 * @author Yap Choon Seong
 * @version 1
 * @see GameWeaponItem
 */
public class GiantAxe extends GameWeaponItem {
    /**
     * Constructor.
     */
    public GiantAxe() {
        super("Giant Axe", 'A', 50, "strike", 80, 1000);
        this.addCapability(Abilities.SPIN_ATTACK);
        this.allowableActions.add(new SpinAttackAction(this));
    }
}
