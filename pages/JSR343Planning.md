<h1>JMS 2.0 Planning</h1>

This page contains lists of issues which were incorporated into JMS 2.0. For information about JMS 2.1 (or whatever version follows JMS 2.0), see the [[JMS21Planning|JMS 2.1 planning page]].

__TOC__

==All issues incorporated in the JMS 2.0 final release==

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&jqlQuery=project+%3D+JMS_SPEC+AND+resolution+%3D+Fixed+AND+fixVersion+%3D+%222.0%22  Query]  (All fixed issues with fix version=JMS20)

==Issues incorporated in the early draft==

[http://java.net/jira/secure/IssueNavigator.jspa?mode=hide&requestId=11293 Query (filter)]

{|- border="1"
! ID
! Description
! Status
! Notes
|-  
| [http://java.net/jira/browse/JMS_SPEC-65 JMS_SPEC-65]
| Clarify use of NoLocal arg when using createDurableSubscriber
| In the Early Draft. 
| This has been further updated for the Public Draft. Resolved.
|-  
| [http://java.net/jira/browse/JMS_SPEC-53 JMS_SPEC-53]
| Make Connection and other interfaces implement AutoCloseable
| In the Early Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-52 JMS_SPEC-52]
| Clarify that a message may be sent using a different session from that used to create the message
| In the Early Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-51 JMS_SPEC-51]
| New methods to replace Session.createDurableSubscriber() and return a MessageConsumer
| In the Early Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-50 JMS_SPEC-50]
| Clarify that JMS providers must implement both P2P and Pub-Sub
| In the Early Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-49 JMS_SPEC-49]
| Improve specification of ExceptionListener 
| In the Early Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-48 JMS_SPEC-48 ]
| Specify that connection.stop() or close() may not be called from a MessageListener 
| In the Early Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-45 JMS_SPEC-45 ]
| Clarify and improve Connection.createSession
| In the Early Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-44 JMS_SPEC-44 ]
| New API to specify delivery delay
| In the Early Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-43 JMS_SPEC-43 ]
| New API to send a message with async acknowledgement from server
| In the Early Draft
| This has been further updated for the Public Draft. Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-42 JMS_SPEC-42 ]
| Make support for JMSXDeliveryCount mandatory
| In the Early Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-39 JMS_SPEC-39 ]
| Make clientId optional when creating a durable subscription
| In the Early Draft
| Shared subscriptions only. Resolved.
|-  
| [http://java.net/jira/browse/JMS_SPEC-34 JMS_SPEC-34 ]
| Calling setJMSDeliveryMode or setJMSPriority on a javax.jms.Message before it is sent don't have any effect
| In the Early Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-33 JMS_SPEC-33 ]
| Improving the JMS API with API simplifications, annotations and CDI
| In the Early Draft. 
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-27 JMS_SPEC-27 ]
| Clarify the relationship between the JMS and other Java EE specifications
| In the Early Draft
| Resolved
|}

==Issues incorporated in the public draft.==

