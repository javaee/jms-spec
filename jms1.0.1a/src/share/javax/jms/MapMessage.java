
/* 
 * @(#)MapMessage.java	1.21 98/10/07
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

import java.util.Enumeration;

/** A MapMessage is used to send a set of name-value pairs where names are 
  * Strings and values are Java primitive types. The entries can be accessed 
  * sequentially or randomly by name. The order of the entries is undefined. 
  * It inherits from <CODE>Message</CODE> and adds a map message body.
  *
  * <P>The primitive types can be read or written explicitly using methods
  * for each type. They may also be read or written generically as objects.
  * For instance, a call to <CODE>MapMessage.setInt("foo", 6)</CODE> is 
  * equivalent to <CODE>MapMessage.setObject("foo", new Integer(6))</CODE>.
  * Both forms are provided because the explicit form is convenient for
  * static programming and the object form is needed when types are not known
  * at compile time.
  *
  * <P>When a client receives a MapMessage, it is in read-only mode. If a 
  * client attempts to write to the message at this point, a 
  * MessageNotWriteableException is thrown. If <CODE>clearBody</CODE> is 
  * called, the message can now be both read from and written to.
  *
  * <P>Map messages support the following conversion table. The marked cases
  * must be supported. The unmarked cases must throw a JMSException. The
  * String to primitive conversions may throw a runtime exception if the
  * primitives <CODE>valueOf()</CODE> method does not accept it as a valid 
  * String representation of the primitive.
  *
  * <P>A value written as the row type can be read as the column type.
  *
  * <PRE>
  * |        | boolean byte short char int long float double String byte[]
  * |----------------------------------------------------------------------
  * |boolean |    X                                            X
  * |byte    |          X     X         X   X                  X
  * |short   |                X         X   X                  X
  * |char    |                     X                           X
  * |int     |                          X   X                  X
  * |long    |                              X                  X
  * |float   |                                    X     X      X
  * |double  |                                          X      X
  * |String  |    X     X     X    X    X   X     X     X      X
  * |byte[]  |                                                        X
  * |----------------------------------------------------------------------
  * </PRE>
  *
  * @version     1.0 - 6 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.BytesMessage
  * @see         javax.jms.Message
  * @see         javax.jms.ObjectMessage
  * @see         javax.jms.StreamMessage
  * @see         javax.jms.TextMessage
  * @see         java.io.DataInputStream
  * @see         java.io.DataOutputStream
  */
 
public interface MapMessage extends Message { 


    /** Return the boolean value with the given name.
      *
      * @param name the name of the boolean
      *
      * @return the boolean value with the given name.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.      */

    boolean 
    getBoolean(String name) throws JMSException;


    /** Return the byte value with the given name.
      *
      * @param name the name of the byte
      *
      * @return the byte value with the given name.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.
      */ 

    byte 
    getByte(String name) throws JMSException;


    /** Return the short value with the given name.
      *
      * @param name the name of the short
      *
      * @return the short value with the given name.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.      */ 

    short 
    getShort(String name) throws JMSException;


    /** Return the Unicode character value with the given name.
      *
      * @param name the name of the Unicode character
      *
      * @return the Unicode character value with the given name.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.     */ 

    char 
    getChar(String name) throws JMSException;


    /** Return the integer value with the given name.
      *
      * @param name the name of the integer
      *
      * @return the integer value with the given name.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.      */ 

    int 
    getInt(String name) throws JMSException;


    /** Return the long value with the given name.
      *
      * @param name the name of the long
      *
      * @return the long value with the given name.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.      */ 

    long 
    getLong(String name) throws JMSException;


    /** Return the float value with the given name.
      *
      * @param name the name of the float
      *
      * @return the float value with the given name.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.     */ 

    float 
    getFloat(String name) throws JMSException;


    /** Return the double value with the given name.
      *
      * @param name the name of the double
      *
      * @return the double value with the given name.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.      */ 

    double 
    getDouble(String name) throws JMSException;


    /** Return the String value with the given name.
      *
      * @param name the name of the String
      *
      * @return the String value with the given name. If there is no item
      * by this name, a null value is returned.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.      */ 

