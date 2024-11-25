package project;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.SerializationHelper;
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

        // 2025년 월별 예측
        for (int month = 1; month <= 12; month++) {
            // 기존 데이터에서 월별 예측을 위한 인스턴스를 가져옴
            Instance newInstance = data.instance(0); // 첫 번째 인스턴스를 복사해서 사용

            // 예측할 월만 수정
            newInstance.setValue(0, month); // 월 설정

            // 기온 예측
            double predictedTemp = model.classifyInstance(newInstance);

            // 예측된 기온 출력
            System.out.println("The predicted temperature for " + month + " 2025 : " + predictedTemp + "C");
        }
    }
}