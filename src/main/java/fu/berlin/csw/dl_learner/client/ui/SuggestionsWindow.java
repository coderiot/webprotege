package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.dl_learner.shared.InitLearningProcessExecutor;
import fu.berlin.csw.dl_learner.shared.InitLearningProcessResult;
import fu.berlin.csw.dl_learner.shared.SuggestionRequest;
import com.google.gwt.canvas.client.Canvas;


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


    final CssColor colorRed = CssColor.make("red");
    final CssColor colorGreen = CssColor.make("green");
    final CssColor colorBlue = CssColor.make("blue");


    public SuggestionsWindow(DLLearnerPortlet wrapperPortlet){
        this.wrapperPortlet = wrapperPortlet;
        setTitle("Find equivalent Classes:");
        setWidth(500);
        setHeight(600);
        //setLayout(new FitLayout());

        this.addSuggestionsButton();
        this.addAddSuggestionButton();
        this.addCancelButton();
        this.addShowCoverageButton();

        suggestionsListView = new SuggestionsListView();

        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.add(suggestionsListView);

        this.add(scrollPanel);

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
                cex.execute(app.getActiveProject().get(), wrapperPortlet.getSelectedEntity().get(), new DispatchServiceCallback<InitLearningProcessResult>() {

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


    private void addShowCoverageButton(){
        showCoverageButton = new Button();
        showCoverageButton.setText("Cancel Process");


        showCoverageButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {


                Window window = new Window();

                Canvas canvas = Canvas.createIfSupported();
                Context2d context;

                canvas.setWidth("300px");
                canvas.setHeight("300px");
                canvas.setCoordinateSpaceWidth(300);
                canvas.setCoordinateSpaceHeight(300);

                window.add(canvas);

                context = canvas.getContext2d();

                context.beginPath();
                context.setFillStyle(colorRed);
                context.fillRect(100, 50, 100, 100);
                context.setFillStyle(colorGreen);
                context.fillRect(200, 150, 100, 100);
                context.setFillStyle(colorBlue);
                context.fillRect(300, 250, 100, 100);
                context.closePath();

                window.show();
            }

        });

        setTopToolbar(new Toolbar());
        Toolbar toolbar = getTopToolbar();

        toolbar.addElement(showCoverageButton.getElement());

        showCoverageButton.setWidth("33%");
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



}
