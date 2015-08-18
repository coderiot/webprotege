package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import org.semanticweb.owlapi.model.AxiomType;

/**
 * Created by lars on 17.08.15.
 */
public class ConfigurationPanel extends Composite{//Window {


    private SuggestionsWindow wrapper;


    @UiTemplate("ConfigurationPanel.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, ConfigurationPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    public CheckBox equivClasses;

    @UiField
    public CheckBox subClasses;

    @UiField
    public CheckBox useSparqlCheckBox;

    @UiField
    public TextBox sparqlEndpointTextBox;

    @UiField
    public IntegerBox maxNumberOfResults;

    @UiField
    public IntegerBox noisePercentage;

    @UiField
    public IntegerBox cardinalityLimit;

    @UiField
    public CheckBox useNegation;

    @UiField
    public CheckBox useCardinalityRestrictions;

    @UiField
    public CheckBox useHasValueConstructor;

    @UiField
    public CheckBox useExistsConstructor;

    @UiField
    public CheckBox useAllConstructor;


    @UiField
    IntegerBox maxExecutionTime;


    @UiField
    com.google.gwt.user.client.ui.Button okbutton;


    @UiHandler("okbutton")
    public void onClick(ClickEvent clickEvent){
        wrapper.hideConfigurationsWindow();
    }


    @UiHandler("equivClasses")
    public void onValueChange4(ValueChangeEvent<Boolean> valueChangeEvent) {
        if (valueChangeEvent.getValue().equals(true)){
            disableSubClasses();
            wrapper.changeTitle();
        } else {
            enableSubClasses();
            wrapper.changeTitle();
        }
    }

    @UiHandler("subClasses")
    public void onValueChange5(ValueChangeEvent<Boolean> valueChangeEvent) {
        if (valueChangeEvent.getValue().equals(true)){
            disableEquivClasses();
            wrapper.changeTitle();
        } else {
            enableEquivClasses();
            wrapper.changeTitle();
        }
    }



    @UiHandler("useSparqlCheckBox")
    public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
        if (valueChangeEvent.getValue().equals(true)){
            enableSparqlTextbox(true);
        } else {
            enableSparqlTextbox(false);
        }
        GWT.log("HALLO BLAH");
    }


    @UiHandler("useCardinalityRestrictions")
    public void onValueChange2(ValueChangeEvent<Boolean> valueChangeEvent) {
        if (valueChangeEvent.getValue().equals(true)){
            enableCardinalityLimit(true);
        } else {
            enableCardinalityLimit(false);
        }
        GWT.log("HALLO BLAH");
    }

    @UiHandler("maxExecutionTime")
    public void onKeyPress(KeyPressEvent event) {
        if (!Character.isDigit(event.getCharCode())
                && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB
                && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE) {
            ((IntegerBox) event.getSource()).cancelKey();
        }
    }

    @UiHandler("cardinalityLimit")
    public void onKeyPress2(KeyPressEvent event) {
        if (!Character.isDigit(event.getCharCode())
                && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB
                && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE) {
            ((IntegerBox) event.getSource()).cancelKey();
        }
    }

    @UiHandler("noisePercentage")
    public void onKeyPress3(KeyPressEvent event) {
        if (!Character.isDigit(event.getCharCode())
                && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB
                && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE) {
            ((IntegerBox) event.getSource()).cancelKey();
        }
    }

    @UiHandler("maxNumberOfResults")
    public void onKeyPress4(KeyPressEvent event) {
        if (!Character.isDigit(event.getCharCode())
                && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB
                && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE) {
            ((IntegerBox) event.getSource()).cancelKey();
        }
    }

    @UiHandler("cardinalityLimit")
    public void onKeyPress5(KeyPressEvent event) {
        if (!Character.isDigit(event.getCharCode())
                && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB
                && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE) {
            ((IntegerBox) event.getSource()).cancelKey();
        }
    }






    public ConfigurationPanel(SuggestionsWindow wrapper){

        this.wrapper = wrapper;

        initWidget(uiBinder.createAndBindUi(this));

        useSparqlCheckBox.setValue(getUseSparqlEndpoint());

        sparqlEndpointTextBox.setEnabled(getUseSparqlEndpoint());
        sparqlEndpointTextBox.setText(getSparqlEndpoint());



    }




    private void enableSparqlTextbox(boolean enabled){
        sparqlEndpointTextBox.setEnabled(enabled);
    }

    private void enableCardinalityLimit(boolean enabled){
        cardinalityLimit.setEnabled(enabled);
    }

    public boolean getUseSparqlEndpoint(){
        return this.useSparqlCheckBox.getValue();
    }

    public void setSparqlEndpoint(String sparqlEndpoint){
        this.sparqlEndpointTextBox.setText(sparqlEndpoint);
    }

    public String getSparqlEndpoint(){
        return this.sparqlEndpointTextBox.getText();
    }

    public boolean getUseExistsConstructorVal(){
        return this.useExistsConstructor.getValue();
    }

    public boolean getUseAllConstructor(){
        return this.useAllConstructor.getValue();
    }

    public boolean getUseHasValueConstructor(){
        return this.useHasValueConstructor.getValue();
    }

    public boolean getUseNegation(){
        return this.useNegation.getValue();
    }

    public int getCardinalityLimit(){
        return this.cardinalityLimit.getValue();
    }

    public int getNoisePercentage(){
        return this.noisePercentage.getValue();
    }

    public int getMaxExecutionTime(){
        return this.maxExecutionTime.getValue();
    }

    public int getMaxNumberOfResults(){
        return this.maxNumberOfResults.getValue();
    }

    public boolean getUseCardinalityRestriction(){
        return this.useCardinalityRestrictions.getValue();
    }


    private void disableEquivClasses(){
        this.equivClasses.setValue(false);
    }

    private void enableEquivClasses(){
        this.equivClasses.setValue(true);
    }


    private void disableSubClasses(){
        this.subClasses.setValue(false);
    }

    private void enableSubClasses(){
        this.subClasses.setValue(true);
    }

    public AxiomType getAxiomType(){
        if (equivClasses.getValue()){
            return AxiomType.EQUIVALENT_CLASSES;
        } else {
            return AxiomType.SUBCLASS_OF;
        }
    }

}
