/*
 * @(#)ConnectionFactory.java	1.11 02/04/09
 *
 * Copyright 1997-2002 Sun Microsystems, Inc. All Rights Reserved.
 *
 *  SUN PROPRIETARY/CONFIDENTIAL.
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package javax.jms;

/** A <CODE>ConnectionFactory</CODE> object encapsulates a set of connection 
  * configuration 
  * parameters that has been defined by an administrator. A client uses 
  * it to create a connection with a JMS provider.
  *
  * <P>A <CODE>ConnectionFactory</CODE> object is a JMS administered object and
  *  supports concurrent use.
  *
  * <P>JMS administered objects are objects containing configuration 
  * information that are created by an administrator and later used by 
  * JMS clients. They make it practical to administer the JMS API in the 
  * enterprise.
  *
  * <P>Although the interfaces for administered objects do not explicitly 
  * depend on the Java Naming and Directory Interface (JNDI) API, the JMS API 
  * establishes the convention that JMS clients find administered objects by 
  * looking them up in a JNDI namespace.
  *
  * <P>An administrator can place an administered object anywhere in a 
  * namespace. The JMS API does not define a naming policy.
  *
  * <P>It is expected that JMS providers will provide the tools an
  * administrator needs to create and configure administered objects in a 
  * JNDI namespace. JMS provider implementations of administered objects 
  * should be both <CODE>javax.jndi.Referenceable</CODE> and 
  * <CODE>java.io.Serializable</CODE> so that they can be stored in all 
  * JNDI naming contexts. In addition, it is recommended that these 
  * implementations follow the JavaBeans<SUP><FONT SIZE="-2">TM</FONT></SUP> 
  * design patterns.
  *
  * <P>This strategy provides several benefits:
  *
  * <UL>
  *   <LI>It hides provider-specific details from JMS clients.
  *   <LI>It abstracts administrative information into objects in the Java 
  *       programming language ("Java objects")
  *       that are easily organized and administered from a common 
  *       management console.
  *   <LI>Since there will be JNDI providers for all popular naming 
  *       services, this means that JMS providers can deliver one implementation 
  *       of administered objects that will run everywhere.
  * </UL>
  *
  * <P>An administered object should not hold on to any remote resources. 
  * Its lookup should not use remote resources other than those used by the
  * JNDI API itself.
  *
  * <P>Clients should think of administered objects as local Java objects. 
  * Looking them up should not have any hidden side effects or use surprising 
  * amounts of local resources.
  *
  * @version     1.1 - February 1, 2002
  * @author      Mark Hapner
  * @author      Rich Burridge
  * @author      Kate Stout
  *
  * @see         javax.jms.Connection
  * @see         javax.jms.QueueConnectionFactory
  * @see         javax.jms.TopicConnectionFactory
  */

public interface ConnectionFactory {
        /** Creates a connection with the default user identity.
      * The connection is created in stopped mode. No messages 
      * will be delivered until the <code>Connection.start</code> method
      * is explicitly called.
      *
      * @return a newly created connection
      *
      * @exception JMSException if the JMS provider fails to create the
      *                         connection due to some internal error.
      * @exception JMSSecurityException  if client authentication fails due to 
      *                         an invalid user name or password.
       * @since 1.1 
     */ 

    Connection
    createConnection() throws JMSException;


    /** Creates a connection with the specified user identity.
      * The connection is created in stopped mode. No messages 
      * will be delivered until the <code>Connection.start</code> method
      * is explicitly called.
      *  
      * @param userName the caller's user name
      * @param password the caller's password
      *  
      * @return a newly created  connection
      *
      * @exception JMSException if the JMS provider fails to create the 
      *                         connection due to some internal error.
      * @exception JMSSecurityException  if client authentication fails due to 
      *                         an invalid user name or password.
      * @since 1.1  
      */ 

    Connection
    createConnection(String userName, String password) 
					     throws JMSException;
    
