
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MazeMenu {
    private GraphinLabyrinth gl;
    private int rows, columns;
    double[] times;

    LabyrinthSolver solver = new LabyrinthSolver();
    private JFrame frame;
    private JPanel panelMaze, panelControl;
    private JComboBox comboBoxDificultades;
    private JButton buttonCrear, buttonGenerar, buttonSolve;
    private JTextField textAreaArchivo;
    private JTable table;
    private JLabel labelTiempo;
    private JList selectorAlgoritmos;

    public MazeMenu() {
        times = new double[3];
        for (int i = 0; i < 3; i++) {
            times[i] = 0;
        }
        frame = new JFrame("Laberintos");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane();

        panelMaze = new JPanel(new BorderLayout());
        panelMaze.setBorder(BorderFactory.createLineBorder(Color.darkGray, 5));
        panelMaze.setPreferredSize(new Dimension(1000,900));
        panelMaze.add(scrollPane, BorderLayout.CENTER);

        buttonCrear = new JButton("Crear");
        buttonCrear.setFocusPainted(false);
        buttonCrear.setBounds(230,115,150,25);
        buttonCrear.addActionListener(actionPerformed -> buttonCrear());

        buttonGenerar = new JButton("Generar");
        buttonGenerar.setFocusPainted(false);
        buttonGenerar.setBounds(230,180,150,25);
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

        selectorAlgoritmos = new JList(algoritmos);
        selectorAlgoritmos.setBounds(30, 115, 170, 60);
        selectorAlgoritmos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        buttonSolve = new JButton("Resolver");
        buttonSolve.setBounds(30, 180, 150, 25);
        buttonSolve.setFocusPainted(false);
        buttonSolve.addActionListener(action -> algoritmoEscogido());

//
//        String[] dificultades = {"Facil", "Media", "Dificil"};
//        comboBoxDificultades = new JComboBox(dificultades);
//        comboBoxDificultades.setBounds(30,220, 170,25);
//        comboBoxDificultades.addActionListener(action ->{
//                    dificultadElegida();
//                }
//        );


        JSeparator linea = new JSeparator(SwingConstants.HORIZONTAL);
        linea.setBackground(Color.gray);
        linea.setBounds(31,70,350,30);

        JLabel labelCrearLaberinto = new JLabel("Crear laberinto");
        labelCrearLaberinto.setFont(new Font("Arial", Font.BOLD,14));
        labelCrearLaberinto.setBounds(230,80,180,40);

        JLabel labelGenerarLaberinto = new JLabel("Generar laberinto");
        labelGenerarLaberinto.setFont(new Font("Arial", Font.BOLD,14));
        labelGenerarLaberinto.setBounds(230,145,180,40);

        JSeparator otraLinea = new JSeparator(SwingConstants.HORIZONTAL);
        otraLinea.setBackground(Color.gray);
        otraLinea.setBounds(31,260,350,30);

        JLabel labelEnunciadoTiempos = new JLabel("Tiempo de ejecucion");
        labelEnunciadoTiempos.setFont(new Font("Arial", Font.BOLD,14));
        labelEnunciadoTiempos.setBounds(140,260,180,40);

        labelTiempo = new JLabel();
        labelTiempo.setOpaque(true);
        labelTiempo.setFont(new Font("Arial", Font.BOLD,14));
        labelTiempo.setBackground(Color.WHITE);
        labelTiempo.setBorder(BorderFactory.createLineBorder(Color.darkGray, 5));
        labelTiempo.setBounds(31,300,350,200);
        labelTiempo.setText("<html>Dijkstra: <br>A*: <br>BFS: </html>");

        JLabel labelSimbologia = new JLabel("Simbologia");
        labelSimbologia.setFont(new Font("Arial", Font.BOLD, 14));
        labelSimbologia.setBounds(165, 677, 350, 200);

        JLabel labelColorD = new JLabel("Dijkstra");
        labelColorD.setFont(new Font("Arial", Font.BOLD, 14));
        labelColorD.setBounds(50, 775,100,50);

        JLabel labelColorB = new JLabel("BFS");
        labelColorB.setFont(new Font("Arial", Font.BOLD, 14));
        labelColorB.setBounds(200, 775,100,50);

        JLabel labelColorA = new JLabel("A*");
        labelColorA.setFont(new Font("Arial", Font.BOLD, 14));
        labelColorA.setBounds(340,775,100,50);

        JSeparator lineaSimbologia = new JSeparator(SwingConstants.HORIZONTAL);
        lineaSimbologia.setBackground(Color.darkGray);
        lineaSimbologia.setBounds(31,750,350,30);

        JLabel labelColorFULLB = new JLabel();
        labelColorFULLB.setOpaque(true);
        labelColorFULLB.setBackground(new Color(25, 255, 14, 128));
        labelColorFULLB.setBounds(180, 793,15,15);

        JLabel labelColorFULLD = new JLabel();
        labelColorFULLD.setOpaque(true);
        labelColorFULLD.setBackground(new Color(255, 0, 0, 128));
        labelColorFULLD.setBounds(30, 793,15,15);

        JLabel labelColorFULLA = new JLabel();
        labelColorFULLA.setOpaque(true);
        labelColorFULLA.setBackground(new Color(222, 14, 255, 128));
        labelColorFULLA.setBounds(320,793,15,15);




        panelControl = new JPanel();

        panelControl.add(labelColorFULLA);
        panelControl.add(labelColorFULLD);
        panelControl.add(labelColorFULLB);
        panelControl.add(labelColorD);
        panelControl.add(labelColorB);
        panelControl.add(labelColorA);
        panelControl.add(labelSimbologia);
        panelControl.add(lineaSimbologia);
        panelControl.add(labelTiempo);
        panelControl.add(labelEnunciadoTiempos);
//        panelControl.add(otraLinea);
        panelControl.setBorder(BorderFactory.createLineBorder(Color.darkGray, 5));
        panelControl.setPreferredSize(new Dimension(400,900));
        panelControl.setLayout(null);
        panelControl.add(labelArchivo);
        panelControl.add(textAreaArchivo);
        panelControl.add(linea);

        panelControl.add(selectorAlgoritmos); // aqui lista
        panelControl.add(buttonSolve);
//        panelControl.add(comboBoxDificultades);

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

    private void dificultadElegida() {
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
//        System.out.println("entre a seleccionar algoritmo");
       ArrayList<Node> path = null;
//        String algoritmo = (String) comboBoxAlgoritmos.getSelectedItem();
        List<String> algoritmosSeleccionados = selectorAlgoritmos.getSelectedValuesList();
        gl.paintComponent(gl.getGraphics());
        Color[] colors = { new Color(25, 255, 14, 128), new Color(255, 0, 0, 128),  new Color(222, 14, 255, 128)};
        int index = 0;
        long tiempoInicio = 0;
        long tiempoFinal = 0;
        long diff = 0;
        long[] tiempos = new long[3];
        for(String algoritmo : algoritmosSeleccionados){
//            System.out.println("PRINT: " + index);
            switch (algoritmo) {
                case "BFS":
                    index = 0;
                    tiempoInicio = System.nanoTime();
                    path = (ArrayList<Node>) solver.bfs(gl.getLabyrinth());
                    tiempoFinal = System.nanoTime();
                    diff = tiempoFinal - tiempoInicio;
                    tiempos[index] = diff;
                    System.out.println("BFS " + path.size());
                    break;
                case "Dijkstra":
                    index = 1;
                    tiempoInicio = System.nanoTime();
                    path = solver.dijkstraAlgorithm(gl.getLabyrinth());
                    tiempoFinal = System.nanoTime();
                    diff = tiempoFinal - tiempoInicio;
//                    for(Node node : path){
//                        System.out.print(node + ",");
//                    }
                    System.out.println("Dijkstra " + path.size());
                    tiempos[index] = diff;
                    break;
                case "A*":
                    index = 2;
                    tiempoInicio = System.nanoTime();
                    path = solver.AstarAlgorithm(gl.getLabyrinth());
                    tiempoFinal = System.nanoTime();
                    diff = tiempoFinal - tiempoInicio;
//                    for(Node node : path){
//                        System.out.print(node + ",");
//                    }
//                    System.out.println(" ");
                    System.out.println("A* " + path.size());
                    tiempos[index] = diff;
                    break;
            }

            String[] algoritmos = {"BFS", "Dijkstra", "A*"};
            int bestTime = 0;
            for(int i =0; i<tiempos.length; i++){
                if(tiempos[i] < tiempos[bestTime] && tiempos[i] != 0 || tiempos[bestTime] == 0){
                    bestTime = i;
                }
            }
            labelTiempo.setText("<html><br>Mejor tiempo: " + algoritmos[bestTime] + "<br>BFS: " + tiempos[0] +"ns<br>Dijkstra: "+ tiempos[1] +"ns<br>A*: "+ tiempos[2]+"ns<br></html>");
            gl.paintWithSolution(path, colors[index]);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Metodo para crear laberintos
     */
    private void buttonCrear() {
        JFrame frameCrear = new JFrame("Crear laberinto");
        frameCrear.setLayout(new BorderLayout());
        JPanel panelInfo = new JPanel();
        panelInfo.setPreferredSize(new Dimension(500, 70));
        JPanel panelTabla = new JPanel();
        JButton buttonCrearGrafo = new JButton("Crear");

        buttonCrearGrafo.setFont(new Font("Arial", Font.BOLD, 14));
        buttonCrearGrafo.setBounds(310,18,127,25);
        buttonCrearGrafo.setEnabled(false);
        buttonCrearGrafo.addActionListener(actPerformed -> buttonCrearGrafo());

        panelInfo.setLayout(null);
        panelTabla.setLayout(new BorderLayout());

        JLabel labelNodos = new JLabel("Filas x columnas");
        labelNodos.setFont(new Font("Arial", Font.BOLD, 14));
        labelNodos.setBounds(20,10,160,40);

        JTextField textFieldNodos = new JTextField();
        textFieldNodos.setBounds(170,18,130,25);

        JSeparator linea = new JSeparator(SwingConstants.HORIZONTAL);
        linea.setBackground(Color.gray);
        linea.setBounds(16,50,550,30);


        panelInfo.add(buttonCrearGrafo);
        panelInfo.add(textFieldNodos);
        panelInfo.add(labelNodos);
        panelInfo.add(linea);
        frameCrear.add(panelInfo, BorderLayout.NORTH);
        frameCrear.add(panelTabla, BorderLayout.SOUTH);
        frameCrear.setSize(new Dimension(600, 500));
        frameCrear.setLocationRelativeTo(null);
        frameCrear.setVisible(true);
        frameCrear.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        textFieldNodos.addActionListener(action -> {
            String texto = textFieldNodos.getText();
            int cantidadNodos = 0;

            try {
                String[] datos;

                if (texto.contains("x")) {
                    datos = texto.split("x");
                    rows = Integer.parseInt(datos[0]);
                    columns = Integer.parseInt(datos[1]);
                } else if (texto.contains(",")) {
                    datos = texto.split(",");
                    rows = Integer.parseInt(datos[0]);
                    columns = Integer.parseInt(datos[1]);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, envia un formato valido");
                }


                cantidadNodos = rows*columns;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, ingresa un numero valido");
            }

            String[] columnas = {"Nodo", "Adyacentes"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 1;
                }
            };

            for (int i = 0; i < cantidadNodos; ++i) {
                modelo.addRow(new Object[]{i + ":", ""});
            }

            table = new JTable(modelo);
            table.setRowHeight(25);
            JScrollPane scrollPane = new JScrollPane(table);
            table.getColumnModel().getColumn(0).setPreferredWidth(15);
            table.getColumnModel().getColumn(1).setPreferredWidth(70);

            DefaultTableCellRenderer centrar = new DefaultTableCellRenderer();
            centrar.setHorizontalAlignment(SwingConstants.CENTER);

            table.getColumnModel().getColumn(0).setCellRenderer(centrar);
            table.getColumnModel().getColumn(1).setCellRenderer(centrar);

            DefaultTableModel listener = (DefaultTableModel) table.getModel();
            int finalCantidadNodos = cantidadNodos;
            listener.addTableModelListener(hayDatos -> {

                if (hayDatos.getType() == TableModelEvent.UPDATE) {
                    for (int i = 0; i < finalCantidadNodos; i++) {
                        Object datos = modelo.getValueAt(i, 1);
                        String datoString = datos.toString();

                        if (datoString.contains(",")) {

                        } else {

                        }
                    }
                    buttonCrearGrafo.setEnabled(true);
                }

            });

            panelTabla.removeAll();
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            frameCrear.pack();
        });
    }

    private void buttonGenerar() {
        //Tenia pensado hacer que salga una ventana emergente donde el usuario elige dificultad y se genera o algo asi,
        //aun no se
        String[] opciones = { "Facil", "Media", "Dificil" };

        String seleccion = (String) JOptionPane.showInputDialog(
                null,
                "Elige una dificultad",
                "Facil",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        MazeGenerator mazeGenerator = new MazeGenerator();
        int opc = 0;
        if(seleccion.equals("Facil")) {
            opc = 0;
        }else if(seleccion.equals("Media")){
            opc = 1;
        }else if(seleccion.equals("Dificil")){
            opc = 2;
        }

        panelMaze.removeAll();
        panelMaze.setLayout(new BorderLayout());
        Labyrinth labyrinth = mazeGenerator.generateMazeRandom(opc);
        gl = new GraphinLabyrinth(labyrinth);

        JScrollPane scrollPane = new JScrollPane(gl,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelMaze.add(scrollPane, BorderLayout.CENTER);

        panelMaze.repaint();
        panelMaze.revalidate();

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

    /**
     * Metodo que extrae los datos almacenados en la tabla que guarda los nodos adyacentes
     */
    private void buttonCrearGrafo() {
        Labyrinth labyrinth;
        MazeGenerator mazeGenerator = new MazeGenerator();
        int cantidadNodos = rows * columns;

        String[] adjancencyList = new String[cantidadNodos];
        DefaultTableModel datosSacados = (DefaultTableModel) table.getModel();

        for (int i = 0; i < cantidadNodos; i++) {
            adjancencyList[i] = datosSacados.getValueAt(i, 1).toString();
            adjancencyList[i] = adjancencyList[i].replace(" ","");
        }


        labyrinth = mazeGenerator.generateMazeFromGivenNodes(rows,columns, adjancencyList);
        if (labyrinth == null) {
            JOptionPane.showMessageDialog(null, "Crea un laberinto valido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LabyrinthSolver solver = new LabyrinthSolver();

        ArrayList<Node> labyrinthTemp = solver.AstarAlgorithm(labyrinth);

        if (labyrinthTemp == null) {
            JOptionPane.showMessageDialog(null, "Crea un laberinto valido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        gl = new GraphinLabyrinth(labyrinth);

        panelMaze.removeAll();
        panelMaze.add(gl, BorderLayout.CENTER);
        panelMaze.repaint();
        panelMaze.revalidate();

    }
}
