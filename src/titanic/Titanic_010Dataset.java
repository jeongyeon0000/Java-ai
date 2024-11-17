package titanic;


import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class Titanic_010Dataset {

    private Instances data = null;
    private Instances titanic_co_reorder = null;
    private Instances titanic_co_reorder_norm = null;
    private Instances titanic_co_reorder_stan = null;
    private Classifier model = null;

    public static void main(String args[]) {
        try {
            // Titanic_010Dataset 객체 생성 및 실행
            Titanic_010Dataset obj = new Titanic_010Dataset();
            obj.openArffonWeb();
            obj.preprocess();
            obj.experiment();
            obj.classify();
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 에러 출력
        }
    }

    // 1) 데이터 로드
    public void openArffonWeb() throws Exception {
        ArffLoader loader = new ArffLoader();
        loader.setURL("https://www.openml.org/data/download/16826755/phpMYEkMl.arff");
        this.data = loader.getDataSet();
        System.out.println("데이터 로드 완료: " + data.size());
    }

    // 2) 데이터 전처리 (순서 변경, 정규화, 표준화)
    public void preprocess() throws Exception {
        Titanic_020Preprocess preprocess = new Titanic_020Preprocess(this.data);
        preprocess.filter(); // 필터 적용

        // 전처리 결과 저장
        this.titanic_co_reorder = preprocess.getReorder();
        this.titanic_co_reorder_norm = preprocess.getNormalize(); // 정규화 데이터
        this.titanic_co_reorder_stan = preprocess.getStandardize(); // 표준화 데이터
    }

    // 3) 모델 실험/학습/평가
    public void experiment() throws Exception {
        Titanic_030Experiment experiment = new Titanic_030Experiment();
        this.model = experiment.experimentModel(this.titanic_co_reorder_stan);
    }

    // 4) 모델 예측
    public void classify() throws Exception {
        Titanic_04Classify classify = new Titanic_04Classify(this.model);

        System.out.println("**********************");
        System.out.println("순서 변경된 데이터로 예측");
        System.out.println("**********************");
        classify.useModel(this.titanic_co_reorder);

        System.out.println("**********************");
        System.out.println("정규화된 데이터로 예측");
        System.out.println("**********************");
        classify.useModel(this.titanic_co_reorder_norm);

        // 추가: 표준화된 데이터로 예측
        System.out.println("**********************");
        System.out.println("표준화된 데이터로 예측");
        System.out.println("**********************");
        classify.useModel(this.titanic_co_reorder_stan);
    }
}