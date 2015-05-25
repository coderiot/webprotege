package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.editor.*;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import fu.berlin.csw.dl_learner.shared.CommitExecutor;
import fu.berlin.csw.dl_learner.shared.CommitResult;
import org.semanticweb.owlapi.model.OWLEntity;
//import fu.berlin.csw.dl_learner.client.rpc.ManagerService;
//import fu.berlin.csw.dl_learner.client.rpc.ManagerServiceAsync;

/**
 * Created by lars on 13.04.15.
 */
public class DLLearnerPortlet extends AbstractOWLEntityPortlet {

    private DLLearnerBasePanel basePanel;

    public DLLearnerPortlet(Project project) {
        super(project);
    }


    @Override
    public void initialize() {



        basePanel = new DLLearnerBasePanel(getProjectId(), this, this);
        setTitle("DLLearner");
        setSize(300, 68);
        add(basePanel);

        CommitExecutor cex = new CommitExecutor(DispatchServiceManager.get());
        Application app = Application.get();

        /*
        addProjectEventHandler(ProjectChangedEvent.TYPE, new ProjectChangedHandler() {
            @Override
            public void handleProjectChanged(ProjectChangedEvent event) {
                MessageBox.showAlert(new Boolean(getSelectedEntity().isPresent()).toString());
            }
        });
        */

/*
        cex.execute(app.getActiveProject().get(), null, new DispatchServiceCallback<CommitResult>() {
            @Override
            public void handleSuccess(CommitResult result) {
                MessageBox.showMessage("Commit success.",
                        result.getMessage());
            }

            @Override
            public void handleExecutionException(Throwable cause) {
                MessageBox.showAlert("Commit failed!",
                        "Something goes wrong :(.");
                UIUtil.hideLoadProgessBar();
                cause.printStackTrace();
            }
        });
*/
/*
        final com.gwtext.client.widgets.Window window = new com.gwtext.client.widgets.Window();
        window.setTitle("Search results");
        window.setWidth(500);
        window.setHeight(365);
        window.setLayout(new FitLayout());

        DLLearnerPanel dllearnerPanel = new DLLearnerPanel();

        dllearnerPanel.setSelectedEntity(getSelectedEntity().get());
*/


    }


}
