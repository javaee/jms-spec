# JMS 2.0 Proposed Final Draft
{: .no_toc}

_JMS 2.0 has now been released. This page is retained here as a historical record. See also the [JMS 2.0 Final Release](/jms-spec/pages/JMS20FinalRelease) page._

The JMS 2.0 Proposed Final Draft was published on 26 Febuary 2013. The specification and API documentation may be downloaded from the JCP website [here](http://jcp.org/aboutJava/communityprocess/pfd/jsr343/index.html). 

## Contents
{: .no_toc}

* auto-gen TOC:
{:toc}

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
* `Connection`, `Session` and other objects with a `close` method now implement the `java.jang.AutoCloseable` interface to allow them to be used in a Java SE 7 try-with-resources statement. 
* A new "simplified API" has been added which offers a simpler alternative to the standard API, especially in Java EE applications.
* New methods have been added to create a session without the need to supply redundant arguments.
* Although setting client ID remains mandatory when creating an unshared durable subscription, it is optional when creating a shared durable subscription. 
* A new method `getBody` has been added to allow an application to extract the body directly from a Message without the need to cast it first to an appropriate subtype. 

A new chapter has been added which describes some additional restrictions and behaviour which apply when using the JMS API in the Java EE web or EJB container. This information was previously only available in the EJB and Java EE platform specifications. 

A new chapter has been added which defines a number of standard configuration properties for JMS message-driven beans.

New methods have been added to `Session` which return a `MessageConsumer` on a durable topic subscription. Applications could previously only obtain a domain-specific `TopicSubscriber`, even though its use was discouraged. 

The specification has been clarified in various places.

## JMS 2.0 demonstration examples 

A [NetBeans](http://netbeans.org) project which demonstrates some new features of JMS 2.0 may be downloaded  [as a zip](/jms-spec/downloads/JMS20Demo.zip) or checked out from the source code repository [here](https://github.com/javaee/jms-spec/tree/master/jms2.0/demos/JMS20Demo). An earlier version was demonstrated at JavaOne 2012.

This demonstration (which has been updated to work with GlassFish build 82 (glassfish-4.0-b82-03_22_2013) shows
* Use of the JMS 2.0 simplified API  using application-managed `JMSContext` objects
* Use of the JMS 2.0 API using Java EE container-managed (injected) `JMSContext` objects
* Use of the new `receiveBody` method to synchronously receive a message and return its body in a single method call
* Use of the new `getBody` method to return the body of a `Message` without the need to cast it to a more specific message type

## Related pages

* [JMS 2.0 Early Draft](/jms-spec/pages/JSR343EarlyDraft)
* [JMS 2.0 Public Draft](/jms-spec/pages/JMS20PublicDraft)
* [JMS 2.0 Final Release](/jms-spec/pages/JMS20FinalRelease)
