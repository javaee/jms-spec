/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.jms;

import java.io.Serializable;

/**
 * A <code>MessagingContext</code> is the main interface in the simplified JMS API introduced for JMS 2.0. 
 * This combines the functionality of several JMS 1.1 objects into one: a <code>Connection</code>, a <code>Session</code> 
 * and an anonymous <code>MessageProducer</code> (one with no destination specified). 
 * It also combines the functionality of <code>MessageConsumer</code>, but only for asynchronous message delivery. 
 * <p> 
 * For synchronous delivery a separate object is still needed. 
 * This is <code>SyncMessageConsumer</code> which provides the functionality of
 * <code>MessageConsumer</code> for synchronous message delivery.
 * In terms of the old API a <code>MessagingContext</code> should be thought of as 
 * representing both a <code>Connection</code> and a <code>Session</code>. 
 * These concepts remain relevant in the new API. 
 * As described in JMS 1.1, a connection represents a physical link to the JMS server, 
 * and a session represents a single-threaded context for sending and receiving messages.
 * <p>
 * Java EE allows only one session to be created on each connection,
 * so combining them in a single method takes advantage of this restriction to offer a simpler API.
 * Java EE applications will create <code>MessagingContext</code> objects
 * using new factory methods on the <code>ConnectionFactory</code> interface.
 * <p>
 * Java SE applications allow multiple sessions on the same connection. 
 * This allows the same physical connection to be used in multiple threads simultaneously.  
 * Java SE applications which require multiple sessions to be created on the same connection
 * should use the using the factory methods on the <code>ConnectionFactory</code> interface
 * to create the first <code>MessagingContext</code> and then 
 * use the <code>createMessagingContext</code> method on <code>MessagingContext</code>
 * to create additional <code>MessagingContext</code> objects that use the same connection.
 * 
 * @version 2.0
 * @since 2.0
 * 
 */

public interface MessagingContext extends AutoCloseable {
	
    /** 
     * Creates a new <code>MessagingContext</code> with the specified session mode
	 * using the same connection as this <code>MessagingContext</code> and creating a new session.
	 * <p>
     * This method does not start the connection. 
     * If the connection has not already been started then it will be automatically started
     * when a SyncMessageConsumer is created or a MessageListener registered on  
     * any of the <code>MessagingContext</code> objects for that connection.
     * <p>
     * This method is for use in a Java SE environment only. 
     * It may not be used in a Java EE environment because this would violate
     * the Java EE restriction that a connection may have only one session.
	 *
	 * @param sessionMode indicates whether the new MessagingContext is transacted or not,
	 * and if it is not, whether the consumer or the client will acknowledge any 
	 * messages it receives. Legal values are <code>MessagingContext.SESSION_TRANSACTED</code>, 
	 * <code>MessagingContext.AUTO_ACKNOWLEDGE</code> and
	 * <code>MessagingContext.CLIENT_ACKNOWLEDGE</code>, and 
	 * <code>MessagingContext.DUPS_OK_ACKNOWLEDGE</code>.
	 * 
	 * @return a newly created MessagingContext
	 *
	 * @exception JMSRuntimeException if the JMS provider fails to create the
	 *                         MessagingContext due to some internal error.
	 * @exception JMSSecurityRuntimeException  if client authentication fails due to 
	 *                         an invalid user name or password.
     * @since 2.0 
     */ 
	MessagingContext createMessagingContext(int sessionMode);
	
// START OF METHODS COPIED FROM CONNECTION 
   
   /** Gets the client identifier for the MessagingContext's connection.
     *  
     * <P>This value is specific to the JMS provider.  It is either preconfigured 
     * by an administrator in a <CODE>ConnectionFactory</CODE> object
     * or assigned dynamically by the application by calling the
     * <code>setClientID</code> method.
     * 
     * @return the unique client identifier 
     *  
     * @exception JMSRuntimeException if the JMS provider fails to return
     *                         the client ID for the MessagingContext's connection due
     *                         to some internal error.
     *
     **/
   String getClientID();


   	/**
	 * Sets the client identifier for the MessagingContext's connection.
	 * 
	 * <P>
	 * The preferred way to assign a JMS client's client identifier is for it to
	 * be configured in a client-specific <CODE>ConnectionFactory</CODE> object
	 * and transparently assigned to the <CODE>Connection</CODE> object it
	 * creates.
	 * 
	 * <P>
	 * Alternatively, a client can set the client identifier for the
	 * MessageContext's connection using a provider-specific value. The facility
	 * to set its client identifier explicitly is not a mechanism for overriding
	 * the identifier that has been administratively configured. It is provided
	 * for the case where no administratively specified identifier exists. If
	 * one does exist, an attempt to change it by setting it must throw an
	 * <CODE>IllegalStateException</CODE>. If a client sets the client
	 * identifier explicitly, it must do so immediately after it creates the
	 * MessagingContext and before any other action on the MessagingContext is
	 * taken. After this point, setting the client identifier is a programming
	 * error that should throw an <CODE>IllegalStateException</CODE>.
	 * 
	 * <P>
	 * The purpose of the client identifier is to associate the
	 * MessagingContext's connection and its objects with a state maintained on
	 * behalf of the client by a provider. The only such state identified by the
	 * JMS API is that required to support durable subscriptions.
	 * 
	 * <P>
	 * If another connection with the same <code>clientID</code> is already
	 * running when this method is called, the JMS provider should detect the
	 * duplicate ID and throw an <CODE>InvalidClientIDException</CODE>.
	 * 
	 * @param clientID
	 *            the unique client identifier
	 * 
	 * @throws JMSRuntimeException
	 *             if the JMS provider fails to set the client ID for the
	 *             MessagingContext's connection due to some internal error.
	 * 
	 * @throws InvalidClientIDRuntimeException
	 *             if the JMS client specifies an invalid or duplicate client
	 *             ID.
	 * @throws IllegalStateRuntimeException
	 *             if the JMS client attempts to set the client ID for the
	 *             MessagingContext's connection at the wrong time or when it
	 *             has been administratively configured.
	 */

   void setClientID(String clientID);


   	/**
	 * Gets the connection metadata for the MessagingContext's connection.
	 * 
	 * @return the connection metadata
	 * 
	 * @throws JMSRuntimeException
	 *             if the JMS provider fails to get the connection metadata 
	 * 
	 * @see javax.jms.ConnectionMetaData
	 */

   ConnectionMetaData getMetaData();

   	/**
	 * Gets the <CODE>ExceptionListener</CODE> object for the MessagingContext's
	 * connection. Not every <CODE>Connection</CODE> has an
	 * <CODE>ExceptionListener</CODE> associated with it.
	 * 
	 * @return the <CODE>ExceptionListener</CODE> for the MessagingContext's
	 *         connection, or null if no <CODE>ExceptionListener</CODE> is
	 *         associated with that connection.
	 * 
	 * @throws JMSRuntimeException
	 *             if the JMS provider fails to get the
	 *             <CODE>ExceptionListener</CODE> for the MessagingContext's
	 *             connection.
	 * @see javax.jms.Connection#setExceptionListener
	 */

   ExceptionListener getExceptionListener();


   	/**
	 * Sets an exception listener for the MessagingContext's connection.
	 * 
	 * <P>
	 * If a JMS provider detects a serious problem with a connection, it informs
	 * the connection's <CODE>ExceptionListener</CODE>, if one has been
	 * registered. It does this by calling the listener's
	 * <CODE>onException</CODE> method, passing it a <CODE>JMSException</CODE>
	 * object describing the problem.
	 * 
	 * <P>
	 * An exception listener allows a client to be notified of a problem
	 * asynchronously. Some connections only consume messages, so they would
	 * have no other way to learn their connection has failed.
	 * 
	 * <P>
	 * A connection serializes execution of its <CODE>ExceptionListener</CODE>.
	 * 
	 * <P>
	 * A JMS provider should attempt to resolve connection problems itself
	 * before it notifies the client of them.
	 * 
	 * @param listener
	 *            the exception listener
	 * 
	 * @throws JMSRuntimeException
	 *             if the JMS provider fails to set the exception listener for
	 *             the MessagingContext's connection.
	 * 
	 */

   void setExceptionListener(ExceptionListener listener);

	/**
	 * Starts (or restarts) delivery of incoming messages by the
	 * MessagingContext's connection. A call to <CODE>start</CODE> on a
	 * connection that has already been started is ignored.
	 * 
	 * @exception JMSRuntimeException
	 *                if the JMS provider fails to start message delivery due to
	 *                some internal error.
	 * 
	 * @see javax.jms.Connection#stop
	 */
   void start();


   /** Temporarily stops the delivery of incoming messages by the MessagingContext's connection.
     * Delivery can be restarted using the <CODE>start</CODE>
     * method. When the connection is stopped,
     * delivery to all the connection's message consumers is inhibited:
     * synchronous receives block, and messages are not delivered to message
     * listeners.
     *
     * <P>This call blocks until receives and/or message listeners in progress
     * have completed.
     *
     * <P>Stopping a connection has no effect on its ability to send messages.
     * A call to <CODE>stop</CODE> on a connection that has already been
     * stopped is ignored.
     *
     * <P>A call to <CODE>stop</CODE> must not return until delivery of messages
     * has paused. This means that a client can rely on the fact that none of 
     * its message listeners will be called and that all threads of control 
     * waiting for <CODE>receive</CODE> calls to return will not return with a 
     * message until the
     * connection is restarted. The receive timers for a stopped connection
     * continue to advance, so receives may time out while the connection is
     * stopped.
     * 
     * <P>If message listeners are running when <CODE>stop</CODE> is invoked, 
     * the <CODE>stop</CODE> call must
     * wait until all of them have returned before it may return. While these
     * message listeners are completing, they must have the full services of the
     * connection available to them.
     * <p>
     * A message listener must not attempt to stop its own MessagingContext as this 
     * would lead to deadlock. The JMS provider must detect this and throw a 
     * javax.jms.IllegalStateRuntimeException.
     * <p>
     * For the avoidance of doubt, if an exception listener for the MessagingContext's connection 
     * is running when <code>stop</code> is invoked, there is no requirement for 
     * the <code>stop</code> call to wait until the exception listener has returned
     * before it may return.   
     * 
     * @exception JMSRuntimeException if the JMS provider fails to stop
     *                         message delivery due to some internal error.
     *
     * @see javax.jms.Connection#start
     */

