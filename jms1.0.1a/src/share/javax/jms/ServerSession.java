
/*
 * @(#)ServerSession.java	1.5 98/10/07
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

/** A ServerSession is an object implemented by an application server. It 
  * is used by a server to associate a thread with a JMS session.
  *
  * <P>A ServerSession implements two methods:
  *
  * <UL>
  *   <LI><CODE>getSession</CODE> - returns the ServerSession's JMS session.
  *   <LI><CODE>start</CODE> - starts the execution of the ServerSession 
  *       thread and results in the execution of the JMS Session's 
  *       <CODE>run</CODE> method.
  * </UL>
  *
  * <P>A ConnectionConsumer implemented by a JMS provider uses a ServerSession
  * to process one or more messages that have arrived. It does this by getting
  * a ServerSession from the ConnectionConsumer's ServerSessionPool; getting 
  * the ServerSession's JMS session; loading it with the messages; and then 
  * starting the ServerSession.
  *
  * <P>In most cases the ServerSession will register some object it provides 
  * as the ServerSession's thread runObject. The ServerSession's 
  * <CODE>start</CODE> method will call the Threads <CODE>start</CODE> method
  * which will start the new thread, and from it, call the <CODE>run</CODE> 
  * method of the ServerSession's runObject. This object will do some 
  * housekeeping and then call the Session's <CODE>run</CODE> method. When 
  * <CODE>run</CODE> returns, the ServerSession's runObject can return the 
  * ServerSession to the ServerSessionPool and the cycle starts again.
  *
  * <P>Note that JMS does not architect how the ConnectionConsumer loads the 
  * Session with messages. Since both the ConnectionConsumer and Session are 
  * implemented by the same JMS provider, they can accomplish the load using 
  * a private mechanism.
  *
  * @version     1.0 - 9 March 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.ServerSessionPool
  * @see         javax.jms.ConnectionConsumer
  */

public interface ServerSession {

    /** Return the ServerSession's Session. This must be a Session
      * created by the same Connection which will be dispatching messages 
      * to it. The provider will assign one or more messages to the Session 
      * and then call start on the ServerSession.
      *
      * @return the server session's session.
      *  
      * @exception JMSException if a JMS fails to get associated
      *                         session for this serverSession due to
      *                         some internal error.
      **/

    Session
    getSession() throws JMSException;


    /** Cause the session's run method to be called to process messages
      * that were just assigned to it.
      *  
      * @exception JMSException if a JMS fails to start the server
      *                         session to process messages.
      */

    void 
    start() throws JMSException; 
}
