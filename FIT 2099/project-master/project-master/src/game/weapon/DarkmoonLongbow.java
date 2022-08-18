package game.weapon;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.Location;
import game.enums.Abilities;
import game.enums.Passives;
import game.enums.Status;

/**
 * Class that represents the Darkmoon Longbow weapon
 *
 * @author Yap Choon Seong
 * @version 1
 * @see GameWeaponItem
 */
public class DarkmoonLongbow extends GameWeaponItem {
    public DarkmoonLongbow() {
        super("Darkmoon Longbow", 'D', 70, "shots", 100, 0);
        this.addCapability(Abilities.ALDRICH_WEAPON);
        this.addCapability(Passives.CRITICAL_STRIKE);
    }

    @Override
    public void tick(Location currentLocation, Actor actor) {
        //
        for (Exit exit1: currentLocation.getExits()) {
            for (Exit exit2: exit1.getDestination().getExits()) {
                for (Exit exit3: exit2.getDestination().getExits()) {
                    Location destination = exit3.getDestination();
                    if (destination.getActor() != null && destination.getActor().hasCapability(Status.HOSTILE_TO_ENEMY)) {
                        actor.addCapability(Passives.RANGED);
                    }
                }
            }
        }
        super.tick(currentLocation, actor);
    }
}
