 import java.awt.*;
 import java.io.BufferedReader;
import java.io.FileReader;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.util.*;
 import java.util.List;

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

                int nodeID = Integer.parseInt(data[0].replaceAll(" ", "").trim());
                System.out.println("Procesando nodo: " + nodeID);
                Node node = labyrinth.getOrCreateNode(Integer.parseInt(data[0].replaceAll(" ", "").trim()));

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

     public Labyrinth generateMazeRandom(int difficult) throws IOException {
         Labyrinth labyrinth = new Labyrinth();
         Random rand = new Random();
         int cols = 0, rows = 0;

         switch (difficult) {
             case 0:
                 cols = rand.nextInt(4) + 3;
                 rows = cols;
                 break;
             case 1:
                 cols = rand.nextInt(7) + 6;
                 rows = cols;
                 break;
             case 2:
                 cols = rand.nextInt(9) + 10;
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

         boolean[] visitado = new boolean[totalNodes];
         Stack<Integer> pila = new Stack<>();
         pila.push(0);
         visitado[0] = true;

         while (!pila.isEmpty()) {
             int actual = pila.peek();
             Node nodoActual = labyrinth.getNode(actual);

             List<Integer> vecinos = new ArrayList<>();

             int x = nodoActual.getX();
             int y = nodoActual.getY();

             if (x > 0 && !visitado[actual - 1]) vecinos.add(actual - 1);
             if (x < cols - 1 && !visitado[actual + 1]) vecinos.add(actual + 1);
             if (y > 0 && !visitado[actual - cols]) vecinos.add(actual - cols);
             if (y < rows - 1 && !visitado[actual + cols]) vecinos.add(actual + cols);

             if (!vecinos.isEmpty()) {
                 int elegido = vecinos.get(rand.nextInt(vecinos.size()));
                 labyrinth.addEdge(actual, elegido);
                 visitado[elegido] = true;
                 pila.push(elegido);
             } else {
                 pila.pop();
             }
         }

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

         FileWriter fileWriter = new FileWriter("aleatorio");
         fileWriter.write(rows + "," + cols + "\n");
         //File writer
         for (int i = 0; i < totalNodes; i++) {
             Node node = labyrinth.getNode(i);
             StringBuilder builder = new StringBuilder();
             builder.append(node.getId() + ":");
             for (Node neighbor : node.getAdjacencyList()) {
                 builder.append(neighbor + ",");
             }
             String nodeLine = builder.toString();
             nodeLine = nodeLine.substring(0, nodeLine.length() - 1);
             nodeLine += "\n";


             fileWriter.write(nodeLine);

         }
         fileWriter.close();

         return labyrinth;
     }
 }
