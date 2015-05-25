package fu.berlin.csw.dl_learner.server;

/**
 * Created by lars on 29.04.15.
 */

import edu.stanford.bmir.protege.web.server.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.dl_learner.client.rpc.ManagerService;
//import org.dllearner.algorithms.celoe.CELOE;
//import org.dllearner.reasoning.OWLAPIReasoner;
//import org.semanticweb.owlapi.reasoner.OWLReasoner;
//import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
//import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
//import com.clarkparsia.pellet.owlapiv3.PelletReasoner;


import java.util.*;
import java.util.logging.Logger;

public class Manager extends WebProtegeRemoteServiceServlet {//implements ManagerService {


   // @Override
    public void init(ProjectId projectId) {


        /*

        final Logger logger = Logger.getLogger(Manager.class.getName());

        logger.info("Hier bin ich!");


        OWLAPIReasoner baseReasoner = new OWLAPIReasoner();

        OWLReasonerFactory pelletFactory = PelletReasonerFactory.getInstance();

        OWLAPIProject project = WebProtegeInjector.get().getInstance(OWLAPIProject.class);

        OWLReasoner reasoner = pelletFactory.createReasoner(project.getRootOntology());

        logger.info("ich habn reasoner : " + reasoner);
        */


    }
}
