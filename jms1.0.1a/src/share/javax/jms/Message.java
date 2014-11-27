
/*
 * @(#)Message.java	1.30 98/10/07
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
import java.util.Properties;

/** The Message interface is the root interface of all JMS messages. It
  * defines the JMS header and the <CODE>acknowledge</CODE> method used for 
  * all messages.
  *
  * <P>Most MOM products treat messages as lightweight entities that consist
  * of a header and a payload. The header contains fields used for message
  * routing and identification; the payload contains the application data
  * being sent.
  *
  * <P>Within this general form, the definition of a message varies
  * significantly across products. It would be quite difficult for JMS to
  * support all of these message models.
  *
  * <P>With this in mind, the JMS message model has the following goals:
  * <UL>
  *   <LI>Provide a single, unified message API
  *   <LI>Provide an API suitable for creating messages that match the
  *       format used by existing non-JMS applications
  *   <LI>Support the development of hetrogeneous applications that span
  *       operating systems, machine architectures and computer languages
  *   <LI>Support messages containing Java objects
  *   <LI>Support messages containing Extensible Markup Language (XML) pages
  * </UL>
  *
  * <P>JMS Messages are composed of the following parts:
  * <UL>
  *   <LI>Header - All messages support the same set of header fields. 
  *       Header fields contain values used by both clients and providers to 
  *       identify and route messages.
  *   <LI>Properties - Each message contains a built-in facility for supporting
  *       application defined property values. Properties provide an efficient 
  *       mechanism for supporting application defined message filtering.
  *   <LI>Body - JMS defines several types of message body which cover the 
  *       majority of messaging styles currently in use.
  * </UL>
  *
  * <P>JMS defines five types of message body:
  * <UL>
  *   <LI>Stream - a stream of Java primitive values. It is filled and read 
  *       sequentially.
  *   <LI>Map - a set of name-value pairs where names are Strings and values 
  *       are Java primitive types. The entries can be accessed sequentially 
  *       or randomly by name. The order of the entries is undefined.
  *   <LI>Text - a message containing a java.util.StringBuffer. The inclusion 
  *       of this message type is based on our presumption that XML will 
  *       likely become a popular mechanism for representing content of all 
  *       kinds including the content of JMS messages.
  *   <LI>Object - a message that contains a Serializable java object
  *   <LI>Bytes - a stream of uninterpreted bytes. This message type is for 
  *       literally encoding a body to match an existing message format. In 
  *       many cases, it will be possible to use one of the other, easier to 
  *       use, body types instead. Although JMS allows the use of message 
  *       properties with byte messages it is typically not done since the 
  *       inclusion of properties may affect the format.
  * </UL>
  *
  * <P>The JMSCorrelationID header field is used for linking one message with
  * another. It typically links a reply message with its requesting message.
  *
  * <P>JMSCorrelationID can hold either a provider-specific message ID, an
  * application-specific String or a provider-native byte[] value.
  *
  * <P>A Message contains a built-in facility for supporting application 
  * defined property values. In effect, this provides a mechanism for adding 
  * application specific header fields to a message.
  *
  * <P>Properties allow an application, via message selectors, to have a JMS 
  * provider select/filter messages on its behalf using application-specific 
  * criteria.
  *
  * <P>Property names must obey the rules for a message selector identifier. 
  *
  * <P>Property values can be boolean, byte, short, int, long, float, 
  * double, and String.
  *
  * <P>Property values are set prior to sending a message. When a client 
  * receives a message, its properties are in read-only mode. If a 
  * client attempts to set properties at this point, a 
  * MessageNotWriteableException is thrown. If <CODE>clearProperties</CODE> is 
  * called, the properties can now be both read from and written to.
  *
  * <P>A property value may duplicate a value in a message's body or it may 
  * not. Although JMS does not define a policy for what should or should not 
  * be made a property, application developers should note that JMS providers 
  * will likely handle data in a message's body more efficiently than data in 
  * a message's properties. For best performance, applications should only 
  * use message properties when they need to customize a message's header. 
  * The primary reason for doing this is to support customized message 
  * selection.
  *
  * <P>Message properties support the following conversion table. The marked 
  * cases must be supported. The unmarked cases must throw a JMSException. The
  * String to primitive conversions may throw a runtime exception if the
  * primitives <CODE>valueOf()</CODE> method does not accept it as a valid
  * String representation of the primitive.
  *
  * <P>A value written as the row type can be read as the column type.
  *
  * <PRE>
  * |        | boolean byte short int long float double String 
  * |----------------------------------------------------------
  * |boolean |    X                                       X
  * |byte    |          X     X    X   X                  X 
  * |short   |                X    X   X                  X 
  * |int     |                     X   X                  X 
  * |long    |                         X                  X 
  * |float   |                               X     X      X 
  * |double  |                                     X      X 
  * |String  |    X     X     X    X   X     X     X      X 
  * |----------------------------------------------------------
  * </PRE>
  *
  * <P>In addition to the type-specific set/get methods for properties, JMS 
  * provides the <CODE>setObjectProperty</CODE> and 
  * <CODE>getObjectProperty</CODE> methods. These support the same set of 
  * property types using the objectified primitive values. Their purpose is 
  * to allow the decision of property type to made at execution time rather 
  * than at compile time. They support the same property value conversions.
  *
  * <P>The <CODE>setObjectProperty</CODE> method accepts values of class 
  * Boolean, Byte, Short, Integer, Long, Float, Double and String. An attempt 
  * to use any other class must throw a JMSException.
  *
  * <P>The <CODE>getObjectProperty</CODE> method only returns values of class 
  * Boolean, Byte, Short, Integer, Long, Float, Double and String.
  *
  * <P>The order of property values is not defined. To iterate through a 
  * message's property values, use <CODE>getPropertyNames</CODE> to retrieve 
  * a property name enumeration and then use the various property get methods 
  * to retrieve their values.
  *
  * <P>A message's properties are deleted by the <CODE>clearProperties</CODE>
  * method. This leaves the message with an empty set of properties.
  *
  * <P>Getting a property value for a name which has not been set returns a 
  * null value. Only the <CODE>getStringProperty</CODE> and 
  * <CODE>getObjectProperty</CODE> methods can return a null value. The other 
  * property get methods must throw a 
  * <CODE>java.lang.NullPointerException</CODE> if they are used to get a 
  * non-existent property.
  *
  * <P>JMS reserves the `JMSX' property name prefix for JMS defined properties.
  * The full set of these properties is defined in the Java Message Service
  * specification. New JMS defined properties may be added in later versions 
  * of JMS.  Support for these properties is optional. The 
  * <CODE>String[] ConnectionMetaData.getJMSXPropertyNames</CODE> method 
  * returns the names of the JMSX properties supported by a connection.
  *
  * <P>JMSX properties may be referenced in message selectors whether or not 
  * they are supported by a connection. They are treated like any other absent
  * property.
  *
  * <P>JSMX properties `set by provider on send' are available to both the 
  * producer and the consumers of the message. JSMX properties `set by 
  * provider on receive' are only available to the consumers.
  *
  * <P>JMSXGroupID and JMSXGroupSeq are simply standard properties clients 
  * should use if they want to group messages. All providers must support them.
  * Unless specifically noted, the values and semantics of the JMSX properties 
  * are undefined.
  *
  * <P>JMS reserves the `JMS_<vendor_name>' property name prefix for 
  * provider-specific properties. Each provider defines there own value of 
  * <vendor_name>. This is the mechanism a JMS provider uses to make its 
  * special per message services available to a JMS client.
  *
  * <P>The purpose of provider-specific properties is to provide special 
  * features needed to support JMS use with provider-native clients. They 
  * should not be used for JMS to JMS messaging.
  *
  * <P>JMS provides a set of message interfaces that define the JMS message 
  * model. It does not provide implementations of these interfaces.
  *
  * <P>Each JMS provider supplies a set of message factories with its Session 
  * object for creating instances of these messages. This allows a provider 
  * to use implementations tailored to their specific needs.
  *
  * <P>A provider must be prepared to accept message implementations that are 
  * not its own. They may not be handled as efficiently as their own 
  * implementations; however, they must be handled.
  *
  * <P>A JMS message selector allows a client to specify by message header the 
  * messages it's interested in. Only messages whose headers match the 
  * selector are delivered. The semantics of not delivered differ a bit 
  * depending on the MessageConsumer being used (see QueueReceiver and 
  * TopicSubscriber).
  *
  * <P>Message selectors cannot reference message body values.
  *
  * <P>A message selector matches a message when the selector evaluates to 
  * true when the message's header field and property values are substituted
  * for their corresponding identifiers in the selector.
  *
  * <P>A message selector is a String, whose syntax is based on a subset of 
  * the SQL92 conditional expression syntax.
  *
  * <P>The order of evaluation of a message selector is from left to right 
  * within precedence level. Parenthesis can be used to change this order.
  *
  * <P>Predefined selector literals and operator names are written here in 
  * upper case; however, they are case insensitive.
  *
  * <P>A selector can contain:
  *
  * <UL>
  *   <LI>Literals:
  *   <UL>
  *     <LI>A string literal is enclosed in single quotes with single quote 
  *         represented by doubled single quote such as `literal' and 
  *         `literal''s'; like Java string literals these use the unicode 
  *         character encoding.
  *     <LI>An exact numeric literal is a numeric value without a decimal 
  *         point such as 57, -957, +62; numbers in the range of Java long 
  *         are supported.
  *     <LI>An approximate numeric literal is a numeric value in scientific 
  *         notation such as 7E3, -57.9E2 or a numeric value with a decimal 
  *         such as 7., -95.7, +6.2; numbers in the range of Java double are 
  *         supported.
  *     <LI>The boolean literals TRUE, true, FALSE and false.
  *   </UL>
  *   <LI>Identifiers:
  *   <UL>
  *     <LI>An identifier is an unlimited length sequence of Java letters 
  *         and Java digits, the first of which must be a Java letter. A 
  *         letter is any character for which the method Character.isJavaLetter
  *         returns true. This includes `_' and `$'.  A letter or digit is any 
  *         character for which the method Character.isJavaLetterOrDigit 
  *         returns true.
  *     <LI>Identifiers cannot be the names NULL, TRUE, or FALSE.
  *     <LI>Identifiers cannot be NOT, AND, OR, BETWEEN, LIKE, IN, and IS.
  *     <LI>Identifiers are either header field references or property 
  *         references.
  *     <LI>Identifiers are case sensitive.
  *     <LI>Message header field references are restricted to JMSDeliveryMode,
  *         JMSPriority, JMSMessageID, JMSTimestamp, JMSCorrelationID, and 
  *         JMSType. JMSMessageID, JMSTimestamp, JMSCorrelationID, and JMSType
  *         values may be null and if so are treated as a NULL value.
  *     <LI>Any name beginning with `JMSX' is a JMS defined property name.
  *     <LI>Any name beginning with `JMS_' is a provider-specific property name.
  *     <LI>Any name that does not begin with `JMS' is an application-specific 
  *         property name. If a property is referenced that does not exist in 
  *         a message its value is NULL. If it does exist, its value is the 
  *         corresponding property value.
  *   </UL>
  *   <LI>Whitespace is the same as that defined for Java: space, horizontal 
  *       tab, form feed and line terminator.
  *   <LI>Expressions: 
  *   <UL>
  *     <LI>A selector is a conditional expression; a selector that evaluates 
  *         to true matches; a selector that evaluates to false or unknown 
  *         does not match.
  *     <LI>Arithmetic expressions are composed of themselves, arithmetic 
  *         operations, identifiers (whose value is treated as a numeric 
  *         literal) and numeric literals.
  *     <LI>Conditional expressions are composed of themselves, comparison 
  *         operations and logical operations.
  *   </UL>
  *   <LI>Standard bracketing () for ordering expression evaluation is 
  *       supported.
  *   <LI>Logical operators in precedence order: NOT, AND, OR
  *   <LI>Comparison operators: =, >, >=, <, <=, <> (not equal)
  *   <UL>
  *     <LI>Only like type values can be compared. An exact numeric value can 
  *         only be compared with an exact numeric value. The same type 
  *         correspondence restriction applies to comparison of approximate 
  *         numeric, String and boolean values. If the comparison of non-like 
  *         type values is attempted, the selector is always false.
  *     <LI>String comparison is restricted to = and <>. Two strings are equal 
  *         if and only if they contain the same sequence of characters.
  *   </UL>
  *   <LI>Arithmetic operators in precedence order:
  *   <UL>
  *     <LI>+, - unary
  *     <LI>*, / multiplication and division
  *     <LI>+, - addition and subtraction
  *     <LI>Arithmetic operations on a NULL value are not supported; if they 
  *         are attempted, the complete selector is always false.
  *     <LI>Arithmetic operations must use Java numeric promotion.
  *   </UL>
  *   <LI>arithmetic-expr1 [NOT] BETWEEN arithmetic-expr2 and arithmetic-expr3 
  *       comparison operator
  *   <UL>
  *     <LI>age BETWEEN 15 and 19 is equivalent to age >= 15 AND age <= 19
  *     <LI>age NOT BETWEEN 15 and 19 is equivalent to age < 15 OR age > 19
  *     <LI>If any of the exprs of a BETWEEN operation are NULL the value of 
  *         the operation is false; if any of the exprs of a NOT BETWEEN 
  *         operation are NULL the value of the operation is true.
  *   </UL>
  *   <LI>identifier [NOT] IN (string-literal1, string-literal2,...) 
  *       comparison operator where identifer has a String or NULL value.
  *   <UL>
  *     <LI>Country IN (' UK', 'US', 'France') is true for `UK' and false 
  *         for `Peru' it is equivalent to the expression (Country = ' UK') 
  *         OR (Country = ' US') OR (Country = ' France')
  *     <LI>Country NOT IN (' UK', 'US', 'France') is false for `UK' and 
  *         true for `Peru' it is equivalent to the expression NOT 
  *         ((Country = ' UK') OR (Country = ' US') OR (Country = ' France'))
  *     <LI>If identifier of an IN or NOT IN operation is NULL the value of 
  *         the operation is unknown.
  *   </UL>
  *   <LI>identifier [NOT] LIKE pattern-value [ESCAPE escape-character] 
  *       comparison operator, where identifier has a String value; 
  *       pattern-value is a string literal where `_' stands for any 
  *       single character; `%' stands for any sequence of characters 
  *       (including the empty sequence); and all other characters stand 
  *       for themselves. The optional escape-character is a single 
  *       character string literal whose character is used to escape the 
  *       special meaning of the `_' and `%' in pattern-value.
  *   <UL>
  *     <LI>phone LIKE `12%3' is true for `123' `12993' and false for `1234'
  *     <LI>word LIKE `l_se' is true for `lose' and false for `loose'
  *     <LI>underscored LIKE `\_%' ESCAPE `\' is true for `_foo' and false 
  *         for `bar'
  *     <LI>phone NOT LIKE `12%3' is false for `123' `12993' and true 
  *         for `1234'
  *     <LI>If identifier of a LIKE or NOT LIKE operation is NULL the value 
  *         of the operation is unknown.
  *   </UL>
  *   <LI>identifier IS NULL comparison operator tests for a null header 
  *       field value, or a missing property value.
  *   <UL>
  *     <LI>prop_name IS NULL
  *   </UL>
  *   <LI>identifier IS NOT NULL comparison operator tests for the existence 
  *       of a non null header field value or a property value.
  *   <UL>
  *     <LI>prop_name IS NOT NULL
  *   </UL>
  *
  * <P>JMS providers are required to verify the syntactic correctness of a 
  *    message selector at the time it is presented. A method providing a 
  *    syntactically incorrect selector must result in a JMSException.
  *
  * <P>The following message selector selects messages with a message type 
  * of car and color of blue and weight greater than 2500 lbs:
  *
  * <P>"JMSType = `car' AND color = `blue' AND weight > 2500"
  *
  * <P>As noted above, property values may be NULL. The evaluation of 
  * selector expressions containing NULL values is defined by SQL 92 NULL 
  * semantics. A brief description of these semantics is provided here.
  *
  * <P>SQL treats a NULL value as unknown. Comparison or arithmetic with 
  * an unknown value always yields an unknown value.
  *
  * <P>The IS NULL and IS NOT NULL operators convert an unknown value into 
  * the respective TRUE and FALSE values.
  *
  * <P>The boolean operators use three valued logic as defined by the 
  * following tables:
  *
  * <P>The definition of the AND operator
  *
  * <PRE>
  * | AND  |   T   |   F   |   U
  * +------+-------+-------+-------
  * |  T   |   T   |   F   |   U
  * |  F   |   F   |   F   |   F
  * |  U   |   U   |   F   |   U
  * +------+-------+-------+-------
  * </PRE>
  *
  * <P>The definition of the OR operator
  *
  * <PRE>
  * | OR   |   T   |   F   |   U
  * +------+-------+-------+--------
  * |  T   |   T   |   T   |   T
  * |  F   |   T   |   F   |   U
  * |  U   |   T   |   U   |   U
  * +------+-------+-------+------- 
  * </PRE> 
  *
  * <P>The definition of the NOT operator
  *
  * <PRE>
  * | NOT
  * +------+------
  * |  T   |   F
  * |  F   |   T
  * |  U   |   U
  * +------+-------
  * </PRE>
  *
  * <P>When used in a message selector JMSDeliveryMode is treated as having 
  *    the values `PERSISTENT' and `NON_PERSISTENT'.
  *
  * <P>Although SQL supports fixed decimal comparison and arithmetic, JMS 
  *    message selectors do not. This is the reason for restricting exact 
  *    numeric literals to those without a decimal (and the addition of 
  *    numerics with a decimal as an alternate representation for an 
  *    approximate numeric values).
  *
  * <P>SQL comments are not supported.
  *
  * @version     1.0 - 6 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.BytesMessage
  * @see         javax.jms.MapMessage
  * @see         javax.jms.ObjectMessage
  * @see         javax.jms.StreamMessage
  * @see         javax.jms.TextMessage
  */

