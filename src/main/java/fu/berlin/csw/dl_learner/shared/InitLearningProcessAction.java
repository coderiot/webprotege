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
    private boolean useSparqlEndpoint;
    private String sparqlEndpoint;

    /**
     * For Serialization purposes only
     */
    private InitLearningProcessAction() {
    }

    public InitLearningProcessAction(ProjectId projectId, OWLEntity selectedEntity, boolean useSparqlEndpoint, String sparqlEndpoint) {
        super(projectId);
        this.selectedEntity = selectedEntity;
        this.useSparqlEndpoint = useSparqlEndpoint;
        this.sparqlEndpoint = sparqlEndpoint;
    }

    public OWLEntity getSelectedEntity(){
        return this.selectedEntity;
    }

    public String getSparqlEndpoint(){
        return this.sparqlEndpoint;
    }

    public boolean getUseSparqlEndpoint(){
        return this.useSparqlEndpoint;
    }

}
