package beans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.*;

@Stateless
@LocalBean
public class JavaEESyncReceiverOld {

    @Resource(lookup = "jms/connectionFactory")
    ConnectionFactory connectionFactory;
    @Resource(lookup = "jms/inboundQueue")
    Queue inboundQueue;

    public String receiveMessageOld() {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession();
            MessageConsumer messageConsumer = session.createConsumer(inboundQueue);
            TextMessage textMessage = (TextMessage) messageConsumer.receive(1000);
            if (textMessage==null){
                return "Received null";
            } else {
                return "Received "+textMessage.getText();
            }
        } catch (JMSException ex) {
            Logger.getLogger(JavaEESyncReceiverOld.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException ex) {
                Logger.getLogger(JavaEESyncReceiverOld.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