   void stop();


   /** Closes the MessagingContext
    * <p>
    * If this MessagingContext was creating by calling <CODE>createMessageConsumer</CODE> on <CODE>ConnectionFactory</CODE> then
    * this method closes the <CODE>MessagingContext</CODE>'s connection, session, producers and consumers.
    * If this MessagingContext was created by calling <CODE>createMessageConsumer</CODE> on <CODE>Connection</CODE> then 
    * this method closes the <CODE>MessagingContext</CODE>'s session, producers and consumers but not the connection.
     *
     * <P>Since a provider typically allocates significant resources outside 
     * the JVM on behalf of a connection, clients should close these resources
     * when they are not needed. Relying on garbage collection to eventually 
     * reclaim these resources may not be timely enough.
     *
     * <P>There is no need to close the sessions, producers, and consumers
     * of a closed connection.
     *
     * <P>Closing a connection causes all temporary destinations to be
     * deleted.
     *
     * <P>When this method is invoked, it should not return until message
     * processing has been shut down in an orderly fashion. This means that all
     * message 
     * listeners that may have been running have returned, and that all pending 
     * receives have returned. A close terminates all pending message receives 
     * on the connection's sessions' consumers. The receives may return with a 
     * message or with null, depending on whether there was a message available 
     * at the time of the close. If one or more of the connection's sessions' 
     * message listeners is processing a message at the time when connection 
     * <CODE>close</CODE> is invoked, all the facilities of the connection and 
     * its sessions must remain available to those listeners until they return 
     * control to the JMS provider. 
     * <p>
     * A message listener must not attempt to close its own MessagingContext as this 
     * would lead to deadlock. The JMS provider must detect this and throw a 
     * javax.jms.IllegalStateRuntimeException.
     * <p>
     * For the avoidance of doubt, if an exception listener for the MessagingContext's connection 
     * is running when <code>close</code> is invoked, there is no requirement for 
     * the <code>close</code> call to wait until the exception listener has returned
     * before it may return.   
     * <P>Closing a connection causes any of its sessions' transactions
     * in progress to be rolled back. In the case where a session's
     * work is coordinated by an external transaction manager, a session's 
     * <CODE>commit</CODE> and <CODE>rollback</CODE> methods are
     * not used and the result of a closed session's work is determined
     * later by the transaction manager.
     * <p>
     * Closing a connection does NOT force an 
     * acknowledgment of client-acknowledged sessions. 
     * 
     * <P>Invoking the <CODE>acknowledge</CODE> method of a received message 
     * from a closed connection's session must throw an 
     * <CODE>IllegalStateException</CODE>.  Closing a closed connection must 
     * NOT throw an exception.
     *  
     * @exception JMSRuntimeException if the JMS provider fails to close the
     *                         MessagingContext due to some internal error. For 
     *                         example, a failure to release resources
     *                         or to close a socket connection can cause
     *                         this exception to be thrown.
     *
     */

   void close(); 
    
// END OF METHODS COPIED FROM CONNECTION
  
// START OF METHODS COPIED FROM SESSION
   
   /** With this session mode, the MessagingContext's session automatically acknowledges
    * a client's receipt of a message either when the session has successfully 
    * returned from a call to <CODE>receive</CODE> or when the message 
    * listener the session has called to process the message successfully 
    * returns.
    */ 

  static final int AUTO_ACKNOWLEDGE = Session.AUTO_ACKNOWLEDGE;

  /** With this session mode, the client acknowledges a consumed 
    * message by calling the message's <CODE>acknowledge</CODE> method. 
    * Acknowledging a consumed message acknowledges all messages that the 
    * session has consumed.
    *
    * <P>When this session mode is used, a client may build up a 
    * large number of unacknowledged messages while attempting to process 
    * them. A JMS provider should provide administrators with a way to 
    * limit client overrun so that clients are not driven to resource 
    * exhaustion and ensuing failure when some resource they are using 
    * is temporarily blocked.
    *
    * @see javax.jms.Message#acknowledge()
    */ 

  static final int CLIENT_ACKNOWLEDGE = Session.CLIENT_ACKNOWLEDGE;

  /** This session mode instructs the MessagingContext's session to lazily acknowledge 
    * the delivery of messages. This is likely to result in the delivery of 
    * some duplicate messages if the JMS provider fails, so it should only be 
    * used by consumers that can tolerate duplicate messages. Use of this  
    * mode can reduce session overhead by minimizing the work the 
    * session does to prevent duplicates.
    */

  static final int DUPS_OK_ACKNOWLEDGE = Session.DUPS_OK_ACKNOWLEDGE;
  
  /** This session mode instructs the MessagingContext's session to
   * deliver and consume messages in a local transaction which 
   * will be subsequently committed by calling
   * <CODE>commit</CODE> or rolled back  by calling <CODE>rollback</CODE>.
   */
  static final int SESSION_TRANSACTED = Session.SESSION_TRANSACTED;
  
 /** Creates a <CODE>BytesMessage</CODE> object. A <CODE>BytesMessage</CODE> 
   * object is used to send a message containing a stream of uninterpreted 
   * bytes.
   *  
   * @exception JMSRuntimeException if the JMS provider fails to create this message
   *                         due to some internal error.
   */ 
 

 BytesMessage createBytesMessage(); 


 /** Creates a <CODE>MapMessage</CODE> object. A <CODE>MapMessage</CODE> 
   * object is used to send a self-defining set of name-value pairs, where 
   * names are <CODE>String</CODE> objects and values are primitive values 
   * in the Java programming language.
   * <p>
   * The message object returned may be sent using any session or messaging context. 
   * It is not restricted to being sent using the messaging context used to create it.
   * <p>
   * The message object returned may be optimised for use with the JMS provider
   * used to create it. However it can be sent using any JMS provider, not just the 
   * JMS provider used to create it.
   * 
   * @exception JMSRuntimeException if the JMS provider fails to create this message
   *                         due to some internal error.
   */ 

 MapMessage createMapMessage(); 


 /** Creates a <CODE>Message</CODE> object. The <CODE>Message</CODE> 
   * interface is the root interface of all JMS messages. A 
   * <CODE>Message</CODE> object holds all the 
   * standard message header information. It can be sent when a message 
   * containing only header information is sufficient.
   * <p>
   * The message object returned may be sent using any session or messaging context. 
   * It is not restricted to being sent using the messaging context used to create it.
   * <p>
   * The message object returned may be optimised for use with the JMS provider
   * used to create it. However it can be sent using any JMS provider, not just the 
   * JMS provider used to create it.
   * 
   * @exception JMSRuntimeException if the JMS provider fails to create this message
   *                         due to some internal error.
   */ 

 Message createMessage();


 /** Creates an <CODE>ObjectMessage</CODE> object. An 
   * <CODE>ObjectMessage</CODE> object is used to send a message 
   * that contains a serializable Java object.
   * <p>
   * The message object returned may be sent using any session or messaging context. 
   * It is not restricted to being sent using the messaging context used to create it.
   * <p>
   * The message object returned may be optimised for use with the JMS provider
   * used to create it. However it can be sent using any JMS provider, not just the 
   * JMS provider used to create it.
   * 
   * @exception JMSRuntimeException if the JMS provider fails to create this message
   *                         due to some internal error.
   */ 

 ObjectMessage createObjectMessage(); 


 /** Creates an initialized <CODE>ObjectMessage</CODE> object. An 
   * <CODE>ObjectMessage</CODE> object is used 
   * to send a message that contains a serializable Java object.
   * <p>
   * The message object returned may be sent using any session or messaging context. 
   * It is not restricted to being sent using the messaging context used to create it.
   * <p>
   * The message object returned may be optimised for use with the JMS provider
   * used to create it. However it can be sent using any JMS provider, not just the 
   * JMS provider used to create it.
   * 
   * @param object the object to use to initialize this message
   *
   * @exception JMSRuntimeException if the JMS provider fails to create this message
   *                         due to some internal error.
   */ 

 ObjectMessage createObjectMessage(Serializable object);


 /** Creates a <CODE>StreamMessage</CODE> object. A 
   * <CODE>StreamMessage</CODE> object is used to send a 
   * self-defining stream of primitive values in the Java programming 
   * language.
   * <p>
   * The message object returned may be sent using any session or messaging context. 
   * It is not restricted to being sent using the messaging context used to create it.
   * <p>
   * The message object returned may be optimised for use with the JMS provider
   * used to create it. However it can be sent using any JMS provider, not just the 
   * JMS provider used to create it.
   * 
   * @exception JMSRuntimeException if the JMS provider fails to create this message
   *                         due to some internal error.
   */

 StreamMessage createStreamMessage();  


