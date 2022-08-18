"""
For the FIT2004 Assignment 4, this file includes two programs, one is used to allocated system admins with a flipped sleeping schedule their nightshifts and another used to find the tragic backstory of a villian. 
"""
__author__ = 'Hazael Frans Christian'
__docformat__ = 'reStructuredText'
__modified__ = 'May 27th 2022'
__since__ = 'May 16th 2022'

def allocate(preferences, sysadmins_per_night, max_unwanted_shifts, min_shifts):
    """
    Function to allocate sleepless system admins, done by making this problem into a biparate graph in which the matchings are between the sysadmins and their night shifts. This problem actually consists of two sub problems, one is which '0' preferences to include and which '1' preferences to drop, both are done with their own biparate matching problem, with different constraints to accomodate them.

    :Input:
        :param preferences: A matrix of 1's and 0's where 1 indicates a sysadmin prefers to work on that night and 0 indicates otherwise. The matrix's height or y-axis indicates the nights, and the matrix's width or x-axis indicates the sysadmins.
        :param sysadmins_per_night: A number that represents **exactly** how many sysadmins should be allocated for that night
        :param max_unwanted_shifts: A number that indicates the maximum amount of unprefered shifts each sysadmin can tolerate 
        :param min_shifts: A number that indicates the minimum amount of shifts every sysadmin should take
    
    :returns: Another matrix, same size as 'prefereces' but the 1's means that sysadmin has been allocated to that shift and 0's mean otherwise or a None if an allocation is impossible

    :PreCond: The width of this matrix, should be consistent, and the height of this matrix should be constant 30, representing the 30 nights
    :PostCond: The returned matrix must be the same size as the 'prefereces' parameter, or a None and the numbers in this returned matrix must be 1's and 0's

    :Time Complexity: O(n^2), where n is the amount of sysadmind. The most significant time complexity comes from using the Ford-Fulkerson algorithm, which has a time complexity of O(FE), where F is the maximum flow and E is the amount of edges, F at worst can total up to 'n'. Edges are made when the 'prefereces' matrix gets turned into a biparate graph, which will at worst have O((w+h) + wh), where 'w' is the width of the matrix and 'h' is the height of the matrix. Since 'h' represents the nights which is a constant 30, and 'w' represents the sysadmind which is 'n', this turns O((w+h) + wh) into O(n), meaning both F and E are n, making O(FE) into O(n^2).
    :Aux Space Complexity: O(n), The biggest thing kept will be a copy of the bipartate graph, which also has a complexity of O((w+h) + wh), which also gets turned in O(n). 
    """
    opposite_preference = flip(preferences)
    weights_for_0 = get_weights(preferences,sysadmins_per_night,0)
    biparate_opposite_preference = biparate(opposite_preference, weights_for_0,max_unwanted_shifts)
    ford_fulkerson(biparate_opposite_preference)
    zero_matrix = turn_graph_to_matrix(biparate_opposite_preference)

    weights_for_1 = get_weights(preferences,sysadmins_per_night,1)
    biparate_normal_preference = biparate(preferences, weights_for_1, float("inf"))
    ford_fulkerson(biparate_normal_preference)
    one_matrix = turn_graph_to_matrix(biparate_normal_preference)

    addsub_matrix(preferences, zero_matrix, "add")
    addsub_matrix(preferences, one_matrix, "sub")

    return checker(preferences, sysadmins_per_night, min_shifts)

def checker(preferences, sysadmins_per_night, min_shifts):
    """
    A fucntion that will decide if the given solution is an actual valid solution or not

    :Input:
        :param preferences: The orginal preferences matrix, by this point it should be editted into an allocation matrix
        :param sysadmins_per_night: A number that represents **exactly** how many sysadmins should be allocated for that night
        :param min_shifts: A number that indicates the minimum amount of shifts every sysadmin should take
    
    :returns: Either it will just return the 'preferences' matrix again or a None

    :PreCond: The 'preferences' parameter must be a matrix
    :PostCond: The 'preferences' should not be editted at all through this proccess

    :Time Complexity: O(n), The whole matrix will be looped, so time complexity is O(n)
    :Aux Space Complexity: O(n), A copy of the 'preferences' matrix will be kept
    """
    valid = True

    #check for sysadmins_per_night
    for outer in range(len(preferences)):
        total = 0
        for inner in range(len(preferences[outer])):
            total += preferences[outer][inner]
        if(not total == sysadmins_per_night):
            valid = False

    #check for min_shifts
    for outer in range(len(preferences[0])):
        total = 0
        for inner in range(len(preferences)):
            total += preferences[inner][outer]
        if((min_shifts - total) > 0):
            valid = False

    if valid:
        return preferences
    else:
        return None

