package fu.berlin.csw.AspectPortlet.client.ui;

import java.util.Collection;
import java.util.Collections;

import com.google.gwt.user.client.Window;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
@SuppressWarnings("unchecked")
public class AspectPortlet extends AbstractOWLEntityPortlet {

    private AspectBasePanel basePanel;

    public AspectPortlet(Project project) {
        super(project);
    }

    public AspectPortlet(Project project, boolean initialize) {
        super(project, initialize);
    }


    @Override
    public void initialize() {
        basePanel = new AspectBasePanel(getProjectId(), this);
        setTitle("Aspect");
        setSize(300, 68);
        add(basePanel);

    }

    @Override
    protected boolean hasRefreshButton() {
        return false;
    }

    @Override
    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
    }




}
