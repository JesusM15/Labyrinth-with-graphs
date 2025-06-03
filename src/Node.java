import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Node implements Comparable<Node> {
    private int id;
    private List<Node> adjacencyList; // ejemplo, Nodo1 -> [Nodo2, Nodo3]
    private int x, y;
    int heuristic;
    int g = Integer.MAX_VALUE;

    public Node(int id) {
        this.id = id;
        adjacencyList = new LinkedList<>();
        this.x = -1;
        this.y = -1;
    }

    public Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.adjacencyList = new LinkedList<>();
    }

    public Node(int id, List<Node> adjacencyList) {
        this.id = id;
        this.adjacencyList = adjacencyList;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setH(int h) {
        this.heuristic = h;
    }

    public int getF() {
        return g + heuristic;
    }

    public void addAdjacent(Node node){
        if(!this.adjacencyList.contains(node)){
            this.adjacencyList.add(node);
        }
    }

    public int getId() {
        return id;
    }

    public List<Node> getAdjacencyList() {
        return adjacencyList;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int compareTo(Node node) {
        return this.getF() - node.getF();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        return sb.toString();
    }
}
