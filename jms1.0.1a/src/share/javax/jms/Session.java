
/*
 * @(#)Session.java	1.24 98/10/29
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

import java.io.Serializable;

/** <P>A JMS Session is a single threaded context for producing and consuming 
  * messages. Although it may allocate provider resources outside the Java 
  * virtual machine, it is considered a light-weight JMS object.
  *
  * <P>A session serves several purposes:
  *
  * <UL>
  *   <LI>It is a factory for its message producers and consumers.
  *   <LI>It supplies provider-optimized message factories.
  *   <LI>It supports a single series of transactions that combine work 
  *       spanning its producers and consumers into atomic units.
  *   <LI>A session defines a serial order for the messages it consumes and 
  *       the messages it produces.
  *   <LI>A session retains messages it consumes until they have been 
  *       acknowledged.
  *   <LI>A session serializes execution of message listeners registered with 
  *       it's message consumers.
  * </UL>
  *
  * <P>A session can create and service multiple message producers and 
  * consumers.
  *
  * <P>One typical use is to have a thread block on a synchronous 
  * MessageConsumer until a message arrives. The thread may then use one or 
  * more of the Session's MessageProducers.
  *
  * <P>Another typical use is to have one thread set up a Session by creating 
  * its producers and one or more asynchronous consumers. Since the JMS 
  * provider serializes execution of a Session's asynchronous consumers, they 
  * can safely share the resources of their session.
  *
  * <P>If a client desires to have one thread producing messages while others 
  * consume them, the client should use a separate Session for its producing 
  * thread.
  *
  * <P>It should be easy for most clients to partition their work naturally 
  * into Sessions. This model allows clients to start simply and incrementally
  * add message processing complexity as their need for concurrency grows.
  *
  * <P>A session may be optionally specified as transacted. Each transacted 
  * session supports a single series of transactions. Each transaction groups 
  * a set of message sends and a set of message receives into an atomic unit 
  * of work. In effect, transactions organize a session's input message 
  * stream and output message stream into series of atomic units. When a 
  * transaction commits, its atomic unit of input is acknowledged and its 
  * associated atomic unit of output is sent. If a transaction rollback is 
  * done, its sent messages are destroyed and the session's input is 
  * automatically recovered.
  *
  * <P>The content of a transaction's input and output units is simply those 
  * messages that have been produced and consumed within the session's current 
  * transaction.
  *
  * <P>A transaction is completed using either its session's 
  * <CODE>commit</CODE> or <CODE>rollback</CODE> method. The completion of a 
  * session's current transaction automatically begins the next. The result 
  * is that a transacted session always has a current transaction within 
  * which its work is done.  
  *
  * <P>JTS, or some other transaction monitor may be used to combine a 
  * session's transaction with transactions on other resources (databases,
  * other JMS sessions, etc.). Since Java distributed transactions are 
  * controlled via JTA, use of the session's commit and rollback methods in 
  * this context is prohibited.
  *
  * <P>JMS does not require support for JTA; however, it does define how a 
  * provider supplies this support.
  *
  * <P>Although it is also possible for a JMS client to handle distributed 
  * transactions directly, it is unlikely that many JMS clients will do this.
  * Support for JTA in JMS is targeted at systems vendors who will be 
  * integrating JMS into their application server products.
  *
  * @version     1.0 - 6 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.QueueSession
  * @see         javax.jms.TopicSession
  * @see         javax.jms.XASession
  */ 
 
public interface Session extends Runnable {

    /** With this acknowledgement mode, the session automatically acknowledges
      * a client's receipt of a message when it has either successfully 
      * returned from a call to receive or the message listener it has called 
      * to process the message successfully returns.
      */ 

    static final int AUTO_ACKNOWLEDGE = 1;

    /** With this acknowledgement mode, the client acknowledges a message by 
      * calling a message's acknowledge method. Acknowledging a message 
      * acknowledges all messages that the Session has consumed.
      *
      * <P>When client acknowledgment mode is used, a client may build up a 
      * large number of unacknowledged messages while attempting to process 
      * them. A JMS provider should provide administrators with a way to 
      * limit client over-run so that clients are not driven to resource 
      * exhaustion and ensuing failure when some resource they are using 
      * is temporarily blocked.
      */ 

    static final int CLIENT_ACKNOWLEDGE = 2;

    /** This acknowledgement mode instructs the session to lazily acknowledge 
      * the delivery of messages. This is likely to result in the delivery of 
      * some duplicate messages if JMS fails, it should only be used by 
      * consumers that are tolerant of duplicate messages. Its benefit is the 
      * reduction of session overhead achieved by minimizing the work the 
      * session does to prevent duplicates.
      */

    static final int DUPS_OK_ACKNOWLEDGE = 3;

    /** Create a BytesMessage. A BytesMessage is used to send a message 
      * containing a stream of uninterpreted bytes.
      *  
      * @exception JMSException if JMS fails to create this message
      *                         due to some internal error.
      */ 

