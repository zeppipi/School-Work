package game;

import java.util.Arrays;
import java.util.List;

import edu.monash.fit2099.engine.*;
import game.character.enemy.AldrichTheDevourer;
import game.character.enemy.Mimic;
import game.character.enemy.Skeleton;
import game.character.enemy.YhormTheGiant;
import game.character.npc.Vendor;
import game.character.player.Player;
import game.terrain.*;
import game.weapon.StormRuler;

/**
 * The main class for the Design O Souls World game.
 *
 */
public class Application {
	public static void main(String[] args) {

			World world = new World(new Display());

			FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new Valley(),
					new Cemetery());

			List<String> map = Arrays.asList(
					"..++++++..+++...........................++++......+++.................+++.......",
					"........+++++..............................+++++++.................+++++........",
					"...........+++.......................................................+++++......",
					"........................................................................++......",
					".....................C...................................................+++....",
					"............................+.............................................+++...",
					".............................+++.......++++.....................................",
					".............................++.......+...............C......++++...............",
					".............................................................+++++++............",
					"..................................###___###...................+++...............",
					"..................................#_______#......................+++............",
					"...........++.....................#_______#.......................+.............",
					".........+++......................#_______#........................++...........",
					"............+++...................####_####..........................+..........",
					"..............+......................................................++.........",
					"..............++..........C......................................++++++.........",
					"............+++......................................C............++++..........",
					"+..................................................................++...........",
					"++...+++.........................................................++++...........",
					"+++......................................+++........................+.++........",
					"++++.......++++................C........++.........................+....++......",
					"#####___#####++++......................+...............................+..+.....",
					"_..._....._._#.++......................+...................................+....",
					"...+.__..+...#+++...........................................................+...",
					"...+.....+._.#.+.....+++++...++..............................................++.",
					"___.......___#.++++++++++++++.+++.............................................++");
			GameMap ProfaneCapital = new GameMap(groundFactory, map);
			world.addGameMap(ProfaneCapital);

			// 58 x 20
			List<String> map2 = Arrays.asList(
					".........+++..................#..................__.......",
					"...........++.................#....#.....#........_....._.",
					"......C.......................#+..............._._........",
					".............................._...._........._......#.....",
					"++.++........................._...._...#....#_...._..._...",
					"..............................#....#..............+.......",
					"...................####_####..############################",
					"......C............#_______#........C............+++......",
					".............+++...#_______#.......................+......",
					"...................#_______#..............................",
					"...................####_####.....................+........",
					".............................................+++++........",
					"......C.............................C..........++++........",
					"......................+++........................++.......",
					"...+++++.............+++..........................+.......",
					"......++...............++.................................",
					"........................+......................+++......++",
					"......C.............................C............+........",
					"...................++++++.................................",
					"......................+++....................+++++++++++++");
			GameMap AnorLondo = new GameMap(groundFactory, map2);
			world.addGameMap(AnorLondo);

			// Place fog door in Profane Capital
			FogDoor fogDoor1 = new FogDoor();
			fogDoor1.addAction(new MoveActorAction(AnorLondo.at(23, 0), "to Anor Londo!"));
			ProfaneCapital.at(38, 25).addItem(fogDoor1);

			// Place fog door in Anor Londo
			FogDoor fogDoor2 = new FogDoor();
			fogDoor2.addAction(new MoveActorAction(ProfaneCapital.at(38, 25), "to Profane Capital!"));
			AnorLondo.at(23, 0).addItem(fogDoor2);

			// Set Firelink Shrine's Bonfire in Proface Capital
			ProfaneCapital.at(38, 11).setGround(new FirelinkShrineBonfire(ProfaneCapital.at(38, 11)));

			// Set Anor Londo's Bonfire in Anor Londo
			AnorLondo.at(23, 8).setGround(new AnorLondoBonfire(AnorLondo.at(23, 8)));

			// Place Unkindled the player in the map
			Actor player = new Player("Unkindled (Player)", '@', 100, ProfaneCapital.at(38, 12));
			world.addPlayer(player, ProfaneCapital.at(38, 12));

			// Place Yhorm the Giant/boss in the map
			ProfaneCapital.at(6, 25).addActor(new YhormTheGiant(ProfaneCapital.at(6, 25)));

			// Place Aldrich The Devourer/boss in the map
			AnorLondo.at(40, 2).addActor(new AldrichTheDevourer(AnorLondo.at(40, 2), player));

			// Place storm ruler beside Yhorm
			ProfaneCapital.at(7, 25).addItem(new StormRuler());

			// Place Skeletons on the map
			ProfaneCapital.at(32, 7).addActor(new Skeleton(ProfaneCapital.at(32,7)));
			ProfaneCapital.at(25, 12).addActor(new Skeleton(ProfaneCapital.at(25, 12)));
			ProfaneCapital.at(45, 12).addActor(new Skeleton(ProfaneCapital.at(45, 12)));
			ProfaneCapital.at(32, 10).addActor(new Skeleton(ProfaneCapital.at(32, 10)));

			//Place Chests on the maps
			ProfaneCapital.at(24,8).addActor(new Mimic(ProfaneCapital.at(24,8)));
			ProfaneCapital.at(38,18).addActor(new Mimic(ProfaneCapital.at(38,18)));//38,14
			ProfaneCapital.at(6,12).addActor(new Mimic(ProfaneCapital.at(6,12)));
			AnorLondo.at(13, 6).addActor(new Mimic(AnorLondo.at(13, 6)));
			AnorLondo.at(16, 8).addActor(new Mimic(AnorLondo.at(16, 8)));
			AnorLondo.at(16, 6).addActor(new Mimic(AnorLondo.at(16, 6)));

			// Place Vendor on the map
			ProfaneCapital.at(37, 11).addActor(new Vendor("Vendor"));
			world.run();
	}

}
