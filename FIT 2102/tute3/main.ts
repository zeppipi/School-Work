/**
 * Surname     | Firstname | Contribution % | Any issues?
 * =====================================================
 * Christian   | Hazael    | 16.7%          |
 * Boodnah     | Riddhi    | 16.7%          |
 * Malini      | Felicia   | 16.7%          |
 * Fadi        | Alailan   | 16.7%          |
 * Lim         | Yun Feng  | 16.7%          |
 * Rakhman     | Kemala    | 16.7%          |
 *
 * Please do not hesitate to contact your tutors if there are
 * issues that you cannot resolve within the group.
 *
 * Complete the worksheet by entering code in the places marked below...
 *
 * For full instructions and tests open the file worksheetChecklist.html
 * in Chrome browser.  Keep it open side-by-side with your editor window.
 * You will edit this file, save it, compile it, and reload the
 * browser window to run the test.
 */

// Stub value to indicate an implementation
const IMPLEMENT_THIS: any = undefined;

/*****************************************************************
 * Exercise 1
 */

function addStuff(a: number, b: number) {
  return a + b;
}
function numberToString(input: string) {
  return JSON.stringify(input);
}

/**
 * Takes a string and adds "padding" to the left.
 * If 'padding' is a string, then 'padding' is appended to the left side.
 * If 'padding' is a number, then that number of spaces is added to the left side.
 */
function padLeft(value: string, padding: number | string) {
  if (typeof padding === "number") {
    return Array(padding + 1).join(" ") + value;
  }
  if (typeof padding === "string") {
    return padding + value;
  }
  throw new Error(`Expected string or number, got '${padding}'.`);
}

padLeft("Hello world", 4); // returns "    Hello world"

// What's the type of arg0 and arg1?
function curry<U,V,W>(f: (x: U, y: V) => W) {
  return function (x: U) {
    return function (y: V) {
      return f(x, y);
    };
  };
}

/*****************************************************************
 * Exercise 2: implement the map function for the cons list below
 */

/**
 * A ConsList is either a function created by cons, or empty (null)
 */
type ConsList<T> = Cons<T> | null;

/**
 * The return type of the cons function, is itself a function
 * which can be given a selector function to pull out either the head or rest
 */
type Cons<T> = (selector: Selector<T>) => T | ConsList<T>;

/**
 * a selector will return either the head or rest
 */
type Selector<T> = (head: T, rest: ConsList<T>) => T | ConsList<T>;

/**
 * cons "constructs" a list node, if no second argument is specified it is the last node in the list
 */
function cons<T>(head: T, rest: ConsList<T>): Cons<T> {
  return (selector: Selector<T>) => selector(head, rest);
}

/**
 * head selector, returns the first element in the list
 * @param list is a Cons (note, not an empty ConsList)
 */
function head<T>(list: Cons<T>): T {
  if (!list) throw new TypeError("list is null");
  return <T>list((head, rest?) => head);
}

/**
 * rest selector, everything but the head
 * @param list is a Cons (note, not an empty ConsList)
 */
function rest<T>(list: Cons<T>): ConsList<T> {
  if (!list) throw new TypeError("list is null");
  return <Cons<T>>list((head, rest?) => rest);
}

/**
 * Use this as an example for other functions!
 * @param f Function to use for each element
 * @param list Cons list
 */
function forEach<T>(f: (_: T) => void, list: ConsList<T>): void {
  if (list) {
    f(head(list));
    forEach(f, rest(list));
  }
}

/**
 * Implement this function! Also, complete this documentation (see forEach).
 * 
 * Places the process of @param f onto every element in the cons list @param l
 * @param f Function that will be placed on every element of the conslist
 * @param l The conslist
 */
function map<T, V>(f: (_: T) => V, l: ConsList<T>): ConsList<V> {
  const ConsListCopy = (listCopy: ConsList<T>): ConsList<V> => listCopy ? cons(f(head(listCopy)), ConsListCopy(rest(listCopy))) : null;
  return ConsListCopy(l);
}

/*****************************************************************
 * Exercise 3
 */

// Example use of reduce
function countLetters(stringArray: string[]): number {
  const list = fromArray(stringArray);
  return reduce((len: number, s: string) => len + s.length, 0, list);
}
console.log(countLetters(["Hello", "there!"]));

/**
 * Turns an ordinary list into a ConsList
 * @param normalList The normal list that will be transformed
 * @returns A new conslist that has the elements from the normal list
 */
function fromArray<A>(normalList: A[]): ConsList<A> 
{
  const newConstList = (enteredList: A[]): ConsList<A> => enteredList.length ? cons(enteredList[0], newConstList(enteredList.slice(1, enteredList.length))) : null;
  return newConstList(normalList);
}

/**
 * Reduces a ConsList into a single value based on the behaviour given
 * @param reduceBehaviour The behaviour on how the elements will reduce
 * @param initialValue What value should the reduce start with
 * @param inputList The list to be reduced
 * @returns A single value
 */
