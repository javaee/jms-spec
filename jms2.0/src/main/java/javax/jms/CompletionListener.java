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
 * A <code>CompletionListener</code> is used in conjunction with methods used to send messages
 * which return immediately, perform the actual send in a separate thread,
 * and notify the specified completion listener when the operation has completed.
 * <p>
 * This allows the JMS provider to perform the actual sending of the message,
 * and the wait for any confirmation from a JMS server, to take place in a separate thread
 * without blocking the calling thread. When the sending of the message is complete,
 * and any confirmation has been received from a JMS server, the JMS provider calls
 * the <code>onCompletion(Message)</code> method of the specified completion listener. 
 * If an exception occurs in the separate thread 
 * then the JMS provider calls the <code>onException(Exception)</code> 
 * method of the specified completion listener.
 * 
 * @version     2.0
 * @since       2.0
 *
 * @see javax.jms.MessageProducer#send(javax.jms.Message,javax.jms.CompletionListener)
 * @see javax.jms.MessageProducer#send(javax.jms.Message,int,int,long,javax.jms.CompletionListener)
 * @see javax.jms.MessageProducer#send(javax.jms.Destination,javax.jms.Message,javax.jms.CompletionListener)
 * @see javax.jms.MessageProducer#send(javax.jms.Destination,javax.jms.Message,int,int,long,javax.jms.CompletionListener)
 * @see javax.jms.JMSContext#send(javax.jms.Destination,javax.jms.Message,javax.jms.CompletionListener)
 * @see javax.jms.JMSContext#send(javax.jms.Destination,javax.jms.Message,int,int,long,javax.jms.CompletionListener)
 * 
 * 
 */
public interface CompletionListener {

	/**
	 * Notifies the application that the message has been successfully sent
	 * 
	 * @param message the message that was sent.
	 */
	void onCompletion(Message message);

	/**
	 * Notifies user that the specified exception was thrown while attempting to send the message
	 * 
	 * @param exception the exception
	 */
	void onException(Exception exception);
}
