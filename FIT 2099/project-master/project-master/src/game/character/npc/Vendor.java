package game.character.npc;

import edu.monash.fit2099.engine.*;
import game.action.TradeAction;
import game.enums.Abilities;
import game.enums.Status;
import game.interfaces.Behaviour;
import game.interfaces.Soul;
import game.weapon.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a vendor
 *
 * @author Yap Choon Seong
 * @version 1
 * @see Actor
 * @see TradeAction
 */
public class Vendor extends Actor implements Soul{
    protected int soul;
    private List<Behaviour> behaviours = new ArrayList<>();

    /**
     * Constructor
     *
     * @param name the name of the vendor
     */
    public Vendor(String name) {
        super(name, 'V', 99999999);
        this.soul = 0;
    }

    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        for (Behaviour behaviour : behaviours) {
            Action action = behaviour.getAction(this, map);
            if(action != null)
                return action;
        }
        return new DoNothingAction();
    }

    @Override
    public Actions getAllowableActions(Actor actor, String direction, GameMap map) {
        Actions actions = super.getAllowableActions(actor, direction, map);
        actions.add(new TradeAction(new BroadSword()));
        actions.add(new TradeAction(new GiantAxe()));
        for (Item item : actor.getInventory()) {
            if (item.hasCapability(Abilities.TRADE_FOR_YHORM_WEAPON)) {
                actions.add(new TradeAction(new YhormGreatMachete()));
            }
            if (item.hasCapability(Abilities.TRADE_FOR_ALDRICH_WEAPON)) {
                actions.add(new TradeAction(new DarkmoonLongbow()));
            }
        }
        // only allow the player to increase max hp if it has not done it before
        if (!actor.hasCapability(Status.INCREASED_MAX_HP)) {
            actions.add(new TradeAction(actor, 200));
        }
        return actions;
    }

    @Override
    public void transferSouls(Soul soulObject) {
        soulObject.addSouls(this.soul);
    }

    @Override
    public boolean addSouls(int souls) {
        this.soul += souls ;
        return true;
    }

    @Override
    public int getSouls() {
        return 0;
    }

}
