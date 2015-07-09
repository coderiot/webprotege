package fu.berlin.csw.dl_learner.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.ManchesterSyntaxParsingContextModule;
import edu.stanford.bmir.protege.web.server.inject.ProjectModule;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxChangeGenerator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.frame.SetManchesterSyntaxFrameAction;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * Created by lars on 08.07.15.
 */
public class ApplyChanges {





    protected ChangeListGenerator<Void> getChangeListGenerator(SetManchesterSyntaxFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
        Injector injector = Guice.createInjector(new ProjectModule(project), new ManchesterSyntaxParsingContextModule(action));
        ManchesterSyntaxChangeGenerator changeGenerator = injector.getInstance(ManchesterSyntaxChangeGenerator.class);
        List<OWLOntologyChange> changes = changeGenerator.generateChanges(action.getFromRendering(), action.getToRendering());
        return new FixedChangeListGenerator<Void>(changes);
    }

}
