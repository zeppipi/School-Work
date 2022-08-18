"""
For the FIT2004 Assignment 3, file includes two functions one that is to find the maximum revenue of a salesman and the other the best strategy for a hero to fight clones in the multiverse
"""
__author__ = 'Hazael Frans Christian'
__docformat__ = 'reStructuredText'
__modified__ = 'May 2nd 2022'
__since__ = 'April 25th 2022'

def best_revenue(revenue, travel_days, start):
    """
    The function that finds max revenue from a starting point with a table of revenues of each city in different days and a table of how long it will take to travel from one city to another with a given starting city

    :Input:
        :param revenue: A list of list that represents how much revenue a city makes in a certain day
        :param travel_days: A list of list that represents how long it'll take to go from one city to another
        :param start: A number that represents what city to start in

    :PreCond: The length of the list inside the revenue's elements must match the length of travel_days and start must be a number below len(travel_days)
    :PostCond: The number returned must be the max revenue

    :returns: A single number that represents the max revenue from the given information

    :Time Complexity: O(n^3+n^2d), where n is len(travel_days) and d is len(revenue), these two variables comes from the two helper functions that are needed for this function
    :Aux Space Complexity: O(nd), comes from making 'res' as an input in the 'bellmanford_seller' function
    """
    res = []

    #Run the Floyd Warshall algorithm onto 'travel_days'
    floyd_warshall(travel_days)
    
    #Make the memo table of negative infinites 
    for outer_index in range(len(revenue)+1):
        res.append([])
        for _ in range(len(revenue[0])):
            res[outer_index].append(-float("inf"))

    #Initialize the first column of 'res' because that column actually represents the day before this begins
    res[0][start] = 0
    
    #Perform bellmans-ford and return result
    return bellmanford_seller(revenue, travel_days, res)

def bellmanford_seller(revenue: list, travel_days: list, res: list) -> int:
    """
    A function that utilizes the dynamic programming part of the Bellmans-Ford algorithm

    :Input:
        :param revenue: A list of list that represents how much revenue a city makes in a certain day
        :param travel_days: A list of list that represents how long it'll take to go from one city to another
        :param res: A list of list of negative infinites that will be edited that will return to show the max revenue

    :returns: The biggest number this proccess has found

    :Time Complexity: O(n^2d), where n is len(travel_days) and d is len(revenue), n^2d because everything in travel_days will be looped through while looping through 'res' which is the same size as 'revenue'
    :Aux Space Complexity: O(nd), this aux space comes from 'revenue'
    """
    big = -float("inf")
    for days in range(1,len(res)):  #loop through each day
        for city in range(len(revenue[days-1])):    #for each day, loop through each city
            
            #copy the previous' day revenue to the new day
            res[days][city] = res[days-1][city] + revenue[days-1][city]
            
            #update 'big' for each new highest revenue found
            if res[days][city] > big:
                big = res[days][city]
            
            #loop through the entirely of the 'travel_days' list of list
            for to_city in range(len(travel_days)):
                for from_city in range(len(travel_days)):
                    
                    #a checker that checks if travelling to this current city from another city is possible
                    days_check = days - travel_days[from_city][to_city] - 1
                    
                    #this if block will be skipped if it is not possible
                    if days_check >= 0:
                        
                        #exclude or include
                        if res[days_check][from_city] + revenue[days-1][to_city] > res[days][to_city]:
                            res[days][to_city] = res[days_check][from_city] + revenue[days-1][to_city]
                            
                            #update 'big' for each new highest revenue found
                            if res[days][to_city] > big:
                                big = res[days][to_city]

    return big

def floyd_warshall(travel_days: list) -> None:
    """
    A function that applies the Floyd Warshall algorithm to the travel days to find their shortest pairs. Also added some edge cases specific for this assingment like turning all -1 into a flag for a vertex being unreachable, and turning all travel_days[a][a] into 0 regardless of what was originally in it.

    :Input:
        :param travel_days: a list of list that represents how long it'll take to go from one city to another

    :PreCond: travel_day's size must be n by n
    :PostCond: all of the elements in the list of list must be optimize to become their best pair

    :returns: None, this list is being edited directly

    :Time Complexity: O(n^3), where n is len(travel_days), n^3 because all possible pairs has to be checked n times to find their best pair
    :Aux Space Complexity: O(1), this function is in-place
    """
    #Edge case where travel_days[a][a] = 0 initialized here
    for index in range(len(travel_days)):
        travel_days[index][index] = 0

    #The algorithm starts here
    for outer_index in range(len(travel_days)):         #O(n)
        for inner_index in range(len(travel_days)):     #O(n)
            for checking in range(len(travel_days)):    #O(n)
                if travel_days[inner_index][checking] == -1:
                    travel_days[inner_index][checking] = float("inf")
                travel_days[inner_index][checking] = min(travel_days[inner_index][checking], travel_days[inner_index][outer_index] + travel_days[outer_index][checking])    #O(1), because it only checks between 2 elements

