<%-- 
    Document   : index
    Created on : 17-Jan-2012, 14:07:42
    Author     : ndeakin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Sending a message (Java EE)</h1>
        <p><a href="Servlet1?option=JavaEESenderOld">Example using the standard API (JavaEESenderOld) - Java EE 6 only</a>
        <p><a href="Servlet1?option=JavaEESenderNew">Example using the simplified API (JavaEESenderNew) - Java EE 6 only</a>
        <p><a href="Servlet1?option=JavaEESenderNewCDI">Example using the simplified API and injection (JavaEESenderNewCDI)</a>
        <h1>Sending a message with delivery options and properties(Java EE)</h1>
        <p><a href="Servlet1?option=JavaEESenderOldWithProperties">Example using the standard API (JavaEESenderOldWithProperties) - Java EE 6 only</a>
        <p><a href="Servlet1?option=JavaEESenderNewWithProperties">Example using the simplified API (JavaEESenderNewWithProperties) - Java EE 6 only</a>
        <p><a href="Servlet1?option=JavaEESenderNewCDIWithProperties">Example using the simplified API and injection (JavaEESenderNewCDIWithProperties)</a>
        <h1>Receiving a message synchronously (Java EE)</h1>
        <p><a href="Servlet1?option=JavaEESyncReceiverOld">Example using the standard API (JavaEESyncReceiverOld) - Java EE 6 only</a>
        <p><a href="Servlet1?option=JavaEESyncReceiverNew">Example using the simplified API (JavaEESyncReceiverNew) - Java EE 6 only</a>
        <p><a href="Servlet1?option=JavaEESyncReceiverNewCDI">Example using the simplified API and injection (JavaEESyncReceiverNewCDI)</a>
        <p><a href="Servlet1?option=JavaEESyncReceiverNewCDIWithProperties">Example using the simplified API and injection (JavaEESyncReceiverNewCDIWithProperties)</a>
    </body>
</html>
