import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    private static SortingAlgorithms algorithms = new SortingAlgorithms();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\nSelecione uma opção:");
            System.out.println("1 - Executar Bubble Sort");
            System.out.println("2 - Executar Quick Sort");
            System.out.println("3 - Executar Merge Sort");
            System.out.println("4 - Executar Selection Sort");
            System.out.println("5 - Executar todos os algoritmos (Sequencial e Paralelo)");
            System.out.println("6 - Limpar conteúdo do arquivo CSV");
            System.out.println("7 - Exibir Gráfico de Desempenho");
            System.out.println("8 - Sair");
            System.out.print("Escolha: ");
            
            int choice = scanner.nextInt();
            int[] array = generateRandomArray(10000); 

            switch (choice) {
                case 1:
                    executeBubbleSort(array);
                    break;
                case 2:
                    executeQuickSort(array);
                    break;
                case 3:
                    executeMergeSort(array);
                    break;
                case 4:
                    executeSelectionSort(array);
                    break;
                case 5:
                    executeBubbleSort(array);
                    executeQuickSort(array);
                    executeMergeSort(array);
                    executeSelectionSort(array);
                    break;
                case 6:
                    clearCSV();
                    System.out.println("Conteúdo do arquivo CSV limpo com sucesso.");
                    break;
                case 7:
                    displayPerformanceGraph();
                    break;
                case 8:
                    running = false;
                    System.out.println("Saindo do programa...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void executeBubbleSort(int[] array) {
        System.out.println("\n--- Executando Bubble Sort ---");
        testAlgorithm(() -> algorithms.bubbleSortSequential(array.clone()), "Bubble Sort Sequencial");
        testAlgorithm(() -> algorithms.bubbleSortParallel(array.clone()), "Bubble Sort Paralelo");
    }

    private static void executeQuickSort(int[] array) {
        System.out.println("\n--- Executando Quick Sort ---");
        testAlgorithm(() -> algorithms.quickSortSequential(array.clone(), 0, array.length - 1), "Quick Sort Sequencial");
        testAlgorithm(() -> algorithms.quickSortParallel(array.clone()), "Quick Sort Paralelo");
    }

    private static void executeMergeSort(int[] array) {
        System.out.println("\n--- Executando Merge Sort ---");
        testAlgorithm(() -> algorithms.mergeSortSequential(array.clone(), 0, array.length - 1), "Merge Sort Sequencial");
        testAlgorithm(() -> algorithms.mergeSortParallel(array.clone()), "Merge Sort Paralelo");
    }

    private static void executeSelectionSort(int[] array) {
        System.out.println("\n--- Executando Selection Sort ---");
        testAlgorithm(() -> algorithms.selectionSortSequential(array.clone()), "Selection Sort Sequencial");
        testAlgorithm(() -> algorithms.selectionSortParallel(array.clone()), "Selection Sort Paralelo");
    }

    private static void testAlgorithm(Runnable algorithm, String algorithmName) {
        long startTime = System.nanoTime();
        algorithm.run();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println(algorithmName + " - Tempo: " + duration + "ns");

        String executionType = algorithmName.contains("Paralelo") ? "Paralelo" : "Sequencial";
        CSVWriter.writeData(algorithmName, executionType, 10000, duration);
    }

    private static void clearCSV() {
        try (FileWriter writer = new FileWriter("resultados.csv")) {
            writer.write("");
        } catch (IOException e) {
            System.out.println("Erro ao limpar o arquivo CSV: " + e.getMessage());
        }
    }

    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(10000);
        }
        return array;
    }

    private static void displayPerformanceGraph() {
        Map<String, ArrayList<Long>> dataMap = CSVReader.readData("resultados.csv");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Desempenho dos Algoritmos de Ordenação");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new GraphPanel(dataMap));
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}
