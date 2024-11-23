package project;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ColdWaveGraph {
    public static void displayGraph(Map<Integer, Integer> advisoryCounts, Map<Integer, Integer> warningCounts) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // 데이터 정렬 및 그래프에 추가
        Set<Integer> years = new HashSet<>(advisoryCounts.keySet());
        years.addAll(warningCounts.keySet());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());

        for (Integer year : sortedYears) {
            dataset.addValue(advisoryCounts.getOrDefault(year, 0), "주의보", year.toString());
            dataset.addValue(warningCounts.getOrDefault(year, 0), "경보", year.toString());
        }

        // JFreeChart 그래프 생성
        JFreeChart chart = ChartFactory.createLineChart(
                "연도별 한파 주의보 및 경보 횟수", // 그래프 제목
                "연도", // X축 라벨
                "횟수", // Y축 라벨
                dataset
        );

        Font font = new Font("맑은 고딕", Font.PLAIN, 12); // 맑은 고딕 폰트 사용
        chart.getTitle().setFont(font); // 차트 제목 폰트
        chart.getCategoryPlot().getDomainAxis().setLabelFont(font); // x축 레이블 폰트
        chart.getCategoryPlot().getRangeAxis().setLabelFont(font); // y축 레이블 폰트
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(font); // x축 눈금 폰트
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(font); // y축 눈금 폰트
        chart.getLegend().setItemFont(font);

        // 그래프 표시를 위한 JFrame 생성
        JFrame frame = new JFrame("Cold Wave Alerts");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 그래프 패널 추가
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

        // 이전 화면으로 돌아가는 버튼 추가
        JButton backButton = new JButton("돌아가기");
        backButton.setFont(font);
        backButton.addActionListener(e -> frame.dispose()); // 창 닫기

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }
}

