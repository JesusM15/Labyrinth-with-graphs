import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MazeMenu {
    private GraphinLabyrinth gl;

    LabyrinthSolver solver = new LabyrinthSolver();
    private JFrame frame;
    private JPanel panelMaze, panelControl;
    private JComboBox comboBoxAlgoritmos;
    private JButton buttonCrear, buttonGenerar;
    private JTextField textAreaArchivo;

    public MazeMenu() {
        frame = new JFrame("Laberintos");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        panelMaze = new JPanel(new BorderLayout());
        panelMaze.setBorder(BorderFactory.createLineBorder(Color.darkGray, 5));
        panelMaze.setPreferredSize(new Dimension(1000,900));

        buttonCrear = new JButton("Crear");
        buttonCrear.setFocusPainted(false);
        buttonCrear.setBounds(230,115,150,25);
        buttonCrear.addActionListener(actionPerformed -> buttonCrear());

        buttonGenerar = new JButton("Crear");
        buttonGenerar.setFocusPainted(false);
        buttonGenerar.setBounds(140,180,150,25);
        buttonGenerar.addActionListener(action -> buttonGenerar());

        JLabel labelArchivo = new JLabel("Introduce el archivo");
        labelArchivo.setFont(new Font("Arial", Font.BOLD,15));
        labelArchivo.setBounds(30,20,160,40);

        textAreaArchivo = new JTextField();
        textAreaArchivo.setBounds(183,28,200,25);
        textAreaArchivo.addActionListener(action -> {
            try {
                obtenerArchivo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        JLabel labelAlgoritmos = new JLabel("Selecciona el algoritmo");
        labelAlgoritmos.setFont(new Font("Arial", Font.BOLD,14));
        labelAlgoritmos.setBounds(30,80,180,40);

        String[] algoritmos = {"Dijkstra", "BFS", "A*"};
        comboBoxAlgoritmos = new JComboBox(algoritmos);
        comboBoxAlgoritmos.setBounds(30,115,170,25);
        comboBoxAlgoritmos.addActionListener(action -> algoritmoEscogido());

        JSeparator linea = new JSeparator(SwingConstants.HORIZONTAL);
        linea.setBackground(Color.gray);
        linea.setBounds(31,70,350,30);

        JLabel labelCrearLaberinto = new JLabel("Crear laberinto");
        labelCrearLaberinto.setFont(new Font("Arial", Font.BOLD,14));
        labelCrearLaberinto.setBounds(230,80,180,40);

        JLabel labelGenerarLaberinto = new JLabel("Generar laberinto");
        labelGenerarLaberinto.setFont(new Font("Arial", Font.BOLD,14));
        labelGenerarLaberinto.setBounds(150,145,180,40);

        panelControl = new JPanel();
        panelControl.setBorder(BorderFactory.createLineBorder(Color.darkGray, 5));
        panelControl.setPreferredSize(new Dimension(400,900));
        panelControl.setLayout(null);
        panelControl.add(labelArchivo);
        panelControl.add(textAreaArchivo);
        panelControl.add(linea);
        panelControl.add(comboBoxAlgoritmos);
        panelControl.add(labelAlgoritmos);
        panelControl.add(labelCrearLaberinto);
        panelControl.add(buttonCrear);
        panelControl.add(labelGenerarLaberinto);
        panelControl.add(buttonGenerar);




        frame.add(panelMaze, BorderLayout.WEST);
        frame.add(panelControl, BorderLayout.EAST);

        frame.setSize(new Dimension(1400,900));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //La clase GraphinLabyrinth ya tiene los metodos getters y setters del laberitno para que puedan resolverlo mediante
    //el algoritmo.
    //Ademas para representar ese arbol o grafo(no se que salida genera el algoritmo) del camino para resolver el laberinto
    //se puede hacer directamente aqui utilizando el parametro g de GraphinLabyrinth, o hacer un metodo en GraphinLabyrinth que nomas tome la salida
    //del algoritmo y ahi mismo pinte el camino.
    /**
     * Metodo que resolvera el algoritmo en cuestion
     */
    private void algoritmoEscogido()
    {
        String algoritmo = (String) comboBoxAlgoritmos.getSelectedItem();

        switch (algoritmo) {
            case "Dijkstra":
                break;
            case "A*":
                ArrayList<Node> path = solver.AstarAlgorithm(gl.getLabyrinth());

                for(Node node : path){
                    System.out.print(node + ",");
                }
                gl.paintWithSolution(path);
                System.out.println(" ");
                break;
            case "BFS":
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Metodo para crear laberintos
     */
    private void buttonCrear() {
        //Tenia pensado hacer que salga una ventana emergente donde le usuario hace las listas de adyacencia
    }

    private void buttonGenerar() {
        //Tenia pensado hacer que salga una ventana emergente donde el usuario elige dificultad y se genera o algo asi,
        //aun no se
    }

    private void obtenerArchivo() throws IOException
    {
        String ruta = textAreaArchivo.getText();
        panelMaze.removeAll();
        panelMaze.setLayout(new BorderLayout());

        try {
            MazeGenerator maze = new MazeGenerator();
            Labyrinth labyrinth = maze.generateMazeFromCSV(ruta);
            gl = new GraphinLabyrinth(labyrinth);
            panelMaze.add(gl, BorderLayout.CENTER);

            panelMaze.repaint();
            panelMaze.revalidate();
        } catch (FileNotFoundException e) {

            JOptionPane.showMessageDialog(null, "Archivo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }


}
