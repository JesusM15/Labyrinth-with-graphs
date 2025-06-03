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

        HashMap<Node, Node> origin = new HashMap<>(); // guarda el nodo anterior desde el que se llega a cada nodo

        Node startNode = labyrinth.getNode(0);
        Node goalNode = labyrinth.getNode(labyrinth.getCols() * labyrinth.getRows() - 1);

        int manhattanDistance = getHeuristicDistance(startNode, goalNode);
        startNode.setH(manhattanDistance);
        startNode.setG(0);

        openSet.add(startNode);

        while(!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            if(currentNode == goalNode){
                return buildPath(origin, goalNode);
            }

            visitedNodes.add(currentNode);

            for(Node neighbor : currentNode.getAdjacencyList()){

                int tempG = currentNode.getG() + 1;

                if(tempG < neighbor.getG() || !visitedNodes.contains(neighbor)){
                    neighbor.setG(tempG);

                    manhattanDistance = getHeuristicDistance(neighbor, goalNode);
                    neighbor.setH(manhattanDistance);

                    origin.put(neighbor, currentNode);
                    visitedNodes.add(neighbor);

                    if(!openSet.contains(neighbor)){
                        openSet.add(neighbor);
                    }else {
                        openSet.remove(neighbor);
                        openSet.add(neighbor);
                    }
                }

            }
        }


        System.out.println("Manhattan distance: " + manhattanDistance);

        return null;
    }

    private ArrayList<Node> buildPath(HashMap<Node, Node> origins, Node goal){
        ArrayList<Node> path = new ArrayList<>();
        Node currentNode = goal;

        path.add(currentNode);
// goal -> [2, 4, 5, 7]
        // current -> 2
        // origins: { 2:4, 4:5, 5:7, 7:null }
        // #1 -> current -> 4
        while(origins.containsKey(currentNode)){
            currentNode = origins.get(currentNode);
            if(currentNode != null){
                path.add(currentNode);
            }
        }

        return path;
    }
}
