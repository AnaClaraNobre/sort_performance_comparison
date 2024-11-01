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
            frame.setSize(900, 400); // Aumentado para acomodar a nova coluna

            String[] columnNames = {"Algoritmo", "Tipo de Execução", "Tamanho dos Dados", "Threads", "Tempo (ns)"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            for (String[] rowData : data) {
                if (rowData.length == 5) {
                    tableModel.addRow(rowData);
                }
            }

            JTable table = new JTable(tableModel);
            frame.add(new JScrollPane(table), BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
