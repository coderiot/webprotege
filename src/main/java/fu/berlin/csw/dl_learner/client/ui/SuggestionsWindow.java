package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
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
import com.google.gwt.user.cellview.client.SimplePager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;


/**
 * Created by lars on 09.06.15.
 */
public class SuggestionsWindow extends Window {

    private DLLearnerPortlet wrapperPortlet;
    private Button startLearningButton;
    private Button addSuggestionButton;
    private Button cancelButton;
    private SuggestionsListView suggestionsListView;
    private ConfigurationPanel configurationsPanel;
    private Window configurationsWindow;

    private String sparqlEndpoint;


    public SuggestionsWindow(DLLearnerPortlet wrapperPortlet){
        this.wrapperPortlet = wrapperPortlet;
        setTitle("DLLearner " + wrapperPortlet.getSelectedEntity().get().asOWLClass().getIRI() + "[EquivalentClasses]");
        setWidth(500);
        setHeight(280);

        // create and add Buttons

        this.addStartLearningButton();
        this.addAddSuggestionButton();
        this.addCancelButton();


        // create and add Configuration Window

        this.configurationsPanel = new ConfigurationPanel(this);
        this.configurationsWindow = new Window();
        configurationsWindow.add(configurationsPanel);


        // create and add Suggestions List View

        final DockLayoutPanel sp = new DockLayoutPanel(Style.Unit.EM);
        suggestionsListView = new SuggestionsListView();
        sp.addNorth(suggestionsListView, 16);

        SimplePager pager = new SimplePager();
        pager.setDisplay(suggestionsListView);
        pager.setPageSize(7);

        sp.addNorth(pager, 16);


        // add Configuration Button

        Button configButton = new Button();
        configButton.setText("Configure Learning Algorithm");
        configButton.setWidth("100%");
        configButton.setHeight("100%");
        configButton.addStyleName("button-style");

        configButton.addListener(new ButtonListenerAdapter(){

            @Override
            public void onClick(Button button, EventObject e) {
                showConfigurationsWindow();
            }
        });

        sp.addSouth(configButton, 2);
        sp.setHeight("100%");

        this.add(sp);

    }



    private void addStartLearningButton(){

        startLearningButton = new Button();
        startLearningButton.setText("Start Learning");

        startLearningButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {

                suggestionsListView.clearList();

                InitLearningProcessExecutor cex = new InitLearningProcessExecutor(DispatchServiceManager.get());
                final ProjectId projectId = Application.get().getActiveProject().get();

                Application app = Application.get();

                // invoke GetSuggestionAction and start learning process on the server

                if (!getConfigurationsPanel().getUseSparqlEndpoint()){
                    sparqlEndpoint = null;
                } else {
                    sparqlEndpoint = getConfigurationsPanel().getSparqlEndpoint();
                }

                AxiomType axiomType = getConfigurationsPanel().getAxiomType();
                int maxExecutionTimeInSeconds = getConfigurationsPanel().getMaxExecutionTime();
                int maxNrOfResults = getConfigurationsPanel().getMaxNumberOfResults();
                int noisePercentage = getConfigurationsPanel().getNoisePercentage();
                int cardinalityLimit = getConfigurationsPanel().getCardinalityLimit();
                boolean useAllConstructor = getConfigurationsPanel().getUseAllConstructor();
                boolean useNegation = getConfigurationsPanel().getUseNegation();
                boolean useCardinalityRestrictions = getConfigurationsPanel().getUseCardinalityRestriction();
                boolean useExistsConstructor = getConfigurationsPanel().getUseExistsConstructorVal();  // ToDo Reafactor
                boolean useHasValueConstructor = getConfigurationsPanel().getUseHasValueConstructor();

                cex.execute(app.getActiveProject().get(), wrapperPortlet.getSelectedEntity().get(), sparqlEndpoint,
                        axiomType, maxExecutionTimeInSeconds, maxNrOfResults, noisePercentage, cardinalityLimit, useAllConstructor,
                        useNegation, useCardinalityRestrictions, useExistsConstructor, useHasValueConstructor,
                        new DispatchServiceCallback<InitLearningProcessResult>() {

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

                    }
                });
                showWaitCursor();

            }

        });

        setTopToolbar(new Toolbar());
        Toolbar toolbar = getTopToolbar();

        toolbar.addElement(startLearningButton.getElement());
        startLearningButton.setWidth("33%");


    }


    private void addAddSuggestionButton(){

        addSuggestionButton = new Button();
        addSuggestionButton.setText("Add Axiom");


        addSuggestionButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {

                AddAxiomExecutor cex = new AddAxiomExecutor(DispatchServiceManager.get());

                Application app = Application.get();

                cex.execute(app.getActiveProject().get(), wrapperPortlet.getSelectedEntity().get(), getSelectedClassExpressionId(), new DispatchServiceCallback<AddAxiomResult>() {

                    @Override
                    public void handleSuccess(AddAxiomResult result) {
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

                CancelLearningExecutor cex = new CancelLearningExecutor(DispatchServiceManager.get());

                Application app = Application.get();

                cex.execute(app.getActiveProject().get(), new DispatchServiceCallback<CancelLearningResult>() {

                    @Override
                    public void handleSuccess(CancelLearningResult result) {
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




    public int getSelectedClassExpressionId(){
        return suggestionsListView.getSelectedSuggestion().getClassExpressionId();
    }


    private void showConfigurationsWindow(){
        this.configurationsWindow.show();
    }

    public void hideConfigurationsWindow(){
        this.configurationsWindow.hide();
    }

    private ConfigurationPanel getConfigurationsPanel(){
        return this.configurationsPanel;
    }

    public void changeTitle(){

        if (!wrapperPortlet.getSelectedEntity().get().isOWLClass()){
            startLearningButton.disable();
            return;
        }

        OWLClass selectedClass = wrapperPortlet.getSelectedEntity().get().asOWLClass();


        configurationsPanel.getAxiomType();

        this.setTitle("DLLearner " + selectedClass.getIRI() + " [" + getConfigurationsPanel().getAxiomType() + "]");
    }

}
