package engine;

import enums.Color;
import java.util.HashSet;
import java.util.Set;



/**
 * Class for representing vertex of graphs.
 * 
 * @author Maxsuel F. de Almeida
 */
public class Vertex {
    
    public Point label;
    public Color color;
    public int distance;
    public Vertex predecessor;
    public Set<Vertex> neighbours;
        
        
    public Vertex(Point label) {
        this.label = label;
        color = Color.WHITE;
        distance = Integer.MAX_VALUE;
        predecessor = null;
        neighbours = new HashSet<>();
    }
}
