
/* 
 * @(#)Queue.java	1.9 98/09/30
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
 *
 */

package javax.jms;


/** A Queue object encapsulates a provider-specific queue name. It is the 
  * way a client specifies the identity of queue to JMS methods.
  *
  * <P>The actual length of time messages are held by a queue and the 
  * consequences of resource overflow are not defined by JMS.
  *
  * @version     1.0 - 9 March 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  */
 
public interface Queue extends Destination { 

    /** Get the name of this queue.
      *  
      * <P>Clients that depend upon the name, are not portable.
      *  
      * @return the queue name
      *  
      * @exception JMSException if JMS implementation for Queue fails to
      *                         to return queue name due to some internal
      *                         error.
      */ 
 
    String
    getQueueName() throws JMSException;  


    /** Return a pretty printed version of the queue name
      *
      * @return the provider specific identity values for this queue.
      */
 
    String
    toString();
}
