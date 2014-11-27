
/*
 * @(#)ConnectionFactory.java	1.6 98/08/03
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

/** A ConnectionFactory encapsulates a set of connection configuration 
  * parameters that has been defined by an administrator. A client uses 
  * it to create a Connection with a JMS provider.
  *
  * <P>ConnectionFactory objects support concurrent use.
  *
  * <P>A ConnectionFactory is a JMS administered object.
  *
  * <P>JMS administered objects are objects containing JMS configuration 
  * information that are created by a JMS administrator and later used by 
  * JMS clients. They make it practical to administer JMS in the enterprise.
  *
  * <P>Although the interfaces for administered objects do not explicitly 
  * depend on JNDI, JMS establishes the convention that JMS clients find 
  * them by looking them up in a JNDI namespace.
  *
  * <P>An administrator can place an administered object anywhere in a 
  * namespace. JMS does not define a naming policy.
  *
  * <P>It is expected that JMS providers will provide the tools an
  * administrator needs to create and configure administered objects in a 
  * JNDI namespace. JMS provider implementations of administered objects 
  * should be both <CODE>javax.jndi.Referenceable</CODE> and 
  * <CODE>java.rmi.Serializable</CODE> so that they can be stored in all 
  * JNDI naming contexts. In addition, it is recommended that these 
  * implementations follow the JavaBeans(TM) design patterns.
  *
  * <P>This strategy provides several benefits:
  *
  * <UL>
  *   <LI>It hides provider-specific details from JMS clients.
  *   <LI>It abstracts JMS administrative information into Java objects 
  *       that are easily organized and administrated from a common 
  *       management console.
  *   <LI>Since there will be JNDI providers for all popular naming 
  *       services, this means JMS providers can deliver one implementation 
  *       of administered objects that will run everywhere.
  * </UL>
  *
  * <P>An administered object should not hold on to any remote resources. 
  * Its lookup should not use remote resources other than those used by 
  * JNDI itself.
  *
  * <P>Clients should think of administered objects as local Java objects. 
  * Looking them up should not have any hidden side affects or use surprising 
  * amounts of local resources.
  *
  * @version     1.0 - 3 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.QueueConnectionFactory
  * @see         javax.jms.TopicConnectionFactory
  */

public interface ConnectionFactory {
}
