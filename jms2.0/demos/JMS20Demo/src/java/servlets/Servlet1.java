/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package servlets;

import beans.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.jms.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@JMSDestinationDefinition(
    name = "java:global/jms/demoQueue",
    description = "Queue to use in demonstration", 
    className = "javax.jms.Queue", 
    destinationName="demoQueue")

@JMSConnectionFactoryDefinition(
    name="java:global/jms/demoConnectionFactory",
    className= "javax.jms.ConnectionFactory",
    description="ConnectionFactory to use in demonstration"
)       

@WebServlet(name = "Servlet1", urlPatterns = {"/Servlet1"})
public class Servlet1 extends HttpServlet {
    
    @EJB private JavaEESenderOld javaEESenderOld;
    @EJB private JavaEESenderNew javaEESenderNew;   
    @EJB private JavaEESenderNewCDI javaEESenderNewCDI;
    
    @EJB private JavaEESenderOldWithProperties javaEESenderOldWithProperties;
    @EJB private JavaEESenderNewCDIWithProperties javaEESenderNewCDIWithProperties;
    
    @EJB private JavaEESyncReceiverOld javaEESyncReceiverOld;
    @EJB private JavaEESyncReceiverNew javaEESyncReceiverNew;
    @EJB private JavaEESyncReceiverNewCDI javaEESyncReceiverNewCDI;
    @EJB private JavaEESyncReceiverNewCDIWithProperties javaEESyncReceiverNewCDIWithProperties;

    // Inject a JMSContext to use - this will use the platform default connection factory
    @Inject JMSContext context;
    
    // Inject a Queue object to use
    @Resource(lookup = "java:global/jms/demoQueue")
    Queue demoQueue;

    /** 
     * Processes requests for both HTTP {@code GET} and {@code POST} methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String option = "";
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Servlet1</title>");  
            out.println("</head>");
            out.println("<body>");
            
            option = request.getParameter("option");
            out.println("<p>Servlet Servlet1 at " + request.getContextPath () + " with option="+option+"</h1>");
            handle(option,out);
        } catch (Exception e){
            e.printStackTrace(out);
        } finally {               
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP {@code GET} method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP {@code POST} method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void handle(String option,PrintWriter out) throws Exception {
        String result = "";
        switch(option) {
            case "JavaEESenderOld":
                out.println("<h1>Using the JMS 1.1-style API<br> to send a message (JavaEESenderOld)</h1>");
                out.println("<p>Number of messages on queue before: "+ getQueueDepth()+"<br/>");
                javaEESenderOld.sendMessageOld("JavaEESenderOld");
                out.println("Message successfully sent using JavaEESenderOld<br/>");
                out.println("Number of messages on queue after: "+ getQueueDepth()+"<br/>");
                break;
            case "JavaEESenderNew":
                out.println("<h1>Using the JMS 2.0 simplified API<br> to send a message (JavaEESenderNew)</h1>");
                out.println("Number of messages on queue before: "+ getQueueDepth()+"<br/>");
                javaEESenderNew.sendMessageNew("JavaEESenderNew");
                out.println("Message successfully sent<br/>");
                out.println("Number of messages on queue after: "+ getQueueDepth()+"<br/>");
                break;
            case "JavaEESenderNewCDI":
                out.println("<h1>Using the JMS 2.0 simplified API and injection<br> to send a message (JavaEESenderNewCDI)</h1>");
                out.println("<p>Number of messages on queue before: "+ getQueueDepth()+"<br/>");
                javaEESenderNewCDI.sendMessageNewCDI("JavaEESenderNewCDI");
                out.println("Message successfully sent<br/>");    
                out.println("Number of messages on queue after: "+ getQueueDepth()+"<br/>");
                break;   
            case "JavaEESenderOldWithProperties":
                out.println("<h1>Using the JMS 1.1-style API<br> to send a message,<br>setting delivery options and message properties (JavaEESenderOldWithProperties)</h1>");
                out.println("<p>Number of messages on queue before: "+ getQueueDepth()+"<br/>");
                javaEESenderOldWithProperties.sendMessageOldWithProperties("JavaEESenderOldWithProperties");
                out.println("Message successfully sent<br/>");
                out.println("Number of messages on queue after: "+ getQueueDepth()+"<br/>");
                break;
            case "JavaEESenderNewCDIWithProperties":
                out.println("<h1>Using the JMS 2.0 simplified API with injection<br> to send a message,<br>setting delivery options and message properties (JavaEESenderNewCDIWithProperties)</h1>");
                out.println("<p>Number of messages on queue before: "+ getQueueDepth()+"<br/>");
                javaEESenderNewCDIWithProperties.sendMessageNewCDIWithProperties("JavaEESenderNewCDIWithProperties");
                out.println("Message sent<br/>");     
                out.println("Number of messages on queue after: "+ getQueueDepth()+"<br/>");
                break;                 
            case "JavaEESyncReceiverOld":
                out.println("<h1>Using the JMS 1.1-style API<br> to receive a message (JavaEESyncReceiverOld)</h1>");
                out.println("<p>Number of messages on queue before: "+ getQueueDepth()+"<br/>");
                result = javaEESyncReceiverOld.receiveMessageOld();
                out.println("Message received: "+result+"<br/>"); 
                out.println("Number of messages on queue after: "+ getQueueDepth()+"<br/>");
                break;
            case "JavaEESyncReceiverNew":
                out.println("<h1>Using the JMS 2.0 simplified API<br> to receive a message (JavaEESyncReceiverNew)</h1>");
                out.println("<p>Number of messages on queue before: "+ getQueueDepth()+"<br/>");
                result = javaEESyncReceiverNew.receiveMessageNew();
                out.println("Message received: "+result+"<br/>"); 
                out.println("Number of messages on queue after: "+ getQueueDepth()+"<br/>");
                break;
            case "JavaEESyncReceiverNewCDI":
                out.println("<h1>Using the JMS 2.0 simplified API and injection<br> to receive a message (JavaEESyncReceiverNewCDI)</h1>");
                out.println("<p>Number of messages on queue before: "+ getQueueDepth()+"<br/>");
                result = javaEESyncReceiverNewCDI.receiveMessageNewCDI();
                out.println("Message received: "+result+"<br/>"); 
                out.println("Number of messages on queue after: "+ getQueueDepth()+"<br/>");
                break;
           case "JavaEESyncReceiverNewCDIWithProperties":
                out.println("<h1>Using the JMS 2.0 simplified API and injection<br> to receive a message,<br>displaying message properties (JavaEESyncReceiverNewCDIWithProperties)</h1>");
                out.println("<p>Number of messages on queue before: "+ getQueueDepth()+"<br/>");
                result = javaEESyncReceiverNewCDIWithProperties.receiveMessageNewCDIWithProperties();
                out.println("Message received: "+result+"<br/>"); 
                out.println("Number of messages on queue after: "+ getQueueDepth()+"<br/>");
                break;
            default:
                throw new Exception("Unexpected option "+option);
        }
        out.println("<br/><br/><img src='arrow.gif'>&nbsp;<a href='/JMS20Demo/"+option+".html'>Now go back to the example to continue</a>");    
        out.println("<br/><br/><a href='/JMS20Demo/'>JMS 2.0 examples home</a>");    
    }
    
    String getQueueDepth() throws JMSException{
        int numMessages=0;
        for (Enumeration queueEnumeration = context.createBrowser(demoQueue).getEnumeration(); queueEnumeration.hasMoreElements();) {
            queueEnumeration.nextElement();
            numMessages++;
        } 
        return "<b>"+numMessages+"</b>";

    }
}
