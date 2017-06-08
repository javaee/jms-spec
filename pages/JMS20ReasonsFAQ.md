# JMS 2.0 design FAQ
{: .no_toc}

This page records some of the design decisions taken during the development of the JMS 2.0 specification,

## Contents
{: .no_toc}

* auto-gen TOC:
{:toc}

## Asynchronous send 

### Why is asynchronous send not permitted in a Java EE web or EJB container?

There are a number of reasons why the use of asynchronous send is not permitted in a Java EE web or EJB container.

**Restriction on asynchronous callbacks in Java EE**

One reason is that performing an async send registers a callback object which is invoked in a separate thread. Performing a callback in a separate thread is something that the Java EE and EJB specifications have historically not allowed. For example the  `MessageConsumer` method `setMessageListener`, which registers a `MessageListener` whose `onMessage` method is called when a message is delivered, has long been prohibited in a Java EE web or EJB container. 

However it has been suggested that this is an unnecessary restriction which it may be possible to remove in a future version of Java EE. One of the issues that would need to be resolved is what the transactional context is within a `CompletionListener` callback.

**Difficulty in implementing in XA transactions**

Another reason is that performing an asynchronous send in a JTA (XA) transaction raises some additional issues which could not be addressed in the time available for JMS 2.0. 

JMS 2.0 states the following for an async send performed in a Java SE local transaction:

:If the session is transacted (uses a local transaction) then when the commit or rollback method is called the JMS provider must block until any incomplete send operations have been completed and all CompletionListener callbacks have returned before performing the commit or rollback.

This requirement is needed because the transaction cannot be committed until all sends performed in the transaction have been completed. Since the `send` and the `commit` are performed in the same thread, and on the same Session object, this is relatively straightforward to implement in the client. 

However if the transaction is XA, things are more complicated. The requirement to block until incomplete send operations have been completed should probably be imposed on the  `prepare` or one-phase `commit` methods on `XAResource`. However the call to `send` and the subsequent calls to `prepare`/`commit` may theoretically take place in different threads, at least according to the XA specification. 

Possibly more importantly,  if the JMS provider implements its transactions in a remote server,  the client-side code implementing the  `prepare` or `commit`  may have no way of connecting the  `prepare` or `commit` call with any preceding `send()` operations. This is because a   `prepare` or `commit`  may be performed on any `XAResource` object, not necessary the one associated with the session used to perform the asynchronous send. And since they may occur in separate threads we can't link them using a `ThreadLocal`. 

It was not possible to resolve this issue  in the time available for JMS 2.0, though it may be possible to do so in a future version of JMS. 

One possible solution might be to require the call to the `XAResource` `end` method (rather than the `prepare` or `commit` methods) to block until any incomplete sends have been completed.  It may be possible to require this only if the `TMSUCCESS` flag was set (rather than the `TMSUSPEND` flag) - more work is needed. 

## Simplified API 

### Why do we need a separate JMSConsumer object. Why can't we move all its methods onto the JMSContext?

When JMS 2.0 was being developed, early versions of the simplified API didn't have a separate consumer object. Instead, all the methods related to consuming messages 
were added to the `JMSContext` itself.

However without a separate consumer object, the API to receive messages gets very complicated, especially for asynchronous consumers.  For example, how do you configure multiple consumers with different message selectors, or remove just one of several message listeners? Without a token or object to represent a particular consumer it all just got too complicated. 

One possibility would have been to reduce the functionality of the simplified API to make it easier to put all the functionality on a single object. However one of the goals of the simplified API was to support _all_ the functionality currently offered in the classic API (so that it could completely supersede it), so this was not an option.

It was slightly easier to provide all the methods on the `JMSConsumer` for synchronous consumers, and at one stage in the development of the simplified API we had a separate object for asynchronous consumption only, with the methods for synchronous consumption on the `JMSContext` itself. However we felt that this was itself rather inconsistent and confusing.

