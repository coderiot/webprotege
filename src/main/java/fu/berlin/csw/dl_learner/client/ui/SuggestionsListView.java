package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
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

    /* ToDo Change to Suggestion !!! */
    private Suggestion selectedSuggestion;
    private ListDataProvider<Suggestion> dataProvider;

    public SuggestionsListView(){


        this.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        TextColumn<Suggestion> nameColumn = new TextColumn<Suggestion>() {
            @Override
            public String getValue(Suggestion suggestion) {
                return suggestion.getClassExpressionManchesterString();
            }
        };

        TextColumn<Suggestion> accuracyColumn = new TextColumn<Suggestion>(){
            @Override
            public String getValue(Suggestion suggestion) {
                return suggestion.getAccuracyAsString();
            }
        };

        // Add a selection model to handle user selection.
        final SingleSelectionModel<Suggestion> selectionModel = new SingleSelectionModel<Suggestion>();
        this.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                Suggestion selected = selectionModel.getSelectedObject();
                selectedSuggestion = selectionModel.getSelectedObject();
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




        dataProvider = new ListDataProvider<Suggestion>();

        dataProvider.addDataDisplay(this);
        list = dataProvider.getList();



        for(Suggestion i : list){
            GWT.log(i.getClassExpressionManchesterString());
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
                            return (o2 != null) ? o1.getClassExpressionManchesterString().compareTo(o2.getClassExpressionManchesterString()) : 1;
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
                            return (o2 != null) ? o1.getAccuracyAsString().compareTo(o2.getAccuracyAsString()) : 1;
                        }
                        return -1;
                    }
                });
        this.addColumnSortHandler(columnSortHandler);


    }

    public void addSuggestion(Suggestion suggestion){
        this.list.add(suggestion);
    }

    public void setList(List<Suggestion> list){
        GWT.log("set SuggestionsListView list with size : " + list.size());

        this.list.clear();

        for (Suggestion suggestion : list){
            this.list.add(suggestion);
            GWT.log("suggestion :::: " + suggestion.getClassExpressionManchesterString());
        }

        dataProvider.refresh();

        //this.list = list;
    }

    public Suggestion getSelectedSuggestion(){
        return this.selectedSuggestion;
    }

    public void clearList(){
        this.list.clear();
    }

}
