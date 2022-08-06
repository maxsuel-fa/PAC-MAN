package elements;

import engine.Point;
import enums.GhostType;
import enums.Mode;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class represents the ghosts.
 *
 * Each ghost will be a instance of this class.
 *
 * @author Maxsuel F. Almeida
 */
public class Ghost extends Element {

    public Mode mode;
    public final GhostType type;
    public ArrayList<Point> path;
    public AnimatedImage animatedImage;
    public Image[] frames;
    public Point graphPosition;
    public long vulnerableTime;

    /**
     * Constructor of the class.
     *
     * @param type The type of the ghost.
     * @param row  Row of the ghost in the board.
     * @param col  Column of the ghost in the board.
     * @param frames All the images that are part of
     * the animation of the ghost.
     */
    public Ghost(GhostType type, int row, int col,  Image[] frames) {

        super(row, col, Speed.GHOST_NORMAL_SPEED);
        mode = Mode.NORMAL;
        this.type = type;
        path = new ArrayList<>();
        animatedImage = new AnimatedImage(8);
        this.frames = frames;
        this.setFrameAnimation();
        graphPosition = new Point(row, col);
        vulnerableTime = 0;
    }

    @Override
    public void render(GraphicsContext graphicsContext) {
        switch (direction) {

        case LEFT:
            this.setImage(animatedImage.getFrame(2, 2));
            break;
        case RIGHT:
            this.setImage(animatedImage.getFrame(0, 2));
            break;
        case UP:
            this.setImage(animatedImage.getFrame(3, 2));
            break;
        case DOWN:
            this.setImage(animatedImage.getFrame(1, 2));
            break;
        default:
            this.setImage(animatedImage.getFrame(0, 2));
            break;
        }

        graphicsContext.drawImage(this.getImage(), this.getX(), this.getY());
    }

    /**
     * Set which frames will be used in the animation depending on the mode of
     * the ghost.
     */
    public void setFrameAnimation() {
        long currMiliTime = System.currentTimeMillis();

        switch (mode) {

        case NORMAL:
            System.arraycopy(frames, 0, animatedImage.frames, 0, 8);
            break;

        case VULNERABLE:
            System.arraycopy(frames, (currMiliTime - vulnerableTime < 5000)
                             ? 8 : 16, animatedImage.frames, 0, 8);
            break;

        case DIED:
            System.arraycopy(frames, 24, animatedImage.frames, 0, 8);
            break;
        }
    }

    /**
     * Set the ghost speed depending on its mode and the level of the game.
     *
     * @param speedFactor Factor that depends of the level of the game.
     */
    public void setGhostSpeed(double speedFactor) {
        switch (mode) {
        case NORMAL:
            if (type.equals(type.PINKY))
                this.setSpeed(speedFactor * Speed.GHOST_NORMAL_SPEED - 0.008);
            else
                this.setSpeed(speedFactor * Speed.GHOST_NORMAL_SPEED);
            break;
        case VULNERABLE:
            this.setSpeed(speedFactor * Speed.GHOST_VULNERABLE_SPEED);
            break;
        case DIED:
            this.setSpeed(Speed.GHOST_DIED_SPEED);
            break;
        default:
            this.setSpeed(0);
            break;
        }
    }
}
