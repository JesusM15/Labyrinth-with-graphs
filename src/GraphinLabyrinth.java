
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

//Clase que modela el laberinto pero ya grafico
public class GraphinLabyrinth extends JPanel  {

    //Atributos para la construccion del laberinto grafico, desde el laberinto y
    // el tamanio de cada celda
    private Labyrinth labyrinth;
    private final int SIZE = 70;

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

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                int ID = i * columns + j;
                Node node = labyrinth.getNode(ID);

                if (node != null) {
                    int posicionY = i * SIZE;
                    int posicionX = j * SIZE;

                    int bordersSize = 6;
                    g.setColor(Color.BLACK);
//                    g.drawRect(posicionX, posicionY, SIZE, SIZE);
                    g.fillRect(posicionX, posicionY, SIZE + bordersSize, SIZE + bordersSize);

                    g.setColor(Color.WHITE);
                    g.fillRect(posicionX + bordersSize, posicionY + bordersSize, SIZE - bordersSize, SIZE - bordersSize);

                    if(node.getId() == 0){
                        g.setColor(Color.GREEN);
                        g.drawString("S", posicionX + SIZE / 2, posicionY + SIZE /2);
                    }else if(node.getId() == labyrinth.getRows()*labyrinth.getCols()-1){
                        g.setColor(Color.RED);
                        g.drawString("E", posicionX + SIZE / 2, posicionY + SIZE / 2);
                    }

                    verifyConexions(node, rows, columns, g, posicionX, posicionY, i, j, bordersSize);
                }
            }
        }

    }

    /**
     * Metodo que verifica si un nodo esta conectado con otro, si lo esta se dibuja una linea blanca, es decir un
     * camino libre
     * @param node
     */
    private void verifyConexions(Node node, int rows, int cols, Graphics g, int posicionX, int posicionY, int i, int j, int borderSize) {
        List<Node> adjacencyList = node.getAdjacencyList();

        //Para obtener la fila se necesita dividir el ID entre el numero de columnas y obtenemos la
        //parte entera que es igual a la fila actual

        //Para obtener la columna actual se ncesita obtener el modulo/residuo de el ID y el numero de columnas
        //esto nos arroja la columna actual
        int row = node.getId() / cols;
        int col = node.getId() % cols;

        int nodoIDInicio = 0;
        int nodoIDFinal = (rows * cols) - 1;

        g.setColor(Color.white);

        //Obtener la columna y fila nos permite obtener los nodos vecinos(arriba, abajo, izquierda, derecha)
        //Izquerda
        if (col > 0) {
            int left = node.getId() - 1;
            boolean connected = isConneted(adjacencyList, left);
            if (connected) {
                g.fillRect(posicionX, posicionY + borderSize, borderSize, SIZE - borderSize);
//                    g.drawLine(posicionX + borderSize, posicionY, posicionX,   SIZE);
            }
        }

        //Derecha
        if (col < cols - 1) {
            int right = node.getId() + 1;
            boolean connected = isConneted(adjacencyList, right);
            if (connected) {
                g.fillRect(posicionX + SIZE, posicionY,  borderSize, SIZE - borderSize);
//                g.drawLine(posicionX + SIZE, posicionY, posicionX + SIZE, posicionY + SIZE);
            }
        }
        //Abaj0
        if (row < rows - 1) {
            int down = node.getId() + cols;

            boolean connected = isConneted(adjacencyList, down);
            if (connected) {
                g.fillRect(posicionX + borderSize, posicionY + SIZE, SIZE - borderSize, borderSize);
            //                g.drawLine(posicionX, posicionY + SIZE, posicionX + SIZE, posicionY + SIZE);
            }
        }
        //Arriba
        if (row > 0) {
            int up = node.getId() - cols;

            boolean connected = isConneted(adjacencyList, up);
            if (connected) {
                g.fillRect(posicionX + borderSize, posicionY, SIZE - borderSize, borderSize);
//                            g.drawLine(posicionX, posicionY, posicionX + SIZE, posicionY);
            }
        }

        if (nodoIDInicio == node.getId()) {
            g.fillRect(posicionX, posicionY, posicionX, posicionY + SIZE + borderSize);
//            g.drawLine(posicionX, posicionY, posicionX, posicionY + SIZE);
        }

        if (nodoIDFinal == node.getId()) {
            g.fillRect(posicionX + SIZE, posicionY, borderSize,  SIZE + borderSize);
//            g.drawLine(posicionX + SIZE, posicionY, posicionX + SIZE, posicionY + SIZE);
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

    public Labyrinth getLabyrinth()
    {
        return labyrinth;
    }

    public void setLabyrinth(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }
}