def addsub_matrix(a, b, operation):
    """
    A function that adds or subtracts two matrices together

    :Input:
        :param a: The original matrix that will be edited
        :param b: By how much this original matrix will be edited
        :param operation: Telling the function which operation should be done

    :returns: None, 'a' will be edited directly

    :PreCond: Both 'a' and 'b' must be the same size, and the only strings allowed for 'operation' must be exactly 'add' and 'sub'
    :PostCond: No operation should be left undone

    :Time Complexity: O(wh), The whole matrix will be looped once
    :Aux Space Complexity: O(wh), A copy of both matrices will be kept
    """
    if operation == "add":
        for outer_index in range(len(a)):
            for inner_index in range(len(a[outer_index])):
                a[outer_index][inner_index] = a[outer_index][inner_index] + b[outer_index][inner_index]
    elif operation == "sub":
        for outer_index in range(len(a)):
            for inner_index in range(len(a[outer_index])):
                a[outer_index][inner_index] = a[outer_index][inner_index] - b[outer_index][inner_index]    

def turn_graph_to_matrix(graph):
    """
    A function that turns a graph object back into a matrix

    :Input:
        :param graph: The graph object to turn back into a matrix

    :returns: A matrix of 1s and 0s

    :PreCond: The graph must be a biparate graph, where the start node is marked index 0 and the end node is marked index 1
    :PostCond: The start and end node shouldn't be included and no info between the connections should be lost

    :Time Complexity: O(V+E), All of the vertices and edges of the graph has to be looped through
    :Aux Space Complexity: O(V^2), The final matrix size should have some of the vertices as their width and some of the vertices as their height
    """
    width = len(graph.vertices[0].edges)
    height = 0
    for vertex in range(graph.Vertex_count):
        for edges in graph.vertices[vertex].edges:
            if edges.v == 1:
                height += 1

    res = []
    for index in range(height):
        res.append([])
        for inner_index in range(width):
            res[index].append(0)
    
    for vertex in range(2, graph.Vertex_count-height):
        for edges in graph.vertices[vertex].edges:
            if edges.z == 1:
                res[(edges.v)-2-width][(edges.u)-2] = 1

    return res
    
def flip(matrix:list) -> list:
    """
    A helper function that flips a matrix of 1s and 0s into their opposite

    :Input:
        :param matrix: A list of list of 1s and 0s that represents a matrix of 1s and 0s

    :PreCond: The only numbers allowed in 'matrix' are 1s and 0s
    :PostCond: The only numbers allowed in the returned matrix are 1s and 0s

    :Time Complexity: O(wh), where 'w' is the width of the matrix and 'h' is the height of the matrix
    :Aux Space Complexity: O(wh), a copy of the matrix has to be made
    """
    matrix_copy = []
    for outer in range(len(matrix)):
        matrix_copy.append([])
        for inner in range(len(matrix[outer])):
            matrix_copy[outer].append(matrix[outer][inner])

    for outer in range(len(matrix_copy)):
        for inner in range(len(matrix_copy[outer])):
            if matrix_copy[outer][inner] == 1:
                matrix_copy[outer][inner] = 0
            else:
                matrix_copy[outer][inner] = 1
    
    return matrix_copy

