package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.server.app.App;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.dl_learner.client.websocket.WebSocket;
import fu.berlin.csw.dl_learner.client.websocket.exception.WebSocketNotSupportedException;
import fu.berlin.csw.dl_learner.shared.GetSuggestionsExecutor;
import fu.berlin.csw.dl_learner.shared.GetSuggestionsResult;
import fu.berlin.csw.dl_learner.shared.Suggestion;
import fu.berlin.csw.dl_learner.shared.SuggestionRequest;

import java.util.logging.Logger;
//import fu.berlin.csw.dl_learner.client.rpc.ManagerService;
//import fu.berlin.csw.dl_learner.client.rpc.ManagerServiceAsync;

/**
 * Created by lars on 13.04.15.
 */
public class DLLearnerPortlet extends AbstractOWLEntityPortlet {


    private SuggestionPresenter presenter;
    private DLLearnerBasePanel basePanel;
    static Logger logger = Logger.getLogger(DLLearnerPortlet.class.getName());

    public DLLearnerPortlet(Project project) {
        super(project);
    }


    @Override
    public void initialize() {

        GWT.log("Hier ist der Logger!!! ");

        //OWLClassExpression ex = new OWLClassExpression();



        basePanel = new DLLearnerBasePanel(getProjectId(), this, this);
        setTitle("DLLearner");
        setSize(300, 68);
        add(basePanel);


        /*
        GetSuggestionsExecutor cex = new GetSuggestionsExecutor(DispatchServiceManager.get());
        Application app = Application.get();

        cex.execute(app.getActiveProject().get(), null, new DispatchServiceCallback<GetSuggestionsResult>() {
            @Override
            public void handleSuccess(GetSuggestionsResult result) {

                MessageBox.showMessage("WebServer started.");//,result.getMessage());
                GWT.log("WebServer started");

            }

            @Override
            public void handleExecutionException(Throwable cause) {
                MessageBox.showAlert("Server Error!",
                        cause.getMessage());
                UIUtil.hideLoadProgessBar();
                cause.printStackTrace();
            }
        });
        */

    }



        public SuggestionPresenter startWebSocket(){


            GWT.log( "ws://" + Window.Location.getHostName() + ":8025/" +  "chat/" + getProjectId().getId());


            String url = "ws://" + Window.Location.getHostName() + ":8025/" +  "chat/" + getProjectId().getId();
            final WebSocket webSocket = new WebSocket(url);



            presenter = new SuggestionPresenter(webSocket);


            try {
                webSocket.open();
            } catch (WebSocketNotSupportedException e) {
                Window.alert("Sorry your browser dosen't support Web Socket");
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
