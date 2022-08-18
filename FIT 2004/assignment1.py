""" 
For the FIT2004 Assignment 1, file includes two programs, one is a 'Wordle' solver and the other a local maximum finder
"""
__author__ = 'Hazael Frans Christian'
__docformat__ = 'reStructuredText'
__modified__ = 'March 24th 2022'
__since__ = 'March 11th 2022'

from os import lstat
from typing import final


def trainer(wordlist:list, word:str, marker:list) -> list: 
    """
    This method takes a wordlist and a guess and gives out the list of the most possible answers within that wordlist
    :param wordlist: The list of words the function will refer to when finding the best answer
    :param word: The word the player has guessed
    :param marker: A list that indicates which letter in 'word' is wrong and not
    :returns: It will return back the wordlist parameter, but all trimmed down to only include the valid words

    :PreCond: The length of 'word', 'marker', and the length of the words in 'wordlist' must be the same
    :PostCond: The returned list will be sorted

    These time complexities assumes that the output will be essentially everything in 'wordlist', which in practice, happens very rarely
    :Time complexity: No changes in the input can change the time complexity, so for both best and worst case, the complexity would be O(3M + N + NM + 2NM) = O(NM)

    :Space complexity: O(1), amount of input space is constant
    :Aux Space complexity: O(N), temporary lists the size of wordlist is needed
    """
    #Big-O notations in the comments are for time complexity only, unless written otherwise
    sorted_word_id_buckets = letter_sorting_bucket(word,0)  #O(M), M for the length of the words
    sorted_word = letter_sorted_concatenation(sorted_word_id_buckets, 0)    #O(M), Sort the letters in 'word'
    sorted_word_id = word_number(sorted_word)   #O(M), An id for 'word'

    word_list_id = []   #What will be a list of ids for all of the words in wordlist
    for index in range(len(wordlist)):  #O(N), N for the amount of words in wordlist
        sorted_wordlist_id_buckets = letter_sorting_bucket(wordlist[index], 0)  #O(M)
        sorted_wordlist = letter_sorted_concatenation(sorted_wordlist_id_buckets,0)   #O(M)
        word_list_id.append(word_number(sorted_wordlist))   #O(M)

    
    res = []
    for index in range(len(word_list_id)):   #O(N), filter out any words in wordlist that doesn't have the same id as 'word'
        if word_list_id[index] == sorted_word_id:   #O(1)
            res.append(wordlist[index])     #O(1)

    wordlist = []
    for index in range(len(res)):   #O(N), filter out any words in wordlist that don't match the markers, 1's for letter should be there and 0's for letter shouldn't be there
        valid = False
        for inner_index in range(len(marker)):  #O(M), even though its checking the marker, 1's gets checked as well, making O(M) instead of O(X), which wouldve been the amount of 0's in the marker
            if((res[index][inner_index] == word[inner_index]) == marker[inner_index]):
                valid = True
            else:
                valid = False
                break
        if valid:
            wordlist.append(res[index])
                
    for index in range(len(word)-1,-1,-1):  #O(M), sorting the result one last time before returning the result
        final_bucket = letter_sorting_bucket(wordlist,index)    #O(N), it is sorting a list making this fucntion O(N) time complexity
        res = letter_sorted_concatenation(final_bucket,1)   #O(N)
        wordlist = res
    
    return wordlist
    
def letter_sorting_bucket(wordlist:list, index:int) -> list:
    """
    An auxillary method that takes a list and puts them in their respective buckets based on the letter chosen by 'index'
    :param wordlist: A list of words to sort
    :param index: An index to indicate which letter to sort by, index 0 is the most left letter
    :returns: A list of list that includes all of the words in their respective buckets

    :PreCond: Index cannot be more than the length of the words in wordlist
    :PostCond: The amount of words in the bucket must stay the same as the amount of words in the initial wordlist

    :Time complexity: Input sensitive! The contents of the bucket could either be the wordlist or letters from a word, which makes their time complexity for both worst and best O(N) for when it's the wordlists or O(M) for when it's a word

    :Space and Aux Space complexity: O(1), even with the buckets, the amount of space needed for this to run stays constant
    """
    bucket = [[] for _ in range(26)]
    
    for word_index in range(len(wordlist)):     #O(N) or O(M)
        word_position = ord(wordlist[word_index][index])-97
        bucket[word_position].append(wordlist[word_index])
    
    return bucket

