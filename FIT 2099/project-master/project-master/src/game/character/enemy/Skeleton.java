package game.character.enemy;

import edu.monash.fit2099.engine.*;
import game.behaviours.WanderBehaviour;
import game.enums.Abilities;
import game.enums.Status;
import game.interfaces.Resettable;
import game.weapon.BroadSword;
import game.weapon.GiantAxe;

import java.util.Random;

/**
 * A class representing a skeleton
 *
 * @author Hazael Frans Christian
 * @version 1
 * @see Enemies
 */
public class Skeleton extends Enemies implements Resettable {

    /**
     * A random generator
     */
    private Random random = new Random();
    // Will need to change this to a collection if Skeleton gets additional Behaviours.

    /**
     * Constructor.
     * All Skeletons are represented by an 'S' and have 100 hitpoints
     */
    public Skeleton(Location location) {
        super("Skeleton", 'S', 100, 250, location);//100
        behaviours.add(new WanderBehaviour());
        addCapability(Abilities.REVIVE);
        //gives skeleton a random starting weapon (broadsword or giant axe)
        int mode = (int) ( Math.random() * 2 + 1); // will return either 1 or 2
        if(mode == 1){
            this.addItemToInventory(new BroadSword());
        }
        else if(mode == 2){
            this.addItemToInventory(new GiantAxe());
        }
    }

    /**
     * Override Enemies' playTurn for the 50% chance to revive
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        // always prioritise reposition action
        if (!this.hasCapability(Status.TO_BE_REPOSITIONED)) {
            // allow skeleton to randomly perform spin attack (if applicable)
            for(Item item: this.getInventory()){
                if(item.hasCapability(Abilities.SPIN_ATTACK)) {
                    if (!(random.nextInt(100) <= 20)) {
                        return item.getAllowableActions().get(0);
                    }
                }
            }
        }

        return super.playTurn(actions, lastAction, map, display);
    }

    @Override
    public void resetInstance() {
        super.resetInstance();
        // keep wander behaviour
        this.behaviours.add(new WanderBehaviour());
    }

    @Override
    public boolean isExist(){
        return this.isConscious();
    }

}
