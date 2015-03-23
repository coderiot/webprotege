package fu.berlin.csw.AspectPortlet.client.rpc;

import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.AspectPortlet.client.ui.Aspects;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;

/**
 * Author: Lars Parmakerli<br>
 * Freie Universität Berlin<br>
 * corporate semantic web<br>
 * Date: 14/08/2014
 */

@RemoteServiceRelativePath("aspect")
public interface AspectService extends RemoteService {
	
	
	Aspects getAspects(
			ProjectId input);

	void postChangeEvent(ProjectId projectId, ProjectChangedEvent event);

    void addCheckedAspect(IRI iri);

    void removeUnCheckedAspect(IRI iri);

    void init(ProjectId projectId);
}