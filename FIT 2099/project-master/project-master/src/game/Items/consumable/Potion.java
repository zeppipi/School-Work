package game.Items.consumable;

import edu.monash.fit2099.engine.Item;
import game.action.ConsumeAction;
import game.enums.Status;
import game.character.player.Player;

/**
 * Class representing items that can be used as a potion.
 * Is a consumable.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see edu.monash.fit2099.engine.Item
 */
public abstract class Potion extends Item {

    /**
     * Player who possess this potion.
     */
    protected Player player;

    /***
     * Constructor.
     * @param name the name of this Item
     * @param displayChar the character to use to represent this item if it is on the ground
     * @param portable true if and only if the Item can be picked up
     */
    public Potion(String name, char displayChar, boolean portable, Player player) {
        super(name, displayChar, portable);
        this.player = player;
        this.addCapability(Status.DRINKABLE);
        this.allowableActions.add(new ConsumeAction(this));
    }

    /**
     * Consume the potion.
     */
    public boolean consume() {
        return false;
    }

    /**
     * return name of the potion
     */
    public String getName() {
        return this.name;
    }
}