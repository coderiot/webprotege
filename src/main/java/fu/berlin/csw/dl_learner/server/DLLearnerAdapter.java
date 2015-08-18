package fu.berlin.csw.dl_learner.server;

/**
 * Created by lars on 29.04.15.
 */

import com.clarkparsia.owlapiv3.OWL;;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.dllearner.algorithms.celoe.CELOE;
import org.dllearner.core.AbstractReasonerComponent;
import org.dllearner.core.EvaluatedDescription;
import org.dllearner.core.KnowledgeSource;
import org.dllearner.kb.OWLAPIOntology;
import org.dllearner.kb.SparqlEndpointKS;
import org.dllearner.kb.sparql.SparqlEndpoint;
import org.dllearner.learningproblems.ClassLearningProblem;
import org.dllearner.learningproblems.EvaluatedDescriptionClass;
import org.dllearner.reasoning.ClosedWorldReasoner;
import org.dllearner.reasoning.OWLAPIReasoner;
import org.dllearner.reasoning.SPARQLReasoner;
import org.dllearner.refinementoperators.RhoDRDown;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLEquivalentClassesAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLSubClassOfAxiomImpl;


import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

import static org.semanticweb.owlapi.model.AxiomType.EQUIVALENT_CLASSES;

public class DLLearnerAdapter implements ClassDescriptionLearner {//implements ManagerService {


    private KnowledgeSource ks;
    private ClassLearningProblem lp;
    private CELOE la;
    private AbstractReasonerComponent reasoner;   // SparqlReasoner ???
    private OWLAPIProject project;
    private UserId userId;
    private OWLEntity selEntity;
    private AxiomType axiomType;
    private static WebProtegeLogger logger = WebProtegeInjector.get().getInstance(WebProtegeLogger.class);
    private List<EvaluatedDescriptionClass> bestEvaluatedDescriptions;
    private int maxNrOfResults;
    private boolean cancelled = false;

    private Set<OWLClassExpression> suggestedClassExpression;


    public DLLearnerAdapter(OWLAPIProject project, UserId userId){
        this.userId = userId;
        this.project = project;
    }


    @Override
    public synchronized void initLearningProblem() throws Exception {
        cancelled = false;

        bestEvaluatedDescriptions = new LinkedList<EvaluatedDescriptionClass>();

        logger.info("[DLLearner] init learning problem...");
        long startTime = System.currentTimeMillis();

        lp = new ClassLearningProblem(reasoner);

        logger.info(selEntity.getEntityType().toString());

        lp.setClassToDescribe(selEntity.asOWLClass());


        lp.setEquivalence(axiomType.equals(EQUIVALENT_CLASSES));


        lp.init();

        logger.info("[DLLearner] Initialisation of learning problem done in " + (System.currentTimeMillis() - startTime) + "ms.");
    }


