# Proposed changes to JMSContext to support injection (Option 4)</h1>

==Summary==

This page proposes some changes to the JMS 2.0 simplified API to support the injection of <tt>JMSContext</tt> objects.

It discusses the potential confusion that might be caused by the sharing of injected <tt>JMSContext</tt> objects between different beans within the same scope, and proposes some changes to the <tt>JMSContext</tt> API, and some new restrictions, to avoid this. Although these proposals are intended to accompany the proposed new scoping rules defined in  [[JMSContextScopeProposalsv4p1|Injection of <tt>JMSContext</tt> objects - Proposals (version 4)]] they would apply to earlier scoping proposals as well.

__TOC__

==The problem==

Let's begin with a description of the problem we need to address. The example below uses the simplified API as defined in the JMS 2.0 Early Draft.

Consider a stateless session bean with a business method <tt>method1</tt> which calls the method <tt>method2</tt> on a second stateless session bean. Both methods use an injected <tt>JMSContext</tt>  to send a message.

This is Bean1

 @TransactionManagement(TransactionManagementType.CONTAINER)
 @Stateless
 public class Bean1 {
 
    @Resource(lookup="jms/inboundQueue") Queue queue;
 
    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    JMSContext context1;
 
    @EJB Bean2 bean2;
 
    @TransactionAttribute(REQUIRED)
    public void method1() {
        context1.send(queue,"Message 1");
        bean2.method2();
    }
 }

This is Bean2


 @TransactionManagement(TransactionManagementType.CONTAINER)
 @Stateless
 public class Bean2 {
 
    @Resource(lookup="jms/inboundQueue") Queue queue;
 
    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    JMSContext context2;
 
    @TransactionAttribute(REQUIRED)
    public void method2() {
        context2.send(queue,"Message 2");
    }
 }

Let's assume that the injected <tt>JMSContext</tt> has a scope which covers both method calls. We're not discussing the details of scope here, but in this example both transaction scope and request scope would achieve this. This means that since <tt>context1</tt> (used in <tt>Bean1</tt>) and <tt>context2</tt> (used in <tt>bean2</tt>) are both injected using identical annotations (e.g. specify the same connection factory) they would actually refer to the same <tt>JMSContext</tt> object.

This is in principle desirable because it would mean that 
* the two beans would use a single JMS connection rather than two, thereby reducing resource usage
* the two messages would use the same <tt>MessageProducer</tt> and <tt>Session</tt> and would therefore be delivered in order. 
* only a single <tt>XAResource</tt> would be used in the XA transaction which would allow the use of single-phase rather than two-phase commits

However it might potentially be confusing for developers. For example, if <tt>Bean1</tt> called
   context1.setTimeToLive(1000);
in order to give the first message a time-to-live of 1000ms, this would have the somewhat unexpected effect of also giving the second message a time-to-live of 1000ms as well, even though the second message was sent using a completely different bean, using what appears to be a completely separate <tt>JMSContext</tt> object.

This is the potentially confusing situation we need to avoid. There have been several previous attempts at addressing this issue:

* The JMS 2.0 Early Draft proposed using a separate <tt>JMSContext</tt> instance for each injection point. However this prevents the sharing of message order and <tt>XAResource</tt> object between different beans within the same scope.

