package game.action;

import edu.monash.fit2099.engine.*;
import game.interfaces.Soul;
import game.Items.token.TokenOfSouls;

/**
 * An action to drop 1, 2, or 3 token of souls with 100 souls in them
 * @author Hazael Frans Chrsitian
 * @version 1
 */
public class ChestDropTokenAction extends Action implements Soul {
    private final int souls = 100;

    @Override
    public String execute(Actor actor, GameMap map) {
        int dice = (int)(Math.random() * 3 + 1);    //give 1, 2, or 3
        Location here = map.locationOf(actor);

        for(int i = dice; i > 0; i--){
            TokenOfSouls newToken = new TokenOfSouls();
            this.transferSouls(newToken);
            here.addItem(newToken);
        }

        return "Token of Souls were littered on the floor";
    }

    @Override
    public String menuDescription(Actor actor) {
        return null;
    }

    @Override
    public void transferSouls(Soul soulObject) {
        soulObject.addSouls(souls);
    }

    @Override
    public int getSouls() {
        return souls;
    }
}