 /** Creates a <CODE>TextMessage</CODE> object. A <CODE>TextMessage</CODE> 
   * object is used to send a message containing a <CODE>String</CODE>
   * object.
   * <p>
   * The message object returned may be sent using any session or messaging context. 
   * It is not restricted to being sent using the messaging context used to create it.
   * <p>
   * The message object returned may be optimised for use with the JMS provider
   * used to create it. However it can be sent using any JMS provider, not just the 
   * JMS provider used to create it.
   * 
   * @exception JMSRuntimeException if the JMS provider fails to create this message
   *                         due to some internal error.
   */ 

 TextMessage createTextMessage(); 


 /** Creates an initialized <CODE>TextMessage</CODE> object. A 
   * <CODE>TextMessage</CODE> object is used to send 
   * a message containing a <CODE>String</CODE>.
   * <p>
   * The message object returned may be sent using any session or messaging context. 
   * It is not restricted to being sent using the messaging context used to create it.
   * <p>
   * The message object returned may be optimised for use with the JMS provider
   * used to create it. However it can be sent using any JMS provider, not just the 
   * JMS provider used to create it.
   * 
   * @param text the string used to initialize this message
   *
   * @exception JMSRuntimeException if the JMS provider fails to create this message
   *                         due to some internal error.
   */ 

 TextMessage createTextMessage(String text);


 /** Indicates whether the MessagingContext's session is in transacted mode.
   *  
   * @return true if the session is in transacted mode
   *  
   * @exception JMSRuntimeException if the JMS provider fails to return the 
   *                         transaction mode due to some internal error.
   */ 

 boolean getTransacted();
 
 /** Returns the session mode of the MessagingContext's session. 
  * This can be set at the time that the MessagingContext is created. Possible values are
  * MessagingContext.SESSION_TRANSACTED, MessagingContext.AUTO_ACKNOWLEDGE,
  * MessagingContext.CLIENT_ACKNOWLEDGE and MessagingContext.DUPS_OK_ACKNOWLEDGE
  * <p>
  * If a session mode was not specified when the MessagingContext was created
  * a value of MessagingContext.AUTO_ACKNOWLEDGE will be returned.
  *
  *@return the session mode of the MessagingContext's session
  *
  *@exception JMSRuntimeException   if the JMS provider fails to return the 
  *                         acknowledgment mode due to some internal error.
  *
  *@see Connection#createSession
  *@since 2.0
  */
 int getSessionMode();


 /** Commits all messages done in this transaction and releases any locks
   * currently held.
   *
   * @exception JMSRuntimeException if the JMS provider fails to commit the
   *                         transaction due to some internal error.
   * @exception TransactionRolledBackRuntimeException if the transaction
   *                         is rolled back due to some internal error
   *                         during commit.
   * @exception IllegalStateRuntimeException if the method is not called by a 
   *                         transacted session.
   */

 void commit();


 /** Rolls back any messages done in this transaction and releases any locks 
   * currently held.
   *
   * @exception JMSRuntimeException if the JMS provider fails to roll back the
   *                         transaction due to some internal error.
   * @exception IllegalStateRuntimeException if the method is not called by a 
   *                         transacted session.
   *                                     
   */

 void rollback();

 /** Stops message delivery in the MessagingContext's session, and restarts message delivery
   * with the oldest unacknowledged message.
   *  
   * <P>All consumers deliver messages in a serial order.
   * Acknowledging a received message automatically acknowledges all 
   * messages that have been delivered to the client.
   *
   * <P>Restarting a session causes it to take the following actions:
   *
   * <UL>
   *   <LI>Stop message delivery
   *   <LI>Mark all messages that might have been delivered but not 
   *       acknowledged as "redelivered"
   *   <LI>Restart the delivery sequence including all unacknowledged 
   *       messages that had been previously delivered. Redelivered messages
   *       do not have to be delivered in 
   *       exactly their original delivery order.
   * </UL>
   *
   * @exception JMSRuntimeException if the JMS provider fails to stop and restart
   *                         message delivery due to some internal error.
   * @exception IllegalStateRuntimeException if the method is called by a 
   *                         transacted session.
   */ 

 void recover();

 /** Creates a <CODE>SyncMessageConsumer</CODE> for the specified destination.
  * 
   * <P>A client uses a <CODE>SyncMessageConsumer</CODE> object to synchronously receive 
   * messages that have been sent to a destination.
   *
   * @param destination the <CODE>Destination</CODE> to access. 
   *
   * @exception JMSRuntimeException if the session fails to create a consumer
   *                         due to some internal error.
   * @exception InvalidDestinationRuntimeException if an invalid destination 
   *                         is specified.
   *
   * @since 2.0 
   */

 SyncMessageConsumer createSyncConsumer(Destination destination);

    /**
	 * Creates a <CODE>SyncMessageConsumer</CODE> for the specified destination,
	 * using a message selector.
	 * <P>
	 * A client uses a <CODE>SyncMessageConsumer</CODE> object to synchronously
	 * receive messages that have been sent to a destination.
	 * 
	 * @param destination
	 *            the <CODE>Destination</CODE> to access
	 * @param messageSelector
	 *            only messages with properties matching the message selector
	 *            expression are delivered. A value of null or an empty string
	 *            indicates that there is no message selector for the message
	 *            consumer.
	 * 
	 * @throws JMSRuntimeException
	 *             if the session fails to create a SyncMessageConsumer due to
	 *             some internal error.
	 * @throws InvalidDestinationRuntimeException
	 *             if an invalid destination is specified.
	 * @throws InvalidSelectorRuntimeException
	 *             if the message selector is invalid.
	 * @since 2.0
	 */
 SyncMessageConsumer createSyncConsumer(Destination destination, java.lang.String messageSelector);
  
  	/**
	 * Creates a <CODE>SyncMessageConsumer</CODE> for the specified destination, using
	 * a message selector. This method can specify whether messages published by
	 * its own connection should be delivered to it, if the destination is a
	 * topic.
	 * <P>
	 * A client uses a <CODE>SyncMessageConsumer</CODE> object to synchronously
	 * receive messages that have been sent to a destination.
	 * <P>
     * The <code>noLocal</code> argument is for use when the
     * destination is a topic and the MessagingContext's connection
     * is also being used to publish messages to that topic.
     * If <code>noLocal</code> is set to true then the
     * <code>SyncMessageConsumer</code> will not receive messages published
     * to the topic by its own connection. The default value of this
     * argument is false. If the destination is a queue
     * then the effect of setting <code>noLocal</code>
     * to true is not specified.
	 * 
	 * @param destination
	 *            the <CODE>Destination</CODE> to access
	 * @param messageSelector
	 *            only messages with properties matching the message selector
	 *            expression are delivered. A value of null or an empty string
	 *            indicates that there is no message selector for the message
	 *            consumer.
     * @param noLocal  - if true, and the destination is a topic,
     *                   then the <code>SyncMessageConsumer</code> will
     *                   not receive messages published to the topic
     *                   by its own connection
	 * 
	 * @throws JMSRuntimeException
	 *             if the session fails to create a SyncMessageConsumer due to some
	 *             internal error.
	 * @throws InvalidDestinationRuntimeException
	 *             if an invalid destination is specified.
	 * @throws InvalidSelectorRuntimeException
	 *             if the message selector is invalid.
	 * 
	 * @since 2.0
	 * 
	 */     
 SyncMessageConsumer createSyncConsumer(Destination destination, java.lang.String messageSelector, boolean noLocal);
 
 
   /** Creates a queue identity given a <CODE>Queue</CODE> name.
   *
   * <P>This facility is provided for the rare cases where clients need to
   * dynamically manipulate queue identity. It allows the creation of a
   * queue identity with a provider-specific name. Clients that depend 
   * on this ability are not portable.
   *
   * <P>Note that this method is not for creating the physical queue. 
   * The physical creation of queues is an administrative task and is not
   * to be initiated by the JMS API. The one exception is the
   * creation of temporary queues, which is accomplished with the 
   * <CODE>createTemporaryQueue</CODE> method.
   *
   * @param queueName the name of this <CODE>Queue</CODE>
   *
   * @return a <CODE>Queue</CODE> with the given name
   *
   * @exception JMSRuntimeException if the session fails to create a queue
   *                         due to some internal error.
   * @since 2.0
   */ 

 Queue
 createQueue(String queueName);
 
   /** Creates a topic identity given a <CODE>Topic</CODE> name.
   *
   * <P>This facility is provided for the rare cases where clients need to
   * dynamically manipulate topic identity. This allows the creation of a
   * topic identity with a provider-specific name. Clients that depend 
   * on this ability are not portable.
   *
   * <P>Note that this method is not for creating the physical topic. 
   * The physical creation of topics is an administrative task and is not
   * to be initiated by the JMS API. The one exception is the
   * creation of temporary topics, which is accomplished with the 
   * <CODE>createTemporaryTopic</CODE> method.
   *  
   * @param topicName the name of this <CODE>Topic</CODE>
   *
   * @return a <CODE>Topic</CODE> with the given name
   *
   * @exception JMSRuntimeException if the session fails to create a topic
   *                         due to some internal error.
   * @since 2.0
   */

 Topic
 createTopic(String topicName);

