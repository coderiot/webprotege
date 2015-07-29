package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Created by lars on 27.07.15.
 */
public class AddEquivalentClassResult implements Result {

    private String message;
    //private PaginationData<EvaluatedDescriptionClass> paginationData;

    /**
     * For serialization only
     */
    private AddEquivalentClassResult() {
    }

    public AddEquivalentClassResult(String message){//, PaginationData<EvaluatedDescriptionClass> paginationData) {
        //this.paginationData = paginationData;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    //public PaginationData<EvaluatedDescription> getPaginationData() {
    //    return this.paginationData;
    //}

}
