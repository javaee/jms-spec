
/*
 * @(#)QueueReceiver.java	1.13 98/09/30
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


/** A client uses a QueueReceiver for receiving messages that have been 
  * delivered to a queue.
  *
  * <P>Although is possible to have two Sessions with a QueueReceiver for 
  * the same queue, JMS does not define how messages are distributed between 
  * the QueueReceivers.
  *
  * @version     1.0 - 9 March 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.MessageConsumer
  */

public interface QueueReceiver extends MessageConsumer {

    /** Get the queue associated with this queue receiver.
      *  
      * @return the queue 
      *  
      * @exception JMSException if JMS fails to get queue for
      *                         this queue receiver
      *                         due to some internal error.
      */ 
 
    Queue
    getQueue() throws JMSException;
}
