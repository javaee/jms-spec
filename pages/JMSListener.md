<h1>More flexible JMS MDBs (Version 1)</h1>

This page contains proposals to simplify the configuration of JMS MDBs in JMS 2.1 and Java EE 8.

This is version 1 of these proposals. These are out of date. Please look at [[JMSListener2 | version 2]].

__TOC__

==Background==

There have been several proposals to improve the ways that JMS applications can consume messages asynchronously:

* In [https://java.net/jira/browse/JMS_SPEC-116 JMS_SPEC-116] John Ament proposed (back in March 2013, just as JMS 2.0 was being finalised) improving the ways that JMS MDBs were defined, taking advantage of some new features that were added to EJB 3.2 and JCA 1.7 (at the suggestion of David Blevins) just before they were released. 

* In [https://java.net/jira/browse/JMS_SPEC-134 JMS_SPEC-134] Reza Rahman proposed that JMS defined some annotations that allowed any CDI bean to listen for JMS messages

* In [https://java.net/jira/browse/JMS_SPEC-100 JMS_SPEC-100]  Bruno Borges proposed improving the ways that JMS MDBs were defined, though in the subsequent discussion he proposed that this could be extended to other types of Java EE class such as session beans. That makes this essentially a combination of the other two proposals.

==Goals==

The proposals on this page are addressed at the first of these proposals,  [https://java.net/jira/browse/JMS_SPEC-116 JMS_SPEC-116]:

* The requirement for a MDB that consumes JMS messages to implement<tt>javax.jms.MessageListener</tt> will be removed.

* Instead of having to provide a callback method <tt>void onMessage(Message m)</tt>, the callback method may have any name and can have multiple parameters of a variety of types. The developer can use parameter annotations to specify that a parameter must be set to the message object, to the message body, or to a specified message header or message property.

* Instead of having to use the general-purpose activation property mechanism to define what messages the MDB will receive, the developer can specify a set of JMS-specific annotations. This is more obvious, less verbose and allows the compiler to detect spelling errors. 

These new annotations will initially be available only on MDBs. This offers a large scope for improvement without the need to consider issues such as listener lifecycle, listener pooling and resource adapter integration. A later stage in the development of JMS 2.1 will consider extending them to other types of Java EE object such as CDI managed beans.

==Comments==

These are currently just proposals, and comments are invited, especially to the various issues mentioned.

See [https://java.net/projects/jms-spec/pages/JMS21#How_to_get_involved_in_JMS_2.1 How to get involved in JMS 2.1].

==Specifying the callback method==
<br/>
In Java EE 7, a JMS MDB must implement the <tt>javax.jms.MessageListener</tt> interface. This means that the callback method must be called <tt>onMessage</tt>, it must return <tt>void</tt> and it must have a single parameter of type <tt>Message</tt>.<br/><br/>

 @MessageDriven(activationConfig = {
   @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/requestQueue"),
   @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
 })
 public class MyMessageBean implements MessageListener {
 
   public void onMessage(Message message){
     ...
   }
 
 }
<br/>
Although this option will remain, it is proposed in Java EE 8 to remove the requirement for a JMS MDB to implement the <tt>javax.jms.MessageListener</tt> interface. Instead the developer can use the <tt>@JMSListener</tt> annotation to designate any method to be the callback method.
<br/><br/>
 @MessageDriven
 public class MyMessageBean <b>implements JMSMessageDrivenBean</b> {
 
   <b>@JMSListener</b>(lookup="java:global/Trades", type=JMSListener.Type.QUEUE)
   public void processTrade(TextMessage tradeMessage){
     ...
   }
 
 }
<br/>

Notes:

* The MDB must implement the interface <tt>javax.jms.JMSMessageDrivenBean</tt>. This is purely a marker interface and defines no methods. It is required to satisfy the requirements of EJB 3.2 section 5.6.5 "Message-Driven Bean with No-Methods Listener Interface".   (Note that it is hoped that the EJB spec can be updated so that this marker interface will not actually be needed.) 

* The names and package of the <tt>JMSMessageDrivenBean</tt> marker interface and the <tt>@JMSListener</tt> annotation which defines the callback method are provisional, and alternative suggestions are invited.

* The details of all these annotations are discussed in the next section.

* The listener class is still a message-driven bean and so must be configured as one, either by adding the <tt>@MessageDriven</tt> annotation or using the <tt>&lt;message-driven&gt;</tt> element in the deployment descriptor.

* A JMS MDB may have only one callback method.  

* These annotations cannot be applied to the <tt>onMessage</tt> method of a <tt>MessageListener</tt>. They will be ignored.

* Both the callback method and the <tt>JMSMessageDrivenBean</tt> interface may be inherited.

* Application servers that implement JMS MDBs using a resource adapter should be able to implement these new features by enhancing the resource adapter itself. Few changes to the EJB container itself will be required. Application servers that implement JMS MDBs directly, without using a resource adapter, will of course need to implement these features in the EJB container itself.

* Since MDBs are defined in the EJB specification, not the JMS specification, it will almost certainly be necessary to work with the EJB maintenance lead to create a revision of the EJB spec containing these new features.

* In the JMS specification itself, chapter 13 "Resource adapter" (which contains optional recommendations for a JMS resource adapter) will be extended to cover these new features.

<b>Issue I1:</b> Any alternative proposals for <tt>JMSMessageDrivenBean</tt>?

<b>Issue I2:</b> Any alternative proposals for <tt>JMSListener</tt>?

<b>Issue I3:</b> The EJB specification does not define a standard way to associate a MDB with a resource adapter. JMS MDBs that require the use of a resource adapter will continue to need to specify the resource adapter in a non-portable way, either using the app-server-specific deployment descriptor (e.g. <tt>glassfish-ejb-jar.xml</tt>) or by using a default resource adapter provided by the application server.

<b>Issue I4:</b> The current proposal is that a JMS MDB has a single listener method. However EJB 3.2 section 5.4.2 and JCA 1.7 does allow a MDB to have more than one listener method, with the resource adapter deciding which method is invoked. Is this something we would want to allow? Would these be alternative callback methods for the same consumer, with the container choosing which one to call depending on the message and the method signature, or would these represent two completely different consumers, on different destinations?

<b>Issue I5:</b> It would be desirable to avoid the need to implement  <tt>javax.jms.JMSMessageDrivenBean</tt> since this is needed purely to satisfy EJB 3.2.  [https://java.net/jira/browse/EJB_SPEC-115 EJB_SPEC-115] proposes removal of this requirement from the next version of EJB.  

<b>Issue I6:</b> The reason why these annotations cannot be applied to the <tt>onMessage</tt> method of a <tt>MessageListener</tt> is that <tt>MessageListener</tt> is not a no-method interface, which means the resource adapter cannot access the methods of the MDB implementation class. It may be possible to change the EJB specification to allow this restriction to be removed.

==Specifying what messages will be received==
<br/>
Before it can be used, a JMS MDB must specify where the messages will come from and how they will be received.  In Java EE 7 these are specified using "activation properties", each of which has a  String name and a String value. The name and value of each property must be hardcoded into the either application code or the deployment descriptor, and the developer gets no help from the compiler or schema to check that they are using the correct name and setting it to an appropriate value. The syntax itself is also cumbersome.
<br/><br/>
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
<br/>
In Java EE 8 a MDB may continue to specify activation properties, even if the MDB does not implement <tt>javax.jms.MessageListener</tt>. However they will be superseded by a new set of JMS-specific annotations which allow each activation property to be configured by a JMS-specific annotation on the callback method itself.
<br/><br/>
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
<br/>
These annotations are introduced in more detail the following sections.

Note:

* The details of all these annotations are discussed in the next section.

* The names and package of these annotations are provisional, and alternative suggestions are invited.

<b>Issue I7:</b> Any alternative proposals for the <tt>@JMSConnectionFactory</tt>, <tt>@Acknowledge</tt>, <tt>@SubscriptionDurability</tt>, <tt>@ClientId</tt>, <tt>@SubscriptionName</tt> or <tt>@MessageSelector</tt> annotations?

<b>Issue I8:</b> Should JMS define a set of deployment descriptor which correspond to these annotations, and which can be used by a deployer to override them? This would require a major change to the EJB and JCA specs since a resource adapter cannot currently access the deployment descriptor. A slightly simpler alternative might be to require the EJB container to convert these deployment descriptor elements to activation properties and pass them to the resource adapter in the activation spec.

<b>Issue I9:</b> What happens if the same attribute is specified using an activation property and using one of these new annotations? It is proposed that a value defined using an activation property always overrides the same property defined using one of these new annotations, since this would provide a way to override these new annotations in the deployment descriptor.

<b>Issue I10:</b> Is an additional annotation required to allow non-standard properties to be passed to the resource adapter or container? Or are activation properties adequate for this purpose?

===Specifying the queue or topic===

The <tt>@JMSListener</tt> method annotation must always be supplied. It designates (1) the method as being a listener callback method (2) the destination from which messages are to be received and (3) whether the specified destination is a queue or topic. 

The destination can be specified in two ways:

1. The <tt>lookup</tt> attribute can be used to specify the JNDI name of the queue or topic. This corresponds to the existing EJB 3.2 activation property <tt>destinationLookup</tt>.
<br/>
 @JMSListener(lookup="java:global/Trades", type=JMSListener.Type.Topic)
<br/>
2. Alternatively the <tt>name</tt> attribute can be used to specify the "provider-specific" name of the queue or topic. The container would use the JMS methods <tt>Session#createQueue</tt> or <tt>Session#createTopic</tt>. This is a new feature which is not defined in EJB 3.2 and since these methods are provider-specific the specification would need to advise against using it in portable applications.
<br/>
 @JMSListener(name="tradeTopic", type=JMSListener.Type.Topic)
<br/>
The <tt>@JMSListener</tt> method annotation also has a mandatory attribute <tt>type</tt>. This must be used to specify whether the destination is a queue or topic.  This corresponds to the existing EJB 3.2 activation property <tt>destinationType</tt>, though the attribute is an enumerated type rather than a <tt>String</tt>.

<b>Issue I11:</b> Allowing the queue or topic to be specified by destination name rather than by JNDI name is a new feature. Since it is not portable, is this actually desirable?

<b>Issue I12:</b> The EJB and Java EE specifications currently define a number of other ways of  defining the destination used by the MDB, such as by setting the <tt>mappedName</tt> attribute of the <tt>@MessageDriven</tt> annotation. The specification will need to clarify the override order used if the destination is specified in multiple ways.

<b>Issue I13:</b> Is it right that the  <tt>@JMSListener</tt> attribute <tt>type</tt> is mandatory,? The existing EJB 3.2 activation property <tt>destinationType</tt> does not define a default value. Should it remain optional, in which case should the specification designate a default value when  <tt>@JMSListener</tt> is used?

===Specifying the connection factory===

The existing <tt>@JMSConnectionFactory</tt> annotation may be used to specify the JNDI name of the connection factory used to receive messages.This corresponds to the existing EJB 3.2 activation property <tt>connectionFactoryLookup</tt>. 
<br/>
 @JMSConnectionFactory("java:global/MyCF")
<br/>
Note that <tt>@JMSConnectionFactory</tt> is an existing annotation which is currently used to configure the connection factory used to create an injected <tt>JMSContext</tt> object. It better to reuse this annotation than have two very similar annotations.

===Specifying the acknowledgement mode when using bean-managed transactions===

The existing <tt>@Acknowledge</tt> annotation may be used to specify acknowledge mode that will be used if bean-managed transaction demarcation is used.
This corresponds to the existing EJB 3.2 activation property <tt>acknowledgeMode</tt>. 
<br/>
 @Acknowledge(Acknowledge.Mode.DUPS_OK_ACKNOWLEDGE)
<br/>
The acknowledgement mode is specified using an enumerated type <tt>Acknowledge.Mode</tt>, which is a nested type of the <tt>Acknowledge</tt> annotation.

===Specifying durable topic subscriptions===

If the MDB is being used to consume messages from a topic, three further annotations are available: <tt>@SubscriptionDurability</tt>, <tt>@SubscriptionName</tt> and <tt>@ClientId</tt>.  These correspond to the EJB 3.2 activation properties  <tt>subscriptionDurability</tt>, <tt>subscriptionName</tt> and <tt>clientId</tt>. 
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
The subscription durability is specified using an enumerated type <tt>SubscriptionDurability.Mode</tt>, which is a nested type of the <tt>SubscriptionDurability</tt> annotation.

<b>Issue I14:</b> Should the <tt>@SubscriptionDurability</tt>, <tt>@SubscriptionName</tt> and <tt>@ClientId</tt> annotations (or perhaps the first two) be combined into a single annotation?

===Specifying a message selector===

The <tt>@MessageSelector</tt> annotation may be used to specify the message selector to be used. This corresponds directly to the EJB 3.2 activation property <tt>messageSelector</tt>, which may be used to override it.
<br/>
 @MessageDriven
 public class MyMessageBean implements JMSMessageDrivenBean {
 
   @MessageSelector("JMSType = 'car' AND colour = 'blue'")
   @JMSListener(lookup="java:global/requestQueue", type=JMSListener.Type.QUEUE)
   public void giveMeAMessage(Message message) {
     ...
   }

==Flexible method signature ==

The callback method may have any name, it may have any return type, and may have any number of parameters of any type.

When a message is delivered the container will set each method parameter to the message, the message body or to a message header or property, depending on the type of the message, the type of the parameter, and any <tt>@MessageHeader</tt> or <tt>@MessageProperty</tt> annotation.

===Message parameters===

A parameter may be <tt>Message</tt> or one of its five subtypes <tt>TextMessage</tt>, <tt>StreamMessage</tt>, <tt>BytesMessage</tt>, <tt>MapMessage</tt>, <tt>ObjectMessage</tt>. This avoids the need for the listener method to cast the <tt>Message</tt> to the expected subtype.
<br/>
 void processTrade(TextMessage textMessage){
   ...
 }

===Parameters for message body===

If the message is a <tt>TextMessage</tt> then any parameter of type <tt>String</tt> (and which is not annotated with <tt>@MessageHeader</tt> or <tt>@MessageProperty</tt>) will be set to contain the message body.
<br/>
 void processTrade(String messageText){
   ...
 }
<br/>
If the message is a <tt>ObjectMessage</tt> then any parameter to which the message body is assignable (and which is not annotated with <tt>@MessageHeader</tt> or <tt>@MessageProperty</tt>) will be set to contain the message body.

 void processTrade(Trade incomingTrade){
   ...
 }
<br/>
If the message is a <tt>MapMessage</tt> then any parameter of type <tt>Map</tt> (and which is not annotated with <tt>@MessageHeader</tt> or <tt>@MessageProperty</tt>) will be set to contain the message body.

 void processTrade(Map tradeData){
   ...
 }
<br/>
If the message is a <tt>BytesMessage</tt> then any parameter of type <tt>byte[]</tt> (and which is not annotated with <tt>@MessageHeader</tt> or <tt>@MessageProperty</tt>) will be set to contain the message body.

 void processTrade(byte[] tradeBytes){
   ...
 }

===Message headers===

The <tt>@MessageHeader</tt> annotation may be used to specify that a parameter should be set to the specified message header.
<br/>
 void processTrade(TextMessage messageText, @MessageHeader(Header.JMSCorrelationID) String correlationId,){
   ...
 } 
<br/>
The message header is specified using an enumerated type <tt>MessageHeader.Header</tt>, which is a nested type of the <tt>MessageHeader</tt> annotation.

===Message properties===

The <tt>@MessageProperty</tt> annotation may be used to specify that a parameter should be set to the specified message property.
<br/>
 void processTrade(TextMessage messageText, @MessageProperty("price") long price,){
   ...
 } 

===Summary===

The following table lists all the options available for customising the method parameters:

{|- border="1"
! Message type
! Parameter type
! Annotation
! Set to
|-
| <tt>TextMessage</tt>
| <tt>TextMessage</tt>
| None
| The <tt>TextMessage</tt> object 
|-
| <tt>StreamMessage</tt>
| <tt>StreamMessage</tt>
| None
| The <tt>StreamMessage</tt> object
|-
| <tt>BytesMessage</tt>
| <tt>BytesMessage</tt>
| None
| The <tt>BytesMessage</tt> object
|-
| <tt>MapMessage</tt>
| <tt>MapMessage</tt>
| None
| The <tt>MapMessage</tt> object
|-
| <tt>ObjectMessage</tt>
| <tt>ObjectMessage</tt>
| None
| The <tt>ObjectMessage</tt> object
|-
| <tt>Message</tt> 
| <tt>Message</tt>
| None
| The <tt>Message</tt> object
|-
| Any
| <tt>someClass</tt>
| None
| The message body, if it can be converted to the specified type using <tt>message.getBody(someClass)</tt> without throwing a <tt>MessageFormatException</tt>
|-
| Any
| <tt>String</tt>
| <tt>@MessageHeader(Header.JMSCorrelationID)</tt>
| <tt>message.getJMSCorrelationID()</tt>.
|-
| Any
| <tt>byte[]</tt>
| <tt>@MessageHeader(Header.JMSCorrelationIDAsBytes)</tt>
| <tt>message.getJMSCorrelationIDAsBytes()</tt>.
|-
| Any
| <tt>Integer</tt> or <tt>int</tt>
| <tt>@MessageHeader(Header.JMSDeliveryMode)</tt>
| <tt>message.getJMSDeliveryMode()</tt>.
|-
| Any
| <tt>Long</tt> or <tt>long</tt>
| <tt>@MessageHeader(Header.JMSDeliveryTime)</tt>
| <tt>message.getJMSDeliveryTime()</tt>.
|-
| Any
| <tt>Destination</tt>
| <tt>@MessageHeader(Header.JMSDestination)</tt>
| <tt>message.getJMSDestination()</tt>.
|-
| Any
| <tt>Long</tt> or <tt>long</tt>
| <tt>@MessageHeader(Header.JMSExpiration)</tt>
| <tt>message.getJMSExpiration()</tt>.
|-
| Any
| <tt>String</tt>
| <tt>@MessageHeader(Header.JMSMessageID)</tt>
| <tt>message.getJMSMessageID()</tt>.
|-
| Any
| <tt>Integer</tt> or <tt>int</tt>
| <tt>@MessageHeader(Header.JMSPriority)</tt>
| <tt>message.getJMSPriority()</tt>.
|-
| Any
| <tt>Boolean</tt> or <tt>boolean</tt>
| <tt>@MessageHeader(Header.JMSRedelivered)</tt>
| <tt>message.getJMSRedelivered()</tt>.
|-
| Any
| <tt>Destination</tt>
| <tt>@MessageHeader(Header.JMSReplyTo)</tt>
| <tt>message.getJMSReplyTo()</tt>.
|-
| Any
| <tt>Long</tt> or <tt>long</tt>
| <tt>@MessageHeader(Header.JMSTimestamp)</tt>
| <tt>message.getJMSTimestamp()</tt>.
|-
| Any
| <tt>String</tt>
| <tt>@MessageHeader(Header.JMSType)</tt>
| <tt>message.getJMSType()</tt>.
|-
| Any
| <tt>Boolean</tt> or <tt>boolean</tt>
| <tt>@MessageProperty("foo")</tt>
| <tt>message.getBooleanProperty("foo")</tt><br/>if this returns without throwing a <tt>MessageFormatException</tt>
|-
| Any
| <tt>byte</tt>
| @MessageProperty("foo")
| <tt>message.getByteProperty("foo")</tt><br/>if this returns without throwing a <tt>MessageFormatException</tt>
|-
| Any
| <tt>Short</tt> or <tt>short</tt>
| @MessageProperty("foo")
| <tt>message.getShortProperty("foo")</tt><br/>if this returns without throwing a <tt>MessageFormatException</tt>
|-
| Any
| <tt>Integer</tt> or <tt>int</tt>
| @MessageProperty("foo")
| <tt>message.getIntProperty("foo")</tt><br/>if this returns without throwing a <tt>MessageFormatException</tt>
|-
| Any
| <tt>Long</tt> or <tt>long</tt>
| @MessageProperty("foo")
| <tt>message.getLongProperty("foo")</tt><br/>if this returns without throwing a <tt>MessageFormatException</tt>
|-
| Any
| <tt>Float</tt> or <tt>float</tt>
| @MessageProperty("foo")
| <tt>message.getFloatProperty("foo")</tt><br/>if this returns without throwing a <tt>MessageFormatException</tt>
|-
| Any
| <tt>Double</tt> or <tt>double</tt>
| @MessageProperty("foo")
| <tt>message.getDoubleProperty("foo")</tt><br/>if this returns without throwing a <tt>MessageFormatException</tt>
|-
| Any
| <tt>String</tt>
| @MessageProperty("foo")
| <tt>message.getStringProperty("foo")</tt><br/>if this returns without throwing a <tt>MessageFormatException</tt>
|} 

* If none of the following applies then the parameter is set to the default value [https://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5 as defined in the JLS] (e.g. <tt>null</tt> for objects, 0 for int etc)

* It is recommended that the callback method has a <tt>void</tt> return type. A non-<tt>void</tt> return type is allowed but the returned value will be ignored.

<b>Issue I15:</b> Any alternative proposals for the <tt>@MessageProperty</tt> or <tt>MessageHeader</tt> annotations?

<b>Issue I16:</b> The table above is based on the principle that if a parameter cannot be set to the required value due to it having an unsuitable type then it should simple be set to null (or a default primitive value) rather than throwing an exception and triggering message delivery. This is because there is no point in redelivering the message since it will simply fail again (and JMS does not define any dead message functionality). Is this the correct approach?

==Summary and links to javadocs==

The draft javadocs can be found [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/index.html?javax/jms/package-summary.html here]. Direct links to the javadocs for each class are given in the table below.

{|- border="1"
! New or modified?
! Interface or annotation?
! Name
| Link to javadocs
|-
| New
| Marker interface
| <tt>javax.jms.JMSMessageDrivenBean</tt>
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSMessageDrivenBean.html javadocs]
|-
| New
| Method annotation
| <tt>javax.jms.JMSListener</tt>
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSListener.html javadocs]
|-
| Modified
| Method or field annotation
| <tt>javax.jms.JMSConnectionFactory</tt>
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSConnectionFactory.html javadocs]
|-
| New
| Method annotation
| <tt>javax.jms.Acknowledge</tt>
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/Acknowledge.html javadocs]
|-|-
| New
| Method annotation
| <tt>javax.jms.SubscriptionDurability</tt>
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/SubscriptionDurability.html javadocs]
|-
| New
| Method annotation
| <tt>javax.jms.SubscriptionName</tt>
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/SubscriptionName.html javadocs]
|-
| New
| Method annotation
| <tt>javax.jms.ClientId</tt>
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/ClientId.html javadocs]
|-
| New
| Method annotation
| <tt>javax.jms.MessageSelector</tt>
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageSelector.html javadocs]
|-
| New
| Parameter annotation
| <tt>javax.jms.MessageHeader</tt>
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageHeader.html javadocs]
|-
| New
| Parameter annotation
| <tt>javax.jms.MessageProperty</tt>
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageProperty.html javadocs]
|} 