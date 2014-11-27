
/*
 * @(#)XAConnectionFactory.java	1.4 98/05/14
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

/** Some application servers provide support for grouping JTS capable 
  * resource use into a distributed transaction. To include JMS transactions
  * in a JTS transaction, an application server requires a JTS aware JMS 
  * provider. A JMS provider exposes its JTS support using a JMS 
  * XAConnectionFactory which an application server uses to create XASessions.
  *
  * <P>XAConnectionFactory's are JMS administered objects just like 
  * ConnectionFactory's. It is expected that application servers will find 
  * them using JNDI.
  *
  * @version     1.0 - 14 May 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.XAQueueConnectionFactory
  * @see         javax.jms.XATopicConnectionFactory
  */

public interface XAConnectionFactory {
}
