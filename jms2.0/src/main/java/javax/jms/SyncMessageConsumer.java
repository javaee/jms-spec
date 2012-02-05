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
 * <CODE>SyncMessageConsumer</CODE> object to receive messages
 * synchronously from a destination. A <CODE>SyncMessageConsumer</CODE> object
 * is created by calling a <code>createSyncConsumer</code> method on a
 * <code>MessagingContext</code>. A <CODE>SyncMessageConsumer</CODE> cannot be
 * used to receive messages asynchronously.
 * 
 * <P>
 * A <CODE>SyncMessageConsumer</CODE> can be created with a message selector. A
 * message selector allows the client to restrict the messages delivered to the
 * <CODE>SyncMessageConsumer</CODE> to those that match the selector.
 * 
 * <P>
 * A client can request the next message from a <CODE>SyncMessageConsumer</CODE> object
 * using one of its <CODE>receive</CODE> methods. There are several
 * variations of <CODE>receive</CODE> that allow a client to poll or wait for
 * the next message.
 * 
 * @version 2.0
 * 
 * @see javax.jms.MessagingContext
 */

public interface SyncMessageConsumer extends AutoCloseable {

    /** Gets this SyncMessageConsumer's message selector expression.
      *  
      * @return this SyncMessageConsumer's message selector, or null if no
      *         message selector exists for the SyncMessageConsumer (that is, if 
      *         the message selector was not set or was set to null or the 
      *         empty string)
      *  
      * @exception JMSRuntimeException if the JMS provider fails to get the message
      *            selector due to some internal error.
      */ 

    String getMessageSelector();

    /** Receives the next message produced for this SyncMessageConsumer.
      *  
      * <P>This call blocks indefinitely until a message is produced
      * or until this SyncMessageConsumer is closed.
      *
      * <P>If this <CODE>receive</CODE> is done within a transaction, the 
      * SyncMessageConsumer retains the message until the transaction commits.
      *  
      * @return the next message produced for this SyncMessageConsumer, or 
      * null if this SyncMessageConsumer is concurrently closed
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
      * timeout expires, or this SyncMessageConsumer is closed.
      * A <CODE>timeout</CODE> of zero never expires, and the call blocks 
      * indefinitely.
      *
      * @param timeout the timeout value (in milliseconds)
      *
      * @return the next message produced for this SyncMessageConsumer, or 
      * null if the timeout expires or this SyncMessageConsumer is concurrently 
      * closed
      *
      * @exception JMSRuntimeException if the JMS provider fails to receive the next
      *            message due to some internal error.
      */ 

    Message receive(long timeout);


    /** Receives the next message if one is immediately available.
      *
      * @return the next message produced for this SyncMessageConsumer, or 
      * null if one is not available
      *  
      * @exception JMSRuntimeException if the JMS provider fails to receive the next
      *            message due to some internal error.
      */ 

    Message receiveNoWait();


	/**
	 * Closes the SyncMessageConsumer.
	 * 
	 * <P>
	 * Since a provider may allocate some resources on behalf of a
	 * <CODE>SyncMessageConsumer</CODE> outside the Java virtual machine,
	 * clients should close them when they are not needed. Relying on garbage
	 * collection to eventually reclaim these resources may not be timely
	 * enough.
	 * 
	 * <P>
	 * This call blocks until a <CODE>receive</CODE> in progress has completed.
	 * A blocked message consumer <CODE>receive</CODE> call returns null when
	 * this SyncMessageConsumer is closed.
	 * 
	 * @exception JMSRuntimeException
	 *                if the JMS provider fails to close the SyncMessageConsumer
	 *                due to some internal error.
	 */ 

    void close();
    
	/**
	 * Receives the next message produced for this SyncMessageConsumer and
	 * returns its payload, which must be of the specified type
	 * 
	 * <P>
	 * This call blocks indefinitely until a message is produced or until this
	 * SyncMessageConsumer is closed.
	 * 
	 * <P>
	 * If this <CODE>receivePayload</CODE> is done within a transaction, the
	 * SyncMessageConsumer retains the message until the transaction commits.
	 * 
	 * @param c
	 *            The class of the payload of the next message. If the next
	 *            message is a TextMessage when this should be set to
	 *            String.class. If the next message is a ObjectMessage this
	 *            should be set to Serializable.class. If the next message is
	 *            not of the expected type a ClassCastException will be thrown
	 *            and the message will not be delivered.
	 * 
	 * @return the payload of the next message produced for this
	 *         SyncMessageConsumer, or null if this SyncMessageConsumer is
	 *         concurrently closed
	 * 
	 * @throws JMSRuntimeException
	 *             - if the JMS provider fails to receive the next message due
	 *             to some internal error
	 * @throws ClassCastException
	 *             - if the next message is not of the expected type
	 */
    <T> T receivePayload(Class<T>  c);
        
	/**
	 * Receives the next message produced for this SyncMessageConsumer and that
	 * arrives within the specified timeout period, and returns its payload,
	 * which must be of the specified type
	 * 
	 * <P>
	 * This call blocks until a message arrives, the timeout expires, or this
	 * SyncMessageConsumer is closed. A timeout of zero never expires, and the
	 * call blocks indefinitely.
	 * 
	 * <P>
	 * If this <CODE>receivePayload</CODE> is done within a transaction, the
	 * SyncMessageConsumer retains the message until the transaction commits.
	 * 
	 * @param c
	 *            The class of the payload of the next message. If the next
	 *            message is a TextMessage when this should be set to
	 *            String.class. If the next message is a ObjectMessage this
	 *            should be set to Serializable.class. If the next message is
	 *            not of the expected type a ClassCastException will be thrown
	 *            and the message will not be delivered.
	 * 
	 * @return the payload of the next message produced for this
	 *         SyncMessageConsumer, or null if this SyncMessageConsumer is
	 *         concurrently closed
	 * 
	 * @throws JMSRuntimeException
	 *             - if the JMS provider fails to receive the next message due
	 *             to some internal error
	 * @throws ClassCastException
	 *             - if the next message is not of the expected type
	 */
    <T> T receivePayload(Class<T> c, long timeout);
    
	/**
	 * Receives the next message produced for this SyncMessageConsumer and
	 * returns its payload, which must be of the specified type
	 * 
	 * <P>
	 * Returns null if a message is not available.
	 * 
	 * <P>
	 * If this <CODE>receivePayload</CODE> is done within a transaction, the
	 * SyncMessageConsumer retains the message until the transaction commits.
	 * 
	 * @param c
	 *            The class of the payload of the next message. If the next
	 *            message is a TextMessage when this should be set to
	 *            String.class. If the next message is a ObjectMessage this
	 *            should be set to Serializable.class. If the next message is
	 *            not of the expected type a ClassCastException will be thrown
	 *            and the message will not be delivered.
	 * 
	 * @return the payload of the next message produced for this
	 *         SyncMessageConsumer, or null if this SyncMessageConsumer is
	 *         concurrently closed
	 * 
	 * @throws JMSRuntimeException
	 *             - if the JMS provider fails to receive the next message due
	 *             to some internal error
	 * @throws ClassCastException
	 *             - if the next message is not of the expected type
	 */
    <T> T receivePayloadNoWait(Class<T> c);
    
}
