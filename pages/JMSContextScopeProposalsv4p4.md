# Proposed changes to JMSContext to support injection (Option 4)
{: .no_toc}

This page proposes some changes to the JMS 2.0 simplified API to support the injection of `JMSContext` objects.

It discusses the potential confusion that might be caused by the sharing of injected `JMSContext` objects between different beans within the same scope, and proposes some changes to the `JMSContext` API, and some new restrictions, to avoid this. Although these proposals are intended to accompany the proposed new scoping rules defined in  [Injection of JMSContext objects - Proposals (version 4](/jms-spec/pages/JMSContextScopeProposalsv4p1) they would apply to earlier scoping proposals as well.

## Contents
{: .no_toc}

* auto-gen TOC:
{:toc}

## The problem 

Let's begin with a description of the problem we need to address. The example below uses the simplified API as defined in the JMS 2.0 Early Draft.

Consider a stateless session bean with a business method `method1` which calls the method `method2` on a second stateless session bean. Both methods use an injected `JMSContext`  to send a message.

This is Bean1
```
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
```

This is Bean2
```
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
```
Let's assume that the injected `JMSContext` has a scope which covers both method calls. We're not discussing the details of scope here, but in this example both transaction scope and request scope would achieve this. This means that since `context1` (used in `Bean1`) and `context2` (used in `bean2`) are both injected using identical annotations (e.g. specify the same connection factory) they would actually refer to the same `JMSContext` object.

This is in principle desirable because it would mean that 
* the two beans would use a single JMS connection rather than two, thereby reducing resource usage
* the two messages would use the same `MessageProducer` and `Session` and would therefore be delivered in order. 
* only a single `XAResource` would be used in the XA transaction which would allow the use of single-phase rather than two-phase commits

However it might potentially be confusing for developers. For example, if `Bean1` called
```
context1.setTimeToLive(1000);
```
in order to give the first message a time-to-live of 1000ms, this would have the somewhat unexpected effect of also giving the second message a time-to-live of 1000ms as well, even though the second message was sent using a completely different bean, using what appears to be a completely separate `JMSContext` object.

This is the potentially confusing situation we need to avoid. There have been several previous attempts at addressing this issue:

* The JMS 2.0 Early Draft proposed using a separate `JMSContext` instance for each injection point. However this prevents the sharing of message order and `XAResource` object between different beans within the same scope.

* The proposal described as  [Option 2](/jms-spec/pages/JMSContextScopeProposals#option-2)  also proposed using a separate `JMSContext` instance for each injection point, which had the same drawbacks.

* The proposal described as [Option 3](/jms-spec/pages/JMSContextScopeProposals#option-3) proposed that although the injected `JMSContext` would have a combined transaction/method scope, the six JavaBean properties of the `JMSContext`'s underlying MessageProducer would have dependent scope so that changing a property in one bean would not affact a property in another bean, even if they both used the same underlying `JMSContext` .  This proposal received some support, but others found it confusing.

This page therefore makes a fourth proposal to address this issue.

## The solution (Option 4) 

Although the behaviour described above is the expected CDI behaviour when injecting objects with a "normal" (non-dependent) scope, the reason this is considered to be potentially confusing in the case of `JMSContext` objects is that the `JMSContext` object is stateful: you can call a method on one bean which affects its behaviour in another bean.

The solution to avoiding the possibility is to modify the `JMSContext` API to make it appear to the application to be stateless.

The following methods on `JMSContext` can be used to change its state:

* `setDeliveryDelay`
* `setTimeToLive`
* `setPriority, getPriority`
* `setDeliveryMode`
* `setDisableMessageTimestamp`
* `setDisableMessageID`
* `setClientID`
* `setExceptionListener`
* `stop`
* `acknowledge`
* `commit`
* `rollback`
* `recover`
* `setAutoStart`
* `start`
* `close`

The following sections will show how all these methods can be moved to another object, prohibited, or are already illegal in the cases where confusion might arise.

### Move the "message send" properties to a new JMSProducer object

Let's start by considering the six methods which specify behaviour when subsequent messages are sent:

* `setDeliveryDelay`
* `setTimeToLive`
* `setPriority`
* `setDeliveryMode`
* `setDisableMessageTimestamp`
* `setDisableMessageID`

We can remove these methods from `JMSContext` and move them to a new `JMSProducer` object. We would also move all the send methods from `JMSContext` to `JMSProducer` . This means that an application that needed to send a message would do the following:
```
JMSProducer producer = context.createProducer();
producer.send(destination,message);
```
An application which needed to set the priority of this message would do the following:
```
JMSProducer producer = context.createProducer();
producer.setPriority(1);
producer.send(destination,message);
```
Note that even if the `JMSContext` were injected, the `JMSProducer` would be an ordinary Java variable, typically an ordinary method variable which is scoped to a specific invocation of the method in which it is declared. This means that there is no possibility of calling `setPriority` on a `JMSProducer` in one bean and for it to have an effect on a `JMSProducer` in another bean.

One of the goals of the simplified API was to reduce the number of objects an application needed to create in order to send or receive a message. Adding a new `JMSProducer` object undermines this goal. However we can take reduce the amount of code needed by making all the setter methods return the `JMSProducer`, in order to allow method chaining:
```
context.createProducer().setPriority(1).send(destination,message);
```

###  Other aspects of using a JMSProducer

A happy side-effect of introducing a `JMSProducer` and allowing method chaining is that it allows us to drop methods such as
```
void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive)
```
since an application can instead do
```
context.createProducer().
  setDeliveryMode(DeliveryMode.NON_PERSISTENT).setPriority(1).setTimeToLive(1000).send(destination,message);
```
Similarly we can drop the need to have async variants of each send method, such as
```
void send(Destination destination, Message message, CompletionListener completionListener)
```
by adding a new `setAsync` method to `JMSProducer` to allow
```
context.createProducer().setAsync(completionListener).send(destination,message);   
```
or
```
context.createProducer().setAsync(completionListener).send(destination,"This is a message");   
```
Note that the Destination to which a message is sent is specified using a parameter to the send method rather than using a `setDestination` method. This is because, unlike all the other properties of a `JMSProducer`,  a `Destination` must always be specified, and allowing it to be configured using   `setDestination` would imply that it was optional.

Although a `JMSProducer` looks a bit like a `MessageProducer` , the proposal here is that the `MessageProducer` is still associated with the `JMSContext`. This means that message order is guaranteed for all messages sent to a given destination using the same `JMSContext` , even if they use different `JMSProducer` objects. (There's actually more to JMS message order than this: see the JMS specification for a complete definition).

### Prohibit all the other methods which change the state of a JMSContext

Now let's consider some of the other methods on `JMSContext` that mak    e it appear stateful:

* `setClientID`
* `setExceptionListener`
* `stop`
* `acknowledge`
* `commit`
* `rollback`
* `recover`

None of these methods is actually permitted in the Java EE web or EJB containers.  `setClientID`, `setExceptionListener` and `stop` are all explicitly prohibited by the Java EE 6 spec whilst `acknowledge`, `commit`, `rollback` and `commit` are implicitly prohibited because client-acknowledgement (including local transactions) are not permitted in a Java EE web or EJB container.  This means that we won't need to worry about users using these methods on an injected `JMSContext` in a Java EE web or EJB container and becoming confused by this affecting an injected `JMSContext` in some other bean within the same scope.

The above methods are, however, permitted in the Java EE application client container (and in Java SE). Although CDI 1.0 does not require CDI injection to be supported in the application client container, it certainly permits it, and it would be desirable if a `JMSContext` object could be injected into such environments, at least in principle.

The CDI spec is not very clear about what scopes are available in the application client container, and the spec looks as if it could do with clarifying. However it seems reasonable to expect dependent scope, application scope, and request scope to be available (CDI 1.0 explicitly mentions that request scope is active during message delivery to a `MessageListener`). In addition, an application client that supports JTA transactions would be able to support any new transaction scope that might be defined in the future. 

This means that if JMS 2.0 defines the scope of an injected `JMSContext` to be request scope, or transaction scope, then the same `JMSContext` instance might be used from different beans within the same scope, and so calling a `JMSContext` method such as acknowledge() in one bean would affect the behaviour of a `JMSContext` in another bean within the same scope (if the `JMSContext` is injected using the same annotations), which is the behaviour we are trying to avoid. This means that would we would need to be aware that a future application client container that allows `JMSContext` objects to be injected would have this potentially confusing behaviour. 

The solution to this problem that is proposed here is simply to **prohibit the use of any of these methods if the `JMSContext` is injected**. If these methods are used on an injected `JMSContext`, an exception would be thrown. This would not not affect applications running in the Java EE web or EJB container, since these methods are not allowed anyway. However they would limit applications running in the Java EE application client container.  Application clients which needed to use these methods would need to manage the `JMSContext` themselves rather than inject it. This would mean creating it using the `ConnectionFactory` method `createContext` and calling `close` after use.

Next let's consider the following two methods which can change the state of a `JMSContext` :

* `setAutoStart`
* `start`

In JMS, a connection will not deliver messages to a consumer until the connection has been started.  In the standard JMS API, a connection is started by explicitly calling its `start` method.  In the JMS 2.0 simplified API, however, a `JMSContext`'s connection was automatically started when its first consumer was created, thereby removing the need to remember to call `start`. However there are some cases where an application needs to control when the connection is started. Such applications can disable automatic starting by calling `setAutoStart(false)` or by using the `JMSAutoStart(false)` annotation. They then need to call `start` explicitly.  

Both `setAutoStart(false)` and `start` change the state of an injected `JMSContext` and so  calling these methods in one bean would affect the behaviour of a `JMSContext` in another bean within the same scope (if the `JMSContext` is injected using the same annotations). 

Although this is potentially confusing, there is in fact little reason to call these methods in a Java EE web or EJB container. This is because the main reason for wanting to defer the start of a `JMSContext` is when setting up an asynchronous `MessageListener`, which is forbidden in a Java EE web or EJB container. However they might still do it, and they might actually need to do it in the application client container. 

The simplest way to avoid this confusion is to **prohibit the use of these methods if the `JMSContext` is injected**. This would not be a significant limitation in the Java EE web or EJB containers where these methods are not needed. However it would limit applications running in the Java EE application client container.  Application clients which needed to use these methods would need to manage the `JMSContext` themselves rather than inject it.

Since the `@AutoStart` annotation is in practice of little use in a Java EE web or EJB container there is little reason to keep it. So it is proposed we drop this annotation.

Finally let's consider the one remaining method that changes the state of a `JMSContext`.

* close

We don't actually want applications to call this method if the `JMSContext` is injected. This is because close() will be called by the container when the object falls out of scope. We can therefore define that if close() is called when the `JMSContext` is injected an exception is thrown.

### Additional feature: setting message properties on a JMSProducer

Now that we have introduced the `JMSProducer` object there are a number of additional features we could add to improve the overall simplified API. 

We can add methods to `JMSProducer` which allow message properties to be set. This would allow message properties to be set for the new methods which send a payload directly such as
 send(Destination destination, String payload).

Currently the following methods on Message can be used to set a message property:

* `void setBooleanProperty(String name, boolean value) `
* `void setByteProperty(String name, byte value)`
* `void setDoubleProperty(String name, double value)` 
* `void setFloatProperty(String name, float value)`
* `void setIntProperty(String name, int value)`
* `void setLongProperty(String name, long value)`
* `void setObjectProperty(String name, Object value)`
* `void setShortProperty(String name, short value)`
* `void setStringProperty(String name, String value)`

We could copy all these methods onto the `JMSProducer` object, define them to return the `JMSProducer` to allow method chaining, and give them all the same name, `setProperty`. We can leave it to Java to work out from the parameters which one to use. This gives us the following methods on `JMSProducer`:

* `JMSProducer setProperty(String name, boolean value)`  
* `JMSProducer setProperty(String name, byte value)`
* `JMSProducer setProperty(String name, double value) `
* `JMSProducer setProperty(String name, float value)`
* `JMSProducer setProperty(String name, int value)`
* `JMSProducer setProperty(String name, long value)`
* `JMSProducer setProperty(String name, Object value)`
* `JMSProducer setProperty(String name, short value)`
* `JMSProducer setProperty(String name, String value)`

This would then allow the properties of a `Message`  to be set prior to sending it as follows
```
context.createProducer().setProperty("foo","bar").send(destination,message);
```
or in the case of the methods which allow the application to sent the payload directly:
```
context.createProducer().setProperty("foo","bar").send(destination,"This is a message");
```

### Additional feature: setting JMS message headers on a JMSProducer

We could also add methods to `JMSProducer` to allow various message headers to be set. The following methods on `Message` allow the application to set a message header before a message is sent:
* `void setJMSReplyTo(Destination replyTo)`
* `void setJMSType(String type)`
* `void  setJMSCorrelationID(String correlationID)`
* `void  setJMSCorrelationIDAsBytes(byte[] correlationID)`

We could copy all these methods onto the `JMSProducer` object and define them to return the `JMSProducer` to allow method chaining. This gives us the following methods on `JMSProducer`:
* `JMSProducer setJMSReplyTo(Destination replyTo)`
* `JMSProducer setJMSType(String type)`
* `JMSProducer setJMSCorrelationID(String correlationID)`
* `JMSProducer setJMSCorrelationIDAsBytes(byte[] correlationID)`

This would allow these message headers to be set prior to sending as follows:

```
context.createProducer().setJMSReplyTo(replyDest).send(destination,message);
```

## Summary of changes 

Here is a summary of these proposals, compared with what is in the JMS 2.0 Early Draft:

### Changes to `JMSContext`

The following methods on `JMSContext` will be **removed**:

* `setDeliveryDelay, getDeliveryDelay`
* `setTimeToLive, getTimeToLive`
* `setPriority, getPriority`
* `setDeliveryMode, getDeliveryMode`
* `setDisableMessageTimestamp, getDisableMessageTimestamp`
* `setDisableMessageID, getDisableMessageID`

* `send(Destination destination, Message message)`
* `send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive)`

* `send(Destination destination, byte[] payload)`
* `send(Destination destination, Map<String,Object> payload)`
* `send(Destination destination, Serializable payload)`
* `send(Destination destination, String payload)`

* `send(Destination destination, Message message, CompletionListener completionListener)`
* `send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive, CompletionListener completionListener)`

The following methods on `JMSContext` will **throw an exception if the `JMSContext` is injected**. They will remain available for use when the `JMSContext` is managed by the application, though note that most of these methods are either prohibited or unnecessary in a Java EE web or EJB container.
*`setClientID`
* `setExceptionListener`
* `start`
* `stop`
* `close`
* `setAutoStart`
* `acknowledge`
* `commit`
* `rollback`
* `recover`

We will **add a new method to `JMSContext`**
* `JMSProducer getProducer()`

### New class JMSProducer    

We will define a new class `JMSProducer`. This a simple object used to configure send-time options before sending one or more messages. It does not hold any state that would ever need a `close` method. Despite the name, it does not itself wrap a `MessageProducer`. Instead the `JMSContext` continues to wrap an anonymous MessageProducer (or perhaps one for each destination) in order to allow the order of message sent to a destination using the same `JMSContext` to be guaranteed.

JMSProducer will have the following methods:

Methods to send messages:

* `send(Destination destination, Message message)`
* `send(Destination destination, byte[] payload)`
* `send(Destination destination, Map<String,Object> payload)`
* `send(Destination destination, Serializable payload)`
* `send(Destination destination, String payload)`

Note that the Destination to which a message is sent is specified using a parameter to the send method rather than using setDestination. This is because, unlike all the other properties of a JMSProducer,  a Destination must always be specified, and allowing it to be configured using  setDestination would imply that it was optional.

A method used to configure an async send

* `JMSProducer setAsync(CompletionListener completionListener)`

A method to query any previously-set `CompletionListener`

* `CompletionListener getAsync()`

Methods used to set message delivery options:

* `JMSProducer setDeliveryDelay(long deliveryDelay);`
* `JMSProducer setTimeToLive(long timeToLive);`
* `JMSProducer setPriority(long priority);`
* `JMSProducer setDeliveryMode (int deliveryMode)`
* `JMSProducer setDisableMessageTimestamp (boolean value)`
* `JMSProducer setDisableMessageID (boolean value)`

Methods to query any previously-set message delivery options

* `long getDeliveryDelay()`
* `long getTimeToLive()`
* `int getPriority()`
* `int getDeliveryMode()`
* `boolean getDisableMessageTimestamp()`
* `boolean getDisableMessageID()`

Methods used to set message properties of any messages subsequently sent. These will override any message property that is already set on the specified message.

* `JMSProducer setProperty(String name, boolean value)`
* `JMSProducer setProperty(String name, byte value)`
* `JMSProducer setProperty(String name, double value)`
* `JMSProducer setProperty(String name, float value)`
* `JMSProducer setProperty(String name, int value)`
* `JMSProducer setProperty(String name, long value)`
* `JMSProducer setProperty(String name, Object value)`
* `JMSProducer setProperty(String name, short value)`
* `JMSProducer setProperty(String name, String value)`

Methods to query any previously-set message properties

* `boolean getBooleanProperty(String name)`
* `byte getByteProperty(String name)`
* `double getDoubleProperty(String name)`
* `gloat getFloatProperty(String name)`
* `int getIntProperty(String name)`
* `long getLongProperty(String name)`
* `Object getObjectProperty(String name)`
* `short getShortProperty(String name)`
* `String getStringProperty(String name)`

Other methods to manage any previously-set message properties (included for consistency with `Message`

* `JMSProducer clearProperties()`
* `Enumeration  getPropertyNames()`
* `boolean propertyExists(String name)`

Methods to allow message headers of any messages subsequently sent. These will override any message header that is already set on the specified message.

* `JMSProducer setJMSCorrelationID(String correlationID correlationID)`
* `JMSProducer setJMSCorrelationIDAsBytes(byte[] correlationID)`
* `JMSProducer setJMSType(String type)`
* `JMSProducer setJMSReplyTo(Destination replyTo)`

Methods to query any previously-set message headers 

* `String getJMSCorrelationID()`
* `byte[] getJMSCorrelationIDAsBytes()`
* `String getJMSType()`
* `String getJMSReplyTo()`

### Other changes

We will drop the proposal to have an `@AutoStart` annotation.
