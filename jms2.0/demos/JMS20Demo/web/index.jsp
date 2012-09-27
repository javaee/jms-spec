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
        <title>JMS 2.0 in Java EE 7 demonstration</title>
    </head> 
    <body>
        <h1>JMS 2.0 means less code to send or receive a message</h1>
        This demonstration allows you to examine the new JMS 2.0 API for sending and receiving messages
        and see how it compares with the existing JMS 1.1 API. <br>
        Follow the links labelled <img src="arrow.gif">&nbsp; to go through the demonstration, or simply explore the links.
        <h2>Sending a message (Java EE)</h1>
        <p>Here are three very simple examples of Java EE applications which send a message.<br> 
            Follow the links to view and run code examples.
        <p><img src="arrow.gif">&nbsp;<a href="JavaEESenderOld.html">Using the JMS 1.1-style API to send a message (JavaEESenderOld)</a>
        <p><a href="JavaEESenderNew.html">Using the JMS 2.0 simplified API to send a message (JavaEESenderNew)</a>
        <p><a href="JavaEESenderNewCDI.html">Using the JMS 2.0 simplified API and injection to send a message (JavaEESenderNewCDI)</a>
        <h2>Receiving a message synchronously (Java EE)</h1>
        <p>Here are three very simple examples of Java EE applications which synchronously receive a message. <br>
            Follow the links to view and run code examples.<br>
            Before running them, use the previous examples to put a few messages on the queue first.
        <p><a href="JavaEESyncReceiverOld.html">Using the JMS 1.1-style API to receive a message (JavaEESyncReceiverOld)</a>
        <p><a href="JavaEESyncReceiverNew.html">Using the JMS 2.0 simplified API to receive a message (JavaEESyncReceiverNew)</a>
        <p><a href="JavaEESyncReceiverNewCDI.html">Using the JMS 2.0 simplified API and injection to receive a message  (JavaEESyncReceiverNewCDI)</a>
        <br>
        <h2>Sending a message, setting delivery options and message properties(Java EE)</h1>
        <p>Here are two slightly more complex examples of Java EE applications which
            set message delivery options and message properties before sending the message.<br> 
            Follow the links to view and run code examples.
        <p><a href="JavaEESenderOldWithProperties.html">Using the JMS 1.1-style API to send a message, setting delivery options and message properties  (JavaEESenderOldWithProperties)</a>
        <p><a href="JavaEESenderNewCDIWithProperties.html">Using the JMS 2.0 simplified API with injection to send a message, setting delivery options and message properties (JavaEESenderNewCDIWithProperties)</a>
        <h2>Receiving a message synchronously, displaying delivery options and message properties (Java EE)</h1>
        <p>Here is an example which demonstrates how to extract message delivery options and message proprtties from the received message.<br>
            Follow the links to view and run code it.<br>
            Before running it, use the previous examples to put a few messages on the queue first.            
        <p><a href="JavaEESyncReceiverNewCDIWithProperties.html">Using the JMS 2.0 simplified API and injection to receive a message, displaying delivery options and message properties (JavaEESyncReceiverNewCDIWithProperties)</a>
        <br>
        <h2>Note</h2>
        <p>
        GlassFish 4.0 currently uses Java SE 6, so these examples do not make use of the Java SE 7 AutoCloseable API.
        </body>
</html>
