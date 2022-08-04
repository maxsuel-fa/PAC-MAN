
package engine;

/**
 * This class represents an ordered pair of the double type.
 * 
 * @author Maxsuel F. de Almeida
 */
public class Point {
   
    /**
    * The x and y attributes are, respectively the horizontal and the vertical
    * coordinate of the point.
    */
   private int x;
   private int y;
   
   /**
    * The default contructor.
    * 
    * @param x Horizontal coordinate
    * @param y Vertical coordinate
    */
   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }
   
    /**
     * Set the horizontal coordinate.
     * 
     * @param x
     */
   public void setX(int x) {
       this.x = x;
   }
   
    /**
     * Set the vertical coordinate.
     * @param y
     */
   public void setY(int y) {
       this.y = y;
   }
   
    /**
     * Return the horizontal coordinate.
     * @return x
     */
   public int getX() {
       return x;
   }
   
    /**
     * Return the vertical coordinate.
     * 
     * @return y
     */
    public int getY() {
       return y;
   }
   
}
