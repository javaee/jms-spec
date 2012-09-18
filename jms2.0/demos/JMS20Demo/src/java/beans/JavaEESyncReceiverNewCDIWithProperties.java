package beans;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;

@Stateless
@LocalBean
public class JavaEESyncReceiverNewCDIWithProperties {

    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    private JMSContext context;
    
    @Resource(lookup = "jms/inboundQueue")
    Queue inboundQueue;

    public String receiveMessageNew() {
        JMSConsumer consumer = context.createConsumer(inboundQueue);
        TextMessage textMessage = (TextMessage) consumer.receive(1000);
        if (textMessage==null){
            return "Received null";
        } else {
            try {
                return "Received "+textMessage.getText()+" with foo="+textMessage.getStringProperty("foo");
            } catch (JMSException ex) {
                return ex.getMessage();
            }
        }
    }
}
