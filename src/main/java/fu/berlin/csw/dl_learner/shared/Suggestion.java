package fu.berlin.csw.dl_learner.shared;

import java.io.Serializable;

/**
 * Created by lars on 26.08.15.
 */
public class Suggestion implements Serializable{

    private String classExpressionManchesterString;

    private double accuracy;

    private int id;

    public Suggestion(){

    }

    public Suggestion(String classExpressionManchesterString, double accuracy){
        this.accuracy=accuracy;
        this.classExpressionManchesterString=classExpressionManchesterString;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public String getClassExpressionManchesterString(){
        return this.classExpressionManchesterString;
    }

    public String getAccuracyAsString(){
        return ((new Double(accuracy)).toString());
    }

}
