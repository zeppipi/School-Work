import "./style.css";
import { interval, fromEvent, Observable, merge } from 'rxjs'
import { map, filter, subscribeOn, scan, elementAt } from "rxjs/operators";

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
  class Tick {constructor(public readonly elapsed: number) {} }                             // A clock
  
  // Store constants
  const Constants = 
  {
    CanvasSize: 600,                // The canvas you are working with is 600x600
    playerSpawnPoint: [300, 500],   // Where the player will spawn
    playerMoveDis: 40,              // Store the distance the player should move
    defaultEnemyMoveDis: 1          // Store the distance a normal enemy should move
  } as const;
  
  /**
   * All possible info of any circle objects
   * Code derived from the Astreoid FRP
   */
  type Circle = Readonly<{pos_x:number, pos_y:number}>

  /**
   * All possible info of any square objects
   */
  type Rectangle = Readonly<{pos_x: number, pos_y: number}>

  /**
   * All possible info of any objects
   * Code derived from the Astreoid FRP
   */
  interface IBody extends Circle, Rectangle{
    ID: string,
    shapeID: string,
    speed: number;
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
      shapeID: "ellipse",
      pos_x: Constants.playerSpawnPoint[0],
      pos_y: Constants.playerSpawnPoint[1],
      speed: 0
    };
  }

  /**
   * Function that creates the enemies!
   */
  function createEnemy(xPosition: number, yPosition: number, movementSpeed: number): Body
  {
    return{
      ID: "car",
      shapeID: "rect",
      pos_x: xPosition,
      pos_y: yPosition,
      speed: movementSpeed
    };
  }
  
  /**
   * The game state type, taken from the Astreoid FRP
   */
  type State = Readonly<{
    time:number,
    frog:Body,
    cars:ReadonlyArray<Body>,
    gameOver:boolean
  }>

  /**
   * The initial state of the game
   */
  const initialState: State = 
  {
    time: 0,
    frog: createPlayer(),
    cars: [createEnemy(0, 350, 1)],
    gameOver: false
  }

  /**
   * What happens on each state in relation to the events
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
      : tick(curState, curEvent.elapsed)//Make this empty for now
  
  /**
   * What happens on each state in relation to the whole game
   */
  const tick = (curState: State , elapsed: number) =>
  {
    return {...curState,
      cars: curState.cars.map(moveBody),
      time: elapsed
    };
  }

  //Player Move logic starts here
  /**
   * All possible keys
   */
  type Key = 'KeyA' | 'KeyD' | 'KeyW' | 'KeyS' 
  
  /**
   * All possible states of the key
   */
  type Event = 'keydown' | 'keyup'
  
  /**
   * Block of code taken from the Astreoid FRP example, simply observes what key is being pressed
   * 
   * @param e What is happening to the key being pressed
   * @param k What key is pressed
   * @param result What function should run when the key is pressed
   * @returns A stream of keyboard inputs
   */
  const
    // FPS setter
    gameClock = interval(20).
      pipe(map(elapsed => new Tick(elapsed))),

    keyPressed = <T>(e:Event, k:Key, result:()=>T)=>
    fromEvent<KeyboardEvent>(document,e)
      .pipe(
        filter(({code})=>code === k),
        filter(({repeat})=>!repeat),
        map(result)),
    
    //All possible movements
    moveLeft = keyPressed('keydown', 'KeyA', () => new Move(-Constants.playerMoveDis, 0)),
    moveRight = keyPressed('keydown', 'KeyD', () => new Move(Constants.playerMoveDis, 0)),
    moveUp = keyPressed('keydown', 'KeyW', () => new Move(0, -Constants.playerMoveDis)),
    moveDown = keyPressed('keydown', 'KeyS', () => new Move(0, Constants.playerMoveDis))

  /**
   * For moving any bodies that isn't controlled by the player, only works horizontally
   * Code derived from the Astreoid FRP
   * 
   * @param curBody The current body being managed
   * @returns 
   */
  const moveBody = (curBody: Body) => <Body>
  {
    ...curBody,
    pos_x: curBody.pos_x + curBody.speed
  }

  /**
   * Merges all game states into one stream
   * Derived from the Astreoid FRP
   */
  const mainGameStream = merge(
    gameClock,
    moveLeft,
    moveRight,
    moveUp,
    moveDown,
  ).
    pipe(scan(currentState, initialState)).
    subscribe(updateView);
  
  /**
   * "Update is called once per frame"
   * Derived from the Astreoid FRP
   * @param theState The current state to update to the next frame
   */
  function updateView(theState: State)
  {
    const
      // Get the canvas to be able to refer to the elements
      //svg = document.getElementById("svgCanvas")!,

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
        
        // const updateBody = (elem: Element, shape: String) =>
        // {
        //   shape == "ellipse" ?
        //   updateBody
        //   elem.setAttribute("cx", String(body.pos_x))
        //   elem.setAttribute("cy", String(body.pos_y))
        //   elem.setAttribute("rx", String(20))   //this is hardcoded... perish
        //   elem.setAttribute("ry", String(20))   //this is hardcoded... perish
        //   :
        //   elem.
        // }
        
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
  }
}

/** Latest progress
 *  + Turning the 'updateBodyView' to be more modular (Line 228)
 *  + Might be able to be done by somply having two 'updateBodyView' one for the circle and the other for squares
 * 
 *  Trello but not really
 *  + Enemy object moves
 *  + Clamp screen
 *  + Make the enemy works like a train
 *  + Give independent bodies the ability to hide and unhide on command
 * 
 *  Done:
 *  + Implement engine
 *  + Player Movement
 * 
 *  Note:
 *  1. Crocodile: can stand on its body, but not the head
 *  2. Player should fill in all goals before adding diffculty
 *  3. Snake: should show up in later diffs, goes across the safe area
 *  4. There is actually no randomness! All rows has a set pattern
 *  5. Nothing moves in a grid, this even includes the frog
 * 
 *  Report:
 *  In order to move a circle, you first make the universe. The first thing to make is 
 *  the frame updating cycle, this is done between the 'mainGameStream' and 'updateView'. 'mainGameStream' 
 *  merges all possible actions that can be done in the game into the 'currentState' to create the next state
 *  ('initialState'), then passes it to the 'updateView' to render what that next state should look like. This
 *  is done so that everything in the game can be updated only once per *frame
 * 
 *  *frame refers to the interaction between 'gameClock' and 'tick', where 'gameClock' makes a stream of 'tick'
 *   with the given interval (which right now is set to 20ms to represent how long a frame shows up in ≈60 FPS)
 */
