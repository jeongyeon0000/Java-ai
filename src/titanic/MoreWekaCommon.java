package titanic;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.CorrelationAttributeEval;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.OneRAttributeEval;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.rules.OneR;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.instance.imagefilter.*;

public class MoreWekaCommon {
    public final static String modelPath = "C:\\data\\model\\";
    public final static String arffPath = "C:\\data\\";
    public final static String imgPath = "C:\\data\\image";

    public static void main(String[] args) {
        // 메인 메소드 (현재는 사용하지 않음)
    }

    /*****************************
     * 모델 이름 반환
     *****************************/
    public static String getModelName(Classifier model) {
        if (model instanceof Logistic)
            return "Logistic";
        else if (model instanceof J48)
            return "J48";
        else if (model instanceof IBk)
            return "IBk";
        else if (model instanceof SMO)
            return "SMO";
        else if (model instanceof OneR)
            return "OneR";
        else if (model instanceof NaiveBayes)
            return "NaiveBayes";
        else if (model instanceof DecisionStump)
            return "DecisionStump";
        else if (model instanceof ZeroR)
            return "ZeroR";
        else if (model instanceof FilteredClassifier)
            return "FilteredClassifier";
        else if (model instanceof NaiveBayesMultinomial)
            return "NaiveBayesMultinomial";

        return "UnknownModel";
    }

    /*****************************
     * 특성 선택 알고리즘 이름 반환
     *****************************/
    public static String getFeatureAlgorithmName(ASEvaluation attrSel) {
        if (attrSel instanceof WrapperSubsetEval)
            return "WrapperSubsetEval";
        else if (attrSel instanceof CorrelationAttributeEval)
            return "CorrelationAttributeEval";
        else if (attrSel instanceof GainRatioAttributeEval)
            return "GainRatioAttributeEval";
        else if (attrSel instanceof InfoGainAttributeEval)
            return "InfoGainAttributeEval";
        else if (attrSel instanceof OneRAttributeEval)
            return "OneRAttributeEval";

        return "UnknownFeatureSelection";
    }

    public static String getImageFilterName(AbstractImageFilter imageFilter) {
        if (imageFilter instanceof ColorLayoutFilter)
            return "ColorLayoutFilter";
        else if (imageFilter instanceof EdgeHistogramFilter)
            return "EdgeHistogramFilter";
        else if (imageFilter instanceof SimpleColorHistogramFilter)
            return "SimpleColorHistogramFilter";

        return "UnknownImageFilter";
    }

    /*****************************
     * URL 또는 경로에서 데이터 로드
     *****************************/
    public static Instances loadData(String pathOrUrl) throws Exception {
        DataSource source = new DataSource(pathOrUrl);
        Instances data = source.getDataSet();

        // 데이터의 클래스 속성을 마지막 속성으로 설정
        if (data.classIndex() == -1) {
            data.setClassIndex(data.numAttributes() - 1);
        }

        return data;
    }
}