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
  class Tick {constructor(public readonly elapsed: number) {} }                          // A clock
  
  // Store constants
  const Constants = 
  {
    gridSize: 20,                   // This game will run with grids size of 20x20
    CanvasSize: 600,                // The canvas you are working with is 600x600
    playerSpawnPoint: [300, 540],   // Where the player will spawn
    playerMoveDis: 40,              // Store the distance the player should move
    playerRadius: 18,               // Store the radius of the player (make sure this syncs with whats in the html)
    enemyHeights: 40,               // Store the height of every non-player objects
    enemySizeMargins: 2,            // Store the margins of every non-player objects
    // carSize: [38, 78],              // Store car size [0] for height and [1] for width
    // kartsSize: [38, 48],            // Same for karts
    // vansSize: [38, 93],             // Same for vans
    // truckSize: [38, 158]            // Same for trucks
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
    size: number,
    sizeWidthMulti: number,
    color?: string,
    collidingEvent: (_: Body) => Body
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
      speed: 0,
      size: 0,
      sizeWidthMulti: 0,
      collidingEvent: Identity
    };
  }

  /**
   * A function that makes several enemies at once, made so that 'createEnemy' don't need to be called
   * so many times
   * 
   * @returns A list of bodies
   */
  const createEnemies = 
    (size: number, sizeWidthMulti: number, color: String, enemyID: String, shapeID: String, movementSpeed: number) =>
    (positionsX: Readonly<number[]>, positionsY: Readonly<number>[], counter: number, collidingFunction: (_:Body) => Body, enemies: Body[]): Body[] => 
      {
        return counter > -1 ? 
          createEnemies(size, sizeWidthMulti, color, enemyID, shapeID, movementSpeed)(positionsX, positionsY, counter - 1, collidingFunction, [createEnemy(positionsX[counter], positionsY[counter], movementSpeed, enemyID, shapeID)(color, size, sizeWidthMulti)(collidingFunction)].concat(enemies))
        :
          enemies
      }
          
  
  /**
  * A function that creates an enemy!
  * 
  * @returns A single body
  */
  const createEnemy = (xPosition: number, yPosition: number, movementSpeed: number, enemyID: String, shapeID: String) => 
                      (color: String, size: number, sizeWidthMulti: number) => (collidingFunction: (_:Body) => Body) =>
    <Body>{
      //Set the body object
      ID: enemyID + (xPosition + ""),
      shapeID: shapeID,
      pos_x: xPosition,
      pos_y: yPosition,
      speed: movementSpeed,
      size: size,
      sizeWidthMulti: sizeWidthMulti,
      color: color,
      collidingEvent: collidingFunction
    }

  /**
   * A function to reset a body to their spawn position
   * 
   * @param player This function is specifically for the player
   * @returns 
   */
  const Reset = (player: Body) => <Body>
  {
   ...player,
   pos_x: Constants.playerSpawnPoint[0],
   pos_y: Constants.playerSpawnPoint[1]
  }

  /**
  * A function that does nothing to the inserted body
  * 
  * @param player For the player
  * @returns 
  */
  const Identity = (player: Body) => <Body>
  {
    ...player
  }

  /**
   * An empty body for when something went terrible wrong
   */
  const emptyBody = createEnemy(0,0,0, "Wrong", "rect")("white", 300, 1)(Identity)
  
  /**
   * The game state type, taken from the Astreoid FRP
   */
  type State = Readonly<{
    time:number,
    frog:Body,
    cars:ReadonlyArray<Body>,
    karts: ReadonlyArray<Body>,
    vans: ReadonlyArray<Body>,
    trucks: ReadonlyArray<Body>,
    gameOver:boolean
  }>

  /**
   * The initial state of the game
   */
  const initialState: State = 
  {
    time: 0,
    frog: createPlayer(),
    cars: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "orange", "car", "rect", 2.5)([200, 400, 600], [440, 440, 440], 2, Reset, []),
    karts: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 1.3, "lightgreen", "karts", "rect", -4)([100, 250, 450, 600], [400, 400, 400, 400], 3, Reset, []),
    vans: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2.3, "white", "vans", "rect", 2)([100, 300, 500], [360, 360, 360], 2, Reset, []),
    trucks: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 4, "red", "trucks", "rect", -1.8)([0, 250, 500], [320, 320, 320], 2, Reset, []),
    gameOver: false
  }

  /**
   * What happens on each state in relation to the events
   */
  const currentState = (curState: State, curEvent: Move | Tick ) =>
    // For when move happens
    curEvent instanceof Move ?
      {...curState,
        frog: clamp(curState.frog.pos_x + curEvent.x_move, curState.frog.pos_y + curEvent.y_move) ?
        {   
          ...curState.frog,
          pos_x: curState.frog.pos_x + curEvent.x_move,
          pos_y: curState.frog.pos_y + curEvent.y_move
        }
        :
        {
          ...curState.frog
        }
      }
    : 
    tick(curState, curEvent.elapsed)
  
  /**
   * What happens on each state in relation to the whole game
   */
  const tick = (curState: State , elapsed: number): State =>
  {
    //Combine all non-player bodies
    const allBodies = curState.cars.concat(curState.karts).concat(curState.vans).concat(curState.trucks)

    // Returns state
    return {...curState,
      cars: curState.cars.map(car => moveBody(car)),
      karts: curState.karts.map(kart => moveBody(kart)),
      vans: curState.vans.map(vans => moveBody(vans)),
      trucks: curState.trucks.map(trucks => moveBody(trucks)),
      //frog: curState.frog,
      frog: collisionsHandler(allBodies, curState.frog),
      time: elapsed
    };
  }

  /**
   * Checks collisions between a single body with a list of bodies
   * If true, play the given function, if false, body is left alone
   * 
   * @param bodyList the list of bodies
   * @param singleBody the single body to check
   * @param eventFunction what event should happen when a collision happens
   */
  const collisionsHandler = (bodyList: Body[], singleBody: Body): Body =>
  {
    const auxCollisionFunc = (bodyListAux: Body[], singleBodyAux: Body): Body =>
    {
      const collidedBodies = bodyListAux.map((currentBodyList) => isColliding(currentBodyList, singleBodyAux) ? currentBodyList : null)
      const filteredCollidedBodies = collidedBodies.filter((current) => current ? current : null)

      //In the list of collided bodies, only the first one will be needed to know if the player has collided
      return filteredCollidedBodies[0]? filteredCollidedBodies[0].collidingEvent(singleBodyAux) : singleBodyAux;
      
    }
    return auxCollisionFunc(bodyList, singleBody)
  }

  /**
   * Checks if the collider is colliding with the collidee
   * 
   * @param collider The instigator (enemy here)
   * @param collidee The victim (player here)
   * @returns True for they did and false for nah
   */
  const isColliding = (collider: Body, collidee: Body): boolean =>
  {
    return collidee.pos_x < collider.pos_x || collidee.pos_x > (collider.pos_x + ((collider.size * collider.sizeWidthMulti) - Constants.enemySizeMargins)) 
            ? false : collidee.pos_y < collider.pos_y || collidee.pos_y > (collider.pos_y + (collider.size - Constants.enemySizeMargins)) 
            ? false : true

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
    // This 'if statement' is to give the objects the ability to warp when out of bounds
    pos_x: curBody.speed >= 0 ? // For objects moving left to right
      curBody.pos_x + curBody.speed > Constants.CanvasSize  ? 
      0 - ((curBody.size * curBody.sizeWidthMulti) - Constants.enemySizeMargins)
      : 
      curBody.pos_x + curBody.speed 
    :
      curBody.size ?  // For objects moving right to left
        curBody.pos_x + curBody.speed < 0 - ((curBody.size * curBody.sizeWidthMulti) - Constants.enemySizeMargins) ?
          Constants.CanvasSize
        :
        curBody.pos_x + curBody.speed
      :
      0
  }

  /**
   * Determines if the given positions are a valid position in the canvas,
   * if false then it is not and if true then it is
   * 
   * @param curPosx The x position to be evaluated
   * @param curPosy The y position to be evaluated
   * @returns a boolean
   */
  const clamp = (curPosx: number, curPosy: number): boolean =>
  {
    return curPosx < Constants.CanvasSize && curPosx > 0 && curPosy < Constants.CanvasSize && curPosy > 0 ? true : false
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
          updateBody.setAttribute("width", String((body.size * body.sizeWidthMulti) - Constants.enemySizeMargins)), 
          updateBody.setAttribute("height", String(body.size)),
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
          updateBody.setAttribute("width", String((body.size * body.sizeWidthMulti) - Constants.enemySizeMargins)), 
          updateBody.setAttribute("height", String(body.size))
        }
        
        const updateBody = document.getElementById(body.ID) || createBodyView()

        body.shapeID == "ellipse" ?
        updateEllipse()
        :
        updatingRect()
      }
    
    // Update all bodies 
    updateBodyView(theState.frog, svg)
    theState.cars.forEach((body => updateBodyView(body, svg)))
    theState.karts.forEach((body => updateBodyView(body, svg)))
    theState.vans.forEach((body => updateBodyView(body, svg)))
    theState.trucks.forEach((body => updateBodyView(body, svg)))

  }

}

// The following simply runs your main function on window load.  Make sure to leave it in place.
if (typeof window !== "undefined") {
  window.onload = () => {
    main();
  }
}

/** Latest progress
 *  + COLLISION DONE HOLY SHIT
 *  + Next is make their logs and their respective function
 * 
 *  Trello but not really
 *  + Make the log lanes
 *  + Give independent bodies the ability to hide and unhide on command
 *  + Life system
 *  + Game Over system
 * 
 *  Done:
 *  + Implement engine
 *  + Player Movement
 *  + Enemy object moves
 *  + Make the enemy works like a train
 *  + Clamp player
 *  + Warp NPCs
 *  + Make the car lanes
 *  + Collision system
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
 * 
 *  The collision system runs in the 'tick' function where in every tick, the player will check if they are colliding
 *  with all of the non-player bodies (the list of all non-player bodies are made by simply concatonating all of the
 *  distinct list of bodies). And when the player is colliding with one of them, it will call the function that is
 *  being held by that body, and throw itself into it. 
 */
