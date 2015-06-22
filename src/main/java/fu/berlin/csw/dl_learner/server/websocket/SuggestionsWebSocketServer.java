package fu.berlin.csw.dl_learner.server.websocket;

/**
 * Created by lars on 04.06.15.
 */
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import fu.berlin.csw.dl_learner.server.Manager;
import fu.berlin.csw.dl_learner.shared.ServerReply;
import fu.berlin.csw.dl_learner.shared.SuggestionRequest;
import org.dllearner.learningproblems.EvaluatedDescriptionClass;
import org.dllearner.utilities.owl.OWLAPIRenderers;

@ServerEndpoint(value = "/suggestions/{projectId}")
public class SuggestionsWebSocketServer {

    private Session session;
    private HttpSession httpSession;
    private OWLAPIProjectManager projectManager;

    private static Logger logger = Logger.getLogger(SuggestionsWebSocketServer.class
            .getName());


    @OnMessage
    public void onMessage(@PathParam("projectId") String projectIdString, String message, final Session session) {

        final List<EvaluatedDescriptionClass> alreadySendDescriptions = new LinkedList<>();


        final Timer timer = new Timer();

        try {
            final SuggestionRequest request = deserializeSuggestionRequest(message);

            logger.info("[DLLearner] deserialized suggestionRequest: data: " + request.getData());
            logger.info("[DLLearner] deserialized suggestionRequest project ID: projectId: " + request.getProjectId());


            // ask for solutions and send them to the client gradually

            timer.schedule(new TimerTask() {
                int i = 0;
                List<EvaluatedDescriptionClass> result;
                boolean learningHasStarted = false;

                @Override
                public void run() {
                    //setProgress(progress);


                    /*das geht schöner!*/
                    if ( /*!isCancelled() &&*/  Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).isLearning()) {

                            result = Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).getCurrentlyLearnedDescriptions();
                            logger.info("[DLLearner] Currently learned descriptions: " + result.toString());
                            if(result.size()>i){

                                for (EvaluatedDescriptionClass descr : Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).getCurrentlyLearnedDescriptions()){
                                    if (!alreadySendDescriptions.contains(descr)){
                                        alreadySendDescriptions.add(descr);
                                        ServerReply suggestion = new ServerReply();
                                        suggestion.setData(OWLAPIRenderers.toManchesterOWLSyntax(descr.getDescription()));
                                        suggestion.setAccuracy(new Double(descr.getAccuracy()).toString());
                                        logger.info("[DLLearner] Send suggestion : " + result.toString());
                                        sendSuggestion(suggestion, session);
                                    }
                                }

                                i++;
                            }

                    }
                }

            }, 10, 500);


            Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).startLearning();

            // Wait 500ms to make sure that all solutions have already been send to the client

            Timer timer2 = new Timer();

            timer2.schedule(new TimerTask(){
                @Override
                public void run() {
                    timer.cancel();
                    ServerReply suggestion = new ServerReply();
                    suggestion.setData("Finished");
                    suggestion.setUsername("Lars");
                    sendSuggestion(suggestion, session);
                }
            }, 500);


        } catch (Exception e ) {


            logger.log(Level.SEVERE, "[DLLearner] Error on web socket server", e);
            timer.cancel();

            ServerReply suggestion = new ServerReply();
            suggestion.setData("Error");
            suggestion.setUsername("Lars");
            suggestion.setException(e);
            sendSuggestion(suggestion, session);

        }

    }



    @OnOpen
    public void onOpen(@PathParam("projectId") String projectIdString, Session session, EndpointConfig conf) {
        this.session = session;    // Todo: add multiple opened sessions

        logger.log(Level.INFO, "[DLLearner] Connection open for:" + session.getId());

    }


    /*   Serialization      */

    private ServerReply deserializeSuggestion(String data)
            throws SerializationException {
        final ServerSerializationStreamReader streamReader = new ServerSerializationStreamReader(
                Thread.currentThread().getContextClassLoader(),
                new CustomSerializationPolicyProvider());
        // Filling stream reader with data
        streamReader.prepareToRead(data);
        // Reading deserialized object from the stream
        final ServerReply suggestion = (ServerReply) streamReader.readObject();
        return suggestion;
    }

    private String serializeSuggestion(final ServerReply messageDto)
            throws SerializationException {
        final ServerSerializationStreamWriter serverSerializationStreamWriter = new ServerSerializationStreamWriter(
                new SimpleSerializationPolicy());

        serverSerializationStreamWriter.writeObject(messageDto);
        String result = serverSerializationStreamWriter.toString();
        return result;
    }


    /*   Serialization for SuggestionRequest      */


    private SuggestionRequest deserializeSuggestionRequest(String data)
            throws SerializationException {
        final ServerSerializationStreamReader streamReader = new ServerSerializationStreamReader(
                Thread.currentThread().getContextClassLoader(),
                new CustomSerializationPolicyProvider());
        // Filling stream reader with data
        streamReader.prepareToRead(data);
        // Reading deserialized object from the stream
        final SuggestionRequest suggestionReq = (SuggestionRequest) streamReader.readObject();
        return suggestionReq;
    }

    private String serializeSuggestionRequest(final SuggestionRequest suggestionReq)
            throws SerializationException {
        final ServerSerializationStreamWriter serverSerializationStreamWriter = new ServerSerializationStreamWriter(
                new SimpleSerializationPolicy());

        serverSerializationStreamWriter.writeObject(suggestionReq);
        String result = serverSerializationStreamWriter.toString();
        return result;
    }




    private void sendSuggestion(ServerReply suggestion, Session session) {

        String result = null;
        try {
            result = serializeSuggestion(suggestion);

            try {
                session.getBasicRemote().sendText(result);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SerializationException e) {
            e.printStackTrace();
        }

    }




}
