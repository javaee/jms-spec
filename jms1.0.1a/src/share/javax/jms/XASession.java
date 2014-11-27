
/*
 * @(#)XASession.java	1.11 98/08/13
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

import javax.transaction.xa.XAResource;

/** XASession extends the capability of Session by adding access to a JMS 
  * provider's support for JTA. This support takes the form of a 
  * <CODE>javax.transaction.xa.XAResource</CODE> object. The functionality of 
  * this object closely resembles that defined by the standard X/Open XA 
  * Resource interface.
  *
  * <P>An application server controls the transactional assignment of an 
  * XASession by obtaining its XAResource. It uses the XAResource to assign 
  * the session to a transaction; prepare and commit work on the
  * transaction; etc.
  *
  * <P>An XAResource provides some fairly sophisticated facilities for 
  * interleaving work on multiple transactions; recovering a list of 
  * transactions in progress; etc. A JTA aware JMS provider must fully 
  * implement this functionality. This could be done by using the services 
  * of a database that supports XA or a JMS provider may choose to implement 
  * this functionality from scratch.
  *
  * <P>A client of the application server is given what it thinks is a 
  * regular JMS Session. Behind the scenes, the application server controls 
  * the transaction management of the underlying XASession.
  * 
  * @version     1.0 - 13 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.Session
  */ 
 
public interface XASession extends Session {

    /** Return an XA resource to the caller.
      *
      * @return an XA resource to the caller
      */

     XAResource
     getXAResource();
}
