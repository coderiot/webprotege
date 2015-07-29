package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.dl_learner.shared.*;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.cellview.client.SimplePager;


/**
 * Created by lars on 09.06.15.
 */
public class SuggestionsWindow extends Window {

    private DLLearnerPortlet wrapperPortlet;
    private Button findEqualClassesButton;
    private Button addSuggestionButton;
    private Button cancelButton;
    private Button showCoverageButton;
    private SuggestionsListView suggestionsListView;

    private String sparqlEndpoint;
    private Boolean useSparqlEndpoint = false;


    final CssColor colorRed = CssColor.make("red");
    final CssColor colorGreen = CssColor.make("green");
    final CssColor colorBlue = CssColor.make("blue");


    public SuggestionsWindow(DLLearnerPortlet wrapperPortlet){
        this.wrapperPortlet = wrapperPortlet;
        setTitle("Find equivalent Classes:");
        setWidth(500);
        setHeight(280);
        //setLayout(new FitLayout());

        this.addSuggestionsButton();
        this.addAddSuggestionButton();
        this.addCancelButton();
        //this.addShowCoverageButton();


        final DockLayoutPanel sp = new DockLayoutPanel(Style.Unit.EM);


        suggestionsListView = new SuggestionsListView();

        sp.addNorth(suggestionsListView, 16);
        //this.add(suggestionsListView);


        SimplePager pager = new SimplePager();
        pager.setDisplay(suggestionsListView);
        pager.setPageSize(7);


        //this.add(pager);
        sp.addNorth(pager, 16);


        //hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        //hp.setHeight("10%");

        StackPanel lp = new StackPanel();


        Label label = new Label();
        Label label2 = new Label();

        CheckBox useSparqlCheckBox = new CheckBox();
        TextBox useSparqlTextBox = new TextBox();

        Button configButton = new Button();
        configButton.setText("Configure Learning Algorithm");
        configButton.setWidth("100%");
        configButton.setHeight("100%");


        configButton.addListener(new ButtonListenerAdapter(){

            private TextBox sparqlEndpointTextBox;
            private Window window;

            public void closeWindow(){
                this.window.close();
            }

            public void disableTextBox(){
                sparqlEndpointTextBox.setEnabled(false);
            }

            public void enableTextBox(){
                sparqlEndpointTextBox.setEnabled(true);
            }

            @Override
            public void onClick(Button button, EventObject e) {

                // Build Configuration Window

                window = new Window();
                window.setTitle("Configuration");

                window.setHeight(100);
                window.setWidth(300);

                // Configure Use of Sparql Endpoint to receive instance data

                CheckBox useSparqlCheckBox = new CheckBox();
                useSparqlCheckBox.setText("use Sparql Reasoner ");

                useSparqlCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
                        if (valueChangeEvent.getValue().equals(true)){
                            setUseSparqlEndpoint(true);
                            enableTextBox();
                        } else {
                            setUseSparqlEndpoint(false);
                            disableTextBox();
                        }
                    }
                });

                sparqlEndpointTextBox = new TextBox();
                sparqlEndpointTextBox.setEnabled(false);
                sparqlEndpointTextBox.setWidth("100%");

                sparqlEndpointTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<String> event) {
                        setSparqlEndpoint(event.getValue());
                    }
                });

                Button okButton = new Button();

                okButton.setText("OK");
                okButton.setWidth("100%");
                okButton.addListener(new ButtonListenerAdapter(){
                    @Override
                    public void onClick(Button button, EventObject e) {
                        closeWindow();
                    }
                });

                window.add(useSparqlCheckBox);
                window.add(sparqlEndpointTextBox);
                window.add(okButton);

                window.show();
            }
        });

        sp.addSouth(configButton, 2);

        sp.setHeight("100%");

        this.add(sp);




    }



    private void addSuggestionsButton(){

        findEqualClassesButton = new Button();
        findEqualClassesButton.setText("Find Equivalent Classes");


        findEqualClassesButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                InitLearningProcessExecutor cex = new InitLearningProcessExecutor(DispatchServiceManager.get());
                final ProjectId projectId = Application.get().getActiveProject().get();

                Application app = Application.get();

                // invoke GetSuggestionAction and start learning process on the server

                // TODO: No entity selected!!
                cex.execute(app.getActiveProject().get(), wrapperPortlet.getSelectedEntity().get(), getUseSparqlEndpoint(), getSparqlEndpoint(), new DispatchServiceCallback<InitLearningProcessResult>() {



                    @Override
                    public void handleSuccess(InitLearningProcessResult result) {

                        GWT.log("Initialisation Process complete!: " +
                                result.getMessage());

                        /*  TODO:  final possible?? */

                        final SuggestionsPresenter presenter = wrapperPortlet.startWebSocket();
                        presenter.setListView(getSuggestionsListView());

                        final SuggestionRequest req = new SuggestionRequest();
                        req.setData("Suggestion Request");
                        req.setProjectId(projectId);
                        req.setUserId(Application.get().getUserId());


                        Timer timer = new Timer()
                        {
                            @Override
                            public void run()
                            {
                                presenter.sendMessage(req);
                            }
                        };

                        timer.schedule(5000);


                    }

                    @Override
                    public void handleExecutionException(Throwable cause) {
                        showDefaultCursor();

                        GWT.log("Server Exception during initialisation of learning process!", cause);

                        if(cause.getMessage() == null){
                            com.google.gwt.user.client.Window.alert("Server Exception during initialisation of learning process! See GWT log for details!" +
                                    "\n" + cause.toString());
                        } else {
                            com.google.gwt.user.client.Window.alert("Server Exception during initialisation of learning process! See GWT log for details!" +
                                    "\n" + cause.getMessage());
                        }

                        //MessageBox.showErrorMessage("Server Exception during initialisation of learning process!",
                        //        cause);
                    }
                });
                showWaitCursor();



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

                AddEquivalentClassExecutor cex = new AddEquivalentClassExecutor(DispatchServiceManager.get());

                Application app = Application.get();

                cex.execute(app.getActiveProject().get(), wrapperPortlet.getSelectedEntity().get(), getSelectedClassExpressionId(), new DispatchServiceCallback<AddEquivalentClassResult>() {

                    @Override
                    public void handleSuccess(AddEquivalentClassResult result) {
                        showDefaultCursor();
                        GWT.log("Adding learned class description complete!: " +
                                result.getMessage());


                    }

                    @Override
                    public void handleExecutionException(Throwable cause) {
                        showDefaultCursor();

                        GWT.log("Server Exception during Adding learned class description!", cause);

                    }


                });
                showWaitCursor();

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



    public static void showWaitCursor() {
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
    }

    public static void showDefaultCursor() {
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
    }



    public void setSparqlEndpoint(String endpoint){
        GWT.log(endpoint);
        this.sparqlEndpoint=endpoint;
    }

    public String getSparqlEndpoint(){
        return this.sparqlEndpoint;
    }

    public void setUseSparqlEndpoint(boolean useSparqlEndpoint){
        this.useSparqlEndpoint = useSparqlEndpoint;
    }

    public boolean getUseSparqlEndpoint(){
        return this.useSparqlEndpoint;
    }


    public int getSelectedClassExpressionId(){
        return suggestionsListView.getSelectedSuggestion().getClassExpressionId();
    }

}
