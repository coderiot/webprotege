package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.dl_learner.shared.GetSuggestionsExecutor;
import fu.berlin.csw.dl_learner.shared.GetSuggestionsResult;
import fu.berlin.csw.dl_learner.shared.SuggestionRequest;


/**
 * Created by lars on 09.06.15.
 */
public class SuggestionsWindow extends com.gwtext.client.widgets.Window {

    private DLLearnerPortlet wrapperPortlet;
    private Button findEqualClassesButton;
    private Button addSuggestionButton;
    private Button cancelButton;
    private SuggestionsListView suggestionsListView;


    public SuggestionsWindow(DLLearnerPortlet wrapperPortlet){
        this.wrapperPortlet = wrapperPortlet;
        setTitle("Find equivalent Classes:");
        setWidth(500);
        setHeight(365);
        //setLayout(new FitLayout());

        this.addSuggestionsButton();
        this.addAddSuggestionButton();
        this.addCancelButton();

        suggestionsListView = new SuggestionsListView();

        this.add(suggestionsListView);

    }



    private void addSuggestionsButton(){

        findEqualClassesButton = new Button();
        findEqualClassesButton.setText("Find Equivalent Classes");


        findEqualClassesButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                GetSuggestionsExecutor cex = new GetSuggestionsExecutor(DispatchServiceManager.get());
                final ProjectId projectId = Application.get().getActiveProject().get();

                Application app = Application.get();

                // Send Suggestion request to websocket in order to stream the suggestions gradually


                final Timer timer = new Timer() {
                    @Override
                    public void run()
                    {
                        final SuggestionPresenter presenter = wrapperPortlet.startWebSocket();
                        presenter.setListView(getSuggestionsListView());

                        final Timer timer2 = new Timer(){
                            @Override
                            public void run()
                            {
                                final SuggestionRequest req = new SuggestionRequest();
                                req.setData("Suggestion Request");
                                req.setProjectId(projectId);

                                presenter.sendMessage(req);
                            }
                        };

                        timer2.schedule(5000);

                    }
                };



                // invoke GetSuggestionAction and start learning process on the server

                // TODO: No entity selected!!
                cex.execute(app.getActiveProject().get(), wrapperPortlet.getSelectedEntity().get(), new DispatchServiceCallback<GetSuggestionsResult>() {
                    @Override
                    public void handleSuccess(GetSuggestionsResult result) {
                        MessageBox.showMessage("Class learning success!",
                                result.getMessage());

                        /*  TODO:  final possible?? */

                        /*final SuggestionPresenter presenter = wrapperPortlet.startWebSocket();
                        presenter.setListView(getSuggestionsListView());



                        final SuggestionRequest req = new SuggestionRequest();
                        req.setData("Suggestion Request");
                        req.setProjectId(projectId);


                        Timer timer = new Timer()
                        {
                            @Override
                            public void run()
                            {
                                presenter.sendMessage(req);
                            }
                        };

                        timer.schedule(5000);*/


                    }

                    @Override
                    public void handleExecutionException(Throwable cause) {
                        MessageBox.showAlert("Server Error!",
                                cause.getMessage());
                        cause.printStackTrace();
                    }
                });


                timer.schedule(5000);



            }

        });

        setTopToolbar(new Toolbar());
        Toolbar toolbar = getTopToolbar();

        toolbar.addElement(findEqualClassesButton.getElement());
        findEqualClassesButton.setWidth("33%");


    }


    private void addAddSuggestionButton(){

        addSuggestionButton = new Button();
        addSuggestionButton.setText("Add Class Suggestion");


        addSuggestionButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {


            }

        });

        setTopToolbar(new Toolbar());
        Toolbar toolbar = getTopToolbar();

        toolbar.addElement(addSuggestionButton.getElement());
        addSuggestionButton.setWidth("33%");


    }


    private void addCancelButton(){
        cancelButton = new Button();
        cancelButton.setText("Cancel Process");


        cancelButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {


            }

        });

        setTopToolbar(new Toolbar());
        Toolbar toolbar = getTopToolbar();

        toolbar.addElement(cancelButton.getElement());

        cancelButton.setWidth("33%");
    }

    public SuggestionsListView getSuggestionsListView(){
        return this.suggestionsListView;
    }



}
