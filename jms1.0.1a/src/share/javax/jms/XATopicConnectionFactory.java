
/*
 * @(#)XATopicConnectionFactory.java	1.8 98/10/07
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

/** An XATopicConnectionFactory provides the same create options as 
  * TopicConnectionFactory.
  *
  * @version     1.0 - 14 May 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.TopicConnectionFactory
  * @see         javax.jms.XAConnectionFactory
  */

public interface XATopicConnectionFactory 
	extends XAConnectionFactory, TopicConnectionFactory {

    /** Create an XA topic connection with default user identity.
      *
      * @return a newly created XA topic connection.
      *
      * @exception JMSException if JMS Provider fails to create XA topic Connection
      *                         due to some internal error.
      * @exception JMSSecurityException  if client authentication fails due to 
      *                         invalid user name or password.
      */ 

    XATopicConnection
    createXATopicConnection() throws JMSException;


    /** Create an XA topic connection with specified user identity.
      *  
      * @param userName the caller's user name
      * @param password the caller's password
      *  
      * @return a newly created XA topic connection.
      *
      * @exception JMSException if JMS Provider fails to create XA topi connection
      *                         due to some internal error.
      * @exception JMSSecurityException  if client authentication fails due to 
      *                         invalid user name or password.
      */ 

    XATopicConnection
    createXATopicConnection(String userName, String password) 
					     throws JMSException;
}
