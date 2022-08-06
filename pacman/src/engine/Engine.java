package engine;

import elements.Board;
import elements.Element;
import elements.Fruit;
import elements.Ghost;
import enums.GhostType;
import elements.LengthConstants;
import enums.Mode;
import elements.PacDot;
import elements.PacMan;
import elements.PowerPill;
import elements.Speed;
import enums.Status;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import utils.Utils;

/**
 * Engine of the game.
 * This class is responsible for processing movement, for check collisions,
 * for counting points, for counting lives and for managing levels.
 * @author Maxsuel F. de Almeida
 */
public class Engine {

    private int score;
    private int lives;
    private int level;
    private int eatenGhosts;
    private long currMilliTime;
    private double speedFactor;

    /**
     * Constructor of the class.
     */
    public Engine() {
        score = 0;
        lives = 3;
        level = 1;
        eatenGhosts = 0;
        this.setSpeedFactor();
    }

    public void elementsUpdate(
        Board board, PacMan pacMan,
        ArrayList<Ghost> ghosts, ArrayList<Element> foods) {
        // Move pacman to a new position
        this.shiftDirection(pacMan, board);
        this.move(pacMan, board);

        // Update all the ghosts
        for (Ghost g : ghosts) {
            int gRow = g.getRow();
            int gCol = g.getColumn();

            // Move the ghosts to a new position
            this.ghostMove(g, pacMan, board);

            // Check collision with the pacman
            if (this.areColliding(pacMan, g)) {
                switch (g.mode) {
                case NORMAL:
                    pacMan.mode = Mode.DIED;
                    pacMan.diedTime = System.currentTimeMillis();
                    lives--;
                    break;

                case VULNERABLE:
                    eatenGhosts++;
                    score += 200 * ((int) Math.pow(2, eatenGhosts - 1));
                    g.mode = Mode.DIED;
                    g.setGhostSpeed(speedFactor);
                    break;

                default:
                    break;
                }
            }

            currMilliTime = System.currentTimeMillis();

            // After 8 seconds the vulnerable time ends
            if (currMilliTime - g.vulnerableTime > 8000) {
                g.mode = Mode.NORMAL;
                eatenGhosts = 0;
                g.setGhostSpeed(speedFactor);
            }

            /*
             * When one ghost get back to the ghosts' cage, it turns back in the
             * normal mode
             */
            if (board.cells.get(28 * gRow + gCol).isGhostHouse(board.maze)) {
                g.mode = Mode.NORMAL;
                g.setGhostSpeed(speedFactor);
            }

        }

        // Iterator for the foods' list
        Iterator<Element> f = foods.iterator();
        Element food;

        // Iterates over the foods' list
        while (f.hasNext()) {
            food = f.next();

            if (areColliding(pacMan, food) && PacDot.class.isInstance(food)) {
                score += ((PacDot) food).getValue();
                f.remove();
                pacMan.eatenPacDots++;
            } else if (areColliding(pacMan, food) && PowerPill.class.isInstance(food)) {
                score += ((PowerPill) food).getValue();
                f.remove();
                pacMan.eatenPacDots++;
                for (Ghost g : ghosts) {
                    g.mode = Mode.VULNERABLE;
                    g.vulnerableTime = System.currentTimeMillis();
                    g.setGhostSpeed(speedFactor);
                    g.path.removeAll(g.path);
                }
            } else if (areColliding(pacMan, food) && Fruit.class.isInstance(food)) {
                score += ((Fruit) food).value;
                f.remove();
            }
        }
    }

    /**
     * Move the given element to its next position.
     *
     * @param elem  The element to be moved.
     * @param board The board.
     */
    public void move(Element elem, Board board) {
        // Get the current position of the element
        int col = elem.getColumn();
        int row = elem.getRow();
        double x = elem.getX();
        double y = elem.getY();

        /*
         * Get the proportion between the array that represents the maze and
         * the backgroud size
         */
        int propX = elem.getProportionX();
        int propY = elem.getProportionY();

        // Get the speed of the element
        double speed = elem.getSpeed();

        switch (elem.direction) {
        case LEFT:
            if (board.cells.get(28 * row + col).isLeftPortal(board.maze)) {
                col = board.maze[0].length;
                elem.doubleCol = col;
                x = propX * col;
            } else if (!board.cells.get(28 * row + col - 1).isWall(board.maze)) {
                elem.doubleCol -= speed;
                col = (elem.doubleCol > col - 1) ? col : col - 1;
                x -= propX * speed;
            }
            break;

        case RIGHT:
            if (board.cells.get(28 * row + col).isRightPortal(board.maze)) {
                col = -1;
                elem.doubleCol = col;
                x = propX * col;
            } else if (!board.cells.get(28 * row + col + 1).isWall(board.maze)) {
                elem.doubleCol += speed;
                col = (elem.doubleCol < col + 1) ? col : col + 1;
                x += propX * speed;
            }
            break;

        case UP:
            if (!board.cells.get(28 * row + col - 28).isWall(board.maze)) {
                elem.doubleRow -= speed;
                row = (elem.doubleRow > row - 1) ? row : row - 1;
                y -= propY * speed;
            }
            break;

        case DOWN:
            if (!board.cells.get(28 * row + col + 28).isWall(board.maze)) {
                elem.doubleRow += speed;
                row = (elem.doubleRow < row + 1) ? row : row + 1;
                y += propY * speed;
            }
            break;

        default:
            break;
        }

        // Set a new position for the element
        elem.setColumn(col);
        elem.setRow(row);
        elem.setX(x);
        elem.setY(y);
    }

