
/* 
 * @(#)TextMessage.java	1.10 98/10/07
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
 *
 */

package javax.jms;

/** A TextMessage is used to send a message containing a 
  * <CODE>java.lang.StringBuffer</CODE>.
  * It inherits from <CODE>Message</CODE> and adds a text message body.
  *
  * <P>The inclusion of this message type is based on our presumption 
  * that XML will likely become a popular mechanism for representing 
  * content of all kinds including the content of JMS messages.
  *
  * <P>When a client receives an TextMessage, it is in read-only mode. If a 
  * client attempts to write to the message at this point, a 
  * MessageNotWriteableException is thrown. If <CODE>clearBody</CODE> is 
  * called, the message can now be both read from and written to.
  *
  * @version     1.0 - 19 March 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.BytesMessage
  * @see         javax.jms.MapMessage
  * @see         javax.jms.Message
  * @see         javax.jms.ObjectMessage
  * @see         javax.jms.StreamMessage
  * @see         java.lang.StringBuffer
  */
 
public interface TextMessage extends Message { 

    /** Set the string containing this message's data.
      *  
      * @param string the String containing the message's data
      *  
      * @exception JMSException if JMS fails to set text due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void
    setText(String string) throws JMSException;


    /** Get the string containing this message's data.  The default
      * value is null.
      *  
      * @return the String containing the message's data
      *  
      * @exception JMSException if JMS fails to get text due to
      *                         some internal JMS error.
      */ 

    String
    getText() throws JMSException;
}
