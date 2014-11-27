
/*
 * @(#)TopicRequestor.java	1.9 98/07/08
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

/** JMS provides a TopicRequestor helper class to simplify making service 
  * requests.
  *
  * <P>The TopicRequestor constructor is given a TopicSession and a 
  * destination Topic. It creates a TemporaryTopic for the responses and 
  * provides a request() method that sends the request message and waits 
  * for its reply.
  *
  * <P>This is a basic request/reply abstraction that should be sufficient 
  * for most uses. JMS providers and clients are free to create more 
  * sophisticated versions
  *
  * @version     1.0 - 8 July 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.QueueRequestor
  */

public class TopicRequestor {

    TopicSession    session;    // The topic session the topic belongs to.
    Topic           topic;      // The topic to perform the request/reply on.  
    TemporaryTopic  tempTopic;
    TopicPublisher  publisher;
    TopicSubscriber subscriber;


    /** Constructor for the TopicRequestor class.
      *  
      * @param session the topic session the topic belongs to.
      * @param topic the topic to perform the request/reply call on.
      *
      * @exception JMSException if a JMS error occurs.
      */ 

    public 
    TopicRequestor(TopicSession session, Topic topic) throws JMSException {
	this.session = session;
	this.topic   = topic;
        tempTopic    = session.createTemporaryTopic();
        publisher    = session.createPublisher(topic);
        subscriber   = session.createSubscriber(tempTopic);
    }


    /** Send a request and wait for a reply. The temporary topic is used for
      * replyTo; the first reply is returned and any following replies are
      * discarded.
      *  
      * @param message the message to send.
      *  
      * @return the reply message.
      *  
      * @exception JMSException if a JMS error occurs.
      */

    public Message
    request(Message message) throws JMSException {
	message.setJMSReplyTo(tempTopic);
        publisher.publish(message);

	return(subscriber.receive());
    }


    /** Since a provider may allocate some resources on behalf of a 
      * TopicRequestor outside the JVM, clients should close them when they 
      * are not needed. Relying on garbage collection to eventually reclaim 
      * these resources may not be timely enough.
      *  
      * @exception JMSException if a JMS error occurs.
      */

    public void
    close() throws JMSException {
        tempTopic.delete();
        publisher.close();
        subscriber.close();
	session.close();
    }
}
