# Flexible JMS MDBs: method annotations (version 5)

This page discusses the method annotations on flexible JMS MDBs.

_These are the latest proposals that were made before work on JMS 2.1 was halted and JSR 368 withdrawn. There are retained here as a historical record and in case they prove useful to a future JMS expert group._

## Contents

* auto-gen TOC:
{:toc}

##  Latest proposal (option H) 

**Note:** This page has been updated to reflect the discussion in the meeting on Thursday 19th November. 

Key features of this proposal

* The callback method is specified using `@QueueListener("java:global/queueName")` or `@TopicListener("java:global/topicName")`. 
  * This means it becomes mandatory to specify the destination type, thereby removing the ambiguity with classic JMS MDBs which left it undefined whether the destination type must be specified. 
  * We discussed whether we should take the opposite approach and specify that the destination type was never required. This would allow a single  `@JMSListener` annotation. However we decided that the destination type was so important in the design of the MDB that applications should be required to specify it.
  * We could have achieved the same result by defining a single annotation with two mandatory elements, such as `@OnMessage(lookup="java@global/myQueue", type=javax.jms.Queue)`. However we decided that it was less verbose to define two separate annotations, `@QueueListener` or `@TopicListener`, each of which had a single element.
  * We discussed whether there should be three annotations `@QueueListener`, `@NonDurableTopicListener` and `@DurableTopicListener` rather than two. However this was rejected because whether the subscription was durable or not was really a sub-classification of the topic case and so should be specified using a separate annotation. That is, the annotations should reflect the fact that there are really two options, of which one has two variants. In addition the names of the annotations were getting complicated (especially `@NonDurableTopicListener`).
  * There wasn't a clear consensus on the best name for these annotations. Alternatives to  `@QueueListener` include `@JMSQueueListener`, which was rejected because we wanted to avoid "JMS" everywhere, and `@OnMessageQueue` and `@OnQueue`, which didn't sound right. The noun "listener" isn't ideal here since it is the object that is the listener, not the callback method, but we couldn't think of anything better. It does have the benefit of being an established noun in this context (e.g. in the `MessageListener` interface).

* If the user has specified `@TopicListener("java:global/topicName")` then they may additionally specify `@DurableSubscription` to designate that the subscription is durable. They may additionally specify `@SubscriptionName` to specify the name of the durable subscription, and `ClientId` to specify the clientId. 
  * If `@DurableSubscription` is not supplied then a non-durable subscription will be used. There is no corresponding `@NonDurableSubscription` annotation. This is because there has always been a long-standing convention in JMS that a topic subscription is non-durable unless otherwise specified. For example, the methods `Session#createConsumer` and `TopicSession#createSubscriber` always create non-durable subscriptions. If the user wants a durable subscription they need to specify this by calling  `Session#createDurableConsumer` or `TopicSession#createDurableSubscriber`.

  * (**Resolved issue**) We discussed combining these three separate annotations into a single annotation with two elements. For example instead of 
