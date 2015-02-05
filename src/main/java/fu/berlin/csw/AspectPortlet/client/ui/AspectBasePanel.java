package fu.berlin.csw.AspectPortlet.client.ui;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import edu.stanford.bmir.protege.web.shared.event.HasEventHandlerManagement;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import fu.berlin.csw.AspectPortlet.client.rpc.AspectService;
import fu.berlin.csw.AspectPortlet.client.rpc.AspectServiceAsync;



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

	private static AspectServiceAsync proxy;

	private AsyncCallback<Message> callbackMessage;
	private AsyncCallback<Void> callbackVoid;

	
    @UiField
    protected FlexTable aspectTable;
	
	@UiField
	Button commit;



	public AspectBasePanel(final ProjectId projectId,
			HasEventHandlerManagement eventHandlerMan) {

		this.projectId = projectId;
		getAspects();
		
		// build GUI
		
		
		this.projectId = projectId;


		initWidget(uiBinder.createAndBindUi(this));
		
		System.out.println("AspectBasePanel wird konstruiert");
		
		
		
		final AspectPanel asPanel = new AspectPanel();
		
		System.out.println("AspectPanel wurde erstellt");

		
		asPanel.setDisplay();
		System.out.println("Text wurde gesetzt");
		
		aspectTable.insertRow(0);
		
		System.out.println("Zeile wurde hinzugefügt");

		
		aspectTable.setWidget(0, 0, asPanel);
		
	}
	
	
	void getAspects(){

		proxy = (AspectServiceAsync) GWT.create(AspectService.class);
		
		/*callbackMessage = new AsyncCallback<Message>() {
			public void onFailure(Throwable caught) {
				Window.alert("Commit Error!");
			}

			public void onSuccess(Message result) {
				message = result.getMessage();
				Iterator<Widget> it = changeEventTable.iterator();
				
				while (it.hasNext()){
					CommitChangesEventPanel wg = (CommitChangesEventPanel) it.next();
					wg.setCommitted();
				}
				Window.alert(message);
			}
		}; */
		
		callbackVoid = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());

			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("Serverzugriff läuft");
			}
		};
		
		proxy.init(projectId, callbackVoid);
	}
	
	
	
}