Apart from API simplicity, a second reason for keeping a separate consumer object was to allow it to have a separate lifecycle from the JMSContext. Some existing JMS vendors "pre-send" messages to the consuming client and cache them in the `MessageConsumer` prior to delivery to the application. This means that if the consumer is closed these cached messages can be released back to the server without closing the connection and session. Although JMS does not specify this as a required behaviour it was thought preferable to design the simplified API in a way which would not cause difficulties for existing implementations. For this reason, the simplified API uses a separate consumer object, the `JMSConsumer`, which is very similar to the existing `MessageConsumer` object.

### Why do we need a separate JMSProducer object? Why can't we move all its methods onto the JMSContext?

When JMS 2.0 was being developed, early versions of the simplified API didn't have a separate producer object. Instead, the `JMSContext` had a number of `send` methods for sending a message, all of which took the queue or topic as a parameter. In addition  the `JMSContext` had the same methods for setting delivery options as `MessageProducer`, namely  `setDeliveryMode`, `set Priority`, `setDisableMessageID`, ` 	setDisableMessageTimestamp`, `setTimeToLive` and the new `setDeliveryDelay`.

Initially this design seemed to work well. Essentially, a `JMSContext` could be thought of as wrapping an anonymous `MessageProducer` (one with no queue or topic associated with it). It allowed the code for sending it to be something like this:
```
 /*
  * original simplfied API for sending a message  
  */
 JMSContext context = connectionFactory.createContext();
 context.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 TextMessage textMessage = context.createTextMessage("Hello world");
 context.send(queue,textMessage);
```
However when we added support for injection of `JMSContext` objects a problem arose.

Like any CDI bean (an object injected using CDI), an injected `JMSContext` needs to have a _scope_. A scope is needed so that the container knows when to "dispose" (close) the injected `JMSContext`. It was decided to follow the example of an injected `EntityManager` in JPA and give it "transaction scope". This means that the injected `JMSContext` will be closed by the container at the end of the transaction.

In addition, we decided to support the case when the injected `JMSContext` was used without a transaction. This is not supported for injected `EntityManager` objects but is an important use case for JMS. We decided that if the injected `JMSContext` was used when there was no JTA transaction then it would have "request scope". This means that the injected `JMSContext` will be closed by the container at the end of the "request" (where "request" has a broader meaning in CDI than simply a HTTP request).

So far, so good. But injecting a CDI bean with a specified scope has an additional consequence. This is that if you inject the CDI bean into two separate EJBs or servlets using the same annotations ("qualifiers" in CDI parlance), and these two objects are used within the same scope, then the _same_ instance of the object should be used. This means that if one bean called another within the same JTA transaction, and both beans injected a `JMSContext` using the same annotations, then the two beans would actually get the same `JMSContext` object.   

Now this reuse of `JMSContext` objects has some nice consequences. In particular, it reduces the number of `JMSContext` objects needed, and hence the number of JMS connections used by the application. 

However this reuse of `JMSContext` objects is also potentially confusing to the developer. Imagine that a developer creates two EJBs, EJB1 and EJB2. 

EJB1 has an injected `JMSContext` which it uses to send a non-persistent message. It then calls EJB2 which sends a persistent message. Here's EJB1:
```
@Stateless
@LocalBean
public class EJB1 implements Serializable {
 
  @Inject JMSContext context1;
 
  @EJB EJB2 ejb2;
 
  public void send1() throws Exception{
    // use original simplified API
    context1.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    TextMessage textMessage = context1.createTextMessage("Message from EJB1");
    context1.send(queue,textMessage);
 
    // call second bean
    ejb2.send2();
  }
 } 
```
and here is EJB2:
```
@Stateless
@LocalBean
public class EJB1 implements Serializable {
 
  @Inject JMSContext context12;
 
  @EJB EJB2 ejb2;
 
  public void send2() throws Exception{
    // use original simplified API
    // there's no need to set delivery mode as the default is DeliveryMode.PERSISTENT, surely?
    TextMessage textMessage = context2.createTextMessage("Message from EJB2");
    context2.send(queue,textMessage);
  }
} 
```
So you might expect that if you call the `EJB1` method  `send1`, this would send a non-persistent message and then calls the `EJB2` method `send2`, which sends a persistent message. 

