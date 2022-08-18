package game.enums;

/**
 * Enum that represents an ability of Actor, Item, or Ground.
 */
public enum Abilities {
    REST, // can rest
    RESET, // can call a reset
    RETRIEVABLE, // can retrieve the soul
    INSTA_KILL, // something that instantly kills the player
    TRADABLE, // can be trade
    WIND_SLASH, // actor can perform wind slash
    REVIVE, // can revive
    BURNABLE, // ground that can be burned
    CHARGE, // charge the weapon
    SPIN_ATTACK, // can perform spin attack
    ENTER_FLOOR, // can enter floor
    ENTER_VALLEY, // can enter valley
    GIVE_TOKENS, //can spew out tokens
    MIMIC, // can become mimic
    YHORM_WEAPON, // only Yhorm The Giant can equip the weapon
    ALDRICH_WEAPON, // only Aldrich The Devourer can equip the weapon
    TRADE_FOR_YHORM_WEAPON,  // can be traded for Yhorm The Giant's weapon
    TRADE_FOR_ALDRICH_WEAPON  // can be traded for Aldrich The Devourer's weapon
}
