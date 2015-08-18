package fu.berlin.csw.dl_learner.server;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import fu.berlin.csw.dl_learner.shared.CancelLearningAction;
import fu.berlin.csw.dl_learner.shared.CancelLearningResult;

/**
 * Created by lars on 18.08.15.
 */
public class CancelLearningActionHandler extends AbstractHasProjectActionHandler<CancelLearningAction, CancelLearningResult> {

    @Inject
    public CancelLearningActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);

    }
    @Override
    protected RequestValidator<CancelLearningAction> getAdditionalRequestValidator(CancelLearningAction action, RequestContext requestContext) {
        return NullValidator.get();

    }

    @Override
    protected CancelLearningResult execute(CancelLearningAction action, OWLAPIProject project, ExecutionContext executionContext) {
        ClassDescriptionLearner learner = Manager.getInstance().getProjectRelatedLearner(project.getProjectId(), executionContext.getUserId());

        learner.cancelLearning();

        String resultMessage = "[DLLearner] Learning successful canceled";

        return new CancelLearningResult(resultMessage);
    }

    @Override
    public Class<CancelLearningAction> getActionClass() {
        return CancelLearningAction.class;
    }
}
