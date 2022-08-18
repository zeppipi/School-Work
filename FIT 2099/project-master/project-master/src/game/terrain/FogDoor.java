package game.terrain;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Item;

/**
 * A class that represents Fog Door.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see Item
 */
public class FogDoor extends Item {

    /***
     * Constructor.
     */
    public FogDoor() {
        super("Fog Door", '=', false);
    }

    public void addAction(Action action) {
        this.allowableActions.add(action);
    }
}
