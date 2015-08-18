package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.shared.event.HasEventHandlerManagement;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.EntityDataSelectionChangedEvent;

import java.awt.*;

/**
 * Created by lars on 13.05.15.
 */
public class DLLearnerBasePanel extends Composite {

    ProjectId projectId;

    HasEventHandlerManagement eventHandlerMan;

    DLLearnerPortlet wrapperPortlet;

    SuggestionsPresenter presenter;

    SuggestionsWindow suggList;

    @UiTemplate("DLLearnerBasePanel.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, DLLearnerBasePanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField
    Button dllearner;


    public DLLearnerBasePanel(final ProjectId projectId,
                           HasEventHandlerManagement eventHandlerMan, DLLearnerPortlet wrapperPortlet) {


        this.projectId = projectId;

        this.eventHandlerMan = eventHandlerMan;

        this.wrapperPortlet = wrapperPortlet;

        initWidget(uiBinder.createAndBindUi(this));

    }

    public void setPresenter(SuggestionsPresenter presenter){
        this.presenter = presenter;
    }



    @UiHandler("dllearner")
    void handleClick(ClickEvent e) {

        final com.gwtext.client.widgets.Window window = new com.gwtext.client.widgets.Window();
        window.setTitle("Start DLLearner:");
        window.setWidth(500);
        window.setHeight(365);
        window.setLayout(new FitLayout());


        suggList = new SuggestionsWindow(wrapperPortlet);

        suggList.show();

    }

    public void setActive(boolean active){
        this.dllearner.setEnabled(active);
    }


    public void forwardSelectionEvent(EntityDataSelectionChangedEvent event){
        GWT.log("Event has been forwarded");
        if (suggList != null){
            suggList.changeTitle();
        }
    }

}
