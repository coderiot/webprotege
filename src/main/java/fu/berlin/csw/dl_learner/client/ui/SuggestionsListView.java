package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import fu.berlin.csw.dl_learner.shared.Suggestion;

import java.util.Comparator;
import java.util.List;

/**
 * Created by lars on 09.06.15.
 */
public class SuggestionsListView extends CellTable<Suggestion> {

    private List<Suggestion> list;

    public SuggestionsListView(){

        this.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        TextColumn<Suggestion> nameColumn = new TextColumn<Suggestion>() {
            @Override
            public String getValue(Suggestion suggestion) {
                return suggestion.getData();
            }
        };

        TextColumn<Suggestion> accuracyColumn = new TextColumn<Suggestion>(){
            @Override
            public String getValue(Suggestion suggestion) {
                return suggestion.getUsername();
            }
        };

        // Add a selection model to handle user selection.
        final SingleSelectionModel<Suggestion> selectionModel = new SingleSelectionModel<Suggestion>();
        this.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                Suggestion selected = selectionModel.getSelectedObject();
                if (selected != null) {
                    //Window.alert("You selected: " + selected.getData());
                }
            }
        });

        this.addColumn(nameColumn, "Suggested Class Expression");
        nameColumn.setSortable(true);
        this.addColumn(accuracyColumn, "Accuracy");
        accuracyColumn.setSortable(true);

        this.setWidth("100%", true);
        this.setColumnWidth(nameColumn, 70.0, com.google.gwt.dom.client.Style.Unit.PCT);
        this.setColumnWidth(accuracyColumn, 30.0, com.google.gwt.dom.client.Style.Unit.PCT);
        //suggestionsTable.setVisibleRange(0, 5);



        Suggestion sugg = new Suggestion();
        sugg.setData("JHallo");
        sugg.setUsername("Peter");

        Suggestion sugg2 = new Suggestion();
        sugg2.setData("CHallo");
        sugg2.setUsername("Klaus");

        Suggestion sugg3 = new Suggestion();
        sugg3.setData("BHallo");
        sugg3.setUsername("Karl");

        Suggestion sugg4 = new Suggestion();
        sugg4.setData("FHallo");
        sugg4.setUsername("Oli");

        Suggestion sugg5 = new Suggestion();
        sugg5.setData("DHallo");
        sugg5.setUsername("Kevin");

        Suggestion sugg6 = new Suggestion();
        sugg6.setData("AHallo");
        sugg6.setUsername("Pascal");



        ListDataProvider<Suggestion> dataProvider = new ListDataProvider<Suggestion>();

        dataProvider.addDataDisplay(this);
        list = dataProvider.getList();

        /*
        list.add(sugg);
        list.add(sugg2);
        list.add(sugg3);
        list.add(sugg4);
        list.add(sugg5);
        list.add(sugg6);
        */


        for(Suggestion i : list){
            GWT.log(i.getData());
        }

        ColumnSortEvent.ListHandler<Suggestion> columnSortHandler = new ColumnSortEvent.ListHandler<Suggestion>(
                list);
        columnSortHandler.setComparator(nameColumn,
                new Comparator<Suggestion>() {
                    public int compare(Suggestion o1, Suggestion o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        // Compare the name columns.
                        if (o1 != null) {
                            return (o2 != null) ? o1.getData().compareTo(o2.getData()) : 1;
                        }
                        return -1;
                    }
                });
        this.addColumnSortHandler(columnSortHandler);

        columnSortHandler.setComparator(accuracyColumn,
                new Comparator<Suggestion>() {
                    public int compare(Suggestion o1, Suggestion o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        // Compare the name columns.
                        if (o1 != null) {
                            return (o2 != null) ? o1.getUsername().compareTo(o2.getUsername()) : 1;
                        }
                        return -1;
                    }
                });
        this.addColumnSortHandler(columnSortHandler);


    }

    public void addSuggestion(Suggestion suggestion){
        this.list.add(suggestion);
    }



}