public interface Message {

    /** The default delivery mode value. */
    static final int DEFAULT_DELIVERY_MODE = -1;

    /** The default priority value. */
    static final int DEFAULT_PRIORITY = -1;

    /** The default time to live value. */
    static final int DEFAULT_TIME_TO_LIVE = -1;


    /** Get the message ID.
      *
      * <P>The messageID header field contains a value that uniquely
      * identifies each message sent by a provider.
      *  
      * <P>When a message is sent, messageID can be ignored. When
      * the send method returns it contains a provider-assigned value.
      *
      * <P>A JMSMessageID is a String value which should function as a 
      * unique key for identifying messages in a historical repository. 
      * The exact scope of uniqueness is provider defined. It should at 
      * least cover all messages for a specific installation of a 
      * provider where an installation is some connected set of message 
      * routers.
      *
      * <P>All JMSMessageID values must start with the prefix `ID:'. 
      * Uniqueness of message ID values across different providers is 
      * not required.
      *
      * <P>Since message ID's take some effort to create and increase a
      * message's size, some JMS providers may be able to optimize message
      * overhead if they are given a hint that message ID is not used by
      * an application. JMS message Producers provide a hint to disable
      * message ID. When a client sets a Producer to disable message ID
      * they are saying that they do not depend on the value of message
      * ID for the messages it produces. These messages must either have
      * message ID set to null or, if the hint is ignored, messageID must
      * be set to its normal unique value.
      *
      * @return the message ID
      *
      * @exception JMSException if JMS fails to get the message Id 
      *                         due to internal JMS error.
      * @see javax.jms.Message#setJMSMessageID()
      */ 
 
