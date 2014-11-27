
/*
 * @(#)BytesMessage.java	1.16 98/10/07
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

import java.io.InputStream;
import java.io.OutputStream;

/** A BytesMessage is used to send a message containing a stream of 
  * uninterpreted bytes. It inherits <CODE>Message</CODE> and adds a bytes
  * message body. The receiver of the message supplies the interpretation
  * of the bytes.
  *
  * <P>It's methods are based largely on those found in 
  * <CODE>java.io.DataInputStream</CODE> and
  * <CODE>java.io.DataOutputStream</CODE>.
  *
  * <P>This message type is for client encoding of existing message formats. 
  * If possible, one of the other self-defining message types should be used 
  * instead.
  *
  * <P>Although JMS allows the use of message properties with byte messages
  * it is typically not done since the inclusion of properties affects the
  * format.
  *
  * <P>The primitive types can be written explicitly using methods
  * for each type. They may also be written generically as objects.
  * For instance, a call to <CODE>BytesMessage.writeInt(6)</CODE> is
  * equivalent to <CODE>BytesMessage.writeObject(new Integer(6))</CODE>.
  * Both forms are provided because the explicit form is convenient for
  * static programming and the object form is needed when types are not known
  * at compile time.
  *
  * <P>When the message is first created, and when <CODE>clearBody</CODE>
  * is called, the body of the message is in write-only mode. After the 
  * first call to <CODE>reset</CODE> has been made, the message is in 
  * read-only mode. When a message has been sent, by definition, the 
  * provider calls <CODE>reset</CODE> in order to read it's content, and 
  * when a message has been received, the provider has called 
  * <CODE>reset</CODE> so that the message is in read-only mode for the client.
  *
  * <P>If <CODE>clearBody</CODE> is called on a message in read-only mode, 
  * the message body is cleared and the message is in write-only mode.
  *
  * <P>If a client attempts to read a message in write-only mode, a 
  * MessageNotReadableException is thrown.
  *
  * <P>If a client attempts to write a message in read-only mode, a 
  * MessageNotWriteableException is thrown.
  *
  * @version     1.0 - 6 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.MapMessage
  * @see         javax.jms.Message
  * @see         javax.jms.ObjectMessage
  * @see         javax.jms.StreamMessage
  * @see         javax.jms.TextMessage
  * @see         java.io.InputStream
  * @see         java.io.OutputStream
  */

public interface BytesMessage extends Message {

    /** Read a <code>boolean</code> from the stream message.
      *
      * @return the <code>boolean</code> value read.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageEOFException if end of message stream
      */

    boolean 
    readBoolean() throws JMSException;


    /** Read a signed 8-bit value from the stream message.
      *
      * @return the next byte from the stream message as a signed 8-bit
      * <code>byte</code>.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 

    byte 
    readByte() throws JMSException;


    /** Read an unsigned 8-bit number from the stream message.
      *  
      * @return the next byte from the stream message, interpreted as an
      * unsigned 8-bit number.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */

    int
    readUnsignedByte() throws JMSException;


    /** Read a signed 16-bit number from the stream message.
      *
      * @return the next two bytes from the stream message, interpreted as a
      * signed 16-bit number.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 

    short 
    readShort() throws JMSException;


    /** Read an unsigned 16-bit number from the stream message.
      *  
      * @return the next two bytes from the stream message, interpreted as an
      * unsigned 16-bit integer.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 
 
    int
    readUnsignedShort() throws JMSException;


    /** Read a Unicode character value from the stream message.
      *
      * @return the next two bytes from the stream message as a Unicode
      * character.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 

    char 
    readChar() throws JMSException;


    /** Read a signed 32-bit integer from the stream message.
      *
      * @return the next four bytes from the stream message, interpreted as
      * an <code>int</code>.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 

    int 
    readInt() throws JMSException;


    /** Read a signed 64-bit integer from the stream message.
      *
      * @return the next eight bytes from the stream message, interpreted as
      * a <code>long</code>.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 

    long 
    readLong() throws JMSException;


    /** Read a <code>float</code> from the stream message.
      *
      * @return the next four bytes from the stream message, interpreted as
      * a <code>float</code>.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 

    float 
    readFloat() throws JMSException;


    /** Read a <code>double</code> from the stream message.
      *
      * @return the next eight bytes from the stream message, interpreted as
      * a <code>double</code>.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 

    double 
    readDouble() throws JMSException;


    /** Read in a string that has been encoded using a modified UTF-8
      * format from the stream message.
      *
      * <P>For more information on the UTF-8 format, see "File System Safe
      * UCS Transformation Format (FSS_UFT)", X/Open Preliminary Specification,
      * X/Open Company Ltd., Document Number: P316. This information also
      * appears in ISO/IEC 10646, Annex P.
      *
      * @return a Unicode string from the stream message.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 

    String 
    readUTF() throws JMSException;


    /** Read a byte array from the stream message.
      *
      * @param value the buffer into which the data is read.
      *
      * @return the total number of bytes read into the buffer, or -1 if 
      * there is no more data because the end of the stream has been reached.
      *
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 

    int
    readBytes(byte[] value) throws JMSException;


    /** Read a portion of the bytes message.
      *  
      * @param value the buffer into which the data is read.
      * @param length the number of bytes to read.
      *  
      * @return the total number of bytes read into the buffer, or -1 if
      * there is no more data because the end of the stream has been reached.
      *  
      * @exception MessageNotReadableException if message in write-only mode.
      * @exception MessageEOFException if end of message stream
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      */ 

