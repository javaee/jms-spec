package beans;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Stateless
@LocalBean
public class JavaEESenderNewCDIWithProperties {

    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    private JMSContext context;
    
    @Resource(mappedName = "jms/inboundQueue")
    private Queue inboundQueue;

    public void sendMessageNew(String payload) {
        context.createProducer().setPriority(1).setProperty("foo","987654").send(inboundQueue, payload);
    }
}
