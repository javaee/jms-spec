
/*
 * @(#)MessageProducer.java	1.10 98/10/07
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

/** A client uses a message producer to send messages to a Destination. It is 
  * created by passing a Destination to a create message producer method 
  * supplied by a Session.
  *
  * <P>A client also has the option of creating a message producer without 
  * supplying a Destination. In this case, a Destination must be input on 
  * every send operation. A typical use for this style of message producer is
  * to send replies to requests using the request's replyTo Destination.
  *
  * <P>A client can specify a default delivery mode, priority and time-to-live 
  * for messages sent by a message producer. It can also specify delivery 
  * mode, priority and time-to-live per message.
  *
  * <P>A client can specify a time-to-live value in milliseconds for each
  * message it sends. This value defines a message expiration time which
  * is the sum of the message's time-to-live and the GMT it is sent (for
  * transacted sends, this is the time the client sends the message, not
  * the time the transaction is committed).
  *
  * <P>A JMS provider should do its best to accurately expire messages;
  * however, JMS does not define the accuracy provided.
  *
  * @version     1.0 - 3 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.TopicPublisher
  * @see         javax.jms.QueueSender
  */

public interface MessageProducer {

    /** Set whether message IDs are disabled.
      *  
      * <P>Since message ID's take some effort to create and increase a
      * message's size, some JMS providers may be able to optimize message
      * overhead if they are given a hint that message ID is not used by
      * an application. JMS message Producers provide a hint to disable
      * message ID. When a client sets a Producer to disable message ID
      * they are saying that they do not depend on the value of message
      * ID for the messages it produces. These messages must either have
      * message ID set to null or, if the hint is ignored, messageID must
      * be set to its normal unique value.
      *
      * <P>Message IDs are enabled by default.
      *
      * @param value indicates if message IDs are disabled.
      *  
      * @exception JMSException if JMS fails to set disabled message
      *                         Id due to some internal error.
      */ 

    void
    setDisableMessageID(boolean value) throws JMSException;


    /** Get an indication of whether message IDs are disabled.
      *  
      * @return an indication of whether message IDs are disabled.
      *  
      * @exception JMSException if JMS fails to get disabled message
      *                         Id due to some internal error.
      */ 

    boolean 
    getDisableMessageID() throws JMSException;


    /** Set whether message timestamps are disabled.
      *  
      * <P>Since timestamps take some effort to create and increase a 
      * message's size, some JMS providers may be able to optimize message 
      * overhead if they are given a hint that timestamp is not used by an 
      * application. JMS message Producers provide a hint to disable 
      * timestamps. When a client sets a producer to disable timestamps 
      * they are saying that they do not depend on the value of timestamp 
      * for the messages it produces. These messages must either have 
      * timestamp set to null or, if the hint is ignored, timestamp must 
      * be set to its normal value.
      *  
      * <P>Message timestamps are enabled by default.
      *
      * @param value indicates if message timestamps are disabled.
      *  
      * @exception JMSException if JMS fails to set disabled message
      *                         timestamp due to some internal error.
      */ 

    void
    setDisableMessageTimestamp(boolean value) throws JMSException;


    /** Get an indication of whether message timestamps are disabled.
      *  
      * @return an indication of whether message IDs are disabled.
      *  
      * @exception JMSException if JMS fails to get disabled message
      *                         timestamp due to some internal error.
      */ 

    boolean
    getDisableMessageTimestamp() throws JMSException;


    /** Set the producer's default delivery mode.
      *  
      * <P>Delivery mode is set to PERSISTENT by default.
      *
      * @param deliveryMode the message delivery mode for this message
      * producer.
      *  
      * @exception JMSException if JMS fails to set delivery mode
      *                         due to some internal error.          
      *
      * @see javax.jms.MessageProducer#getDeliveryMode
      */ 

    void
    setDeliveryMode(int deliveryMode) throws JMSException;


    /** Get the producer's default delivery mode.
      *  
      * @return the message delivery mode for this message producer.
      *  
      * @exception JMSException if JMS fails to get delivery mode
      *                         due to some internal error.
      *
      * @see javax.jms.MessageProducer#setDeliveryMode
      */ 

    int 
    getDeliveryMode() throws JMSException;


    /** Set the producer's default priority.
      *  
      * <P>Priority is set to 4, by default.
      *
      * @param priority the message priority for this message producer.
      *  
      * @exception JMSException if JMS fails to set priority
      *                         due to some internal error.
      *
      * @see javax.jms.MessageProducer#getPriority
      */ 

    void
    setPriority(int deliveryMode) throws JMSException;


    /** Get the producer's default priority.
      *  
      * @return the message priority for this message producer.
      *  
      * @exception JMSException if JMS fails to get priority
      *                         due to some internal error.
      *
      * @see javax.jms.MessageProducer#setPriority
      */ 

    int 
    getPriority() throws JMSException;


    /** Set the default length of time in milliseconds from its dispatch time
      * that a produced message should be retained by the message system.
      *
      * <P>Time to live is set to zero by default.
      *
      * @param timeToLive the message time to live in milliseconds; zero is
      * unlimited
      *
      * @exception JMSException if JMS fails to set Time to Live
      *                         due to some internal error.
      *
      * @see javax.jms.MessageProducer#getTimeToLive
      */
   
    void
    setTimeToLive(int timeToLive) throws JMSException;
   
   
    /** Get the default length of time in milliseconds from its dispatch time
      * that a produced message should be retained by the message system.
      *
      * @return the message time to live in milliseconds; zero is unlimited
      *
      * @exception JMSException if JMS fails to get Time to Live
      *                         due to some internal error.
      *
      * @see javax.jms.MessageProducer#setTimeToLive
      */ 
 
    int
    getTimeToLive() throws JMSException;


    /** Since a provider may allocate some resources on behalf of a
      * MessageProducer outside the JVM, clients should close them when they
      * are not needed. Relying on garbage collection to eventually reclaim
      * these resources may not be timely enough.
      *  
      * @exception JMSException if JMS fails to close the producer
      *                         due to some error.
      */ 

    void
    close() throws JMSException;
}
