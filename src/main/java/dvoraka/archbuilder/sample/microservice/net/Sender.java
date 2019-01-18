package dvoraka.archbuilder.sample.microservice.net;

import dvoraka.archbuilder.sample.microservice.data.message.Message;

public interface Sender<M extends Message> {

    void send(M message) throws Exception;
}
