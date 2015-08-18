package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Created by lars on 18.08.15.
 */
public class CancelLearningAction extends AbstractHasProjectAction<CancelLearningResult> {

    private OWLEntity selectedEntity;
    private int classExpressionId;

    /**
     * For Serialization purposes only
     */
    private CancelLearningAction() {
    }

    public CancelLearningAction(ProjectId projectId) {
        super(projectId);
    }

}
