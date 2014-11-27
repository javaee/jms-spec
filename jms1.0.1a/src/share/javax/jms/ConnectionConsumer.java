
/*
 * @(#)ConnectionConsumer.java	1.9 98/10/07
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

/** For application servers, Connections provide a special facility for 
  * creating a ConnectionConsumer. The messages it is to consume are 
  * specified by a Destination and a Property Selector. In addition, a 
  * ConnectionConsumer must be given a ServerSessionPool to use for 
  * processing its messages.
  *
  * <P>Normally, when traffic is light, a ConnectionConsumer gets a 
  * ServerSession from its pool; loads it with a single message; and, 
  * starts it. As traffic picks up, messages can back up. If this happens, 
  * a ConnectionConsumer can load each ServerSession with more than one 
  * message. This reduces the thread context switches and minimizes resource 
  * use at the expense of some serialization of a message processing.
  *
  * @version     1.0 - 3 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see javax.jms.QueueConnection#createConnectionConsumer
  * @see javax.jms.TopicConnection#createConnectionConsumer
  * @see javax.jms.TopicConnection#createDurableConnectionConsumer
  */

public interface ConnectionConsumer {

    /** Get the server session pool associated with this connection consumer.
      *  
      * @return the server session pool used by this connection consumer.
      *  
      * @exception JMSException if a JMS fails to get server sesion
      *                         pool associated with this consumer due
      *                         to some internal error.
      */

    ServerSessionPool 
    getServerSessionPool() throws JMSException; 

 
    /** Since a provider may allocate some resources on behalf of a 
      * ConnectionConsumer outside the JVM, clients should close them when
      * they are not needed. Relying on garbage collection to eventually 
      * reclaim these resources may not be timely enough.
      *  
      * @exception JMSException if a JMS fails to release resources on
      *                         behalf of ConnectionConsumer or it fails
      *                         to close the connection consumer.
      */

    void 
    close() throws JMSException; 
}
