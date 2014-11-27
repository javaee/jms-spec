
/*
 * @(#)ConnectionMetaData.java	1.6 98/09/30
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

/** ConnectionMetaData provides information describing the Connection.
  *
  * @version     1.0 - 13 March 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  */

public interface ConnectionMetaData {

    /** Get the JMS version.
      *
      * @return the JMS version.
      *  
      * @exception JMSException if some internal error occurs in
      *                         JMS implementation during the meta-data
      *                         retrieval.
      */

    String 
    getJMSVersion() throws JMSException;


    /** Get the JMS major version number.
      *  
      * @return the JMS major version number.
      *  
      * @exception JMSException if some internal error occurs in
      *                         JMS implementation during the meta-data
      *                         retrieval.
      */

    int 
    getJMSMajorVersion() throws JMSException; 
 

    /** Get the JMS minor version number.
      *  
      * @return the JMS minor version number.
      *  
      * @exception JMSException if some internal error occurs in
      *                         JMS implementation during the meta-data
      *                         retrieval.
      */

    int  
    getJMSMinorVersion() throws JMSException;


    /** Get the JMS provider name.
      *
      * @return the JMS provider name.
      *  
      * @exception JMSException if some internal error occurs in
      *                         JMS implementation during the meta-data
      *                         retrieval.
      */ 

    String 
    getJMSProviderName() throws JMSException;


    /** Get the JMS provider version.
      *
      * @return the JMS provider version.
      *  
      * @exception JMSException if some internal error occurs in
      *                         JMS implementation during the meta-data
      *                         retrieval.
      */ 

    String 
    getProviderVersion() throws JMSException;


    /** Get the JMS provider major version number.
      *  
      * @return the JMS provider major version number.
      *  
      * @exception JMSException if some internal error occurs in
      *                         JMS implementation during the meta-data
      *                         retrieval.
      */

    int
    getProviderMajorVersion() throws JMSException; 

 
    /** Get the JMS provider minor version number.
      *  
      * @return the JMS provider minor version number.
      *  
      * @exception JMSException if some internal error occurs in
      *                         JMS implementation during the meta-data
      *                         retrieval.
      */

    int  
    getProviderMinorVersion() throws JMSException;
}
