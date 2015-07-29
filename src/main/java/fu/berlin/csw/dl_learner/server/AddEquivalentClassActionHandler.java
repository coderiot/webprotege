package fu.berlin.csw.dl_learner.server;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import fu.berlin.csw.dl_learner.shared.AddEquivalentClassAction;
import fu.berlin.csw.dl_learner.shared.AddEquivalentClassResult;
import fu.berlin.csw.dl_learner.shared.InitLearningProcessAction;
import fu.berlin.csw.dl_learner.shared.InitLearningProcessResult;

/**
 * Created by lars on 27.07.15.
 */
public class AddEquivalentClassActionHandler extends AbstractHasProjectActionHandler<AddEquivalentClassAction, AddEquivalentClassResult> {

    @Inject
    public AddEquivalentClassActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);

    }
    @Override
    protected RequestValidator<AddEquivalentClassAction> getAdditionalRequestValidator(AddEquivalentClassAction action, RequestContext requestContext) {
        return NullValidator.get();

    }

    @Override
    protected AddEquivalentClassResult execute(AddEquivalentClassAction action, OWLAPIProject project, ExecutionContext executionContext) {
        String resultMessage = "class expression succesful added";
        ClassDescriptionLearner learner = Manager.getInstance().getProjectRelatedLearner(project.getProjectId(), executionContext.getUserId());

        learner.addLearnedDescriptionToProject(action.getClassExpressionId());

        return new AddEquivalentClassResult(resultMessage);
    }

    @Override
    public Class<AddEquivalentClassAction> getActionClass() {
        return AddEquivalentClassAction.class;
    }
}
