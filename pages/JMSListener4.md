<h1>Flexible JMS MDBs: method annotations (version 4) </h1>

This page discusses the method annotations on flexible JMS MDBs

__TOC__

== Original proposal (option A)==

[https://java.net/projects/jms-spec/pages/JMSListener3#Recap_of_current_proposal_%28Option_A%29 Description of option A]

===Queue (option A)===

 @MessageDriven
 public class MyMessageBean {
 
   @JMSListener(lookup="java:global/java:global/requestQueue",type=JMSListener.Type.QUEUE)
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @Acknowledge(Acknowledge.Mode.DUPS_OK_ACKNOWLEDGE)  
   @ListenerProperty(name="foo1", value="bar1")
   @ListenerProperty(name="foo2", value="bar2")
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Non-Durable subscription on topic (option A)===

 @MessageDriven
 public class MyMessageBean {
 
   @JMSNonDurableTopicListener(
   @JMSListener(lookup="java:global/java:global/someTopic",type=JMSListener.Type.TOPIC)
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @Acknowledge(Acknowledge.Mode.DUPS_OK_ACKNOWLEDGE)  
   @ListenerProperty(name="foo1", value="bar1")
   @ListenerProperty(name="foo2", value="bar2")
   )  
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Durable subscription on topic (option A)===

 @MessageDriven
 public class MyMessageBean {
 
   @JMSDurableTopicListener(
   @JMSListener(lookup="java:global/java:global/someTopic",type=JMSListener.Type.TOPIC)
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @Acknowledge(Acknowledge.Mode.DUPS_OK_ACKNOWLEDGE)  
   @SubscriptionName("mySubName")
   @SubscriptionDurability(SubscriptionDurability.Mode.DURABLE)
   @ClientId("myClientId")
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Option A: discussion===

== Proposal in Early Draft (option B) ==

===Queue (option B)===

 @MessageDriven
 public class MyMessageBean {
 
   @JMSQueueListener(
     destinationLookup="java:global/requestQueue",
     connectionFactoryLookup="java:global/connectionFactory",
     messageSelector="JMSType = 'car' AND colour = 'pink'",
     acknowledge=JMSQueueListener.Mode.DUPS_OK_ACKNOWLEDGE
   )  
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Non-Durable subscription on topic (option B) ===

 @MessageDriven
 public class MyMessageBean {
 
   @JMSNonDurableTopicListener(
     destinationLookup="java:global/someTopic",
     connectionFactoryLookup="java:global/connectionFactory",
     messageSelector="JMSType = 'car' AND colour = 'pink'",
     acknowledge=JMSNonDurableTopicListener.Mode.DUPS_OK_ACKNOWLEDGE
   )  
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Durable subscription on topic (option B) ===

 @MessageDriven
 public class MyMessageBean {
 
   @JMSDurableTopicListener(
     destinationLookup="java:global/someTopic",
     connectionFactoryLookup="java:global/connectionFactory",
     messageSelector="JMSType = 'car' AND colour = 'pink'",
     acknowledge=JMSDurableTopicListener.Mode.DUPS_OK_ACKNOWLEDGE,
     subscriptionName="mySubName",
     clientId="myClientId"
   )  
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Proposals in early draft: some observations===

Good:

* Very obvious whether a queue or topic, or whether topic subscription is durable or non-durable

* Once the user has chosen which attribute to use, the IDE can prompt them of the various elements available

* No need for the user to explicitly set destinationType or subscriptionDurability

Bad:

* Many elements (attributes) to set

* The word "listener" (as in JMSQueueListener) is inappropriate on a method as users think of the class that is the listener, not the callback method.

* There are issues with the <tt>acknowledge</tt> element:
** It is set to a enumerated type specific to the annotation. This is verbose and a likely cause of confusion. 
** It is ignored unless bean-managed transactions have been explicitly configured using the class annotation <<tt>@TransactionManagement(value=TransactionManagementType.BEAN)</tt>. Ideally the two settings should be combined, but since the <tt>@TransactionManagement</tt> annotation is used for all types of EJB we can't extend it just for JMS (and it would be confusing if tried to replace it with a different annotation just for flexible JMS MDBs). SO we're probably stuck with this.
** There is never any need to set the <tt>acknowledge</tt> element to AUTO_ACKNOWLEDGE since this is the default. So the only time you'd ever need to set this element is to configure DUPS_OK_ACKNOWLEDGE. If we removed the ability to set auto-ack it may avoid users mistaking thinking this is all they need to do to turn off bean-managed transactions.

== New option F ==

This option is based on the discussion at the JMS face-to-face meeting on 29th October, and marks a return to the original approach which used a larger number of simpler annotations rather than using a smaller number of larger annotations.

===Queue (option F)===

 @MessageDriven
 public class MyMessageBean {
 
   @OnMessage(java:global/requestQueue")
   @Queue 
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @DupsOKAcknowledge
   @ListenerProperty(name="foo1", value="bar1")
   @ListenerProperty(name="foo2", value="bar2")
   )  
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Non-Durable subscription on topic (option F)===

 @MessageDriven
 public class MyMessageBean {
 
   @OnMessage("java:global/someTopic")
   @Topic
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @DupsOKAcknowledge
   @ListenerProperty(name="foo1", value="bar1")
   @ListenerProperty(name="foo2", value="bar2")
   )  
   public void myMessageCallback(Message message) {
     ...
   }
 }

===Durable subscription on topic (option F)===

 @MessageDriven
 public class MyMessageBean {
 
   @OnMessage("java:global/someTopic")
   @Topic
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @DupsOKAcknowledge
   @DurableSubscription(name="mySubName", clientId="myClientId")
   @ListenerProperty(name="foo1", value="bar1")
   @ListenerProperty(name="foo2", value="bar2")
   public void myMessageCallback(Message message) {
     ...
   }
 }

===New option F: discussion===

* <tt>@OnMessage</tt> is used to define the callback method and may only be specified at method level. 
** The <tt>value</tt> element defines the JNDI name of the destination (i.e. the <tt>destinationLookup</tt> property) and must be provided.
** This doesn't use the word "listener" which makes it more appropriate for a callback method. 
** The name is consistent with the existing annotation <tt>@javax.websocket.OnMessage</tt>

*  <tt>@Queue</tt>, <tt>@Topic</tt>, <tt>@JMSConnectionFactory</tt>, <tt>MessageSelector</tt>,  <tt>@DupsOKAcknowledge</tt> <tt>@DurableSubscription</tt> and <tt>ListenerProperty</tt> may be specified at either class or method level. Annotations at method level override annotations are class level.
** If any of <tt>@Queue</tt>, <tt>@Topic</tt>, <tt>@JMSConnectionFactory</tt>, <tt>MessageSelector</tt>,  <tt>@DupsOKAcknowledge</tt> <tt>@DurableSubscription</tt> and <tt>ListenerProperty</tt> are specified at class level and no callback method is defined then deployment will fail.
** If any of <tt>@Queue</tt>, <tt>@Topic</tt>, <tt>@JMSConnectionFactory</tt>, <tt>MessageSelector</tt>,  <tt>@DupsOKAcknowledge</tt> <tt>@DurableSubscription</tt> and <tt>ListenerProperty</tt> are specified at method level but the method is not defined to be a callback method then deployment will fail.

* <tt>JMSConnectionFactory</tt> is an existing annotation (used when injecting <tt>JMSContext</tt> objects) which is being extended for use here. It is optional whether this annotation is used, but if it is used then the <tt>value</tt> element defines the JNDI name of the connection factory (i.e. the <tt>connectionfactoryLookup</tt> property) and must be provided. If this annotation is not used then the application server or resource adapter may either provide a suitable default connection factory or cause deployment to fail. (Note that  we can't simply say that by default the Java EE platform default JMS connection factory is used since this may not be appropriate for the resource adapter being used).

* <tt>@Queue</tt> and <tt>@Topic</tt> define the value of the <tt>destinationType</tt> property.  
** <tt>@Queue</tt> specifies that the destination is a queue. 
** <tt>@Topic</tt> specifies that the destination is a topic. 
** If both are specified on the same method, or both are specified at class level, then deployment will fail.

* What happens if neither <tt>@Queue</tt> nor <tt>@Topic</tt> are specified? Currently the EJB spec does not define what happens if <tt>destinationType</tt> is not specified 
** (Proposed) It is optional whether <tt>@Queue</tt> or <tt>@Topic</tt> are specified. There is no default value. The container will discover the destination type using a new method <tt>getDestinationType</tt> on the <tt>Destination</tt> interface. If <tt>@Queue</tt> or <tt>@Topic</tt>  is specified then deployment will fail if the specified type does not match the actual type of the destination. <br/>
** (Alternative 1) It is undefined whether <tt>@Queue</tt> or <tt>@Topic</tt> is required, and whether there is any default value. This is essentially how <tt>destinationType</tt> works at present.
** (Alternative 2) It is optional whether <tt>@Queue</tt> or <tt>@Topic</tt> are specified. If neither are specified then <tt>@Queue</tt> is assumed.  The container will discover the destination type (see above) and deployment will fail if the specified or default type does not match the actual type of the destination.
** (Alternative 3) There are no <tt>@Queue</tt> or <tt>@Topic</tt> annotations. The container will discover the destination type (see above).

* <tt>@MessageSelector</tt>. It is optional whether this annotation is used, but if it is used then the value element defines the message selector to be used (i.e. the <tt>messageSelector</tt> property) and must be provided. 

* <tt>@DurableSubscription</tt>. It is optional whether this annotation is used. If it is used then the <tt>subscriptionDurability</tt> property is set to <tt>Durable</tt>. It has two elements, both of which are optional. If <tt>clientId</tt> is specified then this sets the <tt>clientId</tt> property. If <tt>name</tt> is specified then this sets the <tt>subscriptionName</tt> property. 

* Annotations for setting acknowledgement mode:
** The <tt>@DupsOKAcknowledge</tt>  annotation is equivalent to setting acknowledgeMode to "Dups-ok-acknowledge". It is ignored unless the class is also annotated with <tt>@TransactionManagement(TransactionManagementType.BEAN)</tt>
** Do we also need a <tt>@AutoAcknowledge</tt> annotation? This would never be needed (since this is the default when container-managed transactions are specified). One advantage of not having a <tt>@AutoAcknowledge</tt> annotation is that a user might mistakenly think that specifying <tt>@AutoAcknowledge</tt> causes the MDB to use auto-acknowledgement, when in fact the user needs to specify the class annotation <tt>@TransactionManagement(TransactionManagementType.BEAN)</tt>.
** Alternatively we could define both <tt>@DupsOKAcknowledge</tt> and <tt>@AutoAcknowledge</tt> and require that deployment fails if the application has not also set <tt>@TransactionManagement(TransactionManagementType.BEAN)</tt>. This would prevent the user thinking they had configured auto-ack but still using a transaction.

* <tt>@ListenerProperty</tt> This annotation may be used to specify a single arbitrary activation property. The elements <tt>name</tt> and <tt>value</tt> must be specified. This is a repeatable annotation and may be specified multiple times.  

== Another proposal (option G)==

Key features of this proposal

* The user is required to specify the messaging semantic being used: queue, durable topic subscription, non-durable topic subscription using <tt>@JMSQueueListener</tt>, <tt>@JMSDurableTopicListener</tt>, <tt>@NonDurableTopicListener</tt>. 

* Other properties are set using individual annotations

* The user has the option of specifying the delivery QoS (transactional, auto-ack, dups-ok-ack) directly on the callback method. 

===Queue (option G)===

 @MessageDriven
 public class MyMessageBean {
 
   @JMSQueueListener(java:global/java:global/requestQueue")
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @DupsOKAcknowledge
   @ListenerProperty(name="foo1", value="bar1")
   @ListenerProperty(name="foo2", value="bar2")
   public void myMessageCallback(Message message) {
     ...
   }
 }

Setting <tt>@DupsOKAcknowledge</tt> overrides an explicit or by-default setting of <tt>@TransactionManagement(TransactionManagementType.CONTAINER)</tt>

===Non-Durable subscription on topic (option G)===

 @MessageDriven
 public class MyMessageBean {
 
   @JMSNonDurableTopicListener("java:global/java:global/someTopic")
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

Setting <tt>@AutoAcknowledge</tt> overrides an explicit or by-default setting of <tt>@TransactionManagement(TransactionManagementType.CONTAINER)</tt>

===Durable subscription on topic (option G)===

 @MessageDriven
 public class MyMessageBean {
 
   @JMSDurableTopicListener(java:global/java:global/someTopic")
   @JMSConnectionFactory("java:global/connectionFactory")
   @MessageSelector("JMSType = 'car' AND colour = 'pink'")
   @javax.jms.Transactional
   @SubscriptionName("mySubName")
   @ClientId("myClientId")
   public void myMessageCallback(Message message) {
     ...
   }
 }

Even though the <tt>@JMSDurableTopicListener</tt> has been specified, the subscription name and clientId are set using two separate annotations. This is because in practice these settings (which only became standard in EJB 3.2) are often not required. It also follows the approach of having multiple annotations rather than having a single annotation with multiple attributes.

===Option G: discussion===

* The user is required to specify the messaging semantic being used: queue, durable topic subscription, non-durable topic subscription using <tt>@JMSQueueListener</tt>, <tt>@JMSDurableTopicListener</tt>, <tt>@NonDurableTopicListener</tt>. 
** The idea here is that there are three messaging semantics, and that we should require the user to specify which they are using. It would be inconsistent to require the user to specify whether a topic subscription is durable or non-durable but not whether the destination is a queue or topic.
** This also reduces the number of annotations needed by combining the annotation which specifies the callback method with the annotation which specifies the messaging semantic.

* The user has the option of specifying the delivery QoS (transactional, auto-ack, dups-ok-ack) directly on the callback method. 
** At the very least, this could be taken as an assertion, so that if the user has configured an incompatible <tt>@TransactionManagement</tt> annotation on the class.
** We could extend this to be an alternative to setting <tt>@TransactionManagement</tt> annotation on the class. In that case we would need to consider what would happen if the two has incompatible settings. Would this be an error, or would one override the other?
** Instead of separate <tt> @AutoAcknowledge</tt>, <tt>@DupsOKAcknowledge</tt>, <tt>@javax.jms.Transactional</tt> annotations we could have a single annotation with three element values: <tt>@QOS(QOS.AutoAcknowledge)</tt>, <tt>@QOS(QOS.DupsOKAcknowledge)</tt>, <tt>@QOS(QOS.Transactional)</tt>.