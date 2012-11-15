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
 
/**
 * A client using the simplified JMS API introduced for JMS 2.0 uses a
 * {@code JMSConsumer} object to receive messages from a queue or topic. A
 * {@code JMSConsumer} object may be created either created by passing a
 * {@code Queue} or {@code Topic} object to one of the {@code createConsumer}
 * methods on a {@code JMSContext}. or by passing a {@code Topic} object to one
 * of the {@code createSharedConsumer} or {@code createDurableConsumer} methods
 * on a {@code JMSContext}.
 * <P>
 * A {@code JMSConsumer} can be created with a message selector. A message
 * selector allows the client to restrict the messages delivered to the
 * {@code JMSConsumer} to those that match the selector.
 * <p>
 * A client may either synchronously receive a {@code JMSConsumer}'s messages or
 * have the {@code JMSConsumer} asynchronously deliver them as they arrive.
 * <p>
 * For synchronous receipt, a client can request the next message from a
 * {@code JMSConsumer} using one of its {@code receive} methods. There are
 * several variations of {@code receive} that allow a client to poll or wait for
 * the next message.
 * <p>
 * For asynchronous delivery, a client can register a {@code MessageListener}
 * object with a {@code JMSConsumer}. As messages arrive at the
 * {@code JMSConsumer}, it delivers them by calling the {@code MessageListener}
 * 's {@code onMessage} method.
 * <p>
 * It is a client programming error for a {@code MessageListener} to throw an
 * exception.
 * 
 * @version 2.0
 * 
 * @see javax.jms.JMSContext
 */

public interface JMSConsumer extends AutoCloseable {

    /** Gets this {@code JMSConsumer}'s message selector expression.
      *  
      * @return this {@code JMSConsumer}'s message selector, or null if no
      *         message selector exists for the {@code JMSConsumer} (that is, if 
      *         the message selector was not set or was set to null or the 
      *         empty string)
      *  
      * @exception JMSRuntimeException if the JMS provider fails to get the message
      *            selector due to some internal error.
      */ 

    String getMessageSelector();
    
    /** Gets the {@code JMSConsumer}'s {@code MessageListener}. 
     * <p>
     * This method must not be used in a Java EE web or EJB application. 
     * Doing so may cause a {@code JMSRuntimeException} to be thrown though this is not guaranteed.
     * 
      * @return the {@code JMSConsumer}'s {@code MessageListener}, or null if one was not set
      *  
      * @exception JMSRuntimeException if the JMS provider fails to get the {@code MessageListener}
      *                         for one of the following reasons:
      *                         <ul>
      *                         <li>an internal error has occurred or
      *                         <li>this method has been called in a Java EE web or EJB application 
      *                         (though it is not guaranteed that an exception is thrown in this case)
      *                         </ul>                      
      *                         
      * @see javax.jms.JMSConsumer#setMessageListener(javax.jms.MessageListener)
      */ 
    MessageListener getMessageListener() throws JMSRuntimeException;
    
    /** Sets the {@code JMSConsumer}'s {@code MessageListener}.
      * <p>
      * Setting the {@code MessageListener} to null is the equivalent of 
      * unsetting the {@code MessageListener} for the {@code JMSConsumer}. 
      * <p>
      * The effect of calling this method
      * while messages are being consumed by an existing listener
      * or the {@code JMSConsumer} is being used to consume messages synchronously
      * is undefined.
      * <p>
      * This method must not be used in a Java EE web or EJB application. 
      * Doing so may cause a {@code JMSRuntimeException} to be thrown though this is not guaranteed.
      * 
      * @param listener the listener to which the messages are to be 
      *                 delivered
      *  
      * @exception JMSRuntimeException if the JMS provider fails to set the {@code JMSConsumer}'s {@code MessageListener}
      *                         for one of the following reasons:
      *                         <ul>
      *                         <li>an internal error has occurred or  
      *                         <li>this method has been called in a Java EE web or EJB application 
      *                         (though it is not guaranteed that an exception is thrown in this case)
      *                         </ul>    
      *                         
      * @see javax.jms.JMSConsumer#getMessageListener()
      */ 
    void setMessageListener(MessageListener listener) throws JMSRuntimeException;
    

