package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import fu.berlin.csw.dl_learner.shared.ServerReply;

import java.util.Comparator;
import java.util.List;

/**
 * Created by lars on 09.06.15.
 */
public class SuggestionsListView extends CellTable<ServerReply> {

    private List<ServerReply> list;

    /* ToDo Change to Suggestion !!! */
    private ServerReply selectedSuggestion;

    public SuggestionsListView(){


        this.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        TextColumn<ServerReply> nameColumn = new TextColumn<ServerReply>() {
            @Override
            public String getValue(ServerReply suggestion) {
                return suggestion.getClassExpressionManchesterString();
            }
        };

        TextColumn<ServerReply> accuracyColumn = new TextColumn<ServerReply>(){
            @Override
            public String getValue(ServerReply suggestion) {
                return suggestion.getAccuracy();
            }
        };

        // Add a selection model to handle user selection.
        final SingleSelectionModel<ServerReply> selectionModel = new SingleSelectionModel<ServerReply>();
        this.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                ServerReply selected = selectionModel.getSelectedObject();
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




        ListDataProvider<ServerReply> dataProvider = new ListDataProvider<ServerReply>();

        dataProvider.addDataDisplay(this);
        list = dataProvider.getList();



        for(ServerReply i : list){
            GWT.log(i.getClassExpressionManchesterString());
        }

        ColumnSortEvent.ListHandler<ServerReply> columnSortHandler = new ColumnSortEvent.ListHandler<ServerReply>(
                list);
        columnSortHandler.setComparator(nameColumn,
                new Comparator<ServerReply>() {
                    public int compare(ServerReply o1, ServerReply o2) {
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
                new Comparator<ServerReply>() {
                    public int compare(ServerReply o1, ServerReply o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        // Compare the name columns.
                        if (o1 != null) {
                            return (o2 != null) ? o1.getAccuracy().compareTo(o2.getAccuracy()) : 1;
                        }
                        return -1;
                    }
                });
        this.addColumnSortHandler(columnSortHandler);


    }

    public void addSuggestion(ServerReply suggestion){
        this.list.add(suggestion);
    }

    public ServerReply getSelectedSuggestion(){
        return this.selectedSuggestion;
    }


}
