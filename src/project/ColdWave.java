package project;

import java.io.*;
import java.util.*;

public class ColdWave {
    public static void main(String[] args) {
        String filePath = "C:\\data\\weather\\cold_wave.csv"; // CSV 파일 경로

        // 연도별 주의보 및 경보 횟수 저장
        Map<Integer, Integer> advisoryCounts = new HashMap<>();
        Map<Integer, Integer> warningCounts = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), "UTF-8"))) { // UTF-8 또는 EUC-KR
            String line = br.readLine(); // 헤더 스킵
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 2) continue; // 데이터가 불완전한 경우 스킵

                // 데이터 파싱
                String date = values[0].trim(); // "2020-05-11"
                String alert = values[1].trim(); // "주의보" 또는 "경보" 또는 "X"

                // 연도 추출
                int year = Integer.parseInt(date.split("-")[0]);

                // 주의보/경보 횟수 계산
                if ("주의보".equals(alert)) {
                    advisoryCounts.put(year, advisoryCounts.getOrDefault(year, 0) + 1);
                } else if ("경보".equals(alert)) {
                    warningCounts.put(year, warningCounts.getOrDefault(year, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 그래프 표시
        ColdWaveGraph.displayGraph(advisoryCounts, warningCounts);
    }
}
