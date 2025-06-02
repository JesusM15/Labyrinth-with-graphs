import java.util.*;

public class LabyrinthSolver {

    public LabyrinthSolver() {

    }

    public int getHeuristicDistance(Node startNode, Node lastNode) {
        int manhattanDistance = Math.abs(startNode.getX() - lastNode.getX()) + Math.abs(startNode.getY() - lastNode.getY());
        return manhattanDistance;
    }

    public ArrayList<Node> AstarAlgorithm(Labyrinth labyrinth) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        HashSet<Node> visitedNodes = new HashSet<>();

        Node startNode = labyrinth.getNode(0);
        Node goalNode = labyrinth.getNode(labyrinth.getCols() * labyrinth.getRows() - 1);

        int manhattanDistance = getHeuristicDistance(startNode, goalNode);
        startNode.setH(manhattanDistance);
        startNode.setG(0);

        openSet.add(startNode);

        while(!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            if(currentNode == goalNode){

            }

            visitedNodes.add(currentNode);

            for(Node neighbor : currentNode.getAdjacencyList()){
                if(visitedNodes.contains(neighbor)) continue;

                // la distancia tentativa de G actual mas 1 por el vecino
                int tentativeG = currentNode.getG() + 1;

                if(tentativeG < neighbor.getG()){

                    neighbor.setG(tentativeG);
                    if(!visitedNodes.contains(neighbor)){
                        visitedNodes.add(neighbor);
                    }
                }
            }
        }


        System.out.println("Manhattan distance: " + manhattanDistance);

        return null;
    }
}