    /** Creates a durable subscription with the specified name on the
     * specified topic, and creates a <code>SyncMessageConsumer</code> 
     * on that durable subscription.
     * <p>
     * If a durable subscription already exists with the same name 
     * and client identifier (if set) and the same topic and message selector 
     * then this method creates a <code>SyncMessageConsumer</code> on the existing durable
     * subscription.
     * <p>
     * A durable subscription is used by a client which needs to receive
     * all the messages published on a topic, including the ones published 
     * when there is no active consumer associated with it. 
     * The JMS provider retains a record of this durable subscription 
     * and ensures that all messages from the topic's publishers are retained 
     * until they are delivered to, and acknowledged by,
     * a consumer on this durable subscription
     * or until they have expired.
     * <p>
     * A durable subscription will continue to accumulate messages 
     * until it is deleted using the <code>unsubscribe</code> method. 
     * <p>
     * A consumer may be created on a durable subscription using the
     * <code>createSyncDurableSubscriber</code> methods on <code>MessagingContext</code>,
     * the appropriate <code>setMessageListener</code> methods on <code>MessagingContext</code>
     * or the <code>createDurableSubscriber</code> methods on <code>Session</code> or <code>TopicSession</code>
     * A durable subscription which has a consumer 
     * associated with it is described as being active. 
     * A durable subscription which has no consumer
     * associated with it is described as being inactive. 
     * <p>
     * Only one session at a time can have a
     * active consumer on a particular durable subscription.
     * <p>
     * A durable subscription is identified by a name specified by the client
     * and by the client identifier if set. If the client identifier was set
     * when the durable subscription was first created then a client which 
     * subsequently wishes to create a <code>SyncMessageConsumer</code>
     * on that durable subscription must use the same client identifier.
     * <p>
     * A client can change an existing durable subscription by calling
     * <code>createSyncDurableSubscriber</code> 
     * with the same name and client identifier (if used),
     * and a new topic and/or message selector. 
     * Changing a durable subscriber is equivalent to 
     * unsubscribing (deleting) the old one and creating a new one.
     *
     * @param topic the non-temporary <CODE>Topic</CODE> to subscribe to
     * @param name the name used to identify this subscription
     *  
     * @exception JMSRuntimeException if the session fails to create a subscriber
     *                         due to some internal error.
     * @exception InvalidDestinationRuntimeException if an invalid topic is specified.
     *
	 */
	SyncMessageConsumer createSyncDurableConsumer(Topic topic, String name);
	
    /** Creates a durable subscription with the specified name on the
     * specified topic, and creates a <code>SyncMessageConsumer</code> 
     * on that durable subscription, specifying a message selector and 
     * whether messages published by its own connection should be added to 
     * the durable subscription.  
     * <p>
     * If a durable subscription already exists with the same name 
     * and client identifier (if set) and the same topic and message selector 
     * then this method creates a <code>MessageConsumer</code> on the existing durable
     * subscription.
     * <p>
     * A durable subscription is used by a client which needs to receive
     * all the messages published on a topic, including the ones published 
     * when there is no active consumer associated with it. 
     * The JMS provider retains a record of this durable subscription 
     * and ensures that all messages from the topic's publishers are retained 
     * until they are delivered to, and acknowledged by,
     * a consumer on this durable subscription
     * or until they have expired.
     * <p>
     * A durable subscription will continue to accumulate messages 
     * until it is deleted using the <code>unsubscribe</code> method. 
     * <p>
     * A consumer may be created on a durable subscription using the
     * <code>createSyncDurableSubscriber</code> methods on <code>MessagingContext</code>,
     * the appropriate <code>setMessageListener</code> methods on <code>MessagingContext</code>
     * or the <code>createDurableSubscriber</code> methods on <code>Session</code> or <code>TopicSession</code>
     * A durable subscription which has a consumer 
     * associated with it is described as being active. 
     * A durable subscription which has no consumer
     * associated with it is described as being inactive. 
     * <p>
     * Only one session at a time can have a
     * active consumer on a particular durable subscription.
     * <p>
     * A durable subscription is identified by a name specified by the client
     * and by the client identifier if set. If the client identifier was set
     * when the durable subscription was first created then a client which 
     * subsequently wishes to create a <code>SyncMessageConsumer</code>
     * on that durable subscription must use the same client identifier.
     * <p>
     * A client can change an existing durable subscription by calling
     * <code>createSyncDurableSubscriber</code> 
     * with the same name and client identifier (if used),
     * and a new topic and/or message selector. 
     * Changing a durable subscriber is equivalent to 
     * unsubscribing (deleting) the old one and creating a new one.
     * <p>
     * The <code>noLocal</code> argument is for use when the MessagingContext's 
     * connection is also being used to publish messages to the topic. 
     * If <code>noLocal</code> is set to true then messages published
     * to the topic by its own connection will not be added to the
     * durable subscription. The default value of this 
     * argument is false. 
     *
     * @param topic the non-temporary <CODE>Topic</CODE> to subscribe to
     * @param name the name used to identify this subscription
     * @param messageSelector only messages with properties matching the
     * message selector expression are delivered.  A value of null or
     * an empty string indicates that there is no message selector 
     * for the message consumer.
     * @param noLocal if true, messages published by its own connection
     * will not be added to the durable subscription.
     *  
     * @exception JMSRuntimeException if the session fails to create a subscriber
     *                         due to some internal error.
     * @exception InvalidDestinationRuntimeException if an invalid topic is specified.
     * @exception InvalidSelectorRuntimeException if the message selector is invalid.
     *
     */ 
	SyncMessageConsumer createSyncDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal);
 
/** Creates a <CODE>QueueBrowser</CODE> object to peek at the messages on 
   * the specified queue.
   *  
   * @param queue the <CODE>queue</CODE> to access
   *
   *  
   * @exception JMSRuntimeException if the session fails to create a browser
   *                         due to some internal error.
   * @exception InvalidRuntimeDestinationException if an invalid destination
   *                         is specified 
   *
   * @since 2.0 
   */ 
 QueueBrowser 
 createBrowser(Queue queue);


 /** Creates a <CODE>QueueBrowser</CODE> object to peek at the messages on 
   * the specified queue using a message selector.
   *  
   * @param queue the <CODE>queue</CODE> to access
   *
   * @param messageSelector only messages with properties matching the
   * message selector expression are delivered. A value of null or
   * an empty string indicates that there is no message selector 
   * for the message consumer.
   *  
   * @exception JMSRuntimeException if the session fails to create a browser
   *                         due to some internal error.
   * @exception InvalidRuntimeDestinationException if an invalid destination
   *                         is specified 
   * @exception InvalidRuntimeSelectorException if the message selector is invalid.
   *
   * @since 2.0 
   */ 

 QueueBrowser
 createBrowser(Queue queue,
		  String messageSelector);

 
  /** Creates a <CODE>TemporaryQueue</CODE> object. Its lifetime will be that 
   * of the MessagingContext's <CODE>Connection</CODE> unless it is deleted earlier.
   *
   * @return a temporary queue identity
   *
   * @exception JMSRuntimeException if the session fails to create a temporary queue
   *                         due to some internal error.
   *
   *@since 2.0
   */

 TemporaryQueue
 createTemporaryQueue();


  /** Creates a <CODE>TemporaryTopic</CODE> object. Its lifetime will be that 
   * of the MessagingContext's <CODE>Connection</CODE> unless it is deleted earlier.
   *
   * @return a temporary topic identity
   *
   * @exception JMSRuntimeException if the session fails to create a temporary
   *                         topic due to some internal error.
   *
   * @since 2.0  
   */

 TemporaryTopic
 createTemporaryTopic();
 
    /** Creates a durable subscription with the specified name on the
     * specified topic, specifying a message selector and 
     * whether messages published by its own connection should be added to 
     * the durable subscription.  
     * <p>
     * A durable subscription is used by a client which needs to receive
     * all the messages published on a topic, including the ones published 
     * when there is no active consumer associated with it. 
     * The JMS provider retains a record of this durable subscription 
     * and ensures that all messages from the topic's publishers are retained 
     * until they are delivered to, and acknowledged by,
     * a consumer on this durable subscription
     * or until they have expired.
     * <p>
     * A durable subscription will continue to accumulate messages 
     * until it is deleted using the <code>unsubscribe</code> method. 
     * <p>
     * A consumer may be created on a durable subscription using the
     * <code>createSyncDurableSubscriber</code> methods on <code>MessagingContext</code>,
     * the appropriate <code>setMessageListener</code> methods on <code>MessagingContext</code>
     * or the <code>createDurableSubscriber</code> methods on <code>Session</code> or <code>TopicSession</code>
     * A durable subscription which has a consumer 
     * associated with it is described as being active. 
     * A durable subscription which has no consumer
     * associated with it is described as being inactive. 
     * <p>
     * Only one session at a time can have a
     * active consumer on a particular durable subscription.
     * <p>
     * A durable subscription is identified by a name specified by the client
     * and by the client identifier if set. If the client identifier was set
     * when the durable subscription was first created then a client which 
     * subsequently wishes to create a consumer on that durable subscription
     * or delete the durable subscription must use the same client identifier.
     * <P>
     * If a durable subscription already exists with the same name
     * and client identifier (if set) and the same topic and message selector
     * then this method does nothing.
     * <p>
     * A client can change an existing durable subscription by calling
     * <code>subscribe</code> 
     * with the same name and client identifier (if used),
     * and a new topic and/or message selector. 
     * Changing a durable subscriber is equivalent to 
     * unsubscribing (deleting) the old one and creating a new one.
     * <p>
     * The <code>noLocal</code> argument is for use when the MessagingContext's 
     * connection is also being used to publish messages to the topic. 
     * If <code>noLocal</code> is set to true then messages published
     * to the topic by its own connection will not be added to the
     * durable subscription. The default value of this 
     * argument is false. 
     *
     * @param topic the non-temporary <CODE>Topic</CODE> on which the durable subscription will be created.
     * @param name the name used to identify this subscription
     * @param messageSelector only messages with properties matching the
     * message selector expression are added to the durable subscription.  
     * A value of null or an empty string indicates that there is no message selector 
     * for the durable subscription.
     * @param noLocal if true, messages published by its own connection
     * will not be added to the durable subscription.
     *  
     * @exception JMSRuntimeException if the session fails to create the durable subscription
     *                         due to some internal error.
     * @exception InvalidDestinationRuntimeException if an invalid topic is specified.
     * @exception InvalidSelectorRuntimeException if the message selector is invalid.
     *
	 */
	void subscribe(Topic topic, String name, String messageSelector, boolean noLocal);


    /** Unsubscribes a durable subscription that has been created by a client.
     *  
     * <P>This method deletes the state being maintained on behalf of the 
     * subscriber by its provider.
     * <p> 
     * A durable subscription is identified by a name specified by the client
     * and by the client identifier if set. If the client identifier was set
     * when the durable subscription was created then a client which 
     * subsequently wishes to use this method to
     * delete a durable subscription must use the same client identifier.
     *
     * <P>It is erroneous for a client to delete a durable subscription
     * while there is an active consumer on that subscription, 
     * or while a consumed message is part of a pending 
     * transaction or has not been acknowledged in the session.
     * <P> 
     * If the active consumer is represented by a <CODE>SyncMessageConsumer</CODE> then calling
     * <CODE>close</CODE> on either that object or the <CODE>MessagingContext</CODE> used to create it
     * will render the consumer inactive and allow the subscription to be deleted. 
     * <P>
     * If the active consumer was created by calling <code>setMessageListener</code> on the <CODE>MessagingContext</CODE> 
     * then calling <CODE>close</CODE> on the <CODE>MessagingContext</CODE>
     * will render the consumer inactive and allow the subscription to be deleted. 
     * <p>
     * If the active consumer is represented by a <code>MessageConsumer</code> or <code>TopicSubscriber</code> then calling
     * <code>close</code> on that object or on the <code>Session</code> or <code>Connection</code> used to create it
     * will render the consumer inactive and allow the subscription to be deleted. 
     *
     * @param name the name used to identify this subscription
     *  
     * @exception JMSRuntimeException if the session fails to unsubscribe to the 
     *                         durable subscription due to some internal error.
     * @exception InvalidDestinationRuntimeException if an invalid subscription name
     *                                        is specified.
     *
     *   
     *   
	 */
	void unsubscribe(String name);
  
  // END OF METHODS COPIED FROM SESSION
  
  // START OF METHODS COPIED FROM MESSAGEPRODUCER
 
 /** Sets whether message IDs are disabled.
  *  
  * <P>Since message IDs take some effort to create and increase a
  * message's size, some JMS providers may be able to optimize message
  * overhead if they are given a hint that the message ID is not used by
  * an application. By calling the <CODE>setDisableMessageID</CODE>  
  * method on this MessagingContext, a JMS client enables this potential 
  * optimization for all messages sent by this MessagingContext. If the JMS 
  * provider accepts this hint, 
  * these messages must have the message ID set to null; if the provider 
  * ignores the hint, the message ID must be set to its normal unique value.
  *
  * <P>Message IDs are enabled by default.
  *
  * @param value indicates if message IDs are disabled
  *  
  * @exception JMSRuntimeException if the JMS provider fails to set message ID to
  *                         disabled due to some internal error.
  */ 