    BytesMessage 
    createBytesMessage() throws JMSException; 

 
    /** Create a MapMessage. A MapMessage is used to send a self-defining 
      * set of name-value pairs where names are Strings and values are Java 
      * primitive types.
      *  
      * @exception JMSException if JMS fails to create this message
      *                         due to some internal error.
      */ 

    MapMessage 
    createMapMessage() throws JMSException; 

 
    /** Create a Message. The Message interface is the root interface of 
      * all JMS messages. It holds all the standard message header 
      * information. It can be sent when a message containing only header 
      * information is sufficient.
      *  
      * @exception JMSException if JMS fails to create this message
      *                         due to some internal error.
      */ 

    Message
    createMessage() throws JMSException;


    /** Create an ObjectMessage. An ObjectMessage is used to send a message 
      * that containing a serializable Java object.
      *  
      * @exception JMSException if JMS fails to create this message
      *                         due to some internal error.
      */ 

    ObjectMessage
    createObjectMessage() throws JMSException; 


    /** Create an initialized ObjectMessage. An ObjectMessage is used 
      * to send a message that containing a serializable Java object.
      *  
      * @param object the object to use to initialize this message.
      *
      * @exception JMSException if JMS fails to create this message
      *                         due to some internal error.
      */ 

    ObjectMessage
    createObjectMessage(Serializable object) throws JMSException;

 
    /** Create a StreamMessage. A StreamMessage is used to send a 
      * self-defining stream of Java primitives.
      *  
      * @exception JMSException if JMS fails to create this message
      *                         due to some internal error.
      */

    StreamMessage 
    createStreamMessage() throws JMSException;  

 
    /** Create a TextMessage. A TextMessage is used to send a message 
      * containing a StringBuffer.
      *  
      * @exception JMSException if JMS fails to create this message
      *                         due to some internal error.
      */ 

    TextMessage 
    createTextMessage() throws JMSException; 


    /** Create an initialized TextMessage. A TextMessage is used to send 
      * a message containing a StringBuffer.
      *  
      * @param stringBuffer the string buffer used to initialize this message.
      *
      * @exception JMSException if JMS fails to create this message
      *                         due to some internal error.
      */ 

    TextMessage
    createTextMessage(StringBuffer stringBuffer) throws JMSException;


    /** Is the session in transacted mode?
      *  
      * @return true if in transacted mode
      *  
      * @exception JMSException if JMS fails to return the transaction
      *                         mode due to internal error in JMS Provider.
      */ 

    boolean
    getTransacted() throws JMSException;


    /** Commit all messages done in this transaction and releases any locks 
      * currently held.
      *
      * @exception JMSException if JMS implementation fails to commit the
      *                         the transaction due to some internal error.
      * @exception TransactionRolledBackException  if the transaction
      *                         gets rolled back due to some internal error
      *                         during commit.
      */

    void
    commit() throws JMSException;


    /** Rollback any messages done in this transaction and releases any locks 
      * currently held.
      *
      * @exception JMSException if JMS implementation fails to rollback the
      *                         the transaction due to some internal error.
      */

    void
    rollback() throws JMSException;


    /** Since a provider may allocate some resources on behalf of a Session 
      * outside the JVM, clients should close them when they are not needed. 
      * Relying on garbage collection to eventually reclaim these resources 
      * may not be timely enough.
      *
      * @exception JMSException if JMS implementation fails to close a 
      *                         Session due to some internal error.
      */

    void
    close() throws JMSException;


    /** Stop message delivery in this session, and restart sending messages
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
      *       acknowledged as `redelivered'
      *   <LI>Restart the delivery sequence including all unacknowledged 
      *       messages that had been previously delivered.
      *
      *          <P>Redelivered messages do not have to be delivered in 
      *             exactly their original delivery order.
      * </UL>
      *
      * @exception JMSException if JMS implementation fails to stop message
      *                         delivery and restart message send due to 
      *                         due to some internal error.
      *
      */ 

    void
    recover() throws JMSException;


    /** Return the session's distinguished message listener.
      *
      * @return the message listener associted with this session.
      *
      * @exception JMSException if JMS fails to get the message listener
      *                         due to an internal error in JMS Provider.
      *
      * @see javax.jms.Session#setMessageListener
      * @see javax.jms.ServerSessionPool
      * @see javax.jms.ServerSession
      */

    MessageListener
    getMessageListener() throws JMSException;


    /** Set the session's distinguished message listener. When it is set no 
      * other form of message receipt in the session can be used; however, 
      * all forms of sending messages are still supported. This is an expert
      * facility not used by regular JMS clients.
      *
      * @param listener the message listener to associate with this session.
      *
     * @exception JMSException if JMS fails to set the message listener
      *                         due to an internal error in JMS Provider.
      *
      * @see javax.jms.Session#getMessageListener
      * @see javax.jms.ServerSessionPool
      * @see javax.jms.ServerSession
      */

    void
    setMessageListener(MessageListener listener) throws JMSException;
}