    /** 
     * Creates a JMSContext with the default user identity
     * and an unspecified sessionMode. 
     * <p>
     * A connection and session are created for use by the new JMSContext. 
     * The connection is created in stopped mode but will be automatically started
     * when a JMSConsumer is created.
     * <p>
     * The behaviour of the session that is created depends on 
     * whether this method is called in a Java SE environment, 
     * in the Java EE application client container, or in the Java EE web or EJB container.
     * If this method is called in the Java EE web or EJB container then the 
     * behaviour of the session also depends on whether or not 
     * there is an active JTA transaction in progress.   
     * <p>
     * In a <b>Java SE environment</b> or in <b>the Java EE application client container</b>:
     * <ul>
     * <li>The session will be non-transacted and received messages will be acknowledged automatically
     * using an acknowledgement mode of <code>JMSContext.AUTO_ACKNOWLEDGE</code> 
     * For a definition of the meaning of this acknowledgement mode see the link below.
     * </ul>
     * <p>
     * In a <b>Java EE web or EJB container, when there is an active JTA transaction in progress</b>:
     * <ul>
     * <li>The session will participate in the JTA transaction and will be committed or rolled back
     * when that transaction is committed or rolled back, 
     * not by calling the <code>JMSContext</code>'s <code>commit</code> or <code>rollback</code> methods.
     * </ul>
     * <p>
     * In the <b>Java EE web or EJB container, when there is no active JTA transaction in progress</b>:
     * <ul>
     * <li>The session will be non-transacted and received messages will be acknowledged automatically
     * using an acknowledgement mode of <code>JMSContext.AUTO_ACKNOWLEDGE</code> 
     * For a definition of the meaning of this acknowledgement mode see the link below.
     * </ul> 
     *
     * @return a newly created JMSContext
     *
     * @exception JMSRuntimeException if the JMS provider fails to create the
     *                         JMSContext due to some internal error.
     * @exception JMSSecurityRuntimeException  if client authentication fails due to 
     *                         an invalid user name or password.
     * @since 2.0 
     * 
     * @see JMSContext#AUTO_ACKNOWLEDGE 
     * 
     * @see javax.jms.ConnectionFactory#createContext(int) 
     * @see javax.jms.ConnectionFactory#createContext(java.lang.String, java.lang.String) 
     * @see javax.jms.ConnectionFactory#createContext(java.lang.String, java.lang.String, int) 
     * @see javax.jms.JMSContext#createContext(int) 
     */
    JMSContext createContext();
   
    /** 
     * Creates a JMSContext with the specified user identity
     * and an unspecified sessionMode. 
     * <p>
     * A connection and session are created for use by the new JMSContext. 
     * The connection is created in stopped mode but will be automatically started
     * when a JMSConsumer.
     * <p>
     * The behaviour of the session that is created depends on 
     * whether this method is called in a Java SE environment, 
     * in the Java EE application client container, or in the Java EE web or EJB container.
     * If this method is called in the Java EE web or EJB container then the 
     * behaviour of the session also depends on whether or not 
     * there is an active JTA transaction in progress.   
     * <p>
     * In a <b>Java SE environment</b> or in <b>the Java EE application client container</b>:
     * <ul>
     * <li>The session will be non-transacted and received messages will be acknowledged automatically
     * using an acknowledgement mode of <code>JMSContext.AUTO_ACKNOWLEDGE</code> 
     * For a definition of the meaning of this acknowledgement mode see the link below.
     * </ul>
     * <p>
     * In a <b>Java EE web or EJB container, when there is an active JTA transaction in progress</b>:
     * <ul>
     * <li>The session will participate in the JTA transaction and will be committed or rolled back
     * when that transaction is committed or rolled back, 
     * not by calling the <code>JMSContext</code>'s <code>commit</code> or <code>rollback</code> methods.
     * </ul>
     * <p>
     * In the <b>Java EE web or EJB container, when there is no active JTA transaction in progress</b>:
     * <ul>
     * <li>The session will be non-transacted and received messages will be acknowledged automatically
     * using an acknowledgement mode of <code>JMSContext.AUTO_ACKNOWLEDGE</code> 
     * For a definition of the meaning of this acknowledgement mode see the link below.
     * </ul> 
     *  
     * @param userName the caller's user name
     * @param password the caller's password
     *  
     * @return a newly created JMSContext
     *
     * @exception JMSRuntimeException if the JMS provider fails to create the 
     *                         JMSContext due to some internal error.
     * @exception JMSSecurityRuntimeException  if client authentication fails due to 
     *                         an invalid user name or password.
     * @since 2.0 
     * 
     * @see JMSContext#AUTO_ACKNOWLEDGE 
     * 
     * @see javax.jms.ConnectionFactory#createContext() 
     * @see javax.jms.ConnectionFactory#createContext(int) 
     * @see javax.jms.ConnectionFactory#createContext(java.lang.String, java.lang.String, int) 
     * @see javax.jms.JMSContext#createContext(int)
     */
    JMSContext createContext(String userName, String password);    

