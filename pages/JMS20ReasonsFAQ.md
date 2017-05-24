# JMS 2.0 design FAQ</h1>

This page records some of the design decisions taken during the development of the JMS 2.0 specification,

__TOC__

==Asynchronous send==

===Why is asynchronous send not permitted in a Java EE web or EJB container?===

There are a number of reasons why the use of asynchronous send is not permitted in a Java EE web or EJB container.

**Restriction on asynchronous callbacks in Java EE**

One reason is that performing an async send registers a callback object which is invoked in a separate thread. Performing a callback in a separate thread is something that the Java EE and EJB specifications have historically not allowed. For example the  <tt>MessageConsumer</tt> method <tt>setMessageListener</tt>, which registers a <tt>MessageListener</tt> whose <tt>onMessage</tt> method is called when a message is delivered, has long been prohibited in a Java EE web or EJB container. 

However it has been suggested that this is an unnecessary restriction which it may be possible to remove in a future version of Java EE. One of the issues that would need to be resolved is what the transactional context is within a <tt>CompletionListener</tt> callback.

**Difficulty in implementing in XA transactions**

Another reason is that performing an asynchronous send in a JTA (XA) transaction raises some additional issues which could not be addressed in the time available for JMS 2.0. 

JMS 2.0 states the following for an async send performed in a Java SE local transaction:

:If the session is transacted (uses a local transaction) then when the commit or rollback method is called the JMS provider must block until any incomplete send operations have been completed and all CompletionListener callbacks have returned before performing the commit or rollback.

This requirement is needed because the transaction cannot be committed until all sends performed in the transaction have been completed. Since the <tt>send</tt> and the <tt>commit</tt> are performed in the same thread, and on the same Session object, this is relatively straightforward to implement in the client. 

However if the transaction is XA, things are more complicated. The requirement to block until incomplete send operations have been completed should probably be imposed on the  <tt>prepare</tt> or one-phase <tt>commit</tt> methods on <tt>XAResource</tt>. However the call to <tt>send</tt> and the subsequent calls to <tt>prepare</tt>/<tt>commit</tt> may theoretically take place in different threads, at least according to the XA specification. 

Possibly more importantly,  if the JMS provider implements its transactions in a remote server,  the client-side code implementing the  <tt>prepare</tt> or <tt>commit</tt>  may have no way of connecting the  <tt>prepare</tt> or <tt>commit</tt> call with any preceding <tt>send()</tt> operations. This is because a   <tt>prepare</tt> or <tt>commit</tt>  may be performed on any <tt>XAResource</tt> object, not necessary the one associated with the session used to perform the asynchronous send. And since they may occur in separate threads we can't link them using a <tt>ThreadLocal</tt>. 

It was not possible to resolve this issue  in the time available for JMS 2.0, though it may be possible to do so in a future version of JMS. 

One possible solution might be to require the call to the <tt>XAResource</tt> <tt>end</tt> method (rather than the <tt>prepare</tt> or <tt>commit</tt> methods) to block until any incomplete sends have been completed.  It may be possible to require this only if the <tt>TMSUCCESS</tt> flag was set (rather than the <tt>TMSUSPEND</tt> flag) - more work is needed. 

==Simplified API==

===Why do we need a separate JMSConsumer object. Why can't we move all its methods onto the JMSContext?===

When JMS 2.0 was being developed, early versions of the simplified API didn't have a separate consumer object. Instead, all the methods related to consuming messages 
were added to the <tt>JMSContext</tt> itself.

However without a separate consumer object, the API to receive messages gets very complicated, especially for asynchronous consumers.  For example, how do you configure multiple consumers with different message selectors, or remove just one of several message listeners? Without a token or object to represent a particular consumer it all just got too complicated. 

One possibility would have been to reduce the functionality of the simplified API to make it easier to put all the functionality on a single object. However one of the goals of the simplified API was to support ''all'' the functionality currently offered in the classic API (so that it could completely supersede it), so this was not an option.

It was slightly easier to provide all the methods on the <tt>JMSConsumer</tt> for synchronous consumers, and at one stage in the development of the simplified API we had a separate object for asynchronous consumption only, with the methods for synchronous consumption on the <tt>JMSContext</tt> itself. However we felt that this was itself rather inconsistent and confusing.

