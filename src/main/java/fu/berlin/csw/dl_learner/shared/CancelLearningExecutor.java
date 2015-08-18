package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Created by lars on 18.08.15.
 */
public class CancelLearningExecutor {

    private DispatchServiceManager dispatchServiceManager;


    @Inject
    public CancelLearningExecutor(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void execute(ProjectId projectId ,DispatchServiceCallback<CancelLearningResult> callback) {
        dispatchServiceManager.execute(new CancelLearningAction(projectId), callback);
    }
}