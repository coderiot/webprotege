package fu.berlin.csw.dl_learner.shared;

/**
 * Created by lars on 29.04.15.
 */
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Created by pierre on 09.04.15.
 */
public class GetSuggestionsExecutor {

    private DispatchServiceManager dispatchServiceManager;


    @Inject
    public GetSuggestionsExecutor(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void execute(ProjectId projectId, OWLEntity selectedEntity, DispatchServiceCallback<GetSuggestionsResult> callback) {
        dispatchServiceManager.execute(new GetSuggestionsAction(projectId , selectedEntity), callback);
    }
}