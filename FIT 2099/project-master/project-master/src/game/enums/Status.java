package game.enums;

/**
 * Use this enum class to give `buff` or `debuff`.
 * It is also useful to give a `state` to abilities or actions that can be attached-detached.
 */
public enum Status {
    HOSTILE_TO_ENEMY, // use this capability to be hostile towards something (e.g., to be attacked by enemy)
    TO_BE_REPOSITIONED, // sense if the actor needs to be repositioned
    UPDATED, // status of token of souls
    ATTACKED, // signalizing being under attacked
    STUN, //actor is stunned
    EMBER_MODE, //actor under ember form
    IS_BOSS, // sign that says "this actor is a boss"
    REVIVED, //actor has revived before
    CHARGING, // represents a charging weapon
    FLAMES, // represents a burning ground
    DRINKABLE, // can call ConsumeAction
    REFILLABLE, // can be refilled
    VULNERABLE, // can be attacked
    FULLY_CHARGED, // represents a fully charged weapon
    INCREASED_MAX_HP, // actor has already increased its max hp
    NOT_ACTIVATED, // bonfire has not been activated yet
    OPENED,  // an opened chest
    MISSED // actor missed the attack
}
