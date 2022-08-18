package game.Items;

import edu.monash.fit2099.engine.Item;
import game.enums.Abilities;

public class CinderOfLord extends Item {
    public CinderOfLord(Abilities abilities) {
        super("Cinder Of Lord", 'L', true);
        this.addCapability(abilities);
    }
}
