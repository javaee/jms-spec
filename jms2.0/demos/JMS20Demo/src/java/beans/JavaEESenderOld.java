package beans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.*;

@Stateless
@LocalBean
public class JavaEESenderOld {
   
    @Resource(lookup = "jms/connectionFactory")
    ConnectionFactory connectionFactory;
	
    @Resource(lookup="jms/inboundQueue")
    Queue inboundQueue;

    public void sendMessageOld(String payload) {
        Connection connection=null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            MessageProducer messageProducer = session.createProducer(inboundQueue);
            TextMessage textMessage = session.createTextMessage(payload);
            messageProducer.send(textMessage);
        } catch (JMSException ex) {
            Logger.getLogger(JavaEESenderOld.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection!=null) try {
                connection.close();
            } catch (JMSException ex) {
                Logger.getLogger(JavaEESenderOld.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } 
}
