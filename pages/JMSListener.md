# More flexible JMS MDBs (Version 1)

This page contains proposals to simplify the configuration of JMS MDBs in JMS 2.1 and Java EE 8.

This is version 1 of these proposals. These are out of date. Please look at [version 2](/jms-spec/pages/JMSListener2).

## Contents

* auto-gen TOC:
{:toc}

## Background 

There have been several proposals to improve the ways that JMS applications can consume messages asynchronously:

* In [issue 116](https://github.com/javaee/jms-spec/issues/116) John Ament proposed (back in March 2013, just as JMS 2.0 was being finalised) improving the ways that JMS MDBs were defined, taking advantage of some new features that were added to EJB 3.2 and JCA 1.7 (at the suggestion of David Blevins) just before they were released. 

* In [issue 134](https://github.com/javaee/jms-spec/issues/134) Reza Rahman proposed that JMS defined some annotations that allowed any CDI bean to listen for JMS messages

* In [issue 100](https://github.com/javaee/jms-spec/issues/100)  Bruno Borges proposed improving the ways that JMS MDBs were defined, though in the subsequent discussion he proposed that this could be extended to other types of Java EE class such as session beans. That makes this essentially a combination of the other two proposals.

## Goals 

The proposals on this page are addressed at the first of these proposals,  [issue 116](https://github.com/javaee/jms-spec/issues/116):

* The requirement for a MDB that consumes JMS messages to implement`javax.jms.MessageListener` will be removed.

* Instead of having to provide a callback method `void onMessage(Message m)`, the callback method may have any name and can have multiple parameters of a variety of types. The developer can use parameter annotations to specify that a parameter must be set to the message object, to the message body, or to a specified message header or message property.

* Instead of having to use the general-purpose activation property mechanism to define what messages the MDB will receive, the developer can specify a set of JMS-specific annotations. This is more obvious, less verbose and allows the compiler to detect spelling errors. 

These new annotations will initially be available only on MDBs. This offers a large scope for improvement without the need to consider issues such as listener lifecycle, listener pooling and resource adapter integration. A later stage in the development of JMS 2.1 will consider extending them to other types of Java EE object such as CDI managed beans.

## Comments 

These are currently just proposals, and comments are invited, especially to the various issues mentioned.

See [How to get involved in JMS 2.1](/jms-spec/pages/JMS21#how-to-get-involved-in-jms-21).

## Specifying the callback method 

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
public class MyMessageBean implements JMSMessageDrivenBean {
 
  @JMSListener(lookup="java:global/Trades", type=JMSListener.Type.QUEUE)
  public void processTrade(TextMessage tradeMessage){
    ...
  }
 
}
```

Notes:

* The MDB must implement the interface `javax.jms.JMSMessageDrivenBean`. This is purely a marker interface and defines no methods. It is required to satisfy the requirements of EJB 3.2 section 5.6.5 "Message-Driven Bean with No-Methods Listener Interface".   (Note that it is hoped that the EJB spec can be updated so that this marker interface will not actually be needed.) 

* The names and package of the `JMSMessageDrivenBean` marker interface and the `@JMSListener` annotation which defines the callback method are provisional, and alternative suggestions are invited.

* The details of all these annotations are discussed in the next section.

* The listener class is still a message-driven bean and so must be configured as one, either by adding the `@MessageDriven` annotation or using the `<message-driven>` element in the deployment descriptor.

* A JMS MDB may have only one callback method.  

* These annotations cannot be applied to the `onMessage` method of a `MessageListener`. They will be ignored.

* Both the callback method and the `JMSMessageDrivenBean` interface may be inherited.

* Application servers that implement JMS MDBs using a resource adapter should be able to implement these new features by enhancing the resource adapter itself. Few changes to the EJB container itself will be required. Application servers that implement JMS MDBs directly, without using a resource adapter, will of course need to implement these features in the EJB container itself.

* Since MDBs are defined in the EJB specification, not the JMS specification, it will almost certainly be necessary to work with the EJB maintenance lead to create a revision of the EJB spec containing these new features.

* In the JMS specification itself, chapter 13 "Resource adapter" (which contains optional recommendations for a JMS resource adapter) will be extended to cover these new features.

<b>Issue I1:</b> Any alternative proposals for `JMSMessageDrivenBean`?

<b>Issue I2:</b> Any alternative proposals for `JMSListener`?

<b>Issue I3:</b> The EJB specification does not define a standard way to associate a MDB with a resource adapter. JMS MDBs that require the use of a resource adapter will continue to need to specify the resource adapter in a non-portable way, either using the app-server-specific deployment descriptor (e.g. `glassfish-ejb-jar.xml`) or by using a default resource adapter provided by the application server.

<b>Issue I4:</b> The current proposal is that a JMS MDB has a single listener method. However EJB 3.2 section 5.4.2 and JCA 1.7 does allow a MDB to have more than one listener method, with the resource adapter deciding which method is invoked. Is this something we would want to allow? Would these be alternative callback methods for the same consumer, with the container choosing which one to call depending on the message and the method signature, or would these represent two completely different consumers, on different destinations?

<b>Issue I5:</b> It would be desirable to avoid the need to implement  `javax.jms.JMSMessageDrivenBean` since this is needed purely to satisfy EJB 3.2.  [EJB spec issue 115](https://github.com/javaee/ejb-spec/issues/115) proposes removal of this requirement from the next version of EJB.  

<b>Issue I6:</b> The reason why these annotations cannot be applied to the `onMessage` method of a `MessageListener` is that `MessageListener` is not a no-method interface, which means the resource adapter cannot access the methods of the MDB implementation class. It may be possible to change the EJB specification to allow this restriction to be removed.

## Specifying what messages will be received 

Before it can be used, a JMS MDB must specify where the messages will come from and how they will be received.  In Java EE 7 these are specified using "activation properties", each of which has a  String name and a String value. The name and value of each property must be hardcoded into the either application code or the deployment descriptor, and the developer gets no help from the compiler or schema to check that they are using the correct name and setting it to an appropriate value. The syntax itself is also cumbersome.

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
    propertyName = "messageSelector", propertyValue = "ticker=ORCL")
}
public class MyMessageBean implements MessageListener {
  ...
}
```

In Java EE 8 a MDB may continue to specify activation properties, even if the MDB does not implement `javax.jms.MessageListener`. However they will be superseded by a new set of JMS-specific annotations which allow each activation property to be configured by a JMS-specific annotation on the callback method itself.
```
@JMSConnectionFactory("java:global/MyCF")
@JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
@SubscriptionDurability(SubscriptionDurability.Mode.DURABLE)
@ClientId("myClientID1")  
@SubscriptionName("mySubName1")
@MessageSelector("ticker=ORCL'")
@Acknowledge(Acknowledge.Mode.DUPS_OK_ACKNOWLEDGE)
public void giveMeAMessage(Message message) {
  ...
}
```
These annotations are introduced in more detail the following sections.

Note:

* The details of all these annotations are discussed in the next section.

* The names and package of these annotations are provisional, and alternative suggestions are invited.

<b>Issue I7:</b> Any alternative proposals for the `@JMSConnectionFactory`, `@Acknowledge`, `@SubscriptionDurability`, `@ClientId`, `@SubscriptionName` or `@MessageSelector` annotations?

<b>Issue I8:</b> Should JMS define a set of deployment descriptor which correspond to these annotations, and which can be used by a deployer to override them? This would require a major change to the EJB and JCA specs since a resource adapter cannot currently access the deployment descriptor. A slightly simpler alternative might be to require the EJB container to convert these deployment descriptor elements to activation properties and pass them to the resource adapter in the activation spec.

<b>Issue I9:</b> What happens if the same attribute is specified using an activation property and using one of these new annotations? It is proposed that a value defined using an activation property always overrides the same property defined using one of these new annotations, since this would provide a way to override these new annotations in the deployment descriptor.

<b>Issue I10:</b> Is an additional annotation required to allow non-standard properties to be passed to the resource adapter or container? Or are activation properties adequate for this purpose?

### Specifying the queue or topic

The `@JMSListener` method annotation must always be supplied. It designates (1) the method as being a listener callback method (2) the destination from which messages are to be received and (3) whether the specified destination is a queue or topic. 

The destination can be specified in two ways:

1. The `lookup` attribute can be used to specify the JNDI name of the queue or topic. This corresponds to the existing EJB 3.2 activation property `destinationLookup`.
```
@JMSListener(lookup="java:global/Trades", type=JMSListener.Type.Topic)`
```

2. Alternatively the `name` attribute can be used to specify the "provider-specific" name of the queue or topic. The container would use the JMS methods `Session#createQueue` or `Session#createTopic`. This is a new feature which is not defined in EJB 3.2 and since these methods are provider-specific the specification would need to advise against using it in portable applications.
```
@JMSListener(name="tradeTopic", type=JMSListener.Type.Topic)`
```

The `@JMSListener` method annotation also has a mandatory attribute `type`. This must be used to specify whether the destination is a queue or topic.  This corresponds to the existing EJB 3.2 activation property `destinationType`, though the attribute is an enumerated type rather than a `String`.

<b>Issue I11:</b> Allowing the queue or topic to be specified by destination name rather than by JNDI name is a new feature. Since it is not portable, is this actually desirable?

<b>Issue I12:</b> The EJB and Java EE specifications currently define a number of other ways of  defining the destination used by the MDB, such as by setting the `mappedName` attribute of the `@MessageDriven` annotation. The specification will need to clarify the override order used if the destination is specified in multiple ways.

<b>Issue I13:</b> Is it right that the  `@JMSListener` attribute `type` is mandatory,? The existing EJB 3.2 activation property `destinationType` does not define a default value. Should it remain optional, in which case should the specification designate a default value when  `@JMSListener` is used?

### Specifying the connection factory

The existing `@JMSConnectionFactory` annotation may be used to specify the JNDI name of the connection factory used to receive messages.This corresponds to the existing EJB 3.2 activation property `connectionFactoryLookup`. 
```
@JMSConnectionFactory("java:global/MyCF")`
```
Note that `@JMSConnectionFactory` is an existing annotation which is currently used to configure the connection factory used to create an injected `JMSContext` object. It better to reuse this annotation than have two very similar annotations.

### Specifying the acknowledgement mode when using bean-managed transactions

The existing `@Acknowledge` annotation may be used to specify acknowledge mode that will be used if bean-managed transaction demarcation is used.
This corresponds to the existing EJB 3.2 activation property `acknowledgeMode`. 
```
@Acknowledge(Acknowledge.Mode.DUPS_OK_ACKNOWLEDGE)`
```
The acknowledgement mode is specified using an enumerated type `Acknowledge.Mode`, which is a nested type of the `Acknowledge` annotation.

### Specifying durable topic subscriptions

If the MDB is being used to consume messages from a topic, three further annotations are available: `@SubscriptionDurability`, `@SubscriptionName` and `@ClientId`.  These correspond to the EJB 3.2 activation properties  `subscriptionDurability`, `subscriptionName` and `clientId`. 
```
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
```
The subscription durability is specified using an enumerated type `SubscriptionDurability.Mode`, which is a nested type of the `SubscriptionDurability` annotation.

<b>Issue I14:</b> Should the `@SubscriptionDurability`, `@SubscriptionName` and `@ClientId` annotations (or perhaps the first two) be combined into a single annotation?

### Specifying a message selector

The `@MessageSelector` annotation may be used to specify the message selector to be used. This corresponds directly to the EJB 3.2 activation property `messageSelector`, which may be used to override it.
```
@MessageDriven
public class MyMessageBean implements JMSMessageDrivenBean {
 
  @MessageSelector("JMSType = 'car' AND colour = 'blue'")
  @JMSListener(lookup="java:global/requestQueue", type=JMSListener.Type.QUEUE)
  public void giveMeAMessage(Message message) {
    ...
  }
```

## Flexible method signature

The callback method may have any name, it may have any return type, and may have any number of parameters of any type.

When a message is delivered the container will set each method parameter to the message, the message body or to a message header or property, depending on the type of the message, the type of the parameter, and any `@MessageHeader` or `@MessageProperty` annotation.

### Message parameters

A parameter may be `Message` or one of its five subtypes `TextMessage`, `StreamMessage`, `BytesMessage`, `MapMessage`, `ObjectMessage`. This avoids the need for the listener method to cast the `Message` to the expected subtype.
```
void processTrade(TextMessage textMessage){
  ...
}
```

### Parameters for message body

If the message is a `TextMessage` then any parameter of type `String` (and which is not annotated with `@MessageHeader` or `@MessageProperty`) will be set to contain the message body.
```
void processTrade(String messageText){
  ...
}
```
If the message is a `ObjectMessage` then any parameter to which the message body is assignable (and which is not annotated with `@MessageHeader` or `@MessageProperty`) will be set to contain the message body.
```
void processTrade(Trade incomingTrade){
  ...
}
```
If the message is a `MapMessage` then any parameter of type `Map` (and which is not annotated with `@MessageHeader` or `@MessageProperty`) will be set to contain the message body.
```
void processTrade(Map tradeData){
  ...
}
```
If the message is a `BytesMessage` then any parameter of type `byte[]` (and which is not annotated with `@MessageHeader` or `@MessageProperty`) will be set to contain the message body.
```
void processTrade(byte[] tradeBytes){
  ...
}
```

### Message headers

The `@MessageHeader` annotation may be used to specify that a parameter should be set to the specified message header.
```
void processTrade(TextMessage messageText, @MessageHeader(Header.JMSCorrelationID) String correlationId,){
  ...
} 
```
The message header is specified using an enumerated type `MessageHeader.Header`, which is a nested type of the `MessageHeader` annotation.

### Message properties

The `@MessageProperty` annotation may be used to specify that a parameter should be set to the specified message property.
```
void processTrade(TextMessage messageText, @MessageProperty("price") long price,){
  ...
} 
```

### Summary

The following table lists all the options available for customising the method parameters:

Message type | Parameter type | Annotation | Set to
:--- | :--- | :--- | :---
`TextMessage` | `TextMessage` | None | The `TextMessage` object 
`StreamMessage` | `StreamMessage` | None | The `StreamMessage` object
`BytesMessage` | `BytesMessage` | None | The `BytesMessage` object
`MapMessage` | `MapMessage` | None | The `MapMessage` object
`ObjectMessage` | `ObjectMessage` | None | The `ObjectMessage` object
`Message`  | `Message` | None  The `Message` object
Any | `someClass` | None | The message body, if it can be converted to the specified type using `message.getBody(someClass)` without throwing a `MessageFormatException`
Any | `String` | `@MessageHeader(Header.JMSCorrelationID)` | `message.getJMSCorrelationID()`.
Any | `byte[]` | `@MessageHeader(Header.JMSCorrelationIDAsBytes)` | `message.getJMSCorrelationIDAsBytes()`.
Any | `Integer` or `int` | `@MessageHeader(Header.JMSDeliveryMode)` | `message.getJMSDeliveryMode()`.
Any | `Long` or `long` | `@MessageHeader(Header.JMSDeliveryTime)` | `message.getJMSDeliveryTime()`.
Any | `Destination` | `@MessageHeader(Header.JMSDestination)` | `message.getJMSDestination()`.
Any | `Long` or `long` | `@MessageHeader(Header.JMSExpiration)` | `message.getJMSExpiration()`.
Any | `String` | `@MessageHeader(Header.JMSMessageID)` | `message.getJMSMessageID()`.
Any | `Integer` or `int` | `@MessageHeader(Header.JMSPriority)` | `message.getJMSPriority()`.
Any | `Boolean` or `boolean` | `@MessageHeader(Header.JMSRedelivered)` | `message.getJMSRedelivered()`.
Any | `Destination` | `@MessageHeader(Header.JMSReplyTo)` | `message.getJMSReplyTo()`.
Any | `Long` or `long` | `@MessageHeader(Header.JMSTimestamp)` | `message.getJMSTimestamp()`.
Any | `String` | `@MessageHeader(Header.JMSType)` | `message.getJMSType()`.
Any | `Boolean` or `boolean` | `@MessageProperty("foo")` | `message.getBooleanProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
Any | `byte` | @MessageProperty("foo") | `message.getByteProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
Any | `Short` or `short` | @MessageProperty("foo") | `message.getShortProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
Any | `Integer` or `int` | @MessageProperty("foo") | `message.getIntProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
Any | `Long` or `long` | @MessageProperty("foo") | `message.getLongProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
Any | `Float` or `float` | @MessageProperty("foo") | `message.getFloatProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
Any | `Double` or `double` | @MessageProperty("foo") | `message.getDoubleProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`
Any | `String` | @MessageProperty("foo") | `message.getStringProperty("foo")`<br/>if this returns without throwing a `MessageFormatException`

* If none of the following applies then the parameter is set to the default value [as defined in the JLS](https://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5) (e.g. `null` for objects, 0 for int etc)

* It is recommended that the callback method has a `void` return type. A non-`void` return type is allowed but the returned value will be ignored.

<b>Issue I15:</b> Any alternative proposals for the `@MessageProperty` or `MessageHeader` annotations?

<b>Issue I16:</b> The table above is based on the principle that if a parameter cannot be set to the required value due to it having an unsuitable type then it should simple be set to null (or a default primitive value) rather than throwing an exception and triggering message delivery. This is because there is no point in redelivering the message since it will simply fail again (and JMS does not define any dead message functionality). Is this the correct approach?

## Summary and links to javadocs 

The draft javadocs can be found [here](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/index.html?javax/jms/package-summary.html). Direct links to the javadocs for each class are given in the table below.

New or modified? | ! Interface or annotation? | Name | Link to javadocs
:--- | :--- | :--- | :---
New | Marker interface | `javax.jms.JMSMessageDrivenBean` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSMessageDrivenBean.html)
New | Method annotation | `javax.jms.JMSListener` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSListener.html)
Modified | Method or field annotation | `javax.jms.JMSConnectionFactory` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSConnectionFactory.html)
New | Method annotation | `javax.jms.Acknowledge` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/Acknowledge.html)
New | Method annotation | `javax.jms.SubscriptionDurability` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/SubscriptionDurability.html)
New | Method annotation | `javax.jms.SubscriptionName` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/SubscriptionName.html)
New | Method annotation | `javax.jms.ClientId` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/ClientId.html)
New | Method annotation | `javax.jms.MessageSelector` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageSelector.html)
New | Parameter annotation | `javax.jms.MessageHeader` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageHeader.html)
New | Parameter annotation | `javax.jms.MessageProperty` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageProperty.html)
