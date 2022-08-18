package game.terrain;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;
import game.character.enemy.Undead;

import java.util.Random;

/**
 * A class representing a Cemetery
 *
 * @editor Hazael Frans Christian
 * @version 2
 * @see Ground
 */
public class Cemetery extends Ground {

    /**
     * A random generator
     */
    Random random = new Random();

    /**
     * Empty constructor to initialize Cemetery's symbol on the map
     */
    public Cemetery(){
        super('C');
    }

    /**
     * Tick function overridden from the ground class, to let the cemetery experience time.
     * With this, the 25% chance of spawning an undead every turn can be made
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location){
        //4 sided die
        int number = 1;
        //random bounds = random.nextInt(max - nim) + min
        int dice = random.nextInt(5-1) + 1;

        if(number == dice && !(location.containsAnActor())){
            location.addActor(new Undead("Undead"));
        }
    }

}
