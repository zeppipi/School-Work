package game.Items.token;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.PickUpItemAction;
import game.enums.Abilities;
import game.enums.Status;
import game.interfaces.Soul;

/**
 * A class that represent the Token of Soul.
 * Managed by Token Manager.
 * The program can hold at most 2 Token at a time, where one should be removed before placing another one.
 * It can only be created, drop and picked up by the player.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see edu.monash.fit2099.engine.Item
 * @see Soul
 */
public class TokenOfSouls extends Item implements Soul{

    protected int soul;

    /**
     * Constructor.
     */
    public TokenOfSouls() {
        super("Token of Souls", '$', true);
        //this.updated = true;
        this.addCapability(Abilities.RETRIEVABLE);
        this.addCapability(Status.UPDATED);
    }

    @Override
    public boolean addSouls(int souls) {
        this.soul += souls ;
        return true;
    }

    @Override
    public int getSouls() {
        return this.soul;
    }

    @Override
    public void transferSouls(Soul soulObject) {
        soulObject.addSouls(this.soul);
        this.soul = 0;
    }

    @Override
    public void tick(Location currentLocation) {
        // remove from game map if the token is no longer updated
        if (!this.hasCapability(Status.UPDATED)) {
            currentLocation.removeItem(this);
        }
        super.tick(currentLocation);
    }

    @Override
    public PickUpItemAction getPickUpAction(Actor actor) {
        if(portable && this.hasCapability(Status.UPDATED))
            return new PickUpTokenAction(this);

        return null;
    }


}