    String
    getJMSMessageID() throws JMSException;


    /** Set the message ID.
      *  
      * <P>Providers set this field when a message is sent. This operation
      * can be used to change the value of a message that's been received.
      *
      * @param id the ID of the message
      *
      * @exception JMSException if JMS fails to set the message Id 
      *                         due to internal JMS error.
      *
      * @see javax.jms.Message#getJMSMessageID()
      */ 

    void
    setJMSMessageID(String id) throws JMSException;


    /** Get the message timestamp.
      *  
      * <P>The JMSTimestamp header field contains the time a message was 
      * handed off to a provider to be sent. It is not the time the 
      * message was actually transmitted because the actual send may occur 
      * later due to transactions or other client side queueing of messages.
      *
      * <P>When a message is sent, JMSTimestamp is ignored. When the send 
      * method returns it contains a a time value somewhere in the interval 
      * between the call and the return. It is in the format of a normal 
      * Java millis time value.
      *
      * <P>Since timestamps take some effort to create and increase a 
      * message's size, some JMS providers may be able to optimize message 
      * overhead if they are given a hint that timestamp is not used by an 
      * application. JMS message Producers provide a hint to disable 
      * timestamps. When a client sets a producer to disable timestamps 
      * they are saying that they do not depend on the value of timestamp 
      * for the messages it produces. These messages must either have 
      * timestamp set to null or, if the hint is ignored, timestamp must 
      * be set to its normal value.
      *
      * @return the message timestamp
      *
      * @exception JMSException if JMS fails to get the Timestamp
      *                         due to internal JMS error.
      *
      * @see javax.jms.Message#setJMSTimestamp()
      */

