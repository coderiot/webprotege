package fu.berlin.csw.dl_learner.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by lars on 04.06.15.
 */
public class ServerReply implements Serializable {


    private static final long serialVersionUID = -2974784212373773166L;
    private String classExpressionManchesterString;
    private String username;
    private Date time;
    private String accuracy;
    private int classExpressionId;
    private String context;

    private List<Suggestion> suggestionsList;


    private Throwable throwable;

    public String getClassExpressionManchesterString() {
        return classExpressionManchesterString;
    }

    public void setClassExpressionManchesterString(String classExpressionManchesterString) {
        this.classExpressionManchesterString = classExpressionManchesterString;
    }

    public void setSuggestionsList(List<Suggestion> suggestionsList){
        this.suggestionsList = suggestionsList;
    }

    public List<Suggestion> getSuggestionsList(){
        return this.suggestionsList;
    }



    public void setClassExpressionId(int classExpressionId){
        this.classExpressionId = classExpressionId;
    }

    public int getClassExpressionId(){
        return this.classExpressionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccuracy(String accuracy){
        this.accuracy = accuracy;
    }

    public String getAccuracy(){
        return this.accuracy;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setThrowable(Throwable throwable){
        this.throwable = throwable;
    };

    public Throwable getThrowable(){
        return this.throwable;
    };

    @Override
    public String toString(){
        return this.classExpressionManchesterString;
    }


    public void setContext(String context){
        this.context = context;
    }

    public String getContext(){
        return this.context;
    }

}
