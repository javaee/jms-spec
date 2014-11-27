/*
 * @(#)MessageFormatException.java	1.3 98/10/08
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
 * <P> This exception must be thrown when a JMS client 
 *     attempts to use a data type not supported by a message or attempts to 
 *     read data in a message as the wrong type. It must also be thrown when 
 *     equivalent type errors are made with message property values. For 
 *     example, this exception must be thrown if StreamMessage.setObject() is 
 *     given an unsupported class or if StreamMessage.getShort() is used to 
 *     read a boolean value. Note that the special case of a failure caused by 
 *     attempting to read improperly formatted String data as numeric values 
 *     should throw the java.lang.NumberFormatException.
 *
 * @version     26 August 1998
 * @author      Rahul Sharma
 **/

public class MessageFormatException extends JMSException {

  /** Construct a MessageFormatException with reason and errorCode 
   *  for exception
   *
   *  @param  reason        a description of the exception
   *  @param  errorCode     a string specifying the vendor specific
   *                        error code
   *                        
   **/
  public 
  MessageFormatException(String reason, String errorCode) {
    super(reason, errorCode);
  }

  /** Construct a MessageFormatException with reason. Error code 
   *  defaults to null.
   *
   *  @param  reason        a description of the exception
   **/
  public 
  MessageFormatException(String reason) {
    super(reason);
  }

}
