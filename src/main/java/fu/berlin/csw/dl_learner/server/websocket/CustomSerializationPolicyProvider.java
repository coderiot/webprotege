package fu.berlin.csw.dl_learner.server.websocket;

/**
 * Created by lars on 04.06.15.
 */

import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;

public class CustomSerializationPolicyProvider
        implements SerializationPolicyProvider {

    @Override
    public SerializationPolicy getSerializationPolicy(String moduleBaseURL,
                                                      String serializationPolicyStrongName) {
        return new SimpleSerializationPolicy();
    }

}