void setDisableMessageID(boolean value);


/** Gets an indication of whether message IDs are disabled.
  *  
  * @return an indication of whether message IDs are disabled
  *  
  * @exception JMSRuntimeException if the JMS provider fails to determine if 
  *                         message IDs are disabled due to some internal 
  *                         error.
  */ 

boolean getDisableMessageID();


/** Sets whether message timestamps are disabled.
  *  
  * <P>Since timestamps take some effort to create and increase a 
  * message's size, some JMS providers may be able to optimize message 
  * overhead if they are given a hint that the timestamp is not used by an 
  * application. By calling the <CODE>setDisableMessageTimestamp</CODE> 
  * method on this MessagingContext, a JMS client enables this potential 
  * optimization for all messages sent by this MessagingContext.  If the 
  * JMS provider accepts this hint, 
  * these messages must have the timestamp set to zero; if the provider 
  * ignores the hint, the timestamp must be set to its normal value.
  * <p> 
  * Message timestamps are enabled by default.
  *
  * @param value indicates if message timestamps are disabled
  *  
  * @exception JMSRuntimeException if the JMS provider fails to set timestamps to
  *                         disabled due to some internal error.
  */ 

void
setDisableMessageTimestamp(boolean value);


/** Gets an indication of whether message timestamps are disabled.
  *  
  * @return an indication of whether message timestamps are disabled
  *  
  * @exception JMSRuntimeException if the JMS provider fails to determine if 
  *                         timestamps are disabled due to some internal 
  *                         error.
  */ 

boolean
getDisableMessageTimestamp();


/** Sets the MessagingContext's default delivery mode.
  * <p>
  * Delivery mode is set to <CODE>PERSISTENT</CODE> by default.
  *
  * @param deliveryMode the message delivery mode for this MessagingContext;
  * legal values are <code>DeliveryMode.NON_PERSISTENT</code>
  * and <code>DeliveryMode.PERSISTENT</code>
  *  
  * @exception JMSRuntimeException if the JMS provider fails to set the delivery 
  *                         mode due to some internal error.          
  *
  * @see javax.jms.MessagingContext#getDeliveryMode
  * @see javax.jms.DeliveryMode#NON_PERSISTENT
  * @see javax.jms.DeliveryMode#PERSISTENT
  * @see javax.jms.Message#DEFAULT_DELIVERY_MODE
  */ 

void
setDeliveryMode(int deliveryMode);


/** Gets the MessagingContext's default delivery mode.
  *  
  * @return the message delivery mode for this MessagingContext
  *  
  * @exception JMSRuntimeException if the JMS provider fails to get the delivery 
  *                         mode due to some internal error.
  *
  * @see javax.jms.MessagingContext#setDeliveryMode
  */ 

int 
getDeliveryMode();


/** Sets the MessagingContext's default priority.
  *  
  * <P>The JMS API defines ten levels of priority value, with 0 as the 
  * lowest priority and 9 as the highest. Clients should consider priorities
  * 0-4 as gradations of normal priority and priorities 5-9 as gradations 
  * of expedited priority. Priority is set to 4 by default.
  *
  * @param defaultPriority the message priority for this MessagingContext;
  *                        must be a value between 0 and 9
  * 
  *  
  * @exception JMSException if the JMS provider fails to set the priority
  *                         due to some internal error.
  *
  * @see javax.jms.MessagingContext#getPriority
  * @see javax.jms.Message#DEFAULT_PRIORITY
  */ 

void
setPriority(int defaultPriority);


/** Gets the MessagingContext's default priority.
  *  
  * @return the message priority for this MessagingContext
  *  
  * @exception JMSRuntimeException if the JMS provider fails to get the priority
  *                         due to some internal error.
  *
  * @see javax.jms.MessageProducer#setPriority
  */ 

int 
getPriority();


/** Sets the default length of time in milliseconds from its dispatch time
  * that a produced message should be retained by the message system.
  *
  * <P>Time to live is set to zero by default.
  *
  * @param timeToLive the message time to live in milliseconds; zero is
  * unlimited
  *
  * @exception JMSRuntimeException if the JMS provider fails to set the time to 
  *                         live due to some internal error.
  *
  * @see javax.jms.MessagingContext#getTimeToLive
  * @see javax.jms.Message#DEFAULT_TIME_TO_LIVE
  */

void
setTimeToLive(long timeToLive);


/** Gets the default length of time in milliseconds from its dispatch time
  * that a produced message should be retained by the message system.
  *
  * @return the message time to live in milliseconds; zero is unlimited
  *
  * @exception JMSRuntimeException if the JMS provider fails to get the time to 
  *                         live due to some internal error.
  *
  * @see javax.jms.MessagingContext#setTimeToLive
  */ 

long
getTimeToLive();

/** Sets the default minimum length of time in milliseconds from its dispatch time
 * before a produced message becomes visible on the target destination and available
 * for delivery to consumers.  
 *
 * <P>deliveryDelay is set to zero by default.
 *
 * @param deliveryDelay the delivery delay in milliseconds.
 *
 * @exception JMSRuntimeException if the JMS provider fails to set the delivery
 *                         delay due to some internal error.
 *
 * @see javax.jms.MessagingContext#getDeliveryDelay
 * @see javax.jms.Message#DEFAULT_DELIVERY_DELAY
 */

void setDeliveryDelay(long deliveryDelay);      

/** Gets the default minimum length of time in milliseconds from its dispatch time
* before a produced message becomes visible on the target destination and available
* for delivery to consumers.  
*
* @return the delivery delay in milliseconds.
*
* @exception JMSRuntimeException if the JMS provider fails to get the delivery 
*                         delay due to some internal error.
*
* @see javax.jms.MessagingContext#setDeliveryDelay
*/ 

