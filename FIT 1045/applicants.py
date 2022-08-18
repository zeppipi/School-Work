list_of_people = [['Rose Winds', 'Cloud Computing', 'USA', 6.5, 5000, 5, 'F'],
 ['Merry Brown', 'Cloud Computing', 'UK', 5, 7000, 5, 'F'],
 ['Lina Mesro', 'Cyber Security', 'Malaysia', 7, 6500, 4, 'F'],
 ['Abdul Fazil', 'Big Data', 'Australia', 11, 8000, 4, 'M'],
 ['Chris Janes', 'Big Data', 'Ireland', 7, 2500, 4, 'M'],
 ['Sireen May', 'Artificial Intelligence', 'Australia', 0, 4000, 4, 'F'],
 ['Jine Tims', 'Robotics', 'Australia', 0, 3500, 3, 'M'],
 ['Niki Rohdes', 'Artificial Intelligence', 'China', 9, 8500, 5, 'F']]

#lists of countries from their continent
Europe = ['UK','Ireland']
Asia = ['Malaysia', 'China']
Oceania = ['Australia', 'New Zealand']
NA = ['America', 'Canada']

#TASK 1
#a function that will take what is in the condition and see if the applicant's profile meet it's requirments 
def test(condition, applicant_profile):

    if condition == qualified_from_europe:
        if applicant_profile[2] in Europe:
            return True
        else:
            return False

    elif condition == from_asia:
        if applicant_profile[2] in Asia:
            return True
        else:
            return False
     
    elif condition == budget_friendly:
        if applicant_profile[4] < 3500:
            return True
        else:
            return False
    
    elif condition == highly_qualified:
        if applicant_profile[5] > 3:
            return True
        else:
            return False

    elif condition == sufficiently_experienced:
        if applicant_profile[3] > 3:
            return True
        else:
            return False
    
    else:
        print("Input Not Valid")


#empty functions so the conditions doesn't need to be written as strings
def qualified_from_europe():
    pass
def budget_friendly():
    pass
def highly_qualified():
    pass
def sufficiently_experienced():
    pass
def from_asia():
    pass

#TASK 2

def filtering_applicants(profiles, conditions):
    accept = [] #empty list to put in all of the profiles that fits the condition
    global filename #make empty list 'filename' global so function 3 can use it later on
    filename = []
    for current_name in range(len(conditions)): #turn function into strings
        filename.append(conditions[current_name].__name__) #even with the red line, code still allows
    filename = "_".join(filename)
    
    for current_profile in range(len(profiles)): #loops through the profiles and inserted conditions
        accept.append(profiles[current_profile]) #starts each loop by accepting the profile
        for current_condidtion in range(len(conditions)):
            if test(conditions[current_condidtion],profiles[current_profile]) == False:
                accept.remove(profiles[current_profile]) #if theres a false condition, remove the profile...
                break #...and end this inner for loop cycle
    
    prompt = input("Would you like to save the profiles into a seperate CSV file? y/n")
    if prompt == "y":
        write_to_file(accept) #the accept list will be the output
    else:
        quit

    
    
    

#TASK 3
def write_to_file(list_of_profiles):
    f = open(filename + '.csv','w') #create file with the custom filename given from function 2
    header = 'ID, Full name, Specialization, Country of highest qualification, Years of experience, Expected salary, Qualification level, Gender\n'
    
    for index in range(len(list_of_profiles)): #give every element in the accepted profiles their IDs
        list_of_profiles[index].insert(0,index+1)

    for outer in range(len(list_of_profiles)): #gives each list element their respective "," "\n" so they output in the file correctly
        for inner in range(len(list_of_profiles[outer])):
            list_of_profiles[outer][inner] = str(list_of_profiles[outer][inner]) +","
            if inner == len(list_of_profiles[outer])-1:
                list_of_profiles[outer][inner] = str(list_of_profiles[outer][inner])+'\n'

    f.write(header) #write the header first

    for oouter in range(len(list_of_profiles)): #loop to write the rest of the results
        for innner in range(len(list_of_profiles[oouter])):
            f.write(list_of_profiles[oouter][innner])
    f.close() #close file






