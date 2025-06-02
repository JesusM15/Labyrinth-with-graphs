import java.util.ArrayList;
import java.util.PriorityQueue;

public class LabyrinthSolver {

    public LabyrinthSolver() {

    }

    public int getHeuristicDistance(Node startNode, Node lastNode) {
        return Math.abs(startNode.getX() - lastNode.getX()) + Math.abs(startNode.getY() - lastNode.getY());
    }

    public ArrayList<Node> AstarAlgorithm(Labyrinth labyrinth) {
        PriorityQueue<Node> nodes = new PriorityQueue<>();

        Node startNode = labyrinth.getNode(0);
        Node goalNode = labyrinth.getNode(labyrinth.getCols() * labyrinth.getRows() - 1);

        int manhattanDistance = getHeuristicDistance(startNode, goalNode);
        System.out.println("Manhattan distance: " + manhattanDistance);

        return null;
    }
}
