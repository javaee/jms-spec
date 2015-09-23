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
 * Specifies that a callback method parameter must be set to the specified
 * message property value. This annotation may be applied to parameters on a
 * callback method on a JMS message-driven bean that has been annotated with the
 * {@code JMSQueueListener} , {@code JMSNonDurableTopicListener} or
 * {@code JMSDurableTopicListener} annotation. It specifies that the parameter
 * must be set to the specified message header value.
 * <p>
 * The method used to obtain the property value from the message will depend on
 * the type of the method parameter to which it is applied, as shown in the
 * following table.
 * 
 * <PRE>
 * +--------------------+------------+------------+-----------------------------------+
 * | Parameter          | Annotation              | Set to                            |
 * | type               |                         |                                   |
 * +--------------------+-------------------------+-----------------------------------+
 * | Boolean or boolean | @MessageProperty("foo") | message.getBooleanProperty("foo") |
 * | byte               | @MessageProperty("foo") | message.getByteProperty("foo")    |
 * | Short or short     | @MessageProperty("foo") | message.getShortProperty("foo")   |
 * | Integer or int     | @MessageProperty("foo") | message.getIntProperty("foo")     |
 * | Long or long       | @MessageProperty("foo") | message.getLongProperty("foo")    |
 * | Float or float     | @MessageProperty("foo") | message.getFloatProperty("foo")   |
 * | Double or double   | @MessageProperty("foo") | message.getFloatProperty("foo")   |
 * | String             | @MessageProperty("foo") | message.getStringProperty("foo")  |
 * |----------------------------------------------------------------------------------+
 * </PRE>
 * 
 * If the specified property has not been set, or the method parameter is not
 * one of the types listed above, or the message property cannot be converted to
 * the specified type using the conversion rules defined for these methods, then
 * callback method will not be invoked and the message will not be delivered.
 * 
 * @see MessageHeader
 * 
 * @version JMS 2.1
 * @since JMS 2.1
 * 
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageProperty {

    /**
     * Specifies the name of the message property to be used
     */
    String value();

}
