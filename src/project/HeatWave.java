package project;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HeatWave {
    public static void main(String[] args) {
        String filePath = "C:\\data\\weather\\heat_wave.csv";

        Map<Integer, Integer> heatWaveCounts = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line = br.readLine(); // 첫 줄 (헤더) 건너뛰기
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 2) continue;

                String date = values[0].trim();
                String heatWave = values[1].trim();

                int year = Integer.parseInt(date.split("-")[0]);

                // 폭염 발생 여부 확인 및 카운트 증가
                if ("O".equals(heatWave)) {
                    heatWaveCounts.put(year, heatWaveCounts.getOrDefault(year, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 폭염 횟수를 그래프로 표시
        HeatWaveGraph.displayGraph(heatWaveCounts);
    }
}