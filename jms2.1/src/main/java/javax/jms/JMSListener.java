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
 * This annotation is used to designate the callback method on a JMS
 * message-driven bean, the destination from which messages will be received,
 * and whether that destination is a queue or topic.
 * <p>
 * The destination may be specified using either the lookup or name attribute.
 * If both are specified then lookup is used. If neither is specified then an
 * error will occur.
 * <p>
 * The type attribute must always be specified
 * <p>
 * This annotation may only be used if JMS message-driven bean implements the
 * JMSMessageDrivenBean marker interface. If the JMS message-driven bean
 * implements the MessageListener interface then this annotation will be
 * ignored.
 * <p>
 * @version JMS 2.1
 * @since JMS 2.1
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface JMSListener {

  /**
   * Name of the queue or topic from which messages will be delivered.
   */
  String name() default "";

  /**
   * Lookup name of the queue or topic from which messages will be delivered
   */
  String lookup() default "";

  /**
   * Destination type. Must be either JMSListener.Type.Queue or
   * JMSListener.Type.Topic
   */
  Type type();

  public enum Type {
    QUEUE, TOPIC
  }

}
