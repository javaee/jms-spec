/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015 Oracle and/or its affiliates. All rights reserved.
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation may be applied to parameters on a callback method on a JMS
 * message-driven bean that has been annotated with the {@code JMSQueueListener}
 * , {@code JMSNonDurableTopicListener} or {@code JMSDurableTopicListener}
 * annotation. It specifies that the parameter must be set to the specified
 * message header value.
 * <p>
 * The parameter type must match the header type as shown in the following
 * table. If it does not then callback method will not be invoked and the
 * message will not be delivered.
 * 
 * <pre>
 * +-------------------+------------------------------------------------+
 * | Parameter         | Annotation                                     |
 * | type              |                                                |
 * +-------------------+------------------------------------------------+
 * | String            | @MessageHeader(Header.JMSCorrelationID)        |     
 * | byte[]            | @MessageHeader(Header.JMSCorrelationIDAsBytes) |
 * | Integer or int    | @MessageHeader(Header.JMSDeliveryMode)         |
 * | Long or long      | @MessageHeader(Header.JMSDeliveryTime)         |
 * | Destination       | @MessageHeader(Header.JMSDestination)          |
 * | Long or long      | @MessageHeader(Header.JMSExpiration)           |
 * | String            | @MessageHeader(Header.JMSMessageID)            |
 * | Integer or int    | @MessageHeader(Header.JMSPriority)             |
 * | Boolean or boolean| @MessageHeader(Header.JMSRedelivered)          |
 * | Destination       | @MessageHeader(Header.JMSReplyTo)              |
 * | Long or long      | @MessageHeader(Header.JMSTimestamp)            |
 * | String            | @MessageHeader(Header.JMSType)                 |
 * +-------------------+------------------------------------------------+
 * </pre>
 * 
 * @version JMS 2.1
 * @since JMS 2.1
 * 
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageHeader {

    /**
     * Specifies the header field to be used
     */
    Header value();

    public enum Header {
        JMSCorrelationID, JMSCorrelationIDAsBytes, JMSDeliveryMode, JMSDeliveryTime, JMSDestination, JMSExpiration, JMSMessageID, JMSPriority, JMSRedelivered, JMSReplyTo, JMSTimestamp, JMSType
    }

}
