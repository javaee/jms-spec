
/*
 * @(#)TopicSubscriber.java	1.20 98/09/30
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

/** A client uses a TopicSubscriber for receiving messages that have been 
  * published to a topic. TopicSubscriber is the Pub/Sub variant of a JMS 
  * message consumer.
  *
  * <P>Regular TopicSubscriber's are not durable. They only receive messages
  * that are published while they are active.
  *
  * <P>Messages filtered out by a subscriber's message selector will never 
  * be delivered to the subscriber. From the subscriber's perspective they 
  * simply don't exist.
  *
  * <P>In some cases, a connection may both publish and subscribe to a topic.
  * The subscriber NoLocal attribute allows a subscriber to inhibit the 
  * delivery of messages published by its own connection.
  *
  * <P>If a client needs to receive all the messages published on a topic 
  * including the ones published while the subscriber is inactive, it uses 
  * a durable TopicSubscriber. JMS retains a record of this durable 
  * subscription and insures that all messages from the topic's publishers 
  * are retained until they are either acknowledged by this durable 
  * subscriber or they have expired.
  *
  * <P>Sessions with durable subscribers must always provide the same client 
  * identifier. In addition, each client must specify a name which uniquely 
  * identifies (within client identifier) each durable subscription it creates.
  *  Only one session at a time can have a TopicSubscriber for a particular 
  * durable subscription. 
  *
  * <P>A client can change an existing durable subscription by creating a 
  * durable TopicSubscriber with the same name and a new topic and/or message 
  * selector. Changing a durable subscription is equivalent to deleting and 
  * recreating it.
  *
  * <P>TopicSessions provide the unsubscribe method for deleting a durable 
  * subscription created by their client. This deletes the state being 
  * maintained on behalf of the subscriber by its provider.
  * 
  * @version     1.0 - 7 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.MessageConsumer
  */

public interface TopicSubscriber extends MessageConsumer {

    /** Get the topic associated with this subscriber.
      *  
      * @return this subscriber's topic
      *  
      * @exception JMSException if JMS fails to get topic for
      *                         this topic subscriber
      *                         due to some internal error.
      */ 

    Topic
    getTopic() throws JMSException;


    /** Get the NoLocal attribute for this TopicSubscriber.
      * The default value for this attribute is false.
      *  
      * @return set to true if locally published messages are being inhibited.
      *  
      * @exception JMSException if JMS fails to get noLocal attribute for
      *                         this topic subscriber
      *                         due to some internal error.
      */ 

    boolean
    getNoLocal() throws JMSException;
}
