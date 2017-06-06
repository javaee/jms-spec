# JMS 2.1 Planning Long List

Improvements to the JMS specification are managed using issues in JIRA. Here is a classified list of all the open issues. Only a subset of these issues will actually make it into JMS 2.1. See the actual [JMS 2.1 plan](/jms-spec/pages/JMS21Planning).

* auto-gen TOC:
{:toc}

## Corrections and clarifications (minor)

ID | Description | Status | Notes
:--- | :--- | :--- | :---
[JMS_SPEC-175](https://github.com/javaee/jms-spec/issues/175) | Fix javadocs to remove need to disable doclint | |
[JMS_SPEC-173](https://github.com/javaee/jms-spec/issues/173) | Javadocs for `JMSContext#start` should mention that it is usually called automatically | |
[JMS_SPEC-171](https://github.com/javaee/jms-spec/issues/171) | Typo in javadoc for JMSDestinationDefinition | |
[JMS_SPEC-153](https://github.com/javaee/jms-spec/issues/153) | Correction to 7.3.8. "Use of the CompletionListener by the JMS provider" | |
[JMS_SPEC-150](https://github.com/javaee/jms-spec/issues/150) | Add guidance on when to use receiveBody and how to handle MessageFormatRuntimeException | |
[JMS_SPEC-121](https://github.com/javaee/jms-spec/issues/121) | Injection of `JMSContext` objects not possible in a WebSocket `@OnMessage` or `@OnClose` callback method | | Not really a bug, but may require noting in the spec
[JMS_SPEC-123](https://github.com/javaee/jms-spec/issues/123) | `JMSProducer#setJMSCorrelationIDAsBytes` should be allowed to throw a `java.lang.UnsupportedOperationException` | |
[JMS_SPEC-104](https://github.com/javaee/jms-spec/issues/104) | API javadocs need to conform to Oracle accessibility standards and W3C HTML validation standards | |  Partially completed for JMS 2.0 but one issue remains.
[JMS_SPEC-84](https://github.com/javaee/jms-spec/issues/84) | Clarify when acknowledged persistent messages may be dropped | | Will defer to JMS 2.1
[JMS_SPEC-75](https://github.com/javaee/jms-spec/issues/75) | Ambiguous javadocs for Connection.createConnectionConsumer and createDurableConnectionConsumer | | Carried forward from JMS 2.0
[JMS_SPEC-47](https://github.com/javaee/jms-spec/issues/47) | Deprecate domain-specific APIs and propose for removal | | Deprecation not allowed by Java EE policy. Could make a recommendation not to use.  
[JMS_SPEC-3](https://github.com/javaee/jms-spec/issues/3) | Fix JavaDocs to reflect missing NumberFormatException from API methods | | Carried forward from JMS 2.0
[JMS_SPEC-2](https://github.com/javaee/jms-spec/issues/2) | Fix JavaDocs to reflect missing IllegalStateException from API methods | | Carried forward from JMS 2.0

## Corrections (major) 

ID | Description | Status | Notes
:--- | :--- | :--- | :---
[JMS_SPEC-152](https://github.com/javaee/jms-spec/issues/152) | New method `XAJMSContext#createXAJMSContext` | | 
[JMS_SPEC-126](https://github.com/javaee/jms-spec/issues/126) |  API to allow app servers to implement `JMSContext` without needing an additional connection pool | | 

## Messaging features (minor)

ID | Description | Status | Notes
:--- | :--- | :--- | :---
[JMS_SPEC-159](https://github.com/javaee/jms-spec/issues/159) | Allow stop and close to be called from a message listener | |
[JMS_SPEC-151](https://github.com/javaee/jms-spec/issues/151) | Add repeating annotation support to `@JMSConnectionFactoryDefinition` and `@JMSDestinationDefinition` | |
[JMS_SPEC-149](https://github.com/javaee/jms-spec/issues/149) | Asynchronous Send Functionality Should Use Java SE 8 Completeable Future | | 
[JMS_SPEC-148](https://github.com/javaee/jms-spec/issues/148) | The Delivery Delay Feature Should Utilize the Java SE 8 Date/Time API | |
[JMS_SPEC-147](https://github.com/javaee/jms-spec/issues/147) | Extend the `@JMSConnectionFactory` annotation to allow a resource reference to be defined | |
[JMS_SPEC-144](https://github.com/javaee/jms-spec/issues/144) | `StreamMessage#getBody` | |   
[JMS_SPEC-139](https://github.com/javaee/jms-spec/issues/139) | Clarify scope of ClientID between JavaDoc and specification | |
[JMS_SPEC-138](https://github.com/javaee/jms-spec/issues/138) | Clarify whether you can call createContext on a `QueueConnectionFactory` or TopicConnectionfactory | | 
[JMS_SPEC-137](https://github.com/javaee/jms-spec/issues/137) | Section 8.7 of the JMS 2.0 spec has a malformed sentence | | 
[JMS_SPEC-124](https://github.com/javaee/jms-spec/issues/124) | Sending a foreign message using a provider which does not support setJMSCorrelationIDAsBytes | | A bit obscure and unimportant.
[JMS_SPEC-118](https://github.com/javaee/jms-spec/issues/118) | MessageListeners should be as simple as lambda expressions | |  
[JMS_SPEC-113](https://github.com/javaee/jms-spec/issues/113) | Clarify the difference (if any) between `JMSException.getLinkedException()` and JMSException.getCause() | |  
[JMS_SPEC-110](https://github.com/javaee/jms-spec/issues/110) | add JMS methods to access an Object's creator: `Message.getSession()`, `Session.getConnection()`, ... | |
[JMS_SPEC-109](https://github.com/javaee/jms-spec/issues/109) | add method Destination.getName() | | 
[JMS_SPEC-108](https://github.com/javaee/jms-spec/issues/108) | add generics to methods currently returning raw types | | 
[JMS_SPEC-91](https://github.com/javaee/jms-spec/issues/91) | New "relaxed message order" option | |
[JMS_SPEC-85](https://github.com/javaee/jms-spec/issues/85) | Clarify how `Message.receiveNoWait()` is expected to behave | |   
[JMS_SPEC-79](https://github.com/javaee/jms-spec/issues/79) | New factory methods to create `BytesMessage` and `MapMessage` and set the payload | |
[JMS_SPEC-71](https://github.com/javaee/jms-spec/issues/71) | Change `XAConnectionFactory` to extend `ConnectionFactory` | |
[JMS_SPEC-68](https://github.com/javaee/jms-spec/issues/68) | Add new method `Session.acknowledge()` | |
[JMS_SPEC-67](https://github.com/javaee/jms-spec/issues/67) | Relaxing the requirement to throw an exception if a message is sent to a deleted temp destination | | 
[JMS_SPEC-66](https://github.com/javaee/jms-spec/issues/66) | Define how `MessageConsumer.receive` should handle a thread interrupt | | 
[JMS_SPEC-24](https://github.com/javaee/jms-spec/issues/24) | Clarify classloader used in `ObjectMessage.getObject()` and/or provide new method getObject(ClassLoader classLoader) | |
[JMS_SPEC-22](https://github.com/javaee/jms-spec/issues/22) | Add JMS defined property `JMSXGroupLast` | | 

## Messaging features (major) 

ID | Description | Status | Notes
:--- | :--- | :--- | :--- 
[JMS_SPEC-154](https://github.com/javaee/jms-spec/issues/154) | Standardize Abstractions for Common Message Processing Patterns | |   
[JMS_SPEC-142](https://github.com/javaee/jms-spec/issues/142) | Standardize Dead Letter Queues | | 
[JMS_SPEC-130](https://github.com/javaee/jms-spec/issues/130) | Allow a JMSContext or Session to opt out of a Java EE transaction | |   
[JMS_SPEC-95](https://github.com/javaee/jms-spec/issues/95) | Individual message acknowledge mode | | 
[JMS_SPEC-83](https://github.com/javaee/jms-spec/issues/83) | Tighter specification of Expired Message Handling in Section 4.8 "Message Time-to-Live" | | 
[JMS_SPEC-73](https://github.com/javaee/jms-spec/issues/73) | Define how messages from a topic are delivered to clustered application server instances | | Added to JMS 2.0 public draft but removed from the final draft
[JMS_SPEC-72](https://github.com/javaee/jms-spec/issues/72) | Poison message management | | 
[JMS_SPEC-59](https://github.com/javaee/jms-spec/issues/59) | Basic metadata/management via JMS | | 
[JMS_SPEC-58](https://github.com/javaee/jms-spec/issues/58) | New method Message.copyMessage() to create a mutable copy of a received message | | 
[JMS_SPEC-41](https://github.com/javaee/jms-spec/issues/41) | Support topic hierarchies | Awaiting proposals from original proposer | 
[JMS_SPEC-37](https://github.com/javaee/jms-spec/issues/37) | Last Value Cache Feature for a topic. | | 
[JMS_SPEC-36](https://github.com/javaee/jms-spec/issues/36) | Allow messages to be delivered asynchronously in batches | | This was deferred from JMS 2.0 until async receive was made simpler, so should be revisited for JMS 2.1
[JMS_SPEC-25](https://github.com/javaee/jms-spec/issues/25) | Standardise the interface between a JMS provider and a Java EE application server | | The JMS 2.0 considered making JCA support mandatory but rejected it. Any other ideas?
[JMS_SPEC-21](https://github.com/javaee/jms-spec/issues/21) | Support for pre-acknowledge ack mode | | 
[JMS_SPEC-18](https://github.com/javaee/jms-spec/issues/18) | Standard set of server JMX MBeans | | 
[JMS_SPEC-14](https://github.com/javaee/jms-spec/issues/14) | Durable subscription browser | | 
[JMS_SPEC-7](https://github.com/javaee/jms-spec/issues/7) | Provide HTTP Binding | |  
[JMS_SPEC-5](https://github.com/javaee/jms-spec/issues/5) | Multi-Value Support in Properties | | 

## New acknowledgement modes 

This section contains various proposals for new acknowledgement modes.

ID | Description | Status | Notes
:--- | :--- | :--- | :--- 
[JMS_SPEC-169](https://github.com/javaee/jms-spec/issues/169) | Vendor-defined acknowledgement modes | |
[JMS_SPEC-168](https://github.com/javaee/jms-spec/issues/168) | No-acknowledge mode | |
[JMS_SPEC-95](https://github.com/javaee/jms-spec/issues/95) | Individual message acknowledge mode | |   

## Resource creation and configuration 

This section contains various proposals affecting the creation and configuration of ConnectionFactory, Queue and Topic objects. Note that the requirements for Java SE applications, Java EE applications and resource adapters are different.

ID | Description | Status | Notes
:--- | :--- | :--- | :--- 
[JMS_SPEC-172](https://github.com/javaee/jms-spec/issues/172) | Resource adapter: define standard destination and connection factory properties | |
[JMS_SPEC-90](https://github.com/javaee/jms-spec/issues/90) | Provide simpler mechanism to refer to queues and topics in a portable way | | Discussed for JMS 2.0 but consensus not reached
[JMS_SPEC-89](https://github.com/javaee/jms-spec/issues/89) | Define standard API to create and configure a ConnectionFactory in Java SE applications and by a Java EE container | | Discussed for JMS 2.0 but consensus not reached

## Application server integration. 

These issues are concerned with the API used to "plug in" a particular JMS provider into a particular application server

They don't affect the JMS API as used by user applications.

ID | Description | Status | Notes
:--- | :--- | :--- | :--- 
[JMS_SPEC-28](https://github.com/javaee/jms-spec/issues/28) | Clarify how the JMS provider should interact with Transaction Managers. | Awaiting proposals from original proposer |
[JMS_SPEC-26](https://github.com/javaee/jms-spec/issues/26) | Decide on the future of the optional Chapter 8 API "JMS Application Server Facilities" | | No consensus yet 

## Behaviour of JMS API in a Java EE application server 

ID | Description | Status | Notes
:--- | :--- | :--- | :--- 
[JMS_SPEC-156](https://github.com/javaee/jms-spec/issues/156) | JMS does not adequately define the behaviour of getAcknowledgeMode, getTransacted and getSessionMode in Java EE | | Deferred from JMS 2.0 Rev. A
[JMS_SPEC-145](https://github.com/javaee/jms-spec/issues/145) | Allow the execution of async message sending on an application server if done within a ManagedExecutorService | |   
[JMS_SPEC-131](https://github.com/javaee/jms-spec/issues/131) | Allow client-acknowledgement and local transactions to be used in the Java EE web and EJB container | |  
[JMS_SPEC-129|https://github.com/javaee/jms-spec/issues/129] | Resolve some undefined use cases when using Java EE bean-managed JTA transactions | | 
[JMS_SPEC-92](https://github.com/javaee/jms-spec/issues/92) | Session.commit() etc should require TransactionInProgressException to be thrown if called in a JTA transaction | | 

## API improvements for JMS MDBs 

This section contains proposals for improvements to the whole programming model for JMS MDBs. This includes improvements to the ways JMS MDBs are configured, making JMS MDBs more flexible, and allowing objects which are not MDBs to list for JMS messages. Changes may require an update to the EJB specification as well as to JMS.

Specific functional improvements to JMS MDBs (rather than improvements to the API) are listed separately in [Functional improvements to JMS MDBs](/jms-spec/pages/JMS21LongList#functional-improvements-to-jms-mdbs) below.

ID | Description | Status | Notes
:--- | :--- | :--- | :--- 
[JMS_SPEC-174](https://github.com/javaee/jms-spec/issues/174) | Define a standard way to specify the resource adapter used by a JMS MDB |  | [EJB_SPEC-127](https://github.com/javaee/ejb-spec/issues/127) 
[JMS_SPEC-146](https://github.com/javaee/jms-spec/issues/146) | Allow the registration of a message listener via a connection when done within an ManagedExecutorService. | |  
[JMS_SPEC-134](https://github.com/javaee/jms-spec/issues/134) | Declarative Annotation Based JMS Listeners | | 
[JMS_SPEC-116](https://github.com/javaee/jms-spec/issues/116) | Take advantage of EJB 3.2's RA improvement for MDBs | | 
[JMS_SPEC-100](https://github.com/javaee/jms-spec/issues/100) | Allow Java EE components other than MDBs to consume messages asynchronously | | 

## Functional improvements to JMS MDBs 

ID | Description | Status | Notes
:--- | :--- | :--- | :--- 
[JMS_SPEC-143](https://github.com/javaee/jms-spec/issues/143) | Define standard MDB activation properties to allow the MDB pool to be configured | |   
[JMS_SPEC-141](https://github.com/javaee/jms-spec/issues/141) | Allow async delivery to temporary destinations in Java EE | | 
[JMS_SPEC-117](https://github.com/javaee/jms-spec/issues/117) | Specifying redelivery behaviour when a JMS MDB performs rollback | | 
[JMS_SPEC-88](https://github.com/javaee/jms-spec/issues/88) | Bind JMS to CDI events and/or business interfaces | | 
[JMS_SPEC-74](https://github.com/javaee/jms-spec/issues/74) | Define lifecycle of durable subscriptions used by MDBs | | 

## EJB spec improvements

This section lists a number of improvements to the EJB specification that have been proposed by the JMS specification lead. Since these are not specific to JMS they need to be considered by the EJB expert group.

ID | Description | Status | Notes
:--- | :--- | :--- | :--- 
[EJB_SPEC-126](https://github.com/javaee/ejb-spec/issues/126) | Allow listener method to always be determined at runtime by resource adapter | | 
[EJB_SPEC-127](https://github.com/javaee/ejb-spec/issues/127) | Define a standard way to specify the resource adapter used by a JMS MDB | | 

## Connector spec improvements

This section lists a number of improvements to the Connector specification that have been proposed by the JMS specification lead.

ID | Description | Status | Notes
:--- | :--- | :--- | :--- 
[CONNECTOR_SPEC-15](https://github.com/javaee/connector-spec/issues/15)| Define a standard way to specify the resource adapter used by a JMS MDB | | Logged at request of JCA spec lead, to track any JCA changes needed to support [EJB_SPEC-127](https://github.com/javaee/ejb-spec/issues/127)

