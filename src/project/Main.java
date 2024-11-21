package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        // JFrame 생성
        JFrame frame = new JFrame("Simple GUI Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // FlowLayout으로 버튼을 나란히 배치
        frame.setLayout(new FlowLayout());

        // 버튼 생성
        JButton button1 = new JButton("구름 차트");
        JButton button2 = new JButton("오늘의 날씨");

        // 버튼 클릭 시 WeatherForecast.main() 메서드 호출
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Cloud의 main 메서드 호출
                Cloud.main(null);
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Rain의 main 메서드 호출
                Today.main(null);
            }
        });

        // 버튼을 프레임에 추가
        frame.getContentPane().add(button1);
        frame.getContentPane().add(button2);

        // 화면에 표시
        frame.setVisible(true);
    }
}
