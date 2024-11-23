package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        // 메인 페이지 프레임 생성
        JFrame frame = new JFrame("날씨와 자연재해");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // 상단 제목
        JLabel titleLabel = new JLabel("날씨와 자연재해", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        // 버튼 패널 생성
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        // "날씨 확인" 버튼
        JButton weatherButton = new JButton("날씨 확인");
        weatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Today.main(null); // 날씨 확인 페이지로 이동
            }
        });

        // "재난 확인" 버튼
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

        // 하단 설명 텍스트 추가
        JLabel footerLabel = new JLabel("날씨 및 재난 정보를 확인하세요!", SwingConstants.CENTER);
        footerLabel.setFont(new Font("맑은 고딕", Font.ITALIC, 14));
        frame.add(footerLabel, BorderLayout.SOUTH);

        // 메인 페이지 표시
        frame.setVisible(true);
    }
}
