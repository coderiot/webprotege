package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.dl_learner.client.websocket.WebSocket;
import fu.berlin.csw.dl_learner.client.websocket.WebSocketHandler;
import fu.berlin.csw.dl_learner.shared.Suggestion;
import fu.berlin.csw.dl_learner.shared.SuggestionRequest;

/**
 * Created by lars on 04.06.15.
 */
public class SuggestionPresenter implements WebSocketHandler {

    //private ChatView view;
    private WebSocket webSocket;
    private SuggestionsListView suggestionsListView;

    public SuggestionPresenter(WebSocket webSocket) {
        super();
        //this.view = view;
        this.webSocket = webSocket;
        webSocket.addWebSocketHandler(this);
    }

    public void sendMessage(SuggestionRequest req) {
        webSocket.sendMessage(req);
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onError(String error) {
        //view.setInfoMessage(error,true);
        GWT.log(error);
    }

    @Override
    public void onMessage(Suggestion suggestion) {
        //view.addMessage(msg);
        GWT.log("Suggestion received : " + suggestion.getData());
        suggestionsListView.addSuggestion(suggestion);


    }

    @Override
    public void onOpen() {
        //view.setInfoMessage("Web Socket Open",false);
        GWT.log("Web socket open!");
    }


    public void setListView(SuggestionsListView suggestionsListView){
        this.suggestionsListView = suggestionsListView;
    }


}
