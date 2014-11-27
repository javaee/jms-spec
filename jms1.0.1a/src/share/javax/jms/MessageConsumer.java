
/*
 * @(#)MessageConsumer.java	1.7 98/10/07
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

/** The parent interface for all message consumers.
  *
  * <P>A client uses a message consumer to receive messages from a Destination.
  * It is created by passing a Destination to a create message consumer method 
  * supplied by a Session.
  *
  * <P>A message consumer can be created with a message selector. This allows 
  * the client to restrict the messages delivered to the message consumer to 
  * those that match the selector.
  *
  * <P>Although a session allows the creation of multiple message consumer's 
  * per Destination, it will only deliver each message for a Destination to one 
  * message consumer. If more than one message consumer could receive it, the 
  * session randomly selects one to deliver it to.
  *
  * <P>A client may either synchronously receive a message consumer's messages 
  * or have the consumer asynchronously deliver them as they arrive.
  *
  * <P>A client can request the next message from a message consumer using one 
  * of its receive methods. There are several variations of receive that allow a 
  * client to poll or wait for the next message.
  *
  * <P>A client can register a MessageListener object with a message consumer. 
  * As messages arrive at the message consumer, it delivers them by calling the 
  * MessageListener's onMessage method.
  *
  * <P>It is a client programming error for a MessageListener to throw an 
  * exception.
  *
  * @version     1.0 - 13 March 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.QueueReceiver
  * @see         javax.jms.TopicSubscriber
  */

public interface MessageConsumer {

    /** Get this message consumer's message selector expression.
      *  
      * @return this message consumer's message selector
      *  
      * @exception JMSException if JMS fails to get message
      *                         selector due to some JMS error
      */ 

    String
    getMessageSelector() throws JMSException;


    /** Get the message consumer's MessageListener.
      *  
      * @return the listener for the message consumer, or null if this isn't
      * one set.
      *  
      * @exception JMSException if JMS fails to get message
      *                         listener due to some JMS error
      * @see javax.jms.MessageConsumer#setMessageListener
      */ 

    MessageListener
    getMessageListener() throws JMSException;


    /** Set the message consumer's MessageListener.
      *  
      * @param messageListener the messages are delivered to this listener
      *  
      * @exception JMSException if JMS fails to set message
      *                         listener due to some JMS error
      * @see javax.jms.MessageConsumer#getMessageListener
      */ 

    void
    setMessageListener(MessageListener listener) throws JMSException;


    /** Receive the next message produced for this message consumer.
      *  
      * <P>This call blocks indefinitely until a message is produced.
      *
      * <P>If this receive is done within a transaction, the message
      * remains on the consumer until the transaction commits.
      *  
      * @exception JMSException if JMS fails to receive the next
      *                         message due to some error.
      *  
      * @return the next message produced for this message consumer.
      */ 
 
    Message
    receive() throws JMSException;


    /** Receive the next message that arrives within the specified
      * timeout interval.
      *  
      * <P>This call blocks until either a message arrives or the
      * timeout expires.
      *  
      * @param timeout the timeout value (in milliseconds)
      *
      * @exception JMSException if JMS fails to receive the next
      *                         message due to some error.
      * @return the next message produced for this message consumer, or 
      * null if one is not available.
      */ 

    Message
    receive(long timeOut) throws JMSException;


    /** Receive the next message if one is immediately available.
      *  
      * @exception JMSException if JMS fails to receive the next
      *                         message due to some error.
      * @return the next message produced for this message consumer, or 
      * null if one is not available.
      */ 

    Message
    receiveNoWait() throws JMSException;


    /** Since a provider may allocate some resources on behalf of a
      * MessageConsumer outside the JVM, clients should close them when they
      * are not needed. Relying on garbage collection to eventually reclaim
      * these resources may not be timely enough.
      *  
      * @exception JMSException if JMS fails to close the consumer
      *                         due to some error.
      */ 

    void
    close() throws JMSException;
}
