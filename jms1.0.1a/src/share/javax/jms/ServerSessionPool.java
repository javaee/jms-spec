
/*
 * @(#)ServerSessionPool.java	1.7 98/10/07
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

/** A ServerSessionPool is an object implemented by an application server 
  * to provide a pool of ServerSessions for processing the messages of a 
  * ConnectionConsumer.
  *
  * <P>Its only method is <CODE>getServerSession</CODE>. JMS does not 
  * architect how the pool is implemented. It could be a static pool of 
  * ServerSessions or it could use a sophisticated algorithm to dynamically 
  * create ServerSessions as needed.
  *
  * <P>If the ServerSessionPool is out of ServerSessions, the 
  * <CODE>getServerSession</CODE> call may block. If a ConnectionConsumer 
  * is blocked, it cannot deliver new messages until a ServerSession is 
  * eventually returned.
  *
  * @version     1.0 - 9 March 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see javax.jms.ServerSession
  */

public interface ServerSessionPool {

    /** Return a server session from the pool.
      *
      * @return a server session from the pool.
      *  
      * @exception JMSException if an application server fails to
      *                         return a Server Session out of its
      *                         server session pool.
      */ 

    ServerSession
    getServerSession() throws JMSException;
}
