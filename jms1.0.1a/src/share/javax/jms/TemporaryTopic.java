
/*
 * @(#)TemporaryTopic.java	1.5 98/10/07
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

/** A TemporaryTopic is a unique Topic object created for the duration of 
  * a TopicConnection. It is a system defined Topic that can only be 
  * consumed by the TopicConnection that created it.
  *
  * @version     1.0 - 9 March 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  */

public interface TemporaryTopic extends Topic {

    /** Delete this temporary topic. If there are still existing publishers
      * or subscribers still using it, then a JMSException will be thrown.
      *  
      * @exception JMSException if JMS implementation fails to delete a 
      *                         Temporary queue due to some internal error.
      */

    void 
    delete() throws JMSException; 
}
