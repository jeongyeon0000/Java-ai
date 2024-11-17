package titanic;

import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.Arrays;
import java.util.Random;
import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.inference.TTest;
import weka.classifiers.Evaluation;

public class Titanic_030Experiment {

    private Classifier[] model;

    public Titanic_030Experiment() {
        this.model = new Classifier[4];
        this.model[0] = new weka.classifiers.trees.J48();
        this.model[1] = new weka.classifiers.functions.SMO();
        this.model[2] = new weka.classifiers.bayes.NaiveBayes();
        this.model[3] = new weka.classifiers.rules.OneR();
    }

    public void saveModel(Classifier model) throws Exception {
        weka.core.SerializationHelper.write(MoreWekaCommon.modelPath + "titanic\\titanic_" +
                MoreWekaCommon.getModelName(model), model);
    }

    public Classifier experimentModel(Instances data) throws Exception {
        int n = 30;
        double[][] sum = new double[this.model.length][n];

        // 각 모델로 교차 검증 실행
        for (int i = 0; i < this.model.length; i++) {
            System.out.println(MoreWekaCommon.getModelName(this.model[i]));
            for (int seed = 0; seed < n; seed++) {
                sum[i][seed] = this.crossValidation(data, this.model[i], seed + 1);
            }
        }

        // 각 모델의 결과를 저장한 Titanic_031ExpResult 배열 생성 및 결과 저장
        Titanic_031ExpResult[] expResult = new Titanic_031ExpResult[this.model.length];
        for (int i = 0; i < this.model.length; i++) {
            expResult[i] = new Titanic_031ExpResult();  // 객체 생성
            expResult[i].setModel(this.model[i]);       // 모델 설정
            expResult[i].setPctCorrect(this.aggregateValue(sum[i]));
            expResult[i].setPctCorrects(sum[i]);
        }

        // 결과를 성능 순으로 정렬
        Arrays.sort(expResult);

        // 최고 성능 모델과 나머지 모델 비교하여 t-검정 수행
        for (int i = 0; i < expResult.length - 1; i++) {
            if (expResult[expResult.length - 1] != null && expResult[i] != null) {  // null 체크
                System.out.print(
                        MoreWekaCommon.getModelName(expResult[expResult.length - 1].getModel()) +
                                " vs. " +
                                MoreWekaCommon.getModelName(expResult[i].getModel()) +
                                " : "
                );
                this.t_test(expResult[expResult.length - 1].getPctCorrects(), expResult[i].getPctCorrects());
            } else {
                System.err.println("expResult 배열에 null 값이 포함되어 있습니다.");
            }
        }

        // 최종 우수 모델 저장 및 반환
        this.saveModel(expResult[expResult.length - 1].getModel());
        return expResult[expResult.length - 1].getModel();
    }

    public double crossValidation(Instances data, Classifier model, int seed) throws Exception {
        data.setClassIndex(data.numAttributes() - 1);
        Instances train = data.trainCV(10, 0, new Random(seed));
        Instances test = data.testCV(10, 0);

        Evaluation eval = new Evaluation(train);
        eval.crossValidateModel(model, train, 10, new Random(seed));
        model.buildClassifier(train);
        eval.evaluateModel(model, test);

        System.out.println("정확도 : " + String.format("%.1f", eval.pctCorrect()) + " %");
        return eval.pctCorrect();
    }

    public double aggregateValue(double[] values) {
        AggregateSummaryStatistics aggregate = new AggregateSummaryStatistics();
        SummaryStatistics sumObj = aggregate.createContributingStatistics();
        for (double value : values)
            sumObj.addValue(value);

        System.out.println("평균 : " + String.format("%.1f", aggregate.getMean()) +
                " %, 표준편차 : " + String.format("%.1f", aggregate.getStandardDeviation()));
        return aggregate.getMean();
    }

    public void t_test(double[] criterion, double[] sample) {
        TTest t_test = new TTest();
        double criterionMean = this.aggregateValue(criterion);
        double sampleMean = this.aggregateValue(sample);

        System.out.println(
                "p-value : " + t_test.tTest(criterion, sample) +
                        " | 유의여부: " + (t_test.tTest(criterion, sample, 0.05) ? "*" : "유의하지 않음"));
    }
}