# JMS 2.0 Final Release</h1> 

JMS 2.0 was finally released on 21 May 2013.

The specification document and API documentation may be downloaded from the JCP website [ http://jcp.org/aboutJava/communityprocess/final/jsr343/index.html here]. 

Browse the API documentation for [http://jms-spec.java.net/2.0/apidocs/index.html JMS 2.0 only] (on jms-spec.java.net) or for [http://docs.oracle.com/javaee/7/api/ all of Java EE 7] (on docs.oracle.com)

JMS 2.0 was developed by the Java Community Process as [http://jcp.org/en/jsr/detail?id=343 JSR 343].

**Comments are invited**. You can either send email to the JMS spec  [mailto:users@jms-spec.java.net user list] (you will need to subscribe first: see [http://java.net/projects/jms-spec/pages/Home#Mailing_lists_connected_to_the_jms-spec.java.net_project how to  subscribe]) or directly to the maintenance lead,  [mailto:nigel.deakin@oracle.com Nigel Deakin]. Reports of possible errors or omissions are especially welcome.

__TOC__

==What's new in JMS 2.0?==

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

The JMS 2.0 specification was developed by recording each proposed change in a a JIRA issue tracker. Here's a summary page of  [http://java.net/projects/jms-spec/pages/JSR343Planning all JIRA issues incorporated into JMS 2.0]<br/>

==More information about JMS 2.0==

Watch this [http://www.youtube.com/watch?v=itx4xjqI7yY&feature=em-share_video_in_list_user&list=PL74xrT3oGQfCCLFJ2HCTR_iN5hV4penDz 15 minute slide presentation]

Read these two OTN articles by by Nigel Deakin, JSR 343 spec lead:
* [http://www.oracle.com/technetwork/articles/java/jms20-1947669.html What's New in JMS 2.0, Part One: Ease of Use] 
* [http://www.oracle.com/technetwork/articles/java/jms2messaging-1954190.html What's New in JMS 2.0, Part Two—New Messaging Features]

Read [[JMS20MeansLessCode|Ten ways in which JMS 2.0 means writing less code]].

Listen to a  [https://blogs.oracle.com/javaspotlight/entry/java_spotlight_episode_131_nigel Java Spotlight Podcast interview] with Nigel Deakin, JSR 343 spec lead in which he talks to host Roger Brinkley about some of the new features in JMS 2.0.

If, having read about JMS 2.0, you have questions about why it was designed as it was, read the [[JMS20ReasonsFAQ | JMS 2.0 design FAQ]].

==Reference implementation==

The purpose of a reference implementation is to prove that a specification can be implemented. There are two reference implementations for JMS 2.0.

*The reference implementation for JMS 2.0 in a Java SE environment is based on Open Message Queue 5.0 and is available at [https://mq.java.net/downloads/ri/] 

* The reference implementation for JMS 2.0 as part of a Java EE 7 application server is based on GlassFish Server Open Source Edition 4.0 and will available at [https://glassfish.java.net/downloads/ri/] just as soon as the Java EE  7 specification is released.  

==JMS 2.0 tutorial===

The [http://docs.oracle.com/javaee/7/tutorial/doc/partmessaging.htm JMS 2.0 tutorial] (part of the [http://docs.oracle.com/javaee/7/tutorial/doc/home.htm Java EE 7 tutorial]) introduces the basics of JMS 2.0 and provides some simple examples that you can download and run.

==JMS 2.0 demonstration examples==

A [http://netbeans.org NetBeans] project which demonstrates some of the new features of JMS 2.0 may be downloaded  [http://java.net/projects/jms-spec/downloads/download/JMS20Demo.zip as a zip] or checked out from the project's subversion repository [http://java.net/projects/jms-spec/sources/repository/show/jms2.0/demos/JMS20Demo here]. An earlier version was demonstrated at JavaOne 2012.

This demonstration (which was last updated to work with GlassFish build 82) shows
* Use of the JMS 2.0 simplified API  using application-managed <tt>JMSContext</tt> objects
* Use of the JMS 2.0 API using Java EE container-managed (injected) <tt>JMSContext</tt> objects
* Use of the new <tt>receiveBody</tt> method to synchronously receive a message and return its body in a single method call
* Use of the new <tt>getBody</tt> method to return the body of a <tt>Message</tt> without the need to cast it to a more specific message type

Please report issues with this demonstration directly to [mailto:nigel.deakin@oracle.com Nigel Deakin].

== JMS 2.0 schedule (historical) ==

<br/>Also see the [http://java.net/projects/jms-spec/pages/JSR343Planning JMS 2.0 Planning] page <br/>

{|- border="1"
! Stage
! Initial plan<br/>(Feb 2011)
! Current plan<br/> (updated  <strike>1 Nov 2012</strike> 6 Feb 2013)
! Actual
|- 
! JSR approval
|  March 2011
|  
| March 2011
|-
! Expert group formation
|  March 2011
|  
| May 2011
|-   
! Early draft review
|  Q3 2011
| 
|   28 Feb - 29 Mar 2012
|-
! Submission of Public Review Draft to the JCP
| 
| 19 Dec 2012 	
| 19 Dec 2012 	
|-
! Start of Public Review
|
| 3  Jan 2013 	
| 3  Jan 2013 
|-
! End of Public Review
|
| 4  Feb 2013 	
| 4  Feb 2013 
|-
! Completion of JCP Public Review Ballot
|
|18 Feb 2013
|18 Feb 2013
|-
! Submission of Proposed Final Draft to the JCP
|
| 20 Feb 2013
| 20 Feb 2013
|-
! Submission of Materials for Final Ballot to the JCP
|
| 20 Mar 2013 	
| 20 Mar 2013 	
|-
! Start of Final Ballot
|
| 26 Mar 2013 	
| 26 Mar 2013 	
|-
! Completion of Final Ballot
|
| 8  Apr 2013 	
| 8  Apr 2013 	
|-
! Release of Spec, RI, TCK 
|
| <strike>15 Apr 2013</strike> <strike>30 Apr 2013</strike> 22 April 2013
| 21 May 2013
|}

This was based in the [http://java.net/projects/javaee-spec/pages/Home#Java_EE_7_Schedule schedule for the Java EE platform]