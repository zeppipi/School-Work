// Surname     | Firstname | Contribution % | Any issues?
// =====================================================
// Christian   | Hazael    | 20%
// Lim         | Yun Feng  | 20%
// Rakhman     | Kemala    | 20%
// Boodnah     | Riddhi    | 20%
//             | Felicia   | 20%
//
// complete Worksheet 2 by entering code in the places marked below...
//
// For full instructions and tests open the file worksheetChecklist.html
// in Chrome browser.  Keep it open side-by-side with your editor window.
// You will edit this file (main.js), save it, and reload the
// browser window to run the test.

/**
 * Exercise 1
 */
const myObj = {
    aProperty: "string",
    anotherProperty: 123

};

/**
 * Exercise 2
 */
function operationOnTwoNumbers(func) {
    return function(x)
    {
        return function(y)
        {
            return func(x,y)
        }
    }
    //Note: when currying, use anonymous functions
}

/**
 * Exercise 3
 */
function callEach(array) {    
    array.forEach((current) => current())

    //Note: the equivalent of getting array[index] is the 'current'
}

/**
 * Exercise 4
 */
function addN(n, array)
{
    return array.map(currElement => operationOnTwoNumbers((x, y) => x + y)(n)(currElement))

    //Note: you can get the current element and the index of 'map' by just simply making more variables, 
    //The first would be the array's current element (array[index]) and the second would be the current iteration (index)
}

function getEvens(array) {
    return array.filter(currElement => currElement % 2 == 0)
}

function multiplyArray(array) {
    return array.reduce((total, num) => num ? total * num : total,1)

    //Note: total is an accumulator of the reduce, and num is the array[index]
}

/* Exercise 5
 */
function range(n){
    const resFunc = n => n ? [n-1].concat(resFunc(n - 1)) : []
    const res = resFunc(n)

    return res.reverse()

    //Note: be careful on using functions like 'reverse' when recursion is involved, 
    //it might just break because it got involved in the recursion
}

/**
 * Exercise 6
 */
function Euler1()
{
    const thousandArray = range(1000)
    const threefiveArray = thousandArray.filter(currElement => currElement % 3 == 0 || currElement % 5 == 0)
    const res = threefiveArray.reduce((total, num) => num ? total + num : total)

    return res
}

/**
 * Exercise 7
 */ 
function infinite_series_calculator(acc_func)           //accumulate
{
    return function predicate(pred_func)                //predicate
    {
        return function transform(trans_func)           //transform
        {
            return function cutoff(cut_n)               //n
            {
                const numberList = range(cut_n)
                const numberListMapped = numberList.map(currElement => trans_func(currElement))
                const numberListMappedFiltered = numberListMapped.filter(currElement => pred_func(currElement))
                const numberListMappedFilteredAccumulated = numberListMappedFiltered.reduce(acc_func)

                return numberListMappedFilteredAccumulated
            }
        }
    }
}

/**
 * Exercise 8
 */
function calculatePiTerm(n)
{
    return (4 * Math.pow(n, 2)) / ((4 * Math.pow(n, 2)) - 1)
}

function skipZero(n)
{
    return n ? true : false
}

function productAccumulate(x, y)
{
    return x * y
}

function calculatePi(n)
{
    return 2 * infinite_series_calculator(productAccumulate)(skipZero)(calculatePiTerm)(n)
}

const pi = calculatePi(1000)

//Note: the last sub-exercise which results in 'const pi' is bothced lol

/**
 * Exercise 9
 */
function factorial(n)
{
    return n ? n * factorial(n-1) : 1
}

function calculateETerm(n)
{
    return ((2 * (n + 1)) / factorial(2*n + 1))
}

function sumAccumulate(x, y)
{
    return x + y
}

function alwaysTrue()
{
    return true
}

function sum_series_calculator(transform)
{
    return function(n)
    {
        const numberList = range(n)
        const numberListMapped = numberList.map(currElement => transform(currElement))
        const numberListMappedAdded = numberListMapped.reduce(sumAccumulate)

        return numberListMappedAdded
    }
}

function calculateE(n)
{
    return sum_series_calculator(calculateETerm)(n)
}

const e = calculateE(1000)

//Note: the last sub-exercise which results in 'const e' is bothced lol

/**
 * Exercise 10
 */
function calculateSinthTerm(x)
{
    return function(n)
    {
        return ((Math.pow(-1, n) * Math.pow(x, (2 * n) + 1)) / factorial((2 * n) + 1))
    }
}

function sin(x, n = 100)
{
    return sum_series_calculator(calculateSinthTerm(x))(n)
}

//Note: Curried functions are some very impressive things
//If a function being passed is on itself a curried function, then keep track of the last thing to be passed in
//and the rest can be passed as arguments

// function sin(x, n = 100)
// {
//     const numberList = range(n)
//     const numberListMapped = numberList.map(currElement => calculateSinthTerm(currElement)(x))
//     const numberListMappedAdded = numberListMapped.reduce(sumAccumulate)

//     return numberListMappedAdded
// }