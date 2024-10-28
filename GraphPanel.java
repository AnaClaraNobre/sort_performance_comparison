import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class GraphPanel extends JPanel {
    private Map<String, ArrayList<Long>> dataMap;

    public GraphPanel(Map<String, ArrayList<Long>> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int padding = 60;
        int labelPadding = 50;
        int width = getWidth();
        int height = getHeight();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double maxDuration = dataMap.values().stream()
                .flatMap(ArrayList::stream)
                .mapToDouble(duration -> duration / 1_000_000_000.0) // Converter para segundos
                .max()
                .orElse(1.0) ;

        int numExecutions = dataMap.values().stream()
                                   .findFirst()
                                   .map(ArrayList::size)
                                   .orElse(1);

        int xInterval = numExecutions > 1
                ? (width - 2 * padding - labelPadding) / (numExecutions - 1)
                : (width - 2 * padding - labelPadding);

        double yScale = ((double) height - 2 * padding - labelPadding) / maxDuration;

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(padding + labelPadding, padding, width - 2 * padding - labelPadding, height - 2 * padding - labelPadding);
        
        // Linhas de grade
        g2.setColor(Color.WHITE);
        for (int i = 0; i <= 10; i++) {
            int y = height - ((i * (height - padding * 2)) / 10 + padding);
            g2.drawLine(padding + labelPadding, y, width - padding, y);
        }

        // Eixos X e Y
        g2.setColor(Color.BLACK);
        g2.drawLine(padding + labelPadding, height - padding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, height - padding, width - padding, height - padding);

        // Rótulos dos eixos
        g2.drawString("Execução", width / 2, height - padding + 15);
        g2.drawString("Tempo de Execução (s)", padding / 2, height / 2);

        Color[] colors = {
            new Color(0, 0, 139),    
            new Color(139, 0, 0),    
            new Color(0, 100, 0),    
            new Color(184, 134, 11), 
            new Color(72, 61, 139),  
            new Color(79, 69, 19),  
            new Color(70, 130, 180), 
            new Color(128, 0, 128) 
        };
        int colorIndex = 0;


        // Desenho dos pontos e linhas para cada algoritmo e tipo de execução
        for (Map.Entry<String, ArrayList<Long>> entry : dataMap.entrySet()) {
            String algorithm = entry.getKey();
            ArrayList<Long> durations = entry.getValue();
            g2.setColor(colors[colorIndex % colors.length]);

            // Conecta os pontos com uma linha para cada algoritmo
            for (int i = 0; i < durations.size(); i++) {
                int x = i * xInterval + padding + labelPadding;
                int y = height - padding - (int) ((durations.get(i) / 1_000_000_000.0) * yScale); // Converter para segundos

                // Desenha o ponto e conecta com uma linha
                g2.fillOval(x - 3, y - 3, 6, 6);
                if (i > 0) {
                    int xPrev = (i - 1) * xInterval + padding + labelPadding;
                    int yPrev = height - padding - (int) ((durations.get(i - 1) / 1_000_000_000.0) * yScale);
                    g2.drawLine(xPrev, yPrev, x, y); // Linha entre os pontos
                }

                if (i == durations.size() - 1 || i == 0) {
                    g2.drawString(String.format("%.3f s", durations.get(i) / 1_000_000_000.0), x - 35, y + 5);
                }
            }
            colorIndex++;
        }

        // Rótulos do eixo Y com ajuste de escala
        g2.setColor(Color.BLACK);
        for (int i = 0; i <= 10; i++) {
            int x0 = padding + labelPadding;
            int y0 = height - ((i * (height - padding * 2)) / 10 + padding);
            g2.drawLine(x0 - 5, y0, x0, y0);
            String yLabel = String.format("%.2f s", (maxDuration * i / 10));
            g2.drawString(yLabel, x0 - 45, y0 + 5);
        }

        // Rótulos do eixo X para cada execução
        for (int i = 0; i < numExecutions; i++) {
            int x0 = i * xInterval + padding + labelPadding;
            int y0 = height - padding;
            g2.drawLine(x0, y0, x0, y0 + 5);
            g2.drawString("Exec " + (i + 1), x0 - 10, y0 + 20);
        }

        int legendX = (width - (colors.length * 225)) / 2;
        int legendY = height - padding + 30;
        colorIndex = 0;
        
        for (String algorithm : dataMap.keySet()) {
            g2.setColor(colors[colorIndex % colors.length]);
            g2.fillRect(legendX + colorIndex * 225, legendY, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawString(algorithm, legendX + colorIndex * 225 + 20, legendY + 12);
            colorIndex++;
        }
    }
}