    /**
     * Verify if the direction is available.
     *
     * @param elem
     * @param board
     */
    public void shiftDirection(Element elem, Board board) {
        // Get the current position of the element
        int col = elem.getColumn();
        int row = elem.getRow();
        double x = elem.getX();
        double y = elem.getY();

        /*
         * Get the proportion between the array that represents the maze and
         * the backgroud size
         */
        int propX = elem.getProportionX();
        int propY = elem.getProportionY();

        switch (elem.nextDirection) {
        case LEFT:
            elem.direction = (
                                 !board.cells.get(28 * row + col - 1).isWall(board.maze)
                                 && y >= (propY * row - 7 + LengthConstants.BACKGROUND_DOWNWARD_OFFSET)
                                 && (y + propY) <= (
                                     propY * (row + 1) + 7 + LengthConstants.BACKGROUND_DOWNWARD_OFFSET
                                 )
                             ) ? elem.nextDirection : elem.direction;
            break;

        case RIGHT:
            elem.direction = (
                                 !board.cells.get(28 * row + col + 1).isWall(board.maze)
                                 && y >= (propY * row - 7 + LengthConstants.BACKGROUND_DOWNWARD_OFFSET)
                                 && (y + propY) <= (
                                     propY * (row + 1) + 7 + LengthConstants.BACKGROUND_DOWNWARD_OFFSET
                                 )
                             ) ? elem.nextDirection : elem.direction;
            break;

        case UP:
            elem.direction = (
                                 !board.cells.get(28 * row + col - 28).isWall(board.maze)
                                 && x >= (propX * col - 7) && (x + propX) <= (propX * (col + 1) + 7)
                             ) ? elem.nextDirection : elem.direction;
            break;

        case DOWN:
            elem.direction = (
                                 !board.cells.get(28 * row + col + 28).isWall(board.maze)
                                 && (
                                     Ghost.class.isInstance(elem)
                                     || !board.cells.get(28 * row + col + 28).isGhostHouse(board.maze)
                                 )
                                 && x >= (propX * col - 7) && (x + propX) <= (propX * (col + 1) + 7)
                             ) ? elem.nextDirection : elem.direction;
            break;

        default:
            break;
        }

    }

    /**
     * Moves the ghost to its next position.
     *
     * @param ghost
     * @param pacMan
     * @param board
     */
    public void ghostMove(Ghost ghost, PacMan pacMan, Board board) {

        int grow = ghost.getRow();
        int gcol = ghost.getColumn();

        switch (ghost.mode) {

        case NORMAL:
            if (ghost.type == GhostType.BLINKY || ghost.type == GhostType.PINKY)
                ghost.path = this.getPath(ghost, board, pacMan);

            else if (ghost.path.size() < 3)
                ghost.path.addAll(this.getRandomPath(ghost, board));
            break;

        case VULNERABLE:
            if (ghost.path.size() < 3)
                ghost.path.addAll(this.getRandomPath(ghost, board));
            break;

        case DIED:
            ghost.path = this.backToGhostHouse(ghost, board);
            break;

        default:
            break;
        }

        if ((grow == ghost.graphPosition.getX() && gcol == ghost.graphPosition.getY())
                || (ghost.graphPosition.getY() == -1 || ghost.graphPosition.getY() == 28)) {

            ghost.graphPosition = ghost.path.get(0);
            ghost.path.remove(0);
        }
        if (gcol > ghost.graphPosition.getY() && !board.cells.get(28 * grow + gcol - 1).isWall(board.maze))
            ghost.direction = KeyCode.LEFT;

        else if (gcol < ghost.graphPosition.getY() && !board.cells.get(28 * grow + gcol + 1).isWall(board.maze))
            ghost.direction = KeyCode.RIGHT;

        else if (grow > ghost.graphPosition.getX() && !board.cells.get(28 * grow + gcol - 28).isWall(board.maze))
            ghost.direction = KeyCode.UP;

        else if (grow < ghost.graphPosition.getX() && !board.cells.get(28 * grow + gcol + 28).isWall(board.maze))
            ghost.direction = KeyCode.DOWN;

        this.move(ghost, board);
    }

