/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

/** A client uses a <CODE>MessageConsumer</CODE> object to receive messages 
  * from a destination.  A <CODE>MessageConsumer</CODE> object is created by 
  * passing a <CODE>Destination</CODE> object to a message-consumer creation
  * method supplied by a session.
  *
  * <P><CODE>MessageConsumer</CODE> is the parent interface for all message 
  * consumers.
  *
  * <P>A message consumer can be created with a message selector. A message
  * selector allows 
  * the client to restrict the messages delivered to the message consumer to 
  * those that match the selector.
  *
  * <P>A client may either synchronously receive a message consumer's messages 
  * or have the consumer asynchronously deliver them as they arrive.
  *
  * <P>For synchronous receipt, a client can request the next message from a 
  * message consumer using one of its <CODE>receive</CODE> methods. There are 
  * several variations of <CODE>receive</CODE> that allow a 
  * client to poll or wait for the next message.
  * 
  * <P>For asynchronous delivery, a client can register a single listener object
  * with a message consumer which may be either a <CODE>MessageListener</CODE> 
  * or a <code>BatchMessageListener</code>object. 
  * <ul>
  * <li>
  * <p>A <CODE>MessageListener</CODE> is used to receive messages individually.
  * As messages arrive at the message consumer, it delivers them by calling the 
  * <CODE>MessageListener</CODE>'s <CODE>onMessage</CODE> method.
  * </li>
  * <li>
  * <p>A <CODE>BatchMessageListener</CODE> is used to receive messages in batches.
  * Messages which arrive at the message consumer will be delivered in batches 
  * by calling the <CODE>BatchMessageListener</CODE>'s <CODE>onMessages</CODE> method.
  * </li>
  * </ul>
  * <P>It is a client programming error for a <CODE>MessageListener</CODE> 
  * or <code>BatchMessageListener</code> to throw an exception.
  *
  * @version     2.0
  *
  * @see         javax.jms.QueueReceiver
  * @see         javax.jms.TopicSubscriber
  * @see         javax.jms.Session
  * @see         javax.jms.MessageListener
  * @see         javax.jms.BatchMessageListener
  */

public interface MessageConsumer extends AutoCloseable{

    /** Gets this message consumer's message selector expression.
     *  
      * @return this message consumer's message selector, or null if no
      *         message selector exists for the message consumer (that is, if 
      *         the message selector was not set or was set to null or the 
      *         empty string)
      *  
      * @exception JMSException if the JMS provider fails to get the message
      *                         selector due to some internal error.
      */ 

    String
    getMessageSelector() throws JMSException;


    /** Gets the message consumer's <code>MessageListener</code>. 
     * <p>
     * A message consumer's listener is configured by calling either
     * <code>setMessageListener</code> or <code>setBatchMessageListener</code>.
     * This method will only return a listener that has been set using <code>setMessageListener</code>.
     * To return a listener that has been set using <code>setBatchMessageListener</code>
     * then use <code>getBatchMessageListener()</code> instead.
     * <p>
     * This method must not be used in a Java EE web or EJB application. 
     * Doing so may cause a <code>JMSException</code> to be thrown though this is not guaranteed.
     * 
      * @return the message consumer's <code>MessageListener</code>, or null if one was not set
      *  
      * @exception JMSException if the JMS provider fails to get the <code>MessageListener</code>
      *                         for one of the following reasons:
      *                         <ul>
      *                         <li>an internal error has occurred or
      *                         <li>this method has been called in a Java EE web or EJB application 
      *                         (though it is not guaranteed that an exception is thrown in this case)
      *                         </ul>                      
      *                         
      * @see javax.jms.MessageConsumer#getBatchMessageListener()
      * @see javax.jms.MessageConsumer#setMessageListener(javax.jms.MessageListener)
      * @see javax.jms.MessageConsumer#setBatchMessageListener(javax.jms.BatchMessageListener,int,long)
      */ 
    MessageListener getMessageListener() throws JMSException;
    
    /** Gets the message consumer's <code>BatchMessageListener</code>. 
     * <p>
     * A message consumer's listener is configured by calling either
     * <code>setMessageListener</code> or <code>setBatchMessageListener</code>.
     * This method will only return a listener that has been set using <code>setBatchMessageListener</code>.
     * To return a listener that has been set using <code>setMessageListener</code>
     * then use <code>getMessageListener()</code> instead.
     * <p>
     * This method must not be used in a Java EE web or EJB application. 
     * Doing so may cause a <code>JMSException</code> to be thrown though this is not guaranteed.
     * 
      * @return the message consumer's <code>BatchMessageListener</code>, or null if one was not set
      *  
      * @exception JMSException if the JMS provider fails to get the <code>BatchMessageListener</code>
      *                         for one of the following reasons:
      *                         <ul>
      *                         <li>an internal error has occurred or  
      *                         <li>this method has been called in a Java EE web or EJB application 
      *                         (though it is not guaranteed that an exception is thrown in this case)
      *                         </ul>    
      *                         
      * @see javax.jms.MessageConsumer#getMessageListener()
      * @see javax.jms.MessageConsumer#setMessageListener(javax.jms.MessageListener)
      * @see javax.jms.MessageConsumer#setBatchMessageListener(javax.jms.BatchMessageListener,int,long)
      */ 
    BatchMessageListener getBatchMessageListener() throws JMSException;


