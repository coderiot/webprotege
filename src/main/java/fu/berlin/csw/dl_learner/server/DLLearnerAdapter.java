package fu.berlin.csw.dl_learner.server;

/**
 * Created by lars on 29.04.15.
 */

import static org.semanticweb.owlapi.model.AxiomType.EQUIVALENT_CLASSES;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.dllearner.algorithms.celoe.CELOE;
import org.dllearner.algorithms.el.ELLearningAlgorithm;
import org.dllearner.core.AbstractCELA;
import org.dllearner.core.AbstractReasonerComponent;
import org.dllearner.core.KnowledgeSource;
import org.dllearner.kb.OWLAPIOntology;
import org.dllearner.kb.SparqlEndpointKS;
import org.dllearner.kb.sparql.ClassBasedSampleGenerator;
import org.dllearner.kb.sparql.SparqlEndpoint;
import org.dllearner.learningproblems.ClassLearningProblem;
import org.dllearner.learningproblems.EvaluatedDescriptionClass;
import org.dllearner.reasoning.ClosedWorldReasoner;
import org.dllearner.reasoning.OWLAPIReasoner;
import org.dllearner.reasoning.ReasonerImplementation;
import org.dllearner.reasoning.SPARQLReasoner;
import org.dllearner.refinementoperators.RhoDRDown;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import uk.ac.manchester.cs.owl.owlapi.OWLEquivalentClassesAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLSubClassOfAxiomImpl;

import com.google.common.collect.Sets;

import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.user.UserId;

public class DLLearnerAdapter implements ClassDescriptionLearner {//implements ManagerService {
	
	// DBpedia specific constants
	// TODO: load from file, i.e. be more flexible
	private static final HashSet<String> allowedObjectNamespaces = Sets.newHashSet("http://dbpedia.org/ontology/", "http://dbpedia.org/resource/");
	private static final HashSet<String> allowedPropertyNameSpaces = Sets.newHashSet("http://dbpedia.org/ontology/");
	private static final Set<String> ignoredProperties = Sets.newHashSet(
			"http://dbpedia.org/ontology/wikiPageRevisionID",
			"http://dbpedia.org/ontology/wikiPageID",
			"http://dbpedia.org/ontology/abstract",
			"http://dbpedia.org/ontology/alias"
			,"http://dbpedia.org/ontology/number"
			,"http://dbpedia.org/ontology/endowment"
			);


    private KnowledgeSource ks;
    private ClassLearningProblem lp;
    private AbstractCELA la;
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
	private boolean useEL;


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

        logger.info("[DLLearner]" + selEntity.getEntityType().toString());

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
            if(useEL) {
            	ELLearningAlgorithm el = new ELLearningAlgorithm(lp, reasoner);
//        		el.setStartClass(cls);
        		el.setClassToDescribe(selEntity.asOWLClass());
        		el.setMaxExecutionTimeInSeconds(maxExecutionTime);
        		el.setNoisePercentage(noisePercentage);
        		el.setMaxNrOfResults(maxNumberOfResults);
        		el.init();
        		
        		la = el;
            } else {
            	CELOE celoe = new CELOE(lp, reasoner);
	
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
	
	            celoe.setOperator(op);
	            
	            celoe.setMaxExecutionTimeInSeconds(maxExecutionTime);//maxExecutionTimeInSeconds);
	            celoe.setNoisePercentage(noisePercentage);//noisePercentage);
	            celoe.setMaxNrOfResults(maxNumberOfResults);//maxNrOfResults);
	            
	            la = celoe;
            }

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
        logger.info("[DLLearner] init SPARQL reasoner...");
        long startTime = System.currentTimeMillis();

        try {
            reasoner = new SPARQLReasoner((SparqlEndpointKS)ks);
            reasoner.init();

            reasoner.prepareSubsumptionHierarchy();

            reasoner.getClasses().contains(selEntity.asOWLClass());


        } catch (Error e){
            e.printStackTrace();
        }

        logger.info("[DLLearner] SPARQL reasoner initialisation done in " + (System.currentTimeMillis() - startTime) + "ms.");
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