    /** Receives the next message produced for this {@code JMSConsumer}.
      *  
      * <P>This call blocks indefinitely until a message is produced
      * or until this {@code JMSConsumer} is closed.
      *
      * <P>If this {@code receive} is done within a transaction, the 
      * JMSConsumer retains the message until the transaction commits.
      *  
      * @return the next message produced for this {@code JMSConsumer}, or 
      * null if this {@code JMSConsumer} is concurrently closed
      *  
      * @exception JMSRuntimeException if the JMS provider fails to receive the next
      *            message due to some internal error.
      * 
      */ 
 
    Message receive();


    /** Receives the next message that arrives within the specified
      * timeout interval.
      *  
      * <P>This call blocks until a message arrives, the
      * timeout expires, or this {@code JMSConsumer} is closed.
      * A {@code timeout} of zero never expires, and the call blocks 
      * indefinitely.
      *
      * @param timeout the timeout value (in milliseconds)
      *
      * @return the next message produced for this {@code JMSConsumer}, or 
      * null if the timeout expires or this {@code JMSConsumer} is concurrently 
      * closed
      *
      * @exception JMSRuntimeException if the JMS provider fails to receive the next
      *            message due to some internal error.
      */ 

    Message receive(long timeout);


    /** Receives the next message if one is immediately available.
      *
      * @return the next message produced for this {@code JMSConsumer}, or 
      * null if one is not available
      *  
      * @exception JMSRuntimeException if the JMS provider fails to receive the next
      *            message due to some internal error.
      */ 

    Message receiveNoWait();


	/**
	 * Closes the {@code JMSConsumer}.
	 * 
	 * <P>
	 * Since a provider may allocate some resources on behalf of a
	 * {@code JMSConsumer} outside the Java virtual machine,
	 * clients should close them when they are not needed. Relying on garbage
	 * collection to eventually reclaim these resources may not be timely
	 * enough.
	 * 
	 * <P>
	 * This call blocks until a {@code receive} in progress has completed.
	 * A blocked message consumer {@code receive} call returns null when
	 * this {@code JMSConsumer} is closed.
	 * 
	 * @exception JMSRuntimeException
	 *                if the JMS provider fails to close the {@code JMSConsumer}
	 *                due to some internal error.
	 */ 

    void close();
    
	/**
	 * Receives the next message produced for this {@code JMSConsumer} and
	 * returns its body as an object of the specified type. 
	 * The message body must be capable of being assigned to the specified type. 
	 * This means that the specified class or interface must either be the same as, 
	 * or a superclass or superinterface of, the class of the message body.
	 * This method may be used for messages of type
	 * {@code TextMessage}, {@code BytesMessage}, {@code MapMessage} and {@code ObjectMessage}.
	 * If the message is not one of these types, or its body cannot be assigned to the specified type,
	 * or it has no body, then a {@code MessageFormatRuntimeException} is thrown and the message
	 * will not be delivered.
	 * <P>
	 * This call blocks indefinitely until a message is produced or until this
	 * {@code JMSConsumer} is closed.
	 * <p>
	 * <P>
	 * If {@code receiveBody} is called within a transaction, the
	 * {@code JMSConsumer} retains the message until the transaction commits.
	 * 
	 * @param c
	 *            The type to which the body of the next message should be assigned.<br/> 
	 *            If the next message is expected to be a {@code TextMessage} then 
	 *            this should be set to {@code String.class} or another class to which a {@code String} is assignable.<br/>
	 *            If the next message is expected to be a {@code ObjectMessage} then 
	 *            this should be set to {@code java.io.Serializable.class} or another class to which the body is assignable. <br/>
	 *            If the next message is expected to be a {@code MapMessage} then this
	 *            should be set to {@code java.util.Map.class}.<br/>
	 *            If the next message is expected to be a {@code BytesMessage} then this
	 *            should be set to {@code byte[].class}.<br/>
	 * 
	 * @return the body of the next message produced for this
	 *         {@code JMSConsumer}, or null if this {@code JMSConsumer} is
	 *         concurrently closed
	 * 
	 * @throws JMSRuntimeException
	 *             if the JMS provider fails to receive the next message due
	 *             to some internal error
	 * @throws MessageFormatRuntimeException 
	 *             if the message is not one of the supported types listed above, 
	 *             or the message body cannot be assigned to the specified type,
	 *             or the message has no body,
	 *             or the message is an {@code ObjectMessage} and object deserialization fails.
	 */
    <T> T receiveBody(Class<T>  c);
        
