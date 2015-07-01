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

import java.awt.*;

/**
 * Created by lars on 13.05.15.
 */
public class DLLearnerBasePanel extends Composite {

    ProjectId projectId;

    HasEventHandlerManagement eventHandlerMan;

    DLLearnerPortlet wrapperPortlet;

    SuggestionsPresenter presenter;

    @UiTemplate("DLLearnerBasePanel.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, DLLearnerBasePanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField
    Button commit;


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



    @UiHandler("commit")
    void handleClick(ClickEvent e) {

        final com.gwtext.client.widgets.Window window = new com.gwtext.client.widgets.Window();
        window.setTitle("Find equivalent Classes:");
        window.setWidth(500);
        window.setHeight(365);
        window.setLayout(new FitLayout());

        //DLLearnerPanel dllearnerPanel = new DLLearnerPanel(wrapperPortlet, null);

        //dllearnerPanel.setSelectedEntity(wrapperPortlet.getSelectedEntity().get());


        SuggestionsWindow suggList = new SuggestionsWindow(wrapperPortlet);

        suggList.show();


        //window.add(suggList);

        //window.show();



        //MessageBox.showAlert(new Boolean(wrapperPortlet.getSelectedEntity().isPresent()).toString());
    }

}
