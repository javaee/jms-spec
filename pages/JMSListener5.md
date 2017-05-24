# Flexible JMS MDBs: method annotations (version 5)</h1>

This page discusses the method annotations on flexible JMS MDBs

__TOC__

== Latest proposal (option H)==

'''Note:''' This page has been updated to reflect the discussion in the meeting on Thursday 19th November. 

Key features of this proposal

* The callback method is specified using <tt>@QueueListener("java:global/queueName")</tt> or <tt>@TopicListener("java:global/topicName")</tt>. <p/>
**This means it becomes mandatory to specify the destination type, thereby removing the ambiguity with classic JMS MDBs which left it undefined whether the destination type must be specified. <p/>
** We discussed whether we should take the opposite approach and specify that the destination type was never required. This would allow a single  <tt>@JMSListener</tt> annotation. However we decided that the destination type was so important in the design of the MDB that applications should be required to specify it.<p/>
** We could have achieved the same result by defining a single annotation with two mandatory elements, such as <tt>@OnMessage(lookup="java@global/myQueue", type=javax.jms.Queue)</tt>. However we decided that it was less verbose to define two separate annotations, <tt>@QueueListener</tt> or <tt>@TopicListener</tt>, each of which had a single element.<p/>
** We discussed whether there should be three annotations <tt>@QueueListener</tt>, <tt>@NonDurableTopicListener</tt> and <tt>@DurableTopicListener</tt> rather than two. However this was rejected because whether the subscription was durable or not was really a sub-classification of the topic case and so should be specified using a separate annotation. That is, the annotations should reflect the fact that there are really two options, of which one has two variants. In addition the names of the annotations were getting complicated (especially <tt>@NonDurableTopicListener</tt>).<p/>
** There wasn't a clear consensus on the best name for these annotations. Alternatives to  <tt>@QueueListener</tt> include <tt>@JMSQueueListener</tt>, which was rejected because we wanted to avoid "JMS" everywhere, and <tt>@OnMessageQueue</tt> and <tt>@OnQueue</tt>, which didn't sound right. The noun "listener" isn't ideal here since it is the object that is the listener, not the callback method, but we couldn't think of anything better. It does have the benefit of being an established noun in this context (e.g. in the <tt>MessageListener</tt> interface).

* If the user has specified <tt>@TopicListener("java:global/topicName")</tt> then they may additionally specify <tt>@DurableSubscription</tt> to designate that the subscription is durable. They may additionally specify <tt>@SubscriptionName</tt> to specify the name of the durable subscription, and <tt>ClientId</tt> to specify the clientId. <p/>
** If <tt>@DurableSubscription</tt> is not supplied then a non-durable subscription will be used. There is no corresponding <tt>@NonDurableSubscription</tt> annotation. This is because there has always been a long-standing convention in JMS that a topic subscription is non-durable unless otherwise specified. For example, the methods <tt>Session#createConsumer</tt> and <tt>TopicSession#createSubscriber</tt> always create non-durable subscriptions. If the user wants a durable subscription they need to specify this by calling  <tt>Session#createDurableConsumer</tt> or <tt>TopicSession#createDurableSubscriber</tt>.
<ul><ul><li>
('''Resolved issue''') We discussed combining these three separate annotations into a single annotation with two elements. For example instead of 
 @TopicListener("java:global/topicName")
 @DurableSubscription
 @SubscriptionName("mySubName")
 @ClientId("myClientId")
we could have
 @TopicListener("java:global/topicName")
 @DurableSubscription(name="mySubName", clientId="myClientId")

