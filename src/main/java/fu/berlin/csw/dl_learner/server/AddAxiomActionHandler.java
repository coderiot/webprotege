package fu.berlin.csw.dl_learner.server;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import fu.berlin.csw.dl_learner.shared.AddAxiomAction;
import fu.berlin.csw.dl_learner.shared.AddAxiomResult;

/**
 * Created by lars on 27.07.15.
 */
public class AddAxiomActionHandler extends AbstractHasProjectActionHandler<AddAxiomAction, AddAxiomResult> {

    @Inject
    public AddAxiomActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);

    }
    @Override
    protected RequestValidator<AddAxiomAction> getAdditionalRequestValidator(AddAxiomAction action, RequestContext requestContext) {
        return NullValidator.get();

    }

    @Override
    protected AddAxiomResult execute(AddAxiomAction action, OWLAPIProject project, ExecutionContext executionContext) {
        String resultMessage = "class expression succesful added";
        ClassDescriptionLearner learner = Manager.getInstance().getProjectRelatedLearner(project.getProjectId(), executionContext.getUserId());

        learner.addLearnedDescriptionToProject(action.getClassExpressionId());

        return new AddAxiomResult(resultMessage);
    }

    @Override
    public Class<AddAxiomAction> getActionClass() {
        return AddAxiomAction.class;
    }
}
