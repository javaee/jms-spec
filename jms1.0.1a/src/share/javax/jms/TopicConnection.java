
/*
 * @(#)TopicConnection.java	1.11 98/10/07
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

/** A TopicConnection is an active connection to a JMS Pub/Sub provider. 
  * A client uses a TopicConnection to create one or more TopicSessions
  * for producing and consuming messages.
  *
  * @version     1.0 - 13 March 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.Connection
  */

public interface TopicConnection extends Connection {

    /** Create a TopicSession
      *  
      * @param transacted if true, the session is transacted.
      * @param acknowledgeMode indicates whether the consumer or the
      * client will acknowledge any messages it receives. This parameter
      * will be ignored if the session is transacted.
      *  
      * @return a newly created topic session.
      *  
      * @exception JMSException if JMS Connection fails to create a
      *                         session due to some internal error or
      *                         lack of support for specific transaction
      *                         and acknowledgement mode.
      */ 

    TopicSession
    createTopicSession(boolean transacted,
                       int acknowledgeMode) throws JMSException;


    /** Create a connection consumer for this connection. This is an expert
      * facility not used by regular JMS clients.
      *  
      * @param topic the topic to access
      * @param messageSelector only messages with properties matching the
      * message selector expression are delivered
      * @param sessionPool the server session pool to associate with this 
      * connection consumer.
      * @param maxMessages the maximum number of messages that can be
      * assigned to a server session at one time.
      *
      * @return the connection consumer.
      *
      * @exception JMSException if JMS Connection fails to create a
      *                         a connection consumer due to some internal 
      *                         error or invalid arguments for sessionPool.
      * @exception InvalidSelectorException if the message selector is invalid.
      * @see javax.jms.ConnectionConsumer
      */ 

    ConnectionConsumer
    createConnectionConsumer(Topic topic,
                             String messageSelector,
                             ServerSessionPool sessionPool,
			     int maxMessages)
			     throws JMSException;


    /** Create a durable connection consumer for this connection. This is an
      * expert facility not used by regular JMS clients.
      *                
      * @param topic the topic to access
      * @param messageSelector only messages with properties matching the
      * message selector expression are delivered
      * @param sessionPool the serversession pool to associate with this 
      * durable connection consumer.
      * @param maxMessages the maximum number of messages that can be
      * assigned to a server session at one time.
      *
      * @return the durable connection consumer. 
      *  
      * @exception JMSException if JMS Connection fails to create a
      *                         a connection consumer due to some internal 
      *                         error or invalid arguments for sessionPool
      *                         and message selector.
      *
      * @see javax.jms.ConnectionConsumer
      */

    ConnectionConsumer
    createDurableConnectionConsumer(Topic topic,
                                    String messageSelector,
                                    ServerSessionPool sessionPool,
				    int maxMessages)
                             throws JMSException;
}
