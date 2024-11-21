package project;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.plot.PlotOrientation;
import java.awt.*;

public class Rain {

    public static void main(String[] args) {
        // API URL 및 인증키 설정
        String urlString = "https://apihub.kma.go.kr/api/typ01/url/fct_afs_wl.php?"
                + "reg=&tmfc1=2013121106&tmfc2=2013121118&tmef1=20131214&tmef2=20131219&disp=0&help=0&authKey=VMoWGvguRqWKFhr4Loalcw";

        try {
            // URL 객체 생성
            URL url = new URL(urlString);

            // HttpURLConnection 객체 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);  // 타임아웃 설정
            connection.setReadTimeout(5000);     // 읽기 타임아웃 설정

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                // 응답 스트림을 BufferedReader로 읽기 (EUC-KR 인코딩 명시)
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "EUC-KR"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                // 응답 내용 읽기
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String data = response.toString();

                // 날짜별 강수량 데이터를 저장할 맵
                Map<String, Integer> precipitationMap = new HashMap<>();

                // 응답 데이터 분석
                String[] parts = data.split(" "); // 공백으로 분리
                String date = ""; // 날짜를 저장할 변수

                // 그래프 데이터셋 준비
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                for (String part : parts) {
                    if (part.contains("2013")) {  // 날짜 정보가 포함된 부분 찾기
                        // 날짜 정보 추출 (예: "201312110600" -> "2013-12-11")
                        date = part.substring(0, 4) + "-" + part.substring(4, 6) + "-" + part.substring(6, 8);
                    }

                    // 강수량 데이터 추출
                    if (part.contains("RN1")) {
                        try {
                            // RN1 다음에 오는 숫자가 강수량
                            int startIndex = part.indexOf("RN1") + 4;
                            int endIndex = part.indexOf("mm", startIndex);
                            if (endIndex > startIndex) {
                                int precipitation = Integer.parseInt(part.substring(startIndex, endIndex));
                                precipitationMap.put(date, precipitation);
                            }
                        } catch (Exception e) {
                            // 강수량 데이터가 없거나 잘못된 경우 예외 처리
                            System.out.println("강수량 데이터를 읽는 중 오류 발생: " + e.getMessage());
                        }
                    }
                }

                // 날짜별로 데이터셋에 추가
                boolean hasValidData = false; // 유효한 데이터가 있는지 확인하는 변수

                for (String currentDate : precipitationMap.keySet()) {
                    int precipitation = precipitationMap.get(currentDate);
                    if (precipitation != -999) {
                        dataset.addValue(precipitation, "강수량(mm)", currentDate);
                        hasValidData = true; // 유효한 데이터가 있으면 true로 설정
                    }
                }

                // 데이터가 없으면 그래프 대신 "데이터 없음" 메시지 표시
                if (!hasValidData) {
                    JOptionPane.showMessageDialog(null, "강수량 데이터가 없습니다.");
                } else {
                    // 차트 생성
                    JFreeChart chart = ChartFactory.createBarChart(
                            "강수량 그래프", // 차트 제목
                            "날짜", // x축 레이블
                            "강수량(mm)", // y축 레이블
                            dataset, // 데이터셋
                            PlotOrientation.VERTICAL, // 그래프의 방향
                            true, // 범례 표시
                            true, // 툴팁 표시
                            false // URL 링크 활성화
                    );

                    // 한글 폰트 설정
                    Font font = new Font("맑은 고딕", Font.PLAIN, 12); // 맑은 고딕 폰트 사용
                    chart.getTitle().setFont(font); // 차트 제목 폰트
                    chart.getCategoryPlot().getDomainAxis().setLabelFont(font); // x축 레이블 폰트
                    chart.getCategoryPlot().getRangeAxis().setLabelFont(font); // y축 레이블 폰트
                    chart.getCategoryPlot().getDomainAxis().setTickLabelFont(font); // x축 눈금 폰트
                    chart.getCategoryPlot().getRangeAxis().setTickLabelFont(font); // y축 눈금 폰트

                    chart.getLegend().setItemFont(font);
                    // 차트를 JFrame에 표시
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.getContentPane().add(new ChartPanel(chart));
                    frame.pack();
                    frame.setVisible(true);
                }
            } else {
                System.out.println("API 호출 실패: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}