def letter_sorted_concatenation(bucket:list, type:int) -> list or str:
    """
    An auxillary method that concatenate the sorted buckets from 'letter_sorting_bucket'
    :param bucket: The bucket to concatenate
    :param type: To tell the function what this bucket should concatonate into, 0 for a string and 1 for a list
    :returns: a string with their letters concatonated, or a list with all of their elements appended

    :PreCond: 'type' should only be 1 or 0
    :PostCond: Must return a single string or a single list

    :Time complexity: An input sensitive function, where if the input is for a string it'll be O(M) and if it's a list O(N), for both worst and best case

    :Space complexity: O(1), The space inputted will be constant for both string and list, because they both will be inside the bucket
    :Aux Space complexity: O(N) or O(M), also input sensitive, depending on the input, a temporary list or string is needed for the return value
    """ 
    res = []
    for index in range(len(bucket)):    #O(M or N + buckets) = O(M or N), the amount of buckets will always stay a constant 26
        for inner_index in range(len(bucket[index])):
            res.append(bucket[index][inner_index])

    if type == 0:
        res = "".join(res)  #The join method is used here to concatonate the letters into a string, O(M)

    return res


def word_number(word:str) -> int:
    """
    An auxillary method that turns a string into a base 26 number
    :param word: Word to turn into a number
    :returns: A base 26 integer 

    :PreCond: The letters in the word must be between a-z
    :PostCond: The resulting number must be base 26

    :Time complexity: O(M), the length of the word has to be looped

    :Space and Aux Space complexity: O(1), no additional space is needed anywhere in this process   
    """
    res = 0
    for index in range(len(word)):
        res += ((ord(word[index])-97)*26**(len(word)-1-index))

    return res

def local_maximum(M:list) -> list:
    """
    A fucntion that finds a local max in an NxN grid of unique integers
    :param M: The grid of numbers
    :returns: A list with two numbers in it that represents the coordinates of a local maximum

    :PreCond: All number in M must be unique and the grid must have the same width and height
    :PostCond: The coordinates returned must be a local maximum

    :Time complexity: O(N), where N is either how tall or wide the grid is. Comes from when processing the whole grid being 2N, 2N gets halved repeated (2N + N + N/2 + N/4....), the halved part would never exceed 2, making the total complexity 4N, which is O(N)

    :Space complexity: O(1), input space is constant
    :Aux Space complexity: O(N), A few copies of M is made for every recursion, which at worst would happen N times 
    """
    x_end = len(M)
    y_end = len(M)

    return local_maximum_helper(M, 0, x_end, 0, y_end)

