package fu.berlin.csw.dl_learner.client.websocket;

/**
 * Created by lars on 04.06.15.
 */

import fu.berlin.csw.dl_learner.shared.ServerReply;

public interface WebSocketHandler {

    void onClose();

    void onError(String error);

    void onMessage(ServerReply suggestion);

    void onOpen();

}