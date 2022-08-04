package graphicinterface;

import elements.Board;
import elements.Element;
import elements.Fruit;
import elements.Ghost;
import elements.LengthConstants;
import enums.Mode;
import elements.PacMan;
import engine.Engine;
import enums.Status;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import utils.Utils;

/**
 * This class is responsible for the graphic interface of the game.
 * 
 * @author Maxsuel F. de Almeida
 */
public class GameScreen extends Application {
    
    private GraphicsContext gc;
    private Scene scene;
    private final int stageWidth = LengthConstants.STAGE_WIDTH;
    private final int stageHeight = LengthConstants.STAGE_HEIGHT;
    private Status gameStatus;
    private final Engine eng;
    private final PacMan pacMan;
    private final Board board;
    private final ArrayList<Ghost> ghosts;
    private final ArrayList<Element> foods;
    
    // Images of the fruits that represents the levels
    Image[] levelsImages = new Image[4];
            
    /**
     * The class constructor.
     */
    public GameScreen() {
        // Image for the background 
        Image boardImage = new Image("images/background.png", 784, 868, true, true);
        
        // Initialize the fruits images that represents the levels
        levelsImages[0] = new Image("images/cherry.png", 36, 36, true, true);
        levelsImages[1] = new Image("images/strawberry.png", 36, 36, true, true);
        levelsImages[2] = new Image("images/orange.png", 36, 36, true, true);
        levelsImages[3] = new Image("images/apple.png", 36, 36, true, true);
        
        gameStatus = Status.MENU;
        eng = new Engine();
        board = new Board(boardImage);   
        pacMan = Utils.addPacMan();
        ghosts = Utils.addGhosts();
        foods = Utils.addFoods(board);
    }
    
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        scene = new Scene(root, stageWidth, stageHeight);
        Canvas canvas = new Canvas(stageWidth, stageHeight);        
        gc = canvas.getGraphicsContext2D();          
        root.getChildren().add(canvas);
        
        stage.setTitle("PACMAN GAME");
        stage.getIcons().add(new Image("images/pacman.png"));
        stage.setScene(scene);
        
