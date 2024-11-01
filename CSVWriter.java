import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    private static final String FILE_NAME = "resultados.csv";

    public static void writeData(String algorithm, String executionType, int dataSize, int threads, long duration) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.append(algorithm).append(",")
                  .append(executionType).append(",")
                  .append(String.valueOf(dataSize)).append(",")
                  .append(String.valueOf(threads)).append(",") 
                  .append(String.valueOf(duration)).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
