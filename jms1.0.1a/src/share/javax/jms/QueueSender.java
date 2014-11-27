
/*
 * @(#)QueueSender.java	1.19 98/10/07
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

/** A client uses a QueueSender to send messages to a queue.
  * 
  * <P>Normally the Queue is specified when a QueueSender is created and 
  * in this case, attempting to use the methods for an unidentified 
  * QueueSender will throws an UnsupportedOperationException.
  * 
  * <P>In the case that the QueueSender with an unidentified Queue is 
  * created, the methods that assume the Queue has been identified throw 
  * an UnsupportedOperationException.
  *
  * @version     1.0 - 4 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.MessageProducer
  */

public interface QueueSender extends MessageProducer {

    /** Get the queue associated with this queue sender.
      *  
      * @return the queue 
      *  
      * @exception JMSException if JMS fails to get queue for
      *                         this queue sender
      *                         due to some internal error.
      */ 
 
    Queue
    getQueue() throws JMSException;


    /** Send a message to the queue. Use the QueueSender's default delivery
      * mode, timeToLive and priority.
      *
      * @param message the message to be sent 
      *  
      * @exception JMSException if JMS fails to send the message 
      *                         due to some internal error.
      * @exception MessageFormatException if invalid message specified
      * @exception InvalidDestinationException if a client uses
      *                         this method with a Queue sender with
      *                         an invalid queue.
      */

    void 
    send(Message message) throws JMSException;


    /** Send a message specifying delivery mode, priority and time to
      * live to the queue.
      *
      * @param message the message to be sent
      * @param deliveryMode the delivery mode to use
      * @param priority the priority for this message
      * @param timeToLive the message's lifetime (in milliseconds). 
      *  
      * @exception JMSException if JMS fails to send the message 
      *                         due to some internal error.
      * @exception MessageFormatException if invalid message specified
      * @exception InvalidDestinationException if a client uses
      *                         this method with a Queue sender with
      *                         an invalid queue.
      */

    void 
    send(Message message, 
	 int deliveryMode, 
	 int priority,
	 long timeToLive) throws JMSException;


    /** Send a message to a queue for an unidentified message producer.
      * Use the QueueSender's default delivery mode, timeToLive and priority.
      *
      * <P>Typically a JMS message producer is assigned a queue at creation 
      * time; however, JMS also supports unidentified message producers
      * which require that the queue be supplied on every message send.
      *  
      * @param queue the queue that this message should be sent to
      * @param message the message to be sent
      *  
      * @exception JMSException if JMS fails to send the message 
      *                         due to some internal error.
      * @exception MessageFormatException if invalid message specified
      * @exception InvalidDestinationException if a client uses
      *                         this method with an invalid queue.
      */ 
 
    void
    send(Queue queue, Message message) throws JMSException;
 
 
    /** Send a message to a queue for an unidentified message producer, 
      * specifying delivery mode, priority and time to live.
      *  
      * <P>Typically a JMS message producer is assigned a queue at creation 
      * time; however, JMS also supports unidentified message producers 
      * which require that the queue be supplied on every message send.
      *  
      * @param queue the queue that this message should be sent to
      * @param message the message to be sent
      * @param deliveryMode the delivery mode to use
      * @param priority the priority for this message
      * @param timeToLive the message's lifetime (in milliseconds).
      *  
      * @exception JMSException if JMS fails to send the message 
      *                         due to some internal error.
      * @exception MessageFormatException if invalid message specified
      * @exception InvalidDestinationException if a client uses
      *                         this method with an invalid queue.
      */ 

    void
    send(Queue queue, 
	 Message message, 
	 int deliveryMode, 
	 int priority,
	 long timeToLive) throws JMSException;
}
