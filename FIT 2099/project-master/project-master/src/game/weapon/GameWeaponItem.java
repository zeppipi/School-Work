package game.weapon;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.DropItemAction;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * A class that represents weapon item in the game
 *
 * @editor Yap Choon Seong
 * @version 2
 * @see WeaponItem
 */
public class GameWeaponItem extends WeaponItem {

    /**
     * Price of the weapon item
     */
    protected int price;

    /**
     * Constructor.
     *
     * @param name        name of the item
     * @param displayChar character to use for display when item is on the ground
     * @param damage      amount of damage this weapon does
     * @param verb        verb to use for this weapon, e.g. "hits", "zaps"
     * @param hitRate     the probability/chance to hit the target.
     */
    public GameWeaponItem(String name, char displayChar, int damage, String verb, int hitRate, int price) {
        super(name, displayChar, damage, verb, hitRate);
        this.price = price;
    }

    /**
     * getter of price attribute
     * @return the price of the weapon item
     */
    public int getPrice(){
        return this.price;
    }

    /**
     * In this game,
     * @param actor an actor that will interact with this item
     * @return null because
     */
    @Override
    public DropItemAction getDropAction(Actor actor) {
        return null;
    }
}
