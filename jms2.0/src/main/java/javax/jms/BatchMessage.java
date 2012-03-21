/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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

/** An <CODE>BatchMessage</CODE> object is a special type of message which can be 
 * created by the JMS provider and used to deliver an ordered batch of ordinary 
 * messages to a consumer. It inherits from the <CODE>Message</CODE> interface 
 * and adds methods to allow the individual messages to be extracted.
 * 
 * Note that this message type is created by the JMS provider, not by
 * the application, and therefore only provides public methods to
 * extract messages, not to set them. 
 *
 * @see         javax.jms.ObjectMessage
 * @see         javax.jms.BytesMessage
 * @see         javax.jms.MapMessage
 * @see         javax.jms.Message
 * @see         javax.jms.StreamMessage
 * @see         javax.jms.TextMessage
 */

public interface BatchMessage extends Message {
	
	/**
	 * returns the number of messages in this BatchMessage
	 * 
	 * @return the number of messages in this BatchMessage
	 * 
	 * @throws JMSException if the JMS provider fails to get the size due to some internal error
	 */
	int size() throws JMSException;
	
	/**
	 * returns the Message at the specified position in the batch
	 * @param index index of the Messagr to return
	 * 
	 * @return the Message at the specified position in the batch
	 * 
	 * @throws JMSException  if the JMS provider fails to get the Message due to some internal error
	 */
	Message get(int index) throws JMSException;
	
}