def ford_fulkerson(graph):
    """
    The Ford Fulkerson algorithm, takes a network flow and finds their max flow

    :Input:
        :param graph: The graph to find the max flow
    
    :PreCond: The given graph must be a network flow, completed with a capacity and flow attribute
    :PostCond: The graph must be edited to have the max flow

    :Time Complexity: O(FE), where 'F' is the maximum flow
    :Aux Space Complexity: O(V+E), a copy of the graph must be stored, specifically for the residual network
    """
    residual_network = make_residualnetwork(graph)  #make residual network

    #loop starts here
    while residual_network.bfs_checker(0,1)[0]:
        path = residual_network.djikstra_getpath(0,1)
        min_flow = residual_network.bfs_checker(0,1)[1]

        for index in range(1,len(path)):
            for edges in graph.vertices[path[index-1]].edges:
                if edges.u == path[index-1] and edges.v == path[index]:
                    edges.z += min_flow
        
        residual_network = make_residualnetwork(graph)

    return graph

def biparate(preference:list, list_of_weights:list, number_for_weights:int):
    """
    A helper function that turns a matrix into a biparate graph, which will be in adjacency list form

    :Input:
        :param preference: A list of list that represents a matrix
        :param list_of_weights: A list of weights which will be used as the capacity for the end vertices in the ford-fulkerson algorithm
        :param number_for_weights: A single number which will be used as the capacity for the start vertices in the ford-fulkerson algorithm

    :PreCond: Every element in the list should be the same length and the length of 'list_of_weights' must be equal as len(preference)
    :PostCond: The amount of edges in the final graph should be at most w+h+wh, where 'w' is the width of the preference matrix and 'h' is the height of the preference matrix

    :Time Complexity: O((w+h)+(wh)), The time complexity is based on the vertices and edges, which are 'w+h' and 'wh' at worst respectively 
    :Aux Space Complexity: O((w+h)+(wh)), The info of the graph has to be kept, which are their vertices and edges
    """
    #start immaginary vertex = 0
    #width vertices = 2...w
    width_startindex = 2
    #height vertices = w+2...h+1
    height_startindex = len(preference[0]) + 2
    #end immaginary vertex = 1
    res = []
    
    #Make immaginary start vertex
    for index in range(len(preference[0])):
        res.append((0, index + 2, number_for_weights, 0))
    
    #Connect the matrix between their height and width
    for outer_index in range(len(preference)):
        for inner_index in range(len(preference[outer_index])):
            if preference[outer_index][inner_index] == 1:
                res.append((inner_index + width_startindex,outer_index + height_startindex,1,0))
    
    #Make immaginary end vertex
    for index in range(len(preference)):
        res.append((height_startindex+index, 1, list_of_weights[index],0))

    return Graph(res)

def make_residualnetwork(graph):
    """
    A function that takes a flow network and creates the flow network's residual network

    :Input:
        :param graph: The flow network that we want to make a residual network out of
    
    :PreCond: The graph that is inputted must be a flow network
    :PostCond: The returned graph must be a valid residual network

    :Time Complexity: O(E), all of the edges in the graph has to be looped through
    :Aux Space Complexity: O(V+E), a copy of the graph has to be made and returned
    """
    res_graph = []
    for vertex in graph.vertices:
        for edges in vertex.edges:
            if edges.w-edges.z > 0:
                res_graph.append((edges.u, edges.v, edges.w - edges.z, 0))
            if edges.z > 0:
                res_graph.append((edges.v, edges.u, edges.z, 0))
    
    return Graph(res_graph)

def get_weights(preferences:list, exact_number:int, flag:int) -> list:
    """
    A helper function that returns the appropiate capacity for the matrix, based on their 1s or 0s

    :Input:
        :param preferences: A list of list of 1s and 0s
        :param exact_number: The number these capacities should aim for
        :param flag: A number that indicates if the capacity should be based on the matrix's 1s or 0s

    :PreCond: The lenght of all of the elements in the preference list must be equal
    :PostCond: The returned list must be the length of the preference list

    :Time Complexity: O(wh), the whole matrix has to be looped through
    :Aux Space Complexity: O(h), the returned list will have the same amount of elements as the height of the matrix
    """
    res = []
    for outer_index in range(len(preferences)):
        sum = 0
        for inner_index in range(len(preferences[0])):
            sum += preferences[outer_index][inner_index]
        res.append(sum - exact_number)
    
    if(flag == 1):
        for index in range(len(res)):
            if(res[index]) < 0:
                res[index] = 0
    else:
        for index in range(len(res)):
            if(res[index]) > 0:
                res[index] = 0
            else:
                res[index] = res[index] * -1
        
    return res

