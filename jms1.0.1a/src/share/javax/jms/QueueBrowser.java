
/*
 * @(#)QueueBrowser.java	1.12 98/09/30
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

import java.util.Enumeration;

/** A client uses a QueueBrowser to look at messages on a queue without 
  * removing them.
  *
  * <P>The browse methods return a java.util.Enumeration that is used to scan 
  * the queue's messages. It may be an enumeration of the entire content of a 
  * queue or it may only contain the messages matching a message selector.
  *
  * <P>Messages may be arriving and expiring while the scan is done. JMS does 
  * not require the content of an enumeration to be a static snapshot of queue 
  * content. Whether these changes are visible or not depends on the JMS 
  * provider.
  *
  * @version     1.0 - 13 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.QueueReceiver
  */

public interface QueueBrowser {

    /** Get the queue associated with this queue browser.
      * 
      * @return the queue
      *  
      * @exception JMSException if JMS fails to get the
      *                         queue associated with this Browser
      *                         due to some JMS error.
      */

    Queue 
    getQueue() throws JMSException;


    /** Get this queue browser's message selector expression.
      *  
      * @return this queue browser's message selector
      *
      * @exception JMSException if JMS fails to get the
      *                         message selector for this browser
      *                         due to some JMS error.
      */

    String
    getMessageSelector() throws JMSException;


    /** Get an enumeration for browsing the current queue messages in the
      * order they would be received.
      *
      * @return an enumeration for browsing the messages
      *  
      * @exception JMSException if JMS fails to get the
      *                         enumeration for this browser
      *                         due to some JMS error.
      */

    Enumeration 
    getEnumeration() throws JMSException;


    /** Since a provider may allocate some resources on behalf of a 
      * QueueBrowser outside the JVM, clients should close them when they 
      * are not needed. Relying on garbage collection to eventually reclaim 
      * these resources may not be timely enough.
      *
      * @exception JMSException if a JMS fails to close this
      *                         Browser due to some JMS error.
      */

    void 
    close() throws JMSException;
}
