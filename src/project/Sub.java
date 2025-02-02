package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Sub {
    public static void main(String[] args) {
        // 재난 페이지 프레임 생성
        JFrame frame = new JFrame("날씨와 자연재해");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // 상단 제목
        JLabel titleLabel = new JLabel("재난 확인", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        frame.add(titleLabel, BorderLayout.NORTH);

        // 버튼 패널 생성
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        // 폭염 여부 버튼
        JButton heatWaveButton = new JButton("폭염");
        heatWaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // HeatWeather 클래스의 main 메서드 호출
                HeatWave.main(null);
            }
        });

        // 한파 여부 버튼
        JButton coldWaveButton = new JButton("한파");
        coldWaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ColdWeather 클래스의 main 메서드 호출
                ColdWave.main(null);
            }
        });

        // 황사 여부 버튼
        JButton yellowDustButton = new JButton("황사");
        yellowDustButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                YellowDust.main(null);
            }
        });

        JButton typhoonButton = new JButton("태풍");
        typhoonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TyphoonGraph.main(null);
            }
        });

        buttonPanel.add(heatWaveButton);
        buttonPanel.add(coldWaveButton);
        buttonPanel.add(yellowDustButton);
        buttonPanel.add(typhoonButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        // 닫기 버튼
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> frame.dispose());
        frame.add(closeButton, BorderLayout.SOUTH);

        // GUI 표시
        frame.setVisible(true);
    }
}