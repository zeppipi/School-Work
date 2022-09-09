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
  class FullReset {constructor () {}}                                                    // A full restart
  
  // Store constants
  const Constants = 
  {
    gridSize: 20,                   // This game will run with grids size of 20x20
    CanvasSize: 600,                // The canvas you are working with is 600x600
    yBorder: 80,                    // Offset of the area's border
    xBorder: 0,                     // Offset of the area's border
    playerSpawnPoint: [300, 540],   // Where the player will spawn
    playerMoveDis: 40,              // Store the distance the player should move
    playerRadius: 18,               // Store the radius of the player (make sure this syncs with whats in the html)
    enemyHeights: 40,               // Store the height of every non-player objects
    enemySizeMargins: 2,            // Store the margins of every non-player objects
    riverStart: 280,                // Store the y-location of where the river section starts
    riverEnd: 120,                  // Store the y-location of where the river section ends
    pointsReward: 200               // Store the amount of points rewarded for each goal reached
    // carSize: [38, 78],           // Store car size [0] for height and [1] for width
    // kartsSize: [38, 48],         // Same for karts
    // vansSize: [38, 93],          // Same for vans
    // truckSize: [38, 158]         // Same for trucks
  } as const;
  
  /**
   * All possible info of any circle objects
   * Code derived from the Astreoid FRP
   */
  type Circle = Readonly<{pos_x:number, pos_y:number}>

  /**
   * All possible info of any square objects
   * Code derived from the Astreoid FRP
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
    disappearRound: number | null,
    lives: number,
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
      disappearRound: null,
      lives: 3,
      collidingEvent: Identity
    };
  }

  /**
   * A function that makes several enemies at once, made so that 'createEnemy' don't need to be called so many times
   * 
   * @returns A list of bodies
   */
  const createEnemies = 
    (size: number, sizeWidthMulti: number, color: String, enemyID: String, shapeID: String, movementSpeed: number) =>
    (positionsX: Readonly<number[]>, positionsY: Readonly<number>[], showRounds: Readonly<number>[], disappearRound: Readonly<number | null>[], counter: number, collidingFunction: (_:Body) => Body, enemies: Body[] = []): Body[] =>
      {
        return counter > -1 ? 
          createEnemies(size, sizeWidthMulti, color, enemyID, shapeID, movementSpeed)(positionsX, positionsY, showRounds, disappearRound, counter - 1, collidingFunction, [createEnemy(positionsX[counter], positionsY[counter], showRounds[counter], disappearRound[counter], movementSpeed, enemyID, shapeID)(color, size, sizeWidthMulti)(collidingFunction)].concat(enemies))
        :
          enemies
      }
          
  /**
  * A function that creates an enemy!
  * 
  * @returns A single body
  */
  const createEnemy = (xPosition: number, yPosition: number, showRound: number, disappearRound: number | null, movementSpeed: number, enemyID: String, shapeID: String) => 
                      (color: String, size: number, sizeWidthMulti: number) => (collidingFunction: (_:Body) => Body, livesAmount: number = 0) =>
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
      disappearRound: disappearRound,
      color: color,
      collidingEvent: collidingFunction,
      lives: livesAmount
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
       const bodyShowRound = curBody.showRound;
       const bodyHideRound = curBody.disappearRound;

       return bodyHideRound ? 
          currentRound >= bodyHideRound ? false : bodyShowRound <= currentRound
        :
          bodyShowRound <= currentRound
     }
  
  /**
   * A function to reset a body to their spawn position, harmlessly
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
   * A function to reset a body to their spawn position, harmfully
   * 
   * @param player 
   * @returns 
   */
  const harmfulReset = (player: Body) => <Body>
  {
    ...player,
    pos_x: Constants.playerSpawnPoint[0],
    pos_y: Constants.playerSpawnPoint[1],
    lives: player.lives - 1
  }

  /**
   * A function that 'destroys' 'otherBody'
   * 
   * @param otherBody This function is specifically for non-players
   * @returns 
   */
  const Destroy = (otherBody: Body): Body =>
  {
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

    return roundDone >= theState.wins.length ? true : false
  }

  /**
   * A function that checks if a single body has touched one of the elements in a list of bodies
   * 
   * @param bodyList The list of bodies
   * @param bodySingle The one body
   * @param theState Needed for the 'isColliding' function
   * @param counter A counter
   * @returns True for 'yes it has' false for 'no it hasnt'
   */
  const touchedOne = (bodyList: ReadonlyArray<Body>, bodySingle: Body, theState: State, counter:number = bodyList.length): boolean =>
  {
    return counter <= 0 ? false : isColliding(bodyList[counter - 1], bodySingle, theState) ? true : touchedOne(bodyList, bodySingle, theState, counter - 1)
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
   * A function that checks if the player is still alive
   * True for they are and false if they aren't
   * 
   * @param player 
   * @returns 
   */
  const playerCheck = (player: Body): boolean =>
  {
    return player.lives <= 0 ? false : true
  }

  /**
   * A function that executes all the nessecary events for a game over
   * Giving the player a set position will cause it to get stuck
   * 
   * @param theState 
   * @returns 
   */
  const gameOver = (theState: State) => <State>
  {
    ...theState,
    frog:
      {
        ...theState.frog,
        pos_x: Constants.playerSpawnPoint[0],
        pos_y: Constants.playerSpawnPoint[1]
      },
    highscore: theState.score > theState.highscore ? theState.score : theState.highscore,
    gameOver: true
  }

  /**
   * An empty body for when something went terrible wrong
   */
  const emptyBody = createEnemy(0,0,0,null,0, "Wrong", "rect")("white", 300, 1)(Identity)
  
  /**
   * The game state type, taken from the Astreoid FRP
   */
  type State = Readonly<{
    time:number,
    score: number,
    highscore: number,
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
    crocodiles1: ReadonlyArray<Body>,
    crocodiles1Head: ReadonlyArray<Body>,
    crocodiles2: ReadonlyArray<Body>,
    crocodiles2Head: ReadonlyArray<Body>,
    turtles1: ReadonlyArray<Body>,
    turtles2: ReadonlyArray<Body>,
    rounds: number,
    gameOver:boolean
  }>

  /**
   * The initial state of the game
   */
  const initialState: State = 
  {
    time: 0,
    score: 0,
    highscore: 0,
    frog: createPlayer(),
    cars: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "orange", "car", "rect", 2.5)([200, 400, 600], [440, 440, 440], [0,2,0], [null, null, null], 2, harmfulReset),
    karts: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 1.3, "lightgreen", "karts", "rect", -4)([100, 250, 450, 600], [400, 400, 400, 400], [0,3,3,2], [null, null, null, null], 3, harmfulReset),
    vans: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2.3, "white", "vans", "rect", 2)([100, 300, 500], [360, 360, 360], [0,2,0], [null, null, null], 2, harmfulReset),
    trucks: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 4, "red", "trucks", "rect", -1.8)([0, 250, 500], [320, 320, 320], [0,2,0], [null, null, null], 2, harmfulReset),
    logs1: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 4, "brown", "logs1", "rect", 2)([0, 250, 500], [240, 240, 240], [0,0,0], [3, 2, 4], 2, (body) => moveBody(body, 2)),
    logs2: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "brown", "logs2", "rect", -3)([0, 160, 320, 480], [200, 200, 200, 200], [0,0,0,0], [4, 2, 3, 4], 3, (body) => moveBody(body, -3)),
    logs3: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 4, "brown", "logs3", "rect", 2)([0, 250, 500], [160, 160, 160], [0,0,0], [2, 4, 2], 2, (body) => moveBody(body, 2)),
    logs4: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "brown", "logs4", "rect", -1.8)([0, 160, 320, 480], [120, 120, 120, 120], [0,0,0,0], [3, 4, 4, 2], 3, (body) => moveBody(body, -1.8)),
    wins: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "cyan", "win", "rect", 0)([70, 270, 470], [80, 80, 80], [0,0,0], [null, null, null], 2, Reset),
    riverWins: createEnemies(Constants.enemyHeights, 2, "blue", "riverWin", "rect", 0)([0, 146, 190, 346, 390, 546], [79.5, 79.5, 79.5, 79.5, 79.5, 79.5],[0,0,0,0,0,0], [null, null, null, null, null, null], 5, harmfulReset),
    snake: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins - 30, 10, "lightgreen", "snake", "rect", 0.8)([0], [295], [3], [null], 0, harmfulReset),
    crocodiles1: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 4, "darkbrown", "crocodiles1", "rect", 2)([0, 500], [240, 240], [3, 4], [null, null], 1, (body) => moveBody(body, 2)),
    crocodiles1Head: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 1, "darkred", "crocodiles1heads", "rect", 2)([120, 620], [240, 240], [3, 4], [null, null], 1, harmfulReset),
    crocodiles2: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 4, "darkbrown", "crocodiles1", "rect", 2)([250], [160], [4], [null], 0, (body) => moveBody(body, 2)),
    crocodiles2Head: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 1, "darkred", "crocodiles1heads", "rect", 2)([370], [160], [4], [null], 0, harmfulReset),
    turtles1: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "lightgreen", "turtles1", "rect", -3)([0, 160, 320, 480], [200, 200, 200, 200], [4, 2, 3, 4], [null, null, null, null], 3, (body) => moveBody(body, -3)),
    turtles2: createEnemies(Constants.enemyHeights - Constants.enemySizeMargins, 2, "lightgreen", "turtles2", "rect", -1.8)([0, 160, 320, 480], [120, 120, 120, 120], [3, 4, 4, 2], [null, null, null, null], 3, (body) => moveBody(body, -1.8)),
    rounds: 1,
    gameOver: false
  }

  /**
   * What happens on each state in relation to the events
   */
  const currentState = (curState: State, curEvent: Move | Tick | FullReset): State =>  
    // For when we are in a game over
    curEvent instanceof FullReset ?
      curState.gameOver ?
      {
        ...curState,
        frog:
          {
            ...curState.frog,
            pos_x: Constants.playerSpawnPoint[0],
            pos_y: Constants.playerSpawnPoint[1],
            lives: 3
          },
        wins: curState.wins.map((win) => win.pos_y < 0 ? Destroy(win) : win),
        rounds: 1,
        score: 0,
        gameOver: false
      }
      :
      {
        ...curState
      }
    :
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
                      concat(curState.wins).concat(curState.riverWins).concat(curState.crocodiles1).concat(curState.crocodiles1Head).
                      concat(curState.crocodiles2).concat(curState.crocodiles2Head).concat(curState.turtles1).concat(curState.turtles2);
    
    //Check if the player is still alive
    return playerCheck(curState.frog) ?
    // Check if round should continue or reset to the next one
    roundChecker(curState) ? 
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
      wins: curState.wins.map(win => isColliding(win, curState.frog, curState) ? Destroy(win) : win),
      snake: curState.snake.map(snake => moveBody(snake)),
      crocodiles1: curState.crocodiles1.map(crocodiles => moveBody(crocodiles)),
      crocodiles1Head: curState.crocodiles1Head.map(crocodiles => moveBody(crocodiles, crocodiles.speed, 114)),
      crocodiles2: curState.crocodiles2.map(crocodiles => moveBody(crocodiles)),
      crocodiles2Head: curState.crocodiles2Head.map(crocodiles => moveBody(crocodiles, crocodiles.speed, 114)),
      turtles1: curState.turtles1.map(turtles => flickingMoving(100, curState, turtles)),
      turtles2: curState.turtles2.map(turtles => flickingMoving(150, curState, turtles)),
      frog: collisionsHandler(allBodies, curState.frog, curState),
      score: touchedOne(curState.wins, curState.frog, curState) ? curState.score + Constants.pointsReward : curState.score,
      time: elapsed
    }
    :
    gameOver(curState)
  }

  /**
   * Checks collisions between a single body with a list of bodies
   * If true, play the given function, if false, body is left alone
   * 
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
          harmfulReset(singleBodyAux)  //In river but not colliding a body
        : 
        collided ? 
          filteredCollidedBodies[0] ? filteredCollidedBodies[0].collidingEvent(singleBodyAux) : singleBodyAux  //Not in river and colliding with a body
          : 
          singleBodyAux   //Not in river and not colliding with a body
      
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
   * @param collider The instigator (recommended: enemy here)
   * @param collidee The victim (recommended: player here)
   * @returns True for they did and false for nah
   */
  const isColliding = (collider: Body, collidee: Body, theState: State): boolean =>
  {
    return collidee.pos_x < collider.pos_x || collidee.pos_x > (collider.pos_x + ((collider.size * collider.sizeWidthMulti) - Constants.enemySizeMargins)) 
            ? false : collidee.pos_y < collider.pos_y || collidee.pos_y > (collider.pos_y + (collider.size - Constants.enemySizeMargins)) 
            ? false : true && existOnRound(theState, collider)

  }

  //Fully player related logic starts here
  /**
   * All possible keys
   */
  type Key = 'KeyA' | 'KeyD' | 'KeyW' | 'KeyS' | 'KeyR' 
  
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
    
    //All possible key events
    moveLeft = keyPressed('keydown', 'KeyA', () => new Move(-Constants.playerMoveDis, 0)),
    moveRight = keyPressed('keydown', 'KeyD', () => new Move(Constants.playerMoveDis, 0)),
    moveUp = keyPressed('keydown', 'KeyW', () => new Move(0, -Constants.playerMoveDis)),
    moveDown = keyPressed('keydown', 'KeyS', () => new Move(0, Constants.playerMoveDis)),
    restart = keyPressed('keydown', 'KeyR', () => new FullReset())
  
  //Fully player related logic ends here

  /**
   * For moving any bodies that isn't controlled by the player, only works horizontally
   * Code derived from the Astreoid FRP
   * 
   * @param curBody The current body being managed
   * @param speed The body's speed
   * @param customClamp For specific situations where you wouldn't want a body to warp the screen immdiately 
   * @returns A moving body
   */
  const moveBody = (curBody: Body, speed: number = curBody.speed, customClamp: number = 0) => <Body>
  {
      ...curBody,
    // This 'if statement' is to give the objects the ability to warp when out of bounds
    pos_x: speed >= 0 ? // For objects moving left to right
      curBody.pos_x + speed > Constants.CanvasSize + customClamp  ? 
      0 - (curBody.size * curBody.sizeWidthMulti)
      : 
      curBody.pos_x + speed
    :
      // For objects moving right to left
      curBody.pos_x + speed < 0 - (curBody.size * curBody.sizeWidthMulti) ?
        Constants.CanvasSize + customClamp
        :
        curBody.pos_x + speed
  }

  /**
   * A special type of 'moveBody' that gives a body the ability to 'flicker' while moving
   * 
   * @param timeFlick The interval of the flicker
   * @param curState The current state being evaluated
   * @param curBody The current body being evaluated
   * @param speed The speeed of the body
   * @param customClamp For specific situations where you wouldn't want a body to warp the screen immdiately 
   * @returns A flickering moving body
   */
  const flickingMoving = (timeFlick: number, curState: State, curBody: Body, speed: number = curBody.speed, customClamp: number = 0): Body =>
  {
    const movedBody = moveBody(curBody)

    return curState.time % timeFlick == 0 ? Destroy(movedBody) : movedBody
  }

  /**
   * Determines if the given positions are a valid position in the canvas,
   * if false then it is not and if true then it is
   * 
   * Suggested to be given for the player
   * 
   * @param curPosx The x position to be evaluated
   * @param curPosy The y position to be evaluated
   * @returns a boolean
   */
  const clamp = (curPosx: number, curPosy: number): boolean =>
  {
    return curPosx < Constants.CanvasSize && curPosx > 0 && curPosy < Constants.CanvasSize && curPosy - Constants.yBorder > 0 ? true : false
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
    restart,
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
    livesText.textContent = String("Lives: " + theState.frog.lives);
    scoreText.textContent = String("Score: " + theState.score);
    highscoreText.textContent = String("High Score: " + theState.highscore);
    
    const
      //Update all bodies on screen
      updateBodyView = (body: Body, canvas: HTMLElement) => 
      {
        //Recreate the html object in case it doesn't spawn for this frame
        const createBodyView = () =>
        {
          //Store the shape
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
          updateBody.setAttribute("width", String(body.size * body.sizeWidthMulti)), 
          updateBody.setAttribute("height", String(body.size))
        }

        //Body shouldn't be rendered
        const notRendered = () =>
        {
          updateBody.setAttribute("x", String(body.pos_x))
          updateBody.setAttribute("y", String(body.pos_y))
          updateBody.setAttribute("width", String(0)), 
          updateBody.setAttribute("height", String(0))
        }
        
        const updateBody = document.getElementById(body.ID) || createBodyView()

        //Check if the body shoult even exist
        existOnRound(theState, body) ?
          //They should
          body.shapeID == "ellipse" ?
          updateEllipse()
          :
          updatingRect()
        :
          //They shouldn't
          notRendered()
      }
    
    // Update all bodies 
    theState.cars.forEach((body) => updateBodyView(body, svg))
    theState.karts.forEach((body) => updateBodyView(body, svg))
    theState.vans.forEach((body) => updateBodyView(body, svg))
    theState.trucks.forEach((body) => updateBodyView(body, svg))
    theState.logs1.forEach((body) => updateBodyView(body, svg))
    theState.logs2.forEach((body) => updateBodyView(body, svg))
    theState.logs3.forEach((body) => updateBodyView(body, svg))
    theState.logs4.forEach((body) => updateBodyView(body, svg))
    theState.wins.forEach((body) => updateBodyView(body, svg))
    theState.riverWins.forEach((body) => updateBodyView(body, svg))
    theState.snake.forEach((body) => updateBodyView(body, svg))
    theState.crocodiles1.forEach((body) => updateBodyView(body, svg))
    theState.crocodiles1Head.forEach((body) => updateBodyView(body, svg))
    theState.crocodiles2.forEach((body) => updateBodyView(body, svg))
    theState.crocodiles2Head.forEach((body) => updateBodyView(body, svg))
    theState.turtles1.forEach((body) => updateBodyView(body, svg))
    theState.turtles2.forEach((body) => updateBodyView(body, svg))
    updateBodyView(theState.frog, svg)

    // React accordingly to the game over
    const showGameOverText = () =>
    {
      gameRestartText.setAttribute("text-anchor", "middle")
      gameRestartText.setAttribute("x", String(300))
      gameRestartText.setAttribute("y", String(400))
      gameRestartText.setAttribute("fill", "red")
      gameRestartText.setAttribute("style", "font-size: 60")
      gameRestartText.textContent = "Press R to Restart"
      svg.appendChild(gameRestartText)
      
      gameOverText.setAttribute("text-anchor", "middle")
      gameOverText.setAttribute("x", String(300))
      gameOverText.setAttribute("y", String(300))
      gameOverText.setAttribute("fill", "red")
      gameOverText.setAttribute("style", "font-size: 100")
      gameOverText.textContent = "Game over"
      svg.appendChild(gameOverText)
    }

    const hideGameOverText = () =>
    {
      gameRestartText.setAttribute("style", "font-size: 0")
      gameRestartText.textContent = ""
      svg.appendChild(gameRestartText)
      
      gameOverText.setAttribute("style", "font-size: 0")
      gameOverText.textContent = ""
      svg.appendChild(gameOverText)
    }
    
    theState.gameOver ?
      showGameOverText()
    :
      hideGameOverText()

  }

  // UI Elements here
  const rounds = document.getElementById("rounds")!;
  const livesText = document.getElementById("lives")!;
  const scoreText = document.getElementById("score")!;
  const highscoreText = document.getElementById("highscore")!;
  const gameOverText = document.createElementNS(svg.namespaceURI, "text")!;
  const gameRestartText = document.createElementNS(svg.namespaceURI, "text")!;

}

// The following simply runs your main function on window load.  Make sure to leave it in place.
if (typeof window !== "undefined") {
  window.onload = () => {
    main();
  }
}
