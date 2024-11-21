
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class SentimentAnalyzer {
    private NaiveBayes model;
    private Instances dataStructure;

    // 생성자에서 CSV 파일을 로드하고 모델을 학습합니다.
    public SentimentAnalyzer(String filePath) throws IOException {
        // CSV 파일이 존재하는지 확인
        File csvFile = new File(filePath);
        if (!csvFile.exists()) {
            throw new IOException("CSV 파일을 찾을 수 없습니다: " + filePath);
        }

        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(csvFile);
            loader.setOptions(new String[]{"-charset", "UTF-8"});
            Instances data = loader.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);

            model = new NaiveBayes();
            model.buildClassifier(data);

            dataStructure = createDataStructure();
            System.out.println("모델 학습 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 데이터 구조를 생성합니다.
    private Instances createDataStructure() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("text", (ArrayList<String>) null));
        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("positive");
        classValues.add("negative");
        attributes.add(new Attribute("label", classValues));

        Instances dataStructure = new Instances("SentimentInstances", attributes, 0);
        dataStructure.setClassIndex(1);
        return dataStructure;
    }

    // 입력된 텍스트를 분석하여 감성을 예측합니다.
    public String analyzeSentiment(String text) throws Exception {
        DenseInstance instance = new DenseInstance(2);
        instance.setValue(dataStructure.attribute(0), text);
        instance.setDataset(dataStructure);

        double prediction = model.classifyInstance(instance);
        return dataStructure.classAttribute().value((int) prediction);
    }

    public static void main(String[] args) {
        // 콘솔 출력 시 UTF-8 인코딩 강제 설정
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("UTF-8 인코딩을 지원하지 않습니다.");
        }

        try {
            String filePath = "C:\\data\\AI\\sentiment_data.csv";
            SentimentAnalyzer analyzer = new SentimentAnalyzer(filePath);

            Scanner scanner = new Scanner(System.in, "UTF-8");
            System.out.println("\n문장을 입력하세요 (종료하려면 'exit' 입력):");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("프로그램을 종료합니다.");
                    break;
                }

                String result = analyzer.analyzeSentiment(input);
                System.out.println("결과: " + result);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}