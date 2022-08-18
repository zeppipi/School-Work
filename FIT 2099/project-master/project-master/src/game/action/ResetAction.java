package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import game.enums.Abilities;
import game.enums.Status;
import game.reset.ResetManager;
import game.Items.token.TokenOfSouls;

/**
 * Special action to reset the game.
 * Can only be called and created by the player when it dies.
 *
 * @author Shin Yung Xin
 * @version 2
 * @see Action
 */
public class ResetAction extends Action {

    private final TokenOfSouls token;
    /**
     * token location
     */
    private final Location tokenLocation;
    /**
     * respawn location of player
     */
    private final Location respawnLocation;


    /**
     * Constructor
     * @param tokenLocation location where the player drops its token
     * @param respawnLocation location where the player is to be respawned
     */
    public ResetAction(Location tokenLocation, Location respawnLocation, TokenOfSouls newToken) {
        this.tokenLocation = tokenLocation;
        this.respawnLocation = respawnLocation;
        this.token = newToken;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        // only actor with RESET capability can call this method
        if (!actor.hasCapability(Abilities.RESET)) {
            return null;
        }

        // message to be sent out
        String result = "";

        // if player is not conscious
        if (!actor.isConscious()) {
            // display cool message in console
            result += """

                                                                                                  \s
                                                                                                  \s
                    ____     ___    ____    ____     ___      ________   ______________ ________  \s
                    `MM(     )M'   6MMMMb   `MM'     `M'      `MMMMMMMb. `MM'`MMMMMMMMM `MMMMMMMb.\s
                     `MM.    d'   8P    Y8   MM       M        MM    `Mb  MM  MM      \\  MM    `Mb\s
                      `MM.  d'   6M      Mb  MM       M        MM     MM  MM  MM         MM     MM\s
                       `MM d'    MM      MM  MM       M        MM     MM  MM  MM    ,    MM     MM\s
                        `MM'     MM      MM  MM       M        MM     MM  MM  MMMMMMM    MM     MM\s
                         MM      MM      MM  MM       M        MM     MM  MM  MM    `    MM     MM\s
                         MM      MM      MM  MM       M        MM     MM  MM  MM         MM     MM\s
                         MM      YM      M9  YM       M        MM     MM  MM  MM         MM     MM\s
                         MM       8b    d8    8b     d8        MM    .M9  MM  MM      /  MM    .M9\s
                        _MM_       YMMMM9      YMMMMM9        _MMMMMMM9' _MM__MMMMMMMMM _MMMMMMM9'\s
                                                                                                  \s
                                                                                                  \s
                                                                                                  \s
                    """;

            // display the cause of death
            if(map.locationOf(actor).getGround().hasCapability(Abilities.INSTA_KILL)){
                // dies from valley
                result += "The " + actor+ " has fell down into a valley";
            }else if(map.locationOf(actor).getGround().hasCapability(Status.FLAMES)){
                // dies from fire
                result += "The " + actor + " burned to death";
            }

            // drop token of soul
            this.tokenLocation.addItem(token);

            // move actor back to last interacted bonfire
            map.moveActor(actor, this.respawnLocation);

        } else {  // nothing happened to the actor, but reset was called
            result += "The " + actor + " took a good rest";
        }

        // reset all resettable instances
        ResetManager resetManager = ResetManager.getInstance();
        resetManager.run();

        // return string
        return result;
    }

    @Override
    public String menuDescription(Actor actor) {
        return null;
    }
}
