package fu.berlin.csw.AspectPortlet.client.ui;

/**
 * Created by lars on 05.02.15.
 */
import java.io.Serializable;
/**
 * Author: Lars Parmakerli<br>
 * Freie Universität Berlin<br>
 * corporate semantic web<br>
 * Date: 14/08/2014
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    public Message(){};
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}