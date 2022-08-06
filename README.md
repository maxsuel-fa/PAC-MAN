# Introduction
This is a Java implementation of the pac-man game made for the college subject "SCC0604 - Object-Oriented Programming" at University Of São Paulo - USP. 
The main goal in implement such project was to apply the four pillar of OOP design: abstraction, encapsulation, inheritance and polymorfism.

# Ghosts behavior
The approach used to control the behavior of the ghosts was not equal to the used in the original game. In fact, it was a way simpler. Here, Inky and Clyde just move randomly in the maze while Blinky and Pinky keep chasing the pac-man. To execute the ghosts' movements, each cell of the maze was considered as a node of a graph and the Breadth First Search algorithm was used to find the shortest path between one ghost and the point such ghost  intended to reach.

# Some images of the game





# How to run

**To run the game, it is neccessary to have the Oracle JDK 8 installed in your machine**

Download the file _pacman.jar_ in the root of this repository and then, in your terminal

    java -jar pacman.jar


    
