import "./style.css";
import { interval, fromEvent, Observable, merge } from 'rxjs'
import { map, filter, subscribeOn, scan } from "rxjs/operators";

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

  // Store the possible game actions
  class Move {constructor(public readonly x_move: number, y_move: number) {} }  // A move state
  class Tick {constructor(public readonly time: number) {} }                    // A clock
  
  // Store constants
  const Constants = 
  {
    CanvasSize: 600,                // The canvas you are working with is 600x600
    playerSpawnPoint: [300, 550],   // Where the player will spawn
    playerMoveDis: 20,              // Store the distance the player should move
    playerFillColor: "green",       // Store the player's fill color
    playerStrokeColor: "green",     // Store the player's stroke color
    playerStrokeWidth: 1            // Store the player's stroke width
  } as const;
  
  /**
   * All possible info of any circle objects
   * Code derived from the Astreoid FRP
   */
  type Circle = Readonly<{pos_x:number, pos_y:number , radius:number}>

  /**
   * All possible info of any objects
   * Code derived from the Astreoid FRP
   */
  interface IBody extends Circle{
    //For cosmetic attributes
    fillColor: String,
    strokeColor: String,
    strokeWidth: number
  }

  // Body object, made from its interface
  type Body = Readonly<IBody>;
  
  /**
   * Function that creates the player!
   */
  function createPlayer(): Body
  {
    return{
      pos_x: Constants.playerSpawnPoint[0],
      pos_y: Constants.playerSpawnPoint[1],
      radius: Constants.playerMoveDis,
      fillColor: Constants.playerFillColor,
      strokeColor: Constants.playerStrokeColor,
      strokeWidth: Constants.playerStrokeWidth
    };
  }
  
  // Adding a circle element that represents the player
  const player = document.createElementNS(svg.namespaceURI, "circle");
  player.setAttribute("r", String(Constants.playerMoveDis));  //They should move by the length of themself
  player.setAttribute("cx", String(Constants.playerSpawnPoint[0]));
  player.setAttribute("cy", String(Constants.playerSpawnPoint[1]));
  player.setAttribute(
    "style",
    "fill: green; stroke: green; stroke-width: 1px;"
  );
  svg.appendChild(player);

  /**
   * The game state type, taken from the Astreoid FRP
   */
  type State = Readonly<{
    time:number,
    frog:Body,
    gameOver:boolean
  }>

  /**
   * The initial state of the game
   */
  const initialState: State = 
  {
    time: 0,
    frog: createPlayer(),
    gameOver: false
  }

  /**
   * What to do on each state
   */
  const currentState = (curState: State, curEvent: Move | Tick) =>
    curEvent instanceof Move ?
      {...curState,
        frog: 
        { ...curState.frog,
          pos_x: curState.frog.pos_x + Constants.playerMoveDis,
          pos_y: curState.frog.pos_y + Constants.playerMoveDis
        }
      }
      : tick(curState)//Make this empty for now

  /**
   * Make this empty for now
   */
  const tick = (curState: State) =>
  {
    return {...curState};
  }

  //Player Move logic starts here
  /**
   * All possible keys
   */
  type Key = 'ArrowLeft' | 'ArrowRight' | 'ArrowUp' | 'ArrowDown' | 'a' | 'd' | 'w' | 's'
  
  /**
   * All possible states of the key
   */
  type Event = 'keyDown' | 'keyUp'
  
  /**
   * Block of code taken from the Astreoid FRP example, simply observes what key is being pressed
   * 
   * @param e What is happening to the key being pressed
   * @param k What key is pressed
   * @param result What function should run when the key is pressed
   * @returns A stream of keyboard inputs
   */
  const
    keyPressed = <T>(e:Event, k:Key, result:()=>T)=>
    fromEvent<KeyboardEvent>(document,e)
      .pipe(
        filter(({code})=>code === k),
        filter(({repeat})=>!repeat),
        map(result)),
    
    //All possible movements
    moveLeft = keyPressed('keyDown', 'ArrowLeft' || 'a', () => new Move(-Constants.playerMoveDis, 0)),
    moveRight = keyPressed('keyDown', 'ArrowRight' || 'd', () => new Move(Constants.playerMoveDis, 0)),
    moveUp = keyPressed('keyDown', 'ArrowUp' || 'w', () => new Move(0, Constants.playerMoveDis)),
    moveDown = keyPressed('keyDown', 'ArrowDown' || 's', () => new Move(-Constants.playerMoveDis, 0)),
    moveIdle = keyPressed('keyUp', 'ArrowLeft' && 'ArrowRight' && 'ArrowUp' && 'ArrowDown' && 'a' && 'd' && 'w' && 's', () => new Move(0,0))

  /**
   * Merges all game states into one stream
   * Derived from the Astreoid FRP
   */
  const mainGameStream = merge(
    moveLeft,
    moveRight,
    moveUp,
    moveDown,
    moveIdle
  ).
    pipe(scan(currentState, initialState)).
    subscribe() //continue later => make update view

}

// The following simply runs your main function on window load.  Make sure to leave it in place.
if (typeof window !== "undefined") {
  window.onload = () => {
    main();
  };
}

/** Latest progress
 *  + Making 'updateview' (line 173)
 * 
 *  Trello but not really
 *  1. Make the whole fuckin engine i guess
 *  2. Player moves
 *  3. Enemy object moves
 * 
 *  Note:
 *  1. Crocodile: can stand on its body, but not the head
 *  2. Player should fill in all goals before adding diffculty
 *  3. Snake: should show up in later diffs, goes across the safe area
 *  4. There is actually no randomness! All rows has a set pattern
 *  5. Nothing moves in a grid, this even includes the frog
 */
