import "./style.css";
import { interval, fromEvent, Observable, merge, from } from 'rxjs'
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
    playerRadius: 20,               // Store the radius of the player (make sure this syncs with whats in the html)
    defaultEnemyMoveDis: 1,         // Store the distance a normal enemy should move
    carSize: [40, 80]               // Store car size [0] for height and [1] for width (just for note)
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
    speed: number,
    size?: number[],
    color?: string
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
  const createEnemy = (xPosition: number, yPosition: number, movementSpeed: number, enemyID: String, shapeID: String) => 
                      (color: String, size: Readonly<number[]>) =>
    <Body>{
      //Set the body object
      ID: enemyID + (xPosition + ""),
      shapeID: shapeID,
      pos_x: xPosition,
      pos_y: yPosition,
      speed: movementSpeed,
      size: size,
      color: color
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
    cars: [createEnemy(0, 360, 1, "car", "rect")("orange", Constants.carSize), createEnemy(-160, 360, 1, "car", "rect")("orange", Constants.carSize), createEnemy(-320, 360, 1, "car", "rect")("orange", Constants.carSize)],
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
          const updateBody = document.createElementNS(canvas.namespaceURI, body.shapeID);

          //Set its id
          updateBody.setAttribute("id", body.ID);
          
          //Different steps for different shapes
          body.shapeID == "rect" ?
          (updateBody.setAttribute("x", String(body.pos_x)),
          updateBody.setAttribute("y", String(body.pos_y)),
          //Size is an optional variable
          body.size ?
            (updateBody.setAttribute("width", String(body.size[1])), 
            updateBody.setAttribute("height", String(body.size[0])))
            :
            0,
          //Color is an optional variable
          body.color ?
            (updateBody.setAttribute("style", "fill: " + body.color + "; stroke: " + body.color + "; stroke-width: 1px;"))
            :
            0,
          canvas.appendChild(updateBody))
          :
          canvas.appendChild(updateBody)
          return updateBody;
        }

        //Updating ellipses
        const updateEllipse = () =>
        {
          updateBody.setAttribute("cx", String(body.pos_x))
          updateBody.setAttribute("cy", String(body.pos_y))
          updateBody.setAttribute("rx", String(Constants.playerRadius))  
          updateBody.setAttribute("ry", String(Constants.playerRadius))
        }

        //Updating rects
        const updatingRect = () =>
        {
          updateBody.setAttribute("x", String(body.pos_x))
          updateBody.setAttribute("y", String(body.pos_y))
          updateBody.setAttribute("width", String(Constants.carSize[1]))   
          updateBody.setAttribute("height", String(Constants.carSize[0]))
        }
        
        const updateBody = document.getElementById(body.ID) || createBodyView()

        body.shapeID == "ellipse" ?
        updateEllipse()
        :
        updatingRect()
      }
    
    // Update all bodies 
    updateBodyView(theState.frog, svg)
    theState.cars.forEach((cars => updateBodyView(cars, svg)))

  }

}

// The following simply runs your main function on window load.  Make sure to leave it in place.
if (typeof window !== "undefined") {
  window.onload = () => {
    main();
  }
}

/** Latest progress
 *  + Make all of the car lanes (line 113)
 * 
 *  Trello but not really
 *  + Clamp screen
 *  + Give independent bodies the ability to hide and unhide on command
 *  + Collision system
 *  + Life system
 *  + Game Over system
 * 
 *  Done:
 *  + Implement engine
 *  + Player Movement
 *  + Enemy object moves
 *  + Make the enemy works like a train
 * 
 *  Note:
 *  1. Crocodile: can stand on its body, but not the head
 *  2. Player should fill in all goals before adding diffculty
 *  3. Snake: should show up in later diffs, goes across the safe area
 *  4. There is actually no randomness! All rows has a set pattern
 *  5. Nothing moves in a grid, this even includes the frog
 * 
 *  Report:
 *  In order to move a circle, you first make the universe, this is done by making 
 *  the frame updating cycle, this is done between the 'mainGameStream' and 'updateView'. 'mainGameStream' 
 *  merges all possible events that can be done in the game into the 'currentState' to create the next state
 *  ('initialState'), then passes it to the 'updateView' to render what that next state should look like. This
 *  is done so that everything in the game can be updated only once per frame.
 * 
 *  Frame refers to the interaction between 'gameClock' and 'tick', where 'gameClock' makes a stream of 'tick'
 *  with a given interval (which right now is set to 20ms to represent how long a frame shows up in ≈60 FPS).
 * 
 *  After creating the universe, next is to move some circles and rectangles. One thing to note is, I decided to 
 *  make everything but the player a rectangle, so that the systems between player and non-player are easily seperatable.
 * 
 *  The player's movement are managed by the 'mainGameStream' where the act of the player pressing a direction is considered
 *  as an event, where the enemies' movements are managed by the 'updateView'. In the 'updateView' method it can be seen that 
 *  rendering the player object requires a lot less steps than non-player objects, this is because a lot of the player's attributes 
 *  are already written in the html file, giving the player some permemnance to their attributes, the other objects are not given 
 *  the same treatment because of ID issues, there will be several copies of the same non-player object in the game, and if all of 
 *  them have the same ID in the html, then all distinct copies will have the same attributes, so instead, their attributes are made 
 *  in the 'initialState'.
 */
