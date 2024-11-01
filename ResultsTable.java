import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResultsTable {
    public static void displayResultsTable() {
        List<String[]> data = CSVReader.readDataForTable("resultados.csv");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Resultados de Desempenho dos Algoritmos de Ordenação");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 400);

            String[] columnNames = {"Algoritmo", "Tipo de Execução", "Tamanho dos Dados", "Threads", "Tempo (s)"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            for (String[] rowData : data) {
                double durationInSeconds = Double.parseDouble(rowData[4]) / 1_000_000_000.0;
                String[] rowWithSeconds = {
                    rowData[0], 
                    rowData[1], 
                    rowData[2], 
                    rowData[3],
                    String.format("%.4f", durationInSeconds) 
                };
                tableModel.addRow(rowWithSeconds);
            }

            JTable table = new JTable(tableModel);
            frame.add(new JScrollPane(table), BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
