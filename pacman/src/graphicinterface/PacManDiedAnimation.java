package graphicinterface;

import elements.PacMan;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
     * This class is responsible for the animation when pacman dies.
     * 
     * @author Maxsuel F. de Almeida
     */
    public class PacManDiedAnimation extends AnimationTimer {
        PacMan pacMan;
        double xCoord, yCoord;
        Image[] frames;
        GraphicsContext gc;

        /**
         * The class constructor.
         * 
         * @param pacMan One reference for the pacman.
         * @param gc Reference for the graphicsContext.
         */
        public PacManDiedAnimation(PacMan pacMan, GraphicsContext gc) {
            this.pacMan = pacMan;
            frames = new Image[30];
            System.arraycopy(pacMan.frames, 0, frames, 0, 30);
            this.gc = gc;
        }
        
        @Override
        public void handle(long now) {
            this.xCoord = pacMan.getX();
            this.yCoord = pacMan.getY();
            gc.drawImage(
                    frames[16 + (int)(System.nanoTime() * 0.000000003) % 14], 
                    xCoord, yCoord);
        }
    }
    