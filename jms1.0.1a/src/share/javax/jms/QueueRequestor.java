
/*
 * @(#)QueueRequestor.java	1.9 98/07/08
 * 
 * Copyright (c) 1997-1998 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 * 
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 * 
 */

package javax.jms;

/** JMS provides a QueueRequestor helper class to simplify making service 
  * requests.
  *
  * <P>The QueueRequestor constructor is given a QueueSession and a 
  * destination Queue. It creates a TemporaryQueue for the responses and 
  * provides a request() method that sends the request message and waits 
  * for its reply.
  *
  * <P>This is a basic request/reply abstraction that should be sufficient 
  * for most uses. JMS providers and clients are free to create more 
  * sophisticated versions.
  *
  * @version     1.0 - 8 July 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.TopicRequestor
  */

public class QueueRequestor {

    QueueSession   session;     // The queue session the queue belongs to.
    Queue          queue;       // The queue to perform the request/reply on.
    TemporaryQueue tempQueue;
    QueueSender    sender;
    QueueReceiver  receiver;


    /** Constructor for the QueueRequestor class.
      *  
      * @param session the queue session the queue belongs to.
      * @param queue the queue to perform the request/reply call on.
      *  
      * @exception JMSException if a JMS error occurs.
      */ 

    public
    QueueRequestor(QueueSession session, Queue queue) throws JMSException {
        this.session = session;
        this.queue   = queue;
        tempQueue    = session.createTemporaryQueue();
        sender       = session.createSender(queue);
        receiver     = session.createReceiver(tempQueue);
    }


    /** Send a request and wait for a reply. The temporary queue is used for
      * replyTo, and only one reply per request is expected.
      *  
      * @param message the message to send.
      *  
      * @return the reply message.
      *  
      * @exception JMSException if a JMS error occurs.
      */

    public Message
    request(Message message) throws JMSException {
	message.setJMSReplyTo(tempQueue);
	sender.send(message);

	return(receiver.receive());
    }


    /** Since a provider may allocate some resources on behalf of a 
      * QueueRequestor outside the JVM, clients should close them when they 
      * are not needed. Relying on garbage collection to eventually reclaim 
      * these resources may not be timely enough.
      *  
      * @exception JMSException if a JMS error occurs.
      */

    public void
    close() throws JMSException {
        tempQueue.delete();
        sender.close();
        receiver.close();
	session.close();
    }
}
