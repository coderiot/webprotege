package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by pierre on 09.04.15.
 */
public class InitLearningProcessAction extends AbstractHasProjectAction<InitLearningProcessResult> {


    private OWLEntity selectedEntity;
    private String sparqlEndpoint;
    private AxiomType axiomType;

    private int maxExecutionTimeInSeconds;
    private int maxNrOfResults;
    private int noisePercentage;
    private int cardinalityLimit;
    private boolean useAllConstructor;
    private boolean useNegation;
    private boolean useCardinalityRestrictions;
    private boolean useExistsConstructor;
    private boolean useHasValueConstructor;

    /**
     * For Serialization purposes only
     */
    private InitLearningProcessAction() {
    }

    public InitLearningProcessAction(ProjectId projectId, OWLEntity selectedEntity, String sparqlEndpoint, AxiomType axiomType, int maxExecutionTimeInSeconds,
                                     int maxNrOfResults, int noisePercentage, int cardinalityLimit, boolean useAllConstructor, boolean useNegation,
                                     boolean useCardinalityRestrictions, boolean useExistsConstructor, boolean useHasValueConstructor) {
        super(projectId);
        this.selectedEntity = selectedEntity;
        this.sparqlEndpoint = sparqlEndpoint;
        this.axiomType = axiomType;
        this.maxExecutionTimeInSeconds = maxExecutionTimeInSeconds;
        this.maxNrOfResults = maxNrOfResults;
        this.noisePercentage= noisePercentage;
        this.cardinalityLimit = cardinalityLimit;
        this.useAllConstructor=useAllConstructor;
        this.useNegation = useNegation;
        this.useCardinalityRestrictions = useCardinalityRestrictions;
        this.useExistsConstructor = useExistsConstructor;
        this.useHasValueConstructor = useHasValueConstructor;
    }

    public OWLEntity getSelectedEntity(){
        return this.selectedEntity;
    }

    public String getSparqlEndpoint(){
        return this.sparqlEndpoint;
    }

    public AxiomType getAxiomType(){
        return this.axiomType;
    }

    public int getMaxExecutionTimeInSeconds(){
        return this.maxExecutionTimeInSeconds;
    }

    public int getMaxNumberOfResults(){
        return this.maxNrOfResults;
    }

    public int getCardinalityLimit(){
        return this.cardinalityLimit;
    }

    public boolean getUseCardinalityRestrictions(){
        return this.useCardinalityRestrictions;
    }

    public int getNoisePercentage(){
        return this.noisePercentage;
    }

    public int getMaxNrOfResults(){
        return this.maxNrOfResults;
    }

    public boolean getUseNegation(){
        return this.useNegation;
    }

    public boolean getUseAllConstructor(){
        return this.useAllConstructor;
    }
    public boolean getUseExistsConstructor(){
        return this.useExistsConstructor;
    }
    public boolean getUseHasValueConstructor(){
        return this.useHasValueConstructor;
    }

}
