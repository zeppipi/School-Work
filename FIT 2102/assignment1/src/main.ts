import "./style.css";
import { interval, fromEvent, Observable, merge } from 'rxjs'
import { map, filter, subscribeOn } from "rxjs/operators";

function main() {
  /**
   * Inside this function you will use the classes and functions from rx.js
   * to add visuals to the svg element in pong.html, animate them, and make them interactive.
   *
   * Study and complete the tasks in observable examples first to get ideas.
   *
   * Course Notes showing Asteroids in FRP: https://tgdwyer.github.io/asteroids/
   *
   * You will be marked on your functional programming style
   * as well as the functionality that you implement.
   *
   * Document your code!
   */

  /**
   * This is the view for your game to add and update your game elements.
   */
  const svg = document.querySelector("#svgCanvas") as SVGElement & HTMLElement;

  // Store constants
  const Constants = 
  {
    CanvasSize: 600,                // The canvas you are working with is 600x600
    playerSpawnPoint: [300, 550],   // Where the player will spawn
    playerMoveDis: 20               // Store the distance the player should move
  } as const;
  
  // Adding a circle element that represents the player
  const player = document.createElementNS(svg.namespaceURI, "circle");
  player.setAttribute("r", String(Constants.playerMoveDis));  //They should move by the length of themselve
  player.setAttribute("cx", String(Constants.playerSpawnPoint[0]));
  player.setAttribute("cy", String(Constants.playerSpawnPoint[1]));
  player.setAttribute(
    "style",
    "fill: green; stroke: green; stroke-width: 1px;"
  );
  svg.appendChild(player);
}

// The following simply runs your main function on window load.  Make sure to leave it in place.
if (typeof window !== "undefined") {
  window.onload = () => {
    main();
  };
}

/** Trello but not really
 *  1. Player moves
 *  2. Enemy object moves
 * 
 *  Note:
 *  1. Crocodile: can stand on its body, but not the head
 *  2. Player should fill in all goals before adding diffculty
 *  3. Snake: should show up in later diffs, goes across the safe area
 *  4. There is actually no randomness! All rows has a set pattern
 *  5. Nothing moves in a grid, this even includes the frog
 */
