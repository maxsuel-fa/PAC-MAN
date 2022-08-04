package elements;

import javafx.scene.image.Image;

/**
 * Create the animation of a dynamic element.
 * @author Maxsuel F. Almeida
 */
public class AnimatedImage {
    protected Image[] frames;

    /**
     * Constructor of the class.
     * @param length
     */
    public AnimatedImage(int length) {
        frames = new Image[length];
    }

    /**
     * Get the frame to be painted on the screen.
     * @param direction The current direction of the element.
     * @param imagesPerDirection The number of image that is part of the
     * animation in one direction.
     * @return The frame to be painted.
     */
    public Image getFrame(int direction, int imagesPerDirection) {
        int index = imagesPerDirection * direction
                    + (int)(System.nanoTime() * 0.00000002) % imagesPerDirection;

        return frames[index];
    }
}