function reduce<A, B>(reduceBehaviour: (x : B, y : A) => B, initialValue: B, inputList: ConsList<A>) : B
{
  return inputList ? reduce(reduceBehaviour, reduceBehaviour(initialValue, head(inputList)), rest(inputList)) : initialValue;
}

/**
 * Reduces a ConsList from the right side into a single value based on the behaviour given
 * @param reduceBehaviour The behaviour on how the elements will reduce
 * @param initialValue What value should the reduce start with
 * @param inputList The list to be reduced
 * @returns A single value
 */
function reduceRight<A, B>(reduceBehaviour: (x : B, y : A) => B, initialValue: B, inputList: ConsList<A>) : B
{
  return inputList ? reduceBehaviour(reduceRight(reduceBehaviour, initialValue, rest(inputList)), head(inputList)) : initialValue
}

/**
 * Filters out the ConsList based on the expression given
 * @param filterBehaviour The expression that determines which elements should be dropped
 * @param inputList The list to be filtered
 * @returns Another ConsList
 */
function filter<A>(filterBehaviour: (regex: A) => boolean, inputList: ConsList<A>): ConsList<A>
{
  const res = (listCopy: ConsList<A>) : ConsList<A> => listCopy ? filterBehaviour(head(listCopy)) ? cons(head(listCopy), res(rest(listCopy))) : res(rest(listCopy)) : null;
  return res(inputList);
}

/**
 * Combines two ConstLists together ConsLists
 * @param list1 The first ConsList
 * @param list2 The second ConsList
 * @returns list1 and list2 combined
 */
function concat<A>(list1: ConsList<A>, list2: ConsList<A>): ConsList<A>
{
  const res = (listCopy: ConsList<A>) : ConsList<A> => listCopy ? cons(head(listCopy), res(rest(listCopy))) : list2 ? cons(head(list2), rest(list2)) : null;
  return res(list1)
}

/**
 * Reverse the conslist
 * @param list The list to be reversed
 * @returns The reversed list
 */
 function reverse<T>(list: ConsList<T>): ConsList<T>
{
  const res = (currentList : ConsList<T>, previousList : ConsList<T>) : ConsList<T> => currentList ? res(rest(currentList), cons(head(currentList), previousList)) : previousList;
  return res(list, null);
}

/*****************************************************************
 * Exercise 4
 * 
 * Tip: Use the functions in exercise 3!
 */

/**
 * A linked list backed by a ConsList
 */
 class List<T> {
  private readonly head: ConsList<T>;

  constructor(list: T[] | ConsList<T>) {
    if (list instanceof Array) {
      this.head = fromArray(list);
    } else {
      // nullish coalescing operator
      // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Nullish_coalescing_operator
      this.head = list ?? null;
    }
  }

  /**
   * create an array containing all the elements of this List
   */
  toArray(): T[] {
    // Getting type errors here?
    // Make sure your type annotation for reduce()
    // in Exercise 3 is correct!
    return reduce((a, t) => [...a, t], <T[]>[], this.head);
  }

  // Add methods here:
  forEach(f: (_:T) => void): List<T>{
    forEach(f, this.head);
    return this;
  }

  filter(f: (_:T) => boolean): List<T>{
    return new List<T>(filter(f, this.head));
  }
  
  map<V>(f: (_:T) => V): List<V>{
    return new List<V>(map(f, this.head));
  }
  
  reduce<V>(f: (accumulator:V, t:T) => V, initialValue: V): V{
    return reduce(f, initialValue, this.head);
  }
  
  concat(other: List<T>): List<T>{
    return new List<T>(concat(this.head, other.head));
  }

  reverse(): List<T>{
    return new List(reverse(this.head));
  }

}

/*****************************************************************
 * Exercise 5
 */

/**
 * To convert a string to a line
 * @param str The str to create a data for
 * @returns The line
 */
 function line(str: string): [number, string]{
  return [0, str]
}

/**
 * Store a line in a list
 * @param line The line to be added
 * @returns The list with the line added
 */
function lineToList(line: [number, string]): List<[number, string]>{
  return new List<[number, string]>([line]);
}

/*****************************************************************
 * Exercise 6
 */

type BinaryTree<T> = BinaryTreeNode<T> | undefined;

class BinaryTreeNode<T> {
  constructor(
    public readonly data: T,
    public readonly leftChild?: BinaryTree<T>,
    public readonly rightChild?: BinaryTree<T>
  ) {}
}

// example tree:
const myTree = new BinaryTreeNode(
  1,
  new BinaryTreeNode(2, new BinaryTreeNode(3)),
  new BinaryTreeNode(4)
);

function nest<B>(indent: number, layout: List<[number, B]>): List<[number, B]>
{
  return layout.map((element: [number, B]) => [element[0] + indent, element[1]]);
}

