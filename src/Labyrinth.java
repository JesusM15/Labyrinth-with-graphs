import java.util.*;


public class Labyrinth {
    private HashMap<Integer, Node> labyrinth; // grafo
    private int rows, cols;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public Labyrinth() {
        this.labyrinth = new HashMap<Integer, Node>();
    }

    public Node getOrCreateNode(int nodeId){
        Node node = labyrinth.get(nodeId);
        if(node == null){
            node = new Node(nodeId);
            labyrinth.put(nodeId, node);
        }
        return node;
    }

    public void addEdge(int nodeId1, int nodeId2){
        Node node1 = getOrCreateNode(nodeId1);
        Node node2 = getOrCreateNode(nodeId2);

        node1.addAdjacent(node2);
        node2.addAdjacent(node1);
    }

    public boolean containsNode(int nodeId){
        return labyrinth.containsKey(nodeId);
    }

    public Node getNode(int nodeId) {
        return labyrinth.get(nodeId);
    }

    public void printGraph() {
        for (Map.Entry<Integer, Node> entry : labyrinth.entrySet()) {
            System.out.print("Node " + entry.getKey() + " -> ");
            for (Node neighbor : entry.getValue().getAdjacencyList()) {
                System.out.print(neighbor.getId() + " ");
            }
            System.out.println(" X: " + entry.getValue().getX() + " Y: " + entry.getValue().getY());
            System.out.println();
        }
    }

}
