package fu.berlin.csw.dl_learner.shared;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by pierre on 09.04.15.
 */
public class CommitAction extends AbstractHasProjectAction<CommitResult> {


    private OWLEntity selectedEntity;

    /**
     * For Serialization purposes only
     */
    private CommitAction() {
    }

    public CommitAction(ProjectId projectId , OWLEntity selectedEntity ) {
        super(projectId);
        this.selectedEntity = selectedEntity;
    }

   public OWLEntity getSelectedEntity(){
        return this.selectedEntity;
    }

}
