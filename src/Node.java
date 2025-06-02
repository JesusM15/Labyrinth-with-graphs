import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Node {
    private int id;
    private List<Node> adjacencyList; // ejemplo, Nodo1 -> [Nodo2, Nodo3]
    private int x, y;

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
}
