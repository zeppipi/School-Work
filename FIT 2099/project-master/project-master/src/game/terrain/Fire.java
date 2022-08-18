package game.terrain;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;
import game.enums.Status;

/**
 * A method that represents fire on the map, represented with the letter V
 *
 * @author Hazael Frans Christian
 * @version 1
 * @see Ground
 */
public class Fire extends Ground {
    private int lifeTime;

    /**
     * Constructor.
     */
    public Fire() {
        super('V');
        this.lifeTime = 3;
        this.addCapability(Status.FLAMES);
    }

    /**
     * tick used to keep track of this ground's lifetime, which is 3 ticks,
     * then after the 3 ticks, turn this tile into dirt.
     * Also check if the player is standing on this, and if they are, hurt them for 25
     *
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location){
        // burn for 3 rounds
        if(lifeTime <= 0){
            location.setGround(new Dirt());
        }

        // hurt the actor stepping on it
        if(location.containsAnActor() && location.getActor().hasCapability(Status.HOSTILE_TO_ENEMY)){
            location.getActor().hurt(25);
        }

        lifeTime -= 1;
    }

}