Apart from API simplicity, a second reason for keeping a separate consumer object was to allow it to have a separate lifecycle from the JMSContext. Some existing JMS vendors "pre-send" messages to the consuming client and cache them in the <tt>MessageConsumer</tt> prior to delivery to the application. This means that if the consumer is closed these cached messages can be released back to the server without closing the connection and session. Although JMS does not specify this as a required behaviour it was thought preferable to design the simplified API in a way which would not cause difficulties for existing implementations. For this reason, the simplified API uses a separate consumer object, the <tt>JMSConsumer</tt>, which is very similar to the existing <tt>MessageConsumer</tt> object.

===Why do we need a separate JMSProducer object? Why can't we move all its methods onto the JMSContext?===

When JMS 2.0 was being developed, early versions of the simplified API didn't have a separate producer object. Instead, the <tt>JMSContext</tt> had a number of <tt>send</tt> methods for sending a message, all of which took the queue or topic as a parameter. In addition  the <tt>JMSContext</tt> had the same methods for setting delivery options as <tt>MessageProducer</tt>, namely  <tt>setDeliveryMode</tt>, <tt>set Priority</tt>, <tt>setDisableMessageID</tt>, <tt> 	setDisableMessageTimestamp</tt>, <tt>setTimeToLive</tt> and the new <tt>setDeliveryDelay</tt>.

Initially this design seemed to work well. Essentially, a <tt>JMSContext</tt> could be thought of as wrapping an anonymous <tt>MessageProducer</tt> (one with no queue or topic associated with it). It allowed the code for sending it to be something like this:

 /*
  * original simplfied API for sending a message  
  */
 JMSContext context = connectionFactory.createContext();
 context.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 TextMessage textMessage = context.createTextMessage("Hello world");
 context.send(queue,textMessage);

However when we added support for injection of <tt>JMSContext</tt> objects a problem arose.

Like any CDI bean (an object injected using CDI), an injected <tt>JMSContext</tt> needs to have a ''scope''. A scope is needed so that the container knows when to "dispose" (close) the injected <tt>JMSContext</tt>. It was decided to follow the example of an injected <tt>EntityManager</tt> in JPA and give it "transaction scope". This means that the injected <tt>JMSContext</tt> will be closed by the container at the end of the transaction.

In addition, we decided to support the case when the injected <tt>JMSContext</tt> was used without a transaction. This is not supported for injected <tt>EntityManager</tt> objects but is an important use case for JMS. We decided that if the injected <tt>JMSContext</tt> was used when there was no JTA transaction then it would have "request scope". This means that the injected <tt>JMSContext</tt> will be closed by the container at the end of the "request" (where "request" has a broader meaning in CDI than simply a HTTP request).

So far, so good. But injecting a CDI bean with a specified scope has an additional consequence. This is that if you inject the CDI bean into two separate EJBs or servlets using the same annotations ("qualifiers" in CDI parlance), and these two objects are used within the same scope, then the ''same'' instance of the object should be used. This means that if one bean called another within the same JTA transaction, and both beans injected a <tt>JMSContext</tt> using the same annotations, then the two beans would actually get the same <tt>JMSContext</tt> object.   

Now this reuse of <tt>JMSContext</tt> objects has some nice consequences. In particular, it reduces the number of <tt>JMSContext</tt> objects needed, and hence the number of JMS connections used by the application. 

However this reuse of <tt>JMSContext</tt> objects is also potentially confusing to the developer. Imagine that a developer creates two EJBs, EJB1 and EJB2. 

EJB1 has an injected <tt>JMSContext</tt> which it uses to send a non-persistent message. It then calls EJB2 which sends a persistent message. Here's EJB1:

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

and here is EJB2:

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

So you might expect that if you call the <tt>EJB1</tt> method  <tt>send1</tt>, this would send a non-persistent message and then calls the <tt>EJB2</tt> method <tt>send2</tt>, which sends a persistent message. 

However this would be wrong. The field <tt>context1</tt> in <tt>EJB1</tt> and the field <tt>context2</tt> in <tt>EJB2</tt> are injected using the same annotations and are executed within the same container-managed transaction. This means that they actually refer to the same object. So when the first bean calls <tt>context1.setDeliveryMode(DeliveryMode.NON_PERSISTENT)</tt> this has the unexpected result of causing the message sent by the second bean to be non-persistent as well. However in the normal Java language these are completely separate fields. 

