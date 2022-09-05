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
    riverStart: 280,                // Store the y-location of where the river section starts
    riverEnd: 120                   // Store the y-location of where the river section ends
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
    showRound: number,
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
      showRound: 0,
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
    (positionsX: Readonly<number[]>, positionsY: Readonly<number>[], showRounds: Readonly<number>[], counter: number, collidingFunction: (_:Body) => Body, enemies: Body[] = []): Body[] =>
      {
        return counter > -1 ? 
          createEnemies(size, sizeWidthMulti, color, enemyID, shapeID, movementSpeed)(positionsX, positionsY, showRounds, counter - 1, collidingFunction, [createEnemy(positionsX[counter], positionsY[counter], showRounds[counter], movementSpeed, enemyID, shapeID)(color, size, sizeWidthMulti)(collidingFunction)].concat(enemies))
        :
          enemies
      }
          
  /**
  * A function that creates an enemy!
  * 
  * @returns A single body
  */
  const createEnemy = (xPosition: number, yPosition: number, showRound: number, movementSpeed: number, enemyID: String, shapeID: String) => 
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
      showRound: showRound,
      color: color,
      collidingEvent: collidingFunction
    }

  /**
   * A function that calculates if a body should exist in the current round
   * 
   * @param theState The state to check what round is it currently
   * @param curBody  The body to check
   * @returns True for they should and false they shouldn't
   */
     const existOnRound = (theState: State, curBody: Body): boolean =>
     {
       const currentRound = theState.rounds;
       const bodyRound = curBody.showRound;
   
       return bodyRound <= currentRound;
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
   * A function that 'destroys' 'otherBody' and resets the 'player'
   * 
   * @param theState This function is specifically for non-players
   * @returns 
   */
  const Destroy = (player: State, otherBody: Body): Body =>
  {
    <State> {
      ...player,
      frog: Reset(player.frog)
    }

    return <Body> {
      ...otherBody,
      pos_y: otherBody.pos_y * -1
    }
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
   * A function that checks if the round has ended or not
   * 
   * @param theState The state to check if we should go to the next round
   */
  const roundChecker = (theState: State): boolean =>
  {
    //True for the goal is gone, false for the goal is still there
    const goalsCondition = theState.wins.map((curBody) => curBody.pos_y < 0 ? 1 : 0)
    const roundDone = goalsCondition.reduce((a:number, b:number):number => a + b, 0)

    return roundDone >= 3 ? true : false
  }

  /**
   * A function that resets the scene and adds the round by one
   * 
   * @param theState The state to send to the next round
   * @returns Edits to "theState"
   */
  const newRound = (theState: State) => <State>
  {
    ...theState,
    rounds: theState.rounds + 1,
    frog: Reset(theState.frog),
    wins: theState.wins.map((curBody) => <Body>{
       ...curBody,
       pos_y: curBody.pos_y * -1
    })
  }

  /**
   * An empty body for when something went terrible wrong
   */
  const emptyBody = createEnemy(0,0,0,0, "Wrong", "rect")("white", 300, 1)(Identity)
  
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
    logs1: ReadonlyArray<Body>,
    logs2: ReadonlyArray<Body>,
    logs3: ReadonlyArray<Body>,
    logs4: ReadonlyArray<Body>,
    wins: ReadonlyArray<Body>,
    riverWins: ReadonlyArray<Body>,
    snake: ReadonlyArray<Body>,
    rounds: number,
    gameOver:boolean
  }>

  /**
   * The initial state of the game
   */
  const initialState: State = 
  {
    time: 0,
    frog: createPlayer(),
    cars: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "orange", "car", "rect", 2.5)([200, 400, 600], [440, 440, 440], [0,2,0], 2, Reset),
    karts: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 1.3, "lightgreen", "karts", "rect", -4)([100, 250, 450, 600], [400, 400, 400, 400], [0,3,3,2], 3, Reset),
    vans: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2.3, "white", "vans", "rect", 2)([100, 300, 500], [360, 360, 360], [0,2,0], 2, Reset),
    trucks: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 4, "red", "trucks", "rect", -1.8)([0, 250, 500], [320, 320, 320], [0,2,0], 2, Reset),
    logs1: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 4, "brown", "logs1", "rect", 2)([0, 250, 500], [240, 240, 240], [0,0,0], 2, (body) => moveBody(body, 2)),
    logs2: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "brown", "logs2", "rect", -3)([0, 160, 320, 480], [200, 200, 200, 200], [0,0,0,0], 3, (body) => moveBody(body, -3)),
    logs3: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 4, "brown", "logs3", "rect", 2)([0, 250, 500], [160, 160, 160], [0,0,0], 2, (body) => moveBody(body, 2)),
    logs4: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "brown", "logs4", "rect", -1.8)([0, 160, 320, 480], [120, 120, 120, 120], [0,0,0,0], 3, (body) => moveBody(body, -1.8)),
    wins: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "cyan", "win", "rect", 0)([70, 270, 470], [80, 80, 80], [0,0,0], 2, (body) => moveBody(body, 0)),
    riverWins: createEnemies(Constants.enemyHeights, 2, "blue", "riverWin", "rect", 0)([0, 146, 190, 346, 390, 546], [79.5, 79.5, 79.5, 79.5, 79.5, 79.5],[0,0,0,0,0,0], 5, Reset),
    snake: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins - 30, 10, "lightgreen", "snake", "rect", 0.8)([0], [295], [3], 0, Reset),
    rounds: 1,
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
    const allBodies = curState.cars.concat(curState.karts).concat(curState.vans).concat(curState.trucks).concat(curState.logs1).
                      concat(curState.logs2).concat(curState.logs3).concat(curState.logs4).concat(curState.wins).concat(curState.snake).
                      concat(curState.wins).concat(curState.riverWins);
    
    // Check if round should continue or reset to a new one
    return roundChecker(curState) ? 
    newRound(curState)
    :
    {...curState,
      cars: curState.cars.map(car => moveBody(car)),
      karts: curState.karts.map(kart => moveBody(kart)),
      vans: curState.vans.map(vans => moveBody(vans)),
      trucks: curState.trucks.map(trucks => moveBody(trucks)),
      logs1: curState.logs1.map(log => moveBody(log)),
      logs2: curState.logs2.map(log => moveBody(log)),
      logs3: curState.logs3.map(log => moveBody(log)),
      logs4: curState.logs4.map(log => moveBody(log)),
      wins: curState.wins.map(win => isColliding(win, curState.frog, curState) ? Destroy(curState, win) : win),
      snake: curState.snake.map(snake => moveBody(snake)),
      frog: collisionsHandler(allBodies, curState.frog, curState),
      time: elapsed
    };
  }

  /**
   * Checks collisions between a single body with a list of bodies
   * If true, play the given function, if false, body is left alone
   * Also calls 'riverChecker' to check if the player is in the river section,
   * in which the collision behaviours work a little differently
   * 
   * @param bodyList the list of bodies
   * @param singleBody the single body to check
   * @param eventFunction what event should happen when a collision happens
   */
  const collisionsHandler = (bodyList: Body[], singleBody: Body, theState: State): Body =>
  {
    const auxCollisionFunc = (bodyListAux: Body[], singleBodyAux: Body): Body =>
    {
      const collidedBodies = bodyListAux.map((currentBodyList) => isColliding(currentBodyList, singleBodyAux, theState) ? currentBodyList : null)
      const filteredCollidedBodies = collidedBodies.filter((current) => current ? current : null)

      //In the list of collided bodies, only the first one will be needed to know if the player has collided
      const collided = filteredCollidedBodies[0] ? true : false
      const river = riverChecker(singleBodyAux)

      //Check if the body is currently in the river
      return river ? 
        collided ?
          filteredCollidedBodies[0] ? filteredCollidedBodies[0].collidingEvent(singleBodyAux) : singleBodyAux  //In river and colliding a body
          :
          Reset(singleBodyAux)  //In river but not colliding a body
        : 
        collided ? 
          filteredCollidedBodies[0] ? filteredCollidedBodies[0].collidingEvent(singleBodyAux) : singleBodyAux  //Not in river and colliding with a body
          : 
          singleBodyAux   //Not in river and not colliding with a body
      
      //return filteredCollidedBodies[0]? filteredCollidedBodies[0].collidingEvent(singleBodyAux) : singleBodyAux;
      
    }
    return auxCollisionFunc(bodyList, singleBody)
  }

  /**
   * Checks if a body is in the 'river' section
   * 
   * @param curBody The body to check
   * @returns A true for 'it is in the river section' and a false for 'it isn't in the river section'
   */
  const riverChecker = (curBody: Body): boolean =>
  {
    return curBody.pos_y < Constants.riverStart ? curBody.pos_y > Constants.riverEnd ? true : false : false;
  }

  /**
   * Checks if the collider is colliding with the collidee
   * 
   * @param collider The instigator (enemy here)
   * @param collidee The victim (player here)
   * @returns True for they did and false for nah
   */
  const isColliding = (collider: Body, collidee: Body, theState: State): boolean =>
  {
    return collidee.pos_x < collider.pos_x || collidee.pos_x > (collider.pos_x + ((collider.size * collider.sizeWidthMulti) - Constants.enemySizeMargins)) 
            ? false : collidee.pos_y < collider.pos_y || collidee.pos_y > (collider.pos_y + (collider.size - Constants.enemySizeMargins)) 
            ? false : true && existOnRound(theState, collider)

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
  const moveBody = (curBody: Body, speed: number = curBody.speed) => <Body>
  {
      ...curBody,
    // This 'if statement' is to give the objects the ability to warp when out of bounds
    pos_x: speed >= 0 ? // For objects moving left to right
      curBody.pos_x + speed > Constants.CanvasSize  ? 
      0 - ((curBody.size * curBody.sizeWidthMulti) - Constants.enemySizeMargins)
      : 
      curBody.pos_x + speed
    :
      // For objects moving right to left
      curBody.pos_x + speed < 0 - ((curBody.size * curBody.sizeWidthMulti) - Constants.enemySizeMargins) ?
        Constants.CanvasSize
        :
        curBody.pos_x + speed
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
    //Update UI elements
    rounds.textContent = String("Rounds: " + theState.rounds);
    
    const
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
          canvas.removeChild
          updateBody.setAttribute("cx", String(body.pos_x))
          updateBody.setAttribute("cy", String(body.pos_y))
          updateBody.setAttribute("rx", String(Constants.playerRadius))  
          updateBody.setAttribute("ry", String(Constants.playerRadius))
          canvas.appendChild(updateBody)
        }

        //Updating rects
        const updatingRect = () =>
        {
          updateBody.setAttribute("x", String(body.pos_x))
          updateBody.setAttribute("y", String(body.pos_y))
          updateBody.setAttribute("width", String((body.size * body.sizeWidthMulti) - Constants.enemySizeMargins)), 
          updateBody.setAttribute("height", String(body.size))
          updateBody.setAttribute("class", "obs")
        }
        
        const updateBody = document.getElementById(body.ID) || createBodyView()

        body.shapeID == "ellipse" ?
        updateEllipse()
        :
        updatingRect()
      }
    
    // Update all bodies 
    // Check 'existRounds' here
    theState.cars.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    theState.karts.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    theState.vans.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    theState.trucks.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    theState.logs1.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    theState.logs2.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    theState.logs3.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    theState.logs4.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    theState.wins.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    theState.riverWins.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    theState.snake.forEach((body) => existOnRound(theState, body) ? updateBodyView(body, svg) : 0)
    existOnRound(theState, theState.frog) ? updateBodyView(theState.frog, svg) : 0

  }

  //UI Elements here
  const rounds = document.getElementById("rounds")!;

}

// The following simply runs your main function on window load.  Make sure to leave it in place.
if (typeof window !== "undefined") {
  window.onload = () => {
    main();
  }
}

/** Latest progress
 *  + The game now has the concept of rounds and game gets progressively harder as it goes
 *  + Biggest issue is on 'Destroy'
 * 
 *  Trello but not really
 *  + Code the wins/goals
 *  + Make turtles
 *  + Make bodies able to hide when a certain round is reached
 *  + Make crocodiles
 *  + Life system
 *  + Game Over system
 *  + Restart system
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
 *  + Make the log lanes
 *  + Make river
 *  + Give independent bodies the ability to hide and unhide on command  
 *  + Round system
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
 *  being held by that body, and throw itself into it. The reason why I make these bodies hold a function, is to make things more
 *  modular, the player hitting a log will do different things to when it hits a car.
 */
