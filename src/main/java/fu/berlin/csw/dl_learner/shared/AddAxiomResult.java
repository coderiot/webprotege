package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Created by lars on 27.07.15.
 */
public class AddAxiomResult implements Result {

    private String message;

    /**
     * For serialization only
     */
    private AddAxiomResult() {
    }

    public AddAxiomResult(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
