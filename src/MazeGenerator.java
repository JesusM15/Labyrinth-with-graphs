 import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
}
