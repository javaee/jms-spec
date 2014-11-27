
/*
 * @(#)DeliveryMode.java	1.4 98/08/07
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

/** Delivery modes supported by JMS.
  *
  * @version     1.0 - 7 August 1998
  * @author      Mark Hapner
  * @author      Rich Burridge
  */

public interface DeliveryMode {

    /** This is the lowest overhead delivery mode because it does not require 
      * that the message be logged to stable storage. The level of JMS provider
      * failure that causes a NON_PERSISTENT message to be lost is not defined.
      *
      * <P>A JMS provider must deliver a NON_PERSISTENT message with an 
      * at-most-once guarantee. This means it may lose the message but it 
      * must not deliver it twice.
      */

    static final int NON_PERSISTENT = 1;

    /** This mode instructs the JMS provider to log the message to stable 
      * storage as part of the client's send operation. This insures the 
      * message will survive a provider process failure.
      *
      * <P>A JMS provider must deliver a PERSISTENT message with a 
      * once-and-only-once guarantee. It must not lose it and it must not 
      * deliver it twice.
      */

    static final int PERSISTENT = 2;
}
