
/*
 * @(#)ObjectMessage.java	1.12 98/10/07
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

import java.io.Serializable;

/** An ObjectMessage is used to send a message that contains a serializable
  * Java object. It inherits from <CODE>Message</CODE> and adds a body
  * containing a single Java reference. Only <CODE>Serializable</CODE> Java
  * objects can be used.
  *
  * <P>If a collection of Java objects must be sent, one of the collection 
  * classes provided in JDK 1.2 can be used.
  *
  * <P>When a client receives an ObjectMessage, it is in read-only mode. If a 
  * client attempts to write to the message at this point, a 
  * MessageNotWriteableException is thrown. If <CODE>clearBody</CODE> is 
  * called, the message can now be both read from and written to.
  *
  * @version     1.0 - 6 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.BytesMessage
  * @see         javax.jms.MapMessage
  * @see         javax.jms.Message
  * @see         javax.jms.StreamMessage
  * @see         javax.jms.TextMessage
  */

public interface ObjectMessage extends Message {

    /** Set the serializable object containing this message's data.
      *
      * @param object the message's data
      *  
      * @exception JMSException if JMS fails to  set object due to
      *                         some internal JMS error.
      * @exception MessageFormatException if object serialization fails
      * @exception MessageNotWriteableException if message in read-only mode.
      */

    void 
    setObject(Serializable object) throws JMSException;


    /** Get the serializable object containing this message's data. The 
      * default value is null.
      *
      * @return the serializable object containing this message's data
      *  
      * @exception JMSException if JMS fails to  get object due to
      *                         some internal JMS error.
      * @exception MessageFormatException if object deserialization fails
      */

    Serializable 
    getObject() throws JMSException;
}
