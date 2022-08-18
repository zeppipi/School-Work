package game.Items.token;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.PickUpItemAction;
import game.enums.Abilities;
import game.interfaces.Soul;

/**
 * Special Pick up action which could only be triggered by token of souls
 * The item will not be stored into actor's inventory, instead, it will transfer soul
 * to the actor instantly after picking up
 *
 * @author Shin Yung Xin
 * @version 1
 * @see PickUpItemAction
 * @see TokenOfSouls
 */
public class PickUpTokenAction extends PickUpItemAction {
    /**
     * Constructor.
     *
     * @param item the item to pick up
     */
    public PickUpTokenAction(Item item) {
        super(item);
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        map.locationOf(actor).removeItem(item);
        // transfer soul in item into actor if it is retrievable
        if (item.hasCapability(Abilities.RETRIEVABLE)) {
            Soul soulItem = item.asSoul();
            soulItem.transferSouls(actor.asSoul());
        } else {
            throw new IllegalArgumentException("Non retrievable item could not be picked up through this action");
        }
        return menuDescription(actor);
    }
}
