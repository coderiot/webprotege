package fu.berlin.csw.DBPediaApp.client.rpc;

import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("dbpedia")
public interface DBPediaService extends RemoteService {
	fu.berlin.csw.DBPediaApp.client.ui.DBPediaPortlet.Message getMessage(ProjectId input);
	void postChangeEvent(ProjectId input, ProjectChangedEvent event);
	void init(ProjectId input);
}