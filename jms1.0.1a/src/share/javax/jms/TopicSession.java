
/*
 * @(#)TopicSession.java	1.25 98/10/07
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

/** A TopicSession provides methods for creating TopicPublisher's, 
  * TopicSubscriber's and TemporaryTopics. It also provides a method for 
  * deleting its client's durable subscribers.
  *
  * @version     1.0 - 4 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.Session
  */

public interface TopicSession extends Session {

    /** Create a Topic given a Topic name.
      *
      * <P>This facility is provided for the rare cases where clients need to
      * dynamically manipulate topic identity. This allows the creation of a
      * topic with a provider specific name. Clients that depend on this
      * ability are not portable.
      *
      * @param topicName the name of this topic
      *
      * @return a Topic with the given name.
      *
      * @exception JMSException if a session fails to create a topic
      *                         due to some JMS error.
      */

    Topic
    createTopic(String topicName) throws JMSException;


    /** Create a non-durable Subscriber to the specified topic.
      *  
      * <P>A client uses a TopicSubscriber for receiving messages that have 
      * been published to a topic.
      *
      * <P>Regular TopicSubscriber's are not durable. They only receive 
      * messages that are published while they are active.
      *
      * <P>In some cases, a connection may both publish and subscribe to a 
      * topic. The subscriber NoLocal attribute allows a subscriber to 
      * inhibit the delivery of messages published by its own connection.
      * The default value for this attribute is false.
      *
      * @param topic the topic to subscribe to
      *  
      * @exception JMSException if a session fails to create a subscriber
      *                         due to some JMS error.
      * @exception InvalidDestinationException if invalid Topic specified.
      */ 

    TopicSubscriber
    createSubscriber(Topic topic) throws JMSException;


    /** Create a non-durable Subscriber to the specified topic.
      *
      * <P>A client uses a TopicSubscriber for receiving messages that have 
      * been published to a topic.
      *  
      * <P>Regular TopicSubscriber's are not durable. They only receive 
      * messages that are published while they are active.
      *
      * <P>Messages filtered out by a subscriber's message selector will 
      * never be delivered to the subscriber. From the subscriber's 
      * perspective they simply don't exist.
      *
      * <P>In some cases, a connection may both publish and subscribe to a 
      * topic. The subscriber NoLocal attribute allows a subscriber to 
      * inhibit the delivery of messages published by its own connection.
      *
      * @param topic the topic to subscribe to
      * @param messageSelector only messages with properties matching the
      * message selector expression are delivered. This value may be null.
      * @param noLocal if set, inhibits the delivery of messages published
      * by its own connection.
      * 
      * @exception JMSException if a session fails to create a subscriber
      *                         due to some JMS error or invalid selector.
      * @exception InvalidDestinationException if invalid Topic specified.
      * @exception InvalidSelectorException if the message selector is invalid.
      */

    TopicSubscriber 
    createSubscriber(Topic topic, 
		     String messageSelector,
		     boolean noLocal) throws JMSException;


    /** Create a durable Subscriber to the specified topic.
      *  
      * <P>If a client needs to receive all the messages published on a 
      * topic including the ones published while the subscriber is inactive,
      * it uses a durable TopicSubscriber. JMS retains a record of this 
      * durable subscription and insures that all messages from the topic's 
      * publishers are retained until they are either acknowledged by this 
      * durable subscriber or they have expired.
      *
      * <P>Sessions with durable subscribers must always provide the same 
      * client identifier. In addition, each client must specify a name which 
      * uniquely identifies (within client identifier) each durable 
      * subscription it creates. Only one session at a time can have a 
      * TopicSubscriber for a particular durable subscription.
      *
      * <P>A client can change an existing durable subscription by creating 
      * a durable TopicSubscriber with the same name and a new topic and/or 
      * message selector. Changing a durable subscriber is equivalent to 
      * deleting and recreating it
      *
      * @param topic the topic to subscribe to
      * @param name the name used to identify this subscription.
      *  
      * @exception JMSException if a session fails to create a subscriber
      *                         due to some JMS error.
      * @exception InvalidDestinationException if invalid Topic specified.
      */ 

    TopicSubscriber
    createDurableSubscriber(Topic topic, 
			    String name) throws JMSException;


    /** Create a durable Subscriber to the specified topic.
      *  
      * <P>If a client needs to receive all the messages published on a 
      * topic including the ones published while the subscriber is inactive,
      * it uses a durable TopicSubscriber. JMS retains a record of this 
      * durable subscription and insures that all messages from the topic's 
      * publishers are retained until they are either acknowledged by this 
      * durable subscriber or they have expired.
      *
      * <P>Sessions with durable subscribers must always provide the same
      * client identifier. In addition, each client must specify a name which
      * uniquely identifies (within client identifier) each durable
      * subscription it creates. Only one session at a time can have a
      * TopicSubscriber for a particular durable subscription.
      *  
      * <P>A client can change an existing durable subscription by creating 
      * a durable TopicSubscriber with the same name and a new topic and/or 
      * message selector. Changing a durable subscriber is equivalent to 
      * deleting and recreating it
      *
      * @param topic the topic to subscribe to
      * @param name the name used to identify this subscription.
      * @param messageSelector only messages with properties matching the
      * message selector expression are delivered. This value may be null.
      * @param noLocal if set, inhibits the delivery of messages published
      * by its own connection.
      *  
      * @exception JMSException if a session fails to create a subscriber
      *                         due to some JMS error or invalid selector.
      * @exception InvalidDestinationException if invalid Topic specified.
      * @exception InvalidSelectorException if the message selector is invalid.
      */ 
 
    TopicSubscriber
    createDurableSubscriber(Topic topic,
                            String name, 
			    String messageSelector,
			    boolean noLocal) throws JMSException;


    /** Create a Publisher for the specified topic.
      *
      * <P>A client uses a TopicPublisher for publishing messages on a topic.
      * Each time a client creates a TopicPublisher on a topic, it defines a 
      * new sequence of messages that have no ordering relationship with the 
      * messages it has previously sent.
      *
      * @param topic the topic to publish to, or null if this is an 
      * unidentifed producer.
      *
      * @exception JMSException if a session fails to create a publisher
      *                         due to some JMS error.
      * @exception InvalidDestinationException if invalid Topic specified.
     */

    TopicPublisher 
    createPublisher(Topic topic) throws JMSException;


    /** Create a temporary topic. It's lifetime will be that of the 
      * TopicConnection unless deleted earlier.
      *
      * @return a temporary topic.
      *
      * @exception JMSException if a session fails to create a temporary
      *                         topic due to some JMS error.
      */
 
    TemporaryTopic
    createTemporaryTopic() throws JMSException;


    /** Unsubscribe a durable subscription that has been created by a client.
      *  
      * <P>This deletes the state being maintained on behalf of the 
      * subscriber by its provider.
      *
      * @param name the name used to identify this subscription.
      *  
      * @exception JMSException if JMS fails to unsubscribe to durable
      *                         subscription due to some JMS error.
      * @exception InvalidDestinationException if invalid Topic specified.
      *                          
      */

    void
    unsubscribe(String name) throws JMSException;
}