    long
    getJMSTimestamp() throws JMSException;


    /** Set the message timestamp.
      *  
      * <P>Providers set this field when a message is sent. This operation
      * can be used to change the value of a message that's been received.
      *
      * @param timestamp the timestamp for this message
      *  
      * @exception JMSException if JMS fails to set the timestamp
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#getJMSTimestamp()
      */

    void
    setJMSTimestamp(long timestamp) throws JMSException;


    /** Get the correlation ID as an array of bytes for the message.
      *  
      * <P>The use of a byte[] value for JMSCorrelationID is non-portable.
      *
      * @return the correlation ID of a message as an array of bytes.
      *
      * @exception JMSException if JMS fails to get correlationId
      *                         due to some internal JMS error.
      *  
      * @see javax.jms.Message#setJMSCorrelationID()
      * @see javax.jms.Message#getJMSCorrelationID()
      * @see javax.jms.Message#setJMSCorrelationIDAsBytes()
      */

    byte []
    getJMSCorrelationIDAsBytes() throws JMSException;


    /** Set the correlation ID as an array of bytes for the message.
      *  
      * <P>If a provider supports the native concept of correlation id, a 
      * JMS client may need to assign specific JMSCorrelationID values to 
      * match those expected by non-JMS clients. JMS providers without native 
      * correlation id values are not required to support this (and the
      * corresponding get) method; their implementation may throw 
      * java.lang.UnsupportedOperationException). 
      *
      * <P>The use of a byte[] value for JMSCorrelationID is non-portable.
      *
      * @param correlationID the correlation ID value as an array of bytes.
      *  
      * @exception JMSException if JMS fails to set correlationId
      *                         due to some internal JMS error.
      *  
      * @see javax.jms.Message#setJMSCorrelationID()
      * @see javax.jms.Message#getJMSCorrelationID()
      * @see javax.jms.Message#getJMSCorrelationIDAsBytes()
      */

