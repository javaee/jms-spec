
/*
 * @(#)XATopicSession.java	1.7 98/05/14
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

/** An XATopicSession provides a regular TopicSession which can be used to
  * create TopicSubscribers and TopicPublishers.
  *
  * @version     1.0 - 14 May 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.XASession
  * @see         javax.jms.TopicSession
  */

public interface XATopicSession extends XASession {

    /** Get the topic session associated with this XATopicSession.
      *   
      * @return the topic session object. 
      *   
      * @exception JMSException if a JMS error occurs.
      */  
  
    TopicSession 
    getTopicSession() throws JMSException;
}