    /** Sets the message consumer's listener to be the specified <CODE>MessageListener</CODE>.
      * <p>
      * A message consumer may only have one listener at a time. 
      * So if a listener has already been configured 
      * (either using this method or <code>setBatchMessageListener</code>)
      * the new new listener replaces the old one, 
      * <p>
      * Setting a value of null will unset any existing listener.
      * <p>
      * The effect of calling this method
      * while messages are being consumed by an existing listener
      * or the consumer is being used to consume messages synchronously
      * is undefined.
     * <p>
     * This method must not be used in a Java EE web or EJB application. 
     * Doing so may cause a <code>JMSException</code> to be thrown though this is not guaranteed.
     * 
      * @param listener the listener to which the messages are to be 
      *                 delivered
      *  
      * @exception JMSException if the JMS provider fails to set the message consumer's listener
      *                         for one of the following reasons:
      *                         <ul>
      *                         <li>an internal error has occurred or  
      *                         <li>this method has been called in a Java EE web or EJB application 
      *                         (though it is not guaranteed that an exception is thrown in this case)
      *                         </ul>    
      *                         
      * @see javax.jms.MessageConsumer#setBatchMessageListener(javax.jms.BatchMessageListener,int,long)
      * @see javax.jms.MessageConsumer#getMessageListener()
      */ 
    void setMessageListener(MessageListener listener) throws JMSException;
    
    /** Sets the message consumer's listener to be the specified <CODE>BatchMessageListener</CODE>
     * with the specified maximum batch size and batch timeout.
     * <p>
     * Messages will be delivered to the specified <CODE>BatchMessageListener</CODE> in batches
     * whose size is no greater than the specified maximum batch size.
     * The JMS provider may defer message delivery until the specified batch timeout has expired 
     * in order to assemble a batch of messages that is as large as possible 
     * but no greater than the batch size. 
     * <p>
     * A message consumer may only have one listener at a time. 
     * So if a listener has already been configured 
     * (either using this method or <code>setMessageListener</code>)
     * the new new listener replaces the old one, 
     * <p>
     * Setting a value of null will unset any existing listener.
     * <p>
     * The effect of calling this method
     * while messages are being consumed by an existing listener
     * or the consumer is being used to consume messages synchronously
     * is undefined.
     * <p>
     * This method must not be used in a Java EE web or EJB application. 
     * Doing so may cause a <code>JMSException</code> to be thrown though this is not guaranteed. 
     * @param listener the listener to which the messages are to be 
     *                 delivered
     * @param maxBatchSize the maximum batch size that should be used. Must be greater than zero.
     * @param batchTimeout the batch timeout in milliseconds. A value of zero means no timeout is required
     * The JMS provider may override the specified timeout with a shorter value.
     *  
      * @exception JMSException if the JMS provider fails to set the message consumer's listener
      *                         for one of the following reasons:
      *                         <ul>
      *                         <li>an internal error has occurred or
      *                         <li>this method has been called in a Java EE web or EJB application 
      *                         (though it is not guaranteed that an exception is thrown in this case)
      *                         </ul>    
     *                         
     * @see javax.jms.MessageConsumer#setMessageListener(javax.jms.MessageListener)
     * @see javax.jms.MessageConsumer#getBatchMessageListener()
     */
    void setBatchMessageListener(BatchMessageListener listener, int maxBatchSize, long batchTimeout) throws JMSException;
       
    /** Receives the next message produced for this message consumer.
      *  
      * <P>This call blocks indefinitely until a message is produced
      * or until this message consumer is closed.
      *
      * <P>If this <CODE>receive</CODE> is done within a transaction, the 
      * consumer retains the message until the transaction commits.
      *  
      * @return the next message produced for this message consumer, or 
      * null if this message consumer is concurrently closed
      *  
      * @exception JMSException if the JMS provider fails to receive the next
      *                         message due to some internal error.
      * 
      */ 
 
    Message
    receive() throws JMSException;


    /** Receives the next message that arrives within the specified
      * timeout interval.
      *  
      * <P>This call blocks until a message arrives, the
      * timeout expires, or this message consumer is closed.
      * A <CODE>timeout</CODE> of zero never expires, and the call blocks 
      * indefinitely.
      *
      * @param timeout the timeout value (in milliseconds)
      *
      * @return the next message produced for this message consumer, or 
      * null if the timeout expires or this message consumer is concurrently 
      * closed
      *
      * @exception JMSException if the JMS provider fails to receive the next
      *                         message due to some internal error.
      */ 

    Message
    receive(long timeout) throws JMSException;


    /** Receives the next message if one is immediately available.
      *
      * @return the next message produced for this message consumer, or 
      * null if one is not available
      *  
      * @exception JMSException if the JMS provider fails to receive the next
      *                         message due to some internal error.
      */ 

    Message
    receiveNoWait() throws JMSException;


    /** Closes the message consumer.
      *
      * <P>Since a provider may allocate some resources on behalf of a
      * <CODE>MessageConsumer</CODE> outside the Java virtual machine, clients 
      * should close them when they
      * are not needed. Relying on garbage collection to eventually reclaim
      * these resources may not be timely enough.
      *
      * <P>This call blocks until a <CODE>receive</CODE> or message listener in 
      * progress has completed. A blocked message consumer <CODE>receive</CODE> 
      * call 
      * returns null when this message consumer is closed.
      *  
      * @exception JMSException if the JMS provider fails to close the consumer
      *                         due to some internal error.
      */ 

    void
    close() throws JMSException;
}
