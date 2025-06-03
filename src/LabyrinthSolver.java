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

    /**
     * Metodo que ejecuta el algoritmo de Dijkstra
     * @param labyrinth
     * @return
     */
    public ArrayList<Node> dijkstraAlgorithm(Labyrinth labyrinth) {

        //Guardar en variables, datos recurrentes
        int rows = labyrinth.getRows();
        int columns = labyrinth.getCols();
        int nodeAmount = rows * columns;

        //Variables necesarias para ejecutar el algoritmo
        int[] distances = new int[nodeAmount];
        boolean[] visited = new boolean[nodeAmount];
        ArrayList<Node> solution = new ArrayList<>();
        HashMap<Node, Node> predecessor = new HashMap<>();

        //Si usamos Dijkstra para laberintos no peude ser muy eficiente, ya que el peso de
        //sus aristas siempre seran 1, por lo tanto, para distinguir Dijkstra con BFS, se utilizo
        //la cola de prioridad, que esta realmente es utiliza para determinar la arista con mas peso.
        PriorityQueue<Node> shortestNode = new PriorityQueue<>();

        Node startNode = labyrinth.getNode(0);
        Node endNode = labyrinth.getNode(nodeAmount - 1);

        //Establecer distancias a -1 e inicio cero
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(visited, false);
        distances[0] = 0;

        //Se mete el primer nodo a la cola, en el cual usaremos para avanzar en todas
        //las posibilidades, en este caso el camino mas corto al endNode
        shortestNode.add(startNode);

        //Se verifican los caminos mas cortos del labertino
        while (!shortestNode.isEmpty()) {
            Node currentNode = shortestNode.poll();
            int ID = currentNode.getId();

            if (visited[ID]) {
                continue;
            }
            visited[ID] = true;


            //La gran diferencia de otros algoritmos es que se le suma el peso
            //al arreglo de distacias, como son laberintos, su peso es de 1
            for (Node neighbors : currentNode.getAdjacencyList()) {
                int newDistance = distances[ID] + 1;

                //Se verifica que la nueva distancia es mas carta que la qye ya teniamos
                //guardada, si es asi, se actualiza la distancia mas corta.
                //Por utltimo se guarda ese nodo a la cola de mas cortos y
                if (newDistance < distances[neighbors.getId()]) {
                    distances[neighbors.getId()] = newDistance;
                    shortestNode.add(neighbors);
                    predecessor.put(neighbors, currentNode);
                }
            }
        }



        //Por ultimo con ayuda de buildPath, generamos el camino mediante el uso de ArrayList de nodos, utilizando
        //el predecesor generado con el ultimo nodo
        solution = buildPath(predecessor, endNode);
        return solution;
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
