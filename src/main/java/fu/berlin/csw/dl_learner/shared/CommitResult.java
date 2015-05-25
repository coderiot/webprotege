package fu.berlin.csw.dl_learner.shared;

/**
 * Created by lars on 29.04.15.
 */

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by pierre on 09.04.15.
 */
public class CommitResult implements Result {
    private String message;

    /**
     * For serialization only
     */
    private CommitResult() {
    }

    public CommitResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}