        gameLoop(stage);
    }
    
    /**
     * Loop for the game.
     * @param stage 
     */
    public void gameLoop(Stage stage) {
        // Animation for the died pacman
        PacManDiedAnimation pacDiedAnimation 
                                    = new PacManDiedAnimation(pacMan, gc);
        AnimationTimer animationTimer;
        
        animationTimer = new AnimationTimer() {
            long startMiliTime;
            long currMiliTime;
            
            //Image for the pacman lives
            Image pacLife = new Image("images/pacman_life.png", 36, 36, true, true);
            
            @Override
            public void handle(long currentNanoTime) {
                // Clears the game screen
                gc.clearRect(0, 0, stageWidth, stageHeight);
                
                // Set the white color for the retangles
                gc.setFill(Color.BLACK);
                
                // Fill the background
                gc.fillRect(0, 0, stageWidth, stageHeight);
                
                switch(gameStatus) {
                    case MENU:
                        // To make the text animated
                        boolean isVisible = 
                                (int) (System.nanoTime() * 0.0000000030) % 2 == 0;
                        
                        // Set the Arial font
                        gc.setFont(new Font("Arial", 35));
                        
                        // Set the white color for the text
                        gc.setFill(Color.WHITE);
                        
                        if(isVisible)
                            // Write the text
                            gc.fillText("PRESS SPACE TO START THE GAME", 78, 434);
                        
                        // Arial font with a smaller size
                        gc.setFont(new Font("Arial", 30));
                        
                        // Write the author's name in the screen
                        gc.fillText("DEVELOPED BY MAXSUEL F. ALMEIDA", 100, 860);
                        
                        // Draws the Menu Logo
                        gc.drawImage(new Image("images/title.png", 546, 138, true, true), 119, 56);
                        
                        /* Get one key typed by the player. If the key is space
                           starts the game.
                        */
                        scene.setOnKeyPressed((KeyEvent e) -> {
                            KeyCode k = e.getCode();
                            if(k == KeyCode.SPACE) {
                                gameStatus = Status.READY;
                                eng.resetLives();
                                eng.resetScore();
                                Utils.resetAll(board, pacMan, ghosts, foods);
                                startMiliTime = System.currentTimeMillis();
                            }
                        });
                        break;
                    
                    case READY:
                        board.render(gc);
                        
                        for(Ghost g : ghosts)
                            g.render(gc);
                        
                        pacMan.render(gc);
                        
                        // Draws the ready logo
                        gc.drawImage(new Image("images/ready.png", 138, 21, true, true), 323, 518);
                        
                        currMiliTime = System.currentTimeMillis();
                        
                        // Spend three seconds in the ready modo
                        if(currMiliTime - startMiliTime > 3000)
                            gameStatus = Status.PLAYING;
                        break;
                    case PLAYING:
                        if(pacMan.mode.equals(Mode.NORMAL)) {
                            /* Set the next direction of the pacman by taking 
                               the key pressed by de player*/
                            pacMan.setNextDirection(scene);
                            
                            /* Updates all the elements positions and check for
                               collisions between elements*/
                            eng.elementsUpdate(board, pacMan, ghosts, foods);
                            
                            /* If the number of eaten pacdots are 70 or 140, add 
                               a fruit in the board*/
                            if(pacMan.eatenPacDots == 70 || pacMan.eatenPacDots == 140)
                            {
                                foods.add(Utils.addFruit(eng.getLevel()));
                                startMiliTime = System.currentTimeMillis();
                            }
                            
                            // After 5 seconds the fruit disappear
                            currMiliTime = System.currentTimeMillis();
                            if(currMiliTime - startMiliTime > 5000 
                               && !foods.isEmpty()
                               && Fruit.class.isInstance(foods.get(foods.size() - 1))) {
                                foods.remove(foods.get(foods.size() - 1));
                            }
                            
                            board.render(gc);
                            
                            /* Draws all the pacdots and power pills that still 
                              in the game*/
                            for(Element f : foods)
                                f.render(gc);
                            
                            for(Ghost g : ghosts) {
                                g.setFrameAnimation();
                                g.render(gc);
                            }
                            
                            pacMan.render(gc);
                            
                            // Color for the score 
                            gc.setFill( Color.WHITE);
                            Font scoreFont = Font.font( "Arial", FontWeight.BOLD, 32);
                            gc.setFont(scoreFont);
                            // Draws the score
                            gc.fillText("SCORE: " + eng.getScore(), 28, 32);
                            // Draws the lives
                            gc.fillText("LIVES: ", 28, 938);
                            for(int i = 0; i < eng.getLives(); i++) {
                                gc.drawImage(pacLife, 162 + 36 * i, 910);
                            }
                            
                            // Draws the level
                            gc.fillText("LEVEL: ", 612, 938);
                            gc.drawImage(levelsImages[(eng.getLevel() - 1) % 4], 734, 910);
                            
                            if(foods.isEmpty()) {
                                eng.levelUp(board, pacMan, ghosts, foods, 
                                        gameStatus);
                                
                                // Timers for one pause
                                startMiliTime = System.currentTimeMillis();
                                currMiliTime = System.currentTimeMillis();
                                
                                
                                while(currMiliTime - startMiliTime < 3000) {
                                    currMiliTime = System.currentTimeMillis();
                                }
                                startMiliTime = System.currentTimeMillis();
                            }
                        }
                        else if(pacMan.mode.equals(Mode.DIED) && eng.getLives() > 0) {
                            board.render(gc);
                            
                            // Starts the animation of the died pacman
                            pacDiedAnimation.start();
                            
                            currMiliTime = System.currentTimeMillis();
                            
                            // Stop the animation after 4 seconds
                            if(currMiliTime - pacMan.diedTime > 4000) {
                                pacDiedAnimation.stop();
                                // Reset the position of all dynamic elements
                                Utils.resetDynamicElems(pacMan, ghosts);
                                // Turn back the pacman mode to normal
                                pacMan.mode = Mode.NORMAL;
                                startMiliTime = System.currentTimeMillis();
                                // Change the game status to ready
                                gameStatus = Status.READY;
                            }
                        }
                        else if(eng.getLives() == 0) {
                            // Change the game status to game over
                            gameStatus = Status.GAMEOVER;
                            startMiliTime = System.currentTimeMillis();
                        }
                        break;
                    case GAMEOVER:
                        // Restart the leve 
                        eng.setLevel(1);
                        board.render(gc);
                        
                        pacDiedAnimation.start();
                        
                        currMiliTime = System.currentTimeMillis();
                        
                        if(currMiliTime - pacMan.diedTime > 4000) {
                            pacDiedAnimation.stop();
                            // Draws the game over logo 
                            gc.drawImage(new Image("images/gameover.png", 142, 14, true, true), 321, 427);
                        }
                        
                        currMiliTime = System.currentTimeMillis();
                        
                        // After ten seconds, go back to the menu
                        if(currMiliTime - startMiliTime > 10000) {
                            gameStatus = Status.MENU;
                        }
                        break;
                }   
                
            }
        };
        animationTimer.start();
        stage.show();
    }
}
