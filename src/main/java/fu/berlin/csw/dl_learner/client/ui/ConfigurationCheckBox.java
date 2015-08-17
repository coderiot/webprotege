package fu.berlin.csw.dl_learner.client.ui;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.CheckBox;
import edu.stanford.bmir.protege.web.shared.entity.EntityTypeGroup;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Iterator;

/**
 * Created by lars on 17.08.15.
 */
public class ConfigurationCheckBox extends CheckBox {

    private String configName;

    public void setConfigName(String configName) {
        this.configName = configName;

        setHTML(configName + "    <span style=\"color: gray; font-size: 90%;\"></span>");
    }
}
