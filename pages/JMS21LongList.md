# JMS 2.1 Planning Long List</h1>

Improvements to the JMS specification are managed using issues in JIRA. Here is a classified list of all the open issues. Only a subset of these issues will actually make it into JMS 2.1. See the actual [[JMS21Plan|JMS 2.1 plan]].

(This page was reorganised on 28 Aug 2015. The previous version is [/jms-spec/pages/JMS21Planning/revisions/88 here].)

__TOC__

## Corrections and clarifications (minor)

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-175 JMS_SPEC-175] <br/>
| Fix javadocs to remove need to disable doclint
|
|
|-
| [http://java.net/jira/browse/JMS_SPEC-173 JMS_SPEC-173] <br/>
| Javadocs for <tt>JMSContext#start</tt> should mention that it is usually called automatically
|
|
|-
| [http://java.net/jira/browse/JMS_SPEC-171 JMS_SPEC-171]
| Typo in javadoc for JMSDestinationDefinition
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-153 JMS_SPEC-153]
| Correction to 7.3.8. "Use of the CompletionListener by the JMS provider"
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-150 JMS_SPEC-150 ]
|  Add guidance on when to use receiveBody and how to handle MessageFormatRuntimeException 
|  
|          
|-
| [http://java.net/jira/browse/JMS_SPEC-121 JMS_SPEC-121]
| Injection of JMSContext objects not possible in a WebSocket @OnMessage or @OnClose callback method 
|  
| Not really a bug, but may require noting in the spec
|-    
| [http://java.net/jira/browse/JMS_SPEC-123 JMS_SPEC-123]
| <tt>JMSProducer#setJMSCorrelationIDAsBytes</tt> should be allowed to throw a <tt>java.lang.UnsupportedOperationException</tt>
|  
| 
|- 
| [http://java.net/jira/browse/JMS_SPEC-104 JMS_SPEC-104] <br/>
| API javadocs need to conform to Oracle accessibility standards and W3C HTML validation standards
|    
|  Partially completed for JMS 2.0 but one issue remains.
|-
| [http://java.net/jira/browse/JMS_SPEC-84 JMS_SPEC-84]
| Clarify when acknowledged persistent messages may be dropped
|  
| Will defer to JMS 2.1
|-  
| [http://java.net/jira/browse/JMS_SPEC-75 JMS_SPEC-75]
| Ambiguous javadocs for Connection.createConnectionConsumer and createDurableConnectionConsumer
|  
| Carried forward from JMS 2.0
|-
| [http://java.net/jira/browse/JMS_SPEC-47 JMS_SPEC-47]
| Deprecate domain-specific APIs and propose for removal
| 
| Deprecation not allowed by Java EE policy. Could make a recommendation not to use.  
|-  
| [http://java.net/jira/browse/JMS_SPEC-3 JMS_SPEC-3
| Fix JavaDocs to reflect missing NumberFormatException from API methods
|  
| Carried forward from JMS 2.0
|-  
| [http://java.net/jira/browse/JMS_SPEC-2 JMS_SPEC-2]
| Fix JavaDocs to reflect missing IllegalStateException from API methods
|  
| Carried forward from JMS 2.0
|-  
|}

## Corrections (major)=### 

{|- border="1"
! ID
! Description
! Status
! Notes
|-   
| [http://java.net/jira/browse/JMS_SPEC-152 JMS_SPEC-152]
| New method XAJMSContext#createXAJMSContext
|  
| 
|-  
| [http://java.net/jira/browse/JMS_SPEC-126 JMS_SPEC-126]
|  API to allow app servers to implement JMSContext without needing an additional connection pool
|  
| 
|-
|}

## Messaging features (minor)

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-159 JMS_SPEC-159 ]
|  Allow stop and close to be called from a message listener
|  
|            
|-
| [http://java.net/jira/browse/JMS_SPEC-151 JMS_SPEC-151 ]
|  Add repeating annotation support to @JMSConnectionFactoryDefinition and @JMSDestinationDefinition
|  
|  
|-



| [http://java.net/jira/browse/JMS_SPEC-149 JMS_SPEC-149]
|  Asynchronous Send Functionality Should Use Java SE 8 Completeable Future
|
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-148 JMS_SPEC-148 ]
| The Delivery Delay Feature Should Utilize the Java SE 8 Date/Time API
|
|
|-

| [http://java.net/jira/browse/JMS_SPEC-147 JMS_SPEC-147 ]
| Extend the @JMSConnectionFactory annotation to allow a resource reference to be defined
|
|
|-
| [http://java.net/jira/browse/JMS_SPEC-144 JMS_SPEC-144 ]
| StreamMessage#getBody
|
|   
|-
| [http://java.net/jira/browse/JMS_SPEC-139 JMS_SPEC-139]
| Clarify scope of ClientID between JavaDoc and specification
|   
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-138 JMS_SPEC-138]
| Clarify whether you can call createContext on a QueueConnectionFactory or TopicConnectionfactory
|  
|  
|-
| [http://java.net/jira/browse/JMS_SPEC-137 JMS_SPEC-137]
| Section 8.7 of the JMS 2.0 spec has a malformed sentence
|
| 
|- 
| [http://java.net/jira/browse/JMS_SPEC-124 JMS_SPEC-124]
| Sending a foreign message using a provider which does not support setJMSCorrelationIDAsBytes
|
|  A bit obscure and unimportant.
|-
| [http://java.net/jira/browse/JMS_SPEC-118 JMS_SPEC-118]
| MessageListeners should be as simple as lambda expressions
| 
|  
|-
| [http://java.net/jira/browse/JMS_SPEC-113 JMS_SPEC-113]
| Clarify the difference (if any) between JMSException.getLinkedException() and JMSException.getCause()
|
|  
|-
| [http://java.net/jira/browse/JMS_SPEC-110 JMS_SPEC-110]
| add JMS methods to access an Object's creator: Message.getSession(), Session.getConnection(), ...
|
|
|-
| [http://java.net/jira/browse/JMS_SPEC-109 JMS_SPEC-109]
| add method Destination.getName() 
|
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-108 JMS_SPEC-108]
| add generics to methods currently returning raw types
|
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-91 JMS_SPEC-91]
| New "relaxed message order" option
|  
|
|-
| [http://java.net/jira/browse/JMS_SPEC-85 JMS_SPEC-85] <br/>
| Clarify how Message.receiveNoWait() is expected to behave
|    
|   
|-
| [http://java.net/jira/browse/JMS_SPEC-79 JMS_SPEC-79]
| New factory methods to create BytesMessage and MapMessage and set the payload
|  
|
|-
| [http://java.net/jira/browse/JMS_SPEC-71 JMS_SPEC-71]
| Change XAConnectionFactory to extend ConnectionFactory
|  
|
|-
| [http://java.net/jira/browse/JMS_SPEC-68 JMS_SPEC-68]
| Add new method Session.acknowledge()
|  
|
|-
| [http://java.net/jira/browse/JMS_SPEC-67 JMS_SPEC-67]
| Relaxing the requirement to throw an exception if a message is sent to a deleted temp destination
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-66 JMS_SPEC-66]
| Define how MessageConsumer.receive should handle a thread interrupt
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-24 JMS_SPEC-24]
| Clarify classloader used in ObjectMessage.getObject() and/or provide new method getObject(ClassLoader classLoader)
|  
|
|-
| [http://java.net/jira/browse/JMS_SPEC-22 JMS_SPEC-22]
| Add JMS defined property JMSXGroupLast
|  
| 
|-
|}

## Messaging features (major)=### 

{|- border="1"
! ID
! Description
! Status
! Notes
|-  
| [http://java.net/jira/browse/JMS_SPEC-154 JMS_SPEC-154 ]
| Standardize Abstractions for Common Message Processing Patterns
| 
|   
|-
| [http://java.net/jira/browse/JMS_SPEC-142 JMS_SPEC-142]
| Standardize Dead Letter Queues
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-130 JMS_SPEC-130]
| Allow a JMSContext or Session to opt out of a Java EE transaction
|  
|   
|-
| [http://java.net/jira/browse/JMS_SPEC-95 JMS_SPEC-95]
| Individual message acknowledge mode
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-83 JMS_SPEC-83]
| Tighter specification of Expired Message Handling in Section 4.8 "Message Time-to-Live"
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-73 JMS_SPEC-73]
| Define how messages from a topic are delivered to clustered application server instances
| 
| Added to JMS 2.0 public draft but removed from the final draft
|-
| [http://java.net/jira/browse/JMS_SPEC-72 JMS_SPEC-72]
| Poison message management
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-59 JMS_SPEC-59]
| Basic metadata/management via JMS
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-58 JMS_SPEC-58]
| New method Message.copyMessage() to create a mutable copy of a received message
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-41 JMS_SPEC-41]
| Support topic hierarchies
| Awaiting proposals from original proposer
| 
|-  
| [http://java.net/jira/browse/JMS_SPEC-37 JMS_SPEC-37]
| Last Value Cache Feature for a topic.
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-36 JMS_SPEC-36]
| Allow messages to be delivered asynchronously in batches
| 
| This was deferred from JMS 2.0 until async receive was made simpler, so should be revisited for JMS 2.1
|-
| [http://java.net/jira/browse/JMS_SPEC-25 JMS_SPEC-25]
| Standardise the interface between a JMS provider and a Java EE application server
| 
| The JMS 2.0 considered making JCA support mandatory but rejected it. Any other ideas?
|-
| [http://java.net/jira/browse/JMS_SPEC-21 JMS_SPEC-21]
| Support for pre-acknowledge ack mode
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-18 JMS_SPEC-18]
| Standard set of server JMX MBeans
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-14 JMS_SPEC-14]
| Durable subscription browser
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-7 JMS_SPEC-7]
| Provide HTTP Binding
|  
|  
|- 
| [http://java.net/jira/browse/JMS_SPEC-5 JMS_SPEC-5]
| Multi-Value Support in Properties
|  
| 
|-
|}


## New acknowledgement modes=### 

This section contains various proposals for new acknowledgement modes.

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-169 JMS_SPEC-169] <br/>
| Vendor-defined acknowledgement modes
|
|
|-
| [http://java.net/jira/browse/JMS_SPEC-168 JMS_SPEC-168] <br/>
| No-acknowledge mode
|    
|
|-
| [http://java.net/jira/browse/JMS_SPEC-95 JMS_SPEC-95] <br/>
| Individual message acknowledge mode
|    
|   
|-
|}

## Resource creation and configuration=### 

This section contains various proposals affecting the creation and configuration of ConnectionFactory, Queue and Topic objects. Note that the requirements for Java SE applications, Java EE applications and resource adapters are different.

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-172 JMS_SPEC-172]
| Resource adapter: define standard destination and connection factory properties 
|  
| 
|-  
| [http://java.net/jira/browse/JMS_SPEC-90 JMS_SPEC-90]
| Provide simpler mechanism to refer to queues and topics in a portable way
| 
| Discussed for JMS 2.0 but consensus not reached
|-  
| [http://java.net/jira/browse/JMS_SPEC-89 JMS_SPEC-89]
| Define standard API to create and configure a ConnectionFactory in Java SE applications and by a Java EE container
| 
| Discussed for JMS 2.0 but consensus not reached
|}

## Application server integration.=### 

These issues are concerned with the API used to "plug in" a particular JMS provider into a particular application server

They don't affect the JMS API as used by user applications.

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-28 JMS_SPEC-28]
| Clarify how the JMS provider should interact with Transaction Managers.
| Awaiting proposals from original proposer
| 
|-  
| [http://java.net/jira/browse/JMS_SPEC-26 JMS_SPEC-26]
| Decide on the future of the optional Chapter 8 API "JMS Application Server Facilities"
| 
| No consensus yet 
|-  
|}

## Behaviour of JMS API in a Java EE application server=### 

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-156 JMS_SPEC-156 ]
| JMS does not adequately define the behaviour of getAcknowledgeMode, getTransacted and getSessionMode in Java EE
| 
| Deferred from JMS 2.0 Rev. A
|-
| [http://java.net/jira/browse/JMS_SPEC-145 JMS_SPEC-145 ]
| Allow the execution of async message sending on an application server if done within a ManagedExecutorService
| 
|   
|-
| [http://java.net/jira/browse/JMS_SPEC-131 JMS_SPEC-131]
| Allow client-acknowledgement and local transactions to be used in the Java EE web and EJB containe
|  
|  
|-
| [http://java.net/jira/browse/JMS_SPEC-129 JMS_SPEC-129]
| Resolve some undefined use cases when using Java EE bean-managed JTA transactions
|  
| 
|- 
| [http://java.net/jira/browse/JMS_SPEC-92 JMS_SPEC-92]
| Session.commit() etc should require TransactionInProgressException to be thrown if called in a JTA transaction
|  
| 
|-   
|}

## API improvements for JMS MDBs=### 

This section contains proposals for improvements to the whole programming model for JMS MDBs. This includes improvements to the ways JMS MDBs are configured, making JMS MDBs more flexible, and allowing objects which are not MDBs to list for JMS messages. Changes may require an update to the EJB specification as well as to JMS.

Specific functional improvements to JMS MDBs (rather than improvements to the API) are listed separately in [/jms-spec/pages/JMS21Planning2#Functional_improvements_to_JMS_MDBs Functional improvements to JMS MDBs] below.

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-174 JMS_SPEC-174]
| Define a standard way to specify the resource adapter used by a JMS MDB
|  
| [https://java.net/jira/browse/EJB_SPEC-127 EJB_SPEC-127] 
|-  
| [http://java.net/jira/browse/JMS_SPEC-146 JMS_SPEC-146]
| Allow the registration of a message listener via a connection when done within an ManagedExecutorService.
|  
|  
|-  
| [http://java.net/jira/browse/JMS_SPEC-134 JMS_SPEC-134]
| Declarative Annotation Based JMS Listeners
|  
| 
|-  
| [http://java.net/jira/browse/JMS_SPEC-116 JMS_SPEC-116]
| Take advantage of EJB 3.2's RA improvement for MDBs
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-100 JMS_SPEC-100]
| Allow Java EE components other than MDBs to consume messages asynchronously
|  
| 
|-  
|}

## Functional improvements to JMS MDBs=### 

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/JMS_SPEC-143 JMS_SPEC-143]
| Define standard MDB activation properties to allow the MDB pool to be configured
|
|   
|-
| [http://java.net/jira/browse/JMS_SPEC-141 JMS_SPEC-141]
| Allow async delivery to temporary destinations in Java EE
|  
| 
|-     
| [http://java.net/jira/browse/JMS_SPEC-117 JMS_SPEC-117]
| Specifying redelivery behaviour when a JMS MDB performs rollback
|  
| 
|-  
| [http://java.net/jira/browse/JMS_SPEC-88 JMS_SPEC-88]
| Bind JMS to CDI events and/or business interfaces
|  
| 
|-
| [http://java.net/jira/browse/JMS_SPEC-74 JMS_SPEC-74]
| Define lifecycle of durable subscriptions used by MDBs
|  
| 
|-
|}

## EJB spec improvements

This section lists a number of improvements to the EJB specification that have been proposed by the JMS specification lead. Since these are not specific to JMS they need to be considered by the EJB expert group.

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/EJB_SPEC-126 EJB_SPEC-126]
| Allow listener method to always be determined at runtime by resource adapter
|  
| 
|-
| [http://java.net/jira/browse/EJB_SPEC-127 EJB_SPEC-127]
| Define a standard way to specify the resource adapter used by a JMS MDB
|  
| 
|}

## Connector spec improvements

This section lists a number of improvements to the Connector specification that have been proposed by the JMS specification lead.

{|- border="1"
! ID
! Description
! Status
! Notes
|-
| [http://java.net/jira/browse/CONNECTOR_SPEC-15 CONNECTOR_SPEC-15]
| Define a standard way to specify the resource adapter used by a JMS MDB
|  
| Logged at request of JCA spec lead, to track any JCA changes needed to support [http://java.net/jira/browse/EJB_SPEC-127 EJB_SPEC-127]
|}
