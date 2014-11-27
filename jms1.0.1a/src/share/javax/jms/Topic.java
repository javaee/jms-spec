
/* 
 * @(#)Topic.java	1.9 98/09/30
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


/** A Topic object encapsulates a provider-specific topic name. It is the 
  * way a client specifies the identity of a topic to JMS methods.
  *
  * <P>Many Pub/Sub providers group topics into hierarchies and provide 
  * various options for subscribing to parts of the hierarchy. JMS places 
  * no restriction on what a Topic object represents. It might be a leaf 
  * in a topic hierarchy or it might be a larger part of the hierarchy.
  *
  * <P>The organization of topics and the granularity of subscriptions to 
  * them is an important part of a Pub/Sub application's architecture. JMS 
  * does not specify a policy for how this should be done. If an application 
  * takes advantage of a provider-specific topic grouping mechanism, it 
  * should document this. If the application is installed using a different 
  * provider, it is the job of the administrator to construct an equivalent 
  * topic architecture and create equivalent Topic objects.
  *
  * @version     1.0 - 13 March 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  *
  * @see         javax.jms.Destination
  */
 
public interface Topic extends Destination {

    /** Get the name of this topic.
      *  
      * <P>Clients that depend upon the name, are not portable.
      *  
      * @return the topic name
      *  
      * @exception JMSException if JMS implementation for Topic fails to
      *                         to return topic name due to some internal
      *                         error.
      */ 

    String
    getTopicName() throws JMSException;


    /** Return a pretty printed version of the topic name.
      *
      * @return the provider specific identity values for this topic.
      */

    String
    toString();
}
