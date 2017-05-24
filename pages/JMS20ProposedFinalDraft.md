# JMS 2.0 Proposed Final Draft

The JMS 2.0 Proposed Final Draft was published on 26 Febuary 2013. The specification and API documentation may be downloaded from the JCP website [ http://jcp.org/aboutJava/communityprocess/pfd/jsr343/index.html here]. 

In addition the API documentation may be browsed online [http://jms-spec.java.net/2.0/apidocs/index.html here]. (This link now takes you to the final API docs)

**Comments are invited** but must be received in time to be considered and incorporated in time for the final submission on 20 March 2013. You can either send email to the JMS spec  [mailto:users@jms-spec.java.net user list] (you will need to subscribe first: see [http://java.net/projects/jms-spec/pages/Home#Mailing_lists_connected_to_the_jms-spec.java.net_project how to  subscribe]) or you can send your comments directly to the specification lead,  [mailto:nigel.deakin@oracle.com Nigel Deakin]. 

* auto-gen TOC:
{:toc}

## Reference implementation 

You are also invited to try out the current version of the JMS 2.0 reference implementation. This is still under development but is essentially feature complete. 
* If you would like to try JMS 2.0 as part of a full Java EE 7 application server then you you need to download a recent build of GlassFish Server Open Source Edition 4.0. The promoted builds are [http://dlc.sun.com.edgesuite.net/glassfish/4.0/promoted/ here] and the less stable nightly builds are [http://dlc.sun.com.edgesuite.net/glassfish/4.0/nightly/ here]. 
*If you are are interested in JMS 2.0 in a Java SE environment only then you can download a recent promoted build of the standalone Open Message Queue 5.0 [http://mq.java.net/5.0.html#download here]. 

(Strictly speaking these are not actually reference implementations. They are community projects which will be used to create the reference implementation when JMS 2.0 is released.)

## JMS 2.0 demonstration examples 

A [http://netbeans.org NetBeans] project which demonstrates some new features of JMS 2.0 may be downloaded  [http://java.net/projects/jms-spec/downloads/download/JMS20Demo.zip as a zip] or checked out from the project's subversion repository [http://java.net/projects/jms-spec/sources/repository/show/jms2.0/demos/JMS20Demo here]. An earlier version was demonstrated at JavaOne 2012.

This demonstration (which has been updated to work with GlassFish build 82 (glassfish-4.0-b82-03_22_2013) shows
* Use of the JMS 2.0 simplified API  using application-managed <tt>JMSContext</tt> objects
* Use of the JMS 2.0 API using Java EE container-managed (injected) <tt>JMSContext</tt> objects
* Use of the new <tt>receiveBody</tt> method to synchronously receive a message and return its body in a single method call
* Use of the new <tt>getBody</tt> method to return the body of a <tt>Message</tt> without the need to cast it to a more specific message type

## What's new in JMS 2.0? 

A full list of the new features, changes and clarifications introduced in JMS 2.0 is given in the specification. See section B.5 "Version 2.0" of the "Change history" appendix. Here is a summary:

The JMS 2.0 specification now requires JMS providers to implement both P2P and Pub-Sub.

The following new messaging features have been added in JMS 2.0:
* Delivery delay: a message producer can now specify that a message must not be delivered until after a specified time interval.
* New send methods have been added to allow an application to send messages asynchronously.
* JMS providers must now set the JMSXDeliveryCount message property.

The following change has been made to aid scalability:
* Applications are now permitted to create multiple consumers on the same durable or non-durable topic subscription. In previous versions of JMS only a single consumer was permitted.

Several changes have been made to the JMS API to make it simpler and easier to use:
* <tt>Connection</tt>, <tt>Session</tt> and other objects with a <tt>close</tt> method now implement the <tt>java.jang.AutoCloseable</tt> interface to allow them to be used in a Java SE 7 try-with-resources statement. 
* A new "simplified API" has been added which offers a simpler alternative to the standard API, especially in Java EE applications.
* New methods have been added to create a session without the need to supply redundant arguments.
* Although setting client ID remains mandatory when creating an unshared durable subscription, it is optional when creating a shared durable subscription. 
* A new method <tt>getBody</tt> has been added to allow an application to extract the body directly from a Message without the need to cast it first to an appropriate subtype. 

A new chapter has been added which describes some additional restrictions and behaviour which apply when using the JMS API in the Java EE web or EJB container. This information was previously only available in the EJB and Java EE platform specifications. 

A new chapter has been added which defines a number of standard configuration properties for JMS message-driven beans.

New methods have been added to <tt>Session</tt> which return a <tt>MessageConsumer</tt> on a durable topic subscription. Applications could previously only obtain a domain-specific <tt>TopicSubscriber</tt>, even though its use was discouraged. 

The specification has been clarified in various places.
