package beans;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;

/**
 * 11.4.4. Receiving a message synchronously (Java EE)
 * 11.4.4.3. Example using the simplified API and injection
 */
@Stateless
@LocalBean
public class JavaEESyncReceiverNewCDI {

    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    private JMSContext context;
    
    @Resource(lookup = "jms/inboundQueue")
    Queue inboundQueue;

    public String receiveMessageNew() {
        JMSConsumer consumer = context.createConsumer(inboundQueue);
        return "Received "+consumer.receivePayload(String.class,1000);
    }
}
