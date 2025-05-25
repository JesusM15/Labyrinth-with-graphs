import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Node {
    private int id;
    private List<Node> adjacencyList; // ejemplo, Nodo1 -> [Nodo2, Nodo3]

    public Node(int id) {
        this.id = id;
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

}
