/**
 * Surname     | Firstname | Contribution % | Any issues?
 * =====================================================
 * Person 1... |           | 25%            |
 * Person 2... |           | 25%            |
 * Person 3... |           | 25%            |
 * Person 4... |           | 25%            |
 *
 * Please do not hesitate to contact your tutors if there are
 * issues that you cannot resolve within the group.
 *
 * Complete Worksheet 4/5 by entering code in the places marked below...
 *
 * For full instructions and tests open the file observableexamples.html
 * in Chrome browser.  Keep it open side-by-side with your editor window.
 * You will edit this file (observableexamples.ts), save it, build it, and reload the
 * browser window to run the test.
 */

import { interval, fromEvent, merge } from "rxjs";
import { map, filter, subscribeOn } from "rxjs/operators";

// Simple demonstration
// ===========================================================================================
// ===========================================================================================
/**
 * an example of traditional event driven programming style - this is what we are
 * replacing with observable.
 * The following adds a listener for the mouse event
 * handler, sets p and adds or removes a highlight depending on x position
 */
function mousePosEvents() {
  const pos = document.getElementById("pos")!;

  document.addEventListener("mousemove", ({ clientX, clientY }) => {
    const p = clientX + ", " + clientY;
    pos.innerHTML = p;
    if (clientX > 400) {
      pos.classList.add("highlight");
    } else {
      pos.classList.remove("highlight");
    }
  });
}

/**
 * constructs an Observable event stream with three branches:
 *   Observable<x,y>
 *    |- set <p>
 *    |- add highlight
 *    |- remove highlight
 */
function mousePosObservable() {
  const pos = document.getElementById("pos")!,
    o = fromEvent<MouseEvent>(document, "mousemove").pipe(
      map(({ clientX, clientY }) => ({ x: clientX, y: clientY }))
    );

  o.pipe(map(({ x, y }) => `${x},${y}`)).subscribe(
    (s: string) => (pos.innerHTML = s)
  );

  o.pipe(filter(({ x }) => x > 400)).subscribe((_) =>
    pos.classList.add("highlight")
  );

  o.pipe(filter(({ x }) => x <= 400)).subscribe(({ x, y }) => {
    pos.classList.remove("highlight");
  });
}

// Exercise 5
// ===========================================================================================
// ===========================================================================================
function piApproximation() {
  // a simple, seedable, pseudo-random number generator
  class RNG {
    // LCG using GCC's constants
    m = 0x80000000; // 2**31
    a = 1103515245;
    c = 12345;
    state: number;
    constructor(seed: number) {
      this.state = seed ? seed : Math.floor(Math.random() * (this.m - 1));
    }
    nextInt() {
      this.state = (this.a * this.state + this.c) % this.m;
      return this.state;
    }
    nextFloat() {
      // returns in range [0,1]
      return this.nextInt() / (this.m - 1);
    }
  }

  const resultInPage = document.getElementById("value_piApproximation"),
    canvas = document.getElementById("piApproximationVis");

  if (!resultInPage || !canvas) {
    console.log("Not on the observableexamples.html page");
    return;
  }

  // Some handy types for passing data around
  type Colour = "red" | "green" | "blue"; //please use colorblind friendly palletes
  type Dot = { x: number; y: number; colour?: Colour };
  interface Data {
    point?: Dot;
    insideCount: number;
    totalCount: number;
  }

  // an instance of the Random Number Generator with a specific seed
  const rng = new RNG(20);
  // return a random number in the range [-1,1]
  const nextRandom = () => rng.nextFloat() * 2 - 1;
  // you'll need the circleDiameter to scale the dots to fit the canvas
  const circleRadius = Number(canvas.getAttribute("width")) / 2;
  // test if a point is inside a unit circle
  // Radius of the circle isnt unit, its the canvas and also circle is centered at (0,0)
  const inCircle = ({ x, y }: Dot) => (x - circleRadius) * (x - circleRadius) + (y - circleRadius) * (y - circleRadius) <= circleRadius * circleRadius;
  // you'll also need to set innerText with the pi approximation
  resultInPage.innerText =
    "Pi Approximation here";

  // Your code starts here!
  // =========================================================================================
  function createDot(x_pos: number, y_pos: number, dot_colour?: Colour) {
    if (!canvas) throw "Couldn't get canvas element!";
    const dot = document.createElementNS(canvas.namespaceURI, "circle");
    
    const x = x_pos,
      y = y_pos; // all points are at 50,50!
    
      // Set circle properties
    dot.setAttribute("cx", String(x));
    dot.setAttribute("cy", String(y));
    dot.setAttribute("r", "5");
    dot.setAttribute("fill", dot_colour); // All points red

    // Add the dot to the canvas
    canvas.appendChild(dot);
  }

  // A stream of random numbers that are valid positions
  const randomNumberStream = interval(50).pipe(
    map(
      ()=>[Math.abs(nextRandom()) * circleRadius * 2, Math.abs(nextRandom()) * circleRadius * 2]
    )
  );
  
  // Resetting the page to update texts after every new dot
  randomNumberStream.subscribe(
    ([x, y]: number[]) =>{
      resultInPage.innerText = String(piResult(x, y))
    }
  );

  //Count result
  const piData = 
  {
    inCircle: 0,
    totalCircle: 0
  }
  
  //Calculate result
  function piResult(x_pos: number, y_pos: number) : number
  {
    createDot(x_pos, y_pos, inCircle({x: x_pos, y: y_pos}) ? "blue" : "red");
    
    piData.totalCircle += 1;
    inCircle({x: x_pos, y: y_pos}) ? piData.inCircle += 1 : piData.inCircle += 0;

    return piShowReslt();
  }

  //Show result
  function piShowReslt() : number
  {
    return (piData.inCircle / piData.totalCircle) * 4;
  }

}

