package engine;

import enums.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is for a graph data structure and comprises the breadth first
 * search method that will be used in the search of the pacman for the ghosts.
 * 
 * @author Maxsuel F. de Almeida
 */
public class Graph {
    
    /*Vertices of the graph*/
    public Set<Vertex> V;
    
    /**
     * The class constructor.
     */
    public Graph() {
        V = new HashSet<>();
    }
    
    /**
     * Given a point, add it as a vertex of the graph.
     * @param p The point to be added.
     */
    public void addVertex(Point p) {
        Vertex v = new Vertex(p);
        V.add(v);
    }
    
    /**
     * Given two points, add they into the graph and creat an edge between them.
     * @param p The first point.
     * @param q The second point.
     */
    public void addEdge(Point p, Point q) {
        
        Vertex u = null, v = null;
        
        for(Vertex w : V) {
            if(w.label.getX() == p.getX() && w.label.getY() == p.getY()) u = w;
            if(w.label.getX() == q.getX() && w.label.getY() == q.getY()) v = w;
        }
        
        if(u == null) {
            u = new Vertex(p);
            V.add(u);
        }
        
        if(v == null) {
            v = new Vertex(q);
            V.add(v);
        }
        
        u.neighbours.add(v);
        v.neighbours.add(u);
        
    }
    
    /**
     * Breadth First Search method for traversing the graph.
     * 
     * @param start the initial node, i.e, the node where the
     * traversing starts.
     * 
     * @throws Exception 
     */
    public void breadthFirstSearch(Point start) throws Exception {
        
        ArrayList<Vertex> Q = new ArrayList<>();
        Vertex s = null, u;
        
        for(Vertex v : V) 
            if(v.label.getX() == start.getX() && v.label.getY() == start.getY()) s = v;
        
        if(s == null) throw new Exception("There is no such node in the graph");
        
        s.color = Color.GRAY;
        s.distance = 0;
        Q.add(s);
        
        while(!Q.isEmpty()) {
            u = Q.get(0);
            Q.remove(u);
            
            for(Vertex v: u.neighbours) {
                if(v.color.equals(Color.WHITE)) {
                    v.color = Color.GRAY;
                    v.distance = u.distance + 1;
                    v.predecessor = u;
                    Q.add(v);
                }
            }
            
            u.color = Color.BLACK;
        }
        
    }
    
    /**
     * This method uses the BFS algorithm to find the sortest path between two 
     * nodes in the graph.
     * 
     * Such method will be used to search for the pacman.
     * 
     * @param start Initial node.
     * @param end   Final node.
     * @return  ArrayList comprising the shortest path.
     */
    public ArrayList<Point> shortestPath(Point start, Point end) {
        try {
            this.restartBFS();
            breadthFirstSearch(start);
               
            Vertex u;
            Vertex pre = null;
            ArrayList<Point> shortestPathList = new ArrayList<>();
            
            for(Vertex v : V) {
                if(v.label.getX() == end.getX() && v.label.getY() == end.getY()) 
                {
                    u = v;
                    pre = u.predecessor;
                }
            }
            shortestPathList.add(end);
            
            while(pre != null && pre.predecessor != null) {
                shortestPathList.add(0, pre.label);
                pre = pre.predecessor;
            }
            
            return shortestPathList;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Restart the BFS algorithm.
     */
    public void restartBFS() {
        for(Vertex v : V) {
            v.color = Color.WHITE;
            v.distance = 0;
            v.predecessor = null;
        }
    }
    
}
