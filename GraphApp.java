import javax.swing.*;

import java.util.ArrayList;
import java.util.Map;

public class GraphApp {
    public static void main(String[] args) {
        Map<String, ArrayList<Long>> dataMap = CSVReader.readData("resultados.csv");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Desempenho dos Algoritmos");
            GraphPanel chartPanel = new GraphPanel(dataMap);
            frame.add(chartPanel);
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
