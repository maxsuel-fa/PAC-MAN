package elements;

import enums.FruitType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class represents the fruits of the game.
 * 
 * Each fruit will be a instance of such class.
 * 
 * @author Maxsuel F. de Almeida
 */
public class Fruit extends Element{
    
    public FruitType type;
    public int value;
    
    /**
     * Constructor of the class.
     * 
     * @param row Row of the fruit in the board.
     * @param col Column of the fruit in the board.
     * @param type Fruit type.
     * @param value Fruit value in terms of score.
     * @param image Image of the fruit.
     */
    public Fruit(int row, int col, FruitType type, int value, Image image) {
        super(row, col, image);
        this.type = type;
        this.value = value;
    }
    
    @Override
    public void render(GraphicsContext graphicsContext) {
        graphicsContext.drawImage(this.getImage(), this.getX(), this.getY());
    }
    
}
