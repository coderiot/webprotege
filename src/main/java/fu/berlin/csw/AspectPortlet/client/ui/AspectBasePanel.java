package fu.berlin.csw.AspectPortlet.client.ui;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.logging.client.SystemLogHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.HasEventHandlerManagement;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import fu.berlin.csw.AspectPortlet.client.rpc.AspectService;
import fu.berlin.csw.AspectPortlet.client.rpc.AspectServiceAsync;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import edu.stanford.bmir.protege.web.shared.event.HasEventHandlerManagement;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedHandler;

import java.util.Iterator;
import java.util.List;

import java.util.logging.Logger;
import com.google.gwt.logging.client.HasWidgetsLogHandler;
import com.google.gwt.logging.client.LoggingPopup;


/**
 * Author: Lars Parmakerli<br>
 * Freie Universität Berlin<br>
 * corporate semantic web<br>
 * Date: 20/08/2014
 */

public class AspectBasePanel extends Composite {

	ProjectId projectId;


	private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

	@UiTemplate("AspectBasePanel.ui.xml")
	interface MyUiBinder extends UiBinder<Widget, AspectBasePanel> {
	}


    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	final private static AspectServiceAsync proxy = (AspectServiceAsync) GWT.create(AspectService.class);

	private AsyncCallback<Aspects> callbackAspects;
	private AsyncCallback<Void> callbackVoid;

	
    @UiField
    protected FlexTable aspectTable;



	public AspectBasePanel(final ProjectId projectId,
			HasEventHandlerManagement eventHandlerMan) {

		this.projectId = projectId;

        //proxy = (AspectServiceAsync) GWT.create(AspectService.class);

        // build GUI

        initWidget(uiBinder.createAndBindUi(this));

        // get Aspects and add them

		getAspects();

        // Listen for change Events and add Aspects to new Axioms

        // addChangeListener(projectId, eventHandlerMan);


    }
	
	
	void getAspects(){

		callbackAspects = new AsyncCallback<Aspects>() {
			public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
			}

			public void onSuccess(Aspects result) {
				List<IRI> aspects = result.getAspects();

                callbackVoid = new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        //Window.alert("Serverzugriff läuft");
                    }
                };
				//Iterator<Widget> it = changeEventTable.iterator();

                for (IRI aspect : aspects){

                    final CheckBox cb = new CheckBox(aspect.toString());

                    final AspectPanel asPanel = new AspectPanel();

                    //asPanel.setDisplay("Aspect", aspect.toString(), "");

                    cb.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {

                            GWT.log("CHECKBOX HANDLER AUFGERUFEN !!!!!!!!!!!!!!!!!!!!!!!!!!!");

                            boolean checked = cb.getValue();
                            if (checked){
                                GWT.log("CHECKBOX IST GEKLICKT!!!!!!!!!!! " + cb.getText());

                                proxy.addCheckedAspect(IRI.create(cb.getText()), callbackVoid);
                            } else {
                                proxy.removeUnCheckedAspect(IRI.create(cb.getText()), callbackVoid);
                            }
                        }
                    });


                    aspectTable.insertRow(0);

                    System.out.println("Zeile wurde hinzugefügt");


                    aspectTable.setWidget(0, 0, cb);


                }
			}
		};

		proxy.getAspects(projectId, callbackAspects);

	}
	

    void addChangeListener(final ProjectId projectId,     // wahrscheinlich hinfällig
                           final HasEventHandlerManagement eventHandlerMan){


        eventHandlerMan.addProjectEventHandler(ProjectChangedEvent.TYPE,
                new ProjectChangedHandler() {

                    @Override
                    public void handleProjectChanged(ProjectChangedEvent event) {
                        if (event.getProjectId().equals(projectId)) {
                            proxy.postChangeEvent(projectId, event,
                                    callbackVoid);
                        }
                    }
                });



    }
	
}
