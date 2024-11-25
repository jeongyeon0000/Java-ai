package project;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TyphoonGraph {
    public static void main(String[] args) {
        // 태풍 횟수 데이터
        int[] years = {2020, 2021, 2022, 2023};
        int[] typhoonCounts = {23, 22, 25, 17};

        // 데이터셋 생성
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < years.length; i++) {
            dataset.addValue(typhoonCounts[i], "태풍 횟수", Integer.toString(years[i]));
        }

        // 차트 생성
        JFreeChart chart = ChartFactory.createBarChart(
                "연도별 태풍 횟수", // 제목
                "연도",           // X축 라벨
                "횟수",           // Y축 라벨
                dataset
        );

        // 폰트 설정
        Font font = new Font("맑은 고딕", Font.PLAIN, 12);
        chart.getTitle().setFont(font); // 차트 제목 폰트
        chart.getCategoryPlot().getDomainAxis().setLabelFont(font); // X축 레이블 폰트
        chart.getCategoryPlot().getRangeAxis().setLabelFont(font); // Y축 레이블 폰트
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(font); // X축 눈금 폰트
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(font); // Y축 눈금 폰트
        chart.getLegend().setItemFont(font); // 범례 폰트

        // Swing으로 그래프 표시
        JFrame frame = new JFrame("태풍 데이터 시각화");
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