class Graph:
    """
    Class that makes a graph, code references from the FIT2004 Assignment 2
    """
    
    def __init__(self, roads:list) -> None:
        """
        Function will initialize the graph structure, this graph will be using the adjaceny list representation
        
        :Input:
            :param roads: a list of tuples to each represents an edge with their starting node, ending node, and weight
        
        :PreCond: 'roads' must be a list of tuples
        :PostCond: No info from 'roads' must be left out
        
        :Time complexity: O(V+E), the creation of all of the vertices and edges are done seperately, so their complexity is simply added on
        :Aux Space complexity: O(V+E), all of the information for the vertices and edges has to be kept somewhere
        """
        self.Vertex_count = self.vertex_count(roads)
        self.vertices = [None]*self.Vertex_count   #initialize vertices
        for index in range(self.Vertex_count): #O(V), where V is the amount of vertices
            self.vertices[index] = Vertex(index)
        
        self.add_edges(roads)   #O(E)

    def __str__(self) -> str:
        """
        Function to print out the graph
        """
        return_string = ''
        for vertex in self.vertices:
            return_string = return_string + "Vertex " + str(vertex) + "\n"
        
        return return_string
    
    def vertex_count(self, roads:list) -> int:
        """
        Function that counts exactly how many vertices there are in the graph

        :Input:
            :param roads: A list of tuples that represents how many edges there are in the graph
        
        :PreCond: 'roads' must be a list of tuples
        :PostCond: The returned number must be how many vertices there are in the graph

        :Time complexity: O(E), E for how many edges there, since roads has to be looped through and that represents the edges in the graph
        :Aux Space complexity: O(1), no additional space is needed to proccess this
        """
        vertex_count = 0
        for index in range(len(roads)):
            if roads[index][0] > vertex_count:
                vertex_count = roads[index][0]

        return vertex_count + 1
    
    def add_edges(self, edge_arg:list) -> None:
        """
        Function makes edges based on the info given from 'edge_arg'
        
        :Input:
            :param edge_arg: a list of tuples that contains edges
        
        :PreCond: 'edge_arg' must be a list of tuples
        :PostCond: All edges should be located in their appropiate vertex 

        :Time Complexity: O(E), where E is the amount of edges the whole graph has
        :Aux Space Complexity: O(E), space comes from the 'add_edge' function from the vertex class that keeps a list of all of the edges a particular vertex has 
        """
        for edge in edge_arg:
            u = edge[0]     #start
            v = edge[1]     #end
            w = edge[2]     #capacity
            z = edge[3]     #flow

            current_edge = Edges(u,v,w,z) #make an edge object with the currently selected info
            current_vertex = self.vertices[u]   #take the appropiate vertex to put the edge
            current_vertex.add_edge(current_edge)   #add edge to vertex

    def bfs_checker(self, start, end):
        """
        Function to see if this graph has a valid path from the inputted start vertex to the end vertex

        :Input:
            :param start: Number to indicate where this path should start
            :param end: Number to indicate where this path should end

        :returns: A list of numbers that tells the order of vertices to make the path, or returns a None, if no valid path can be made

        :PreCond: Both the 'start' number and the 'end' 
        :PostCond: The given path must start from 'start' and end at 'end'

        :Time Complexity: O(V+E), all of the vertices has to be looped through and while looping through the vertices, each of their edges also has to be looped, which at most will looped through all of their edges
        :Aux Space Complexity: O(V), at worst, the path will include all of the vertices, making O(V) the aux space complexity
        """
        from collections import deque   #This would be needed for bfs
        
        discovered = deque()
        discovered.append(self.vertices[start])
        self.vertices[start].visited = True

        #returned_bfs = []
        #returned_bfs.append(self.vertices[start])

        res = [False, float("inf")]

        while len(discovered) > 0:
            u = discovered.popleft()
            u.visited = True
            #returned_bfs.append(u)
            for edge in u.edges:
                v = edge.v
                if v == end:
                    res[0] = True
                if v < self.Vertex_count:
                    if self.vertices[v].visited == False and not edge.w == 0:
                        discovered.append(self.vertices[v])
                        self.vertices[v].visited = True
                        if(edge.w < res[1] and not edge.w == 0):
                            res[1] = edge.w
        
        for vertex in range(self.Vertex_count):
            self.vertices[vertex].visited = False
        return res

    def djikstra_getpath(self, start, end):
        """
        Finds the most efficient path between the start and end, an editted code from the FIT2004 Assignment 2

        :Input:
            :param start: The starting location
            :param end: The ending location
        
        :returns: A list of integers that represents the most efficient path between start and end

        :PreCond: The numbers in start and end must be valid vertices in the graph
        :PostCond: All the numbers in the returned list must be valid vertices in the graph

        :Time Complexity: O(VlogE), Most of the other proccesses in this function runs at O(V), the O(VlogE) comes from djikstra being needed in the proccess
        :Aux Space Complexity: O(V+E), A copy of the graph is needed for this function to run
        """
        paths = self.djikstra(start)

        backtrack_res = []
        res = []
        current_vertex = end    #start looking from the end
        backtrack_res.append(current_vertex)
        while paths[current_vertex] is not current_vertex:  #O(V) time, at worst this can backtrack to every vertex
            
            #We reached an unreachable place
            if paths[current_vertex] == None:
                return None
            
            temp = paths[current_vertex]
            backtrack_res.append(temp)
            current_vertex = temp
        
        for index in range(len(backtrack_res)-1, -1, -1):
            res.append(backtrack_res[index])
            

        if res[0] != start:    #the root they found is not the start, meaning theres no path
            return None
        else:
            return res

    def djikstra(self, source: int) -> list:
        """
        A Djikstra algorithm implementation, refers from the graph in the initialization and creates a list that represents the shortest paths of that graph. Does that by putting all of the graph's edges into a heap and keep serving it to itself to find which vertex has the most efficient parent.

        :Input:
            :param source: A number that represents a vertex from where this algorithm should start

        :returns: A list where their length is the amount of vertices and the number in each index represents that vertex's most efficient parent

        :PreCond: 'source' must be a number of one of the edges in the graph
        :PostCond: The length of the returned list must match how many vertices there are

        :Time Complexity: O(VLogE), Where V is the amount of vertices in the graph and E is the amount of edges. The V comes from how long the while loop will run, and for each loop, LogE will run from serving and adding edges into the heap
        :Aux Space Complexity: O(V + E), V from making the return list where its length is the amount of vertices there are in the graph, and E from making the heaps, which at worst would have to keep all of the edges in the graph
        """
        edges = MinHeap(len(self.vertices)**2)     #a heap
        
        parents = [None]*self.Vertex_count    #list for parents
        
        current_vertex = self.vertices[source]
        current_vertex.discovered = True
        current_vertex.distance = 0
        parents[source] = source

        for index in current_vertex.edges:
            edges.add(index)    #add time comp: O(LogN) #N = edges

        while len(edges) > 0:   #despite what its looping, this while loop actually loops for every Vertex and not edges
            current_edge = edges.serve()   #serve time comp: O(LogN) #N = edges
            v = current_edge.v      #the end vertex
            u = current_edge.u      #the starting vertex
            w = current_edge.w      #this edge's weight

            if v < self.Vertex_count:
                v_current_distance = self.vertices[v].distance  #this vertex's current distance
                parent_distance = self.vertices[u].distance + w #their parent's current distance

                if parent_distance <= v_current_distance:    #is it better to update the distance or leave it
                    self.vertices[v].distance = self.vertices[u].distance + w
                    parents[v] = u  #u has been confirmed to be the efficient parents of v
            
                if self.vertices[v].discovered == False:
                    for index in self.vertices[v].edges:    #adds all of the next edges onto the heaps
                        edges.add(index)
                    self.vertices[v].discovered = True
            
        for vertex in range(self.Vertex_count):
            self.vertices[vertex].discovered = False
        return parents

