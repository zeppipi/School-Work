package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import game.enums.Abilities;
import game.enums.Status;
import game.interfaces.Soul;
import game.weapon.GameWeaponItem;

/**
 * Class that gives an actor the ability to trade souls for weapons
 *
 * @author Yap Choon Seong
 * @version 1
 * @see Action
 */
public class TradeAction extends Action {

    protected GameWeaponItem weapon;
    protected Actor actor;
    protected int price;

    /**
     * Constructor
     *
     * @param weapon weapon to be traded
     */
    public TradeAction(GameWeaponItem weapon) {
        this.weapon = weapon;
    }

    /**
     * Constructor
     *
     * @param actor actor that perform trade
     */
    public TradeAction(Actor actor, int price) {
        this.actor = actor;
        this.price = price;
    }

    private void swapWeapon(Actor actor, GameMap map){
        SwapWeaponAction swapWeapon = new SwapWeaponAction(weapon);
        swapWeapon.execute(actor, map);
    }

    private void increaseMaxHp(int souls_to_upgrade){
        int increaseHP = 25;
        actor.asSoul().subtractSouls(souls_to_upgrade);
        actor.increaseMaxHp(increaseHP);
        actor.addCapability(Status.INCREASED_MAX_HP);
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        Soul player = actor.asSoul();
        int player_souls = player.getSouls();
        if (actor.hasCapability(Abilities.TRADABLE))
            if (weapon != null) {
                if (weapon.hasCapability(Abilities.YHORM_WEAPON)) {
                    for (Item item : actor.getInventory()) {
                        if (item.hasCapability(Abilities.TRADE_FOR_YHORM_WEAPON)) {
                            actor.removeItemFromInventory(item);
                            swapWeapon(actor, map);
                            return actor + " trade Yhorm's " + item + " for " + weapon.toString();
                        }
                    }
                    return actor + " don't have Cinder of Lord to trade for " + weapon.toString();
                } else if (weapon.hasCapability(Abilities.ALDRICH_WEAPON)) {
                    for (Item item : actor.getInventory()) {
                        if (item.hasCapability(Abilities.TRADE_FOR_ALDRICH_WEAPON)) {
                            actor.removeItemFromInventory(item);
                            swapWeapon(actor, map);
                            return actor + " trade Aldrich's " + item + " for " + weapon.toString();
                        }
                    }
                    return actor + " don't have Cinder of Lord to trade for " + weapon.toString();
                } else {
                    if (player_souls >= weapon.getPrice()) {
                        actor.asSoul().subtractSouls(weapon.getPrice());
                        swapWeapon(actor, map);
                        return actor + " bought " + weapon.toString() + " (" + weapon.getPrice() + " Souls)";
                    } else {
                        return actor + " don't have enough Souls to buy " + weapon.toString() + " (" + weapon.getPrice() + " Souls)";
                    }
                }
            } else {
                if (player_souls >= price) {
                    increaseMaxHp(price);
                    return actor + "'s max Hp is increased by 25";
                } else {
                    return actor + " don't have enough Souls for selected trade";
                }
            }
        else {
            return actor + " is not a player, trading is not allowed";
        }
    }

    @Override
    public String menuDescription(Actor actor) {
        if (weapon != null) {
            if (weapon.hasCapability(Abilities.YHORM_WEAPON)) {
                return actor + " buys " + weapon + " (1x Cinder Of Yhorm The Giant)";
            } else if (weapon.hasCapability(Abilities.ALDRICH_WEAPON)) {
                return actor + " buys " + weapon + " (1x Cinder Of Aldrich The Devourer)";
            } else {
                return actor + " buys " + weapon + " (" + weapon.getPrice() + " Souls)";
            }
        } else {
            return "Increase " + actor + "’s maximum hit points by 25";
        }
    }
}
