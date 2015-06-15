package fu.berlin.csw.dl_learner.server;

import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.dl_learner.server.websocket.SuggestionWebSocketServer;
import org.dllearner.core.ComponentInitException;
import org.glassfish.tyrus.server.Server;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.websocket.DeploymentException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lars on 11.06.15.
 */
public class Manager {

    private static Manager instance;
    private static Set<ClassDescriptionLearner> classDescriptionLearnerSet = new HashSet<>();
    private static Server server;
    private static WebProtegeLogger logger = WebProtegeInjector.get().getInstance(WebProtegeLogger.class);



    public static synchronized Manager getInstance(){
        if(instance == null){
            logger.info("[DLLearner Plugin] Create new Manager");

            instance = new Manager();

            /* Init Server for suggestion streaming and logger */

            try {
                server = new Server("localhost", 8025, "/", null,SuggestionWebSocketServer.class);
                server.start();
                logger.info("[DLLearner Plugin] start suggestions-stream-webservice");
            } catch (final DeploymentException e1) {
                e1.printStackTrace();
                logger.info("[DLLearner Plugin] suggestions-stream-webservice already started!");

            }
        }
        return instance;
    }


    public void startLearnProcess(OWLAPIProject project, OWLEntity selectedEntity) throws Exception{

        ClassDescriptionLearner classDescriptionLearner = null;

        for (ClassDescriptionLearner cdl : classDescriptionLearnerSet){
            if (project.getProjectId().hashCode() == cdl.hashCode()){
                classDescriptionLearner = cdl;
            }
        }

        if (classDescriptionLearner == null ){
            classDescriptionLearner = new DLLearnerAdapter(project);
            classDescriptionLearnerSet.add(classDescriptionLearner);

            logger.info("[DLLearner Plugin] Add new class description learner for project : " + project.getProjectId());
        }

        try {


            classDescriptionLearner.setAxiomType(AxiomType.EQUIVALENT_CLASSES);            // TODO: generic solution
            classDescriptionLearner.setEntity(selectedEntity);
            classDescriptionLearner.initKnowledgeSource(ReasonerType.SPARQL_REASONER);     // TODO: generic solution
            classDescriptionLearner.initReasoner(ReasonerType.SPARQL_REASONER);
            classDescriptionLearner.initLearningProblem();
            classDescriptionLearner.initLearningAlgorithm();

            logger.info("[DLLearner Plugin] Initialisation of learning process finished.");

            logger.info("[DLLearner Plugin] Start learning.");

            classDescriptionLearner.startLearning();




        }
        catch(ComponentInitException e){
            e.printStackTrace();
        }

        catch(Exception e){
            e.printStackTrace();
        }
        catch(Error e){
            e.printStackTrace();
        }


    }




    public static ClassDescriptionLearner getProjectRelatedLearner(ProjectId projectId){
        for (ClassDescriptionLearner cdl : classDescriptionLearnerSet){
            if (cdl.hashCode() == projectId.hashCode()){
                return cdl;
            }
        }

        // Todo: throw Exception
        return null;
    }




}
