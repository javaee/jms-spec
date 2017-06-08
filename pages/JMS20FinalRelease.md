# JMS 2.0 Final Release 
{: .no_toc}

JMS 2.0 was finally released on 21 May 2013.

The specification document and API documentation of the JMS 2.0 final release may be downloaded from the JCP website [here](http://jcp.org/aboutJava/communityprocess/final/jsr343/index.html). 

Since the JMS 2.0 final release there has since been a maintenance release which may be downloaded from the JCP website [here](https://jcp.org/aboutJava/communityprocess/mrel/jsr343/index.html). For more information see [JMS 2.0 errata page](/jms-spec/pages/JMS20RevA).

Browse the [JMS 2.0 javadocs](http://docs.oracle.com/javaee/7/api/index.html?javax/jms/package-summary.html).

JMS 2.0 was developed by the Java Community Process as [JSR 343](http://jcp.org/en/jsr/detail?id=343).

**Comments are invited**. Reports of possible errors or omissions are especially welcome. See [Getting involved](/jms-spec/#getting-involved).
  
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

The JMS 2.0 specification was developed by recording each proposed change in an issue tracker. Here's a summary page of [all issues incorporated into JMS 2.0](/jms-spec/pages/JSR343Planning)

## More information about JMS 2.0 

Watch this [15 minute slide presentation](http://www.youtube.com/watch?v=itx4xjqI7yY&feature=em-share_video_in_list_user&list=PL74xrT3oGQfCCLFJ2HCTR_iN5hV4penDz).

Read these two OTN articles by by Nigel Deakin, JSR 343 spec lead:
* [What's New in JMS 2.0, Part One: Ease of Use](http://www.oracle.com/technetwork/articles/java/jms20-1947669.html) 
* [What's New in JMS 2.0, Part Two—New Messaging Features](http://www.oracle.com/technetwork/articles/java/jms2messaging-1954190.html)

Read [Ten ways in which JMS 2.0 means writing less code](/jms-spec/pages/JMS20MeansLessCode).

If, having read about JMS 2.0, you have questions about why it was designed as it was, read the [JMS 2.0 design FAQ](/jms-spec/pages/JMS20ReasonsFAQ).

## Reference implementation 

The purpose of a reference implementation is to prove that a specification can be implemented. There are two reference implementations for JMS 2.0.

* The reference implementation for JMS 2.0 in a Java SE environment is based on Open Message Queue 5.0. 
  * [Open Message Queue project page](/openmq)
  * [Open MQ RI downloads page](/openmq/www/downloads/ri/)

* The reference implementation for JMS 2.0 as part of a Java EE 7 application server is based on GlassFish Server Open Source Edition 4.0.
  * [GlassFish project page](/glassfish/)
  * [GlassFish RI downloads page](/glassfish/downloads/ri/index.html)

## JMS 2.0 tutorial

The [JMS 2.0 tutorial](https://docs.oracle.com/javaee/7/tutorial/partmessaging.htm) (part of the [Java EE 7 tutorial](https://docs.oracle.com/javaee/7/tutorial/)) introduces the basics of JMS 2.0 and provides some simple examples that you can download and run.

## JMS 2.0 demonstration examples 

A [NetBeans](http://netbeans.org) project which demonstrates some of the new features of JMS 2.0 may be downloaded  [as a zip](/jms-spec/downloads/JMS20Demo.zip) or checked out from the project's GitHub repository [here](https://github.com/javaee/jms-spec/tree/master/jms2.0/demos/JMS20Demo). An earlier version was demonstrated at JavaOne 2012.

This demonstration (which was last updated to work with GlassFish build 82) shows
* Use of the JMS 2.0 simplified API  using application-managed `JMSContext` objects
* Use of the JMS 2.0 API using Java EE container-managed (injected) `JMSContext` objects
* Use of the new `receiveBody` method to synchronously receive a message and return its body in a single method call
* Use of the new `getBody` method to return the body of a `Message` without the need to cast it to a more specific message type

Please report issues with this demonstration using the [issue tracker](https://github.com/javaee/jms-spec/issues).

##  JMS 2.0 schedule (historical)

Also see the [http://java.net/projects/jms-spec/pages/JSR343Planning JMS 2.0 Planning] page 

Stage | Initial plan<br/>(Feb 2011) | Current plan<br/> (updated  <strike>1 Nov 2012</strike> 6 Feb 2013) | Actual
:--- | :--- | :---
JSR approval | March 2011 |   | March 2011
Expert group formation | March 2011 | | May 2011
Early draft review | Q3 2011 | |   28 Feb - 29 Mar 2012
Submission of Public Review Draft to the JCP | | 19 Dec 2012 | 19 Dec 2012 	
Start of Public Review | | 3 Jan 2013 | 3  Jan 2013 
End of Public Review | | 4  Feb 2013 | 4  Feb 2013 
Completion of JCP Public Review Ballot | |18 Feb 2013 |18 Feb 2013
Submission of Proposed Final Draft to the JCP | | 20 Feb 2013 | 20 Feb 2013
Submission of Materials for Final Ballot to the JCP | | 20 Mar 2013 | 20 Mar 2013 	
Start of Final Ballot | | 26 Mar 2013 | 26 Mar 2013 	
Completion of Final Ballot | | 8  Apr 2013 | 8  Apr 2013 	
Release of Spec, RI, TCK | | <strike>15 Apr 2013</strike> <strike>30 Apr 2013</strike> 22 April 2013 | 21 May 2013

This was based on the schedule for the Java EE 7 platform.
