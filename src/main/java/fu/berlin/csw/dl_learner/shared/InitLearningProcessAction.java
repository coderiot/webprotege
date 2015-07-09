package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by pierre on 09.04.15.
 */
public class InitLearningProcessAction extends AbstractHasProjectAction<InitLearningProcessResult> {


    private OWLEntity selectedEntity;

    /**
     * For Serialization purposes only
     */
    private InitLearningProcessAction() {
    }

    public InitLearningProcessAction(ProjectId projectId, OWLEntity selectedEntity) {
        super(projectId);
        this.selectedEntity = selectedEntity;
    }

   public OWLEntity getSelectedEntity(){
        return this.selectedEntity;
    }

}
