
/*
 * @(#)TopicPublisher.java	1.20 98/10/07
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

/** A client uses a TopicPublisher for publishing messages on a topic. 
  * TopicPublisher is the Pub/Sub variant of a JMS message producer.
  *
  * <P>Normally the Topic is specified when a TopicPublisher is created and 
  * in this case, attempting to use the methods for an unidentified 
  * TopicPublisher will throws an UnsupportedOperationException.
  *
  * <P>In the case that the TopicPublisher with an unidentified Topic is 
  * created, the methods that assume the Topic has been identified throw 
  * an UnsupportedOperationException.
  *
  * @version     1.0 - 4 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  */

public interface TopicPublisher extends MessageProducer {

    /** Get the topic associated with this publisher.
      *
      * @return this publisher's topic
      *  
      * @exception JMSException if JMS fails to get topic for
      *                         this topic publisher
      *                         due to some internal error.
      */

    Topic 
    getTopic() throws JMSException;

 
    /** Publish a Message to the topic
      * Use the topics default delivery mode, timeToLive and priority. 
      *
      * @param message the message to publish
      *
      * @exception JMSException if JMS fails to publish the message
      *                         due to some internal error.
      * @exception MessageFormatException if invalid message specified
      * @exception InvalidDestinationException if a client uses
      *                         this method with a Topic Publisher with
      *                         an invalid topic.
      */

    void 
    publish(Message message) throws JMSException;


    /** Publish a Message to the topic specifying delivery mode, priority 
      * and time to live to the topic.
      *
      * @param message the message to publish
      * @param deliveryMode the delivery mode to use
      * @param priority the priority for this message
      * @param timeToLive the message's lifetime (in milliseconds).
      *
      * @exception JMSException if JMS fails to publish the message
      *                         due to some internal error.
      * @exception MessageFormatException if invalid message specified
      * @exception InvalidDestinationException if a client uses
      *                         this method with a Topic Publisher with
      *                         an invalid topic.
      */
 
    void
    publish(Message message, 
            int deliveryMode, 
	    int priority,
	    long timeToLive) throws JMSException;


    /** Publish a Message to a topic for an unidentified message producer.	
      * Use the topics default delivery mode, timeToLive and priority.
      *  
      * <P>Typically a JMS message producer is assigned a topic at creation 
      * time; however, JMS also supports unidentified message producers 
      * which require that the topic be supplied on every message publish.
      *
      * @param topic the topic to publish this message to
      * @param message the message to send
      *  
      * @exception JMSException if JMS fails to publish the message
      *                         due to some internal error.
      * @exception MessageFormatException if invalid message specified
      * @exception InvalidDestinationException if a client uses
      *                         this method with an invalid topic.
      */ 

    void
    publish(Topic topic, Message message) throws JMSException;


    /** Publish a Message to a topic for an unidentified message producer,
      * specifying delivery mode, priority and time to live.
      *  
      * <P>Typically a JMS message producer is assigned a topic at creation
      * time; however, JMS also supports unidentified message producers
      * which require that the topic be supplied on every message publish.
      *
      * @param topic the topic to publish this message to
      * @param message the message to send
      * @param deliveryMode the delivery mode to use
      * @param priority the priority for this message
      * @param timeToLive the message's lifetime (in milliseconds).
      *  
      * @exception JMSException if JMS fails to publish the message
      *                         due to some internal error.
      * @exception MessageFormatException if invalid message specified
      * @exception InvalidDestinationException if a client uses
      *                         this method with an invalid topic.
      */ 

    void
    publish(Topic topic, 
            Message message, 
            int deliveryMode, 
            int priority,
	    long timeToLive) throws JMSException;
}