    void
    setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException;


    /** Set the correlation ID for the message.
      *  
      * <P>A client can use the JMSCorrelationID header field to link one
      * message with another. A typically use is to link a response message
      * with its request message.
      *  
      * <P>JMSCorrelationID can hold one of the following:
      *    <UL>
      *      <LI>A provider-specific message ID
      *      <LI>An application-specific String
      *      <LI>A provider-native byte[] value.
      *    </UL>
      *  
      * <P>Since each message sent by a JMS provider is assigned a message ID
      * value it is convenient to link messages via message ID. All message ID
      * values must start with the `ID:' prefix.
      *  
      * <P>In some cases, an application (made up of several clients) needs to
      * use an application specific value for linking messages. For instance,
      * an application may use JMSCorrelationID to hold a value referencing
      * some external information. Application specified values must not start
      * with the `ID:' prefix; this is reserved for provider-generated message
      * ID values.
      *  
      * <P>If a provider supports the native concept of correlation ID, a JMS
      * client may need to assign specific JMSCorrelationID values to match
      * those expected by non-JMS clients. A byte[] value is used for this
      * purpose. JMS providers without native correlation ID values are not
      * required to support byte[] values. The use of a byte[] value for
      * JMSCorrelationID is non-portable.
      *  
      * @param correlationID the message ID of a message being referred to.
      *  
      * @exception JMSException if JMS fails to set correlationId
      *                         due to some internal JMS error.
      *  
      * @see javax.jms.Message#getJMSCorrelationID()
      * @see javax.jms.Message#getJMSCorrelationIDAsBytes()
      * @see javax.jms.Message#setJMSCorrelationIDAsBytes()
      */ 

    void
    setJMSCorrelationID(String correlationID) throws JMSException;


    /** Get the correlation ID for the message.
      *  
      * <P>This method is used to return correlation id values that are 
      * either provider-specific message ID's or application-specific Strings.
      *
      * @return the correlation ID of a message as a String.
      *
      * @exception JMSException if JMS fails to get correlationId
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#setJMSCorrelationID()
      * @see javax.jms.Message#getJMSCorrelationIDAsBytes()
      * @see javax.jms.Message#setJMSCorrelationIDAsBytes()
      */ 
 
