package fu.berlin.csw.dl_learner.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lars on 04.06.15.
 */
public class ServerReply implements Serializable {


    private static final long serialVersionUID = -2974784212373773166L;
    private String data;
    private String username;
    private Date time;
    private String accuracy;

    private Throwable throwable;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        return this.data;
    }

}
