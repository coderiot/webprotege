package fu.berlin.csw.dl_learner.server;

import org.dllearner.learningproblems.EvaluatedDescriptionClass;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.List;

/**
 * Created by lars on 11.06.15.
 */
public interface ClassDescriptionLearner {

    public void startLearning();

    public List<EvaluatedDescriptionClass> getCurrentlyLearnedDescriptions();

    public boolean isLearning();

    public void setEntity(OWLEntity selEntity);

    public void setAxiomType(AxiomType axiomType);

    public void initKnowledgeSource(ReasonerType reasonerType) throws Exception;

    public void initReasoner(ReasonerType reasonerType) throws Exception;

    public void initLearningAlgorithm() throws Exception;

    public void initLearningProblem() throws Exception;

    public void init(ReasonerType reasonerType) throws Exception;

    public boolean isActive();


}
