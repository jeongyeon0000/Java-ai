package project;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TemperatureChart {
    public static void main(String[] args) {
        try {
            // 1. 연도별 평균 기온 계산
            Map<String, Double> yearlyAvgTemp = fetchYearlyAverageTemperature(58, 125);

            // 2. JFreeChart 데이터 세트 생성
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            yearlyAvgTemp.forEach((year, avgTemp) -> dataset.addValue(avgTemp, "기온", year));

            // 3. 그래프 생성
            JFreeChart chart = ChartFactory.createLineChart(
                    "연도별 평균 기온",   // 차트 제목
                    "연도",              // X축 라벨
                    "기온 (℃)",         // Y축 라벨
                    dataset              // 데이터
            );

            Font font = new Font("맑은 고딕", Font.PLAIN, 12); // 맑은 고딕 폰트 사용
            chart.getTitle().setFont(font); // 차트 제목 폰트
            chart.getCategoryPlot().getDomainAxis().setLabelFont(font); // x축 레이블 폰트
            chart.getCategoryPlot().getRangeAxis().setLabelFont(font); // y축 레이블 폰트
            chart.getCategoryPlot().getDomainAxis().setTickLabelFont(font); // x축 눈금 폰트
            chart.getCategoryPlot().getRangeAxis().setTickLabelFont(font); // y축 눈금 폰트

            chart.getLegend().setItemFont(font);

            // 4. 그래프 패널 생성 및 표시
            JFrame frame = new JFrame("Temperature Chart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // API에서 연도별 데이터를 수집하고 평균 기온을 계산하는 메서드
    public static Map<String, Double> fetchYearlyAverageTemperature(int x, int y) throws Exception {
        Map<String, Double> yearlyTempSum = new HashMap<>();
        Map<String, Integer> yearlyTempCount = new HashMap<>();

        // 2020년부터 2023년까지 각 연도, 월에 대해 기온 데이터를 수집
        for (int year = 2020; year <= 2023; year++) {
            for (int month = 1; month <= 12; month++) {
                String baseDate = LocalDate.of(year, month, 1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                String[] data = new String[5];
                String error = get(x, y, data, baseDate);

                if (error == null && data[3] != null) { // 기온 데이터가 있는 경우
                    double temp = Double.parseDouble(data[3]); // 기온
                    yearlyTempSum.put(String.valueOf(year), yearlyTempSum.getOrDefault(String.valueOf(year), 0.0) + temp);
                    yearlyTempCount.put(String.valueOf(year), yearlyTempCount.getOrDefault(String.valueOf(year), 0) + 1);

                    // 디버깅 로그 추가
                    System.out.println("Year: " + year + ", Month: " + month + ", Temperature: " + temp);
                } else if (error != null) {
                    // 에러 메시지 출력
                    System.out.println("Error fetching data for " + year + "-" + month + ": " + error);
                }
            }
        }

        // 연도별 평균 기온 계산
        Map<String, Double> yearlyAvgTemp = new HashMap<>();
        for (String year : yearlyTempSum.keySet()) {
            double sum = yearlyTempSum.get(year);
            int count = yearlyTempCount.get(year);
            double avgTemp = sum / count; // 평균 기온 계산
            yearlyAvgTemp.put(year, avgTemp);

            // 디버깅 로그 추가
            System.out.println("Year: " + year + ", Average Temperature: " + avgTemp);
        }
        return yearlyAvgTemp;
    }

    // API 호출을 통해 기온 데이터를 가져오는 메서드
    public static String get(int x, int y, String[] v, String baseDate) {
        HttpURLConnection con = null;
        String errorMessage = null; // 에러 메시지

        try {
            LocalDateTime t = LocalDateTime.now().minusMinutes(30); // 현재 시각 30분전

            // API 호출 URL
            URL url = new URL(
                    "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
                            + "?ServiceKey=hp2nMhDfaycAzWeYtdBtAyoY4H4%2F8ArVQim7XrByjLDJ3c7rKKGbaD90YNoViKi7cuLt2RET3cyCIJqlfwzbeA%3D%3D"
                            + "&numOfRows=60"
                            + "&base_date=" + baseDate
                            + "&base_time=0600"
                            + "&nx=" + x
                            + "&ny=" + y
            );

            con = (HttpURLConnection) url.openConnection();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());

            boolean success = false; // <resultCode>00</resultCode> 획득 여부

            Element e;
            NodeList ns = doc.getElementsByTagName("header");
            if (ns.getLength() > 0) {
                e = (Element) ns.item(0);
                if ("00".equals(e.getElementsByTagName("resultCode").item(0).getTextContent())) {
                    success = true; // 성공 여부
                } else { // 에러 메시지
                    errorMessage = e.getElementsByTagName("resultMsg").item(0).getTextContent();
                }
            }

            if (success) {
                String fd = null, ft = null; // 가장 빠른 예보 시각
                String cat; // category
                String val; // fcstValue

                ns = doc.getElementsByTagName("item");
                for (int i = 0; i < ns.getLength(); i++) {
                    e = (Element) ns.item(i);

                    if (ft == null) { // 가져올 예보 시간 결정
                        fd = e.getElementsByTagName("fcstDate").item(0).getTextContent(); // 예보 날짜
                        ft = e.getElementsByTagName("fcstTime").item(0).getTextContent(); // 예보 시각
                    } else if (!fd.equals(e.getElementsByTagName("fcstDate").item(0).getTextContent()) ||
                            !ft.equals(e.getElementsByTagName("fcstTime").item(0).getTextContent())) {
                        continue;
                    }

                    cat = e.getElementsByTagName("category").item(0).getTextContent(); // 자료구분코드
                    val = e.getElementsByTagName("fcstValue").item(0).getTextContent(); // 예보 값

                    // 기온(T1H)만 추출하여 v[3]에 저장
                    if ("T1H".equals(cat)) {
                        v[3] = val; // 기온
                    }
                }

                v[0] = fd;
                v[1] = ft;
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        if (con != null)
            con.disconnect();

        return errorMessage;
    }
}