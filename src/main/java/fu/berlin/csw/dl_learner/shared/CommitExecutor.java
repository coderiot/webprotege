package fu.berlin.csw.dl_learner.shared;

/**
 * Created by lars on 29.04.15.
 */
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pierre on 09.04.15.
 */
public class CommitExecutor {

    private DispatchServiceManager dispatchServiceManager;


    @Inject
    public CommitExecutor(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void execute(ProjectId projectId, OWLEntity selectedEntity, DispatchServiceCallback<CommitResult> callback) {
        dispatchServiceManager.execute(new CommitAction(projectId , selectedEntity), callback);
    }
}