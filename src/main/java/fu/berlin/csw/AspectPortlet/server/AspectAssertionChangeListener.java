package fu.berlin.csw.AspectPortlet.server;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyManagerImpl;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lars on 23.02.15.
 */
public class AspectAssertionChangeListener implements OWLOntologyChangeListener {

    private final Logger logger;
    private ProjectId projectId;
    private OWLAPIProject project;
    private UserId user;
    private OWLOntologyManager oM;
    private AspectServiceImpl caller;


    public AspectAssertionChangeListener(){
        logger = Logger.getLogger(AspectServiceImpl.class.getName());
    }


    public void setChangeEnvironment(ProjectId projectId, UserId user, OWLOntologyManager oM, AspectServiceImpl caller){
        this.user = user;
        this.oM = oM;
        this.projectId = projectId;
        this.caller = caller;
    }


    @Override
    public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {

        OWLDataFactory dF = new OWLDataFactoryImpl();
        OWLAnnotationProperty annProp = dF.getOWLAnnotationProperty(IRI.create("fu.berlin.csw.hasAspect"));

        List<OWLAnnotation> checkedAspectList = new LinkedList<OWLAnnotation>();

        for (IRI aspect : caller.getCheckedAspects().getAspects()){
            OWLAnnotation ann=dF.getOWLAnnotation(annProp, aspect);
            checkedAspectList.add(ann);
        }

        List<OWLOntologyChange> aspectChanges = new ArrayList<OWLOntologyChange>();


        logger.info("Changes Size :::: " + changes.size());
        logger.info("User    Name :::: " + user);


        for ( OWLOntologyChange change : changes ){
            OWLAxiom oldAxiom= change.getAxiom();

            if (oldAxiom.isLogicalAxiom() | oldAxiom.isAnnotationAxiom()){

                logger.info("ANNOTATION AXION   BZW     LOGICAL AXIOM ::::::::");

                OWLAxiom newAxiom = oldAxiom.getAnnotatedAxiom(new HashSet<OWLAnnotation>(checkedAspectList));

                aspectChanges.add(new RemoveAxiom(change.getOntology(), oldAxiom));
                aspectChanges.add(new AddAxiom(change.getOntology(), newAxiom));

                logger.info("NEW AXIOM:: " + newAxiom.toString());

                continue;

            }

            for (OWLEntity ent : oldAxiom.getSignature()){


                logger.info("SIGNATURE :::: " + ent.getIRI());
                logger.info("CHECKED ASPECT LIST.SIZE()  :::: " + checkedAspectList.size());

                for (OWLAnnotation ann : checkedAspectList){
                    OWLAxiom newAxiom = dF.getOWLAnnotationAssertionAxiom(ent.getIRI() , ann);
                    aspectChanges.add(new AddAxiom(change.getOntology(), newAxiom));

                    logger.info("NEW AXIOM :::::::: " + newAxiom.toString());
                }


            }


        }

        oM = OWLAPIProjectManager.getProjectManager()
                .getProject(projectId).getRootOntology().getOWLOntologyManager();


        oM.removeOntologyChangeListener(this);
        OWLAPIProjectManager.getProjectManager()
                .getProject(projectId).applyChanges(user, aspectChanges, "Set Axioms" +
                "");
        oM.addOntologyChangeListener(this);

    }
}