   /** Creates a JMSContext with the specified user identity 
     * and the specified session mode. 
     * <p>
     * A connection and session are created for use by the new JMSContext. 
     * The JMSContext is created in stopped mode but will be automatically started
     * when a JMSConsumer is created.
     * <p>
     * The effect of setting the <code>sessionMode</code>  
     * argument depends on whether this method is called in a Java SE environment, 
     * in the Java EE application client container, or in the Java EE web or EJB container.
     * If this method is called in the Java EE web or EJB container then the 
     * effect of setting the <code>sessionMode</code> argument also depends on 
     * whether or not there is an active JTA transaction in progress. 
     * <p>
     * In a <b>Java SE environment</b> or in <b>the Java EE application client container</b>:
     * <ul>
     * <li>If <code>sessionMode</code> is set to <code>JMSContext.SESSION_TRANSACTED</code> then the session 
     * will use a local transaction which may subsequently be committed or rolled back 
     * by calling the <code>JMSContext</code>'s <code>commit</code> or <code>rollback</code> methods. 
     * <li>If <code>sessionMode</code> is set to any of 
     * <code>JMSContext.CLIENT_ACKNOWLEDGE</code>, 
     * <code>JMSContext.AUTO_ACKNOWLEDGE</code> or
     * <code>JMSContext.DUPS_OK_ACKNOWLEDGE</code>.
     * then the session will be non-transacted and 
     * messages received by this session will be acknowledged
     * according to the value of <code>sessionMode</code>.
     * For a definition of the meaning of these acknowledgement modes see the links below.
     * </ul>
     * <p>
     * In a <b>Java EE web or EJB container, when there is an active JTA transaction in progress</b>:
     * <ul>
     * <li>The argument <code>sessionMode</code> is ignored.
     * The session will participate in the JTA transaction and will be committed or rolled back
     * when that transaction is committed or rolled back, 
     * not by calling the <code>JMSContext</code>'s <code>commit</code> or <code>rollback</code> methods.
     * Since the argument is ignored, developers are recommended to use 
     * <code>createSession()</code>, which has no arguments, instead of this method.
     * </ul>
     * <p>
     * In the <b>Java EE web or EJB container, when there is no active JTA transaction in progress</b>:
     * <ul>
     * <li>The argument <code>acknowledgeMode</code> must be set to either of 
     * <code>JMSContext.AUTO_ACKNOWLEDGE</code> or
     * <code>JMSContext.DUPS_OK_ACKNOWLEDGE</code>.
     * The session will be non-transacted and messages received by this session will be acknowledged
     * automatically according to the value of <code>acknowledgeMode</code>.
     * For a definition of the meaning of these acknowledgement modes see the links below.
     * The values <code>JMSContext.SESSION_TRANSACTED</code> and <code>JMSContext.CLIENT_ACKNOWLEDGE</code> may not be used.
     * </ul> 
     * @param userName the caller's user name
     * @param password the caller's password
     * @param sessionMode indicates which of four possible session modes will be used.
     * <ul>
     * <li>If this method is called in a Java SE environment or in the Java EE application client container, 
     * the permitted values are 
     * <code>JMSContext.SESSION_TRANSACTED</code>, 
     * <code>JMSContext.CLIENT_ACKNOWLEDGE</code>, 
     * <code>JMSContext.AUTO_ACKNOWLEDGE</code> and
     * <code>JMSContext.DUPS_OK_ACKNOWLEDGE</code>. 
     * <li> If this method is called in the Java EE web or EJB container when there is an active JTA transaction in progress 
     * then this argument is ignored.
     * <li>If this method is called in the Java EE web or EJB container when there is no active JTA transaction in progress, the permitted values are
     * <code>JMSContext.AUTO_ACKNOWLEDGE</code> and
     * <code>JMSContext.DUPS_OK_ACKNOWLEDGE</code>.
     * In this case the values <code>JMSContext.TRANSACTED</code> and <code>JMSContext.CLIENT_ACKNOWLEDGE</code> are not permitted.
     * </ul>
     *  
     * @return a newly created JMSContext
     *
     * @exception JMSRuntimeException if the JMS provider fails to create the 
     *                         JMSContext due to some internal error.
     * @exception JMSSecurityRuntimeException  if client authentication fails due to 
     *                         an invalid user name or password.
     * @since 2.0  
     * @see JMSContext#SESSION_TRANSACTED 
     * @see JMSContext#CLIENT_ACKNOWLEDGE 
     * @see JMSContext#AUTO_ACKNOWLEDGE 
     * @see JMSContext#DUPS_OK_ACKNOWLEDGE 
     * 
     * @see javax.jms.ConnectionFactory#createContext() 
     * @see javax.jms.ConnectionFactory#createContext(int) 
     * @see javax.jms.ConnectionFactory#createContext(java.lang.String, java.lang.String) 
     * @see javax.jms.JMSContext#createContext(int) 
     */ 
    JMSContext createContext(String userName, String password, int sessionMode);    
   
