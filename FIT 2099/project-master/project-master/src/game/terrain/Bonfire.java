package game.terrain;

import edu.monash.fit2099.engine.*;
import game.action.ActivateBonfireAction;
import game.action.RestAction;
import game.action.TeleportAction;
import game.enums.LastBonfire;
import game.enums.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class that represents Bonfire.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see Ground
 */
public abstract class Bonfire extends Ground {

    /**
     * Lists of available bonfire
     */
    public static List<Bonfire> bonfires;
    /**
     * Name of the bonfire
     */
    private final String name;
    /**
     * return position when teleporting from one bonfire to another
     */
    private final Location returnPosition;
    /**
     * Status of the last bonfire which the player interacted with
     */
    private final Enum<LastBonfire> status;

    /**
     * Constructor
     */
    public Bonfire(String name,Enum<LastBonfire> lastBonfireStatus, Location position) {
        super('B');
        this.name = name;
        this.returnPosition = position;
        this.status = lastBonfireStatus;

        if (bonfires == null) {
            bonfires = new ArrayList<>();
        }
        bonfires.add(this);
    }

    @Override
    public Actions allowableActions(Actor actor, Location location, String direction) {
        Actions actions = new Actions();
        if (this.hasCapability(Status.NOT_ACTIVATED)) {
            actions.add(new ActivateBonfireAction(this, this.status));
        } else {
            actions.add(new RestAction(this.name, this.status));
            // allow player to perform teleport
            for (Bonfire bonfire : bonfires) {
                if (this != bonfire) {
                    actions.add(new TeleportAction(bonfire.returnPosition, bonfire.toString()));
                }
            }
        }
        return actions;
    }

    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Get the respawn position of last bonfire.
     *
     * @param lastBonfireEnum the status of last bonfire
     * @return respawn location of last bonfire
     */
    public static Location lastBonfireLocation(Enum<LastBonfire> lastBonfireEnum) {
        for (Bonfire bonfire : bonfires) {
            if (bonfire.status == lastBonfireEnum) {
                return bonfire.returnPosition;
            }
        }
        return null;
    }
}

