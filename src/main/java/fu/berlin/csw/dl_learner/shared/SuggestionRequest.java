package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Created by lars on 04.06.15.
 */
public class SuggestionRequest implements Serializable, IsSerializable {

    private static final long serialVersionUID = -2974784212373773166L;
    private String data;
    private ProjectId projectId;
    private UserId userId;

    /**
     * Default no-args constructor for GWT serialization purposes.
     */
    public SuggestionRequest(){
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public void setProjectId(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId(){
        return this.projectId;
    }


    public UserId getUserId(){
        return this.userId;
    }

    public void setUserId(UserId userId){
        this.userId = userId;
    }


    @Override
    public String toString(){
        return this.data;
    }

}