def hero(attacks):
    """
    A heroic function that finds the most amount of clones to beat in a list of multiverses, where the information given is which multiverse this fight is taking place, what day it starts, what day it ends, and how many clones are there in there

    :Input:
        :param attacks: a list of list with all of the information of what's happening in the multiverses

    :PreCond: the length of each elements in the list must be 4 elements
    :PostCond: the amount of elements returned cannot be more than len(attacks)

    :returns: a list of list that shows which multiverses to fight in
    
    :Time Complexity: O(nlogn), most of the complexity comes from making the memo, where it has to loop n times and for each n, a binary search has to happen which is logn
    :Aux Space Complexity: O(n), temporary lists were needed to store the results
    """
    sortlistolist(attacks, 2)

    memo_numbers = []   #memo for value
    memo_map = []   #memo to keep track of which elements chosen

    for _ in range(len(attacks)):
        memo_numbers.append(0)
        memo_map.append(None)

    memo_numbers[0] = attacks[0][3]     #seed
    memo_map[0] = 0

    for index in range(1, len(attacks)):    #loop through all of the elements O(n)
        current_clones = attacks[index][3]
        last_clones = 0
        
        #search the latest non-conflict element O(logn)
        last_clones_index = searchlistolist(attacks, index)
        
        #a bunch of comparisons
        if last_clones_index != -1:
            last_clones = memo_numbers[last_clones_index]

        include_clones = current_clones+last_clones

        if include_clones > memo_numbers[index-1]:
            memo_numbers[index] = include_clones
            
            #memo_map[index] = index
            memo_map[index] = last_clones_index
            if memo_map[index-1] != None:
                memo_map[memo_map[index-1]] = None
            if last_clones_index != -1:
                memo_map[last_clones_index] = last_clones_index
        else:
            memo_numbers[index] = memo_numbers[index-1]
            
            #memo_map[index] = memo_map[index-1]
    
    res = []
    for index in range(len(memo_map)):
        if memo_map[index] == index:
            res.append(attacks[index])
    
    return res

def searchlistolist(listolist:list, index:int) -> int:
    """
    A search function that will be used to find the latest non-conflicting element right before the chosen element. The search algorithm that will be used here is binary search

    :Input:
        :param listolist: the list of list to search in
        :param index: the current element we are looking for

    :PreCond: index < len(listolist) and index > 0
    :PreCond: the returned value must be lower than len(listolist) and at least bigger than -1

    :returns: a number that will show the index of the latest non-conflicting element before the given index, if it is -1, it means there are no latest non-conflicting element

    :Time Complexity: O(logn), worst case scenario for binary search is having to loop through the whole list by halving it all of the time, which happens when the element can't be found
    :Aux Space Complexity: O(1), no temporary space is needed for this function
    """
    low = 0
    high = index - 1

    while low <= high:
        middle = (low + high) // 2
        if listolist[middle][2] < listolist[index][1]:     #is the element in 'middle' conflicting with 'index''s
            if listolist[middle+1][2] < listolist[index][1]:   #it was! Now, is the element right after 'middle' conflicting with 'index''s
                low = middle+1  #it wasn't, update low
            else:
                return middle   #if it is then middle was the latest non-conflicting element
        else:
            #it wasn't, update high
            high = middle - 1
    
    return -1   #this line will only be reached if the other returns was never reached

def sortlistolist(listolist:list, index:int) -> None:
    """
    A sort function to sort a list of list based on a given index. The sort algorithm that will be used here is merge sort
    This function is a slightly modified merge code given from Programiz
    https://www.programiz.com/dsa/merge-sort

    :Input:
        :param listolist: the list of list that will be sorted
        :param index: a number that represents which index should this list of list be sorted by

    :PreCond: index must be lower than the amount of elements in the list's elements
    :PostCond: the list of list will be sorted on non-increasing order based on a given index

    :returns: None, the list of list will be edited directly

    :Time Complexity: O(nlogn), worst, best, and average time complexity of mergesort is nlogn
    :Aux Space Complexity: O(n), where we need a temporary list to place the sorted elements
    """
    if len(listolist) > 1:
        middle = len(listolist)//2
        left = listolist[:middle]
        right = listolist[middle:]

        sortlistolist(left, index)
        sortlistolist(right, index)

        i = j = k = 0
        while i < len(left) and j < len(right):
            if left[i][index] < right[j][index]:
                listolist[k] = left[i]
                i += 1
            else:
                listolist[k] = right[j]
                j += 1
            k += 1
        
        while i < len(left):
            listolist[k] = left[i]
            i += 1
            k += 1

        while j < len(right):
            listolist[k] = right[j]
            j += 1
            k += 1


