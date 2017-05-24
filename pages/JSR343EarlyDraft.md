# JMS 2.0 Early Draft

The JMS 2.0 Early Draft is now published and may be downloaded [ http://jcp.org/aboutJava/communityprocess/edr/jsr343/index.html here]. The formal review period ended on 29 March 2012. 

**Comments are still welcome**. You can either send email to the  [mailto:users@jms-spec.java.net user list] ([http://java.net/projects/jms-spec/pages/Home#Mailing_lists_connected_to_the_jms-spec.java.net_project how to  subscribe]) or you can add a comment to the specific JIRA issue [http://java.net/projects/jms-spec/pages/Home#Issue_tracker (how]).

* auto-gen TOC:
{:toc}

## What's new in JMS 2.0? 

The following new messaging features have been added in JMS 2.0:

* Batch delivery: new API has been added to allow a JMS provider to deliver messages to an asynchronous consumer in batches.
* Delivery delay: a message producer can now specify that a message must not be delivered until after a specified time interval.
* New send methods have been added to allow an application to send messages asynchronously.
* JMS providers must now set the `JMSXDeliveryCount` message property.

Several changes have been made to the JMS API to make it simpler and easier to use:

* `Connection`, `Session` and other objects with a `close()` method now implement the `java.jang.AutoCloseable` interface to allow them to be used in a Java SE 7 try-with-resources statement.
* A new "simplified API" has been added which offers a simpler alternative to the standard API, especially in Java EE applications.
* New methods have been added to create a session without the need to supply redundant arguments.
* Client ID is now optional when creating a durable subscription

A new chapter has been added which describes some additional restrictions and behaviour which apply when using the JMS API in the Java EE web or EJB container. This information was previously only available in the EJB and Java EE platform specifications.

New methods have been added to `Session` which return a `MessageConsumer` on a durable topic subscription. Applications could previously only obtain a domain-specific `TopicSubscriber`, even though its use was discouraged.

The JMS 2.0 specification now requires JMS providers to implement both P2P and Pub-Sub.

The specification has been clarified in various places.

A full list of the new features, changes and clarifications introduced in JMS 2.0 is given in section B.5 "Version 2.0" of the draft specification. 

##  Unresolved issues

The JCP process encourages the release of the Early Draft when the specification still has some unresolved issues. Although comments and suggestions are welcome on all parts of the specification, the following issues in particular are known to be unresolved:

* Simplified JMS API: Should the new `MessagingContext` object allow multiple consumers to be created on the same session, which will require separate consumer objects to be created, or should the API restrict a session to a single consumer in order to avoid the need to create a separate object?
* Simplified API: what is the most appropriate API, scope and behaviour for the injection of `>MessagingContext` objects?
* Batch message delivery: Is the proposed API appropriate or is it getting too complicated?

A more detailed discussion of these unresolved issues is given in section A.2 "Unresolved issues in the JMS 2.0 Early Draft" in the draft specification.

##  JIRA Queries related to the Early Draft

The following JIRA query will list all the "issues" included in the Early Draft: <br/>

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&jqlQuery=project+%3D+JMS_SPEC+AND+Tags+%3D+ed20-added Issues included in the JMS 2.0 Early Draft]<br/>

##  What's planned for after the early draft? 

Quite a few issues didn't make it into the Early Draft and have been deferred until the next draft (the Public Draft): These can be listed using the JIRA query below.

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&jqlQuery=project+%3D+JMS_SPEC+AND+Tags+%3D+pd20-underreview Issues which are not included in the JMS 2.0 Early Draft but will be considered for inclusion in the JMS 2.0 Public Draft]<br/>

##  Related specifications

Several JMS-related issues affect other specifications and have been submitted to the appropriate expert group: <br/>

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&jqlQuery=project+%3D+JMS_SPEC+AND+Tags+%3D+jms20-jsr345 Issues which have been passed to the EJB 3.2 expert group for inclusion in that specification]<br/>
[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&jqlQuery=project+%3D+JMS_SPEC+AND+Tags+%3D+jms20-jsr342 Issues which have been passed to the Java EE 7  expert group for inclusion in that specification]<br/>

##  Rejected issues

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&jqlQuery=project+%3D+JMS_SPEC+AND+resolution+%3D+%22Won%27t+Fix%22+AND+status+%3D+Closed Issues that have been rejected]

(More JIRA queries on the [[JIRAUsage | JIRA usage]] page).

