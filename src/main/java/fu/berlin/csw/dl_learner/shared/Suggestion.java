package fu.berlin.csw.dl_learner.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lars on 04.06.15.
 */
public class Suggestion implements Serializable {


    private static final long serialVersionUID = -2974784212373773166L;
    private String data;
    private String username;
    private Date time;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString(){
        return this.data;
    }

}