    int
    readBytes(byte[] value, int length) 
			throws JMSException;


    /** Write a <code>boolean</code> to the stream message as a 1-byte value.
      * The value <code>true</code> is written out as the value 
      * <code>(byte)1</code>; the value <code>false</code> is written out as 
      * the value <code>(byte)0</code>.
      *
      * @param value the <code>boolean</code> value to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */

    void 
    writeBoolean(boolean value) 
			throws JMSException;


    /** Write out a <code>byte</code> to the stream message as a 1-byte value.
      *
      * @param value the <code>byte</code> value to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 

    void 
    writeByte(byte value) throws JMSException;


    /** Write a <code>short</code> to the stream message as two bytes, high 
      * byte first.
      *
      * @param value the <code>short</code> to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 

    void 
    writeShort(short value) throws JMSException;


    /** Write a <code>char</code> to the stream message as a 2-byte value, 
      * high byte first.
      *
      * @param value the <code>char</code> value to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 

    void 
    writeChar(char value) throws JMSException;


    /** Write an <code>int</code> to the stream message as four bytes, 
      * high byte first.
      *
      * @param value the <code>int</code> to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 

    void 
    writeInt(int value) throws JMSException;


    /** Write a <code>long</code> to the stream message as eight bytes, 
      * high byte first.
      *
      * @param value the <code>long</code> to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 

    void 
    writeLong(long value) throws JMSException;


    /** Convert the float argument to an <code>int</code> using the
      * <code>floatToIntBits</code> method in class <code>Float</code>,
      * and then writes that <code>int</code> value to the stream
      * message as a 4-byte quantity, high byte first.
      *
      * @param value the <code>float</code> value to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 

    void 
    writeFloat(float value) throws JMSException;


    /** Convert the double argument to a <code>long</code> using the
      * <code>doubleToLongBits</code> method in class <code>Double</code>,
      * and then writes that <code>long</code> value to the stream
      * message as an 8-byte quantity, high byte first.
      *
      * @param value the <code>double</code> value to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 

    void 
    writeDouble(double value) throws JMSException;


    /** Write a string to the stream message using UTF-8 encoding in a 
      * machine-independent manner.
      *
      * <P>For more information on the UTF-8 format, see "File System Safe 
      * UCS Transformation Format (FSS_UFT)", X/Open Preliminary Specification,       * X/Open Company Ltd., Document Number: P316. This information also 
      * appears in ISO/IEC 10646, Annex P. 
      *
      * @param value the <code>String</code> value to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 

    void 
    writeUTF(String value) throws JMSException;


    /** Write a byte array to the stream message.
      *
      * @param value the byte array to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 

    void
    writeBytes(byte[] value) throws JMSException;


    /** Write a portion of a byte array to the stream message.
      *  
      * @param value the byte array value to be written.
      * @param offset the initial offset within the byte array.
      * @param length the number of bytes to use.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 
 
    void
    writeBytes(byte[] value, int offset, int length) 
			throws JMSException;


    /** Write a Java object to the stream message.
      *
      * <P>Note that this method only works for the objectified primitive
      * object types (Integer, Double, Long ...), String's and byte arrays.
      *
      * @param value the Java object to be written.
      *
      * @exception MessageNotWriteableException if message in read-only mode.
      * @exception MessageFormatException if object is invalid type.
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      */ 

    void 
    writeObject(Object value) throws JMSException;


    /** Put the message in read-only mode, and reposition the stream of 
      * bytes to the beginning.
      *  
      * @exception JMSException if JMS fails to reset the message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if message has an invalid
      *                         format
      */ 

    void
    reset() throws JMSException;
}
