package project;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class YellowDust {
    public static void main(String[] args) {
        String filePath = "C:\\data\\weather\\yellow_dust.csv";

        Map<Integer, Integer> yellowDustCounts = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line = br.readLine(); // 첫 번째 줄은 헤더
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 2) continue;

                String date = values[0].trim();
                String observed = values[1].trim();

                int year = Integer.parseInt(date.split("-")[0]);

                if ("O".equals(observed)) {
                    yellowDustCounts.put(year, yellowDustCounts.getOrDefault(year, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 그래프로 결과 표시
        YellowDustGraph.displayGraph(yellowDustCounts);
    }
}