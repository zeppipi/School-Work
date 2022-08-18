package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.enums.Status;

/**
 * An Action that announce the death of an actor.
 *
 * @author Shin Yung Xin
 * @version 1
 * @see Action
 */
public class DieAction extends Action {

    public DieAction() {
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        // remove dead actor from map
        map.removeActor(actor);

        // if the boss died, output special message
        if (actor.hasCapability(Status.IS_BOSS)) {
            return """

                                                                                                                                                                                                                                                                                                   \s
                                                                                                                                                                                                                                                                                                   \s
                    __________ ____    _____________       ____       ____   ________   ________            ____   ________          ____   ______      ___________  __________ ________          ____    ____      _        ____         ________   _     ____     ____     __________ ___      ___
                    MMMMMMMMMM `MM'    `MM`MMMMMMMMM       `MM'      6MMMMb  `MMMMMMMb. `MMMMMMMb.         6MMMMb  `MMMMMMM         6MMMMb/ `MM`MM\\     `M'`MMMMMMMb.`MMMMMMMMM `MMMMMMMb.        `MM'    `MM'     dM.      6MMMMb\\       `MMMMMMM  dM.    `MM'     `MM'     `MMMMMMMMM `MM\\     `M'
                    /   MM   \\  MM      MM MM      \\        MM      8P    Y8  MM    `Mb  MM    `Mb        8P    Y8  MM    \\        8P    YM  MM MMM\\     M  MM    `Mb MM      \\  MM    `Mb         MM      MM     ,MMb     6M'    `        MM    \\ ,MMb     MM       MM       MM      \\  MMM\\     M\s
                        MM      MM      MM MM               MM     6M      Mb MM     MM  MM     MM       6M      Mb MM            6M      Y  MM M\\MM\\    M  MM     MM MM         MM     MM         MM      MM     d'YM.    MM              MM      d'YM.    MM       MM       MM         M\\MM\\    M\s
                        MM      MM      MM MM    ,          MM     MM      MM MM     MM  MM     MM       MM      MM MM   ,        MM         MM M \\MM\\   M  MM     MM MM    ,    MM     MM         MM      MM    ,P `Mb    YM.             MM   , ,P `Mb    MM       MM       MM    ,    M \\MM\\   M\s
                        MM      MMMMMMMMMM MMMMMMM          MM     MM      MM MM    .M9  MM     MM       MM      MM MMMMMM        MM         MM M  \\MM\\  M  MM     MM MMMMMMM    MM    .M9         MMMMMMMMMM    d'  YM.    YMMMMb         MMMMMM d'  YM.   MM       MM       MMMMMMM    M  \\MM\\  M\s
                        MM      MM      MM MM    `          MM     MM      MM MMMMMMM9'  MM     MM       MM      MM MM   `        MM         MM M   \\MM\\ M  MM     MM MM    `    MMMMMMM9'         MM      MM   ,P   `Mb        `Mb        MM   `,P   `Mb   MM       MM       MM    `    M   \\MM\\ M\s
                        MM      MM      MM MM               MM     MM      MM MM  \\M\\    MM     MM       MM      MM MM            MM         MM M    \\MM\\M  MM     MM MM         MM  \\M\\           MM      MM   d'    YM.        MM        MM    d'    YM.  MM       MM       MM         M    \\MM\\M\s
                        MM      MM      MM MM               MM     YM      M9 MM   \\M\\   MM     MM       YM      M9 MM            YM      6  MM M     \\MMM  MM     MM MM         MM   \\M\\          MM      MM  ,MMMMMMMMb        MM        MM   ,MMMMMMMMb  MM       MM       MM         M     \\MMM\s
                        MM      MM      MM MM      /        MM    / 8b    d8  MM    \\M\\  MM    .M9        8b    d8  MM             8b    d9  MM M      \\MM  MM    .M9 MM      /  MM    \\M\\         MM      MM  d'      YM. L    ,M9        MM   d'      YM. MM    /  MM    /  MM      /  M      \\MM\s
                       _MM_    _MM_    _MM_MMMMMMMMM       _MMMMMMM  YMMMM9  _MM_    \\M\\_MMMMMMM9'         YMMMM9  _MM_             YMMMM9  _MM_M_      \\M _MMMMMMM9'_MMMMMMMMM _MM_    \\M\\_      _MM_    _MM_dM_     _dMM_MYMMMM9        _MM__dM_     _dMM_MMMMMMM _MMMMMMM _MMMMMMMMM _M_      \\M\s
                                                                                                                                                                                                                                                                                                   \s
                                                                                                                                                                                                                                                                                                   \s
                                                                                                                                                                                                                                                                                                   \s
                    """;
        }
        return menuDescription(actor);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " dies";
    }

}