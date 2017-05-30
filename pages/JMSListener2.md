# More flexible JMS MDBs (Version 2)

This page contains version 2 of proposals to simplify the configuration of JMS MDBs in JMS 2.1 and Java EE 8. 

Comments are invited, especially to the various issues mentioned. See [How to get involved in JMS 2.1](/jms-spec/pages/JMS21#how-to-get-involved-in-jms-21).

See the [summary of changes](#changes-from-version-1) compared to [Version 1 of these proposals](/jms-spec/pages/JMSListener).

* auto-gen TOC:
{:toc}

## Changes from version 1 
If you haven't read version 1 then you may prefer to skip this section and go straight to the following section, [Background](#background). When you've read the rest you may wish to come back here and review the list of new issues added in this section.

<b>Multiple callback methods</b>

* A MDB may define multiple callback methods. The earlier proposal to allow only a single callback method has been dropped. Each callback method will be treated as representing a separate consumer, and so may specify a different queue or topic, connection factory, durable subscription, message selector etc.

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>New Issue I17:</b> Should multiple callback methods be permitted or should MDBs be restricted to a single callback method as in the previous version?. There is an argument that allowing multiple callback methods may be confusing for developers, who may not realise the concurrency implications. It may also make implementation more complex for application servers that automatically calculate the size of the MDB pool. Comments are invited.
</td></tr></table>

<b>Allowed return types</b>

* The callback method must return void. The earlier proposal to allow any return type, but ignore any returned value, has been dropped since it prevents a future version of JMS defining behaviour for returned values. If the `@JMSListener` annotation is used on a method which does not return void then deployment must fail. If a resource adapter is used then `endpointActivation` must throw an exception.

<b>Required annotations</b>

* Each callback method must be annotated with `@JMSListener`. If this annotation is omitted then the method will not be treated as a callback method; any other callback annotations are ignored. (This restriction does not apply to the `javax.jms.MessageListener` method `onMessage(Message m)`).

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>New Issue I18:</b> Should we relax the requirement for each callback method (other than  the <tt>javax.jms.MessageListener</tt> method `onMessage(Message m)`) to be annotated with <tt>@JMSListener</tt>, and allow  the presence of <i>any</i> of the annotations <tt>@JMSConnectionFactory</tt>, <tt>@JMSListener</tt>, <tt>@SubscriptionDurability</tt>, <tt>@ClientId</tt>, <tt>@SubscriptionName</tt>, <tt>@MessageSelector</tt> or <tt>@Acknowledge</tt> to be sufficient to designate a callback method?
</td></tr></table>

<b>If the MDB implements javax.jms.MessageListener</b>

* If the MDB implements `javax.jms.MessageListener` then the new callback annotations may be applied to the `onMessage(Message m)` method.  Any of the new callback annotations may be specified; it is not necessary to specify `@JMSListener`.

* If the MDB implements `javax.jms.MessageListener` then application-defined callback methods (in addition to `onMessage(Message m)`) may only be implemented if the MDB is explicitly configured to specify that its listener interface is `JMSMessageDrivenBean` (e.g. by using the annotation `@MessageDriven(messageListenerInterface=JMSMessageDrivenBean.class)`). This is required to satisfy the requirements of EJB 3.2 section 5.6.5 "Message-Driven Bean with No-Methods Listener Interface".  [EJB spec issue 126](https://github.com/javaee/ejb-spec/issues/126) proposes the removal of this requirement.

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>New Issue I19:</b> This requirement may be a cause of unexpected errors, since the resource adapter has no way to verify at deployment  time whether the MDB has been configured to specify that its listener interface is <tt>JMSMessageDrivenBean</tt>. The <tt>endpointActivation</tt> method has no access to this information. This means that the resource adapter will only discover when it tries to deliver a message that the message endpoint does not implement the callback method. Note that  although <tt>endpointActivation</tt> has access to an instance of <tt>MessageEndpointFactory</tt> this cannot be used to examine what methods are implemented by the endpoint class since  it may not be valid to call <tt>createEndpoint</tt> until after deployment has completed. Note that <a href="https://github.com/javaee/ejb-spec/issues/126">EJB spec issue 126</a>  would remove this issue.
</td></tr></table>

<b>Incompatible method parameters</b>

* The earlier proposal to set a parameter to null if it cannot be set because its type is incompatible has been dropped. 

* If any of the parameters of the callback method cannot be set because they have an incompatible type then callback method will not be invoked. Such messages may either be discarded or delivered to a provider-specific dead message queue.

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>New Issue I20:</b> Is this an adequate definition of the required behaviour when parameters of the callback method cannot be set?
</td></tr></table>

<b>Callback methods that throw exceptions</b>

* Callback methods will be allowed to declare and throw exceptions. Exceptions thrown by the callback method (including unchecked exceptions thrown by the `onMessage` method of a `MessageListener`) will be handled by the EJB container as defined in the EJB 3.2 specification section 9.3.4 "Exceptions thrown from Message-Driven Bean Message Listener methods".  This defines whether or not any transaction in progress is committed or rolled back, depending on whether or not the exception is a "system exception" or an "application exception", whether or not the application exception is specified as causing rollback, and whether or not the application has called `setRollbackOnly`. It also defines whether or not the MDB instance is discarded. If the transaction is rolled back, or a transaction is not being used, then the message will be redelivered. 

* The JMS provider should detect repeated attempts to redeliver the same message to a MDB. Such messages may either be discarded or delivered to a provider-specific dead message queue. (Note that this not completely new to JMS: JMS 2.1 section 8.7 refers to a JMS provider "giving up" after a message has been redelivered a certain number of times).

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>New Issue I21:</b> Is this an adequate definition of the required behaviour when a callback method throws an exception?
</td></tr></table>

<b>Other changes</b>

* The ability to specify the queue or topic by destination name rather than JNDI name has been removed because JMS considers this name to be non-portable. If possible, JMS 2.1 will define a portable way to obtain a Queue or Topic object that doesn't require JNDI, though this will be kept as a separate issue.

<b>Other new issues</b>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I22:</b> How about replacing <tt>@JMSListener</tt> with separate <tt>@JMSQueueListener</tt> and <tt>@JMSTopicListener</tt> annotations? This would remove the need for a separate "type" attribute. 
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I23:</b> Currently the <tt>acknowledgeMode</tt> activation property is rather confusing, as it is ignored when the bean is configured to use container-managed transactions. It is only used when the MDB is configured to use bean-managed transactions, such as with the class-level annotation <tt>@TransactionManagement(TransactionManagementType.BEAN)</tt>.  The same confusion will apply if we define the new <tt>@Acknowledge</tt> annotation to work the same way as <tt>acknowledgeMode</tt>. 
<br><br>
In fact if you want the MDB to consume messages without a transaction and using automatic-acknowledgement then all you need to do is to set <tt>@TransactionManagement(TransactionManagementType.BEAN)</tt>. You don't actually need to set the <tt>acknowledgeMode</tt> activation property, since it defaults to auto-ack anyway. The only reason you ever need to use the <tt>acknowledgeMode</tt> activation property is if you wanted to specify <tt>DUPS_OK</tt>.  
<br><br>
We can't change the behaviour of <tt>acknowledgeMode</tt>, but it would be better if we could replace the existing <tt>@TransactionManagement</tt> annotation and the proposed <tt>@AcknowledgeMode</tt> annotation with a single annotation which could define both at the same time. 
</td></tr></table>

## Background 

There have been several proposals to improve the ways that JMS applications can consume messages asynchronously:

* In [https://java.net/jira/browse/JMS_SPEC-116 JMS_SPEC-116] John Ament proposed (back in March 2013, just as JMS 2.0 was being finalised) improving the ways that JMS MDBs were defined, taking advantage of some new features that were added to EJB 3.2 and JCA 1.7 (at the suggestion of David Blevins) just before they were released. 

* In [https://java.net/jira/browse/JMS_SPEC-134 JMS_SPEC-134] Reza Rahman proposed that JMS defined some annotations that allowed any CDI bean to listen for JMS messages

* In [https://java.net/jira/browse/JMS_SPEC-100 JMS_SPEC-100]  Bruno Borges proposed improving the ways that JMS MDBs were defined, though in the subsequent discussion he proposed that this could be extended to other types of Java EE class such as session beans. That makes this essentially a combination of the other two proposals.

## Goals 

The proposals on this page are addressed at the first of these proposals,  [https://java.net/jira/browse/JMS_SPEC-116 JMS_SPEC-116]:

* The requirement for a MDB that consumes JMS messages to implement`javax.jms.MessageListener` will be removed.

* Instead of having to provide a callback method `void onMessage(Message m)`, the callback method may have any name and can have multiple parameters of a variety of types. The developer can use parameter annotations to specify that a parameter must be set to the message object, to the message body, or to a specified message header or message property.

* Instead of having to use the general-purpose activation property mechanism to define what messages the MDB will receive, the developer can specify a set of JMS-specific annotations. This is more obvious, less verbose and allows the compiler to detect spelling errors. 

These new annotations will initially be available only on MDBs. This offers a large scope for improvement without the need to consider issues such as listener lifecycle, listener pooling and resource adapter integration. A later stage in the development of JMS 2.1 will consider extending them to other types of Java EE object such as CDI managed beans.

## Specifying the callback method 
<br/>
In Java EE 7, a JMS MDB must implement the `javax.jms.MessageListener` interface. This means that the callback method must be called `onMessage`, it must return `void` and it must have a single parameter of type `Message`.
```
 @MessageDriven(activationConfig = {
   @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/requestQueue"),
   @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
 })
 public class MyMessageBean implements MessageListener {
 
   public void onMessage(Message message){
     ...
   }
 
 }
```

Although this option will remain, it is proposed in Java EE 8 to remove the requirement for a JMS MDB to implement the `javax.jms.MessageListener` interface. Instead the developer can use the `@JMSListener` annotation to designate any method to be the callback method.
```
 @MessageDriven
 public class MyMessageBean <b>implements JMSMessageDrivenBean</b> {
 
   <b>@JMSListener</b>(lookup="java:global/Trades", type=JMSListener.Type.QUEUE)
   public void processTrade(TextMessage tradeMessage){
     ...
   }
 
 }
```

<b>This is still a MDB</b>

* The listener class is still a message-driven bean and so must be configured as one, either by adding the `@MessageDriven` annotation or using the `&lt;message-driven&gt;` element in the deployment descriptor. It may also be necessary to specify which resource adapter should be used.

<b>Application-defined callback methods</b>

* A JMS MDB may have any number of application-defined callback methods. Each callback method will be treated as representing a separate consumer, and so may specify a different queue or topic, connection factory, durable subscription, message selector etc. 

* Each application-defined callback method must be specified using the annotation `@JMSListener`. The `@JMSListener` annotation may also be used to specify the queue or topic from where messages are to be received. Additional information about how the consumer is configured may be specified using the annotations `@JMSConnectionFactory`, `@Acknowledge`, `@SubscriptionDurability`, `@ClientId`, `@SubscriptionName` or `@MessageSelector`. 

* A callback method may have any number of parameters. Depending on the parameter type and any parameter annotations, each parameter will may be set to the message, the message body, a message header or a message property. 

* Each application-defined callback method must return void.

* An MDB with application-defined callback methods must implement the interface `javax.jms.JMSMessageDrivenBean` This is a new marker interface which defines no methods, and is required to satisfy the requirements of EJB 3.2 section 5.6.5 "Message-Driven Bean with No-Methods Listener Interface".   (Note that it is hoped that the EJB spec can be updated so that this marker interface will not actually be needed. See  [https://java.net/jira/browse/EJB_SPEC-115 EJB_SPEC-115]  and  [https://java.net/jira/browse/EJB_SPEC-126 EJB_SPEC-126] ) 

* Both the callback method and the `JMSMessageDrivenBean` interface may be inherited.

<b>MDBs that implement javax.jms.MessageListener</b>

* JMS MDBs may continue to implement `javax.jms.MessageListener` as they do now. In this case they must implement the callback method `onMessage(Message m)`.  Any (or none) of the new callback annotations may be applied to this method; it is not necessary to specify `@JMSListener`.

* If the MDB implements `javax.jms.MessageListener` then additional callback methods (in addition to `onMessage(Message m)`) may only be implemented if the MDB also implements `JMSMessageDrivenBean` and is also explicitly configured to specify that its listener interface is `JMSMessageDrivenBean` (e.g. by using the annotation `@MessageDriven(messageListenerInterface=JMSMessageDrivenBean.class)`). 

<b>Threading rules</b>

These proposals do not change the existing threading rules for MDBs, even for MDBs with multiple callback methods. These rules are defined in the EJB 3.2 specification:

<table style="text-align:left; margin-left:16px"> <tr> <td style="text-align:left;">
5.4.11 Serializing message-driven bean methods

The container serializes calls to each message-driven bean instance. Most containers will support many instances of a message-driven bean executing concurrently; however, each instance sees only a serialized sequence of method calls. Therefore, a message-driven bean does not have to be coded as reentrant.

The container must serialize all the container-invoked callbacks (e.g., lifecycle callback interceptor methods and timeout callback methods), and it must serialize these callbacks with the message listener method calls.
</td></tr></table>

<b>Exceptions</b>

* Callback methods will be allowed to declare and throw exceptions. Exceptions thrown by the callback method (including unchecked exceptions thrown by the `onMessage` method of a `MessageListener`) will be handled by the EJB container as defined in the EJB 3.2 specification section 9.3.4 "Exceptions thrown from Message-Driven Bean Message Listener methods".  This defines whether or not any transaction in progress is committed or rolled back, depending on whether or not the exception is a "system exception" or an "application exception", whether or not the application exception is specified as causing rollback, and whether or not the application has called `setRollbackOnly`. It also defines whether or not the MDB instance is discarded. If the transaction is rolled back, or a transaction is not being used, then the message will be redelivered. 

* The JMS provider should detect repeated attempts to redeliver the same message to a MDB. Such messages may either be discarded or delivered to a provider-specific dead message queue. (Note that this not completely new to JMS: JMS 2.1 section 8.7 refers to a JMS provider "giving up" after a message has been redelivered a certain number of times).

<b>Implementation</b>

* Application servers that implement JMS MDBs using a resource adapter should be able to implement these new features by enhancing the resource adapter itself. Few changes to the EJB container itself will be required. Application servers that implement JMS MDBs directly, without using a resource adapter, will of course need to implement these features in the EJB container itself.

<b>Specifications affected</b>

* Since MDBs are defined in the EJB specification, not the JMS specification, it will almost certainly be necessary to work with the EJB maintenance lead to create a revision of the EJB spec containing these new features.

* In the JMS specification itself, chapter 13 "Resource adapter" (which contains optional recommendations for a JMS resource adapter) will be extended to cover these new features.

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I1:</b> Deleted
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I2:</b> Deleted
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I3:</b> The EJB specification does not define a standard way to associate a MDB with a resource adapter. JMS MDBs that require the use of a resource adapter will continue to need to specify the resource adapter in a non-portable way, either using the app-server-specific deployment descriptor (e.g. `glassfish-ejb-jar.xml`) or by using a default resource adapter provided by the application server.  (Note that it is hoped that the EJB specification can be updated to define a standard way to associate a MDB with a resource adapter. See [https://java.net/jira/browse/EJB_SPEC-127 EJB_SPEC-127])
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I4:</b> Deleted (superseded by New Issue I17)
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I5:</b> It would be desirable to avoid the need to implement  `javax.jms.JMSMessageDrivenBean` since this is needed purely to satisfy EJB 3.2.   [https://java.net/jira/browse/EJB_SPEC-115 EJB_SPEC-115]  and  [https://java.net/jira/browse/EJB_SPEC-126 EJB_SPEC-126] propose removal of this requirement from the next version of EJB.  
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I6:</b> The reason why these annotations cannot be applied to the `onMessage` method of a `MessageListener` is that `MessageListener` is not a no-method interface, which means the resource adapter cannot access the methods of the MDB implementation class. It may be possible to change the EJB specification to allow this restriction to be removed.
</td></tr></table>

## Specifying what messages will be received 
<br/>
Before it can be used, a JMS MDB must specify where the messages will come from and how they will be received.  In Java EE 7 these are specified using "activation properties", each of which has a  String name and a String value. The name and value of each property must be hardcoded into either the application code or the deployment descriptor, and the developer gets no help from the compiler or schema to check that they are using the correct name and setting it to an appropriate value. The syntax itself is also cumbersome.
```
 @MessageDriven(activationConfig = {
   @ActivationConfigProperty(
     propertyName = "connectionFactoryLookup", propertyValue = "java:global/MyCF"),
   @ActivationConfigProperty(
     propertyName = "destinationLookup", propertyValue = "java:global/java:global/Trades"),
   @ActivationConfigProperty
     (propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
   @ActivationConfigProperty(
     propertyName = "clientId", propertyValue = "Consumer123"),
   @ActivationConfigProperty(
     propertyName = "subscriptionDurability", propertyValue = "Durable"),
   @ActivationConfigProperty(
     propertyName = "subscriptionName", propertyValue = "Subscription456"),
   @ActivationConfigProperty(
     propertyName = "messageSelector", propertyValue = "ticker='ORCL'")
 }
 public class MyMessageBean implements MessageListener {
   ...
 }
```

In Java EE 8 a JMS MDB may continue to specify activation properties, even if the MDB does not implement `javax.jms.MessageListener`. However they will be superseded by a new set of JMS-specific annotations which allow each activation property to be configured by a JMS-specific annotation on the callback method itself.
```
   @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
   @JMSConnectionFactory("java:global/MyCF")
   @SubscriptionDurability(SubscriptionDurability.Mode.DURABLE)
   @ClientId("myClientID1")  
   @SubscriptionName("mySubName1")
   @MessageSelector("ticker='ORCL'")
   @Acknowledge(Acknowledge.Mode.DUPS_OK_ACKNOWLEDGE)
   public void giveMeAMessage(Message message) {
     ...
   }
```
These annotations are introduced in more detail in the following sections.

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I7:</b> Any alternative proposals for the `@JMSConnectionFactory`, `@Acknowledge`, `@SubscriptionDurability`, `@ClientId`, `@SubscriptionName` or `@MessageSelector` annotations?
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I8:</b> Should JMS define a set of deployment descriptor which correspond to these annotations, and which can be used by a deployer to override them? This would require a major change to the EJB and JCA specs since a resource adapter cannot currently access the deployment descriptor. A slightly simpler alternative might be to require the EJB container to convert these deployment descriptor elements to activation properties and pass them to the resource adapter in the activation spec.
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I9:</b> What happens if the same attribute is specified using an activation property and using one of these new annotations? It is proposed that a value defined using an activation property always overrides the same property defined using one of these new annotations, since this would provide a way to override these new annotations in the deployment descriptor.
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I10:</b> Is an additional annotation required to allow non-standard properties to be passed to the resource adapter or container? Or are activation properties adequate for this purpose?
</td></tr></table>

### Specifying the queue or topic

The `@JMSListener` method annotation must always be supplied (except in the special case of the `onMessage` method of a `MessageListener`). It designates (1) the method as being a listener callback method and optionally (2) the destination from which messages are to be received and (3) whether the specified destination is a queue or topic. 

The `lookup` attribute may be used to specify the JNDI name of the queue or topic. This corresponds to the existing EJB 3.2 activation property `destinationLookup`.
<br/>
 @JMSListener(lookup="java:global/Trades", type=JMSListener.Type.Topic)
<br/>
The `@JMSListener` method annotation also has a mandatory attribute `type`. This must be used to specify whether the destination is a queue or topic.  This corresponds to the existing EJB 3.2 activation property `destinationType`, though the attribute is an enumerated type rather than a `String`.

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I11:</b> Deleted
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I12:</b> The EJB and Java EE specifications currently define a number of other ways of  defining the destination used by the MDB, such as by setting the `mappedName` attribute of the `@MessageDriven` annotation. The specification will need to clarify the override order used if the destination is specified in multiple ways.
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I13:</b> Is it right that the  `@JMSListener` attribute `"type"` is mandatory,? The existing EJB 3.2 activation property `destinationType` does not define a default value. Should it remain optional, in which case should the specification designate a default value when  `@JMSListener` is used?
</td></tr></table>

### Specifying the connection factory

The existing `@JMSConnectionFactory` annotation may be used to specify the JNDI name of the connection factory used to receive messages.This corresponds to the existing EJB 3.2 activation property `connectionFactoryLookup`. 
<br/>
 @JMSConnectionFactory("java:global/MyCF")
<br/>
Note that `@JMSConnectionFactory` is an existing annotation which is currently used to configure the connection factory used to create an injected `JMSContext` object. It better to reuse this annotation than have two very similar annotations.

### Specifying the acknowledgement mode when using bean-managed transactions

The existing `@Acknowledge` annotation may be used to specify the acknowledgement mode that will be used if bean-managed transaction demarcation is used.
This corresponds to the existing EJB 3.2 activation property `acknowledgeMode`. 
<br/>
 @Acknowledge(Acknowledge.Mode.DUPS_OK_ACKNOWLEDGE)
<br/>
The acknowledgement mode is specified using an enumerated type `Acknowledge.Mode`, which is a nested type of the `Acknowledge` annotation.

### Specifying durable topic subscriptions

If the MDB is being used to consume messages from a topic, three further annotations are available: `@SubscriptionDurability`, `@SubscriptionName` and `@ClientId`.  These correspond to the EJB 3.2 activation properties  `subscriptionDurability`, `subscriptionName` and `clientId`. 
<br/>
 @MessageDriven
 public class MyMessageBean implements JMSMessageDrivenBean {
 
   @Resource(mappedName = "java:global/replyQueue")
   private Queue replyQueue;
 
   @Inject
   private JMSContext jmsContext;
  
   @SubscriptionDurability(SubscriptionDurability.Mode.DURABLE)
   @SubscriptionName("mySubName1")
   @ClientId("myClientID1")  
   @JMSListener(lookup="java:global/inboundTopic", type=JMSListener.Type.TOPIC)
   public void giveMeAMessage(Message message) {
     ...
   }
 }
<br/>
The subscription durability is specified using an enumerated type `SubscriptionDurability.Mode`, which is a nested type of the `SubscriptionDurability` annotation.

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I14:</b> Should the `@SubscriptionDurability`, `@SubscriptionName` and `@ClientId` annotations (or perhaps the first two) be combined into a single annotation?
</td></tr></table>

### Specifying a message selector

The `@MessageSelector` annotation may be used to specify the message selector to be used. This corresponds directly to the EJB 3.2 activation property `messageSelector`, which may be used to override it.
<br/>
 @MessageDriven
 public class MyMessageBean implements JMSMessageDrivenBean {
 
   @MessageSelector("JMSType = 'car' AND colour = 'blue'")
   @JMSListener(lookup="java:global/requestQueue", type=JMSListener.Type.QUEUE)
   public void giveMeAMessage(Message message) {
     ...
   }

## Flexible method signature

When a message is delivered the container will set each method parameter to the message, the message body or to a message header or property, depending on the type of the message, the type of the parameter, and any `@MessageHeader` or `@MessageProperty` annotation.

### Message parameters

A parameter may be `Message` or one of its five subtypes `TextMessage`, `StreamMessage`, `BytesMessage`, `MapMessage`, `ObjectMessage`. This avoids the need for the listener method to cast the `Message` to the expected subtype.
<br/>
 void processTrade(TextMessage textMessage){
   ...
 }

### Parameters for message body

If the message is a `TextMessage` then any parameter of type `String` (and which is not annotated with `@MessageHeader` or `@MessageProperty`) will be set to contain the message body.
<br/>
 void processTrade(String messageText){
   ...
 }
<br/>
If the message is a `ObjectMessage` then any parameter to which the message body is assignable (and which is not annotated with `@MessageHeader` or `@MessageProperty`) will be set to contain the message body.

 void processTrade(Trade incomingTrade){
   ...
 }
<br/>
If the message is a `MapMessage` then any parameter of type `Map` (and which is not annotated with `@MessageHeader` or `@MessageProperty`) will be set to contain the message body.

 void processTrade(Map tradeData){
   ...
 }
<br/>
If the message is a `BytesMessage` then any parameter of type `byte[]` (and which is not annotated with `@MessageHeader` or `@MessageProperty`) will be set to contain the message body.

 void processTrade(byte[] tradeBytes){
   ...
 }

### Message headers

The `@MessageHeader` annotation may be used to specify that a parameter should be set to the specified message header.
<br/>
 void processTrade(TextMessage messageText, @MessageHeader(Header.JMSCorrelationID) String correlationId,){
   ...
 } 
<br/>
The message header is specified using an enumerated type `MessageHeader.Header`, which is a nested type of the `MessageHeader` annotation.

### Message properties

The `@MessageProperty` annotation may be used to specify that a parameter should be set to the specified message property.
<br/>
 void processTrade(TextMessage messageText, @MessageProperty("price") long price,){
   ...
 } 

### Summary of callback method parameters

The following table lists all the options available for customising the method parameters:

{|- border="1"
! Message type
! Parameter type
! Annotation
! Set to
|-
| `TextMessage`
| `TextMessage`
| None
| The `TextMessage` object 
|-
| `StreamMessage`
| `StreamMessage`
| None
| The `StreamMessage` object
|-
| `BytesMessage`
| `BytesMessage`
| None
| The `BytesMessage` object
|-
| `MapMessage`
| `MapMessage`
| None
| The `MapMessage` object
|-
| `ObjectMessage`
| `ObjectMessage`
| None
| The `ObjectMessage` object
|-
| `Message` 
| `Message`
| None
| The `Message` object
|-
| Any
| `someClass`
| None
| The message body, if it can be converted to the specified type using `message.getBody(someClass)` without throwing a `MessageFormatException`
|-
| Any
| `String`
| `@MessageHeader(Header.JMSCorrelationID)`
| `message.getJMSCorrelationID()`.
|-
| Any
| `byte[]`
| `@MessageHeader(Header.JMSCorrelationIDAsBytes)`
| `message.getJMSCorrelationIDAsBytes()`.
|-
| Any
| `Integer` or `int`
| `@MessageHeader(Header.JMSDeliveryMode)`
| `message.getJMSDeliveryMode()`.
|-
| Any
| `Long` or `long`
| `@MessageHeader(Header.JMSDeliveryTime)`
| `message.getJMSDeliveryTime()`.
|-
| Any
| `Destination`
| `@MessageHeader(Header.JMSDestination)`
| `message.getJMSDestination()`.
|-
| Any
| `Long` or `long`
| `@MessageHeader(Header.JMSExpiration)`
| `message.getJMSExpiration()`.
|-
| Any
| `String`
| `@MessageHeader(Header.JMSMessageID)`
| `message.getJMSMessageID()`.
|-
| Any
| `Integer` or `int`
| `@MessageHeader(Header.JMSPriority)`
| `message.getJMSPriority()`.
|-
| Any
| `Boolean` or `boolean`
| `@MessageHeader(Header.JMSRedelivered)`
| `message.getJMSRedelivered()`.
|-
| Any
| `Destination`
| `@MessageHeader(Header.JMSReplyTo)`
| `message.getJMSReplyTo()`.
|-
| Any
| `Long` or `long`
| `@MessageHeader(Header.JMSTimestamp)`
| `message.getJMSTimestamp()`.
|-
| Any
| `String`
| `@MessageHeader(Header.JMSType)`
| `message.getJMSType()`.
|-
| Any
| `Boolean` or `boolean`
| `@MessageProperty("foo")`
| `message.getBooleanProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
|-
| Any
| `byte`
| @MessageProperty("foo")
| `message.getByteProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
|-
| Any
| `Short` or `short`
| @MessageProperty("foo")
| `message.getShortProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
|-
| Any
| `Integer` or `int`
| @MessageProperty("foo")
| `message.getIntProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
|-
| Any
| `Long` or `long`
| @MessageProperty("foo")
| `message.getLongProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
|-
| Any
| `Float` or `float`
| @MessageProperty("foo")
| `message.getFloatProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
|-
| Any
| `Double` or `double`
| @MessageProperty("foo")
| `message.getDoubleProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
|-
| Any
| `String`
| @MessageProperty("foo")
| `message.getStringProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
|} 

### Incompatible method parameters

If the callback method parameter cannot be set, either because it does not match any of the cases in the preceding table, or because a `MessageFormatException` was encountered whilst trying to convert the required value to the specified type, then the callback method will not be invoked. The message may either be discarded or delivered to a provider-specific dead message queue. 

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I15:</b> Deleted
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I16:</b> Deleted
</td></tr></table>

## Summary and links to javadocs 

The draft javadocs can be found [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/index.html?javax/jms/package-summary.html here]. Direct links to the javadocs for each class are given in the table below.

{|- border="1"
! New or modified?
! Interface or annotation?
! Name
| Link to javadocs
|-
| New
| Marker interface
| `javax.jms.JMSMessageDrivenBean`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSMessageDrivenBean.html javadocs]
|-
| New
| Method annotation
| `javax.jms.JMSListener`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSListener.html javadocs]
|-
| Modified
| Method or field annotation
| `javax.jms.JMSConnectionFactory`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSConnectionFactory.html javadocs]
|-
| New
| Method annotation
| `javax.jms.Acknowledge`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/Acknowledge.html javadocs]
|-|-
| New
| Method annotation
| `javax.jms.SubscriptionDurability`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/SubscriptionDurability.html javadocs]
|-
| New
| Method annotation
| `javax.jms.SubscriptionName`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/SubscriptionName.html javadocs]
|-
| New
| Method annotation
| `javax.jms.ClientId`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/ClientId.html javadocs]
|-
| New
| Method annotation
| `javax.jms.MessageSelector`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageSelector.html javadocs]
|-
| New
| Parameter annotation
| `javax.jms.MessageHeader`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageHeader.html javadocs]
|-
| New
| Parameter annotation
| `javax.jms.MessageProperty`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageProperty.html javadocs]
|} 