    /**
     * Get the shortest path between the ghost and the pacman.
     *
     * @param ghost
     * @param board
     * @param pacMan
     * @return The shortest path found.
     */
    public ArrayList<Point> getPath(Ghost ghost, Board board, PacMan pacMan) {
        return board.mazeGraph.shortestPath(new Point(ghost.getRow(), ghost.getColumn()),
                                            new Point(pacMan.getRow(), pacMan.getColumn()));
    }

    /**
     * Get the path to the ghosts' cage.
     *
     * @param ghost
     * @param board
     * @return
     */
    public ArrayList<Point> backToGhostHouse(Ghost ghost, Board board) {

        ArrayList<Point> path;

        switch (ghost.type) {
        case INKY:
            path = board.mazeGraph.shortestPath(new Point(ghost.getRow()
                                                , ghost.getColumn()), new Point(14, 13));
            break;

        case CLYDE:
            path = board.mazeGraph.shortestPath(new Point(ghost.getRow()
                                                , ghost.getColumn()), new Point(14, 14));
            break;

        case PINKY:
            path = board.mazeGraph.shortestPath(new Point(ghost.getRow()
                                                , ghost.getColumn()), new Point(13, 13));
            break;

        default:
            path = board.mazeGraph.shortestPath(new Point(ghost.getRow()
                                                , ghost.getColumn()), new Point(13, 14));
            break;
        }
        return path;
    }

    /**
     * Get a random path for the ghosts.
     *
     * @param ghost
     * @param board
     * @return Some random path.
     */
    public ArrayList<Point> getRandomPath(Ghost ghost, Board board) {

        Random generator = new Random();
        int row, col;
        int grow, gcol;
        int prow, pcol;

        grow = ghost.getRow();
        gcol = ghost.getColumn();
        prow = (ghost.path.isEmpty())
               ? grow
               : ghost.path.get(ghost.path.size() - 1).getX();
        pcol = (ghost.path.isEmpty())
               ? gcol
               : ghost.path.get(ghost.path.size() - 1).getY();

        do {
            row = generator.nextInt(30);
            col = generator.nextInt(27);
        } while (board.cells.get(28 * row + col).isWall(board.maze)
                 || board.cells.get(28 * row + col).isGhostHouse(board.maze));

        return board.mazeGraph.shortestPath(new Point(prow, pcol)
                                            , new Point(row, col));
    }

    /**
     * Verify collision between two elements.
     *
     * @param e1 The first element.
     * @param e2 The second element.
     * @return Boolean.
     */
    public boolean areColliding(Element e1, Element e2) {

        int propX, propY;
        double x1, y1, x2, y2;

        propX = e1.getProportionX() - 10;
        propY = e1.getProportionY() - 10;

        x1 = e1.getX() + 10;
        y1 = e1.getY() + 10;
        x2 = e2.getX() + 10;
        y2 = e2.getY() + 10;

        return (new Rectangle2D(x1, y1, propX, propY).intersects(
                    x2, y2, propX, propY));
    }

    /**
     * Get the remaining lives.
     *
     * @return The number of remaining lives.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Reset the number of lives to three.
     */
    public void resetLives() {
        lives = 3;
    }

    /**
     * Returns a string version of the score.
     *
     * @return A string version of the score.
     */
    public String getScore() {
        String scoreStr = "000000" + score;
        scoreStr = scoreStr.substring(scoreStr.length() - 6, scoreStr.length());
        return scoreStr;
    }

    /**
     * Reset the score to zero.
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * @return The current level of the game.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set a new level for the game.
     *
     * @param level New level.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Set the speed factor according to the level.
     */
    public void setSpeedFactor() {
        speedFactor = 1 + level * 0.02;
    }

    /**
     * Get the current speed factor.
     *
     * @return The speed factor.
     */
    public double getSpeedFactor() {
        return speedFactor;
    }

    /**
     * Update the level of the game.
     * @param board
     * @param pacMan
     * @param ghosts
     * @param foods
     * @param gameStatus
     */
    public void levelUp(Board board, PacMan pacMan,
                        ArrayList<Ghost> ghosts, ArrayList<Element> foods,
                        Status gameStatus) {
        // Update the level
        this.setLevel(this.getLevel() + 1);
        // Set one speed factor according to the level

        speedFactor = 1 + this.getLevel() * 0.02;

        // Turn back the status of the game to ready
        gameStatus = Status.READY;
        // Reset all the elements
        Utils.resetAll(board, pacMan, ghosts, foods);
        // Increases the ghosts speed
        for (Ghost g : ghosts) {
            if (g.type.equals(GhostType.PINKY))
                g.setSpeed(speedFactor * Speed.GHOST_NORMAL_SPEED - 0.008);
            else
                g.setSpeed(speedFactor * Speed.GHOST_NORMAL_SPEED);
        }

        // Reset the number of pacDots eaten
        pacMan.eatenPacDots = 0;
    }

}
