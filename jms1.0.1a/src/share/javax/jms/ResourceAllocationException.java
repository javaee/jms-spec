/*
 * @(#)ResourceAllocationException.java	1.2 98/10/08
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
 * <P>This exception is thrown when a provider is unable to allocate the 
 *    resources required by a method. For example, this exception should be 
 *    throw when a call to createTopicConnection fails due to lack of JMS 
 *    Provider resources.
 *
 * @version     26 August 1998
 * @author      Rahul Sharma
 **/

public class ResourceAllocationException extends JMSException {

  /** Construct a ResourceAllocationException with reason and errorCode 
   *  for exception
   *
   *  @param  reason        a description of the exception
   *  @param  errorCode     a string specifying the vendor specific
   *                        error code
   *                        
   **/
  public 
  ResourceAllocationException(String reason, String errorCode) {
    super(reason, errorCode);
  }

  /** Construct a ResourceAllocationException with reason. Error code 
   *  defaults to null.
   *
   *  @param  reason        a description of the exception
   **/
  public 
  ResourceAllocationException(String reason) {
    super(reason);
  }

}
