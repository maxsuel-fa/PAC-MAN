package utils;

import elements.Board;
import elements.Element;
import elements.Fruit;
import elements.Ghost;
import elements.LengthConstants;
import elements.PacDot;
import elements.PacMan;
import elements.PowerPill;
import elements.Speed;
import engine.Point;
import enums.FruitType;
import enums.GhostType;
import enums.Mode;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

/**
 * Class to hold useful methods.
 * This class hold the methods that are useful in the construction of the game
 * but do not fit in other classes.
 * @author Maxsuel F. de Almeida
 */
public class Utils {
    /**
     * Add the pacdots and the power pills in the game.
     * @param board where the foods will be added.
     * @return ArrayList comprising the foods(pacdots and power pills).
     */
    static public ArrayList<Element> addFoods(Board board) {
        /* Images for pacdots and power pills*/
        Image[] foodImages = new Image[2];

        foodImages[0] = new Image("images/food.png", 7, 7, true, true);
        foodImages[1] = new Image("images/powerPill.png", 14, 14, true, true);

        /* Instantiate the foods' list*/
        ArrayList<Element> foodList = new ArrayList<>();

        /* Add pacdots and power pills in the board*/
        for(int i = 0; i < board.maze.length; i++) {
            for(int j = 0; j < board.maze[0].length; j++) {
                if(board.maze[i][j] == '.')
                    foodList.add(new PacDot(i, j, foodImages[0]));
                if(board.maze[i][j] == 'O')
                    foodList.add(new PowerPill(i, j, foodImages[1]));
            }
        }
        
        return foodList;
    }

    /**
     * Add the ghosts in the game.
     * @return ArrayList comprising the ghosts.
     */
    static public ArrayList<Ghost> addGhosts() {
        /* Images for each kind of ghost*/
        Image[] blinkyImages = new Image[32];
        Image[] inkyImages = new Image[32];
        Image[] pinkyImages = new Image[32];
        Image[] clydeImages = new Image[32];

        /* Initiale the images*/
        for(int i = 0; i < 8; i++) {
            /* Initiate the blinky images*/
            blinkyImages[i] = new Image("images/ghost_0" + "_" + i + ".png", 36, 36, false, false);
            blinkyImages[i + 8] = new Image("images/ghost_vulnerable_0_" + i + ".png", 36, 36, false, false);
            blinkyImages[i + 16] = new Image("images/ghost_vulnerable_1_" + i + ".png", 36, 36, false, false);
            blinkyImages[i + 24] = new Image("images/ghost_died_" + i + ".png", 36, 36, false, false);
            
            /* Initiate the inky images*/
            inkyImages[i] = new Image("images/ghost_2" + "_" + i + ".png", 36, 36, false, false);
            inkyImages[i + 8] = new Image("images/ghost_vulnerable_0_" + i + ".png", 36, 36, false, false);
            inkyImages[i + 16] = new Image("images/ghost_vulnerable_1_" + i + ".png", 36, 36, false, false);
            inkyImages[i + 24] = new Image("images/ghost_died_" + i + ".png", 36, 36, false, false);
            
            /* Initiate the pinky images*/
            pinkyImages[i] = new Image("images/ghost_1" + "_" + i + ".png", 36, 36, false, false);
            pinkyImages[i + 8] = new Image("images/ghost_vulnerable_0_" + i + ".png", 36, 36, false, false);
            pinkyImages[i + 16] = new Image("images/ghost_vulnerable_1_" + i + ".png", 36, 36, false, false);
            pinkyImages[i + 24] = new Image("images/ghost_died_" + i + ".png", 36, 36, false, false);
            
            /* Initiate the clyde images*/
            clydeImages[i] = new Image("images/ghost_3" + "_" + i + ".png", 36, 36, false, false);
            clydeImages[i + 8] = new Image("images/ghost_vulnerable_0_" + i + ".png", 36, 36, false, false);
            clydeImages[i + 16] = new Image("images/ghost_vulnerable_1_" + i + ".png", 36, 36, false, false);
            clydeImages[i + 24] = new Image("images/ghost_died_" + i + ".png", 36, 36, false, false);
        }
        /* Instantiate the ghosts' list*/
        ArrayList<Ghost> ghostList = new ArrayList<>();

        /* Creat all the ghosts and put them in the ghosts' list*/
        ghostList.add(new Ghost(GhostType.BLINKY, 13, 12, blinkyImages));
        ghostList.add(new Ghost(GhostType.PINKY, 14, 15, pinkyImages));
        ghostList.add(new Ghost(GhostType.INKY, 14, 12, inkyImages));
        ghostList.add(new Ghost(GhostType.CLYDE, 13, 15, clydeImages));

        /* PINKY needs to be a little bit slower than BLINKY*/
        ghostList.get(1).setSpeed(Speed.GHOST_NORMAL_SPEED - 0.008);
        
        return ghostList;
    }

