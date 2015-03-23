package fu.berlin.csw.AspectPortlet.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;

import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

public class AspectPanel extends Composite implements AspectItemDisplay {
	
	
	interface AspectPanelUiBinder extends
	UiBinder<HTMLPanel, AspectPanel> {

}
	private static AspectPanelUiBinder ourUiBinder = GWT
			.create(AspectPanelUiBinder.class);
	
	

    @UiField
    protected InlineLabel typeLabel;

    @UiField
    protected InlineLabel browserTextLabel;

    @UiField
    protected InlineLabel iriLabel;
    
    @UiField
    protected CheckBox box;


    public AspectPanel() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }





    public void setDisplay(String typeLabel, String browserText, String iriLabel) {
        this.typeLabel.setText(typeLabel);
        browserTextLabel.setText(browserText);
        this.iriLabel.setText(iriLabel);
        box.setText(iriLabel);
    }


	@Override
	public void updateElapsedTimeDisplay() {
		// TODO Auto-generated method stub
		
	}
	
	
	
/*	@UiField
	protected InlineLabel TestLabel;
	
	public AspectPanel() {
		HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
		initWidget(rootElement);
	}

	
		@Override
		public void updateElapsedTimeDisplay() {
		// TODO Auto-generated method stub
		
		}
		
		public void setText(String text){
			this.TestLabel.setText(text);
		}  */
		
}


