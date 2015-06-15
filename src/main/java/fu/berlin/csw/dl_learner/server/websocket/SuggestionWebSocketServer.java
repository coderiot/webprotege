package fu.berlin.csw.dl_learner.server.websocket;

/**
 * Created by lars on 04.06.15.
 */
import java.io.IOException;
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
import fu.berlin.csw.dl_learner.server.GetSuggestionsActionHandler;
import fu.berlin.csw.dl_learner.server.Manager;
import fu.berlin.csw.dl_learner.shared.Suggestion;
import fu.berlin.csw.dl_learner.shared.SuggestionRequest;
import org.dllearner.learningproblems.EvaluatedDescriptionClass;

@ServerEndpoint(value = "/chat/{projectId}")
public class SuggestionWebSocketServer {

    private Session session;
    private HttpSession httpSession;
    private OWLAPIProjectManager projectManager;

    private static Logger logger = Logger.getLogger(SuggestionWebSocketServer.class
            .getName());


    @OnMessage
    public void onMessage(@PathParam("projectId") String projectIdString, String message, final Session session) {


        Timer timer = new Timer();

        logger.info("message: " + message);
        logger.info("session: " + session);

        try {
            final SuggestionRequest request = deserializeSuggestionRequest(message);

            logger.info("deserialized suggestionRequest: data: " + request.getData());
            logger.info("deserialized suggestionRequest project ID: projectId: " + request.getProjectId());


            timer.schedule(new TimerTask() {
                int i = 0;
                List<EvaluatedDescriptionClass> result;
                boolean learningHasStarted = false;

                @Override
                public void run() {
                    //setProgress(progress);

                    if (Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).isActive()){
                        learningHasStarted = true;

                        result = Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).getCurrentlyLearnedDescriptions();
                        logger.info("Currently learned descriptions: " + result.toString());
                        if(result.size()>i){
                            Suggestion suggestion = new Suggestion();
                            suggestion.setData("Suggestion: " + Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).getCurrentlyLearnedDescriptions().get(i));
                            suggestion.setUsername("Lars");
                            sendSuggestion(suggestion, session);
                            i++;
                        }
                    } else if (learningHasStarted == true) {
                        result = Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).getCurrentlyLearnedDescriptions();
                        logger.info("Currently learned descriptions: " + result.toString());
                        if(result.size()>i){
                            Suggestion suggestion = new Suggestion();
                            suggestion.setData("Suggestion: " + Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).getCurrentlyLearnedDescriptions().get(i));
                            suggestion.setUsername("Lars");
                            sendSuggestion(suggestion, session);
                            i++;
                        }

                        this.cancel();
                    }


                    /*das geht schöner!*/
                    /*if ( !isCancelled() && Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).isLearning()) {

                            learningHasStarted = true;

                            result = Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).getCurrentlyLearnedDescriptions();
                            logger.info("Currently learned descriptions: " + result.toString());
                            if(result.size()>i){
                                Suggestion suggestion = new Suggestion();
                                suggestion.setData("Suggestion: " + Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).getCurrentlyLearnedDescriptions().get(i));
                                suggestion.setUsername("Lars");
                                sendSuggestion(suggestion, session);
                                i++;
                            }
                    } else if (learningHasStarted){
                        result = Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).getCurrentlyLearnedDescriptions();
                        logger.info("Currently learned descriptions: " + result.toString());
                        if(result.size()>i){
                            Suggestion suggestion = new Suggestion();
                            suggestion.setData("Suggestion: " + Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).getCurrentlyLearnedDescriptions().get(i));
                            suggestion.setUsername("Lars");
                            sendSuggestion(suggestion, session);
                            i++;
                        }

                        this.cancel();
                    }*/
                }

            }, 1000, 500);

            //Manager.getInstance().getProjectRelatedLearner(request.getProjectId()).startLearning();


        } catch (final /*Serialization*/Exception e ) {
            logger.log(Level.WARNING, "Error on web socket server", e);
            timer.cancel();
        }



    }



    @OnOpen
    public void onOpen(@PathParam("projectId") String projectIdString, Session session, EndpointConfig conf) {
        this.session = session;    // Todo: add multiple opened sessions

        logger.log(Level.INFO, "Connection open for:" + session.getId());

    }


    /*   Serialization for Suggestion      */

    private Suggestion deserializeSuggestion(String data)
            throws SerializationException {
        final ServerSerializationStreamReader streamReader = new ServerSerializationStreamReader(
                Thread.currentThread().getContextClassLoader(),
                new CustomSerializationPolicyProvider());
        // Filling stream reader with data
        streamReader.prepareToRead(data);
        // Reading deserialized object from the stream
        final Suggestion suggestion = (Suggestion) streamReader.readObject();
        return suggestion;
    }

    private String serializeSuggestion(final Suggestion messageDto)
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


    private void sendSuggestion(Suggestion suggestion, Session session) {

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
