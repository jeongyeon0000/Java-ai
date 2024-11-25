package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("날씨와 자연재해");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("날씨와 자연재해", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton weatherButton = new JButton("날씨 확인");
        weatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openWeatherOptions(); // 날씨 확인 옵션 창 열기
            }
        });

        JButton disasterButton = new JButton("재난 확인");
        disasterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sub.main(null); // 재난 확인 페이지로 이동
            }
        });

        buttonPanel.add(weatherButton);
        buttonPanel.add(disasterButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("날씨 및 자연재해 정보를 확인하세요!", SwingConstants.CENTER);
        footerLabel.setFont(new Font("맑은 고딕", Font.ITALIC, 14));
        frame.add(footerLabel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void openWeatherOptions() {
        JFrame frame = new JFrame("날씨와 자연재해");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("날씨 확인", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton todayWeatherButton = new JButton("오늘의 날씨");
        todayWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Today.main(null); // 오늘의 날씨 페이지로 이동
            }
        });

        JButton weatherAiButton = new JButton("날씨 예측 AI");
        weatherAiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TemperaturePredictionAi.main(null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "날씨 예측 AI 실행 중 오류가 발생했습니다: " + ex.getMessage());
                    ex.printStackTrace(); // 디버깅용 에러 출력
                }
            }
        });

        buttonPanel.add(todayWeatherButton);
        buttonPanel.add(weatherAiButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> frame.dispose());
        frame.add(closeButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}