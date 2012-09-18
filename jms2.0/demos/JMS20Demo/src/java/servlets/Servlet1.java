/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ndeakin
 */
@WebServlet(name = "Servlet1", urlPatterns = {"/Servlet1"})
public class Servlet1 extends HttpServlet {
    
    @EJB private JavaEESenderOld javaEESenderOld;
    @EJB private JavaEESenderNew javaEESenderNew;   
    @EJB private JavaEESenderNewCDI javaEESenderNewCDI;
    
    @EJB private JavaEESenderOldWithProperties javaEESenderOldWithProperties;
    @EJB private JavaEESenderNewWithProperties javaEESenderNewWithProperties;   
    @EJB private JavaEESenderNewCDIWithProperties javaEESenderNewCDIWithProperties;
    
    @EJB private JavaEESyncReceiverOld javaEESyncReceiverOld;
    @EJB private JavaEESyncReceiverNew javaEESyncReceiverNew;
    @EJB private JavaEESyncReceiverNewCDI javaEESyncReceiverNewCDI;
    @EJB private JavaEESyncReceiverNewCDIWithProperties javaEESyncReceiverNewCDIWithProperties;


    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<h1>Servlet Servlet1 at " + request.getContextPath () + " with option="+option+"</h1>");
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
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
                javaEESenderOld.sendMessageOld("Hello world: this is JavaEESenderOld");
                out.println("Message successfully sent");
                break;
            case "JavaEESenderNew":
                javaEESenderNew.sendMessageNew("Hello world: this is JavaEESenderNew");
                out.println("Message successfully sent");
                break;
            case "JavaEESenderNewCDI":
                javaEESenderNewCDI.sendMessageNew("Hello world: this is JavaEESenderNewCDI");
                out.println("Message successfully sent");                
                break;   
            case "JavaEESenderOldWithProperties":
                javaEESenderOldWithProperties.sendMessageOld("Hello world: this is JavaEESenderOldWithProperties");
                out.println("Message successfully sent");
                break;
            case "JavaEESenderNewWithProperties":
                javaEESenderNewWithProperties.sendMessageNew("Hello world: this is JavaEESenderNewWithProperties");
                out.println("Message successfully sent");
                break;
            case "JavaEESenderNewCDIWithProperties":
                javaEESenderNewCDIWithProperties.sendMessageNew("Hello world:: this is JavaEESenderNewCDIWithProperties");
                out.println("Message successfully sent");                
                break;                 
            case "JavaEESyncReceiverOld":
                result = javaEESyncReceiverOld.receiveMessageOld();
                out.println("<p> "+result);
                break;
            case "JavaEESyncReceiverNew":
                result = javaEESyncReceiverNew.receiveMessageNew();
                out.println("<p> "+result);
                break;
            case "JavaEESyncReceiverNewCDI":
                result = javaEESyncReceiverNewCDI.receiveMessageNew();
                out.println("<p> "+result);
                break;
           case "JavaEESyncReceiverNewCDIWithProperties":
                result = javaEESyncReceiverNewCDIWithProperties.receiveMessageNew();
                out.println("<p> "+result);
                break;
            default:
                throw new Exception("Unexpected option "+option);
        }
    
    }
}
