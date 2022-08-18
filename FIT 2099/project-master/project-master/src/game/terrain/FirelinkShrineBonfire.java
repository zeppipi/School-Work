package game.terrain;


import edu.monash.fit2099.engine.Location;
import game.enums.LastBonfire;

/**
 * A class that represents the Firelink Shrine's Bonfire.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see Bonfire
 */
public class FirelinkShrineBonfire extends Bonfire{

    /**
     * Constructor
     */
    public FirelinkShrineBonfire(Location position) {
        super("Firelink Shrine", LastBonfire.FIRELINK_SHRINE, position);
    }
}
