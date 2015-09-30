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
 * {@code JMSDurableTopicListener} annotation.
 * <p>
 * The parameter must have a type appropriate to the specified property.
 * <p>
 * The method that will be used by the application server or resource adapter to
 * obtain the property value will depend on the parameter type. The table below
 * defines the method that will be used to obtain the value of a parameter
 * annotated with {@code @MessageProperty("foo")}.
 * <p>
 * If the method parameter is not one of the types listed then deployment must
 * fail.
 * <p>
 * 
 * <PRE>
 * +-------------------------------------------------------+
 * | Parameter | Set to                                    |
 * | type      |                                           |
 * +-----------+-------------------------------------------+
 * | boolean   | message.getBooleanProperty("foo")         |
 * | Boolean   | (Boolean)message.getObjectProperty("foo") |
 * | byte      | message.getByteProperty("foo")            |
 * | Byte      | (Byte)message.getObjectProperty("foo")    |
 * | short     | message.getShortProperty("foo")           |
 * | Short     | (Short)message.getObjectProperty("foo")   |
 * | int       | message.getIntProperty("foo")             |
 * | Integer   | (Integer)message.getObjectProperty("foo") |
 * | long      | message.getLongProperty("foo")            |
 * | Long      | (Long)message.getObjectProperty("foo")    |
 * | float     | message.getFloatProperty("foo")           |
 * | Float     | (Float)message.getObjectProperty("foo")   |
 * | double    | message.getFloatProperty("foo")           |
 * | Double    | (Double)message.getObjectProperty("foo")  |
 * | String    | message.getStringProperty("foo")          |
 * |-------------------------------------------------------+
 * </PRE>
 * <p>
 * Note that only {@code getObjectProperty} and {@code getStringObject} can
 * return a null value. This means that if the specified property is not set
 * then the parameter must be an object type ({@code Boolean}, {@code Byte},
 * {@code Short}, {@code Integer}, {@code Long}, {@code Float}, {@code Double}
 * or {@code String}), in which case the parameter will be set to null. *
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
