import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MazeGenerator {

    public MazeGenerator() {

    }

    public Labyrinth generateMazeFromCSV(String file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = bufferedReader.readLine();
        Labyrinth labyrinth = new Labyrinth();

        while(line != null){
            if(!line.contains(":")){
                String[] dimensions = line.split(",");
                labyrinth.setRows(Integer.parseInt(dimensions[0]));
                labyrinth.setCols(Integer.parseInt(dimensions[1]));
            }else {
                String[] data = line.split(":");

                String[] adjacends = data[1].split(",");

                Node node = labyrinth.getOrCreateNode(Integer.parseInt(data[0]));

                // Agregar al nodo actual sus adyacentes
                for(String adjacend : adjacends){
                    if(Integer.parseInt(adjacend) > labyrinth.getCols()*labyrinth.getRows()-1){
                        return null;
                    }
                    labyrinth.addEdge(node.getId(), Integer.parseInt(adjacend));
                }
            }
            line = bufferedReader.readLine();
        }

        return labyrinth;
    }

}
