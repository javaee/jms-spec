package beans;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Stateless
@LocalBean
public class JavaEESyncReceiverNew {

    @Resource(lookup = "jms/connectionFactory")
    ConnectionFactory connectionFactory;
    @Resource(lookup = "jms/inboundQueue")
    Queue inboundQueue;

    public String receiveMessageNew() {
        JMSContext context = connectionFactory.createContext();
        try {
            JMSConsumer consumer = context.createConsumer(inboundQueue);
            return "Received " + consumer.receivePayload(String.class,1000);
        } finally {
            context.close();
        }
    }
}
