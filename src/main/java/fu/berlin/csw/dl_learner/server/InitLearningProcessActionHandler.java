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
import fu.berlin.csw.dl_learner.shared.InitLearningProcessAction;
import fu.berlin.csw.dl_learner.shared.InitLearningProcessException;
import fu.berlin.csw.dl_learner.shared.InitLearningProcessResult;


import java.util.*;

import org.glassfish.tyrus.server.Server;


/**
 * Created by lars on 29.04.15.
 */
public class InitLearningProcessActionHandler extends AbstractHasProjectActionHandler<InitLearningProcessAction, InitLearningProcessResult> {

    private Timer timer;
    private ProjectId projectId;
    public WebProtegeLogger logger;
    private Server server;

    private static Set<DLLearnerAdapter> DLLearnerAdapterSet = new HashSet<>();


    @Inject
    public InitLearningProcessActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);

    }

    @Override
    public Class<InitLearningProcessAction> getActionClass() {
        return InitLearningProcessAction.class;
    }

    @Override
    protected RequestValidator<InitLearningProcessAction> getAdditionalRequestValidator(InitLearningProcessAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected InitLearningProcessResult execute(InitLearningProcessAction action, OWLAPIProject project, ExecutionContext executionContext) {

        String resultMessage = "Learning process successful initialised";
        Manager manager = Manager.getInstance();

        //try{
        manager.initLearnProcess(project, action.getSelectedEntity());
        //} catch (Exception e){
        //    e.printStackTrace();
        //    throw new InitLearningProcessException(e.getMessage());
        //}

        //server.stop();

        /*for (EvaluatedDescriptionClass descr : Manager.getProjectRelatedLearner(projectId).getCurrentlyLearnedDescriptions()){
            logger.info("Description " + descr.toString());
        }*/

        return new InitLearningProcessResult(resultMessage);

    }



}