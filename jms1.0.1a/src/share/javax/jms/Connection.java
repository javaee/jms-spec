
/*
 * @(#)Connection.java	1.9 98/10/07
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

/** A JMS Connection is a client's active connection to its JMS provider. 
  * It will typically allocate provider resources outside the Java virtual 
  * machine.
  *
  * <P>Connections support concurrent use.
  *
  * <P>A Connection serves several purposes:
  *
  * <UL>
  *   <LI>It encapsulates an open connection with a JMS provider. It 
  *       typically represents an open TCP/IP socket between a client and 
  *       a provider service daemon.
  *   <LI>Its creation is where client authenticating takes place.
  *   <LI>It can specify a unique client identifier.
  *   <LI>It provides ConnectionMetaData.
  *   <LI>It supports an optional ExceptionListener.
  * </UL>
  *
  * <P>Due to the authentication and communication setup done when a 
  * Connection is created, a Connection is a relatively heavy-weight JMS 
  * object. Most clients will do all their messaging with a single Connection.
  * Other more advanced applications may use several Connections. JMS does 
  * not architect a reason for using multiple connections; however, there may 
  * be operational reasons for doing so.
  *
  * <P>A JMS client typically creates a Connection; one or more Sessions; 
  * and a number of message producers and consumers. When a Connection is 
  * created it is in stopped mode. That means that no messages are being 
  * delivered.
  *
  * <P>It is typical to leave the Connection in stopped mode until setup 
  * is complete. At that point the Connection's start() method is called 
  * and messages begin arriving at the Connection's consumers. This setup 
  * convention minimizes any client confusion that may result from 
  * asynchronous message delivery while the client is still in the process 
  * of setting itself up.
  *
  * <P>A Connection can immediately be started and the setup can be done 
  * afterwards. Clients that do this must be prepared to handle asynchronous 
  * message delivery while they are still in the process of setting up.
  *
  * <P>A message producer can send messages while a Connection is stopped.
  *
  * @version     1.0 - 3 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.QueueConnection
  * @see         javax.jms.TopicConnection
  */

public interface Connection {

    /** Get the client identifier for this connection.
      *  
      * @return the unique client identifier.
      *  
      * @exception JMSException if JMS implementation fails to return
      *                         the client ID for this Connection due
      *                         to some internal error.
      *
      **/
    String
    getClientID() throws JMSException;


    /** Set the client identifier for this connection.
      *  
      * <P>The preferred way to assign a Client's client identifier is for 
      * it to be configured in a client-specific ConnectionFactory and 
      * transparently assigned to the Connection it creates. Alternatively, 
      * a client can set a Connections's client identifier using a 
      * provider-specific value.
      *
      * <P>The purpose of client identifier is to associate a session and 
      * its objects with a state maintained on behalf of the client by a 
      * provider. The only such state identified by JMS is that required 
      * to support durable subscriptions
      *
      * @param clientID the unique client identifier
      *  
      * @exception JMSException general exception if JMS implementation fails to
      *                         set the client ID for this Connection due
      *                         to some internal error.
      *
      * @exception InvalidClientIDException if JMS client specifies an
      *                         invalid or duplicate client id.
      */ 

    void
    setClientID(String clientID) throws JMSException;

 
    /** Get the meta data for this connection.
      *  
      * @return the connection meta data.
      *  
      * @exception JMSException general exception if JMS implementation fails to
      *                         get the Connection meta-data for this Connection.
      *
      * @see javax.jms.ConnectionMetaData
      */

    ConnectionMetaData
    getMetaData() throws JMSException;


    /** Set an exception listener for this connection.
      *
      * <P>If a JMS provider detects a serious problem with a connection it 
      * will inform the connection's ExceptionListener if one has been 
      * registered. It does this by calling the listener's onException() 
      * method passing it a JMSException describing the problem.
      *
      * <P>This allows a client to be asynchronously notified of a problem. 
      * Some connections only consume messages so they would have no other 
      * way to learn their connection has failed.
      *
      * <P>A Connection serializes execution of its ExceptionListener.
      *
      * <P>A JMS provider should attempt to resolve connection problems 
      * itself prior to notifying the client of them.
      *
      * @param handler the exception listener.
      *
      * @exception JMSException general exception if JMS implementation fails to
      *                         set the Exception listener for this Connection.
      */

    void 
    setExceptionListener(ExceptionListener listener) throws JMSException;

    /** Start (or restart) a Connection's delivery of incoming messages.
      * Restart begins with the oldest unacknowledged message.
      * Starting a started session is ignored.
      *  
      * @exception JMSException if JMS implementation fails to start the
      *                         message delivery due to some internal error.
      *
      * @see javax.jms.Connection#stop
      */

    void
    start() throws JMSException;

 
    /** Used to temporarily stop a Connection's delivery of incoming messages.
      * It can be restarted using its <CODE>start</CODE> method. When stopped,
      * delivery to all the Connection's message consumers is inhibited: 
      * synchronous receive's block and messages are not delivered to message 
      * listeners.
      *
      * <P>After stop is called there may still be some messages delivered.
      *
      * <P>Stopping a Session has no affect on its ability to send messages. 
      * Stopping a stopped session is ignored.
      *  
      * @exception JMSException if JMS implementation fails to stop the
      *                         message delivery due to some internal error.
      *
      * @see javax.jms.Connection#start
      */

    void
    stop() throws JMSException;

 
    /** Since a provider typically allocates significant resources outside 
      * the JVM on behalf of a Connection, clients should close them when 
      * they are not needed. Relying on garbage collection to eventually 
      * reclaim these resources may not be timely enough.
      *  
      * @exception JMSException if JMS implementation fails to close the
      *                         connection due to internal error. For 
      *                         example, a failure to release resources
      *                         or to close socket connection can lead
      *                         to throwing of this exception.
      *
      */

    void 
    close() throws JMSException; 
}