	/**
	 * Receives the next message produced for this {@code JMSConsumer} that
	 * arrives within the specified timeout period and returns its body
	 * as an object of the specified type.
	 * The message body must be capable of being assigned to the specified type. 
	 * This means that the specified class or interface must either be the same as, 
	 * or a superclass or superinterface of, the class of the message body.
	 * This method may be used for messages of type
	 * {@code TextMessage}, {@code BytesMessage}, {@code MapMessage} and {@code ObjectMessage}.
	 * If the message is not one of these types, or its body cannot be assigned to the specified type,
	 * or it has no body, then a {@code MessageFormatRuntimeException} is thrown and the message
	 * will not be delivered.
     * <p>
	 * This call blocks until a message arrives, the timeout expires, or this
	 * {@code JMSConsumer} is closed. A timeout of zero never expires, and the
	 * call blocks indefinitely.
	 * <p>
	 * If {@code receiveBody} is called within a transaction, the
	 * {@code JMSConsumer} retains the message until the transaction commits.
	 * 
	 * @param c
	 *            The type to which the body of the next message should be assigned.<br/> 
	 *            If the next message is expected to be a {@code TextMessage} then 
	 *            this should be set to {@code String.class} or another class to which a {@code String} is assignable.<br/>
	 *            If the next message is expected to be a {@code ObjectMessage} then 
	 *            this should be set to {@code java.io.Serializable.class} or another class to which the body is assignable. <br/>
	 *            If the next message is expected to be a {@code MapMessage} then this
	 *            should be set to {@code java.util.Map.class}.<br/>
	 *            If the next message is expected to be a {@code BytesMessage} then this
	 *            should be set to {@code byte[].class}.<br/>
	 * 
	 * @return the body of the next message produced for this {@code JMSConsumer},
	 *         or null if the timeout expires or this {@code JMSConsumer} is concurrently closed
	 * 
	 * @throws JMSRuntimeException
	 *             if the JMS provider fails to receive the next message due
	 *             to some internal error
	 * @throws MessageFormatRuntimeException 
	 *             if the message is not one of the supported types listed above, 
	 *             or the message body cannot be assigned to the specified type,
	 *             or the message has no body,
	 *             or the message is an {@code ObjectMessage} and object deserialization fails.
	 */
    <T> T receiveBody(Class<T> c, long timeout);
    
	/**
	 * Receives the next message produced for this {@code JMSConsumer} if one is immediately available 
	 * and returns its body as an object of the specified type. 
	 * The message body must be capable of being assigned to the specified type. 
	 * This means that the specified class or interface must either be the same as, 
	 * or a superclass or superinterface of, the class of the message body.
	 * This method may be used for messages of type
	 * {@code TextMessage}, {@code BytesMessage}, {@code MapMessage} and {@code ObjectMessage}.
	 * If the message is not one of these types, or its body cannot be assigned to the specified type,
	 * or it has no body, then a {@code MessageFormatRuntimeException} is thrown and the message
	 * will not be delivered.
	 * <p>
	 * If a message is not immediately available null is returned. 
	 * <P>
	 * If {@code receiveBodyNoWait} is called within a transaction, the
	 * {@code JMSConsumer} retains the message until the transaction commits.
	 * 
	 * @param c
	 *            The type to which the body of the next message should be assigned.<br/> 
	 *            If the next message is expected to be a {@code TextMessage} then 
	 *            this should be set to {@code String.class} or another class to which a {@code String} is assignable.<br/>
	 *            If the next message is expected to be a {@code ObjectMessage} then 
	 *            this should be set to {@code java.io.Serializable.class} or another class to which the body is assignable. <br/>
	 *            If the next message is expected to be a {@code MapMessage} then this
	 *            should be set to {@code java.util.Map.class}.<br/>
	 *            If the next message is expected to be a {@code BytesMessage} then this
	 *            should be set to {@code byte[].class}.<br/>
	 * 
	 * @return the body of the next message produced for this {@code JMSConsumer},
	 *         or null if one is not immediately available or this {@code JMSConsumer} is concurrently closed
	 * 
	 * @throws JMSRuntimeException
	 *             if the JMS provider fails to receive the next message due
	 *             to some internal error
	 * @throws MessageFormatRuntimeException 
	 *             if the message is not one of the supported types listed above, 
	 *             or the message body cannot be assigned to the specified type,
	 *             or the message has no body,
	 *             or the message is an {@code ObjectMessage} and object deserialization fails.
	 */
    <T> T receiveBodyNoWait(Class<T> c);
    
}
