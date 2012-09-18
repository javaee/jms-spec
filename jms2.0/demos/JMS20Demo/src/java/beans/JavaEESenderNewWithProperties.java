package beans;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Stateless
@LocalBean
public class JavaEESenderNewWithProperties {

    @Resource(lookup = "jms/connectionFactory")
    ConnectionFactory connectionFactory;
    @Resource(lookup = "jms/inboundQueue")
    Queue inboundQueue;

    public void sendMessageNew(String payload) {

        JMSContext context = connectionFactory.createContext();
        try {
            context.createProducer().setPriority(1).setProperty("foo", "bar").send(inboundQueue, payload);
        } finally {
            context.close();
        }
    }
}
