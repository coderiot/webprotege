package fu.berlin.csw.dl_learner.shared;

/**
 * Created by lars on 29.04.15.
 */
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Created by pierre on 09.04.15.
 */
public class InitLearningProcessExecutor {

    private DispatchServiceManager dispatchServiceManager;


    @Inject
    public InitLearningProcessExecutor(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void execute(ProjectId projectId, OWLEntity selectedEntity, String sparqlEndpoint, AxiomType axiomType, int maxExecutionTimeInSeconds,
                        int maxNrOfResults, int noisePercentage, int cardinalityLimit, boolean useAllConstructor, boolean useNegation,
                        boolean useCardinalityRestrictions, boolean useExistsConstructor, boolean useHasValueConstructor,
                        DispatchServiceCallback<InitLearningProcessResult> callback) {
        dispatchServiceManager.execute(new InitLearningProcessAction(projectId , selectedEntity, sparqlEndpoint, axiomType, maxExecutionTimeInSeconds, maxNrOfResults,
                noisePercentage, cardinalityLimit, useAllConstructor, useNegation, useCardinalityRestrictions, useExistsConstructor, useHasValueConstructor), callback);
    }
}