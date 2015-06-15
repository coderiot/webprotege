package fu.berlin.csw.dl_learner.client.websocket.service;

/**
 * Created by lars on 04.06.15.
 */

    import com.google.gwt.user.client.rpc.RemoteService;
    import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
    import fu.berlin.csw.dl_learner.shared.Suggestion;

/**
     * The Interface CommandService.
     */
    @RemoteServiceRelativePath("MessageService")
    public interface SuggestionService extends RemoteService {

        Suggestion getMessage(Suggestion suggestion);


}