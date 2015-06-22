package fu.berlin.csw.dl_learner.client.ui;

import com.google.gwt.core.client.GWT;
import fu.berlin.csw.dl_learner.client.websocket.WebSocket;
import fu.berlin.csw.dl_learner.client.websocket.WebSocketHandler;
import fu.berlin.csw.dl_learner.shared.ServerReply;
import fu.berlin.csw.dl_learner.shared.SuggestionRequest;

/**
 * Created by lars on 04.06.15.
 */
public class SuggestionsPresenter implements WebSocketHandler {

    private WebSocket webSocket;
    private SuggestionsListView suggestionsListView;

    public SuggestionsPresenter(WebSocket webSocket) {
        super();
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
        GWT.log(error);
    }

    @Override
    public void onMessage(ServerReply reply) {

        GWT.log("Suggestion received : " + reply.getData());


        if (reply.getData().equals("Finished")){

            // Computation of class expression suggestions finished

            SuggestionsWindow.showDefaultCursor();
            webSocket.close();
            GWT.log("[DLLearner] Class learning process finished!");
            com.google.gwt.user.client.Window.alert("Class learning process finished!");

        } else if (reply.getData().equals("Error")) {

            // Handle Server Error

            SuggestionsWindow.showDefaultCursor();
            GWT.log("[DLLearner] Server Exception during learning process!", reply.getException());
            webSocket.close();

            if(reply.getException().getMessage()== null){
                com.google.gwt.user.client.Window.alert("Server Exception during learning process! See GWT log for details!" +
                        "\n" + reply.getException().toString());
            } else {
                com.google.gwt.user.client.Window.alert("Server Exception during learning process! See GWT log for details!" +
                        "\n" + reply.getException().getMessage());
            }

        } else {

            //  add class description suggestion

            suggestionsListView.addSuggestion(reply);
        }

    }

    @Override
    public void onOpen() {
        GWT.log("[DLLearner] Web socket open!");
    }


    public void setListView(SuggestionsListView suggestionsListView){
        this.suggestionsListView = suggestionsListView;
    }


}
