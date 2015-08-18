package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Created by lars on 18.08.15.
 */
public class CancelLearningResult implements Result {

    private String message;

    /**
     * For serialization only
     */
    private CancelLearningResult() {
    }

    public CancelLearningResult(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }



}

