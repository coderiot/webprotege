package fu.berlin.csw.AspectPortlet.server;

import fu.berlin.csw.AspectPortlet.client.ui.Message;
import org.semanticweb.owlapi.model.OWLOntology;

import edu.stanford.bmir.protege.web.server.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.server.logging.DefaultLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;

import fu.berlin.csw.AspectPortlet.client.rpc.AspectService;

public class AspectServiceImpl extends WebProtegeRemoteServiceServlet
implements AspectService{
	
	private ProjectId projectId;
    private WebProtegeLogger logger = new DefaultLogger(AspectService.class);

	
	@Override
	public void postChangeEvent(ProjectId projectId, ProjectChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(ProjectId projectId) {
		
		this.projectId = projectId;
		
		
		logger.info("Blubb hier bin ich");
		
		OWLAPIProject project = OWLAPIProjectManager.getProjectManager()
				.getProject(projectId);
		
		for (OWLOntology ont : project.getRootOntology().getDirectImports()){
			System.out.println(ont);
		}
		
		
		
		
	}

	@Override
	public Message getMessage(ProjectId input) {
		// TODO Auto-generated method stub
		return null;
	}

}
