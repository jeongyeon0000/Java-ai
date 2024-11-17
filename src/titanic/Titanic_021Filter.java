package titanic;


import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.RenameAttribute;
import weka.filters.unsupervised.attribute.RenameNominalValues;
import weka.filters.unsupervised.attribute.Reorder;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToNominal;

public class Titanic_021Filter {

    private Instances data = null;

    public Titanic_021Filter(Instances data) {
        this.data = data;
    }

    public Instances action() throws Exception {
        this.renameAttributes(); // 속성 이름으로 변경
        this.numericToNominal("1"); // 숫자 속성을 범주형으로 변환
        this.renameValues(); // 속성값을 이름으로 변경
        this.remove("10,13,14"); // 속성을 33%로 축소 (연령, 객실번호 등의 속성 제거)
        this.reOrder("1,3-last,2"); // 속성 재정렬 (2번째 위치의 클래스 속성을 마지막으로 이동)
        this.stringToNominal("2,7,10"); // String을 범주형으로 변환
        return this.data;
    }

    public Instances normalize() throws Exception {
        Normalize normalize = new Normalize();
        runFilter(normalize);
        return this.data;
    }

    public Instances standardize() throws Exception {
        Standardize standardize = new Standardize();
        runFilter(standardize);
        return this.data;
    }

    /******************
     * 속성 이름 변경 메서드
     ******************/
    public void renameAttributes() throws Exception {
        this.setRenameAttributeFilter("1", "객실등급");
        this.setRenameAttributeFilter("2", "생존여부");
        this.setRenameAttributeFilter("3", "이름");
        this.setRenameAttributeFilter("4", "성별");
        this.setRenameAttributeFilter("5", "나이");
        this.setRenameAttributeFilter("6", "동반_형제자매_배우자수");
        this.setRenameAttributeFilter("7", "동반_부모_자녀수");
        this.setRenameAttributeFilter("8", "티켓번호");
        this.setRenameAttributeFilter("9", "요금");
        this.setRenameAttributeFilter("10", "객실번호");
        this.setRenameAttributeFilter("11", "탑승항구");
        this.setRenameAttributeFilter("12", "객실번호_수정");
        this.setRenameAttributeFilter("13", "출발지");
        this.setRenameAttributeFilter("14", "종료지");
    }

    public void setRenameAttributeFilter(String index, String replace) throws Exception {
        RenameAttribute renameAttr = new RenameAttribute();
        renameAttr.setAttributeIndices(index);
        renameAttr.setReplace(replace);
        this.runFilter(renameAttr);
    }

    public void renameValues() throws Exception {
        this.setRenameValueFilter("1", "1:1등급,2:2등급,3:3등급"); // NumericToNominal 변환 후 사용
        this.setRenameValueFilter("2", "1:생존,0:사망");
        this.setRenameValueFilter("4", "female:여성,male:남성");
    }

    public void setRenameValueFilter(String index, String replacement) throws Exception {
        RenameNominalValues renameValue = new RenameNominalValues();
        renameValue.setSelectedAttributes(index);
        renameValue.setValueReplacements(replacement);
        this.runFilter(renameValue);
    }

    public void numericToNominal(String index) throws Exception {
        NumericToNominal numToNom = new NumericToNominal();
        numToNom.setAttributeIndices(index);
        this.runFilter(numToNom);
    }

    public void stringToNominal(String index) throws Exception {
        StringToNominal strToNom = new StringToNominal();
        strToNom.setAttributeRange(index);
        this.runFilter(strToNom);
    }

    public void reOrder(String index) throws Exception {
        Reorder reorder = new Reorder();
        reorder.setAttributeIndices(index);
        this.runFilter(reorder);
    }

    public Instances remove(String index) throws Exception {
        Remove remove = new Remove();
        remove.setAttributeIndices(index);
        runFilter(remove);
        return this.data;
    }

    public Instances runFilter(Filter filter) throws Exception {
        filter.setInputFormat(data);
        data = Filter.useFilter(data, filter);
        return this.data;
    }

    public Instances getData() {
        return this.data;
    }
}