However we decided to define three separate annotations. Discussion and reasons [https://java.net/projects/jms-spec/lists/users/archive/2015-11/message/14 here].
</li></ul></ul>

* <tt>@AutoAcknowledge</tt> or <tt>@DupsOKAcknowledge</tt> are used to specify the corresponding non-transactional acknowledgement mode. To use these modes the MDB must also be configured to use bean-managed transaction demarcation, such as using the class-level annotation <tt>@TransactionManagement(value=TransactionManagementType.BEAN)</tt>. The application server will be recommended to fail deployment if the user specifies <tt>@AutoAcknowledge</tt> or <tt>@DupsOKAcknowledge</tt> without also configuring bean-managed transaction demarcation.<p/>
** The goal here is to prevent the user from setting <tt>@AutoAcknowledge</tt> or <tt>@DupsOKAcknowledge</tt> without realising that they also need to configure bean-managed transaction demarcation. Ideally the application server or resource adapter would be required to fail deployment if the user specifies <tt>@AutoAcknowledge</tt> or <tt>@DupsOKAcknowledge</tt> but leaves the MDB configured to use container-managed transactions. However there is no standard way for a resource adapter to find out whether bean-managed transaction demarcation has been configured, so this needs to be just a recommendation. 
** This is different to classic JMS MDBs, where the EJB 3.2 spec suggests that if <tt>acknowledgeMode</tt> is set to <tt>Auto-acknowledge</tt> or <tt>Dups-ok-acknowledge</tt> but the MDB is configured to use container-managed transactions then the value of <tt>acknowledgeMode</tt> should be ignored.
** We considered whether <tt>@AutoAcknowledge</tt> and <tt>@DupsOKAcknowledge</tt> should force the MDB to use bean-managed transaction demarcation, irrespective of what has been configured. However since the way that transaction demarcation is configured is an old and long-established feature of EJBs we decided that it would be confusing and inconsistent to override it.
** ('''Open issue'''): Since <tt>@AutoAcknowledge</tt> and <tt>@DupsOKAcknowledge</tt> are alternatives and are not valid together, should we combine them in a single annotation which takes an enumerated type value. Such as <tt>@AcknowledgeMode(AcknowledgeMode.Mode.AutoAcknowledge)</tt> and <tt>@AcknowledgeMode(AcknowledgeMode.Mode.DupsOKAcknowledge)</tt>? If so, should the enumerated type be a nested class of the annotation, or a separate type? If it's a separate type, what should its name be? (it can't be have the same name as the annotation).
** ('''Open issue'''): Should we define a third element such as <tt>@javax.jms.Transactional</tt> or <tt>@javax.jms.AssertTransactional</tt>  which could be used to state that a container-managed XA transaction was required, with the application server or resource adapter being recommended to fail deployment if the user had configured bean-managed transactions?  This is unnecessary since it is a basic principle of both EJBs and MDBs that methods use a container-managed XA transaction by default.

* Other properties are set using individual annotations: <tt> @JMSConnectionFactory</tt> (which reuses an existing annotation) and <tt>MessageSelector</tt>.

===Queue (option G)===

 @MessageDriven
 public class MyMessageBean {
 
   @QueueListener(java:global/java:global/requestQueue")
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @DupsOKAcknowledge
   @ListenerProperty(name="foo1", value="bar1")
   @ListenerProperty(name="foo2", value="bar2")
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Non-Durable subscription on topic (option G)===

 @MessageDriven
 public class MyMessageBean {
 
   @TopicListener("java:global/java:global/someTopic")
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @AutoAcknowledge
   @ListenerProperty(name="foo1", value="bar1")
   @ListenerProperty(name="foo2", value="bar2")
   )  
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Durable subscription on topic (option G)===

 @MessageDriven
 public class MyMessageBean {
 
   @TopicListener(java:global/java:global/someTopic")
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @DurableSubscription
   @SubscriptionName("mySubName")
   @ClientId(myClientId")
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Other notes (option G)===

* These annotations will be designed to allow JMS MDBs to define multiple callback methods. However in the first instance only a single callback method will be allowed. 

* The annotations <tt>@QueueListener</tt>, <tt>@TopicListener</tt>, <tt>@DurableSubscription</tt>, <tt>@SubscriptionName</tt>, and <tt>@ClientId</tt> may only be specified on the callback method.

* The annotations <tt>@JMSConnectionFactory</tt>, <tt>@MessageSelector</tt>, <tt>@DupsOKAcknowledge</tt>, <tt>@AutoAcknowledge</tt>, <tt>@ListenerProperty</tt> may be specified on either the callback method or on class. If an annotation is specified on the class then it applies to all callback methods. 

* The annotation <tt>@JMSConnectionFactory</tt> is already defined in JMS 2.0. It can be used as a field annotation to specify the connection factory used when creating an injected  <tt>JMSContext</tt>. This usage is unchanged.

* If an annotation is specified at class level then a contradictory annotation must not be specified at method level. Doing so will cause deployment to fail. 

* ('''Open issue''') Instead of forbidding an annotation to be specified at both class and method level, should we specify the latter as overriding the former? No, this would cause confusion. Also, since this would be one part of the class overriding another part of the same class, it's not necessary. Overrides are relevant when you're trying to override something you can't change, like an XML deployment descriptor overriding something in Java code. But that's not the case here. 

* ('''Open issue''') If an activation property is specified using both an annotation and an activation property (either in the Java code or in the XML deployment descriptor), should we specify the latter as overriding the former? Perhaps yes, since this allows the user to override annotations using the XML deployment descriptor. 

* As previously agreed, these annotations must not be used on  a message-driven bean that implements the MessageListener interface. Doing so will cause deployment to fail. (Except of course when using <tt>@JMSConnectionFactory</tt> on an injected <tt>JMSContext</tt>).
