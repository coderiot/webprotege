package fu.berlin.csw.dl_learner.client.ui;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.selection.EntityDataSelectionChangedEvent;
import edu.stanford.bmir.protege.web.shared.selection.EntityDataSelectionChangedHandler;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import fu.berlin.csw.dl_learner.client.websocket.WebSocket;
import fu.berlin.csw.dl_learner.client.websocket.exception.WebSocketNotSupportedException;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.logging.Logger;

/**
 * Created by lars on 13.04.15.
 */
public class DLLearnerPortlet extends AbstractOWLEntityPortlet {


    private SuggestionsPresenter presenter;
    private DLLearnerBasePanel basePanel;
    static Logger logger = Logger.getLogger(DLLearnerPortlet.class.getName());

    public DLLearnerPortlet(SelectionModel selectionModel, Project project) {
        super(selectionModel, project);
    }


    @Override
    public void initialize() {

        basePanel = new DLLearnerBasePanel(getProjectId(), this, this);
        setTitle("DLLearner");
        setSize(300, 68);
        add(basePanel);

        this.getSelectionModel().addSelectionChangedHandler(new EntityDataSelectionChangedHandler() {
            @Override
            public void handleSelectionChanged(EntityDataSelectionChangedEvent event) {
                boolean active = false;

                basePanel.forwardSelectionEvent(event);

                Optional<OWLEntity> selectedEntity = getSelectedEntity();

                if (selectedEntity.get() instanceof OWLClass){
                    active = true;
                } else {
                    active = false;
                }

                basePanel.setActive(active);



            }
        });


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

        return presenter;

    }





}
