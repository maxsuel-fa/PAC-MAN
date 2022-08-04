package elements;


import enums.Mode;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * This class represents the pac-man.
 * 
 * @author Maxsuel F. de Almeida
 */
public class PacMan extends Element {
    
    private final AnimatedImage animatedImage;
    public final Image[] frames;
    
    public Mode mode;
    public long diedTime;
    public int eatenPacDots;
    
    /**
     * The class constructor.
     * @param frames
     */
    public PacMan(Image[] frames)
    {
        super(23, 14, 0.11);
        animatedImage = new AnimatedImage(16);
        this.frames = frames;
        mode = Mode.NORMAL;
        this.setFrameAnimation();
        diedTime = 0;
        eatenPacDots = 0;
    }
    
    @Override
    public void render(GraphicsContext graphicsContext)
    {
        switch(direction)
        {
            case LEFT:
                this.setImage(animatedImage.getFrame(2, 4));
                break;
            case RIGHT:
                this.setImage(animatedImage.getFrame(0, 4));
                break;
            case UP:
                this.setImage(animatedImage.getFrame(3, 4));
                break;
            case DOWN:
                this.setImage(animatedImage.getFrame(1, 4));
                break;
            default:
                this.setImage(animatedImage.getFrame(0, 4));
                break;
        }
        
        graphicsContext.drawImage(this.getImage(), this.getX(), this.getY());
    }
    
    /**
     * Get the key pressed by the player and stores it in the next direction 
     * atribute.
     * 
     * @param scene
     */
    public void setNextDirection(Scene scene) {
        
        scene.setOnKeyPressed((KeyEvent e) -> 
        {
            KeyCode key = e.getCode();
            if(key == KeyCode.LEFT || key == KeyCode.RIGHT || key == KeyCode.UP || key == KeyCode.DOWN) {
                nextDirection = key;
            }     
        });
    }
    
    /**
     * Set which images will be used in the animation.
     */
    public void setFrameAnimation() {
        System.arraycopy(frames, 0, animatedImage.frames, 0, 16);   
    }
}
