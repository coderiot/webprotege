package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Created by lars on 27.07.15.
 */
public class AddEquivalentClassExecutor {
    private DispatchServiceManager dispatchServiceManager;


    @Inject
    public AddEquivalentClassExecutor(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void execute(ProjectId projectId, OWLEntity selectedEntity, int classExpressionId,DispatchServiceCallback<AddEquivalentClassResult> callback) {
        dispatchServiceManager.execute(new AddEquivalentClassAction(projectId , selectedEntity, classExpressionId), callback);
    }
}