        logger.info("[DLLearner] Hermit reasoner initialisation done in " + (System.currentTimeMillis() - startTime) + "ms.");
    }


    private void initPelletReasoner() throws Exception{
        logger.info("[DLLearner] init Pellet Reasoner...");
        long startTime = System.currentTimeMillis();

        OWLAPIReasoner baseReasoner = new OWLAPIReasoner(ks);
        baseReasoner.setReasonerImplementation(ReasonerImplementation.PELLET);
        baseReasoner.init();

        // closed world reasoner
        reasoner = new ClosedWorldReasoner(baseReasoner);

        reasoner.init();

        logger.info("[DLLearner] Pellet reasoner initialisation done in " + (System.currentTimeMillis() - startTime) + "ms.");

    }



    @Override
    public void initKnowledgeSource(ReasonerType reasonerType, String sparqlEndpoint) throws Exception{
    	useEL = sparqlEndpoint != null;

        if (reasonerType.equals(ReasonerType.HERMIT_REASONER)){
            initOWLKnowledgeSoure();
        } else if(reasonerType.equals(ReasonerType.SPARQL_REASONER)){
            initSparqlKnowledgeSource(sparqlEndpoint);
        }

    }

    private void initSparqlKnowledgeSource(String sparqlEndpoint) throws Exception{
        logger.info("[DLLearner] init SPARQL knowledge source...");

        SparqlEndpoint ep = new SparqlEndpoint(new URL(sparqlEndpoint));

        KnowledgeSource schemaKs = new OWLAPIOntology(project.getRootOntology());
        SparqlEndpointKS ks = new SparqlEndpointKS(ep, schemaKs);
        ((SparqlEndpointKS)ks).setUseCache(false);
        ks.init();
        logger.info("[DLLearner] initialisation of SPARQL knowledge source done");
        
        logger.info("[DLLearner] generation of knowledge base sample...");
        this.ks = generateKnowledgebaseSample(ks);
        logger.info("[DLLearner] generation of knowledge base sample done");
    }

    private void initOWLKnowledgeSoure() throws Exception{
        logger.info("[DLLearner] init OWL knowledge source...");

        ks = new OWLAPIOntology(project.getRootOntology());
        ks.init();

        logger.info("[DLLearner] initialisation of OWL knowledge source done");
    }
    
    private KnowledgeSource generateKnowledgebaseSample(SparqlEndpointKS ks) throws Exception{
    	// extract sample of the knowledge base
    	ClassBasedSampleGenerator sampleGen = new ClassBasedSampleGenerator(ks);
    	sampleGen.addAllowedPropertyNamespaces(allowedPropertyNameSpaces);
    	sampleGen.addIgnoredProperties(ignoredProperties);
    	sampleGen.addAllowedObjectNamespaces(allowedObjectNamespaces);
    	
    	OWLOntology sampleOntology = sampleGen.getSample(selEntity.asOWLClass());
    	
    	// add schema ontology
    	OWLOntology schemaOntology = project.getRootOntology();
    	OWLOntologyManager man = OWLManager.createOWLOntologyManager();
    	schemaOntology = man.createOntology(schemaOntology.getAxioms());
    	man.removeAxioms(schemaOntology, schemaOntology.getAxioms(AxiomType.FUNCTIONAL_DATA_PROPERTY));
		man.removeAxioms(schemaOntology, schemaOntology.getAxioms(AxiomType.DISJOINT_CLASSES));
		man.removeAxioms(schemaOntology, schemaOntology.getAxioms(AxiomType.DATA_PROPERTY_RANGE));
    	
    	KnowledgeSource sampleKS = new OWLAPIOntology(sampleOntology);
    	sampleKS.init();
        
        return sampleKS;
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
        logger.info(la.toString());
        logger.info(la.getCurrentlyBestDescriptions().toString());
        logger.info(maxNrOfResults + "");
        if (la != null) {
            result = Collections.unmodifiableList((List<EvaluatedDescriptionClass>) la
                    .getCurrentlyBestEvaluatedDescriptions(maxNrOfResults, 0.5, true));
        } else {
            result = Collections.emptyList();
        }
        bestEvaluatedDescriptions.addAll(result);
        return result;
    }

    @Override
    public synchronized void startLearning(){
    	bestEvaluatedDescriptions = new ArrayList<EvaluatedDescriptionClass>();

        logger.info("[DLLearner] started learning of " +
                axiomType.getName() + " axioms");

        la.start();

        logger.info("[DLLearner] finished learning of " +
                axiomType.getName() + " axioms");





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

        try {
			OWLClassExpression subequivclass = null;
			OWLAxiom axiomToAdd = null;
			
			for (EvaluatedDescriptionClass evaluatedDescr : bestEvaluatedDescriptions){
			    if (classExpressionId == evaluatedDescr.getDescription().hashCode()){
			        subequivclass = evaluatedDescr.getDescription();
			    }
			}

			if (this.axiomType == AxiomType.EQUIVALENT_CLASSES){
			    Set<OWLClassExpression> exprSet = new HashSet<>();
			    exprSet.add(subequivclass);
			    exprSet.add(selEntity.asOWLClass());
			    axiomToAdd = new OWLEquivalentClassesAxiomImpl(exprSet, new HashSet<OWLAnnotation>());
			} else {
			    axiomToAdd = new OWLSubClassOfAxiomImpl(selEntity.asOWLClass(), subequivclass, new HashSet<OWLAnnotation>());
			}

			OWLOntologyChange ontChange = new AddAxiom(project.getRootOntology(), axiomToAdd);

			List<OWLOntologyChange> changeSet = new LinkedList<>();
			changeSet.add(ontChange);

			project.applyChanges(userId, FixedChangeListGenerator.get(changeSet), FixedMessageChangeDescriptionGenerator.get("added by DLLearner"));
		} catch (Exception e) {
			logger.severe(e);
		}
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
