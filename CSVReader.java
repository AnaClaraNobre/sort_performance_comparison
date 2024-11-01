import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {
    
    public static Map<String, ArrayList<Long>> readData(String fileName) {
        Map<String, ArrayList<Long>> dataMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String algorithmKey = values[0] + " - " + values[1]; 
                long duration = Long.parseLong(values[4]); // Correção do índice para 4

                dataMap.putIfAbsent(algorithmKey, new ArrayList<>());
                dataMap.get(algorithmKey).add(duration);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataMap;
    }
    
 public static List<String[]> readDataForTable(String fileName) {
    List<String[]> data = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            data.add(values); 
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return data;
}
}