* The proposal described as  [[JMSContextScopeProposals#Option_2|Option 2]]  also proposed using a separate <tt>JMSContext</tt> instance for each injection point, which had the same drawbacks.

* The proposal described as [[JMSContextScopeProposals#Option_3|Option 3]] proposed that although the injected <tt>JMSContext</tt> would have a combined transaction/method scope, the six JavaBean properties of the <tt>JMSContext</tt>'s underlying MessageProducer would have dependent scope so that changing a property in one bean would not affact a property in another bean, even if they both used the same underlying <tt>JMSContext</tt> .  This proposal received some support, but others found it confusing.

This page therefore makes a fourth proposal to address this issue.

==The solution (Option 4)==

Although the behaviour described above is the expected CDI behaviour when injecting objects with a "normal" (non-dependent) scope, the reason this is considered to be potentially confusing in the case of <tt>JMSContext</tt> objects is that the <tt>JMSContext</tt> object is stateful: you can call a method on one bean which affects its behaviour in another bean.

The solution to avoiding the possibility is to modify the <tt>JMSContext</tt> API to make it appear to the application to be stateless.

The following methods on <tt>JMSContext</tt> can be used to change its state:

* <tt>setDeliveryDelay</tt>
* <tt>setTimeToLive</tt>
* <tt>setPriority, getPriority</tt>
* <tt>setDeliveryMode</tt>
* <tt>setDisableMessageTimestamp</tt>
* <tt>setDisableMessageID</tt>
* <tt>setClientID</tt>
* <tt>setExceptionListener</tt>
* <tt>stop</tt>
* <tt>acknowledge</tt>
* <tt>commit</tt>
* <tt>rollback</tt>
* <tt>recover</tt>
* <tt>setAutoStart</tt>
* <tt>start</tt>
* <tt>close</tt>

The following sections will show how all these methods can be moved to another object, prohibited, or are already illegal in the cases where confusion might arise.

===Move the "message send" properties to a new JMSProducer object===

Let's start by considering the six methods which specify behaviour when subsequent messages are sent:

* <tt>setDeliveryDelay</tt>
* <tt>setTimeToLive</tt>
* <tt>setPriority</tt>
* <tt>setDeliveryMode</tt>
* <tt>setDisableMessageTimestamp</tt>
* <tt>setDisableMessageID</tt>

We can remove these methods from <tt>JMSContext</tt> and move them to a new <tt>JMSProducer</tt> object. We would also move all the send methods from <tt>JMSContext</tt> to <tt>JMSProducer</tt> . This means that an application that needed to send a message would do the following:

    JMSProducer producer = context.createProducer();
    producer.send(destination,message);

An application which needed to set the priority of this message would do the following:

    JMSProducer producer = context.createProducer();
    producer.setPriority(1);
    producer.send(destination,message);

Note that even if the <tt>JMSContext</tt> were injected, the <tt>JMSProducer</tt> would be an ordinary Java variable, typically an ordinary method variable which is scoped to a specific invocation of the method in which it is declared. This means that there is no possibility of calling <tt>setPriority</tt> on a <tt>JMSProducer</tt> in one bean and for it to have an effect on a <tt>JMSProducer</tt> in another bean.

One of the goals of the simplified API was to reduce the number of objects an application needed to create in order to send or receive a message. Adding a new <tt>JMSProducer</tt> object undermines this goal. However we can take reduce the amount of code needed by making all the setter methods return the <tt>JMSProducer</tt>, in order to allow method chaining:
    context.createProducer().setPriority(1).send(destination,message);

=== Other aspects of using a JMSProducer===

A happy side-effect of introducing a <tt>JMSProducer</tt> and allowing method chaining is that it allows us to drop methods such as
    void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive)
since an application can instead do
    context.createProducer().
      setDeliveryMode(DeliveryMode.NON_PERSISTENT).setPriority(1).setTimeToLive(1000).send(destination,message);

Similarly we can drop the need to have async variants of each send method, such as
    void send(Destination destination, Message message, CompletionListener completionListener)
by adding a new <tt>setAsync</tt> method to <tt>JMSProducer</tt> to allow
    context.createProducer().setAsync(completionListener).send(destination,message);   
or
    context.createProducer().setAsync(completionListener).send(destination,"This is a message");   

Note that the Destination to which a message is sent is specified using a parameter to the send method rather than using a <tt>setDestination</tt> method. This is because, unlike all the other properties of a <tt>JMSProducer</tt>,  a <tt>Destination</tt> must always be specified, and allowing it to be configured using   <tt>setDestination</tt> would imply that it was optional.

Although a <tt>JMSProducer</tt> looks a bit like a <tt>MessageProducer</tt> , the proposal here is that the <tt>MessageProducer</tt> is still associated with the <tt>JMSContext</tt>. This means that message order is guaranteed for all messages sent to a given destination using the same <tt>JMSContext</tt> , even if they use different <tt>JMSProducer</tt> objects. (There's actually more to JMS message order than this: see the JMS specification for a complete definition).

===Prohibit all the other methods which change the state of a JMSContext===

Now let's consider some of the other methods on <tt>JMSContext</tt> that make it appear stateful:

* <tt>setClientID</tt>
* <tt>setExceptionListener</tt>
* <tt>stop</tt>
* <tt>acknowledge</tt>
* <tt>commit</tt>
* <tt>rollback</tt>
* <tt>recover</tt>

None of these methods is actually permitted in the Java EE web or EJB containers.  <tt>setClientID</tt>, <tt>setExceptionListener</tt> and <tt>stop</tt> are all explicitly prohibited by the Java EE 6 spec whilst <tt>acknowledge</tt>, <tt>commit</tt>, <tt>rollback</tt> and <tt>commit</tt> are implicitly prohibited because client-acknowledgement (including local transactions) are not permitted in a Java EE web or EJB container.  This means that we won't need to worry about users using these methods on an injected <tt>JMSContext</tt> in a Java EE web or EJB container and becoming confused by this affecting an injected <tt>JMSContext</tt> in some other bean within the same scope.

The above methods are, however, permitted in the Java EE application client container (and in Java SE). Although CDI 1.0 does not require CDI injection to be supported in the application client container, it certainly permits it, and it would be desirable if a <tt>JMSContext</tt> object could be injected into such environments, at least in principle.

The CDI spec is not very clear about what scopes are available in the application client container, and the spec looks as if it could do with clarifying. However it seems reasonable to expect dependent scope, application scope, and request scope to be available (CDI 1.0 explicitly mentions that request scope is active during message delivery to a <tt>MessageListener</tt>). In addition, an application client that supports JTA transactions would be able to support any new transaction scope that might be defined in the future. 

This means that if JMS 2.0 defines the scope of an injected <tt>JMSContext</tt> to be request scope, or transaction scope, then the same <tt>JMSContext</tt> instance might be used from different beans within the same scope, and so calling a <tt>JMSContext</tt> method such as acknowledge() in one bean would affect the behaviour of a <tt>JMSContext</tt> in another bean within the same scope (if the <tt>JMSContext</tt> is injected using the same annotations), which is the behaviour we are trying to avoid. This means that would we would need to be aware that a future application client container that allows <tt>JMSContext</tt> objects to be injected would have this potentially confusing behaviour. 

The solution to this problem that is proposed here is simply to **prohibit the use of any of these methods if the <tt>JMSContext</tt> is injected**. If these methods are used on an injected <tt>JMSContext</tt>, an exception would be thrown. This would not not affect applications running in the Java EE web or EJB container, since these methods are not allowed anyway. However they would limit applications running in the Java EE application client container.  Application clients which needed to use these methods would need to manage the <tt>JMSContext</tt> themselves rather than inject it. This would mean creating it using the <tt>ConnectionFactory</tt> method <tt>createContext</tt> and calling <tt>close</tt> after use.

Next let's consider the following two methods which can change the state of a <tt>JMSContext</tt> :

* <tt>setAutoStart</tt>
* <tt>start</tt>

In JMS, a connection will not deliver messages to a consumer until the connection has been started.  In the standard JMS API, a connection is started by explicitly calling its <tt>start</tt> method.  In the JMS 2.0 simplified API, however, a <tt>JMSContext</tt>'s connection was automatically started when its first consumer was created, thereby removing the need to remember to call <tt>start</tt>. However there are some cases where an application needs to control when the connection is started. Such applications can disable automatic starting by calling <tt>setAutoStart(false)</tt> or by using the <tt>JMSAutoStart(false)</tt> annotation. They then need to call <tt>start</tt> explicitly.  

Both <tt>setAutoStart(false)</tt> and <tt>start</tt> change the state of an injected <tt>JMSContext</tt> and so  calling these methods in one bean would affect the behaviour of a <tt>JMSContext</tt> in another bean within the same scope (if the <tt>JMSContext</tt> is injected using the same annotations). 

Although this is potentially confusing, there is in fact little reason to call these methods in a Java EE web or EJB container. This is because the main reason for wanting to defer the start of a <tt>JMSContext</tt> is when setting up an asynchronous <tt>MessageListener</tt>, which is forbidden in a Java EE web or EJB container. However they might still do it, and they might actually need to do it in the application client container. 

The simplest way to avoid this confusion is to **prohibit the use of these methods if the <tt>JMSContext</tt> is injected**. This would not be a significant limitation in the Java EE web or EJB containers where these methods are not needed. However it would limit applications running in the Java EE application client container.  Application clients which needed to use these methods would need to manage the <tt>JMSContext</tt> themselves rather than inject it.

Since the <tt>@AutoStart</tt> annotation is in practice of little use in a Java EE web or EJB container there is little reason to keep it. So it is proposed we drop this annotation.

Finally let's consider the one remaining method that changes the state of a <tt>JMSContext</tt>.

* close

We don't actually want applications to call this method if the <tt>JMSContext</tt> is injected. This is because close() will be called by the container when the object falls out of scope. We can therefore define that if close() is called when the <tt>JMSContext</tt> is injected an exception is thrown.

===Additional feature: setting message properties on a JMSProducer===

Now that we have introduced the <tt>JMSProducer</tt> object there are a number of additional features we could add to improve the overall simplified API. 

We can add methods to <tt>JMSProducer</tt> which allow message properties to be set. This would allow message properties to be set for the new methods which send a payload directly such as
 send(Destination destination, String payload).

Currently the following methods on Message can be used to set a message property:

* <tt>void setBooleanProperty(String name, boolean value) </tt>
* <tt>void setByteProperty(String name, byte value)</tt>
* <tt>void setDoubleProperty(String name, double value)</tt> 
* <tt>void setFloatProperty(String name, float value)</tt>
* <tt>void setIntProperty(String name, int value)</tt>
* <tt>void setLongProperty(String name, long value)</tt>
* <tt>void setObjectProperty(String name, Object value)</tt>
* <tt>void setShortProperty(String name, short value)</tt>
* <tt>void setStringProperty(String name, String value)</tt>

We could copy all these methods onto the <tt>JMSProducer</tt> object, define them to return the <tt>JMSProducer</tt> to allow method chaining, and give them all the same name, <tt>setProperty</tt>. We can leave it to Java to work out from the parameters which one to use. This gives us the following methods on <tt>JMSProducer</tt>:

* <tt>JMSProducer setProperty(String name, boolean value)</tt>  
* <tt>JMSProducer setProperty(String name, byte value)</tt>
* <tt>JMSProducer setProperty(String name, double value) </tt>
* <tt>JMSProducer setProperty(String name, float value)</tt>
* <tt>JMSProducer setProperty(String name, int value)</tt>
* <tt>JMSProducer setProperty(String name, long value)</tt>
* <tt>JMSProducer setProperty(String name, Object value)</tt>
* <tt>JMSProducer setProperty(String name, short value)</tt>
* <tt>JMSProducer setProperty(String name, String value)</tt>

This would then allow the properties of a <tt>Message</tt>  to be set prior to sending it as follows
    context.createProducer().setProperty("foo","bar").send(destination,message);
or in the case of the methods which allow the application to sent the payload directly:
    context.createProducer().setProperty("foo","bar").send(destination,"This is a message");

===Additional feature: setting JMS message headers on a JMSProducer===

We could also add methods to <tt>JMSProducer</tt> to allow various message headers to be set. The following methods on <tt>Message</tt> allow the application to set a message header before a message is sent:
* <tt>void setJMSReplyTo(Destination replyTo)</tt>
* <tt>void setJMSType(String type)</tt>
* <tt>void  setJMSCorrelationID(String correlationID)</tt>
* <tt>void  setJMSCorrelationIDAsBytes(byte[] correlationID)</tt>

We could copy all these methods onto the <tt>JMSProducer</tt> object and define them to return the <tt>JMSProducer</tt> to allow method chaining. This gives us the following methods on <tt>JMSProducer</tt>:
* <tt>JMSProducer setJMSReplyTo(Destination replyTo)</tt>
* <tt>JMSProducer setJMSType(String type)</tt>
* <tt>JMSProducer setJMSCorrelationID(String correlationID)</tt>
* <tt>JMSProducer setJMSCorrelationIDAsBytes(byte[] correlationID)</tt>

This would allow these message headers to be set prior to sending as follows:

    context.createProducer().setJMSReplyTo(replyDest).send(destination,message);

==Summary of changes==

Here is a summary of these proposals, compared with what is in the JMS 2.0 Early Draft:

===Changes to <tt>JMSContext</tt>===

The following methods on <tt>JMSContext</tt> will be **removed**:

* <tt>setDeliveryDelay, getDeliveryDelay</tt>
* <tt>setTimeToLive, getTimeToLive</tt>
* <tt>setPriority, getPriority</tt>
* <tt>setDeliveryMode, getDeliveryMode</tt>
* <tt>setDisableMessageTimestamp, getDisableMessageTimestamp</tt>
* <tt>setDisableMessageID, getDisableMessageID</tt>

* <tt>send(Destination destination, Message message)</tt>
* <tt>send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive)</tt>

* <tt>send(Destination destination, byte[] payload)</tt>
* <tt>send(Destination destination, Map<String,Object> payload)</tt>
* <tt>send(Destination destination, Serializable payload)</tt>
* <tt>send(Destination destination, String payload)</tt>

* <tt>send(Destination destination, Message message, CompletionListener completionListener)</tt>
* <tt>send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive, CompletionListener completionListener)</tt>

The following methods on <tt>JMSContext</tt> will **throw an exception if the <tt>JMSContext</tt> is injected**. They will remain available for use when the <tt>JMSContext</tt> is managed by the application, though note that most of these methods are either prohibited or unnecessary in a Java EE web or EJB container.
*<tt>setClientID</tt>
* <tt>setExceptionListener</tt>
* <tt>start</tt>
* <tt>stop</tt>
* <tt>close</tt>
* <tt>setAutoStart</tt>
* <tt>acknowledge</tt>
* <tt>commit</tt>
* <tt>rollback</tt>
* <tt>recover</tt>

We will **add a new method to <tt>JMSContext</tt>**
* <tt>JMSProducer getProducer()</tt>

===New class JMSProducer===

We will define a new class <tt>JMSProducer</tt>. This a simple object used to configure send-time options before sending one or more messages. It does not hold any state that would ever need a <tt>close</tt> method. Despite the name, it does not itself wrap a <tt>MessageProducer</tt>. Instead the <tt>JMSContext</tt> continues to wrap an anonymous MessageProducer (or perhaps one for each destination) in order to allow the order of message sent to a destination using the same <tt>JMSContext</tt> to be guaranteed.

JMSProducer will have the following methods:

Methods to send messages:

* <tt>send(Destination destination, Message message)</tt>
* <tt>send(Destination destination, byte[] payload)</tt>
* <tt>send(Destination destination, Map<String,Object> payload)</tt>
* <tt>send(Destination destination, Serializable payload)</tt>
* <tt>send(Destination destination, String payload)</tt>

Note that the Destination to which a message is sent is specified using a parameter to the send method rather than using setDestination. This is because, unlike all the other properties of a JMSProducer,  a Destination must always be specified, and allowing it to be configured using  setDestination would imply that it was optional.

A method used to configure an async send

* <tt>JMSProducer setAsync(CompletionListener completionListener)</tt>

A method to query any previously-set <tt>CompletionListener</tt>

* <tt>CompletionListener getAsync()</tt>

Methods used to set message delivery options:

* <tt>JMSProducer setDeliveryDelay(long deliveryDelay);</tt>
* <tt>JMSProducer setTimeToLive(long timeToLive);</tt>
* <tt>JMSProducer setPriority(long priority);</tt>
* <tt>JMSProducer setDeliveryMode (int deliveryMode)</tt>
* <tt>JMSProducer setDisableMessageTimestamp (boolean value)</tt>
* <tt>JMSProducer setDisableMessageID (boolean value)</tt>

Methods to query any previously-set message delivery options

* <tt>long getDeliveryDelay()</tt>
* <tt>long getTimeToLive()</tt>
* <tt>int getPriority()</tt>
* <tt>int getDeliveryMode()</tt>
* <tt>boolean getDisableMessageTimestamp()</tt>
* <tt>boolean getDisableMessageID()</tt>

Methods used to set message properties of any messages subsequently sent. These will override any message property that is already set on the specified message.

* <tt>JMSProducer setProperty(String name, boolean value)</tt>
* <tt>JMSProducer setProperty(String name, byte value)</tt>
* <tt>JMSProducer setProperty(String name, double value)</tt>
* <tt>JMSProducer setProperty(String name, float value)</tt>
* <tt>JMSProducer setProperty(String name, int value)</tt>
* <tt>JMSProducer setProperty(String name, long value)</tt>
* <tt>JMSProducer setProperty(String name, Object value)</tt>
* <tt>JMSProducer setProperty(String name, short value)</tt>
* <tt>JMSProducer setProperty(String name, String value)</tt>

Methods to query any previously-set message properties

* <tt>boolean getBooleanProperty(String name)</tt>
* <tt>byte getByteProperty(String name)</tt>
* <tt>double getDoubleProperty(String name)</tt>
* <tt>gloat getFloatProperty(String name)</tt>
* <tt>int getIntProperty(String name)</tt>
* <tt>long getLongProperty(String name)</tt>
* <tt>Object getObjectProperty(String name)</tt>
* <tt>short getShortProperty(String name)</tt>
* <tt>String getStringProperty(String name)</tt>

Other methods to manage any previously-set message properties (included for consistency with <tt>Message</tt>

* <tt>JMSProducer clearProperties()</tt>
* <tt>Enumeration  getPropertyNames()</tt>
* <tt>boolean propertyExists(String name)</tt>

Methods to allow message headers of any messages subsequently sent. These will override any message header that is already set on the specified message.

* <tt>JMSProducer setJMSCorrelationID(String correlationID correlationID)</tt>
* <tt>JMSProducer setJMSCorrelationIDAsBytes(byte[] correlationID)</tt>
* <tt>JMSProducer setJMSType(String type)</tt>
* <tt>JMSProducer setJMSReplyTo(Destination replyTo)</tt>

Methods to query any previously-set message headers 

* <tt>String getJMSCorrelationID()</tt>
* <tt>byte[] getJMSCorrelationIDAsBytes()</tt>
* <tt>String getJMSType()</tt>
* <tt>String getJMSReplyTo()</tt>

===Other changes===

We will drop the proposal to have an <tt>@AutoStart</tt> annotation.
