/*
 * @(#)IllegalStateException.java	1.3 98/10/07
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

/**
 * <P> This exception is thrown when a method is 
 *     invoked at an illegal or inappropriate time or if the provider is 
 *     not in an appropriate state for the requested operation. For example, 
 *     this exception should be thrown if Session.commit() is called on a 
 *     non-transacted session.
 *
 * @version     26 August 1998
 * @author      Rahul Sharma
 **/

public class IllegalStateException extends JMSException {

  /** Construct a IllegalStateException with reason and errorCode 
   *  for exception
   *
   *  @param  reason        a description of the exception
   *  @param  errorCode     a string specifying the vendor specific
   *                        error code
   *                        
   **/
  public 
  IllegalStateException(String reason, String errorCode) {
    super(reason, errorCode);
  }

  /** Construct a IllegalStateException with reason. Error code 
   *  defaults to null.
   *
   *  @param  reason        a description of the exception
   **/
  public 
  IllegalStateException(String reason) {
    super(reason);
  }

}
