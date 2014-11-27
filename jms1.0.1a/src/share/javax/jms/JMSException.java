
/*
 * @(#)JMSException.java	1.10 98/10/09
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
 * <P>This is the root class of all JMS exceptions.
 *
 * <P>It provides following information:
 * <UL>
 *   <LI> A provider-specific string describing the error - This string is 
 *        the standard Java exception message, and is available via 
 *        getMessage().
 *   <LI> A provider-specific, string error code 
 *   <LI> A reference to another exception - Often a JMS exception will 
 *        be the result of a lower level problem. If appropriate, this 
 *        lower level exception can be linked to the JMS exception.
 * </UL>
 * @version     1.0 - 5 Oct 1998
 * @author      Mark Hapner
 * @author      Rich Burridge
 **/

public class JMSException extends Exception {

  /** Vendor specific error code
  **/
  private String errorCode;

  /** Exception reference
  **/
  private Exception linkedException;


  /** Construct a JMSException with reason and errorCode for exception
   *
   *  @param  reason        a description of the exception
   *  @param  errorCode     a string specifying the vendor specific
   *                        error code
   **/
  public 
  JMSException(String reason, String errorCode) {
    super(reason);
    this.errorCode = errorCode;
    linkedException = null;
  }

  /** Construct a JMSException with reason and with error code defaulting
   *  to null
   *
   *  @param  reason        a description of the exception
   **/
  public 
  JMSException(String reason) {
    super(reason);
    this.errorCode = null;
    linkedException = null;
  }

  /** Get the vendor specific error code
   *  @return   a string specifying the vendor specific
   *                        error code
  **/
  public 
  String getErrorCode() {
    return this.errorCode;
  }

  /**
   * Get the exception linked to this one.
   *
   * @return the linked Exception, null if none
  **/
  public 
  Exception getLinkedException() {
    return (linkedException);
  }

  /**
   * Add a linked Exception.
   *
   * @param ex       the linked Exception
  **/
  public 
  synchronized void setLinkedException(Exception ex) {
      linkedException = ex;
  }
}
