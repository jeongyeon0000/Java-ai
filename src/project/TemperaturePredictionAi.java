package project;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.SerializationHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class TemperaturePredictionAi {

    public static void main(String[] args) throws Exception {
        // ARFF 파일 로딩
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File("C:\\data\\weather\\extremum_7.arff")); // ARFF 파일 경로 설정
        Instances data = loader.getDataSet();

        // 클래스 속성 설정 (평균기온)
        data.setClassIndex(data.numAttributes() - 1);

        // 학습된 RandomForest 모델 로드
        Classifier model = (Classifier) SerializationHelper.read("C:\\data\\weather\\temperature_model5.model");

        // 테이블에 데이터를 추가하기 위한 모델 준비
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("월");
        tableModel.addColumn("예측된 기온 (°C)");

        // 2025년 월별 예측 데이터를 테이블에 추가
        for (int month = 1; month <= 12; month++) {
            // 기존 데이터에서 월별 예측을 위한 인스턴스를 가져옴
            Instance newInstance = data.instance(0); // 첫 번째 인스턴스를 복사해서 사용

            // 예측할 월만 수정
            newInstance.setValue(0, month); // 월 설정

            // 기온 예측
            double predictedTemp = model.classifyInstance(newInstance);

            // 예측된 기온을 테이블에 추가
            tableModel.addRow(new Object[]{month, String.format("%.2f°C", predictedTemp)});
        }

        // JFrame 생성
        JFrame frame = new JFrame("2025년 월별 기온 예측");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // JTable 생성 및 설정
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> frame.dispose());
        frame.add(closeButton, BorderLayout.SOUTH);

        // GUI 표시
        frame.setVisible(true);
    }
}