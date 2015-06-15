package fu.berlin.csw.dl_learner.client.websocket;

/**
 * Created by lars on 04.06.15.
 */

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import fu.berlin.csw.dl_learner.client.websocket.exception.WebSocketNotSupportedException;
import fu.berlin.csw.dl_learner.client.websocket.service.SuggestionRequestService;
import fu.berlin.csw.dl_learner.client.websocket.service.SuggestionService;
import fu.berlin.csw.dl_learner.shared.Suggestion;
import fu.berlin.csw.dl_learner.shared.SuggestionRequest;


public class WebSocket {

    private String url;
    private JavaScriptObject ws;
    private ArrayList<WebSocketHandler> handlers = new ArrayList<>();
    private SerializationStreamFactory suggestionRequestFactory;
    private SerializationStreamFactory suggestionFactory;


    public WebSocket(String url) {
        super();
        this.url = url;
        suggestionRequestFactory = (SerializationStreamFactory) GWT.create(SuggestionRequestService.class);
        suggestionFactory = (SerializationStreamFactory) GWT.create(SuggestionService.class);
    }

    public void open() throws WebSocketNotSupportedException {
        try {
            ws = init();
        } catch (JavaScriptException e) {
            throw new WebSocketNotSupportedException();
        }

        GWT.log("webserver successful opened!");

    }

    public void addWebSocketHandler(WebSocketHandler handler) {
        handlers.add(handler);
    }

    public void removeWebSocketHandler(WebSocketHandler handler) {
        handlers.add(handler);
    }

    public void sendMessage(SuggestionRequest req) {
        try {
            final SerializationStreamWriter writer = suggestionRequestFactory
                    .createStreamWriter();

            GWT.log("Trying to serialize Data : ");

            writer.writeObject(req);

            // Sending serialized object content
            final String data = writer.toString();

            GWT.log("Send message : " + data);

            send(data);
        } catch (final SerializationException e) {
            GWT.log(e.getMessage());
        }
    }

    public void close() {
        if (ws != null) {
            destroy();
            ws = null;
        }
    }

    private void onClose() {
        for (WebSocketHandler handler : handlers) {
            handler.onClose();
        }

    }

    private void onError() {
        for (WebSocketHandler handler : handlers) {
            handler.onError("Websocket error");
        }

    }

    private void onMessage(String msg) {
        try {
            final SerializationStreamReader streamReader = suggestionFactory
                    .createStreamReader(msg);
            final Suggestion suggestion = (Suggestion) streamReader.readObject();
            for (WebSocketHandler handler : handlers) {
                handler.onMessage(suggestion);
            }
        } catch (SerializationException e) {
            for (WebSocketHandler handler : handlers) {
                handler.onError("Serialization error");
            }
        }

    }

    private void onOpen() {
        for (WebSocketHandler handler : handlers) {
            handler.onOpen();
        }

    }

    private native JavaScriptObject init() /*-{
        if (!$wnd.WebSocket) {
            throw "WebSocket not supported";
        }
        var websocket = new WebSocket(
            this.@fu.berlin.csw.dl_learner.client.websocket.WebSocket::url);
        var wrapper = this;
        websocket.onopen = function(evt) {
            wrapper.@fu.berlin.csw.dl_learner.client.websocket.WebSocket::onOpen()();
        };
        websocket.onclose = function(evt) {
            wrapper.@fu.berlin.csw.dl_learner.client.websocket.WebSocket::onClose()();
        };
        websocket.onmessage = function(evt) {
            wrapper.@fu.berlin.csw.dl_learner.client.websocket.WebSocket::onMessage(Ljava/lang/String;)(evt.data);
        };
        websocket.onerror = function(evt) {
            wrapper.@fu.berlin.csw.dl_learner.client.websocket.WebSocket::onError()();
        };
        return websocket;
    }-*/;

    private native void send(String message) /*-{
        this.@fu.berlin.csw.dl_learner.client.websocket.WebSocket::ws
            .send(message);
    }-*/;

    private native boolean destroy() /*-{
        this.@fu.berlin.csw.dl_learner.client.websocket.WebSocket::ws.close();
    }-*/;

}