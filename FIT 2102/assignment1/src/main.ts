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
  class Move {constructor(public readonly x_move: number, readonly y_move: number) {} }  // A move state
  class Tick {constructor(public readonly time: number) {} }                    // A clock
  
  // Store constants
  const Constants = 
  {
    CanvasSize: 600,                // The canvas you are working with is 600x600
    playerSpawnPoint: [300, 550],   // Where the player will spawn
    playerMoveDis: 20,              // Store the distance the player should move
  } as const;
  
  /**
   * All possible info of any circle objects
   * Code derived from the Astreoid FRP
   */
  type Circle = Readonly<{pos_x:number, pos_y:number}>

  /**
   * All possible info of any objects
   * Code derived from the Astreoid FRP
   */
  interface IBody extends Circle{
    ID: string;
  }

  // Body object, made from its interface
  type Body = Readonly<IBody>;
  
  /**
   * Function that creates the player!
   */
  function createPlayer(): Body
  {
    return{
      ID: "frog",
      pos_x: Constants.playerSpawnPoint[0],
      pos_y: Constants.playerSpawnPoint[1],
    };
  }
  
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
          pos_x: curState.frog.pos_x + curEvent.x_move,
          pos_y: curState.frog.pos_y + curEvent.y_move
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
    subscribe(updateView) //continue later => make update view

  /**
   * "Update is called once per frame"
   * Derived from the Astreoid FRP
   * @param theState The current state to update to the next frame
   */
    function updateView(theState: State)
  {
    const
      // Get the canvas to be able to refer to the elements
      svg = document.getElementById("svgCanvas")!,

      //Update all bodies on screen
      updateBodyView = (body: Body, canvas: HTMLElement) => 
      {
        //Recreate the html object in case it doesn't spawn for this frame
        const createBodyView = () =>
        {
          //Shape is hardcoded... perish
          const updateBody = document.createElementNS(canvas.namespaceURI, "ellipse");

          //Set its id
          updateBody.setAttribute("id", body.ID);

          //Add to HTML and return
          canvas.appendChild(updateBody);
          return updateBody;
        }
        
        const updateBody = document.getElementById(body.ID) || createBodyView()

        //Condition so the body that gets updated is the appropiate variables
        //dealing only with the circles for now
        updateBody.setAttribute("cx", String(body.pos_x))
        updateBody.setAttribute("cy", String(body.pos_y))
        updateBody.setAttribute("rx", String(20))   //this is hardcoded... perish
        updateBody.setAttribute("ry", String(20))   //this is hardcoded... perish
      }
    
    // Update the body of the frog
    updateBodyView(theState.frog, svg)
  }

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