    String 
    getString(String name) throws JMSException;


    /** Return the byte array value with the given name.
      *
      * @param name the name of the byte array
      *
      * @return the byte array value with the given name. If there is no
      * item by this name, a null value is returned.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.      */ 

    byte[] 
    getBytes(String name) throws JMSException;


    /** Return the Java object value with the given name.
      *
      * <P>Note that this method can be used to return in objectified format,
      * an object that had been stored in the Map with the equivalent
      * <CODE>setObject</CODE> method call, or it's equivalent primitive
      * set<type> method.
      *
      * @param name the name of the Java object
      *
      * @return the Java object value with the given name, in objectified
      * format (ie. if it set as an int, then a Integer is returned).
      * If there is no item by this name, a null value is returned.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
  */ 

    Object 
    getObject(String name) throws JMSException;



    /** Return an Enumeration of all the Map message's names.
      *
      * @return an enumeration of all the names in this Map message.
      *
      * @exception JMSException if JMS fails to read message due to
      *                         some internal JMS error.
     */

    Enumeration
    getMapNames() throws JMSException;


    /** Set a boolean value with the given name, into the Map.
      *
      * @param name the name of the boolean
      * @param value the boolean value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */

    void 
    setBoolean(String name, boolean value) throws JMSException;


    /** Set a byte value with the given name, into the Map.
      *
      * @param name the name of the byte
      * @param value the byte value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void 
    setByte(String name, byte value) 
			throws JMSException;


    /** Set a short value with the given name, into the Map.
      *
      * @param name the name of the short
      * @param value the short value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void 
    setShort(String name, short value) 
			throws JMSException;


    /** Set a Unicode character value with the given name, into the Map.
      *
      * @param name the name of the Unicode character
      * @param value the Unicode character value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void 
    setChar(String name, char value) 
			throws JMSException;


    /** Set an integer value with the given name, into the Map.
      *
      * @param name the name of the integer
      * @param value the integer value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void 
    setInt(String name, int value) 
			throws JMSException;


    /** Set a long value with the given name, into the Map.
      *
      * @param name the name of the long
      * @param value the long value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void 
    setLong(String name, long value) 
			throws JMSException;


    /** Set a float value with the given name, into the Map.
      *
      * @param name the name of the float
      * @param value the float value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void 
    setFloat(String name, float value) 
			throws JMSException;


    /** Set a double value with the given name, into the Map.
      *
      * @param name the name of the double
      * @param value the double value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void 
    setDouble(String name, double value) 
			throws JMSException;


    /** Set a String value with the given name, into the Map.
      *
      * @param name the name of the String
      * @param value the String value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void 
    setString(String name, String value) 
			throws JMSException;


    /** Set a byte array value with the given name, into the Map.
      *
      * @param name the name of the byte array
      * @param value the byte array value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void
    setBytes(String name, byte[] value) 
			throws JMSException;


    /** Set a portion of the byte array value with the given name, into the Map.
      *  
      * @param name the name of the byte array
      * @param value the byte array value to set in the Map.
      * @param offset the initial offset within the byte array.
      * @param length the number of bytes to use.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 
 
    void
    setBytes(String name, byte[] value, 
		 int offset, int length) 
			throws JMSException;


    /** Set a Java object value with the given name, into the Map.
      *
      * <P>Note that this method only works for the objectified primitive
      * object types (Integer, Double, Long ...), String's and byte arrays.
      *
      * @param name the name of the Java object
      * @param value the Java object value to set in the Map.
      *
      * @exception JMSException if JMS fails to write message due to
      *                         some internal JMS error.
      * @exception MessageFormatException if object is invalid
      * @exception MessageNotWriteableException if message in read-only mode.
      */ 

    void 
    setObject(String name, Object value) 
			throws JMSException;


    /** Check if an item exists in this MapMessage.
      *
      * @param name the name of the item to test
      *
      * @return true if the item does exist.
      *
      * @exception JMSException if a JMS error occurs.
      */
 
    boolean
    itemExists(String name) throws JMSException;
}
