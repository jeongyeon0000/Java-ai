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
    public static void displayGraph(Map<Integer, Integer> heatWaveCounts) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // 폭염 횟수 데이터를 그래프 데이터셋에 추가
        for (Integer year : heatWaveCounts.keySet()) {
            dataset.addValue(heatWaveCounts.get(year), "폭염 횟수 (회)", year.toString());
        }

        // 차트 생성
        JFreeChart chart = ChartFactory.createLineChart(
                "연도별 폭염 횟수", // 제목
                "연도", // X축 레이블
                "횟수", // Y축 레이블
                dataset
        );

        // 폰트 설정 (맑은 고딕)
        Font font = new Font("맑은 고딕", Font.PLAIN, 12);
        chart.getTitle().setFont(font); // 차트 제목 폰트
        chart.getCategoryPlot().getDomainAxis().setLabelFont(font); // X축 레이블 폰트
        chart.getCategoryPlot().getRangeAxis().setLabelFont(font); // Y축 레이블 폰트
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(font); // X축 눈금 폰트
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(font); // Y축 눈금 폰트
        chart.getLegend().setItemFont(font); // 범례 폰트

        // Swing 프레임에 그래프 표시
        JFrame frame = new JFrame("연도별 폭염 횟수 그래프");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 차트 패널 추가
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

        // 돌아가기 버튼 추가
        JButton backButton = new JButton("돌아가기");
        backButton.setFont(font);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // 현재 창 닫기
            }
        });

        // 버튼 패널 설정
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);

        // 프레임에 차트와 버튼 추가
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}