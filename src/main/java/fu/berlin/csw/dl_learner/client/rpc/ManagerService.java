package fu.berlin.csw.dl_learner.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Created by lars on 29.04.15.
 */

//@RemoteServiceRelativePath("dllearner")
public interface ManagerService extends RemoteService {

    void init(ProjectId projectId);

}