[http://java.net/jira/secure/IssueNavigator.jspa?mode=hide&requestId=11296 Query (filter)] (<tt>tag=pd20-added</tt>)

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-107 JMS_SPEC-107]
| Extend connection consumer API to support shared durable and non-durable subscriptions
| In the public draft
| Resolved
|-
| [http://java.net/jira/browse/JMS_SPEC-106 JMS_SPEC-106]
| Methods on JMSContext that are disallowed if the context is injected should throw a IllegalStateException not a JMSException 
| In the public draft
| Resolved
|-
| [http://java.net/jira/browse/JMS_SPEC-105 JMS_SPEC-105]
| Provide API to allow an app server or resource adapter to obtain a XAResource from a JMSContext
| In the public draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-102 JMS_SPEC-102]
| Make JMSConsumer.receivePayload methods consistent with Message.getBody
| In the public draft
|  Resolved
|-
| [http://java.net/jira/browse/JMS_SPEC-101 JMS_SPEC-101]
| New method Message.getBody
| In the public draft
| Resolved
|-
| [http://java.net/jira/browse/JMS_SPEC-98 JMS_SPEC-98]
| Fix findbugs warnings in JMSException, JMSRuntimeException, QueueRequestor, TopicRequestor
| Fixed
| RI rather than spec issue. Resolved
|-
| [http://java.net/jira/browse/JMS_SPEC-96 JMS_SPEC-96]
| Define Java EE JMS Connection Factory Definition annotation and descriptor elements
| In the public draft
| Further updated for the proposed final draft and the final release. Annotations are defined in JMS, descriptor elements will be defined in the Java EE schema. Resolved
|-
| [http://java.net/jira/browse/JMS_SPEC-97 JMS_SPEC-97]
| Define Java EE JMS Destination Definition annotation and descriptor elements
| In the public draft
| Further updated for the proposed final draft and the final release. Annotations are defined in JMS, descriptor elements will be defined in the Java EE schema. Resolved
|-    
| [http://java.net/jira/browse/JMS_SPEC-94 JMS_SPEC-94]
| Define what characters are valid in a durable (or shared subscription) name
| In the public draft
|  Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-93 JMS_SPEC-93]
| Does changing the noLocal flag when connecting to a durable subscription cause the durable subscription to be deleted?
| In the Public Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-82 JMS_SPEC-82]
| Clarify definition of JMSExpiration, replacing GMT with UTC
| In the Public Draft 
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-80 JMS_SPEC-80]
| Error in example 9.3.3.2 "Reconnect to a topic using a durable subscription"
|  In the Public Draft
| Resolved
|-    
| [http://java.net/jira/browse/JMS_SPEC-78 JMS_SPEC-78]
| JMS implementation of QueueRequestor and TopicRequestor doesn't throw correct exception when destination is null
| Fixed
| RI rather than spec issue. Resolved
|-
| [http://java.net/jira/browse/JMS_SPEC-77 JMS_SPEC-77]
| MapMessage.setBytes API discrepancy found in the javadocs
|  In the Public Draft
|  Resolved
|-
| [http://java.net/jira/browse/JMS_SPEC-70 JMS_SPEC-70]
| Define annotations for injecting MessagingContext objects
|  In the Public Draft
| Resolved
|-
| [http://java.net/jira/browse/JMS_SPEC-64 JMS_SPEC-64]
| Define simplified JMS API
| In the Public Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-55 JMS_SPEC-55]
| Define a standard way to configure the connection factory used by a JMS MDB to consume messages
| In the Public Draft
| Now covered in a new JMS 2.0 chapter  "Resource adapter". Text also added to EJB 3.2 specification. Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-54 JMS_SPEC-54]
| Define a standard way to configure the destination on which a JMS MDB consumes messages
| In the Public Draft
| Now covered in a new JMS 2.0 chapter "Resource adapter". Text also added to EJB 3.2 specification. Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-40 JMS_SPEC-40]
| Allow multiple consumers to be created on the same topic subscription
| In the Public Draft
|  Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-31 JMS_SPEC-31]
| change javadoc on session.createQueue and createTopic to make clearer the provider may create a physical destination
| In the Public Draft
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-30 JMS_SPEC-30]
| Define mandatory activation config properties clientId and subscriptionName
| In the Public Draft
| text also added to EJB 3.2 specification. Resolved
|-   
|}

==Minor documentation updates included in the proposed final draft==

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&customfield_10002=jms20-fd20-doc-added Query]  (<tt>tag=jms20-fd20-doc-added </tt>)

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-69 JMS_SPEC-69]
| Clarify that QueueRequestor and TopicRequestor only work in a non-transacted session with auto or dups-ok ack
| 
| Resolved
|-
| [http://java.net/jira/browse/JMS_SPEC-81 JMS_SPEC-81]
| Remove Change History for previous versions from the specification
| 
| Resolved 
|-  
| [http://java.net/jira/browse/JMS_SPEC-87 JMS_SPEC-87]
| Section 2.5 "Interfaces" needs updating to introduce the simplified API
| 
| Resolved
|-  
| [http://java.net/jira/browse/JMS_SPEC-114 JMS_SPEC-114]
| Clarify javadoc descriptions of XAQueueConnection#createSession and XATopicSession#createSession
| 
| Resolved
|-  
|-
| [http://java.net/jira/browse/JMS_SPEC-115 JMS_SPEC-115] <br/>
| Remove the statement that portable applications should only have one consumer per queue
| 
| Resolved  
|}

==Minor documentation updates included in the final release==

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&customfield_10002=jms20-fr20-doc-added Query]  (<tt>tag=jms20-fr20-doc-added </tt>)

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-86 JMS_SPEC-86]
| Chapter 1 "Introduction" is a little dated and requires rewriting
|  
| Resolved
|- 
|}

==Issues incorporated in the Java EE 7 specification==

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&customfield_10002=jms20-jsr342-added  Query]  (<tt>tag=jms20-jsr342-added </tt>)

{|- border="1"
! ID
! Description
! Status
! Notes
|-  
| [http://java.net/jira/browse/JMS_SPEC-63 JMS_SPEC-63]
| Introduce concept of platform default JMS connection factory in Java EE
| Included in '''Java EE 7''' Early Draft
|  
|-
|}

== Issues incorporated in the Java Connector Architecture 1.7 specification ==

{|- border="1"
! ID
! Description
! Status
! Notes
|-  
| [http://java.net/jira/browse/CONNECTOR_SPEC-4 CONNECTOR_SPEC-4 ]
| Clarify whether the ResourceAdapter.endpointActivation call has full access to administered objects in JNDI
| Agreed by JCA EG. Resolved.
| Needed for  [http://java.net/jira/browse/JMS_SPEC-54 JMS_SPEC-54] and  [http://java.net/jira/browse/JMS_SPEC-55 JMS_SPEC-55]
|}

==Reference implementation==

The following queries will list the implementation issues for the reference implementation

{|- border="1"
! Component
! Issues
|-
| Message Queue
| [http://java.net/jira/secure/IssueNavigator.jspa?mode=hide&requestId=11294 JIRA query]
|-
| GlassFish
| [http://java.net/jira/secure/IssueNavigator.jspa?mode=hide&requestId=11555  JIRA query]
|}
