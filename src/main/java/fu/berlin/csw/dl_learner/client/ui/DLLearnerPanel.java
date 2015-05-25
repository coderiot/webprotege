package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.*;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.hp.hpl.jena.vocabulary.OWL;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.search.SearchResultsProxyImpl;
import edu.stanford.bmir.protege.web.client.ui.util.PaginationUtil;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.dl_learner.shared.CommitExecutor;
import fu.berlin.csw.dl_learner.shared.CommitResult;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * The panel used for displaying the search results.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class DLLearnerPanel extends GridPanel {
    private RecordDef recordDef;
    private Store store;
    private GridRowListener gridRowListener;
    private SearchResultsProxyImpl proxy;

    private TextField searchField;
    private Component busyComponent;
    private AsyncCallback<Boolean> asyncCallback;

    private OWLEntity selectedEntity;

    public DLLearnerPanel(){
        this(null);
    }

    public DLLearnerPanel(AsyncCallback<Boolean> asyncCallback){
        this.asyncCallback = asyncCallback;
        createGrid();
        addGridRowListener(getRowListener());
    }

    public void reload(ProjectId projectId, String searchText, ValueType valueType) {
        proxy.resetParams();
        proxy.setProjectName(projectId);
        proxy.setSearchText(searchText);
        proxy.setValueType(valueType);

        if (busyComponent != null) {
            busyComponent.getEl().mask("Searching...", true);
        }
        store.load(0, ((PagingToolbar) this.getBottomToolbar()).getPageSize());
    }

    protected GridRowListener getRowListener() {
        if (gridRowListener == null) {
            gridRowListener = new GridRowListenerAdapter() {
                @Override
                public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
                    onEntityDblClick();
                }
            };
        }
        return gridRowListener;
    }

    protected void onEntityDblClick() {
    }

    protected void createGrid() {
        ColumnConfig browserTextCol1 = new ColumnConfig();
        browserTextCol1.setHeader("Accuracy");
        browserTextCol1.setId("browserText");
        browserTextCol1.setDataIndex("browserText");
        browserTextCol1.setResizable(true);
        browserTextCol1.setSortable(true);
        browserTextCol1.setTooltip("Double click search result to select in tree.");
        browserTextCol1.setWidth(10);

        ColumnConfig browserTextCol2 = new ColumnConfig();
        browserTextCol2.setHeader("Class expression");
        browserTextCol2.setId("browserText");
        browserTextCol2.setDataIndex("browserText");
        browserTextCol2.setResizable(true);
        browserTextCol2.setSortable(true);
        browserTextCol2.setTooltip("Double click search result to select in tree.");
        browserTextCol1.setWidth(20);


        ColumnConfig[] columns = new ColumnConfig[] { browserTextCol1, browserTextCol2 };

        ColumnModel columnModel = new ColumnModel(columns);
        setColumnModel(columnModel);

        recordDef = new RecordDef(new FieldDef[] { new ObjectFieldDef("entity"), new StringFieldDef("browserText") });

        ArrayReader reader = new ArrayReader(recordDef);

        proxy = new SearchResultsProxyImpl(new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                GWT.log("Problems at search", caught);
                if (busyComponent != null) {
                    busyComponent.getEl().unmask();
                }
                emptyContent();
                if (asyncCallback != null) {
                    asyncCallback.onFailure(caught);
                }
            }

            public void onSuccess(Boolean result) {
                if (result == false) {
                    emptyContent();
                }
                if (busyComponent != null) {
                    busyComponent.getEl().unmask();
                }
                if (asyncCallback != null) {
                    asyncCallback.onSuccess(result);
                }
            }
        });

        store = new Store(proxy, reader);
        setStore(store);

        PagingToolbar pToolbar = PaginationUtil.getNewPagingToolbar(store, 20);
        setBottomToolbar(pToolbar);

        addSearchToolbar();

        setStripeRows(true);
        setAutoExpandColumn("browserText");
        setFrame(true);
    }



    public EntityData getSelection() {
        Record selRecord = getSelectionModel().getSelected();
        if (selRecord == null) { return null; }
        return (EntityData) selRecord.getAsObject("entity");
    }


    private void addSearchToolbar() {
        setTopToolbar(new Toolbar());
        Toolbar toolbar = getTopToolbar();
        Button findEqualClassesButton = new Button();

        findEqualClassesButton.setText("find equivalent classes");

        findEqualClassesButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                CommitExecutor cex = new CommitExecutor(DispatchServiceManager.get());
                Application app = Application.get();


                cex.execute(app.getActiveProject().get(), selectedEntity, new DispatchServiceCallback<CommitResult>() {
                    @Override
                    public void handleSuccess(CommitResult result) {
                        MessageBox.showMessage("Commit success.",
                                result.getMessage());
                    }

                    @Override
                    public void handleExecutionException(Throwable cause) {
                        MessageBox.showAlert("Commit failed!",
                                cause.getMessage());
                        UIUtil.hideLoadProgessBar();
                        cause.printStackTrace();
                    }
                });

            }

        });




        final Component searchField = createSearchField();
        if (searchField != null) {
            toolbar.addElement(findEqualClassesButton.getElement());
            toolbar.addText("<i>Search</i>:&nbsp&nbsp");
            //toolbar.addElement(searchField.getElement());
        }
    }

    protected Component createSearchField() {
        searchField = new TextField("Search: ", "search");
        searchField.setValidateOnBlur(false);
        searchField.setSelectOnFocus(true);
        searchField.setWidth(400);
        //searchField.setAutoWidth(true);
        searchField.setEmptyText("Type search string");
        searchField.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onSpecialKey(final Field field, final EventObject e) {
                if (e.getKey() == EventObject.ENTER) {
                    String searchText = searchField.getText();
                    if (searchText != null) {
                        searchText = searchText.trim();
                    }
                    if (searchText.length() > 0) {
                        reload(proxy.getProjectId(), searchText, proxy.getValueType());
                    }
                }
            }

            @Override
            public void onValid(Field field) {
                onSearchTextChange(field.getValueAsString());
            }
        });
        return searchField;
    }

    private void onSearchTextChange(String searchText) {
        if (searchText == null) {
            return;
        }
        searchText = searchText.trim();
        if (searchText.length() > 2) {
            reload(proxy.getProjectId(), searchText, proxy.getValueType());
        } else {
            emptyContent();
        }
    }

    public void setSearchFieldText(String text) {
        searchField.setValue(text);
    }

    public void setProjectId(ProjectId projectId) {
        proxy.setProjectName(projectId);
    }

    public void setAsyncCallback(AsyncCallback<Boolean> asyncCallback) {
        this.asyncCallback = asyncCallback;
    }

    public void setBusyComponent(Component busyComponent) {
        this.busyComponent = busyComponent;
    }

    public void emptyContent() {
        store.removeAll();
    }

    public SearchResultsProxyImpl getProxy() {
        return proxy;
    }

    public void setSelectedEntity(OWLEntity selectedEntity){
        this.selectedEntity = selectedEntity;
    }

}
