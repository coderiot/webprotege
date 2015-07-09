package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import fu.berlin.csw.dl_learner.client.websocket.WebSocket;
import fu.berlin.csw.dl_learner.client.websocket.exception.WebSocketNotSupportedException;
import fu.berlin.csw.dl_learner.shared.SuggestionRequest;

import java.util.logging.Logger;
//import fu.berlin.csw.dl_learner.client.rpc.ManagerService;
//import fu.berlin.csw.dl_learner.client.rpc.ManagerServiceAsync;

/**
 * Created by lars on 13.04.15.
 */
public class DLLearnerPortlet extends AbstractOWLEntityPortlet {


    private SuggestionsPresenter presenter;
    private DLLearnerBasePanel basePanel;
    static Logger logger = Logger.getLogger(DLLearnerPortlet.class.getName());

    public DLLearnerPortlet(Project project) {
        super(project);
    }


    @Override
    public void initialize() {



        basePanel = new DLLearnerBasePanel(getProjectId(), this, this);
        setTitle("DLLearner");
        setSize(300, 68);
        add(basePanel);


    }



        public SuggestionsPresenter startWebSocket(){


            GWT.log( "ws://" + Window.Location.getHostName() + ":8025/" +  "suggestions/" + getProjectId().getId());


            String url = "ws://" + Window.Location.getHostName() + ":8025/" +  "suggestions/" + getProjectId().getId();
            final WebSocket webSocket = new WebSocket(url);



            presenter = new SuggestionsPresenter(webSocket);


            try {
                webSocket.open();
            } catch (WebSocketNotSupportedException e) {
                MessageBox.showErrorMessage("Couldn't start WebSocket on Client", e);
                GWT.log("Couldn't start WebSocket on Client!", e);
            }


            /*

            Suggestion suggestion = new Suggestion();
            suggestion.setData("Ich bin eine Test-Suggestion!!!");

            GWT.log("Test-Suggestion gesendet!!!");

            presenter.sendMessage(suggestion);

            */


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

        return presenter;

    }

    public void sendSuggestionRequest(SuggestionRequest req) {
        presenter.sendMessage(req);
    }




}
