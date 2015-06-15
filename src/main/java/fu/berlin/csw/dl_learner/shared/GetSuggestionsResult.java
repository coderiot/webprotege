package fu.berlin.csw.dl_learner.shared;

/**
 * Created by lars on 29.04.15.
 */

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.OWLClass;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by pierre on 09.04.15.
 */
public class GetSuggestionsResult implements Result {
    private String message;
    //private PaginationData<EvaluatedDescriptionClass> paginationData;

    /**
     * For serialization only
     */
    private GetSuggestionsResult() {
    }

    public GetSuggestionsResult(String message){//, PaginationData<EvaluatedDescriptionClass> paginationData) {
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