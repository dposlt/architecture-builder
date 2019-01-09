package dvoraka.archbuilder.test.microservice.net;

import dvoraka.archbuilder.test.microservice.data.message.Message;

public interface NetworkComponent<M extends Message, R extends Message>
        extends Sender<M>, Receiver<R> {
}
