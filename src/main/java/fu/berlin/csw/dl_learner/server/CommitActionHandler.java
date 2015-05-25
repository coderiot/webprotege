package fu.berlin.csw.dl_learner.server;

//import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import fu.berlin.csw.dl_learner.shared.CommitAction;
import fu.berlin.csw.dl_learner.shared.CommitResult;
import org.coode.owlapi.turtle.TurtleOntologyStorer;
import org.dllearner.algorithms.DisjointClassesLearner;
import org.dllearner.algorithms.properties.AxiomAlgorithms;
import org.dllearner.core.AbstractAxiomLearningAlgorithm;
import org.dllearner.core.ComponentInitException;
import org.dllearner.kb.LocalModelBasedSparqlEndpointKS;
import org.dllearner.kb.SparqlEndpointKS;
import org.dllearner.reasoning.OWLAPIReasoner;
import org.dllearner.utilities.OwlApiJenaUtils;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import com.hp.hpl.jena.rdf.model.Model;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 * Created by lars on 29.04.15.
 */
public class CommitActionHandler extends AbstractHasProjectActionHandler<CommitAction, CommitResult> {

    @Inject
    public CommitActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    public Class<CommitAction> getActionClass() {
        return CommitAction.class;
    }

    @Override
    protected RequestValidator<CommitAction> getAdditionalRequestValidator(CommitAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected CommitResult execute(CommitAction action, OWLAPIProject project, ExecutionContext executionContext) {

        OWLEntity selEntity = action.getSelectedEntity();

        String message = "done! " + action.getSelectedEntity().getIRI().toString();



        try {

            final Logger logger = Logger.getLogger(Manager.class.getName());

            logger.info("execute request!");

            OWLOntology ont = project.getRootOntology();

            Reasoner hermit = new Reasoner(project.getRootOntology());

            //OWLReasoner reasoner=new Reasoner.ReasonerFactory().createReasoner(project.getRootOntology());

            OWLAPIReasoner baseReasoner = new OWLAPIReasoner(hermit);

            ont.getOWLOntologyManager().addOntologyStorer(new TurtleOntologyStorer());
            Model model = OwlApiJenaUtils.getModel(ont);

            LocalModelBasedSparqlEndpointKS ks = new LocalModelBasedSparqlEndpointKS(model);


            Class<? extends AbstractAxiomLearningAlgorithm<? extends OWLAxiom, ? extends OWLObject, ? extends OWLEntity>> algorithmClass
                    = AxiomAlgorithms.getAlgorithmClass(AxiomType.DISJOINT_CLASSES);

            AbstractAxiomLearningAlgorithm alg;

            alg = algorithmClass.getConstructor(SparqlEndpointKS.class).newInstance(ks);


            alg.setEntityToDescribe(selEntity);

            alg.setUseSampling(false);

            alg.init();
            alg.start();



            DisjointClassesLearner algDis = ((DisjointClassesLearner)alg);

            System.out.println(algDis.getCurrentlyBestEvaluatedAxioms());


            //logger.info(alg.getCurrentlyBestEvaluatedAxioms().toString());

            //logger.info("ich habn reasoner : " + reasoner);
            //logger.info("ich habn reasoner : " + baseReasoner);
            //logger.info("selected entity:  " + action.getSelectedEntity());

            //message += "\n" + action.getSelectedEntity().toString();


        } catch (ComponentInitException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }



        return new CommitResult(message);


    }

}