class Vertex:
    """
    Class that makes vertices
    """

    def __init__(self, vertex) -> None:
        """
        Function will initialize a vertex, does things like keeping track of the edges it has and the ability to add more to it
        """
        self.vertex = vertex

        #this vertex's edges
        self.edges = []

        #the vertex has been visited for bfs
        self.visited = False

        #the vertex's status, if it is discovered for djikstra
        self.discovered = False

        #the vertex's distance for djikstra
        self.distance = float("inf")

    def __str__(self) -> None:
        """
        Prints out this vertex's information
        """
        return_string = str(self.vertex)
        for edge in self.edges:
            return_string = return_string + " with edge " + str(edge)
            
        return return_string
    
    def add_edge(self, edge) -> None:
        """
        Takes an 'Edge' object and add it to this vertex's list of edges
        """
        self.edges.append(edge)

class Edges:
    """
    Class that makes edges
    """

    def __init__(self, u:int, v:int, w:int, z:int) -> None:
        """
        Initializes the information of this edge, where it starts, where it ends, and their weight
        :Input:
            :param u: where the edge starts
            :param v: where the edge ends
            :param w: the capacity of the edge
            :param z: the flow of the edge
        """
        self.u = u
        self.v = v
        self.w = w
        self.z = z

    def __str__(self) -> str:
        """
        Print out this edge's information
        """
        return_string = "from " + str(self.u) + " to " + str(self.v) + " with capacity " + str(self.w) + " and flow of " + str(self.z) + "\n"
        return return_string

    def __eq__(self, other):
        """
        Magic function that will be called when two edges objects are being compared if they're equal
        """
        return self.w == other.w

    def __gt__(self, other):
        """
        Magic function that will be called when two edges objects are being compared if they're greater
        """
        return self.w > other.w

    def __ge__(self, other):
        """
        Magic function that will be called when two edges objects are being compared if they're greater or equal
        """
        return self.w >= other.w

    def __lt__(self, other):
        """
        Magic function that will be called when two edges objects are being compared if they're lesser
        """
        return self.w < other.w

    def __le__(self, other):
        """
        Magic function that will be called when two edges objects are being compared of they're lesser or equal
        """
        return self.w <= other.w