    String
    getJMSCorrelationID() throws JMSException;


    /** Get where a reply to this message should be sent.
      *  
      * @return where to send a response to this message
      *
      * @exception JMSException if JMS fails to get ReplyTo Destination
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#setJMSReplyTo()
      */ 
 
    Destination
    getJMSReplyTo() throws JMSException;


    /** Set where a reply to this message should be sent.
      *  
      * <P>The replyTo header field contains the destination where a reply 
      * to the current message should be sent. If it is null no reply is 
      * expected. The destination may be either a Queue or a Topic.
      *
      * <P>Messages with a null replyTo value are called JMS datagrams. 
      * Datagrams may be a notification of some change in the sender (i.e. 
      * they signal a sender event) or they may just be some data the sender 
      * thinks is of interest.
      *
      * Messages with a replyTo value are typically expecting a response. 
      * A response may be optional, it is up to the client to decide. These 
      * messages are called JMS requests. A message sent in response to a 
      * request is called a reply.
      *
      * In some cases a client may wish to match up a request it sent earlier 
      * with a reply it has just received. This can be done using the 
      * correlationID.
      *
      * @param replyTo where to send a response to this message
      *
      * @exception JMSException if JMS fails to set ReplyTo Destination
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#getJMSReplyTo()
      */ 

    void
    setJMSReplyTo(Destination replyTo) throws JMSException;


    /** Get the destination for this message.
      *  
      * <P>The destination field contains the destination to which the
      * message is being sent.
      *  
      * <P>When a message is sent this value is ignored. After completion
      * of the send method it holds the destination specified by the send.
      *  
      * <P>When a message is received, its destination value must be
      * equivalent to the value assigned when it was sent.
      *
      * @return the destination of this message.
      *  
      * @exception JMSException if JMS fails to get JMS Destination
      *                         due to some internal JMS error.
      *  
      * @see javax.jms.Message#setJMSDestination()
      */ 

    Destination
    getJMSDestination() throws JMSException;


    /** Set the destination for this message.
      *  
      * <P>Providers set this field when a message is sent. This operation 
      * can be used to change the value of a message that's been received.
      *
      * @param destination the destination for this message.
      *  
      * @exception JMSException if JMS fails to set JMS Destination
      *                         due to some internal JMS error.
      *  
      * @see javax.jms.Message#getJMSDestination()
      */ 

    void
    setJMSDestination(Destination destination) throws JMSException;


    /** Get the delivery mode for this message.
      *  
      * @return the delivery mode of this message.
      *  
      * @exception JMSException if JMS fails to get JMS DeliveryMode
      *                         due to some internal JMS error.
      *  
      * @see javax.jms.Message#setJMSDeliveryMode()
      * @see javax.jms.DeliveryMode
      */ 
 
    int
    getJMSDeliveryMode() throws JMSException;
 
 
    /** Set the delivery mode for this message.
      *  
      * <P>Providers set this field when a message is sent. This operation 
      * can be used to change the value of a message that's been received.
      *
      * @param deliveryMode the delivery mode for this message.
      *  
      * @exception JMSException if JMS fails to set JMS DeliveryMode
      *                         due to some internal JMS error.
      *  
      * @see javax.jms.Message#getJMSDeliveryMode()
      * @see javax.jms.DeliveryMode
      */ 
 
    void 
    setJMSDeliveryMode(int deliveryMode) throws JMSException;


    /** Get an indication of whether this message is being redelivered.
      *
      * <P>If a client receives a message with the redelivered indicator set,
      * it is likely, but not guaranteed, that this message was delivered to
      * the client earlier but the client did not acknowledge its receipt at
      * that earlier time.
      *
      * @return set to true if this message is being redelivered.
      *  
      * @exception JMSException if JMS fails to get JMS Redelivered flag
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#setJMSRedelivered()
      */ 
 
    boolean
    getJMSRedelivered() throws JMSException;
 
 
    /** Set to indicate whether this message is being redelivered.
      *  
      * <P>This field is set at the time the message is delivered. This
      * operation can be used to change the value of a message that's
      * been received.
      *
      * @param redelivered an indication of whether this message is being
      * redelivered.
      *  
      * @exception JMSException if JMS fails to set JMS Redelivered flag
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#getJMSRedelivered()
      */ 
 
    void
    setJMSRedelivered(boolean redelivered) throws JMSException;


    /** Get the message type.
      *
      * @return the message type
      *  
      * @exception JMSException if JMS fails to get JMS message type
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#setJMSType()
      */
       
    String
    getJMSType() throws JMSException;


    /** Set the message type.
      *
      * <P>Some JMS providers use a message repository that contains the 
      * definition of messages sent by applications. The type header field 
      * contains the name of a message's definition.
      *
      * <P>JMS does not define a standard message definition repository nor 
      * does it define a naming policy for the definitions it contains. JMS 
      * clients should use symbolic values for type that can be configured 
      * at installation time to the values defined in the current providers 
      * message repository.
      *
      * <P>JMS clients should assign a value to type whether the application 
      * makes use of it or not. This insures that it is properly set for 
      * those providers that require it.
      *
      * @param type the class of message
      *  
      * @exception JMSException if JMS fails to set JMS message type
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#getJMSType()
      */