if __name__ == "__main__":
    print("👍")
    attacks= [[0, 8, 84, 25], [1, 44, 68, 79], [2, 9, 108, 99], [3, 38, 63, 23], [4, 43, 96, 11], [5, 54, 81, 92], [6, 6, 106, 67], [7, 66, 104, 100], [8, 73, 139, 71], [9, 37, 102, 66], [10, 26, 117, 46], [11, 94, 124, 74], [12, 43, 116, 70], [13, 3, 99, 100], [14, 15, 97, 44], [15, 52, 70, 10], [16, 21, 60, 20], [17, 56, 112, 2], [18, 3, 50, 14], [19, 80, 85, 58], [20, 52, 58, 45], [21, 59, 132, 36], [22, 95, 169, 83], [23, 1, 16, 11], [24, 71, 109, 99], [25, 5, 19, 77], [26, 71, 136, 5], [27, 100, 108, 43], [28, 92, 141, 60], [29, 78, 166, 16], [30, 33, 44, 96], [31, 34, 76, 21], [32, 14, 113, 37], [33, 27, 28, 8], [34, 33, 73, 37], [35, 38, 99, 41], [36, 9, 17, 41], [37, 57, 58, 26], [38, 23, 107, 63], [39, 13, 71, 30], [40, 15, 99, 6], [41, 51, 56, 62], [42, 88, 93, 83], [43, 68, 112, 7], [44, 2, 58, 25], [45, 11, 60, 24], [46, 3, 68, 47], [47, 75, 92, 54], [48, 65, 161, 2], [49, 27, 57, 78], [50, 33, 131, 2], [51, 9, 106, 35], [52, 89, 184, 95], [53, 64, 89, 25], [54, 50, 81, 38], [55, 96, 98, 63], [56, 35, 77, 6], [57, 93, 155, 57], [58, 9, 70, 95], [59, 14, 111, 100], [60, 71, 86, 11], [61, 93, 179, 48], [62, 24, 94, 33], [63, 70, 130, 79], [64, 81, 83, 62], [65, 18, 66, 100], [66, 21, 59, 52], [67, 32, 107, 7], [68, 20, 80, 89], [69, 23, 27, 14], [70, 55, 144, 22], [71, 69, 79, 71], [72, 68, 141, 73], [73, 24, 102, 98], [74, 13, 22, 24], [75, 20, 35, 87], [76, 13, 23, 28], [77, 24, 114, 17], [78, 82, 104, 13], [79, 74, 150, 59], [80, 7, 50, 50], [81, 49, 85, 82], [82, 21, 24, 56], [83, 5, 6, 62], [84, 25, 113, 14], [85, 13, 71, 11], [86, 84, 101, 63], [87, 85, 107, 76], [88, 89, 154, 40], [89, 17, 19, 62], [90, 98, 191, 77], [91, 19, 90, 41], [92, 56, 146, 86], [93, 75, 159, 16], [94, 27, 100, 100], [95, 44, 97, 98], [96, 36, 75, 4], [97, 22, 45, 81], [98, 89, 187, 12], [99, 57, 130, 13], [100, 79, 131, 29], [101, 66, 76, 6], [102, 37, 89, 41], [103, 100, 198, 51], [104, 37, 44, 94], [105, 10, 96, 66], [106, 71, 144, 18], [107, 19, 20, 34], [108, 34, 74, 81], [109, 63, 132, 89], [110, 41, 131, 29], [111, 19, 84, 24], [112, 13, 80, 31], [113, 33, 33, 93], [114, 82, 131, 81], [115, 39, 43, 96], [116, 25, 62, 34], [117, 99, 118, 33], [118, 36, 61, 75], [119, 9, 102, 93], [120, 79, 152, 99], [121, 94, 137, 68], [122, 1, 8, 19], [123, 70, 121, 43], [124, 32, 101, 29], [125, 46, 120, 73], [126, 94, 138, 30], [127, 52, 124, 61], [128, 64, 147, 13], [129, 2, 22, 99], [130, 56, 137, 88], [131, 25, 100, 17], [132, 87, 97, 63], [133, 91, 130, 51], [134, 43, 81, 15], [135, 65, 106, 49], [136, 86, 128, 97], [137, 4, 67, 17], [138, 94, 162, 99], [139, 92, 132, 27], [140, 6, 40, 52], [141, 21, 36, 30], [142, 8, 10, 68], [143, 59, 81, 73], [144, 43, 115, 61], [145, 41, 100, 35], [146, 18, 108, 85], [147, 44, 125, 19], [148, 9, 99, 25], [149, 53, 107, 89], [150, 8, 34, 65], [151, 94, 172, 53], [152, 54, 73, 8], [153, 99, 169, 26], [154, 94, 105, 67], [155, 56, 154, 85], [156, 58, 78, 37], [157, 75, 127, 11], [158, 57, 136, 2], [159, 55, 152, 99], [160, 59, 75, 79], [161, 20, 40, 84], [162, 58, 137, 14], [163, 84, 175, 74], [164, 70, 113, 84], [165, 64, 158, 65], [166, 41, 134, 81]]
    print(hero(attacks))
    #bug: this fuckin backtracking is still not working, problem arise because when a change happens, it doesn't backtrack far enough to corrently make all of the changes
    #line 159, it cuts off a node one branch up, but in order for this to work, everything in that brach must be cut of
    #maybe make all not included None and the route is a bunch of numbers referring to another number until it reaches to a number that refers to itself