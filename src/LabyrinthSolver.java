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

        while (!openSet.isEmpty()) {
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

  public List<Node> bfs(Labyrinth labyrinth) {
        Queue<Node> cola = new LinkedList<>();
        List<Node> solution = new ArrayList<>();
        Set<Node> visitados = new HashSet<>();
        Node nodoInicial = labyrinth.getNode(0);
        Node nodoFinal = labyrinth.getNode(labyrinth.getCols() * labyrinth.getRows() - 1);
        Map<Node, Node> predecesor = new HashMap<>();

        cola.add(nodoInicial);
        visitados.add(nodoInicial);

        boolean encontrado = false;
        //recorremos el grafo hasta que todos los nodos sean visitados
        while (!cola.isEmpty()) {
            //sacamos da la cola los nodos desde donde recorreremos el grafo
            Node actual = cola.poll();

            if (actual.equals(nodoFinal)) {
                encontrado = true;
                break;
            }
            //verificaremos el nodo actual y sus adyacentes para si no fue visitado lo anadiremos a la cola
            //y lo agraremos a la lista de visitado
            for (Node vecino : actual.getAdjacencyList()) {
                if (!visitados.contains(vecino)) {
                    cola.add(vecino);
                    visitados.add(vecino);
                    // el predecesor lo ocupamos para poder reconstruir el camino despues
                    predecesor.put(vecino, actual);
                }
            }
        }
        if (encontrado) {
            List<Node> ruta = new ArrayList<>();
            Node paso = nodoFinal;

            while (paso != null) {
                ruta.add(paso);
                paso = predecesor.get(paso);
            }

            System.out.println("Ruta más corta:");
            for (Node n : ruta) {
                solution.add(n);
                System.out.print(n.getId() + " ");
            }
            System.out.println();
            return solution;
        } else {
            System.out.println("No se encontró una ruta al nodo final.");
        }
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