    void 
    setJMSType(String type) throws JMSException;


    /** Get the message's expiration value.
      *  
      * <P>When a message is sent, expiration is left unassigned. After
      * completion of the send method, it holds the expiration time of the
      * message. This is the sum of the time-to-live value specified by the
      * client and the GMT at the time of the send.
      *
      * <P>If the time-to-live is specified as zero, expiration is set to
      * zero which indicates the message does not expire.
      *
      * <P>When a message's expiration time is reached, a provider should
      * discard it. JMS does not define any form of notification of message
      * expiration.
      *
      * <P>Clients should not receive messages that have expired; however,
      * JMS does not guarantee that this will not happen.
      *
      * @return the time the message expires. It is the sum of the
      * time-to-live value specified by the client, and the GMT at the
      * time of the send.
      *  
      * @exception JMSException if JMS fails to get JMS message expiration
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#setJMSExpiration()
      */ 
 
    long
    getJMSExpiration() throws JMSException;
 
 
    /** Set the message's expiration value.
      *
      * <P>Providers set this field when a message is sent. This operation 
      * can be used to change the value of a message that's been received.
      *  
      * @param expiration the message's expiration time
      *  
      * @exception JMSException if JMS fails to set JMS message expiration
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#getJMSExpiration() 
      */
 
    void
    setJMSExpiration(long expiration) throws JMSException;


    /** Get the message priority.
      *  
      * <P>JMS defines a ten level priority value with 0 as the lowest
      * priority and 9 as the highest. In addition, clients should consider
      * priorities 0-4 as gradations of normal priority and priorities 5-9
      * as gradations of expedited priority.
      *  
      * <P>JMS does not require that a provider strictly implement priority
      * ordering of messages; however, it should do its best to deliver 
      * expedited messages ahead of normal messages.
      *  
      * @return the default message priority
      *  
      * @exception JMSException if JMS fails to get JMS message priority
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#setJMSPriority() 
      */ 

    int
    getJMSPriority() throws JMSException;


    /** Set the priority for this message.
      *  
      * <P>Providers set this field when a message is sent. This operation 
      * can be used to change the value of a message that's been received.
      *
      * @param priority the priority of this message
      *  
      * @exception JMSException if JMS fails to set JMS message priority
      *                         due to some internal JMS error.
      *
      * @see javax.jms.Message#getJMSPriority() 
      */ 

    void
    setJMSPriority(int priority) throws JMSException;


    /** Clear a message's properties.
      *
      * @exception JMSException if JMS fails to clear JMS message 
      *                         properties due to some internal JMS
      *                         error.
      */ 

    void
    clearProperties() throws JMSException;


    /** Check if a property value exists.
      *
      * @param name the name of the property to test
      *
      * @return true if the property does exist.
      *  
      * @exception JMSException if JMS fails to  check if property
      *                         exists due to some internal JMS
      *                         error.
      */

    boolean
    propertyExists(String name) throws JMSException;


    /** Return the boolean property value with the given name.
      *  
      * @param name the name of the boolean property
      *  
      * @return the boolean property value with the given name.
      *  
      * @exception JMSException if JMS fails to  get Property due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid. 
      */ 

    boolean
    getBooleanProperty(String name) throws JMSException;


    /** Return the byte property value with the given name.
      *  
      * @param name the name of the byte property
      *  
      * @return the byte property value with the given name.
      *  
      * @exception JMSException if JMS fails to  get Property due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid. 
      */ 

    byte
    getByteProperty(String name) throws JMSException;


    /** Return the short property value with the given name.
      *
      * @param name the name of the short property
      *
      * @return the short property value with the given name.
      *
      * @exception JMSException if JMS fails to  get Property due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.
      */
   
    short
    getShortProperty(String name) throws JMSException;
 
 
    /** Return the integer property value with the given name.
      *  
      * @param name the name of the integer property
      *  
      * @return the integer property value with the given name.
      *  
      * @exception JMSException if JMS fails to  get Property due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.
      */ 

    int
    getIntProperty(String name) throws JMSException;


    /** Return the long property value with the given name.
      *  
      * @param name the name of the long property
      *  
      * @return the long property value with the given name.
      *  
      * @exception JMSException if JMS fails to  get Property due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.
      */ 

    long
    getLongProperty(String name) throws JMSException;


    /** Return the float property value with the given name.
      *  
      * @param name the name of the float property
      *  
      * @return the float property value with the given name.
      *  
      * @exception JMSException if JMS fails to  get Property due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.
      */ 

    float
    getFloatProperty(String name) throws JMSException;


    /** Return the double property value with the given name.
      *  
      * @param name the name of the double property
      *  
      * @return the double property value with the given name.
      *  
      * @exception JMSException if JMS fails to  get Property due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.
      */ 

    double
    getDoubleProperty(String name) throws JMSException;


    /** Return the String property value with the given name.
      *  
      * @param name the name of the String property
      *  
      * @return the String property value with the given name. If there 
      * is no property by this name, a null value is returned.
      *  
      * @exception JMSException if JMS fails to  get Property due to
      *                         some internal JMS error.
      * @exception MessageFormatException if this type conversion is invalid.
      */ 