def local_maximum_helper(M:list, x_start:int, x_end:int, y_start:int, y_end:int) -> list:
    """
    A helper function that does most of 'local_maximum''s functionality
    :param M: The grid of numbers
    :param x_start: The start of the column's domain
    :param x_end: The end of the column's domain
    :param y_start: The start of the row's domain
    :param y_end: The end of the row's domain
    :returns: A list that represents coordinates of the local max

    :PreCond: x and y start can't be lower than 0 and x and y end can't be more than len(M) 
    :PostCond: What this function returns must be the coordinates of a local maximum

    :Time complexity: O(N), refer to 'local_maximum' for explaination

    :Space complexity: O(1), input space is constant
    :Aux Space complexity: O(N), A few copies of M is made for every recursion, which at worst would happen N times  
    """
    #base case
    if(x_end-x_start == 1):
        if check_if_local_maximum(M, x_start, y_start):
            return [x_start, y_start]
        else:
            return "Something went terribly wrong"
    else:   #normal case? i forgor what its called
        max_coord = find_max(M, (x_end + x_start)//2, x_start, x_end, (y_end + y_start)//2, y_start, y_end)
        if check_if_local_maximum(M, max_coord[0], max_coord[1]):   #if the max of the middle row and column is luckily a local max
            return max_coord
        else:
            return direction(M, max_coord, x_start, x_end, y_start, y_end)

def recurse_to_corner(tag:str, M:list, x_start:int, x_end:int, y_start:int, y_end:int) -> list:
    """
    An auxillary function that is responsible to recurse back into 'local_maximum' into the selected quadrant
    :param tag: A string to tell which corner this is recursing into
    :param M: The grid of numbers
    :param x_start: The start of the column's domain
    :param x_end: The end of the column's domain
    :param y_start: The start of the row's domain
    :param y_end: The end of the row's domain
    :returns: A list that represents coordinates of the local max

    :PreCond: x and y start can't be lower than 0 and x and y end can't be more than len(M) 
    :PostCond: What 'local_maximum_helper' returns must be the grid's local maximum

    :Time complexity: O(1), this functions part in the algorithm is constant as it simply does a comparison before doing a recusrion

    :Aux Space and Space complexity: O(1), the space used for this proccess is constant
    """

    if tag == "TR":     #'TR' for top right
        return local_maximum_helper(M, x_start, (((x_end-1)+x_start)//2), (((y_end+1)+y_start)//2), y_end)
    elif tag == "TL":    #'TL' for top left
        return local_maximum_helper(M, x_start, (((x_end-1)+x_start)//2), y_start, (((y_end-1)+y_start)//2))
    elif tag == "BR":   #'BR' for bottom right
        return local_maximum_helper(M,(((x_end+1)+x_start)//2), x_end, (((y_end+1)+y_start)//2), y_end)
    elif tag == "BL":   #'BL' for bottom left
        return local_maximum_helper(M,(((x_end+1)+x_start)//2), x_end, y_start, (((y_end+1)+y_start)//2))
    elif tag == "LMH":  #'LMH' for local max here
        return  [x_start, y_start]
    else:   #how could this happen
        return "Something went terribly wrong"

def direction(M:list, max_coord:list, x_start:int, x_end:int, y_start:int, y_end:int) -> list:
    """
    An auxillary function that tells the direction to recurse into
    :param M: The grid of numbers 
    :param max_coord: The list that includes the coordinates of biggest number in the current quodrant's center row and column
    :param x_start: The start of the column's domain
    :param x_end: The end of the column's domain
    :param y_start: The start of the row's domain
    :param y_end: The end of the row's domain
    :returns: returns what 'recurse_to_corner' returns

    :PreCond: No number in max_coord, x_start, x_end, y_start, and y_end can be bigger than len(M) or smaller than 0
    :PostCond: The string outputted should only be "TR", "TL", "BR", "BL", or "LMH"

    :Time complexity: O(1), all of the comparisons that happens in here are constant time

    :Aux Space and Space complexity: O(1), the space used here are constant
    """                                           
    column_pos = (x_start+x_end)//2
    row_pos = (y_start+y_end)//2

    top = False
    bottom = False
    right = False
    left = False

    if max_coord[0] == column_pos:  #max_coord is on the column
        if(max_coord[1] > (y_end + y_start)//2):    #max coord is on the right
            right = True
        else:   #max_coord is on the left
            left = True
        
        if(M[max_coord[0]-1][max_coord[1]] > M[max_coord[0]+1][max_coord[1]]):    #bigger number is on top
            if check_if_local_maximum(M, max_coord[0]-1, max_coord[1]):
                return recurse_to_corner("LMH", M, max_coord[0]-1, 0, max_coord[1], 0)  #bigger number on the top is a local max!
            else:
                top = True
        else:
            if check_if_local_maximum(M, max_coord[0]+1, max_coord[1]):     #bigger number is below
                return recurse_to_corner("LMH", M, max_coord[0]+1, 0, max_coord[1], 0)  #bigger number below is a local max!
            else:
                bottom = True
    
    elif max_coord[1] == row_pos:   #max_coord is on the row
        if(max_coord[0] < (x_end + x_start)//2):    #max coord is on the top
            top = True
        else: #max coord is below
            bottom = True
        
        if(M[max_coord[0]][max_coord[1]+1] > M[max_coord[0]][max_coord[1]-1]):    #bigger number is on the right
            if check_if_local_maximum(M, max_coord[0], max_coord[1]+1):
                return recurse_to_corner("LMH", M, max_coord[0], 0, max_coord[1]+1, 0)  #bigger number on the right is a local max!
            else:
                right = True
        else:
            if check_if_local_maximum(M, max_coord[0], max_coord[1]-1):     #bigger number is on the left
                return recurse_to_corner("LMH", M, max_coord[0], 0, max_coord[1]-1, 0)  #bigger number on the left is a local max!
            else:
                left = True
    
    if top and right:
        return recurse_to_corner("TR", M, x_start, x_end, y_start, y_end)
    elif top and left:
        return recurse_to_corner("TL", M, x_start, x_end, y_start, y_end)
    elif bottom and right:
        return recurse_to_corner("BR", M, x_start, x_end, y_start, y_end)
    elif bottom and left:
        return recurse_to_corner("BL", M, x_start, x_end, y_start, y_end)
    else:   #how did this happen?
        return "Something went terribly wrong"

def check_if_local_maximum(M:list, x:int ,y:int) -> bool:
    """
    An auxillary method to check if the chosen coordinate is a local maximum
    :param M: The grid of numbers
    :param x: The outer coords of M
    :param y: The inner coords of M
    :returns: a bool to confirm that said coords is or isn't a local maximum

    :PreCond: x or y can't be more than len(M) or less than 0
    :PostCond: When return True, position must be a local max and when return False, position musn't be a local max
    
    :Time complexity: O(1), checking a number's neighbours can only be at most 4 comparisons

    :Space and Aux Space complexity: O(1), no variable additional space is needed anywhere in this proccess
    """
    edges = len(M)-1
    x_neighbours = [x-1, x, x+1, x]
    y_neighbours = [y, y+1, y, y-1]

    res = True
    for index in range(len(x_neighbours)):
        if x_neighbours[index] > edges or y_neighbours[index] > edges or x_neighbours[index] < 0 or y_neighbours[index] < 0:
            continue
        elif M[x_neighbours[index]][y_neighbours[index]] > M[x][y]:
            res = False

    return res
    

def find_max(M:list, column_pos:int, x_start:int, x_end:int, row_pos:int, y_start:int, y_end:int) -> list:
    """
    An auxillary method to find the maximum from a selected row and column
    :param M: The grid of numbers
    :param x_start: In the selected column, which coords to start at
    :param x_end: In the selected column, which coords at end at
    :param column_pos: Which column are we selecting
    :param y_start: In the selected row, which coords to start at
    :param y_end: In the selected row, which coords to end at
    :param row_pos: Which row are we selecting
    :returns: A list with coords

    :PreCond: x and y start can't be lower than 0 and x and y end can't be more than len(M)
    :PostCond: At least one of the numbers in the parameters will be included in the returned coords

    :Time complexity: O(N), the whole row and column must be looped through to find the max

    :Space and Aux Space complexity: O(1), no additional space is needed anywhere in this proccess
    """
    max_row = 0
    row_index = 0
    for index in range(x_start, x_end):     #O(N)
        if M[index][row_pos] > max_row:
            max_row = M[index][row_pos]
            row_index = index

    max_column = 0
    column_index = 0
    for index in range(y_start, y_end):     #O(N)
        if M[column_pos][index] > max_column:
            max_column = M[column_pos][index]
            column_index = index
    
    if(max_row > max_column):
        return [row_index, row_pos]
    else:
        return [column_pos, column_index]