long getDeliveryDelay();   

    /** Sends a message to the specified destination, using
     * the <CODE>MessagingContext</CODE>'s default delivery mode, priority,
     * and time to live.
     *
     * @param destination the destination to send this message to
     * @param message the message to send
     *  
     * @exception JMSRuntimeException if the JMS provider fails to send the message 
     *                         due to some internal error.
     * @exception MessageFormatRuntimeException if an invalid message is specified.
     * @exception InvalidDestinationRuntimeException if a client uses
     *                         this method with an invalid destination.
     * 
     * @see javax.jms.MessagingContext#setDeliveryMode
     * @see javax.jms.MessagingContext#setPriority
     * @see javax.jms.MessagingContext#setTimeToLive
     *
	 */
	void send(Destination destination, Message message);

    /** Sends a message to the specified destination, 
     * specifying delivery mode, priority and time to live.
     *  
     * @param destination the destination to send this message to
     * @param message the message to send
     * @param deliveryMode the delivery mode to use
     * @param priority the priority for this message
     * @param timeToLive the message's lifetime (in milliseconds)
     *  
     * @exception JMSRuntimeException if the JMS provider fails to send the message 
     *                         due to some internal error.
     * @exception MessageFormatRuntimeException if an invalid message is specified.
     * @exception InvalidDestinationRuntimeException if a client uses
     *                         this method with an invalid destination.
     *
	 */
	void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive);
	
    /** Sends a message to the specified destination, using
     * the <CODE>MessagingContext</CODE>'s default delivery mode, priority,
     * and time to live,
     * returning immediately and notifying the specified completion listener 
     * when the operation has completed.
     * <p>
     * This method allows the JMS provider to perform the actual sending of the message,
     * and the wait for any confirmation from a JMS server, to take place in a separate thread
     * without blocking the calling thread. When the sending of the message is complete,
     * and any confirmation has been received from a JMS server, the JMS provider calls
     * the <code>onCompletion(Message)</code> method of the specified completion listener. 
     * If an exception occurs in the separate thread 
     * then the JMS provider calls the <code>onException(Exception)</code> method of the specified completion listener.
     * <p>
     * JMS does not define what operations are performed in the calling thread and what operations, if any,
     * are performed in the separate thread. In particular the use of this method does not itself specify whether
     * the separate thread should obtain confirmation from a JMS server. 
     * <p>
     * The exceptions listed below may be thrown in either thread. 
     *
     * @param destination the destination to send this message to
     * @param message the message to send
     * @param completionListener a <code>CompletionListener</code> to be notified when the send has completed
     *  
     * @exception JMSRuntimeException if the JMS provider fails to send the message 
     *                         due to some internal error.
     * @exception MessageFormatRuntimeException if an invalid message is specified.
     * @exception InvalidDestinationRuntimeException if a client uses
     *                         this method with an invalid destination.
     * 
     * @see javax.jms.MessagingContext#setDeliveryMode
     * @see javax.jms.MessagingContext#setPriority
     * @see javax.jms.MessagingContext#setTimeToLive
     * @see javax.jms.CompletionListener
     *
	 */
	void send(Destination destination, Message message,CompletionListener completionListener);

    /** Sends a message to the specified destination, 
     * specifying delivery mode, priority and time to live,
     * returning immediately and notifying the specified completion listener 
     * when the operation has completed.
     * <p>
     * This method allows the JMS provider to perform the actual sending of the message,
     * and the wait for any confirmation from a JMS server, to take place in a separate thread
     * without blocking the calling thread. When the sending of the message is complete,
     * and any confirmation has been received from a JMS server, the JMS provider calls
     * the <code>onCompletion(Message)</code> method of the specified completion listener. 
     * If an exception occurs in the separate thread 
     * then the JMS provider calls the <code>onException(Exception)</code> method of the specified completion listener.
     * <p>
     * JMS does not define what operations are performed in the calling thread and what operations, if any,
     * are performed in the separate thread. In particular the use of this method does not itself specify whether
     * the separate thread should obtain confirmation from a JMS server. 
     * <p>
     * The exceptions listed below may be thrown in either thread. 
     * 
     * @param destination the destination to send this message to
     * @param message the message to send
     * @param deliveryMode the delivery mode to use
     * @param priority the priority for this message
     * @param timeToLive the message's lifetime (in milliseconds)
     * @param completionListener a <code>CompletionListener</code> to be notified when the send has completed
     *  
     * @exception JMSRuntimeException if the JMS provider fails to send the message 
     *                         due to some internal error.
     * @exception MessageFormatRuntimeException if an invalid message is specified.
     * @exception InvalidDestinationRuntimeException if a client uses
     *                         this method with an invalid destination.
     *
     * @see javax.jms.CompletionListener
  	 */
	void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive,CompletionListener completionListener);	
  
// END OF METHODS COPIED FROM MESSAGEPRODUCER
  
