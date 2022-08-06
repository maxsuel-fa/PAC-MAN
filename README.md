# Introduction
It is a Java implementation of the pac-man game made for the college subject "SCC0604 - Object-Oriented Programming" at the University of São Paulo - USP. 
The main goal in implementing such project was to apply the four pillars of OOP design: abstraction, encapsulation, inheritance and polymorphism.

# Ghosts behavior
The approach used to control the behavior of the ghosts was not equal to the used in the original game. In fact, it was a way simpler. Here, Inky and Clyde just move randomly in the maze while Blinky and Pinky keep chasing the pac-man. To execute the ghosts' movements, each cell of the maze was considered as a node of a graph and the Breadth First Search algorithm was used to find the shortest path between one ghost and the point such ghost  intended to reach.

# Some images of the game

## Start Screen

<img src="https://github.com/maxsuel-fa/PAC-MAN/blob/main/imgs/startScreen.gif" style="width:390.5px;height:472px;">

## Ready! Screen

<img src="https://github.com/maxsuel-fa/PAC-MAN/blob/main/imgs/readyScreen.png" style="width:390.5px;height:472px;">

## Playing Screen

<div class="img_container">
    <img class="image-align-left" src="https://github.com/maxsuel-fa/PAC-MAN/blob/main/imgs/Playing1.png" style="width:390.5px;height:472px;"/>
    <img class="image-align-left" src="https://github.com/maxsuel-fa/PAC-MAN/blob/main/imgs/Playing2.png" style="width:390.5px;height:472px;">
</div>


# How to run

**To run the game, it is required to have the Oracle JDK 8 installed on your machine.**

Download the file _pacman.jar_ in the root of this repository and then, in your terminal

    java -jar pacman.jar

# To Do list
* Adding sound to the game;
* Change the chasing mechanism of the ghosts.
    
