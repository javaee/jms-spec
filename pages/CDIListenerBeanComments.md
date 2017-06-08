# JMS Listener beans: summary of comments
{: .no_toc}

This page lists and discusses some of the comments made on the proposals for JMS listener beans  [/jms-spec/pages/CDIBeansAsJMSListeners JMS listener beans].

## Contents
{: .no_toc}

* auto-gen TOC:
{:toc}

## Creating listener beans automatically 

### Comment

_Is there a way to avoid the application having to inject and instantiate the listener bean?_

### Summary of issue

The JMS listener bean cannot start listening for messages until it has been created, and needs to be created just like any other CDI managed bean. The section on  [Listener lifecycles](/jms-spec/pages/CDIBeansAsJMSListeners#listener-lifecycles) describes some of ways a JMS listener bean could be created. They typically involve injecting the listener bean into some other bean and, if the bean is normal-scoped, calling a method (such as `toString()`) on it after the start of the scope. This contrasts with MDBs which can simply be defined without the need for the application to do anything to create them.

### Discussion

#### `@Initialized`

If the bean is normal scoped then CDI does provide a way to automatically create the listener bean when a new scope starts.  This involves adding a method to the listener bean which observes CDI events which have the qualifier `@Initialized(X.class)`, where `X.class` is the class of the scope.  This event is fired whenever a new scope begins. CDI defines that whenever an event is fired, if an instance of the observer class does not already exist (in that scope) then one will be automatically created. This means that if the JMS listener bean observes events with the qualifier, say, `@Initialized(RequestScoped.class)` then an instance of the bean will be created whenever a new request scope begins.

So in the following example, whenever a new request scope is created  an instance of `MyListenerBean` will be automatically created and will start listening for JMS messages. When the request scope ends the instance will stop listening for messages and will be destroyed.

```
@RequestScoped
public class MyListenerBean{
 
  public void myInit(@Observes @Initialized(RequestScoped.class) Object event) {
    // no need to do anything
  }
 
  @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
  public void deliver(Message message) {
    ...
  }
}
```
However since there are a lot of different cases which create a new request scope. The application may only be interested in listening for JMS messages whilst processing a HTTP request to a servlet. However the bean above would also be created whenever a session bean is invoked or a MDB receives a message, which may not be what the application wants. So this mechanism is unlikely to be useful with request scoped listeners.

Probably the most useful use of this mechanism would be for the `@ApplicationScoped` scope. In the following example, whenever the application is started a new application scope is created. This causes an instance of `MyListenerBean` will be automatically created and will start listening for JMS messages. The bean will then continue to listen for messages until the application is shut down. This gives a lifecycle similar to that of a MDB:
```
@ApplicationScoped
public class MyListenerBean{
 
  public void myInit(@Observes @Initialized(ApplicationScoped.class) Object event) {
    // no need to do anything
  }
 
  @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
  public void deliver(Message message) {
    ...
  }
}
```
This mechanism only works for scopes that fire the `@Initialized` event. CDI 1.2 section 6.7 specifies that these events must be fired for `@RequestScoped`, `@SessionScoped`, `@ApplicationScoped` and `@ConversationScoped`. For user-defined scopes firing these events is "encouraged" but nor mandatory. For scopes defined in other specifications (JSF, JTA etc)  it would depend on those specs. 

#### `@Eager`

There's a utility library called [Omnifaces](http://showcase.omnifaces.org/), intended for JSF applications. This includes a number of convenient CDI extensions. One of these is an annotation [http://showcase.omnifaces.org/cdi/Eager `@Eager`] which can be added to any managed bean of scope `@RequestScoped`, `@ViewScoped`, `@SessionScoped` or `@ApplicationScoped` and which will cause the bean to be "instantiated automatically at the start of each such scope instead of on demand when a bean is first referenced." 

This is effectively a shorter alternative to listening for `@Initialize` events, with some additional features for `@RequestScoped` and `@ViewScoped` to allow the application to specifty which particular request URI/view it applies to.

One possibility would be for JMS to define a similar annotation. Another is for CDI to adopt it in JMS 2.0. This is proposed in [https://issues.jboss.org/browse/CDI-473 CDI-473] but no decision has been made yet to do so.

## Runtime customisation 

### Comment

_Is there a way for the queue, message selector etc to be specified at runtime rather than hardcoded as an annotation?_

### Summary of issue

The current proposal is that the listener bean class specifies the destination, connection factory, destination type, acknowledge mode, subscription durability, clientId, subscription name and message selector using annotations, which means these are set at compile time.

It would be desirable to allow these to be specified at runtime, for each listener bean instance separately.

### Discussion

#### Customisation using bean callbacks

[One suggestion](https://java.net/projects/jms-spec/lists/users/archive/2015-08/message/23) is to allow the listener bean itself to have callbacks which return these values:
```
@SessionScoped
public class MyCDIBean21 {
  
  @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
  @JMSConnectionFactory("java:global/MyCF")
  @MessageSelector("ticker='ORCL'")
  public void processNewsItem(String newsItem) {
    ...
  }
 
  @GetMessageSelector
  public void returnMessageSelector(){
    // some logic to work out message selector
    return ... 
  }
}
```
These callbacks would be called during the bean's `postConstruct` phase, after the bean has been created but before the consumer is created.  This could be done either

* by the portable extension prior to calling `ResourceAdapter#endpointActivation`, which would convert them to activation properties passed into `ResourceAdapter#endpointActivation`. 
* or by `ResourceAdapter#endpointActivation` itself, but in this case the JCA API would need to be changed to allow the bean instance to be passed in.

If the bean had multiple multiple callbacks then each callback might have different destinations, message selectors, etc. To handle that the callback may need to have a parameter which specifies method the call applies to:
```
@GetMessageSelector
public void returnMessageSelectors(Method m){
  // some logic to work out message selector for the specified method
  return ...
}
```
It would be simpler overall if we allowed these beans to define just a single callback method. (The same issue applies to the new-style MDBs).

#### Customisation using EL expressions

[Another proposal](https://java.net/projects/jms-spec/lists/users/archive/2015-08/message/45) is to allow parameters to the various annotations to be specified using the Java Unified Expression Language (EL). 

In this the application would define a bean class with a callback method that returns the required value, and use `@Named` to give the bean a name
```
import javax.inject.Named;
 
@Named
public class SelectorProvider {
 
  public String tickerSelector() {
    return "ticker='ORCL'";
  }
 
}
```
The JMS listener bean would then refer to this bean by name using EL syntax:
```
@MessageSelector("method=#{selectorProvider.tickerSelector}")
```
A suggestion for implementing this is [here](https://java.net/projects/jms-spec/lists/users/archive/2015-09/message/1).

#### Customisation by the injecting code

The proposal above for [Customisation using bean callbacks](#customisation-using-bean-callbacks) allows the listen bean itself to decide what values should be used for destination lookup,  message selector, and so on. However this  doesn't provide a way for the application which is injecting it to decide the message selector. This is probably the main requirement.

CDI provides a way to programmatically obtain an instance of the listener bean. This is described in the proposals [here](/jms-spec/pages/CDIBeansAsJMSListeners#jms-listener-bean-with-dependent-scope-and-explicit-lifecycle-management).
```
Inject Instance&lt;MyCDIBean21 &gt; listenerProvider;
MyCDIBean21 jmsListener1 = listenerProvider.get();
```
However since the consumer is created during the bean's @postCreate stage then we need a way for the application to specify the message selector etc before we actually create the bean. CDI allows qualifiers to be specified before calling get(), but these annotations are not qualifiers. Ideas welcome.

## Listening to temporary queues and topics 

### Comment

_It might be desirable to allow JMS listener beans to listen to temporary queues and topics._

### Discussion

One possible way to achieve this would be to specify that the bean should create the temporary queue or topic when it is initialised, perhaps by replacing the `@JMSListener` annotation with a new `@TemporaryQueueListener ` annotation:
```
@SessionScoped
public class MyCDIBean21 {
  
  @TemporaryQueueListener
  @JMSConnectionFactory("java:global/MyCF")
  @MessageSelector("ticker='ORCL'")
  public void processNewsItem(String newsItem) {
    ...
  }
...
```
Having created the bean, the application would then need some way to obtain the `TemporaryQueue` or `TemporaryTopic` on which the bean was listening.

We could allow the application to inject the temporary queue or topic directly into the listener bean
```
@SessionScoped
public class MyCDIBean21 {
 
  // Inject the temporary queue this bean is listening to
  @Inject @TemporaryQueue TemporaryQueue tempQueue;
 
  @TemporaryQueueListener
  @JMSConnectionFactory("java:global/MyCF")
  @MessageSelector("ticker='ORCL'")
  public void processNewsItem(String newsItem) {
    ...
  }
 
  public TemporaryQueue getTempQueue(){
    return tempQueue;
  }
 
}
```
The application could create the listener bean, call the `getTempQueue()` getter method to find out the temporary destination, and then either send messages to it or pass it in the `replyTo` header of a request message. 

Note that if we allow JMS listener beans to specify more than one callback method then it might be configured to listen on more than one temporary destination. The syntax for injecting the temporary destination would need to be extended to allow the callback method to be specified. (This is another argument against allowing multiple callback methods).

## Handling failures 

### Comment

_It's important that my application deploys even if the JMS provider isn't fully available yet_

### Summary of issue

Let's start by thinking about MDBs. Imagine that you deploy a MDB but there's a problem looking up the JMS connection factory or destination, or in connecting to the JMS server. The MDB can't start running as the application requires. What happens?

In practice, an application server (or the resource adapter or JMS client it uses internally) does one of the following:

* It throws an exception to the container and causes deployment to fail
* It blocks deployment until the resources can be obtained and the connection can be established, throwing an exception when a timeout is reached
* It allows deployment to complete, even though the MDB has not been initialised, and then retries in the background.

The EJB specification expects either (1) or (2) to occur (they're really the same thing: (2) is effectively (1) with a longer timeout). However in many cases option (3) is often the most helpful to users, despite this not being required by the specification (or even being permitted by it). This is particularly the case if the initial failure was due to a temporary timing issue such as the JNDI provider or JMS server simply taking longer to start than the MDB. 

Now let's consider JMS listener beans. Whereas a MDB is only initialised when the application is deployed, a JMS listener bean can be created at any point in the life of the application. We don't have the option of failing deployment if an error is encountered when initialising the bean. This means that option (3) becomes even more important, as it avoids exceptions being thrown by a running application which the application either cannot catch (because they are thrown directly to the container) or cannot handle sensibly because they are thrown as the unexpected side-effect of the application doing something else.

### Discussion

With JMS listener beans, it seems reasonable that if the bean cannot be initialised then the default behaviour should be options(1) or (2), which is to throw an exception in whatever thread was directly or indirectly attempting to create the listener bean. Exactly how long the initialisation blocks for whilst attempting to perform initialisation should be left to the application server, resource adapter or JMS provider to define.

However the application should be able to specify an alternative behaviour using, say, an `@ASyncInit` annotation on the bean. This specifies that if the bean cannot be initialised then the application server should create the bean anyway without throwing an exception. In this case the application server should perform retries in the background. 
```
@AsyncInit
@SessionScoped
public class MyCDIBean21 {
  
  @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
  @JMSConnectionFactory("java:global/MyCF")
  @MessageSelector("ticker='ORCL'")
  public void processNewsItem(String newsItem) {
    ...
  }

}
```
It is probably desirable to provide a way for the application server to notify the application that there has been an initialisation error. This could be done using a special, optional, callback on the listener bean. This callback could also be used to notify that a listener bean which had previously been successfully initialised had stopped working for some reason such as connection to the JMS server having been lost:
```
@OnError
public void onError(Exception e) {
  ...
}
```
This might be accompanied by a callback to specify that the error previously reported had now been cleared (e.g. connection to the JMS server had been re-established), and the listener bean was now working correctly.
```
@onSuccess
public void onSuccess() {
  ...
}
```
## What scopes are active when the callback method is invoked? 

### Comment

_What scopes are active when the callback method is invoked?_

_Additionally, @RequestScoped is kinda assumed to be an "@ThreadScoped" thing, e.g. there's the expectation that only the current thread will
access it. If the JMS provider will asynchronously call a method on the bean instance from another thread, then this breaks this assumption._

### Discussion

When a message is delivered to a MDB there is an active request scope. This request scope is created when the MDB's callback method is invoked and destroyed when it returns. The application scope will also be active, and the transaction scope will be active if there's a current transaction.

It would seem reasonable to apply the same rule when a message is delivered to the callback method of a JMS listener bean. When a message is delivered to a JMS listener bean there is an active request scope, which will be created when the callback is invoked and destroyed when it returns. The application scope will also be active, and the transaction scope will be active if there's a current transaction.

Unlike MDBs, JMS listener beans may have business methods just like any other CDI managed bean. When these methods are called, whether or not there is an active request scope will depend on the calling thread, and will be unrelated to any request scope created when a message is delivered. 

This may have some unexpected consequences if the listener bean has normal-scoped fields:
```
public class MyCDIBean21 {
 
  @Inject  private MyApplicationScopedBean appScopedBean; 
  @Inject  private MyRequestScopedBean requestScopedBean;
  @Inject  private MyConversationScopedBean convScopedBean;
 
  @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC)
  @JMSConnectionFactory("java:global/MyCF")
  @MessageSelector("ticker='ORCL'")
  public void processNewsItem(String newsItem) {
    // will see the same instance of MyApplicationScopedBean as businessMethod
    // will see a different instance of MyRequestScopedBean than businessMethod
    // will never see an instance of MyConversationScopedBean
    // since never called in an active conversation scope
    ... 
  }
 
  public void businessMethod(String param) {
    // will see the same instance of MyApplicationScopedBean as businessMethod
    // will always see a different instance of MyRequestScopedBean than callback method
    // will only see an instance of MyConversationScopedBean 
    // if called in an active conversation scope
    ...
  }  
 
}
```
* If the listener bean injects an application-scoped bean into a field then both message callbacks and business methods will see the **same** bean instance.

* If the listener bean injects a request-scoped bean into a field then all calls to business methods within a particular request will see the same bean instance. However message callbacks will never see that bean instance and will always see a **different** bean instance. 

* If the listener bean injects a transaction-scoped bean into a field then all calls to business methods within the same transaction will see the same bean instance. However message callbacks will never see that bean instance. If the callback is using a transaction then it will always see a **different** bean instance.  If the callback is not using a transaction then any attempt to access the injected bean will cause a "no active context" error.  

* If the listener bean injects a bean of some other normal scope then the scope may be active when the business method is invoked but not be active when the message callback is invoked. In this the business method could access the injected bean but any attempt by the callback method to do so will cause a "no active context" error.

It also might cause surprising behaviour if the listener bean is request scoped, since request scope essentially means thread scope:
```
@RequestScoped
public class MyCDIBean21 {
 
  // for a given bean instance, 
  // businessMethod1 and businessMethod2 will never be called at the same time from different threads
  // however processNewsItem may be called at the same time as a call to one of the business methods
  
  @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC)
  @JMSConnectionFactory("java:global/MyCF")
  @MessageSelector("ticker='ORCL'")
  public void processNewsItem(String newsItem) {
    ...
  }
 
  public void businessMethod1(String param) {
   ...
  }  
 
  public void businessMethod2(String param) {
   ...
  }  
 
}
```

* With ordinary CDI managed beans each application thread "sees" a different instance of the bean. This has the effect of causing all accesses to a given bean instance via business methods to take place in the same thread. 

* With JMS listener beans the same applies to business methods: all accesses to a given bean instance via business methods will take place in the same thread. However callback method will always be called from a different thread to that, and may take place at the same time as a business method is being called. The listener bean needs to be designed with this in mind.

## Are CDI events better? 

### Comment

_I think we should leverage the existing CDI event/observer functionality instead of introducing a completely new delivery mechanism_

### Summary of issue

CDI allows any CDI managed bean to define a callback method which is called whenever a CDI event of the specified type is "fired":
```
public void afterLogin(@Observes LoggedInEvent event) { ... }
```
Qualifiers may be used to specify that the callback only wants to receive a subset of events: 
```
public void afterAdminLogin(@Observes @Role("admin") LoggedInEvent event) { ... }
```
JMS messages are rather like CDI events. Can we extend the CDI observer mechanism to allow JMS messages to be received?

### Discussion

This is an appealing idea. However there are some significant differences between the CDI event model and  the JMS message model which make it unclear how this would work in practice. 

Here's a summary of some of the differences between the way that CDI delivers events to observers and the way that JMS delivers messages to asynchronous consumers.

* CDI events are _pub-sub_ in the sense that if two different observer bean classes are configured to receive events of the same type, then when the event is fired an instance of each class will be created or obtained and the same event delivered to each. This corresponds approximately with JMS topics. <br><br>However CDI does not offer an equivalent to "point-to-point" as used by JMS queues, in which each message is delivered to a single recipient.

* If a CDI event is delivered to more than one observer bean then in CDI 1.2 (the current version) each observer is invoked in turn from a the same thread that is firing the event. This not only ties the firer and observer to the same thread; it also means a particular observer will not receive the event until the previous observers have processed the same event. This differs from JMS in which multiple listeners on the same topic may receive the same message at the same time, using a completely unrelated thread to that which originally sent the message to the queue or topic.

* CDI assumes that the firer of an event is running in the same CDI container (i.e. JVM) as the observer of that event. This makes it possible to create instances of the observer class automatically when an event is fired.

  * If the observer has dependent scope then a new instance of the observer class is created for every single elegible event. There is no way to disable this behaviour and re-use an existing instance of the observer bean. This also means that there is no way to start and stop delivery of events.

  * If the observer has normal scope then, by default, a new instance of the bean will be created for every elegible event if one does not already exist for that scope. Once created, the same bean instance will be used to receive events within the same scope.  However this means that there is no way to start and stop delivery of events for a given scope since if there is no bean in existence when an event is fired then one will always be automatically created.

  * If the observer specifies `@Observes(notifyObserver=Reception.IF_EXISTS)` then if there is no instance of the bean in existence for a given scope an instance is _not_ created and the event is _not_ delivered. to that scope. This allows the observing code to control whether events are received within a given scope. 
  
  * It is not possible to create instances of the observer class automatically when receiving JMS messages. This is because the listener code must explicitly subscribe to the queue or topic before any messages will arrive in the JVM. This extra step of creating a subscription has no analogue in CDI. Qualifiers on the <tt>Observes</tt> annotation simply define a subset of events that the observer will receive. This is not the same as creating a subscription on a queue or topic.

* CDI events are always delivered as a single object. Any Java type may be an event, so there is no reason why events of type `javax.jms.Message` could not be used.<br><br>However since CDI may deliver the same event object to multiple observers the application needs to be aware that JMS messages are not designed for concurrent access from multiple threads. <br><br>Even if the message is not used concurrently from multiple threads there would still be the possibility that two unrelated observer beans would receive the same message. For messages of type `BytesMessage` and `StreamMessage`, reading a message will change the current read position. This means that each observer bean would need to be written to take account that some other bean may have changed the message's current read position. <br><br>This is less of an issue with JMS since which each consumer receives a separate instance of `javax.jms.Message`, even if they represent the same sent message.

* CDI observer callbacks simply pass the event object. There is no equivalent to the proposed JMS listener bean callbacks which allow the listener method to specify that one argument should be the message body, another argument should be a specified message property, and so on:
```
void processTrade(TextMessage messageText, @MessageProperty("price") long price,){
  ...
} 
```
* If a CDI observer has normal scope, then it will only receive events if the event is fired from within the same scope. It is not possible to configure a normal-scoped bean to receive messages that were fired in some other scope. This imposes a dependency between firer and observer that does not exist in JMS. It means that the only way for an observer to receive events sent from any other part of the application is for the observer to be dependent scope. However if the observer is dependent scoped then a new instance will always be created for every event. There is no way to define an observer with a limited lifetime (e.g. for the duration of a request or session) which can receive events from anywhere.

What all this means is that there's more to receiving JMS messages as events than simply defining some special qualifiers that allow an `Observes` method to specify a JMS queue or topic. 