   /** Creates a JMSContext with the default user identity
     * and the specified session mode. 
     * <p> 
     * A connection and session are created for use by the new JMSContext. 
     * The JMSContext is created in stopped mode but will be automatically started
     * when a JMSConsumer is created.
     * <p>
     * The effect of setting the <code>sessionMode</code>  
     * argument depends on whether this method is called in a Java SE environment, 
     * in the Java EE application client container, or in the Java EE web or EJB container.
     * If this method is called in the Java EE web or EJB container then the 
     * effect of setting the <code>sessionMode</code> argument also depends on 
     * whether or not there is an active JTA transaction in progress. 
     * <p>
     * In a <b>Java SE environment</b> or in <b>the Java EE application client container</b>:
     * <ul>
     * <li>If <code>sessionMode</code> is set to <code>JMSContext.SESSION_TRANSACTED</code> then the session 
     * will use a local transaction which may subsequently be committed or rolled back 
     * by calling the <code>JMSContext</code>'s <code>commit</code> or <code>rollback</code> methods. 
     * <li>If <code>sessionMode</code> is set to any of 
     * <code>JMSContext.CLIENT_ACKNOWLEDGE</code>, 
     * <code>JMSContext.AUTO_ACKNOWLEDGE</code> or
     * <code>JMSContext.DUPS_OK_ACKNOWLEDGE</code>.
     * then the session will be non-transacted and 
     * messages received by this session will be acknowledged
     * according to the value of <code>sessionMode</code>.
     * For a definition of the meaning of these acknowledgement modes see the links below.
     * </ul>
     * <p>
     * In a <b>Java EE web or EJB container, when there is an active JTA transaction in progress</b>:
     * <ul>
     * <li>The argument <code>sessionMode</code> is ignored.
     * The session will participate in the JTA transaction and will be committed or rolled back
     * when that transaction is committed or rolled back, 
     * not by calling the <code>JMSContext</code>'s <code>commit</code> or <code>rollback</code> methods.
     * Since the argument is ignored, developers are recommended to use 
     * <code>createSession()</code>, which has no arguments, instead of this method.
     * </ul>
     * <p>
     * In the <b>Java EE web or EJB container, when there is no active JTA transaction in progress</b>:
     * <ul>
     * <li>The argument <code>acknowledgeMode</code> must be set to either of 
     * <code>JMSContext.AUTO_ACKNOWLEDGE</code> or
     * <code>JMSContext.DUPS_OK_ACKNOWLEDGE</code>.
     * The session will be non-transacted and messages received by this session will be acknowledged
     * automatically according to the value of <code>acknowledgeMode</code>.
     * For a definition of the meaning of these acknowledgement modes see the links below.
     * The values <code>JMSContext.SESSION_TRANSACTED</code> and <code>JMSContext.CLIENT_ACKNOWLEDGE</code> may not be used.
     * </ul> 
     *
     * @param sessionMode indicates which of four possible session modes will be used.
     * <ul>
     * <li>If this method is called in a Java SE environment or in the Java EE application client container, 
     * the permitted values are 
     * <code>JMSContext.SESSION_TRANSACTED</code>, 
     * <code>JMSContext.CLIENT_ACKNOWLEDGE</code>, 
     * <code>JMSContext.AUTO_ACKNOWLEDGE</code> and
     * <code>JMSContext.DUPS_OK_ACKNOWLEDGE</code>. 
     * <li> If this method is called in the Java EE web or EJB container when there is an active JTA transaction in progress 
     * then this argument is ignored.
     * <li>If this method is called in the Java EE web or EJB container when there is no active JTA transaction in progress, the permitted values are
     * <code>JMSContext.AUTO_ACKNOWLEDGE</code> and
     * <code>JMSContext.DUPS_OK_ACKNOWLEDGE</code>.
     * In this case the values <code>JMSContext.TRANSACTED</code> and <code>JMSContext.CLIENT_ACKNOWLEDGE</code> are not permitted.
     * </ul>
     * 
     * @return a newly created JMSContext
     * 
     * @exception JMSRuntimeException if the JMS provider fails to create the
     *                         JMSContext due to some internal error.
     * @exception JMSSecurityRuntimeException  if client authentication fails due to 
     *                         an invalid user name or password.
     * @since 2.0 
     * 
     * @see JMSContext#SESSION_TRANSACTED 
     * @see JMSContext#CLIENT_ACKNOWLEDGE 
     * @see JMSContext#AUTO_ACKNOWLEDGE 
     * @see JMSContext#DUPS_OK_ACKNOWLEDGE 
     * 
     * @see javax.jms.ConnectionFactory#createContext() 
     * @see javax.jms.ConnectionFactory#createContext(java.lang.String, java.lang.String) 
     * @see javax.jms.ConnectionFactory#createContext(java.lang.String, java.lang.String, int) 
     * @see javax.jms.JMSContext#createContext(int) 
	 */
	JMSContext createContext(int sessionMode);
      
}