// *** uncomment the following code once you have implemented List and nest function (above) ***

function prettyPrintBinaryTree<T>(node: BinaryTree<T>): List<[number, string]> {
    if (!node) {
        return new List<[number, string]>([])
    }
    const thisLine = lineToList(line(node.data.toString())),
          leftLines = prettyPrintBinaryTree(node.leftChild),
          rightLines = prettyPrintBinaryTree(node.rightChild);
    return thisLine.concat(nest(1, leftLines.concat(rightLines)))
}

const output = prettyPrintBinaryTree(myTree)
                    .map(aLine => new Array(aLine[0] + 1).join('-') + aLine[1])
                    .reduce((a,b) => a + '\n' + b, '').trim();
console.log(output);

/*****************************************************************
 * Exercise 7: Implement prettyPrintNaryTree, which takes a NaryTree as input
 * and returns a list of the type expected by your nest function
 */

class NaryTree<T> {
  constructor(
    public data: T,
    public children: List<NaryTree<T>> = new List(undefined)
  ) {}
}

// Example tree for you to print:
const naryTree = new NaryTree(
  1,
  new List([
    new NaryTree(2),
    new NaryTree(3, new List([new NaryTree(4)])),
    new NaryTree(5),
  ])
);

// Implement: function prettyPrintNaryTree(...)
function prettyPrintNaryTree<T>(node: NaryTree<T>): List<[number, string]> {
  
  //An auxiliary function
  //The auxiliary function's paramteres and their types
  const auxPrettyPrintNaryTree = (node: NaryTree<T>, tab: number): List<[number, string]> =>
    
    //Function's funtionality begins here
    new List<[number, string]>([[tab, node.data.toString()]]).
      
      //Function calls concat for the one item in this scope below
      concat
      (
        //Calling this node's children
        node.children.
        
        //Inside the concat, a reduce function is called
        reduce
        (
          
          //The first element in the reduce is another function
          ((acc: List<[number, string]>, elem: NaryTree<T>): List<[number, string]> =>
            
            //The function in the first element is a recursive call to the auxiliary function
            acc.concat(auxPrettyPrintNaryTree(elem, tab + 1))), 
          
          //The second element in the reduce is a new empty list
          new List(undefined)
        )
      )
  ;
  
  return auxPrettyPrintNaryTree(node, 0);

}

// *** uncomment the following code once you have implemented prettyPrintNaryTree (above) ***

const outputNaryTree = prettyPrintNaryTree(naryTree)
                    .map(aLine => new Array(aLine[0] + 1).join('-') + aLine[1])
                    .reduce((a,b) => a + '\n' + b, '').trim();
console.log(outputNaryTree);

/*****************************************************************
 * Exercise 8 (Supplementary)
 */

type jsonTypes =
  | Array<jsonTypes>
  | { [key: string]: jsonTypes }
  | string
  | boolean
  | number
  | null;

const jsonPrettyToDoc: (json: jsonTypes) => List<[number, string]> = (json) => {
  if (Array.isArray(json)) {
    // Handle the Array case.
  } else if (typeof json === "object" && json !== null) {
    // Handle the object case.
    // Hint: use Object.keys(json) to get a list of
    // keys that the object has.
  } else if (typeof json === "string") {
    // Handle string case.
  } else if (typeof json === "number") {
    // Handle number
  } else if (typeof json === "boolean") {
    // Handle the boolean case
  } else if (json === null) {
    // Handle the null case
  }

  // Default case to fall back on.
  return new List<[number, string]>([]);
};

// *** uncomment the following code once you are ready to test your implemented jsonPrettyToDoc ***
// const json = {
//     unit: "FIT2102",
//     year: 2021,
//     semester: "S2",
//     active: true,
//     assessments: {"week1": null as null, "week2": "Tutorial 1 Exercise", "week3": "Tutorial 2 Exercise"},
//     languages: ["Javascript", "Typescript", "Haskell", "Minizinc"]
// }
//
// function lineIndented(aLine: [number, string]): string {
//     return new Array(aLine[0] + 1).join('    ') + aLine[1];
// }
//
// function appendLine(acc: string, nextLine: string): string {
//     return nextLine.slice(-1) === "," ? acc + nextLine.trim() :
//            acc.slice(-1) === ":"      ? acc + " " + nextLine.trim() :
//            acc + '\n' + nextLine;
// }
//
// console.log(jsonPrettyToDoc(json)
//               .map(lineIndented)
//               .reduce(appendLine, '').trim());

// *** This is what it should look like in the console ***
//
// {
//     unit: FIT2102,
//     year: 2021,
//     semester: S2,
//     active: true,
//     assessments: {
//         week1: null,
//         week2: Tutorial 1 Exercise,
//         week3: Tutorial 2 Exercise
//     },
//     languages: [
//         Javascript,
//         Typescript,
//         Haskell,
//         Minizinc
//     ]
// }
