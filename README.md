# movies-dedupication

**assumptions:** 
1. All data that is loaded from file can fit in memory

2. No need to persist data for long term storage or query all data and compare it to the current file, in that case we can use some read optimized data store, i dont have any expirience with eastic but i started thinking of going to that direction.

3. input file may have corrupted columns(bad type, out or order columns), in that case i the parsing will fail(i had some difficulty to ignore it)

4. My notation of matching logic for lists(actors,genres and directors) is that if both lists are empty or if one list is empty and the other has 1 element or one list is a sub list of another they will match, 
in all other cases they don't match.

5. Each raw in the output file match all movies of it's kind, meaning if` a~b `and` b~c `we will have one row only with` a~b~c` 

6. I used spring boot for easy "dependency injection" so testing will be easier

7. Movie id is unique.

**basic architecture :**

1. Load all data from csv to memory.

2. Create search index that is based on TreeMap with a compound key of year(lower cardinality) and length which will help me search for
matching movies and then filter it with all the other fields matching.

3. Iterate of the list of movies and to match it with similar movies

4. Merge collection that have common member so that each movie
 
5. For that purpose i build a graph that each list of matching movies is a vertex and each other list that have a common member is it's neighbour.

6. Run dfs on vertex and find all trees in the forest. each tree represent a list of matching movies

7. Write all lists of matching movies to csv file


in order to run the app please also add two arguments for input and output file, and also add in the vm options: Dspring.profiles.active=main 

** i wrote some basic test but i hadn't used my time for writing test for the merging lists part

i probably have bugs there
