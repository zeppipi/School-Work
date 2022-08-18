"""
For the FIT2004 Assignment 2, file includes two programs, one is to find an ideal coordinate in a list of coordinates where their distance between each coordinate and ideal coordinate's distance is minimal, and another that finds the most minimal route in a graph between a start, chores, and end.
"""
__author__ = 'Hazael Frans Christian'
__docformat__ = 'reStructuredText'
__modified__ = '13th of April 2022'
__since__ = '6th of April 2022'

import math

class RoadGraph:
    """
    Class makes graph that represents routes of roads
    """
    
    def __init__(self, roads:list) -> None:
        """
        Function will initialize the road graph structure, this graph will be using the adjaceny list representation
        
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
            w = edge[2]     #weight

            current_edge = Edges(u,v,w) #make an edge object with the currently selected info
            current_vertex = self.vertices[u]   #take the appropiate vertex to put the edge
            current_vertex.add_edge(current_edge)   #add edge to vertex
    
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
        edges = MinHeap(len(self.vertices))     #a heap
        
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

            v_current_distance = self.vertices[v].distance  #this vertex's current distance
            parent_distance = self.vertices[u].distance + w #their parent's current distance

            if parent_distance < v_current_distance:    #is it better to update the distance or leave it
                self.vertices[v].distance = self.vertices[u].distance + w
                parents[v] = u  #u has been confirmed to be the efficient parents of v
            
            if self.vertices[v].discovered == False:
                for index in self.vertices[v].edges:    #adds all of the next edges onto the heaps
                    edges.add(index)
                self.vertices[v].discovered = True
            
        return parents

    def make_graph(self, vertex_list:list) -> list:
        """
        A function that takes a list of vertex objects and turn it into a list of tuples, so they can be rendered again by the '__innit__' function

        :Input:
            :param vertex_list: list of vertex objects
        
        :PreCond: All elements in the list must be vertex objects
        :PostCond: the len of 'vertex_list' and the returned list must be the same

        :Time complexity: O(n), where n is the length of 'vertex_list'
        :Aux Space complexity: O(n), a temporary list is needed to return the value
        """
        res = []
        for vertex in vertex_list:
            for index in range(len(vertex.edges)):
                u = vertex.edges[index].u
                v = vertex.edges[index].v
                w = vertex.edges[index].w
                tuple = (u,v,w)
                res.append(tuple)
        
        return res

    def duplicate_graph(self, chores:list) -> list:
        """
        A function that returns two copies of the graph, connected by the vertices in 'chores'
        
        :Input:
            :param chores: The list of chores
        
        :returns: A list of tuples, that represents a new graph
        
        :PreCond: Numbers listed in 'chores' must be included vertices from self.vertices
        :PostCond: The amount of vertices returned must be len(self.vertices)*2

        :Time complexity: O(V), where V is the amount of vertices, this is because the worst amount of chores will be V
        :Aux Space Complexity: O(V+E), a copy of the grpah must be made
        """
        othergraph = self.make_graph(self.vertices)

        for index in range(len(othergraph)):
            u = othergraph[index][0]
            v = othergraph[index][1]
            w = othergraph[index][2]

            othergraph.append((u + len(self.vertices), v + len(self.vertices), w))
        
        for index in range(len(chores)):
            othergraph.append((chores[index], chores[index] + len(self.vertices), 0))

        return othergraph

    def routing(self, start:int, end:int, chores_location:list):
        """
        Finds the most efficient route from start to end by going to at least one chore. It does this by duplicating the orignal graph into two where you can only go to the duplicated graph using bridges made at the chores vertex, then running djikstra for that big double graph. Becuase of this method, backtracking from the duplicated end vertex to the start vertex will guaruntee a path that goes from start to end and going to at least one chore.

        :Input:
            :param start: The starting location
            :param end: The ending location
            :param chores_location: The list of chore locations
        
        :returns: A list of integers that represents the most efficient route between start, chores, and end

        :PreCond: All the numbers in 'start', 'end', and 'chores_location' must be valid vertices in the graph
        :PostCond: All the numbers in the returned list must be valid vertices in the graph

        :Time Complexity: O(VlogE), Most of the other proccesses in this function runs at O(V), the O(VlogE) comes from djikstra being needed in the proccess
        :Aux Space Complexity: O(V+E), A copy of the graph is needed for this function to run
        """
        double_graph = self.duplicate_graph(chores_location)    #duplicate the graph and connect them via the chores vertex
        double_graph = RoadGraph(double_graph)      #instantiate this new double graph
        paths = double_graph.djikstra(start)        #peform djikstra on this big graph

        backtrack_res = []
        res = []
        current_vertex = end + self.Vertex_count    #start looking from the end
        backtrack_res.append(current_vertex)
        while paths[current_vertex] is not current_vertex:  #O(V) time, at worst this can backtrack to every vertex
            
            #We reached an unreachable place
            if paths[current_vertex] == None:
                return None
            
            temp = paths[current_vertex]
            backtrack_res.append(temp)
            current_vertex = temp
        
        last_vertex = math.inf
        for index in range(len(backtrack_res)-1, -1, -1):
            if backtrack_res[index] >= self.Vertex_count:   #this vertex was a duplicate
                temp = backtrack_res[index] - self.Vertex_count
            else: 
                temp = backtrack_res[index]                 #this vertex wasnt a duplicate
            
            if temp != last_vertex:         #check if this is a copy
                res.append(temp)
            
            last_vertex = temp

        if res[0] != start:    #the root they found is not the start, meaning theres no path
            return None
        else:
            return res

class Vertex:
    """
    Class makes vertex which represents locations
    """

    def __init__(self, vertex) -> None:
        """
        Function will initialize a vertex, does things like keeping track of the edges it has and the ability to add more to it
        """
        self.vertex = vertex

        #this vertex's edges
        self.edges = []

        #the vertex's distance, for the djikstra
        self.distance = math.inf

        #the vertex's status, if it is discovered
        self.discovered = False

    def __str__(self) -> None:
        """
        Prints out this vertex's information
        """
        return_string = str(self.vertex)
        for edge in self.edges:
            return_string = return_string + "\n distance from source: " + str(self.distance) + " with edge " + str(edge)
            
        return return_string
    
    def add_edge(self, edge) -> None:
        """
        Takes an 'Edge' object and add it to this vertex's list of edges
        """
        self.edges.append(edge)

class Edges:
    """
    Class makes edges which represents the routes between locations
    """

    def __init__(self, u:int, v:int, w:int) -> None:
        """
        Initializes the information of this edge, where it starts, where it ends, and their weight
        :Input:
            :param u: where the edge starts
            :param v: where the edge ends
            :param w: the weight of the edge
        """
        self.u = u
        self.v = v
        self.w = w

    def __str__(self) -> str:
        """
        Print out this edge's information
        """
        return_string = "from " + str(self.u) + " to " + str(self.v) + " with weight " + str(self.w)
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

import sys
    
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


if __name__ == "__main__":
    print("👍")