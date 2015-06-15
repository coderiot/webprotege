package fu.berlin.csw.dl_learner.server;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.dl_learner.shared.GetSuggestionsAction;
import fu.berlin.csw.dl_learner.shared.GetSuggestionsResult;


import java.util.*;

import org.glassfish.tyrus.server.Server;


/**
 * Created by lars on 29.04.15.
 */
public class GetSuggestionsActionHandler extends AbstractHasProjectActionHandler<GetSuggestionsAction, GetSuggestionsResult> {

    private Timer timer;
    private ProjectId projectId;
    public WebProtegeLogger logger;
    private Server server;

    private static Set<DLLearnerAdapter> DLLearnerAdapterSet = new HashSet<>();


    @Inject
    public GetSuggestionsActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);

    }

    @Override
    public Class<GetSuggestionsAction> getActionClass() {
        return GetSuggestionsAction.class;
    }

    @Override
    protected RequestValidator<GetSuggestionsAction> getAdditionalRequestValidator(GetSuggestionsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected GetSuggestionsResult execute(GetSuggestionsAction action, OWLAPIProject project, ExecutionContext executionContext) {

        String resultMessage = null;
        Manager manager = Manager.getInstance();

        try{
            manager.startLearnProcess(project, action.getSelectedEntity());
        } catch (Exception e){
            e.printStackTrace();
            resultMessage = e.getMessage();
        }

        //server.stop();

        return new GetSuggestionsResult(resultMessage);

    }



}