    @Override
    public synchronized void initLearningAlgorithm(boolean useAllConstructor, boolean useExistConstructor, boolean useHasValueConstructor
            , boolean useCardinalityLimit, int cardinalityLimit, int maxExecutionTime, int noisePercentage, int maxNumberOfResults, boolean useNegation) throws Exception {
        try {
            logger.info("[DLLearner] init learning algorithm...");
            long startTime = System.currentTimeMillis();
            la = new CELOE(lp, reasoner);

            this.maxNrOfResults = maxNumberOfResults;

            RhoDRDown op = new RhoDRDown();

            op.setReasoner(reasoner);
            op.setUseNegation(useNegation);
            op.setUseAllConstructor(useAllConstructor);
            op.setUseCardinalityRestrictions(useCardinalityLimit);
            if(useCardinalityLimit){
                op.setCardinalityLimit(cardinalityLimit);
            }
            op.setUseExistsConstructor(useExistConstructor);
            op.setUseHasValueConstructor(useHasValueConstructor);
            op.init();

            la.setOperator(op);

            la.setMaxExecutionTimeInSeconds(maxExecutionTime);//maxExecutionTimeInSeconds);
            la.setNoisePercentage(noisePercentage);//noisePercentage);
            la.setMaxNrOfResults(maxNumberOfResults);//maxNrOfResults);

            la.init();
            logger.info("[DLLearner] Initialisation of learning algorithm done in " + (System.currentTimeMillis()-startTime) + "ms.");
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    @Override
    public synchronized void initReasoner(ReasonerType reasonerType) throws Exception{

        if(reasonerType.equals(ReasonerType.HERMIT_REASONER)){
            initHermitReasoner();
        } else if(reasonerType.equals(ReasonerType.SPARQL_REASONER)){
            initSparqlReasoner();
        } else if(reasonerType.equals((ReasonerType.PELLET_REASONER))){
            initPelletReasoner();
        }

    }


    private void initSparqlReasoner() throws Exception{
        logger.info("[DLLearner] init SparqlReasoner...");
        long startTime = System.currentTimeMillis();

        try {


            reasoner = new SPARQLReasoner((SparqlEndpointKS)ks);
            reasoner.init();

            reasoner.prepareSubsumptionHierarchy();

            reasoner.getClasses().contains(selEntity.asOWLClass());


        } catch (Error e){
            e.printStackTrace();
        }

        logger.info("[DLLearner] Sparql Reasoner Initialisation done in " + (System.currentTimeMillis() - startTime) + "ms.");
    }


    private void initHermitReasoner() throws Exception{
        logger.info("[DLLearner] init Hermit Reasoner...");
        long startTime = System.currentTimeMillis();

        Reasoner hermit = new Reasoner(project.getRootOntology());

        OWLAPIReasoner baseReasoner = new OWLAPIReasoner(hermit);
        baseReasoner.init();

        // closed world reasoner
        reasoner = new ClosedWorldReasoner(Collections.singleton(ks));
        ((ClosedWorldReasoner)reasoner).setReasonerComponent(baseReasoner);

        reasoner.init();

        logger.info("[DLLearner] Hermit Reasoner Initialisation done in " + (System.currentTimeMillis() - startTime) + "ms.");
    }


    private void initPelletReasoner() throws Exception{
        logger.info("[DLLearner] init Pellet Reasoner...");
        long startTime = System.currentTimeMillis();

        OWLReasonerFactory pelletFactory = com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory.getInstance();

        OWLReasoner pellet = pelletFactory.createReasoner(project.getRootOntology());

        OWLAPIReasoner baseReasoner = new OWLAPIReasoner(pellet);

        baseReasoner.init();

        // closed world reasoner
        reasoner = new ClosedWorldReasoner(Collections.singleton(ks));
        ((ClosedWorldReasoner)reasoner).setReasonerComponent(baseReasoner);

        //reasoner.setProgressMonitor(progressMonitor);TODO integrate progress monitor
        reasoner.init();

        logger.info("[DLLearner] Hermit Reasoner Initialisation done in " + (System.currentTimeMillis() - startTime) + "ms.");

    }



    @Override
    public void initKnowledgeSource(ReasonerType reasonerType, String sparqlEndpoint) throws Exception{

        // TODO

        if (reasonerType.equals(ReasonerType.HERMIT_REASONER)){
            initOWLKnowledgeSoure();
        } else if(reasonerType.equals(ReasonerType.SPARQL_REASONER)){
            initSparqlKnowledgeSource(sparqlEndpoint);
        }

    }

    private void initSparqlKnowledgeSource(String sparqlEndpoint) throws Exception{
        logger.info("[DLLearner] init sparql knowledge source...");

        SparqlEndpoint ep = new SparqlEndpoint(new URL(sparqlEndpoint));

        ks = new SparqlEndpointKS(ep);

        ((SparqlEndpointKS)ks).setUseCache(false);

        ks.init();
        logger.info("[DLLearner] initialisation of sparql knowledge source done");
    }

    private void initOWLKnowledgeSoure() throws Exception{
        logger.info("[DLLearner] init OWL knowledge source...");

        ks = new OWLAPIOntology(project.getRootOntology());
        ks.init();

        logger.info("[DLLearner] initialisation of OWL knowledge source done");
    }


    @Override
    public synchronized void setAxiomType(AxiomType axiomType) {
        this.axiomType = axiomType;
    }

    @Override
    public synchronized void setEntity(OWLEntity selEntity) {
        this.selEntity = selEntity;
    }

    @Override
    public boolean isLearning(){
        return la != null && la.isRunning();
    }

    @Override
    public List<EvaluatedDescriptionClass> getCurrentlyLearnedDescriptions() {
        List<EvaluatedDescriptionClass> result;
        if (la != null) {
            result = Collections.unmodifiableList((List<EvaluatedDescriptionClass>) la
                    .getCurrentlyBestEvaluatedDescriptions(maxNrOfResults, 0.5, true));
        } else {
            result = Collections.emptyList();
        }
        return result;
    }

    @Override
    public synchronized void startLearning(){

        logger.info("[DLLearner] started learning of " +
                axiomType.getName() + " axioms");

        la.start();

        logger.info("[DLLearner] finished learning of " +
                axiomType.getName() + " axioms");

        this.bestEvaluatedDescriptions = getCurrentlyLearnedDescriptions();




    }


    @Override
    public int hashCode(){
       return this.project.getProjectId().hashCode() * userId.hashCode();
    }

    public void addSuggestedClassExpression(OWLClassExpression classExpression){
        if (suggestedClassExpression == null){
            suggestedClassExpression = new HashSet<>();
        }
        suggestedClassExpression.add(classExpression);
    }

    @Override
    public void addLearnedDescriptionToProject(int classExpressionId){

        OWLClassExpression subequivclass = null;
        OWLAxiom axiomToAdd = null;

        /*ToDo id instead of hashcode*/
        for (EvaluatedDescriptionClass evaluatedDescr : bestEvaluatedDescriptions){
            if (classExpressionId == evaluatedDescr.hashCode()){
                subequivclass = evaluatedDescr.getDescription();
            }
        }

        if (this.axiomType == AxiomType.EQUIVALENT_CLASSES){
            Set<OWLClassExpression> exprSet = new HashSet<>();
            exprSet.add(subequivclass);
            axiomToAdd = new OWLEquivalentClassesAxiomImpl(exprSet, new HashSet<OWLAnnotation>());
        } else {
            axiomToAdd = new OWLSubClassOfAxiomImpl(selEntity.asOWLClass(), subequivclass, new HashSet<OWLAnnotation>());

        }

        OWLOntologyChange ontChange = new AddAxiom(project.getRootOntology(), axiomToAdd);

        List<OWLOntologyChange> changeSet = new LinkedList<>();
        changeSet.add(ontChange);

        project.applyChanges(userId, FixedChangeListGenerator.get(changeSet), FixedMessageChangeDescriptionGenerator.get("added by DLLearner"));
    }

    @Override
    public void cancelLearning(){
        la.stop();
        cancelled = true;
    }

    @Override
    public boolean isCancelled(){
        return cancelled;
    }


}
