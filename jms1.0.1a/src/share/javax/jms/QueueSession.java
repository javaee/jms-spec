
/*
 * @(#)QueueSession.java	1.15 98/10/07
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

/** A QueueSession provides methods for creating QueueReceiver's, 
  * QueueSender's, QueueBrowser's and TemporaryQueues.
  *
  * <P>If there are messages that have been received but not acknowledged 
  * when a QueueSession terminates, these messages will be retained and 
  * redelivered when a consumer next accesses the queue.
  *
  * @version     1.0 - 14 May 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.Session
  */

public interface QueueSession extends Session {

    /** Create a Queue given a Queue name.
      *
      * <P>This facility is provided for the rare cases where clients need to
      * dynamically manipulate queue identity. This allows the creation of a
      * queue with a provider specific name. Clients that depend on this
      * ability are not portable.
      *
      * @param queueName the name of this queue
      *
      * @return a Queue with the given name.
      *
      * @exception JMSException if a session fails to create a queue
      *                         due to some JMS error.
      */ 
 
    Queue
    createQueue(String queueName) throws JMSException;


    /** Create a QueueReceiver to receive messages from the specified queue.
      *
      * @param queue the queue to access
      *
      * @exception JMSException if a session fails to create a receiver
      *                         due to some JMS error.
      * @exception InvalidDestinationException if invalid Queue specified.
      */

    QueueReceiver
    createReceiver(Queue queue) throws JMSException;


    /** Create a QueueReceiver to receive messages from the specified queue.
      *  
      * @param queue the queue to access
      * @param messageSelector only messages with properties matching the
      * message selector expression are delivered
      *  
      * @exception JMSException if a session fails to create a receiver
      *                         due to some JMS error.
      * @exception InvalidDestinationException if invalid Queue specified.
      * @exception InvalidSelectorException if the message selector is invalid.
      *
      */ 

    QueueReceiver
    createReceiver(Queue queue, 
		   String messageSelector) throws JMSException;


    /** Create a QueueSender to send messages to the specified queue.
      *
      * @param queue the queue to access, or null if this is an unidentifed
      * producer.
      *
      * @exception JMSException if a session fails to create a sender
      *                         due to some JMS error.
      * @exception InvalidDestinationException if invalid Queue specified.
      */
 
    QueueSender
    createSender(Queue queue) throws JMSException;


    /** Create a QueueBrowser to peek at the messages on the specified queue.
      *
      * @param queue the queue to access
      *
      * @exception JMSException if a session fails to create a browser
      *                         due to some JMS error.
      * @exception InvalidDestinationException if invalid Queue specified.
      */

    QueueBrowser 
    createBrowser(Queue queue) throws JMSException;


    /** Create a QueueBrowser to peek at the messages on the specified queue.
      *  
      * @param queue the queue to access
      * @param messageSelector only messages with properties matching the
      * message selector expression are delivered
      *  
      * @exception JMSException if a session fails to create a browser
      *                         due to some JMS error.
      * @exception InvalidDestinationException if invalid Queue specified.
      * @exception InvalidSelectorException if the message selector is invalid.
      */ 

    QueueBrowser
    createBrowser(Queue queue,
		  String messageSelector) throws JMSException;


    /** Create a temporary queue. It's lifetime will be that of the 
      * QueueConnection unless deleted earlier.
      *
      * @return a temporary queue.
      *
      * @exception JMSException if a session fails to create a Temporary Queue
      *                         due to some JMS error.
      */

    TemporaryQueue
    createTemporaryQueue() throws JMSException;
}
