package game.terrain;

import edu.monash.fit2099.engine.Location;
import game.enums.LastBonfire;
import game.enums.Status;

/**
 * A class that represents the Anor Londo's Bonfire.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see Bonfire
 */
public class AnorLondoBonfire extends Bonfire{

    /**
     * Constructor
     */
    public AnorLondoBonfire(Location position) {
        super("Anor Londo", LastBonfire.ANOR_LONDO, position);
        this.addCapability(Status.NOT_ACTIVATED);
    }
}
