package fu.berlin.csw.dl_learner.shared;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by lars on 04.06.15.
 */
public class SuggestionRequest implements Serializable, IsSerializable {

    private static final long serialVersionUID = -2974784212373773166L;
    private String data;
    private ProjectId projectId;

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


    @Override
    public String toString(){
        return this.data;
    }

}