// START OF NEW METHODS FOR ASYNC MESSAGE CONSUPTION

	/**
	 * Creates a new consumer on the specified destination that will deliver
	 * messages to the specified <code>MessageListener</code>.
	 * <p>
	 * If the specified listener is null then this method does nothing.
	 * 
	 * @param destination - The destination from which messages are to be consumed
	 * @param listener - The listener to which the messages are to be delivered
	 * @throws JMSRuntimeException - If the operation fails due to some internal error.
	 * @throws InvalidDestinationRuntimeException - If an invalid destination is specified.
	 */
	void setMessageListener(Destination destination, MessageListener listener);

	/**
	 * Creates a new consumer on the specified destination that will deliver
	 * messages in batches to the specified <code>BatchMessageListener</code> 
	 * using the specified maximum batch size and timeout.
	 * <p>
	 * Messages will be delivered to the specified <code>BatchMessageListener</code> 
	 * in batches whose size is no greater than the specified maximum batch size. 
	 * The JMS provider may defer message
	 * delivery until the specified batch timeout has expired in order to
	 * assemble a batch of messages that is as large as possible but no greater
	 * than the batch size.
	 * <p>
	 * If the specified listener is null then this method does nothing.
	 * 
	 * @param destination - The destination from which messages are to be consumed
	 * @param listener - The listener to which the messages are to be delivered
	 * @param maxBatchSize - The maximum batch size that should be used. Must be greater than zero.
	 * @param batchTimeout - The batch timeout in milliseconds. A value of zero means no
	 *            timeout is required The JMS provider may override the
	 *            specified timeout with a shorter value.
	 * 
	 * @throws JMSRuntimeException - If the operation fails due to some internal error.
	 * @throws InvalidDestinationRuntimeException - If an invalid destinaiton is specified.
	 */
	void setBatchMessageListener(Destination destination, BatchMessageListener listener, int maxBatchSize,
			long batchTimeout);

    /**
     * Creates a consumer on the specified destination,
     * that will deliver messages to the specified <code>MessageListener</code>,
     * using a message selector.
     * <p>
     * If the specified listener is null then this method does nothing.
     * 
     * @param destination - The destination from which messages are to be consumed
     * @param messageSelector - Only messages with properties matching the message selector
     *            expression are delivered. A value of null or an empty string
     *            indicates that there is no message selector for the message
     *            consumer.
     * @param listener - the listener to which the messages are to be delivered 
     * @throws JMSRuntimeException - If the operation fails due to some internal error.
     * @throws InvalidDestinationRuntimeException - If an invalid destination is specified.
     * @throws InvalidSelectorRuntimeException - If the message selector is invalid.
     */
	void setMessageListener(Destination destination, String messageSelector, MessageListener listener);
	
    /**
     * Creates a consumer on the specified destination,
     * that will deliver messages in batches to the specified <code>BatchMessageListener</code> 
     * using a message selector and the specified maximum batch size and timeout.
     * and 
	 * <p>
	 * Messages will be delivered to the specified <code>BatchMessageListener</code> 
	 * in batches whose size is no greater than the specified maximum batch size. 
	 * The JMS provider may defer message
	 * delivery until the specified batch timeout has expired in order to
	 * assemble a batch of messages that is as large as possible but no greater
	 * than the batch size.
     * <p>
     * If the specified listener is null then this method does nothing.
     * 
     * @param destination - The destination from which messages are to be consumed
     * @param messageSelector - Only messages with properties matching the message selector
     *            expression are delivered. A value of null or an empty string
     *            indicates that there is no message selector for the message
     *            consumer.
     * @param listener - the listener to which the messages are to be delivered 
	 * @param maxBatchSize - The maximum batch size that should be used. Must be greater than zero.
	 * @param batchTimeout - The batch timeout in milliseconds. A value of zero means no
	 *            timeout is required The JMS provider may override the
	 *            specified timeout with a shorter value.
     * @throws JMSRuntimeException - If the operation fails due to some internal error.
     * @throws InvalidDestinationRuntimeException - If an invalid destination is specified.
     * @throws InvalidSelectorRuntimeException - If the message selector is invalid.
     */
	void setBatchMessageListener(Destination destination, String messageSelector, BatchMessageListener listener, int maxBatchSize,
			long batchTimeout);	

	/**
	 * Creates a consumer on the specified destination, 
	 * that will deliver messages to the specified <code>MessageListener</code>,
	 * using a message selector. This method can specify whether messages 
	 * published by its own connection should be delivered to it, if the destination is a topic.
     * <p>
     * The <code>noLocal</code> argument is for use when the
     * destination is a topic and the MessagingContext's connection
     * is also being used to publish messages to that topic.
     * If <code>noLocal</code> is set to true then the
     * consumer will not receive messages published
     * to the topic by its own connection. The default value of this
     * argument is false. If the destination is a queue
     * then the effect of setting <code>noLocal</code>
     * to true is not specified.
	 * <p>
	 * If the specified listener is null then this method does nothing.
     *
	 * @param destination - The destination from which messages are to be consumed
	 * @param messageSelector - Only messages with properties matching the message selector
	 *            expression are delivered. A value of null or an empty string
	 *            indicates that there is no message selector for the message
	 *            consumer.
     * @param noLocal  - if true, and the destination is a topic,
     *                   then the consumer will
     *                   not receive messages published to the topic
     *                   by its own connection.
	 * @param listener - The listener to which the messages are to be delivered
	 * @throws JMSRuntimeException - If the operation fails due to some internal error.
	 * @throws InvalidDestinationRuntimeException - If an invalid destination is specified.
	 * @throws InvalidSelectorRuntimeException - If the message selector is invalid.
	 */
	void setMessageListener(Destination destination, String messageSelector, boolean noLocal, MessageListener listener);
	
	/**
	 * Creates a consumer on the specified destination, 
	 * that will deliver messages in batches to the specified <code>BatchMessageListener</code>,
	 * using a message selector and the specified maxBatchSize and timeout. 
	 * This method can specify whether messages 
	 * published by its own connection should be delivered to it, if the destination is a topic.
	 * <p>
	 * Messages will be delivered to the specified <code>BatchMessageListener</code> 
	 * in batches whose size is no greater than the specified maximum batch size. 
	 * The JMS provider may defer message
	 * delivery until the specified batch timeout has expired in order to
	 * assemble a batch of messages that is as large as possible but no greater
	 * than the batch size.
	 * <p>
     * The <code>noLocal</code> argument is for use when the
     * destination is a topic and the MessagingContext's connection
     * is also being used to publish messages to that topic.
     * If <code>noLocal</code> is set to true then the
     * consumer will not receive messages published
     * to the topic by its own connection. The default value of this
     * argument is false. If the destination is a queue
     * then the effect of setting <code>noLocal</code>
     * to true is not specified.
	 * <p>
	 * If the specified listener is null then this method does nothing.
	 * 
	 * @param destination - The destination from which messages are to be consumed
	 * @param messageSelector - Only messages with properties matching the message selector
	 *            expression are delivered. A value of null or an empty string
	 *            indicates that there is no message selector for the message
	 *            consumer.
     * @param noLocal  - if true, and the destination is a topic,
     *                   then the consumer will
     *                   not receive messages published to the topic
     *                   by its own connection.
	 * @param listener - The listener to which the messages are to be delivered
	 * @param maxBatchSize - The maximum batch size that should be used. Must be greater than zero.
	 * @param batchTimeout - The batch timeout in milliseconds. A value of zero means no
	 *            timeout is required The JMS provider may override the
	 *            specified timeout with a shorter value.
	 * @throws JMSRuntimeException - If the operation fails due to some internal error.
	 * @throws InvalidDestinationRuntimeException - If an invalid destination is specified.
	 * @throws InvalidSelectorRuntimeException - If the message selector is invalid.
	 */
	void setBatchMessageListener(Destination destination, String messageSelector, boolean noLocal, BatchMessageListener listener, int maxBatchSize,
			long batchTimeout);		

	/**
	 * Creates a durable subscription with the specified name on the specified topic, 
	 * and creates a consumer on that durable subscription 
	 * that will deliver messages to the specified <code>MessageListener</code>. 
     * <p>
     * If a durable subscription already exists with the same name 
     * and client identifier (if set) and the same topic and message selector 
     * then this method creates a consumer on the existing durable subscription 
	 * that will deliver messages to the specified <code>MessageListener</code>. 
	 * <p>
     * A durable subscription is used by a client which needs to receive
     * all the messages published on a topic, including the ones published 
     * when there is no active consumer associated with it. 
     * The JMS provider retains a record of this durable subscription 
     * and ensures that all messages from the topic's publishers are retained 
     * until they are delivered to, and acknowledged by,
     * a consumer on this durable subscription
     * or until they have expired.
     * <p>
     * A durable subscription will continue to accumulate messages 
     * until it is deleted using the <code>unsubscribe</code> method. 
     * <p>
     * A consumer may be created on a durable subscription using the
     * <code>createSyncDurableSubscriber</code> methods on <code>MessagingContext</code>,
     * the appropriate <code>setMessageListener</code> methods on <code>MessagingContext</code>
     * or the <code>createDurableSubscriber</code> methods on <code>Session</code> or <code>TopicSession</code>
     * A durable subscription which has a consumer 
     * associated with it is described as being active. 
     * A durable subscription which has no consumer
     * associated with it is described as being inactive. 
     * <p>
     * Only one session or messaging context at a time can have a
     * active consumer on a particular durable subscription.
     * <p>
     * A durable subscription is identified by a name specified by the client
     * and by the client identifier if set. If the client identifier was set
     * when the durable subscription was first created then a client which 
     * subsequently wishes to create a consumer
     * on that durable subscription must use the same client identifier.
     * <p>
     * A client can change an existing durable subscription by calling
     * this or any of the other methods listed above
     * with the same name and client identifier (if used),
     * and a new topic and/or message selector. 
     * Changing a durable subscriber is equivalent to 
     * unsubscribing (deleting) the old one and creating a new one.
	 * <p>
	 * If the specified listener is null then this method does nothing.
	 * 
	 * @param topic - The topic from which messages are to be consumed
	 * @param subscriptionName - The name used to identify the durable subscription 
	 * @param listener - The listener to which the messages are to be delivered 
	 * @throws JMSRuntimeException - If the operation fails due to some internal error.
	 * @throws InvalidDestinationRuntimeException - If an invalid topic is specified.
	 */