class MinHeap:
    """
    Class implementing the Min Heaps data structure
    This class will be heavily referencing the heaps data structure implemented in FIT 1008
    """
    def __init__(self, max_size:int) -> None:
        """
        Initialize

        :Input:
            :param max_size: An integer to tell the data structure, how big should the heaps array be
        """
        self.length = 0
        self.the_array = [None]*(max_size + 1)

    def __len__(self) -> int:
        """
        See the length of the list
        """
        return self.length

    def __str__(self) -> str:
        """
        Print out whats in the heaps array for debugging purposes
        """
        res = ""
        for index in range(self.length+1):
            if self.the_array[index] is not None:
                res += str(self.the_array[index]) + " "
        
        return res

    def is_full(self) -> bool:
        """
        Is the heap full?
        """
        return self.length == len(self.the_array)

    def swap(self, a:int, b:int) -> None:
        """
        Swap the element in index a with the element in index b
        """
        temp = self.the_array[a]
        self.the_array[a] = self.the_array[b]
        self.the_array[b] = temp
    
    def rise(self, k:int) -> None:
        """
        Rise the element in index k to its correct position

        :Input:
            :param k: The index needed to be risen
        
        :Time complexity: O(logN), the while loop may need to go through the whole array in k//2 steps
        :Aux Space complexity: O(1), this proccess won't need any additional space 
        """
        while k > 1 and self.the_array[k] < self.the_array[k//2]:   #swaps when the parent is bigger
            self.swap(k, k//2)
            k = k//2

    def add(self, element) -> bool:
        """
        Adds an element into the heaps array

        :Input:
            :param element: The element to be inserted into the heap
        
        :Time complexity: O(logN), where N is the amount of elements in the heaps array, time complexity mostly comes from the 'rise' funtcion
        :Aux Space complexity: O(1), this proccess is done in-place
        """
        has_space = not self.is_full()

        if has_space:
            self.length += 1
            self.the_array[self.length] = element
            self.rise(self.length)

        return has_space
    
    def smallest_child(self, k:int) -> int:
        """
        Checks for the smallest child of the index k

        :Input:
            :param k: The index to check their child

        :Time complexity: O(1), this is a simple lookup
        :Aux Space complexity: O(1), no additional space is required for this proccess
        """
        if 2*k == self.length or self.the_array[2*k] < self.the_array[2*k + 1]:
            return 2*k
        else:
            return 2*k+1

    def sink(self, k: int) -> None:
        """
        Sinks an element in an index to its correct position

        :Input:
            :param k: The index to sink into the right position
        
        :Time complexity: O(logN), where N is the amount of elementsin the heaps array, for cases where an element need to sank to the very lowest level, in which the while loop will loop through the whole list in 2*k steps
        :Aux Space complexity: O(1), this proccess is done in-place
        """
        while 2*k <= self.length:
            child = self.smallest_child(k)
            if self.the_array[k] <= self.the_array[child]:
                break
            self.swap(child, k)
            k = child
    
    def serve(self):
        """
        Serves from the heaps array, which from the nature of min heaps, will be the smallest item
        
        :Time complexity: O(logN), where N is the amount of elements in the heaps array and comes from the proccess of sinking everything into the right positions
        :Aux Space complexity: O(1), This proccess is done in-place
        """
        res = self.the_array[1]
        self.the_array[1] = None
        self.the_array[1] = self.the_array[self.length]
        self.sink(1)
        self.the_array[self.length] = None
        self.length -= 1

        return res

class EventsTrie:
    def __init__(self, timelines):
        """
        Initialize trie data structure
        """
        self.root = Node()
        self.timelines = timelines
        
        for index in range(len(timelines)):
            self.insert(timelines[index], index)
    
    def insert(self, key, word_index, data = None):
        """
        Insert data into the trie

        :Input:
            :param key: A string where each othe its characters becomes a node in the trie
            :param data: A payload of data 'key' holds

        :Time complexity: O(M^2), where M is how many letters they are in 'key', mostly comes from the recursion
        :Aux Space complexity: O(M), more specifically is M times the list in the node, but the list in the node is fixed
        """
        #start a counter
        counter = 0
        
        #Start current from root
        current = self.root

        #Go through 'key'
        for char in key:
            
            #turn char into an index ($ is 0)
            index = ord(char) - 97 + 1
            
            #Path exists
            if current.links[index] is not None:
                current = current.links[index]
                counter += 1
                
                #Make sure no data gets duplicated
                if len(current.data) == 0 or current.data[len(current.data)-1] != char and current.string is not None or current.string != char:
                    current.data.append([key])

                #Part of a new word
                if len(current.string) != 0:
                    if current.string[-1] is not word_index:
                        current.string.append(word_index)
                else:
                    current.string.append(word_index)
                

            #Path don't exist
            else:
                counter += 1
                current.links[index] = Node()
                current = current.links[index]
                
                current.string.append(word_index)
                current.substring = key
                current.index = counter

                #Make sure no data gets duplicated
                if len(current.data) == 0 or current.data[len(current.data)-1] != char and current.string is not None or current.string != char:
                    current.data.append([key])

        #After looping through 'key' put down the terminal symbol
        index = 0
        if current.links[index] is not None:
            current = current.links[index]
        else:
            current.links[index] = Node()
            current = current.links[index]

        #Insert the data
        current.data = data
        current.substring = key

        #Recurse the insert to turn into suffix
        if len(key) > 0:
            self.insert(key[1::], word_index)

    def search_occurances(self, key):
        """
        Search data in the trie

        :Input:
            :param key: A string that is a key to access to a certain leaf in the trie

        :returns: A number that indicates how many times 'key' appears as a substring in the 'timelines'

        :Time complexity: O(M), where M is how many letters they are in 'key'
        :Aux Space complexity: O(M), more specifically is M times the list in the node, but the list in the node is fixed
        """
        #Start current from root
        current = self.root

        #Go through 'key'
        for char in key:
            
            #turn char into an index
            index = ord(char) - 97 + 1
            
            #Path exists
            if current.links[index] is not None:
                current = current.links[index]

            #Path don't exist
            else:
                return None

        return (len(current.data), current.string)
    
    def getLongestChainAux(self, noonccurence, source):
        """
        The 'getLongestChain' auxillery function, does most of their heavy lifting

        :Input:
            :param noonccurence: The constraint for how many times a substring should occur in order for the substring to be considered valid
            :param source: Where should the search for the best child node start

        :returns: A node object, considered to be the best

        :PreCond: 'noonccurence' must be a positive integer
        :PostCond: The returned node must exist

        :Time complexity: O(k), this function in on itself runs O(26^26), but it recurse into itself at most k times
        :Aux Space complexity: O(k), will have to keep at most k nodes
        """
        for index in range(len(source.links)):
            if source.links[index] is not None: #Find a node
                
                #Are they included in more than 'noonccurance' words
                if len(source.links[index].string) >= noonccurence:
                    #Count this node's branches
                    branching = False
                    branch_counting = 0
                    for branch_index in range(1, len(source.links[index].links)):
                        if source.links[index].links[branch_index] is not None:
                            branch_counting += 1
                    
                    #Does this node branch?
                    if branch_counting > 1:
                        branching = True
                    
                    #While counting your branches, noticed that they have none at all
                    elif branch_counting == 0:
                        continue

                    #Does the node have a terminal
                    if source.links[index].links[0] is None and branching == False:

                        #Valid node found, enter
                        source.links[index] = self.getLongestChainAux(noonccurence, source.links[index])

                    else:

                        #Is the child of this node included in less words than this one
                        childs_counter = 0
                        for child_index in range(len(source.links[index].links)):
                            if source.links[index].links[child_index] is not None:
                                if len(source.links[index].links[child_index].string) > childs_counter:
                                    childs_counter = len(source.links[index].links[child_index].string)
                        
                        #Best node found, leave it
                        if childs_counter < noonccurence:
                            continue
                            #source.links[index] = self.getLongestChainAux(noonccurence, source.links[index])
                        
                        #Valid node found, enter
                        else:
                            source.links[index] = self.getLongestChainAux(noonccurence, source.links[index])
                
                #Making sure to delete the ones that dont even fullfill the noonccurance (except if its a terminal node)
                else:
                    if index != 0:
                        source.links[index] = None
        
        #All candidate nodes lined up, find the best
        best = None
        for finish_index in range(len(source.links)):
            if source.links[finish_index] is not None:
                if best == None:
                    best = source.links[finish_index]
                elif source.links[finish_index].index > best.index:
                    best = source.links[finish_index]
        
        #Return best node
        return best


    def getLongestChain(self, noonccurence):
        """
        Function that finds the longest string that occurs at least 'noonccurence' times given a Suffix Trie

        :Input:
            :param noonccurence: The constraint for how many times a substring should occur in order for the substring to be considered valid

        :returns: A string when a valid string can be found or a None for when no valid strings can be found

        :PreCond: 'noonccurence' must be a positive integer
        :PosCond: The returned string should be a subtring of any of the strings given in the __innit__

        :Time complexity: O(k), where k is the length of the longest string that occurs at least 'noonccurence' times, comes from traversing the suffix trie where each node will try to find their best child which continues until we get to the root, the proccess of finding the best child node is around O(26^26), and the proccess of finding the best child will happen at most k times, making the final complexity O(k26^26) -> O(k).
        :Aux Space complexity: O(k), several nodes has to be kept, and the amount of nodes being kept largely depends on k.
        """
        if noonccurence > len(self.timelines):
            return None
        else:
            best_node = self.getLongestChainAux(noonccurence, self.root)

        return best_node.substring[0:best_node.index]
    
class Node:
    def __init__(self, data = None) -> None:
        self.data = []
        self.string = []
        self.substring = None
        self.index = 0
        
        #Fixed size, 26 alphabets and 1 terminal symbol ($)
        self.links = [None] * 27

if __name__ == "__main__":
    print("👍")