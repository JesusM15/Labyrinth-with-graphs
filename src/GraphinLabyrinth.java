
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Clase que modela el laberinto pero ya grafico
public class GraphinLabyrinth extends JPanel  {

    //Atributos para la construccion del laberinto grafico, desde el laberinto y
    // el tamanio de cada celda
    private Labyrinth labyrinth;
    private final int SIZE = 54;

    /**
     * Constructor con laberinto ya definido
     * @param labyrinth
     */
    public GraphinLabyrinth(Labyrinth labyrinth) {

        //Para representar realmente el laberinto pero grafico, es necsario asignar el tamanio de
        //cada celda de acuerdo a las filas y columnas del mismo grafo, por eso mismo se multiplica
        //cada fila, columna por el tamanio de la celda
        this.labyrinth = labyrinth;
        Dimension dimensions = new Dimension(labyrinth.getCols() * SIZE, labyrinth.getRows() * SIZE);
        this.setPreferredSize(dimensions);
    }

    /**
     * Constructor con solamente la ruta como parametro para que se cree el laberinto
     * @param ruta
     * @throws IOException
     */
    public GraphinLabyrinth(String ruta) throws IOException {
        MazeGenerator maze = new MazeGenerator();
        labyrinth = maze.generateMazeFromCSV(ruta);
        Dimension dimensions = new Dimension(labyrinth.getCols() * SIZE, labyrinth.getRows() * SIZE);
        this.setPreferredSize(dimensions);
    }


    //NOTA: La idea de representar un laberinto graficamente se tomo de ChatGPT, que arrojo como
    //mejor opcion hacerlo mediante paintComponent
    /**
     * Metodo sobreescrito que dibuja las celdas del laberinto
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int rows = labyrinth.getRows();
        int columns = labyrinth.getCols();
        int bordersSize = 6;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(bordersSize));
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, columns * SIZE, rows * SIZE);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int nodeID = i * columns + j;
                Node node = labyrinth.getNode(nodeID);

                if (node != null) {
                    int x = j * SIZE;
                    int y = i * SIZE;

                    verifyConexions(node, rows, columns, g2, x, y);

                    if (nodeID == 0) {
                        g2.setColor(Color.GREEN);
                        g2.drawString("S", x + SIZE / 3, y + SIZE / 2);
                    } else if (nodeID == rows * columns - 1) {
                        g2.setColor(Color.RED);
                        g2.drawString("E", x + SIZE / 3, y + SIZE / 2);
                    }

                }
            }
        }
    }

    public void paintWithSolution(ArrayList<Node> pathSolution, Color color) {
        Graphics g = this.getGraphics();

        int rows = labyrinth.getRows();
        int columns = labyrinth.getCols();
        int bordersSize = 6;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(bordersSize));
//        g2.setColor(Color.WHITE);
//        g2.fillRect(0, 0, columns * SIZE, rows * SIZE);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int nodeID = i * columns + j;
                Node node = labyrinth.getNode(nodeID);

                if (node != null && isOnArrayList(nodeID, pathSolution)) {
                    int x = j * SIZE;
                    int y = i * SIZE;
                    g2.setColor(color);
                    g2.fillRect(x, y, SIZE, SIZE);

                    g2.setColor(Color.BLACK);
                    verifyConexions(node, rows, columns, g2, x, y);

                    if (nodeID == 0) {
                        g2.setColor(Color.GREEN);
                        g2.drawString("S", x + SIZE / 3, y + SIZE / 2);
                    } else if (nodeID == rows * columns - 1) {
                        g2.setColor(Color.RED);
                        g2.drawString("E", x + SIZE / 3, y + SIZE / 2);
                    }
                }
            }
        }
    }

    private boolean isOnArrayList(int id, ArrayList<Node> pathSolution) {
        for(Node node : pathSolution) {
            if(id == node.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que verifica si un nodo esta conectado con otro, si lo esta se dibuja una linea blanca, es decir un
     * camino libre
     * @param node
     */
    private void verifyConexions(Node node, int rows, int cols, Graphics2D g2, int x, int y) {
        List<Node> adjacentList = node.getAdjacencyList();
        int nodeID = node.getId();

        g2.setColor(Color.BLACK);

        // arriba
        if (y == 0 || !isConneted(adjacentList, nodeID - cols)) {
            g2.drawLine(x, y, x + SIZE, y);
        }

        // izzquierda
        if (x == 0 || !isConneted(adjacentList, nodeID - 1)) {
            g2.drawLine(x, y, x, y + SIZE);
        }

        // derecha
        if (nodeID % cols == cols - 1 || !isConneted(adjacentList, nodeID + 1)) {
            g2.drawLine(x + SIZE, y, x + SIZE, y + SIZE);
        }

        // abajo
        if (nodeID / cols == rows - 1 || !isConneted(adjacentList, nodeID + cols)) {
            g2.drawLine(x, y + SIZE, x + SIZE, y + SIZE);
        }
    }


    /**
     * Metodo que verifica si un nodo actual esta conectado con un nodo vecino
     * @param adjancencyList
     * @param ID
     * @return
     */
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

    private boolean isLastRow(int nodeId){
        int row = nodeId / labyrinth.getCols();
        System.out.println("row:" + row + "rows:" + labyrinth.getRows());
        return row == labyrinth.getRows()-1;
    }

    private boolean isLastCol(int nodeId){
        int col = nodeId % labyrinth.getCols();

        return col == labyrinth.getCols()-1;
    }

    public Labyrinth getLabyrinth()
    {
        return labyrinth;
    }

    public void setLabyrinth(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }
}