// Exercise 6
// ===========================================================================================
// ===========================================================================================
/**
 * animates an SVG rectangle, passing a continuation to the built-in HTML5 setInterval function.
 * a rectangle smoothly moves to the right for 1 second.
 */
function animatedRectTimer() {
  // get the svg canvas element
  const svg = document.getElementById("animatedRect")!;
  // create the rect
  const rect = document.createElementNS(svg.namespaceURI, "rect");
  Object.entries({
    x: 100,
    y: 70,
    width: 120,
    height: 80,
    fill: "#95B3D7",
  }).forEach(([key, val]) => rect.setAttribute(key, String(val)));
  svg.appendChild(rect);

  const animate = setInterval(
    () => rect.setAttribute("x", String(1 + Number(rect.getAttribute("x")))),
    10
  );
  const timer = setInterval(() => {
    clearInterval(animate);
    clearInterval(timer);
  }, 1000);
}

/**
 * Demonstrates the interval method
 * You want to choose an interval so the rectangle animates smoothly
 * It terminates after 1 second (1000 milliseconds)
 */
function animatedRect() {
  // Your code starts here!
  // =========================================================================================
  // Note: this block is just copy pasted from animatedRectTimer()
  // get the svg canvas element
  const svg = document.getElementById("animatedRect")!;
  // create the rect
  const rect = document.createElementNS(svg.namespaceURI, "rect");
  Object.entries({
    x: 100,
    y: 70,
    width: 120,
    height: 80,
    fill: "#95B3D7",
  }).forEach(([key, val]) => rect.setAttribute(key, String(val)));
  svg.appendChild(rect);

  //Animation starts here
  //Create a stream of 1s every 5ms
  const numberStream = interval(5).pipe(map (() => 1));
  
  //Capture where the square was before animation starts
  const initialX = rect.getAttribute("x");

  //The square movement starts here
  numberStream.subscribe
  (
    //Subscribe to the streams of 1s
    (x: number) => 
      //Check if one second has passed
      //Calculated by 'one second' / 'interval of stream'
      Number(rect.getAttribute("x")) - Number(initialX) == 200 ? 
        //When one second has passed, do nothing
        0 
        : 
        //Add the position of the square by one
        rect.setAttribute("x", String(x + Number(rect.getAttribute("x"))))
  )
  
}

// Exercise 7
// ===========================================================================================
// ===========================================================================================
/**
 * Create and control a rectangle using the keyboard! Use only one subscribe call and not the interval method
 * If statements
 */
function keyboardControl() {
  //Get the svg canvas element
  const svg = document.getElementById("moveableRect")!;

  //Your code starts here!
  //=========================================================================================
  //Make da square
  const rect = document.createElementNS(svg.namespaceURI, "rect");
  Object.entries({
    x: 70,
    y: 70,
    width: 80,
    height: 80,
    fill: "#95B3D7",
  }).forEach(([key, val]) => rect.setAttribute(key, String(val)));
  svg.appendChild(rect);

  //Make fromEvents
  const key$ = fromEvent<KeyboardEvent>(document, "keydown");
  
  //Store all of the possible key inputs
  const isMovingUp = (x: String) => x == 'w' || x == 'ArrowUp' ? 'up' : 'null';
  const isMovingRight = (x: String) => x == 'd' || x == 'ArrowRight' ? 'right' : 'null';
  const isMovingDown = (x: String) => x == 's' || x == 'ArrowDown' ? 'down' : 'null';
  const isMovingLeft = (x: String) => x == 'a' || x == 'ArrowLeft' ? 'left' : 'null';
  
  //Each action are merged and call the function uniquely
  merge
  (
    key$.pipe(map(x => isMovingUp(x.key))),
    key$.pipe(map(x => isMovingRight(x.key))),
    key$.pipe(map(x => isMovingDown(x.key))),
    key$.pipe(map(x => isMovingLeft(x.key)))
  ).subscribe((x:String) => Moving(x, 20))
  
  //Logic of the movement is executed here!
  //Note: this is all imperative code and I think it's bad,
  //      but it's movement is perfect for frogger
  function Moving(movementType: String, movementAmount: number)
  {
    if(movementType == "up")
    {
      rect.setAttribute("y", String(-movementAmount + Number(rect.getAttribute("y"))))
    } 
    
    if(movementType == "right")
    {
      rect.setAttribute("x", String(movementAmount + Number(rect.getAttribute("x"))))
    }

    if(movementType == "down")
    {
      rect.setAttribute("y", String(movementAmount + Number(rect.getAttribute("y"))))
    }

    if(movementType == "left")
    {
      rect.setAttribute("x", String(-movementAmount + Number(rect.getAttribute("x"))))
    }
  }
}

// Running the code
// ===========================================================================================
// ===========================================================================================
document.addEventListener("DOMContentLoaded", function (event) {
  piApproximation();

  // compare mousePosEvents and mousePosObservable for equivalent implementations
  // of mouse handling with events and then with Observable, respectively.
  //mousePosEvents();
  mousePosObservable();

  //animatedRectTimer();
  // replace the above call with the following once you have implemented it:
  animatedRect()
  keyboardControl();
});
