package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Created by lars on 27.07.15.
 */
public class AddAxiomAction extends AbstractHasProjectAction<AddAxiomResult> {

    private OWLEntity selectedEntity;
    private int classExpressionId;
    private AxiomType axiomType;

    /**
     * For Serialization purposes only
     */
    private AddAxiomAction() {
    }

    public AddAxiomAction(ProjectId projectId, OWLEntity selectedEntity, int classExpressionId) {
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
