package project;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class HeatWaveGraph {
    public static void displayGraph(Map<Integer, Double> maxTemperatures, Map<Integer, Integer> heatWaveCounts) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Integer year : maxTemperatures.keySet()) {

            dataset.addValue(heatWaveCounts.getOrDefault(year, 0), "폭염 횟수 (회)", year.toString());
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "연도별 폭염 횟수", // 제목
                "연도", // X축
                "값", // Y축
                dataset
        );

        Font font = new Font("맑은 고딕", Font.PLAIN, 12); // 맑은 고딕 폰트 사용
        chart.getTitle().setFont(font); // 차트 제목 폰트
        chart.getCategoryPlot().getDomainAxis().setLabelFont(font); // x축 레이블 폰트
        chart.getCategoryPlot().getRangeAxis().setLabelFont(font); // y축 레이블 폰트
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(font); // x축 눈금 폰트
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(font); // y축 눈금 폰트

        chart.getLegend().setItemFont(font);

        // Swing으로 그래프 표시
        JFrame frame = new JFrame("Weather Statistics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 차트 패널 추가
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

        // 버튼 생성
        JButton backButton = new JButton("돌아가기");
        backButton.setFont(font);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // 현재 창 닫기
            }
        });

        // 버튼 패널 추가
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);

        // 프레임에 차트와 버튼 추가
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}

