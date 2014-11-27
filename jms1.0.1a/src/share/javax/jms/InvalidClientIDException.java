/*
 * @(#)InvalidClientIDException.java	1.4 98/10/07
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
 * <P> This exception must be thrown when a 
 *     client attempts to set a connection's client id to a value that 
 *     is rejected by a provider.
 *
 * @version     26 August 1998
 * @author      Rahul Sharma
 **/

public class InvalidClientIDException extends JMSException {

  /** Construct a InvalidClientIDException with reason and errorCode 
   *  for exception
   *
   *  @param  reason        a description of the exception
   *  @param  errorCode     a string specifying the vendor specific
   *                        error code
   *                        
   **/
  public 
  InvalidClientIDException(String reason, String errorCode) {
    super(reason, errorCode);
  }

  /** Construct a InvalidClientIDException with reason. Error code defaults to 
   *  null.
   *
   *  @param  reason        a description of the exception
   **/
  public 
  InvalidClientIDException(String reason) {
    super(reason);
  }

}