However this would be wrong. The field `context1` in `EJB1` and the field `context2` in `EJB2` are injected using the same annotations and are executed within the same container-managed transaction. This means that they actually refer to the same object. So when the first bean calls `context1.setDeliveryMode(DeliveryMode.NON_PERSISTENT)` this has the unexpected result of causing the message sent by the second bean to be non-persistent as well. However in the normal Java language these are completely separate fields. 

It was decided that this was potentially confusing and a possible cause of errors, especially if the two beans were developed by different people.

We decided to solve this problem by making the  `JMSContext` _stateless_ by removing all the methods which could be used to change its behaviour. This meant removing the methods   `setDeliveryMode`, `set Priority`, `setDisableMessageID`, `setDisableMessageTimestamp`, `setTimeToLive` and `setDeliveryDelay` and moving these onto a separate object. This is the `JMSProducer`, which is a perfectly normal Java object. Instances of `JMSProducer` would never be reused unexpectedly.

Moving these "state" methods to `JMSProducer` means that we had to move `send` there as well.

To lessen the inconvenience of managing this extra object, we designed it so that all the methods which changed its state returned the `JMSProducer` as the return value. This allows these methods to be chained together in a "builder pattern". 

For the same reason we declared that a `JMSProducer` was intended to be a lightweight object which simply held a set of message delivery options, which could be created feely, and which did not need a `close` method.  We envisaged that the `JMSContext` would continue to hold an anonymous `MessageProducer`. The `JMSProducer` `send` method would be implemented to copy the various settings of the `JMSProducer` to the `JMSContext`'s anonymous `MessageProducer` and then use it to send the message. It would then reset the  `JMSContext`'s anonymous `MessageProducer` to its original state ready for the next message.

The fact that the   `JMSProducer` was lightweight, never needed closing, and supported a builder pattern means that in most cases it's never necessary to ever save it in a field. This means that the code to send a non-persistent message actually turns out to be very simple, despite using an extra object:
```
public void send1() throws Exception{
  // use actual simplified API
  TextMessage textMessage = context1.createTextMessage("Message from EJB1");
  context1.createProducer().setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(queue,textMessage);
 
  ...
}
```
Having gone this far in allowing the `JMSProducer` to be used in a builder pattern, we extended it to allow message headers and properties to be set using builder methods, This means that in most cases it isn't necessary to create a message object at all. Instead, you can call methods on `JMSProducer` to set delivery options, message headers and message properties and then simply pass the message body into the `send` method.
```
public void send1() throws Exception{
  // use actual simplified API
  context1.createProducer().setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(queue,"Message from EJB1);

  ...
}
```
### Why don't we allow client-acknowledgement or local transactions on an injected JMSContext?

The section 
<a href="/jms-spec/pages/JMS20ReasonsFAQ#why-do-we-need-a-separate-jmsproducer-object-why-cant-we-move-all-its-methods-onto-the-jmscontext">Why do we need a separate JMSProducer object?</a>
above explained that an injected `JMSContext` needs to be _stateless_ to avoid users getting confused (or worse) by the same `JMSContext` object being used in multiple components within the same scope. 

Imagine a user creates two EJBs (or other Java EE components such as a servlet or CDI bean) and injects a  `JMSContext`  into each, using the same annotations. If the two EJBs are used within the same transaction, or, if there is no transaction, within the same request, then the two injected  `JMSContext` fields will (in accordance with expected CDI behaviour) refer to the same object. This means that calling a method such as `acknowledge()` or `commit()` on one   `JMSContext` field would have an effect on the other   `JMSContext` field. It was decided that this was potentially confusing and a possible cause of errors, especially if the two EJBs were developed by different people. To avoid this confusion it was decided to simply disallow the use of client-acknowledgement or local transactions on an injected `JMSContext`.

Client-acknowledgement and local transactions are not allowed in a EJB or web application anyway (this is defined in EJB 3.1 and is not new). Restricting their use on an injected `JMSContext` does not therefore actually introduce any new limitations. However if the EJB specification was relaxed in the future to allow client-acknowledgement and local transactions for JMS it might still be necessary to restrict their use when the JMSContext was injected.
