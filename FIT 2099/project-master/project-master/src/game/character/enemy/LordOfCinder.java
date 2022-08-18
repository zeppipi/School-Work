package game.character.enemy;

import edu.monash.fit2099.engine.Location;

/**
 * The boss of Design o' Souls
 * FIXME: This boss is Boring. It does nothing. You need to implement features here.
 * TODO: Could it be an abstract class? If so, why and how?
 * Lord of Cinder could be an abstract class, because lord of cinder isn't a singular boss but
 * the name of a group of bosses. It can be an abstract the same way the Enemies class is an abstract class
 *
 * @editor Hazael Frans Christian
 * @version 2
 * @see Enemies
 */
public abstract class LordOfCinder extends Enemies {

    /**
     * Constructor.
     */
    public LordOfCinder(String name, char displayChar, int hitPoints, int souls, Location location) {
        super(name, displayChar, hitPoints, souls, location);
    }

}
