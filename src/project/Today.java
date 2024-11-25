package project;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Today {
    public static void main(String[] args) {
        // JFrame 생성
        JFrame frame = new JFrame("날씨와 자연재해");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400); // 버튼 추가로 크기 조정
        frame.setLayout(new BorderLayout());

        // 상단 설명 텍스트
        JLabel titleLabel = new JLabel("오늘의 날씨", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        titleLabel.setForeground(new Color(79, 78, 78));
        frame.add(titleLabel, BorderLayout.NORTH);

        // 날씨 정보를 표시할 패널
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 1, 10, 10));
        infoPanel.setBackground(new Color(255, 247, 185));

        JLabel dateLabel = new JLabel("날짜: ", SwingConstants.CENTER);
        JLabel timeLabel = new JLabel("시간: ", SwingConstants.CENTER);
        JLabel weatherLabel = new JLabel("날씨: ", SwingConstants.CENTER);
        JLabel tempLabel = new JLabel("기온: ", SwingConstants.CENTER);
        JLabel humidityLabel = new JLabel("습도: ", SwingConstants.CENTER);

        Font labelFont = new Font("맑은 고딕", Font.PLAIN, 16);
        dateLabel.setFont(labelFont);
        timeLabel.setFont(labelFont);
        weatherLabel.setFont(labelFont);
        tempLabel.setFont(labelFont);
        humidityLabel.setFont(labelFont);

        infoPanel.add(dateLabel);
        infoPanel.add(timeLabel);
        infoPanel.add(weatherLabel);
        infoPanel.add(tempLabel);
        infoPanel.add(humidityLabel);

        frame.add(infoPanel, BorderLayout.CENTER);

        // 하단에 돌아가기 버튼 추가
        JButton backButton = new JButton("돌아가기");
        backButton.addActionListener(e -> frame.dispose());
        frame.add(backButton, BorderLayout.SOUTH);

        // 로딩 스레드
        SwingUtilities.invokeLater(() -> {
            dateLabel.setText("날짜: 로딩 중...");
            timeLabel.setText("시간: 로딩 중...");
            weatherLabel.setText("날씨: 로딩 중...");
            tempLabel.setText("기온: 로딩 중...");
            humidityLabel.setText("습도: 로딩 중...");

            String[] weatherData = new String[5];
            String error = TodaySub.get(58, 125, weatherData); // 서울 금천구 가산동 예시

            if (error == null) {
                dateLabel.setText("날짜: " + weatherData[0]);
                timeLabel.setText("시간: " + weatherData[1]);
                weatherLabel.setText("날씨: " + weatherData[2]);
                tempLabel.setText("기온: " + weatherData[3] + "℃");
                humidityLabel.setText("습도: " + weatherData[4] + "%");
            } else {
                JOptionPane.showMessageDialog(frame, "에러 발생: " + error, "Error", JOptionPane.ERROR_MESSAGE);
                dateLabel.setText("날짜: 정보 없음");
                timeLabel.setText("시간: 정보 없음");
                weatherLabel.setText("날씨: 정보 없음");
                tempLabel.setText("기온: 정보 없음");
                humidityLabel.setText("습도: 정보 없음");
            }
        });

        // GUI 표시
        frame.setVisible(true);
    }
}

class TodaySub {
    public static String get(int x, int y, String[] v) {
        HttpURLConnection con = null;
        String s = null;

        try {
            LocalDateTime t = LocalDateTime.now().minusMinutes(30);

            URL url = new URL(
                    "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"
                            + "?ServiceKey=hp2nMhDfaycAzWeYtdBtAyoY4H4%2F8ArVQim7XrByjLDJ3c7rKKGbaD90YNoViKi7cuLt2RET3cyCIJqlfwzbeA%3D%3D"
                            + "&numOfRows=60"
                            + "&base_date=" + t.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            + "&base_time=" + t.format(DateTimeFormatter.ofPattern("HHmm"))
                            + "&nx=" + x
                            + "&ny=" + y
            );

            con = (HttpURLConnection) url.openConnection();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());

            boolean ok = false;

            Element e;
            NodeList ns = doc.getElementsByTagName("header");
            if (ns.getLength() > 0) {
                e = (Element) ns.item(0);
                if ("00".equals(e.getElementsByTagName("resultCode").item(0).getTextContent()))
                    ok = true;
                else
                    s = e.getElementsByTagName("resultMsg").item(0).getTextContent();
            }

            if (ok) {
                String fd = null, ft = null;
                String pty = null;
                String sky = null;
                String cat;
                String val;

                ns = doc.getElementsByTagName("item");
                for (int i = 0; i < ns.getLength(); i++) {
                    e = (Element) ns.item(i);

                    if (ft == null) {
                        fd = e.getElementsByTagName("fcstDate").item(0).getTextContent();
                        ft = e.getElementsByTagName("fcstTime").item(0).getTextContent();
                    } else if (!fd.equals(e.getElementsByTagName("fcstDate").item(0).getTextContent()) ||
                            !ft.equals(e.getElementsByTagName("fcstTime").item(0).getTextContent()))
                        continue;

                    cat = e.getElementsByTagName("category").item(0).getTextContent();
                    val = e.getElementsByTagName("fcstValue").item(0).getTextContent();

                    if ("PTY".equals(cat)) pty = val;
                    else if ("SKY".equals(cat)) sky = val;
                    else if ("T1H".equals(cat)) v[3] = val;
                    else if ("REH".equals(cat)) v[4] = val;
                }

                v[0] = fd;
                v[1] = ft;

                if ("0".equals(pty)) {
                    if ("1".equals(sky)) v[2] = "맑음";
                    else if ("3".equals(sky)) v[2] = "구름많음";
                    else if ("4".equals(sky)) v[2] = "흐림";
                } else if ("1".equals(pty)) v[2] = "비";
                else if ("2".equals(pty)) v[2] = "비/눈";
                else if ("3".equals(pty)) v[2] = "눈";
                else if ("5".equals(pty)) v[2] = "빗방울";
                else if ("6".equals(pty)) v[2] = "빗방울눈날림";
                else if ("7".equals(pty)) v[2] = "눈날림";
            }
        } catch (Exception e) {
            s = e.getMessage();
        }

        if (con != null)
            con.disconnect();

        return s;
    }
}