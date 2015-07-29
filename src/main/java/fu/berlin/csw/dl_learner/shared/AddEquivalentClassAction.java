package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Created by lars on 27.07.15.
 */
public class AddEquivalentClassAction extends AbstractHasProjectAction<AddEquivalentClassResult> {

    private OWLEntity selectedEntity;
    private int classExpressionId;

    /**
     * For Serialization purposes only
     */
    private AddEquivalentClassAction() {
    }

    public AddEquivalentClassAction(ProjectId projectId, OWLEntity selectedEntity, int classExpressionId) {
        super(projectId);
        this.selectedEntity = selectedEntity;
        this.classExpressionId = classExpressionId;
    }

    public OWLEntity getSelectedEntity(){
        return this.selectedEntity;
    }

    public int getClassExpressionId(){
        return this.classExpressionId;
    }
}