    String
    getStringProperty(String name) throws JMSException;


    /** Return the Java object property value with the given name.
      *  
      * <P>Note that this method can be used to return in objectified format,
      * an object that had been stored as a property in the Message with the 
      * equivalent <CODE>setObject</CODE> method call, or it's equivalent 
      * primitive set<type> method.
      *  
      * @param name the name of the Java object property
      *  
      * @return the Java object property value with the given name, in 
      * objectified format (ie. if it set as an int, then a Integer is 
      * returned). If there is no property by this name, a null value 
      * is returned.
      *  
      * @exception JMSException if JMS fails to  get Property due to
      *                         some internal JMS error.
      */ 

    Object
    getObjectProperty(String name) throws JMSException;


    /** Return an Enumeration of all the property names.
      *  
      * @return an enumeration of all the names of property values.
      *  
      * @exception JMSException if JMS fails to  get Property names due to
      *                         some internal JMS error.
      */ 
     
    Enumeration
    getPropertyNames() throws JMSException;


    /** Set a boolean property value with the given name, into the Message.
      *
      * @param name the name of the boolean property
      * @param value the boolean property value to set in the Message.
      *  
      * @exception JMSException if JMS fails to  set Property due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if properties are read-only
      */ 

    void
    setBooleanProperty(String name, boolean value)
                        throws JMSException;


    /** Set a byte property value with the given name, into the Message.
      *  
      * @param name the name of the byte property
      * @param value the byte property value to set in the Message.
      *  
      * @exception JMSException if JMS fails to  set Property due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if properties are read-only
      */ 

    void
    setByteProperty(String name, byte value)
                        throws JMSException;


    /** Set a short property value with the given name, into the Message.
      *  
      * @param name the name of the short property
      * @param value the short property value to set in the Message.
      *  
      * @exception JMSException if JMS fails to  set Property due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if properties are read-only      */ 

    void
    setShortProperty(String name, short value)
                        throws JMSException;


    /** Set an integer property value with the given name, into the Message.
      *  
      * @param name the name of the integer property
      * @param value the integer property value to set in the Message.
      *  
      * @exception JMSException if JMS fails to  set Property due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if properties are read-only      */ 

    void
    setIntProperty(String name, int value)
                        throws JMSException;


    /** Set a long property value with the given name, into the Message.
      *  
      * @param name the name of the long property
      * @param value the long property value to set in the Message.
      *  
      * @exception JMSException if JMS fails to  set Property due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if properties are read-only      */ 

    void
    setLongProperty(String name, long value)
                        throws JMSException;


    /** Set a float property value with the given name, into the Message.
      *  
      * @param name the name of the float property
      * @param value the float property value to set in the Message.
      *  
      * @exception JMSException if JMS fails to  set Property due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if properties are read-only      */ 

    void
    setFloatProperty(String name, float value)
                        throws JMSException;


    /** Set a double property value with the given name, into the Message.
      *  
      * @param name the name of the double property
      * @param value the double property value to set in the Message.
      *  
      * @exception JMSException if JMS fails to  set Property due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if properties are read-only      */ 

    void
    setDoubleProperty(String name, double value)
                        throws JMSException;


    /** Set a String property value with the given name, into the Message.
      *
      * @param name the name of the String property
      * @param value the String property value to set in the Message.
      *  
      * @exception JMSException if JMS fails to  set Property due to
      *                         some internal JMS error.
      * @exception MessageNotWriteableException if properties are read-only     */ 

    void
    setStringProperty(String name, String value)
                        throws JMSException;


    /** Set a Java object property value with the given name, into the Message.
      *  
      * <P>Note that this method only works for the objectified primitive
      * object types (Integer, Double, Long ...) and String's.
      *  
      * @param name the name of the Java object property.
      * @param value the Java object property value to set in the Message.
      *  
      * @exception JMSException if JMS fails to  set Property due to
      *                         some internal JMS error.
      * @exception MessageFormatException if object is invalid
      * @exception MessageNotWriteableException if properties are read-only      */ 

    void
    setObjectProperty(String name, Object value)
                        throws JMSException;


    /** Acknowledge this and all previous messages received.
      *  
      * <P>All JMS messages support the acknowledge() method for use when a 
      * client has specified that a JMS consumers messages are to be 
      * explicitly acknowledged.
      *
      * <P>JMS defaults to implicit message acknowledgement. In this mode, 
      * calls to acknowledge() are ignored.
      *
      * <P>Acknowledgment of a message automatically acknowledges all 
      * messages previously received by the consumer. Clients may 
      * individually acknowledge messages or they may choose to acknowledge 
      * messages in application defined groups (which is done by acknowledging 
      * the last received message in the group).
      *
      * <P>Messages that have been received but not acknowledged may be 
      * redelivered to the consumer.
      *
      * @exception JMSException if JMS fails to acknowledge due to some 
      *                         internal JMS error.
      */ 

    void
    acknowledge() throws JMSException;


    /** Clear out the message body. All other parts of the message are left
      * untouched.
      *
      * @exception JMSException if JMS fails to due to some internal JMS error.
      */

    void
    clearBody() throws JMSException;
}