void setMessageListener (Topic topic, String subscriptionName, MessageListener listener);

    /**
     * Creates a durable subscription with the specified name on the specified topic, 
     * and creates a consumer on that durable subscription 
     * that will deliver messages in batches to the specified <code>BatchMessageListener</code>
	 * using the specified maximum batch size and timeout.
     * <p>
     * If a durable subscription already exists with the same name 
     * and client identifier (if set) and the same topic and message selector 
     * then this method creates a consumer on the existing durable subscription 
	 * that will deliver messages to the specified <code>BatchMessageListener</code>. 
     * <p>
     * A durable subscription is used by a client which needs to receive
     * all the messages published on a topic, including the ones published 
     * when there is no active consumer associated with it. 
     * The JMS provider retains a record of this durable subscription 
     * and ensures that all messages from the topic's publishers are retained 
     * until they are delivered to, and acknowledged by,
     * a consumer on this durable subscription
     * or until they have expired.
     * <p>
     * A durable subscription will continue to accumulate messages 
     * until it is deleted using the <code>unsubscribe</code> method. 
     * <p>
     * A consumer may be created on a durable subscription using the
     * <code>createSyncDurableSubscriber</code> methods on <code>MessagingContext</code>,
     * the appropriate <code>setMessageListener</code> methods on <code>MessagingContext</code>
     * or the <code>createDurableSubscriber</code> methods on <code>Session</code> or <code>TopicSession</code>
     * A durable subscription which has a consumer 
     * associated with it is described as being active. 
     * A durable subscription which has no consumer
     * associated with it is described as being inactive. 
     * <p>
     * Only one session or messaging context at a time can have a
     * active consumer on a particular durable subscription.
     * <p>
     * A durable subscription is identified by a name specified by the client
     * and by the client identifier if set. If the client identifier was set
     * when the durable subscription was first created then a client which 
     * subsequently wishes to create a consumer
     * on that durable subscription must use the same client identifier.
     * <p>
     * A client can change an existing durable subscription by calling
     * this or any of the other methods listed above
     * with the same name and client identifier (if used),
     * and a new topic and/or message selector. 
     * Changing a durable subscriber is equivalent to 
     * unsubscribing (deleting) the old one and creating a new one.
	 * <p>
	 * Messages will be delivered to the specified <code>BatchMessageListener</code> 
	 * in batches whose size is no greater than the specified maximum batch size. 
	 * The JMS provider may defer message
	 * delivery until the specified batch timeout has expired in order to
	 * assemble a batch of messages that is as large as possible but no greater
	 * than the batch size.
	 * <p>
	 * If the specified listener is null then this method does nothing.
	 * 
	 * @param topic - The topic from which messages are to be consumed
	 * @param subscriptionName - The name used to identify the durable subscription 
	 * @param listener - The listener to which the messages are to be delivered 
	 * @param maxBatchSize - The maximum batch size that should be used. Must be greater than zero.
	 * @param batchTimeout - The batch timeout in milliseconds. A value of zero means no
	 *            timeout is required The JMS provider may override the
	 *            specified timeout with a shorter value.
	 * @throws JMSRuntimeException - If the operation fails due to some internal error.
	 * @throws InvalidDestinationRuntimeException - If an invalid topic is specified.
	 */
	void setBatchMessageListener(Topic topic, String subscriptionName, BatchMessageListener listener, int maxBatchSize,
			long batchTimeout);

	/**
	 * Creates a durable subscription with the specified name on the specified topic, 
	 * and creates a consumer on that durable subscription 
	 * that will deliver messages to the specified <code>MessageListener</code>, 
	 * using a message selector and specifying whether messages published by its
     * own connection should be added to the durable subscription. 
     * <p>
     * If a durable subscription already exists with the same name 
     * and client identifier (if set) and the same topic and message selector 
     * then this method creates a consumer on the existing durable subscription 
	 * that will deliver messages to the specified <code>MessageListener</code>. 
	 * <p>
     * A durable subscription is used by a client which needs to receive
     * all the messages published on a topic, including the ones published 
     * when there is no active consumer associated with it. 
     * The JMS provider retains a record of this durable subscription 
     * and ensures that all messages from the topic's publishers are retained 
     * until they are delivered to, and acknowledged by,
     * a consumer on this durable subscription
     * or until they have expired.
     * <p>
     * A durable subscription will continue to accumulate messages 
     * until it is deleted using the <code>unsubscribe</code> method. 
     * <p>
     * A consumer may be created on a durable subscription using the
     * <code>createSyncDurableSubscriber</code> methods on <code>MessagingContext</code>,
     * the appropriate <code>setMessageListener</code> methods on <code>MessagingContext</code>
     * or the <code>createDurableSubscriber</code> methods on <code>Session</code> or <code>TopicSession</code>
     * A durable subscription which has a consumer 
     * associated with it is described as being active. 
     * A durable subscription which has no consumer
     * associated with it is described as being inactive. 
     * <p>
     * Only one session or messaging context at a time can have a
     * active consumer on a particular durable subscription.
     * <p>
     * A durable subscription is identified by a name specified by the client
     * and by the client identifier if set. If the client identifier was set
     * when the durable subscription was first created then a client which 
     * subsequently wishes to create a consumer
     * on that durable subscription must use the same client identifier.
     * <p>
     * A client can change an existing durable subscription by calling
     * this or any of the other methods listed above
     * with the same name and client identifier (if used),
     * and a new topic and/or message selector. 
     * Changing a durable subscriber is equivalent to 
     * unsubscribing (deleting) the old one and creating a new one.
	 * <p>
     * The <code>noLocal</code> argument is for use when the MessagingContext's
     * connection is also being used to publish messages to the topic.
     * If <code>noLocal</code> is set to true then messages published
     * to the topic by its own connection will not be added to the
     * durable subscription. The default value of this
     * argument is false.
	 * <p>
	 * If the specified listener is null then this method does nothing.
	 * 
	 * @param topic - The topic from which messages are to be consumed
	 * @param subscriptionName - The name used to identify the durable subscription 
	 * @param messageSelector - Only messages with properties matching the message selector
	 *            expression are delivered. A value of null or an empty string
	 *            indicates that there is no message selector for the message
	 *            consumer.
     * @param noLocal if true, messages published by its own connection
     *            will not be added to the durable subscription.
	 * @param listener - The listener to which the messages are to be delivered
	 * @throws JMSRuntimeException - If the operation fails due to some internal error.
	 * @throws InvalidDestinationRuntimeException - If an invalid topic is specified.
	 * @throws InvalidSelectorRuntimeException - If the message selector is invalid.
	 */
	void setMessageListener(Topic topic, String subscriptionName, String messageSelector, boolean noLocal,
			MessageListener listener);
	
    /**
     * Creates a durable subscription with the specified name on the specified topic, 
     * and creates a consumer on that durable subscription 
     * that will deliver messages in batches to the specified <code>BatchMessageListener</code>
     * using a message selector, the specified maximum batch size and timeout,
	 * and specifying whether messages published by its own connection
	 * should be added to the durable subscription.
     * <p>
     * If a durable subscription already exists with the same name 
     * and client identifier (if set) and the same topic and message selector 
     * then this method creates a consumer on the existing durable subscription 
	 * that will deliver messages to the specified <code>BatchMessageListener</code>. 
     * <p>
     * A durable subscription is used by a client which needs to receive
     * all the messages published on a topic, including the ones published 
     * when there is no active consumer associated with it. 
     * The JMS provider retains a record of this durable subscription 
     * and ensures that all messages from the topic's publishers are retained 
     * until they are delivered to, and acknowledged by,
     * a consumer on this durable subscription
     * or until they have expired.
     * <p>
     * A durable subscription will continue to accumulate messages 
     * until it is deleted using the <code>unsubscribe</code> method. 
     * <p>
     * A consumer may be created on a durable subscription using the
     * <code>createSyncDurableSubscriber</code> methods on <code>MessagingContext</code>,
     * the appropriate <code>setMessageListener</code> methods on <code>MessagingContext</code>
     * or the <code>createDurableSubscriber</code> methods on <code>Session</code> or <code>TopicSession</code>
     * A durable subscription which has a consumer 
     * associated with it is described as being active. 
     * A durable subscription which has no consumer
     * associated with it is described as being inactive. 
     * <p>
     * Only one session or messaging context at a time can have a
     * active consumer on a particular durable subscription.
     * <p>
     * A durable subscription is identified by a name specified by the client
     * and by the client identifier if set. If the client identifier was set
     * when the durable subscription was first created then a client which 
     * subsequently wishes to create a consumer
     * on that durable subscription must use the same client identifier.
     * <p>
     * A client can change an existing durable subscription by calling
     * this or any of the other methods listed above
     * with the same name and client identifier (if used),
     * and a new topic and/or message selector. 
     * Changing a durable subscriber is equivalent to 
     * unsubscribing (deleting) the old one and creating a new one.
	 * <p>
	 * Messages will be delivered to the specified <code>BatchMessageListener</code> 
	 * in batches whose size is no greater than the specified maximum batch size. 
	 * The JMS provider may defer message
	 * delivery until the specified batch timeout has expired in order to
	 * assemble a batch of messages that is as large as possible but no greater
	 * than the batch size.
	 * <p>
	 * The <code>noLocal</code> argument is for use when the MessagingContext's
     * connection is also being used to publish messages to the topic.
     * If <code>noLocal</code> is set to true then messages published
     * to the topic by its own connection will not be added to the
     * durable subscription. The default value of this
     * argument is false.
	 * <p>
	 * If the specified listener is null then this method does nothing.
	 * 
	 * @param topic - The topic from which messages are to be consumed
	 * @param subscriptionName - The name used to identify the durable subscription 
	 * @param messageSelector - Only messages with properties matching the message selector
	 *            expression are delivered. A value of null or an empty string
	 *            indicates that there is no message selector for the message
	 *            consumer.
     * @param noLocal if true, messages published by its own connection
     *            will not be added to the durable subscription.
	 * @param listener - The listener to which the messages are to be delivered
	 * @param maxBatchSize - The maximum batch size that should be used. Must be greater than zero.
	 * @param batchTimeout - The batch timeout in milliseconds. A value of zero means no
	 *            timeout is required The JMS provider may override the
	 *            specified timeout with a shorter value.
	 * @throws JMSRuntimeException - If the operation fails due to some internal error.
	 * @throws InvalidDestinationRuntimeException - If an invalid topic is specified.
	 * @throws InvalidSelectorRuntimeException - If the message selector is invalid.
	 */
	void setBatchMessageListener(Topic topic, String subscriptionName, String messageSelector, boolean noLocal,
			BatchMessageListener listener, int maxBatchSize, long batchTimeout);		

// END OF NEW METHODS FOR ASYNC MESSAGE CONSUPTION

// START OF NEW MESSAGE PAYLOAD CONVENIENCE METHODS

/**
 * Send a TextMessage with the specified payload to the specified destination, using
 * the MessagingContext's default delivery mode, priority and time to live.
 * 
 * @param destination - the destination to send this message to
 * @param payload - the payload of the TextMessage that will be sent.  
 * @throws JMSRuntimeException if the JMS provider fails to send the message due to some internal error.
 * @throws MessageFormatRuntimeException if an invalid message is specified.
 * @throws InvalidDestinationRuntimeException if a client uses this method with an invalid destination.
  * @see javax.jms.MessagingContext#setDeliveryMode
  * @see javax.jms.MessagingContext#setPriority
  * @see javax.jms.MessagingContext#setTimeToLive
 */
void send(Destination destination, String payload);

/**
 * Send an ObjectMessage with the specified payload to the specified destination, using
 * the MessagingContext's default delivery mode, priority and time to live.
 * 
 * @param destination - the destination to send this message to
 * @param payload - the payload of the ObjectMessage that will be sent.  
 * @throws JMSRuntimeException if the JMS provider fails to send the message due to some internal error.
 * @throws MessageFormatRuntimeException if an invalid message is specified.
 * @throws InvalidDestinationRuntimeException if a client uses this method with an invalid destination.
  * @see javax.jms.MessagingContext#setDeliveryMode
  * @see javax.jms.MessagingContext#setPriority
  * @see javax.jms.MessagingContext#setTimeToLive
 */
void send(Destination destination, Serializable payload);

// END OF NEW MESSAGE PAYLOAD CONVENIENCE METHODS

// START OF METHODS COPIED FROM MESSAGE

/** 
 * Acknowledges all messages consumed by the MessagingContext's session.
 * <p> 
 * This method is for use when the session has an acknowledgement mode of 
 * CLIENT_ACKNOWLEDGE. If the session is transacted or has an acknowledgement
 * mode of AUTO_ACKNOWLEDGE or DUPS_OK_ACHNOWLEDGE calling this method has no effect.
 * <p>
 * This method has identical behaviour to the <code>acknowledge</code>
 * method on <code>Message</code>. A client may individually acknowledge 
 * each message as it is consumed, or it may choose to acknowledge messages 
 * as an application-defined group. In both cases it makes no difference 
 * which of these two methods is used. 
 * <p>
 * Messages that have been received but not acknowledged may be 
 * redelivered.
 *
 * @exception JMSException if the JMS provider fails to acknowledge the
 *                         messages due to some internal error.
 * @exception IllegalStateException if this method is called on a closed
 *                         session.
 *
 * @see javax.jms.Session#CLIENT_ACKNOWLEDGE
 * @see javax.jms.Message#acknowledge
 */ 

void acknowledge() throws JMSException;

//END OF METHODS COPIED FROM MESSAGE


}
