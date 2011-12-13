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
    
    /** Creates a MessagingContext with the default user identity
     * and an unspecified sessionMode. 
     * 
     * Since no sessionMode is specified, this method should only
     * be used in a Java EE application with a defined transactional context.
     * 
     * A connection and session are created for use by the new MessagingContext. 
     * The connection is created in stopped mode but will be automatically started
     * when a SyncMessageConsumer is created or a MessageListener registered.
     *
     * @return a newly created MessagingContext
     *
     * @exception JMSRuntimeException if the JMS provider fails to create the
     *                         MessagingContext due to some internal error.
     * @exception JMSSecurityRuntimeException  if client authentication fails due to 
     *                         an invalid user name or password.
     * @since 2.0 
    */ 
   MessagingContext createMessagingContext();
   
   /** Creates a MessagingContext with the specified user identity
    * and an unspecified sessionMode. 
    * 
    * Since no sessionMode is specified, this method should only
    * be used in a Java EE application with a defined transactional context.
    * 
    * A connection and session are created for use by the new MessagingContext. 
    * The connection is created in stopped mode but will be automatically started
    * when a SyncMessageConsumer is created or a MessageListener registered.
    *  
    * @param userName the caller's user name
    * @param password the caller's password
    *  
    * @return a newly created MessagingContext
    *
    * @exception JMSRuntimeException if the JMS provider fails to create the 
    *                         MessagingContext due to some internal error.
    * @exception JMSSecurityRuntimeException  if client authentication fails due to 
    *                         an invalid user name or password.
    * @since 2.0  
    */ 
  MessagingContext createMessagingContext(String userName, String password);    

   /** Creates a MessagingContext with the specified user identity 
     * and the specified session mode. 
     * 
     * A connection and session are created for use by the new MessagingContext. 
     * The MessagingContext is created in stopped mode but will be automatically started
     * when a SyncMessageConsumer is created or a MessageListener registered.
     *  
     * @param userName the caller's user name
     * @param password the caller's password
     * @param sessionMode indicates whether the MessagingContext is transacted or not,
     * and if it is not, whether the consumer or the client will acknowledge any 
     * messages it receives. Legal values are <code>MessagingContext.SESSION_TRANSACTED</code>, 
     * <code>MessagingContext.AUTO_ACKNOWLEDGE</code>, 
     * <code>MessagingContext.CLIENT_ACKNOWLEDGE</code>, and 
     * <code>MessagingContext.DUPS_OK_ACKNOWLEDGE</code>.
     *  
     * @return a newly created MessagingContext
     *
     * @exception JMSRuntimeException if the JMS provider fails to create the 
     *                         MessagingContext due to some internal error.
     * @exception JMSSecurityRuntimeException  if client authentication fails due to 
     *                         an invalid user name or password.
     * @since 2.0  
     */ 
   
MessagingContext createMessagingContext(String userName, String password, int sessionMode);    
   
   /** Creates a MessagingContext with the default user identity
    * and the specified session mode. A connection and session are created.
    * 
    * A connection and session are created for use by the new MessagingContext. 
    * The MessagingContext is created in stopped mode but will be automatically started
    * when a SyncMessageConsumer is created or a MessageListener registered.
    *
    * @param sessionMode indicates whether the MessagingContext is transacted or not,
    * and if it is not, whether the consumer or the client will acknowledge any 
    * messages it receives. Legal values are <code>MessagingContext.SESSION_TRANSACTED</code>, 
    * <code>MessagingContext.AUTO_ACKNOWLEDGE</code>, 
    * <code>MessagingContext.CLIENT_ACKNOWLEDGE</code>, and 
    * <code>MessagingContext.DUPS_OK_ACKNOWLEDGE</code>.
    * 
    * @return a newly created MessagingContext
    *
    * @exception JMSRuntimeException if the JMS provider fails to create the
    *                         MessagingContext due to some internal error.
    * @exception JMSSecurityRuntimeException  if client authentication fails due to 
    *                         an invalid user name or password.
    * @since 2.0 
   */ 
  MessagingContext createMessagingContext(int sessionMode);
      
}
