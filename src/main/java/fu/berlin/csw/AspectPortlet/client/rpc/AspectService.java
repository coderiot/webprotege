package fu.berlin.csw.AspectPortlet.client.rpc;

import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.AspectPortlet.client.ui.Message;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Author: Lars Parmakerli<br>
 * Freie Universität Berlin<br>
 * corporate semantic web<br>
 * Date: 14/08/2014
 */

@RemoteServiceRelativePath("dbpedia")
public interface AspectService extends RemoteService {
	
	
	Message getMessage(
			ProjectId input);

	void postChangeEvent(ProjectId projectId, ProjectChangedEvent event);

	void init(ProjectId projectId);
}