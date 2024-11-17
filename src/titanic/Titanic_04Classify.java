package titanic;

import java.io.File;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class Titanic_04Classify {

    private Classifier model = null;

    public static void main(String[] args) {
        try {
            // Classifier 모델 객체를 초기화하여 실행
            Classifier classifier = new weka.classifiers.trees.J48(); // 기본으로 J48 모델 사용
            Titanic_04Classify titanicClassifier = new Titanic_04Classify(classifier);

            // 모델 로드 및 데이터 로드
            Instances data = MoreWekaCommon.loadData("https://www.openml.org/data/download/16826755/phpMYEkMl.arff"); // 데이터 파일 URL
            titanicClassifier.useModel(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Titanic_04Classify(Classifier model) {
        System.out.println("Classifier model : " + MoreWekaCommon.getModelName(model));
        this.model = model;
    }

    // 모델 로드 메서드
    public Classifier loadModel(String modelName) throws Exception {
        String modelPath = MoreWekaCommon.modelPath + "titanic" + File.separator + "titanic_" + modelName;

        // 모델 파일 존재 여부 확인
        File modelFile = new File(modelPath);
        if (!modelFile.exists()) {
            throw new Exception("Model file does not exist at path: " + modelPath);
        }

        return (Classifier) weka.core.SerializationHelper.read(modelPath);
    }

    // 모델 사용
    public void useModel(Instances data) throws Exception {
        Classifier model = loadModel(MoreWekaCommon.getModelName(this.model));

        // 데이터의 클래스 속성을 마지막 속성으로 설정
        data.setClassIndex(data.numAttributes() - 1);

        // 각 인스턴스에 대해 예측 수행
        for (int x = 0; x < data.numInstances(); x++) {
            Instance inst = data.get(x);
            double predictedValue = model.classifyInstance(inst);

            // 예측 결과와 실제 값 비교 출력
            if (data.get(x).classValue() != predictedValue) {
                System.out.println(
                        "Instance: " + inst.toString() + ", Predicted: " + predictedValue + ", Actual: " + data.get(x).classValue());
            }
        }
    }
}