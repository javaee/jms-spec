# JMS 2.0 Planning

This page contains lists of issues which were incorporated into JMS 2.0. For information about JMS 2.1 (or whatever version follows JMS 2.0), see the [JMS 2.1 planning page](/jms-spec/pages/JMS21Planning).

* auto-gen TOC:
{:toc}

## All issues incorporated in the JMS 2.0 final release 

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&jqlQuery=project+%3D+JMS_SPEC+AND+resolution+%3D+Fixed+AND+fixVersion+%3D+%222.0%22  Query]  (All fixed issues with fix version=JMS20)

## Issues incorporated in the early draft 

[All issues with the milestone "2.0ED"](https://github.com/javaee/jms-spec/issues?q=is%3Aclosed+milestone%3A2.0ED)

ID | Description | Notes
:--- | :--- | :---
[JMS_SPEC-65](https://github.com/javaee/jms-spec/issues/65) | Clarify use of NoLocal arg when using createDurableSubscriber | This was introduced into the Early Draft and further updated for the Public Draft. 
[JMS_SPEC-53](https://github.com/javaee/jms-spec/issues/53) | Make Connection and other interfaces implement AutoCloseable | 
[JMS_SPEC-52](https://github.com/javaee/jms-spec/issues/52) | Clarify that a message may be sent using a different session from that used to create the message |
[JMS_SPEC-51](https://github.com/javaee/jms-spec/issues/51) | New methods to replace Session.createDurableSubscriber() and return a MessageConsumer |
[JMS_SPEC-50](https://github.com/javaee/jms-spec/issues/50) | Clarify that JMS providers must implement both P2P and Pub-Sub | In the Early Draft | 
[JMS_SPEC-49](https://github.com/javaee/jms-spec/issues/49) | Improve specification of ExceptionListener | 
[JMS_SPEC-48](https://github.com/javaee/jms-spec/issues/48) | Specify that connection.stop() or close() may not be called from MessageListener | 
[JMS_SPEC-45](https://github.com/javaee/jms-spec/issues/45) | Clarify and improve Connection.createSession 
[JMS_SPEC-44](https://github.com/javaee/jms-spec/issues/44) | New API to specify delivery delay 
[JMS_SPEC-43](https://github.com/javaee/jms-spec/issues/43) | New API to send a message with async acknowledgement from server | This was introduced into the Early Draft and further updated for the Public Draft. 
[JMS_SPEC-42](https://github.com/javaee/jms-spec/issues/42) | Make support for JMSXDeliveryCount mandatory |
[JMS_SPEC-39](https://github.com/javaee/jms-spec/issues/39) | Make clientId optional when creating a durable subscription | Shared subscriptions only
[JMS_SPEC-34](https://github.com/javaee/jms-spec/issues/34) | Calling setJMSDeliveryMode or setJMSPriority on a javax.jms.Message before it is sent don't have any effect |
[JMS_SPEC-33](https://github.com/javaee/jms-spec/issues/33) | Improving the JMS API with API simplifications, annotations and CDI | 
[JMS_SPEC-27](https://github.com/javaee/jms-spec/issues/27) | Clarify the relationship between the JMS and other Java EE specifications | 

## Issues incorporated in the public draft. 

[All issues with the milestone "2.0PD"](https://github.com/javaee/jms-spec/issues?q=is%3Aclosed+milestone%3A2.0PD)

ID | Description | Notes
:--- | :--- | :---
[JMS_SPEC-107](https://github.com/javaee/jms-spec/issues/107) | Extend connection consumer API to support shared durable and non-durable subscriptions |
[JMS_SPEC-106](https://github.com/javaee/jms-spec/issues/106) | Methods on JMSContext that are disallowed if the context is injected should throw a IllegalStateException not a JMSException |
[JMS_SPEC-105](https://github.com/javaee/jms-spec/issues/105) | Provide API to allow an app server or resource adapter to obtain a XAResource from a JMSContext |
[JMS_SPEC-102](https://github.com/javaee/jms-spec/issues/102) | Make JMSConsumer.receivePayload methods consistent with Message.getBody |
[JMS_SPEC-101](https://github.com/javaee/jms-spec/issues/101) | New method Message.getBody | In the public draft | Resolved
[JMS_SPEC-98](https://github.com/javaee/jms-spec/issues/98) | Fix findbugs warnings in JMSException, JMSRuntimeException, QueueRequestor, TopicRequestor | RI rather than spec issue, resolved
[JMS_SPEC-96](https://github.com/javaee/jms-spec/issues/96) | Define Java EE JMS Connection Factory Definition annotation and descriptor elements | This was introduced into the public draft and further updated for the proposed final draft and the final release. Annotations are defined in JMS, descriptor elements are defined in the Java EE schema. 
[JMS_SPEC-97](https://github.com/javaee/jms-spec/issues/97) | This was introduced into the public draft and further updated for the proposed final draft and the final release. Annotations are defined in JMS, descriptor elements are defined in the Java EE schema
[JMS_SPEC-94](https://github.com/javaee/jms-spec/issues/94) | Define what characters are valid in a durable (or shared subscription) name |[JMS_SPEC-93](https://github.com/javaee/jms-spec/issues/93) | Does changing the noLocal flag when connecting to a durable subscription cause the durable subscription to be deleted? |
[JMS_SPEC-82](https://github.com/javaee/jms-spec/issues/82) | Clarify definition of JMSExpiration, replacing GMT with UTC | 
[JMS_SPEC-80](https://github.com/javaee/jms-spec/issues/80) | Error in example 9.3.3.2 "Reconnect to a topic using a durable subscription" |
[JMS_SPEC-78](https://github.com/javaee/jms-spec/issues/78) | JMS implementation of QueueRequestor and TopicRequestor doesn't throw correct exception when destination is null | RI rather than spec issue, resolved
[JMS_SPEC-77](https://github.com/javaee/jms-spec/issues/77) | MapMessage.setBytes API discrepancy found in the javadocs |
[JMS_SPEC-70](https://github.com/javaee/jms-spec/issues/70) | Define annotations for injecting MessagingContext objects |
[JMS_SPEC-64](https://github.com/javaee/jms-spec/issues/64) | Define simplified JMS API |
[JMS_SPEC-55](https://github.com/javaee/jms-spec/issues/55) | Define a standard way to configure the connection factory used by a JMS MDB to consume messages | Now covered in a new JMS 2.0 chapter  "Resource adapter". Text also added to EJB 3.2 specification. 
[JMS_SPEC-54](https://github.com/javaee/jms-spec/issues/54) | Define a standard way to configure the destination on which a JMS MDB consumes messages | Now covered in a new JMS 2.0 chapter "Resource adapter". Text also added to EJB 3.2 specification. 
[JMS_SPEC-40](https://github.com/javaee/jms-spec/issues/40) | Allow multiple consumers to be created on the same topic subscription |
[JMS_SPEC-31](https://github.com/javaee/jms-spec/issues/31) | change javadoc on session.createQueue and createTopic to make clearer the provider may create a physical destination |
[JMS_SPEC-30](https://github.com/javaee/jms-spec/issues/30) | Define mandatory activation config properties clientId and subscriptionName | Text also added to EJB 3.2 specification. 

## Minor documentation updates included in the proposed final draft 

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&customfield_10002=jms20-fd20-doc-added Query]  (`tag=jms20-fd20-doc-added `)

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [JMS_SPEC-69](https://github.com/javaee/jms-spec/issues/69)
| Clarify that QueueRequestor and TopicRequestor only work in a non-transacted session with auto or dups-ok ack
| 
| Resolved
|-
| [JMS_SPEC-81](https://github.com/javaee/jms-spec/issues/81)
| Remove Change History for previous versions from the specification
| 
| Resolved 
|-  
| [JMS_SPEC-87](https://github.com/javaee/jms-spec/issues/87)
| Section 2.5 "Interfaces" needs updating to introduce the simplified API
| 
| Resolved
|-  
| [JMS_SPEC-114](https://github.com/javaee/jms-spec/issues/114)
| Clarify javadoc descriptions of XAQueueConnection#createSession and XATopicSession#createSession
| 
| Resolved
|-  
|-
| [JMS_SPEC-115](https://github.com/javaee/jms-spec/issues/115) <br/>
| Remove the statement that portable applications should only have one consumer per queue
| 
| Resolved  
|}

## Minor documentation updates included in the final release 

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&customfield_10002=jms20-fr20-doc-added Query]  (`tag=jms20-fr20-doc-added `)

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [JMS_SPEC-86](https://github.com/javaee/jms-spec/issues/86)
| Chapter 1 "Introduction" is a little dated and requires rewriting
|  
| Resolved
|- 
|}

## Issues incorporated in the Java EE 7 specification 

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&customfield_10002=jms20-jsr342-added  Query]  (`tag=jms20-jsr342-added `)

{|- border="1"
! ID
! Description
! Status
! Notes
|-  
| [JMS_SPEC-63](https://github.com/javaee/jms-spec/issues/63)
| Introduce concept of platform default JMS connection factory in Java EE
| Included in **Java EE 7** Early Draft
|  
|-
|}

##  Issues incorporated in the Java Connector Architecture 1.7 specification

{|- border="1"
! ID
! Description
! Status
! Notes
|-  
| [http://java.net/jira/browse/CONNECTOR_SPEC-4 CONNECTOR_SPEC-4 )
| Clarify whether the ResourceAdapter.endpointActivation call has full access to administered objects in JNDI
| Agreed by JCA EG. Resolved.
| Needed for  [JMS_SPEC-54](https://github.com/javaee/jms-spec/issues/54) and  [JMS_SPEC-55](https://github.com/javaee/jms-spec/issues/55)
|}

## Reference implementation 

The following queries will list the implementation issues for the reference implementation

{|- border="1"
! Component
! Issues
|-
| Message Queue
| [http://java.net/jira/secure/IssueNavigator.jspa?mode=hide&requestId=11294 JIRA query)
|-
| GlassFish
| [http://java.net/jira/secure/IssueNavigator.jspa?mode=hide&requestId=11555  JIRA query)
|}
