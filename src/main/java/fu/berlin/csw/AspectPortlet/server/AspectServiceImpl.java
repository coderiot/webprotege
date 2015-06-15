package fu.berlin.csw.AspectPortlet.server;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import fu.berlin.csw.AspectPortlet.client.ui.Aspects;
import org.semanticweb.owlapi.model.*;

import edu.stanford.bmir.protege.web.server.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;


import fu.berlin.csw.AspectPortlet.client.rpc.AspectService;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyManagerImpl;


import java.util.*;
import java.util.logging.Logger;

public class AspectServiceImpl extends WebProtegeRemoteServiceServlet
implements AspectService{

    private ProjectId projectId;

    private LinkedList<IRI> checkedAspects;

    private boolean initiated = false;

    @Override
	public void init(ProjectId projectId) {

        /*
        final Logger logger = Logger.getLogger(AspectServiceImpl.class.getName());
        this.projectId = projectId;


        logger.info("Der Logger loggt");

		OWLAPIProject project = OWLAPIProjectManager.getProjectManager()
				.getProject(projectId);


        OWLOntology rootOnt = project.getRootOntology();

        for (OWLClass owlClass : rootOnt.getClassesInSignature()){

            if (owlClass.getIRI().equals(IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect_owl#FunctionalAspect"))){

                for (OWLClass funcAspect : project.getClassHierarchyProvider().getDescendants(owlClass)){
                    logger.info("Nachfahren von Functional Aspect: " + funcAspect);
                };

            }
        }
        */

	}

	@Override
	public Aspects getAspects(ProjectId input) {

        if (!initiated){
            this.projectId = input;

            OWLAPIProject project = OWLAPIProjectManager.getProjectManager()
                    .getProject(projectId);

            UserId user = getUserInSession();
            OWLOntologyManager oM =  project.getRootOntology().getOWLOntologyManager();

            checkedAspects = new LinkedList<IRI>();
            AspectAssertionChangeListener aACL = new AspectAssertionChangeListener();
            aACL.setChangeEnvironment(projectId, user, oM, this);  // todo remove oM

            oM.addOntologyChangeListener(aACL);

            initiated = true;

        }

        final Logger logger = Logger.getLogger(AspectServiceImpl.class.getName());


        this.projectId = input;

        OWLAPIProject project = OWLAPIProjectManager.getProjectManager()
                .getProject(projectId);


        OWLOntology rootOnt = project.getRootOntology();

        List<IRI> funcAspects = new LinkedList<IRI>();

        for (OWLClass owlClass : rootOnt.getClassesInSignature()){

            if ((owlClass.getIRI().toString().contains("Aspect"))){


            //IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect_owl#FunctionalAspect")))
            //        || (owlClass.getIRI().equals(IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect_owl#NonfunctionalAspect")))){

                logger.info("Aspect gefunden!");

                funcAspects.add(owlClass.getIRI());

            //    for (OWLClass funcAspect : project.getClassHierarchyProvider().getDescendants(owlClass)){
            //        logger.info("Nachfahren von Functional Aspect: " + funcAspect);
            //        funcAspects.add(funcAspect.getIRI());
            //    };
            }
        }

        Aspects result = new Aspects();
        result.setAspects(funcAspects);


        for (IRI iri : checkedAspects){
            if (!result.getAspects().contains(iri)){
                checkedAspects.remove(iri);
            }
        }

        result.setCheckedAspects(checkedAspects);

        return result;
    }


    @Override
    public void postChangeEvent(ProjectId projectId, ProjectChangedEvent event) {  // wahrscheinlich hinfällig
        final Logger logger = Logger.getLogger(AspectServiceImpl.class.getName());

        logger.info("Update project ID from : " + this.projectId.toString());
        this.projectId = projectId;
        logger.info("To :" + this.projectId);
        OWLAPIProject project = OWLAPIProjectManager.getProjectManager()
                .getProject(projectId);




/*

        OWLDataFactory dF = new OWLDataFactoryImpl();
        OWLAnnotationProperty annProp = dF.getOWLAnnotationProperty(IRI.create("fu.berlin.csw.hasAspect"));
        OWLAnnotation ann=dF.getOWLAnnotation(annProp, IRI.create("fu.berlin.csw.TestAspect"));

        List<OWLOntologyChange> aspectChanges = new ArrayList<OWLOntologyChange>();



        OWLAPIProject project = OWLAPIProjectManager.getProjectManager()
                .getProject(projectId);


        for (OWLEntityData ent : event.getSubjects()){

            logger.info("SIGNATURE :::: " + ent.getEntity().getIRI());
            OWLAxiom newAxiom = dF.getOWLAnnotationAssertionAxiom(ent.getEntity().getIRI() , ann);
            aspectChanges.add(new AddAxiom(project.getRootOntology(), newAxiom));


            project.applyChanges(event.getUserId(), aspectChanges, "Set Axioms" +
                    "");

        }
*/
    }

    @Override
    public void addCheckedAspect(IRI iri){
        final Logger logger = Logger.getLogger(AspectServiceImpl.class.getName());
        logger.info("add IRI: " + iri.toString());
        checkedAspects.add(iri);
    }

    @Override
    public void removeUnCheckedAspect(IRI iri){
        final Logger logger = Logger.getLogger(AspectServiceImpl.class.getName());
        logger.info("remove IRI: " + iri.toString());
        checkedAspects.remove(iri);
    }




    public LinkedList<IRI> getCheckedAspects(){
        return this.checkedAspects;
    }
}


