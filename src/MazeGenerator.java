 import java.awt.*;
 import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Random;
 import java.util.Stack;

 public class MazeGenerator {
    private Labyrinth labyrinth;
    public MazeGenerator() {

    }
    public Labyrinth getLabyrinth() {
        return labyrinth;
    }
    public void setLabyrinth(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }
    public Labyrinth generateMazeFromCSV(String file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = bufferedReader.readLine();
        Labyrinth labyrinth = new Labyrinth();

        int contadorActual = 0;
        while(line != null){
            System.out.println("Leyendo línea: " + line);

            if(!line.trim().isEmpty() && !line.contains(":")){
                String[] dimensions = line.split(",");
                labyrinth.setRows(Integer.parseInt(dimensions[0].trim()));
                labyrinth.setCols(Integer.parseInt(dimensions[1].trim()));

            }else {
                String[] data = line.split(":");

                int nodeID = Integer.parseInt(data[0].trim());
                System.out.println("Procesando nodo: " + nodeID);
                Node node = labyrinth.getOrCreateNode(Integer.parseInt(data[0]));

                int x = node.getId() % labyrinth.getCols();
                int y = node.getId() / labyrinth.getCols();
                node.setX(x);
                node.setY(y);

                if (data.length > 1 && !data[1].trim().isEmpty()) {

                    String[] adjacends = data[1].split(",");

                     for(String adjacend : adjacends){
                         int adjId = Integer.parseInt(adjacend.trim());

                         if(adjId > labyrinth.getCols()*labyrinth.getRows()-1){
                             return null;
                         }
                         labyrinth.addEdge(node.getId(), adjId);
                     }

                }


            }
            line = bufferedReader.readLine();
            ++contadorActual;
        }

        return labyrinth;
    }

    public Labyrinth generateMazeFromGivenNodes(int rows, int columns, String[] adjancencyLists) {
        Labyrinth labyrinth = new Labyrinth();
        int nodeAmount = rows * columns;
        System.out.println("FILAS: " + rows + " COLUMNAS: " + columns + " DESDE MAZEGENERATOR");
        labyrinth.setRows(rows);
        labyrinth.setCols(columns);

        if (adjancencyLists.length == nodeAmount) {
            for (int i = 0; i < adjancencyLists.length; i++) {
                int nodeID = i;
                Node node = labyrinth.getOrCreateNode(nodeID);

                int x = node.getId() % labyrinth.getCols();
                int y = node.getId() / labyrinth.getCols();
                node.setX(x);
                node.setY(y);

                if (!adjancencyLists[i].trim().isEmpty()) {
                    String[] adjancencyListOfCurrentNode = adjancencyLists[i].split(",");
                    for (String adjacend : adjancencyListOfCurrentNode) {
                        int adjId = Integer.parseInt(adjacend.trim());

                        if(adjId > labyrinth.getCols()*labyrinth.getRows()-1){
                            return null;
                        }

                        if (!verifyConexions(node, labyrinth.getRows(), labyrinth.getCols(), adjId)) {
                            return null;
                        }

                        labyrinth.addEdge(node.getId(), adjId);
                    }
                }
            }
        } else {
            return null;
        }

        return labyrinth;
    }

     private boolean verifyConexions(Node node, int rows, int cols, int ID) {
         List<Node> adjacentList = node.getAdjacencyList();
         int nodeID = node.getId();

         //Primer caso, desbordamiento de fila
         if (ID < 0 || (ID > rows * cols - 1)) {
             return false;

         }

         //Caso columna derecha
         if ((ID+1) % cols == 1) {
             if ((nodeID - 1) == ID || (nodeID - cols) == ID || (nodeID + cols) == ID) {
                 return true;
             }
             //Caso columna izquierda
         } else if ((ID+1) % cols == 0) {
             if ((nodeID + 1) == ID || (nodeID - cols) == ID || (nodeID + cols) == ID) {
                 return true;
             }
             //Caso normal
         } else {
             if ((nodeID - 1) == ID || (nodeID + 1) == ID || (nodeID - cols) == ID || (nodeID + cols) == ID) {
                 return true;
             }
         }

         return false;
     }


     private boolean isConneted(List<Node> adjancencyList, int ID) {
         boolean connected = false;

         for (Node neighboor : adjancencyList) {
             if (neighboor.getId() == ID) {
                 connected = true;
                 break;
             }
         }

         return connected;
     }

     public Labyrinth generateMazeRandom(int difficult) {
         Labyrinth labyrinth = new Labyrinth();
         Random rand = new Random();
         int cols = 0, rows = 0;

         switch (difficult) {
             case 0:
                 cols = rand.nextInt(4) + 3; // 3 al 6
                 rows = cols;
                 break;
             case 1:
                 cols = rand.nextInt(7) + 6; // 6 al 12
                 rows = cols;
                 break;
             case 2:
                 cols = rand.nextInt(9) + 10; // 10 al 18
                 rows = cols;
                 break;
             default:
                 cols = rand.nextInt(7) + 6;
                 rows = cols;
         }

         labyrinth.setCols(cols);
         labyrinth.setRows(rows);
         int totalNodes = rows * cols;

         for (int i = 0; i < totalNodes; i++) {
             Node node = labyrinth.getOrCreateNode(i);
             int x = i % cols;
             int y = i / cols;
             node.setX(x);
             node.setY(y);
         }

        // hasta esta parte del metodo nos vimos atorados, por lo que recurrimos a pedirle idea a chatgpt
         // basicamente nos dio la idea y con eso ya supimos como rhacer que se generara siempre un camino
         // esto se hace por medio del algoritmo de busqueda en profundidad por eso se utiliza una pila
         // simplemente hace un camino del nodo 0 al nodo final realizando conexiones entre los vecinos de forma aleatoria.
         boolean[] visitado = new boolean[totalNodes];
         Stack<Integer> pila = new Stack<>();
         pila.push(0);
         visitado[0] = true; // el nodo de inicio se marca como visitado

         while (!pila.isEmpty()) {
             int actual = pila.peek(); // se observa el nodo eb la cima de la pila
             Node nodoActual = labyrinth.getNode(actual);

             List<Integer> vecinos = new ArrayList<>();

             int x = nodoActual.getX();
             int y = nodoActual.getY();

             // aqui se busca si algun nodo que no se ha sido visitado e svecino y se agrega a los vecuinos del nodo
             if (x > 0 && !visitado[actual - 1]) vecinos.add(actual - 1);
             if (x < cols - 1 && !visitado[actual + 1]) vecinos.add(actual + 1);
             if (y > 0 && !visitado[actual - cols]) vecinos.add(actual - cols);
             if (y < rows - 1 && !visitado[actual + cols]) vecinos.add(actual + cols);

             // si hay vecinos entonces se elige uno aleatorio de los nodos vecinos
             // para poder conectarlo al nodo actual
             if (!vecinos.isEmpty()) {
                 int elegido = vecinos.get(rand.nextInt(vecinos.size()));
                 labyrinth.addEdge(actual, elegido);
                 visitado[elegido] = true;
                 pila.push(elegido); // se agrega a la pila y como visitado para indicar que ahora continuaremos el camino desde este nodo
             } else {
                 pila.pop();
             }
         }

         // se agregan conexioness al hacer entre nodos para hacer mas realista el laberinto.
         for (int i = 0; i < totalNodes; i++) {
             Node node = labyrinth.getNode(i);
             int x = node.getX();
             int y = node.getY();

             if (x < cols - 1 && rand.nextInt(5) == 0) {
                 labyrinth.addEdge(i, i + 1);
             }

             if (y < rows - 1 && rand.nextInt(5) == 0) {
                 labyrinth.addEdge(i, i + cols);
             }
         }

         return labyrinth;
     }
 }
