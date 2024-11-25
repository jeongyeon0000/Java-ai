package project;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HeatWave {
    public static void main(String[] args) {
        String filePath = "C:\\data\\weather\\heat_wave.csv";

        Map<Integer, Double> maxTemperatures = new HashMap<>();
        Map<Integer, Integer> heatWaveCounts = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 3) continue;

                String date = values[0].trim();
                String heatWave = values[1].trim();
                double maxTemperature = Double.parseDouble(values[2].trim());

                int year = Integer.parseInt(date.split("-")[0]);

                maxTemperatures.put(year, Math.max(maxTemperatures.getOrDefault(year, Double.MIN_VALUE), maxTemperature));

                if ("O".equals(heatWave)) {
                    heatWaveCounts.put(year, heatWaveCounts.getOrDefault(year, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 그래프로 결과표시
        HeatWaveGraph.displayGraph(maxTemperatures, heatWaveCounts);
    }
}