It was decided that this was potentially confusing and a possible cause of errors, especially if the two beans were developed by different people.

We decided to solve this problem by making the  <tt>JMSContext</tt> ''stateless'' by removing all the methods which could be used to change its behaviour. This meant removing the methods   <tt>setDeliveryMode</tt>, <tt>set Priority</tt>, <tt>setDisableMessageID</tt>, <tt> 	setDisableMessageTimestamp</tt>, <tt>setTimeToLive</tt> and <tt>setDeliveryDelay</tt> and moving these onto a separate object. This is the <tt>JMSProducer</tt>, which is a perfectly normal Java object. Instances of <tt>JMSProducer</tt> would never be reused unexpectedly.

Moving these "state" methods to <tt>JMSProducer</tt> means that we had to move <tt>send</tt> there as well.

To lessen the inconvenience of managing this extra object, we designed it so that all the methods which changed its state returned the <tt>JMSProducer</tt> as the return value. This allows these methods to be chained together in a "builder pattern". 

For the same reason we declared that a <tt>JMSProducer</tt> was intended to be a lightweight object which simply held a set of message delivery options, which could be created feely, and which did not need a <tt>close</tt> method.  We envisaged that the <tt>JMSContext</tt> would continue to hold an anonymous <tt>MessageProducer</tt>. The <tt>JMSProducer</tt> <tt>send</tt> method would be implemented to copy the various settings of the <tt>JMSProducer</tt> to the <tt>JMSContext</tt>'s anonymous <tt>MessageProducer</tt> and then use it to send the message. It would then reset the  <tt>JMSContext</tt>'s anonymous <tt>MessageProducer</tt> to its original state ready for the next message.

The fact that the   <tt>JMSProducer</tt> was lightweight, never needed closing, and supported a builder pattern means that in most cases it's never necessary to ever save it in a field. This means that the code to send a non-persistent message actually turns out to be very simple, despite using an extra object:

    public void send1() throws Exception{
       // use actual simplified API
       TextMessage textMessage = context1.createTextMessage("Message from EJB1");
       context1.createProducer().setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(queue,textMessage);
 
       ...
    }

Having gone this far in allowing the <tt>JMSProducer</tt> to be used in a builder pattern, we extended it to allow message headers and properties to be set using builder methods, This means that in most cases it isn't necessary to create a message object at all. Instead, you can call methods on <tt>JMSProducer</tt> to set delivery options, message headers and message properties and then simply pass the message body into the <tt>send</tt> method.

    public void send1() throws Exception{
       // use actual simplified API
       context1.createProducer().setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(queue,"Message from EJB1);
 
       ...
    }

===Why don't we allow client-acknowledgement or local transactions on an injected JMSContext?===

The section 
<a href="https://java.net/projects/jms-spec/pages/JMS20ReasonsFAQ#Why_do_we_need_a_separate_JMSProducer_object?_Why_can_t_we_move_all_its_methods_onto_the_JMSContext?">Why do we need a separate JMSProducer object?</a>
above explained that an injected <tt>JMSContext</tt> needs to be ''stateless'' to avoid users getting confused (or worse) by the same <tt>JMSContext</tt> object being used in multiple components within the same scope. 

Imagine a user creates two EJBs (or other Java EE components such as a servlet or CDI bean) and injects a  <tt>JMSContext</tt>  into each, using the same annotations. If the two EJBs are used within the same transaction, or, if there is no transaction, within the same request, then the two injected  <tt>JMSContext</tt> fields will (in accordance with expected CDI behaviour) refer to the same object. This means that calling a method such as <tt>acknowledge()</tt> or <tt>commit()</tt> on one   <tt>JMSContext</tt> field would have an effect on the other   <tt>JMSContext</tt> field. It was decided that this was potentially confusing and a possible cause of errors, especially if the two EJBs were developed by different people. To avoid this confusion it was decided to simply disallow the use of client-acknowledgement or local transactions on an injected <tt>JMSContext</tt>.

Client-acknowledgement and local transactions are not allowed in a EJB or web application anyway (this is defined in EJB 3.1 and is not new). Restricting their use on an injected <tt>JMSContext</tt> does not therefore actually introduce any new limitations. However if the EJB specification was relaxed in the future to allow client-acknowledgement and local transactions for JMS it might still be necessary to restrict their use when the JMSContext was injected.