    /**
     * Add the pacman in the game.
     * @return Pacman.
     */
    static public PacMan addPacMan() {
        /* Images for the pacman*/
        Image[] pacImages = new Image[30];

        /* Instanciate the normal images*/
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                pacImages[4 * i + j] = new Image("images/pacman_" + i + "_" + j + ".png", 36, 36, false, false);
        
        /* Instanciate the died images*/
        for(int i = 0; i < 14; i++)
            pacImages[i + 16] = new Image("images/pacman_died_" + i + ".png", 36, 36, false, false);

        /* Instanciate the pacman*/
        return new PacMan(pacImages);
    }
    
    /**
     * Static method to add fruit in the game.
     * @param level
     * @return
     */
    static public Fruit addFruit(int level) {
        // Images of the fruits
        Image[] fruitImages = new Image[4];
        
        fruitImages[0] = new Image("images/cherry.png", 36, 36, true, true);
        fruitImages[1] = new Image("images/strawberry.png", 36, 36, true, true);
        fruitImages[2] = new Image("images/orange.png", 36, 36, true, true);
        fruitImages[3] = new Image("images/apple.png", 36, 36, true, true);
        
        // Instatiate the fruit
        Fruit fruit;
        /* This index helps to decide each kind of fruit will placed in the
           board according to the level*/
        int index = (level - 1) % 4;
        
        // Arithmetic progression is not that useless hahaha
        int value = 100 + (level - 1) * 200;
        
        switch(index) {
            case 1:
                fruit = new Fruit(17, 15, FruitType.CHERRY, value, fruitImages[index]);
                break;
                
            case 2:
                fruit = new Fruit(17, 15, FruitType.STRAWBERRY, value, fruitImages[index]);
                break; 
            
            case 3:
                fruit = new Fruit(17, 15, FruitType.ORANGE, value, fruitImages[index]);
                break;
                
            default:
                fruit = new Fruit(17, 15, FruitType.APPLE, value, fruitImages[index]);
                break;
        }
        
        return fruit;
    }
    
    /**
     * Put all the dynamic elements in their initial positions
     * @param pacMan
     * @param ghosts
     */
    static public void resetDynamicElems(PacMan pacMan, ArrayList<Ghost> ghosts) {
        // Auxiliar variables
        int row, col;
        
        // Initializing the aux variables with the pacman initial position
        col = 14;
        row = 23;
        
        // Reset pacman position
        pacMan.setColumn(col);
        pacMan.setRow(row);
        pacMan.doubleCol = col;
        pacMan.doubleRow = row;
        pacMan.setX(pacMan.getProportionX() * col);
        pacMan.setY(pacMan.getProportionY() * row + LengthConstants.BACKGROUND_DOWNWARD_OFFSET);
        pacMan.direction = KeyCode.UNDEFINED;
        pacMan.nextDirection = KeyCode.UNDEFINED;
        
        // Reset the position of all ghosts
        for(Ghost g : ghosts) {
            switch(g.type) {
                case BLINKY:
                    row = 13;
                    col = 12;
                    break;
                case CLYDE:
                    row = 13;
                    col = 15;
                    break;
                case INKY:
                    row = 14;
                    col = 12;
                    break;
                default: // PINKY
                    row = 14;
                    col = 15;
                    break;
            }  
            
            
            g.setColumn(col);
            g.setRow(row);
            g.doubleCol = col;
            g.doubleRow = row;
            g.setX(g.getProportionX() * col);
            g.setY(g.getProportionY() * row + LengthConstants.BACKGROUND_DOWNWARD_OFFSET);
            g.path.removeAll(g.path);
            g.graphPosition = new Point(col, row);
            g.mode = Mode.NORMAL;
            g.setFrameAnimation();
        }
    }

    /**
     * Reset all the elements in the game.
     * @param board
     * @param pacMan
     * @param ghosts
     * @param foods
     */
    static public void resetAll(Board board, PacMan pacMan,
            ArrayList<Ghost> ghosts, ArrayList<Element> foods) {
        // Reset the positions of pacman and ghosts
        resetDynamicElems(pacMan, ghosts);
        // Revome all remaining food in the board
        foods.removeAll(foods);
        // Add foods in all possible positions
        foods.addAll(Utils.addFoods(board));
    }
}