```
@TopicListener("java:global/topicName")
@DurableSubscription
@SubscriptionName("mySubName")
@ClientId("myClientId")
```
we could have
```
@TopicListener("java:global/topicName")
@DurableSubscription(name="mySubName", clientId="myClientId")
```
However we decided to define three separate annotations. Discussion and reasons [here](https://java.net/projects/jms-spec/lists/users/archive/2015-11/message/14).

* `@AutoAcknowledge` or `@DupsOKAcknowledge` are used to specify the corresponding non-transactional acknowledgement mode. To use these modes the MDB must also be configured to use bean-managed transaction demarcation, such as using the class-level annotation `@TransactionManagement(value=TransactionManagementType.BEAN)`. The application server will be recommended to fail deployment if the user specifies `@AutoAcknowledge` or `@DupsOKAcknowledge` without also configuring bean-managed transaction demarcation.
  * The goal here is to prevent the user from setting `@AutoAcknowledge` or `@DupsOKAcknowledge` without realising that they also need to configure bean-managed transaction demarcation. Ideally the application server or resource adapter would be required to fail deployment if the user specifies `@AutoAcknowledge` or `@DupsOKAcknowledge` but leaves the MDB configured to use container-managed transactions. However there is no standard way for a resource adapter to find out whether bean-managed transaction demarcation has been configured, so this needs to be just a recommendation. 
  * This is different to classic JMS MDBs, where the EJB 3.2 spec suggests that if `acknowledgeMode` is set to `Auto-acknowledge` or `Dups-ok-acknowledge` but the MDB is configured to use container-managed transactions then the value of `acknowledgeMode` should be ignored.
  * We considered whether `@AutoAcknowledge` and `@DupsOKAcknowledge` should force the MDB to use bean-managed transaction demarcation, irrespective of what has been configured. However since the way that transaction demarcation is configured is an old and long-established feature of EJBs we decided that it would be confusing and inconsistent to override it.
  * (**Open issue**): Since `@AutoAcknowledge` and `@DupsOKAcknowledge` are alternatives and are not valid together, should we combine them in a single annotation which takes an enumerated type value. Such as `@AcknowledgeMode(AcknowledgeMode.Mode.AutoAcknowledge)` and `@AcknowledgeMode(AcknowledgeMode.Mode.DupsOKAcknowledge)`? If so, should the enumerated type be a nested class of the annotation, or a separate type? If it's a separate type, what should its name be? (it can't be have the same name as the annotation).
  * (**Open issue**): Should we define a third element such as `@javax.jms.Transactional` or `@javax.jms.AssertTransactional`  which could be used to state that a container-managed XA transaction was required, with the application server or resource adapter being recommended to fail deployment if the user had configured bean-managed transactions?  This is unnecessary since it is a basic principle of both EJBs and MDBs that methods use a container-managed XA transaction by default.

* Other properties are set using individual annotations: ` @JMSConnectionFactory` (which reuses an existing annotation) and `MessageSelector`.

### Queue (option G)
```
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
```

### Non-Durable subscription on topic (option G)
```
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
```

### Durable subscription on topic (option G)
```
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
```

### Other notes (option G)

* These annotations will be designed to allow JMS MDBs to define multiple callback methods. However in the first instance only a single callback method will be allowed. 

* The annotations `@QueueListener`, `@TopicListener`, `@DurableSubscription`, `@SubscriptionName`, and `@ClientId` may only be specified on the callback method.

* The annotations `@JMSConnectionFactory`, `@MessageSelector`, `@DupsOKAcknowledge`, `@AutoAcknowledge`, `@ListenerProperty` may be specified on either the callback method or on class. If an annotation is specified on the class then it applies to all callback methods. 

* The annotation `@JMSConnectionFactory` is already defined in JMS 2.0. It can be used as a field annotation to specify the connection factory used when creating an injected  `JMSContext`. This usage is unchanged.

* If an annotation is specified at class level then a contradictory annotation must not be specified at method level. Doing so will cause deployment to fail. 

* (**Open issue**) Instead of forbidding an annotation to be specified at both class and method level, should we specify the latter as overriding the former? No, this would cause confusion. Also, since this would be one part of the class overriding another part of the same class, it's not necessary. Overrides are relevant when you're trying to override something you can't change, like an XML deployment descriptor overriding something in Java code. But that's not the case here. 

* (**Open issue**) If an activation property is specified using both an annotation and an activation property (either in the Java code or in the XML deployment descriptor), should we specify the latter as overriding the former? Perhaps yes, since this allows the user to override annotations using the XML deployment descriptor. 

* As previously agreed, these annotations must not be used on  a message-driven bean that implements the MessageListener interface. Doing so will cause deployment to fail. (Except of course when using `@JMSConnectionFactory` on an injected `JMSContext`).
