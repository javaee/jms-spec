# CDI Managed Beans as JMS listeners (Version 1)

This page contains proposals to allow CDI managed beans in a Java EE application to listen asynchronously for JMS messages. 

These changes are proposed for JMS 2.1 which will be part of Java EE 8. Comments are invited. See [How to get involved in JMS 2.1](/jms-spec/pages/JMS21#how-to-get-involved-in-jms-21).

For a summary of the comments made so far, see [JMS Listener beans: summary of comments](/jms-spec/pages/CDIListenerBeanComments). 

These proposals are separate to the proposals for [More flexible JMS MDBs](/jms-spec/pages/JMSListener2), though the two sets of proposals are designed to use a common set of annotations.

* auto-gen TOC:
{:toc}

## Introduction

### In Java EE 7, the only way to consume JMS messages asynchronously is to use a MDB

In the current version of Java EE the only way that an application can consume JMS messages asynchronously is to use a MDB (message-driven bean). JMS MDBs allow the container to manage a pool of MDB instances which can share the work of processing a large number of messages amongst multiple threads. This is a very useful feature allowing a large throughput of messages to be handled.

Here's an example of a MDB in Java EE 7:

```
@MessageDriven(activationConfig = {
  @ActivationConfigProperty(
    propertyName = "connectionFactoryLookup", propertyValue = "java:global/MyCF"),
  @ActivationConfigProperty(
    propertyName = "destinationLookup", propertyValue = "java:global/java:global/News"),
  @ActivationConfigProperty
    (propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
  @ActivationConfigProperty(
    propertyName = "messageSelector", propertyValue = "ticker='ORCL'")
}
public class MyMDB20 implements MessageListener {
 
  public void onMessage(Message message){
    ...
  }
}
```

### In Java EE 8, we're proposing that JMS MDBs will be more flexible

The separate proposals for [More flexible JMS MDBs](/jms-spec/pages/JMSListener2) in JMS 2.1 
will make them even simpler by removing the need for the MDB to implement the 
`javax.jms.MessageListener` and defining some new, JMS-specific, annotations.
Here's an example of how those proposals would allow a JMS MDB to look in Java EE 8: 

```
@MessageDriven
public class MyMDB21 {
 
  @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
  @JMSConnectionFactory("java:global/MyCF")
  @MessageSelector("ticker='ORCL'")
  public void processNewsItem(String newsItem) {
    ...
  }
}
```
However those proposals leave the MDB lifecycle unchanged. This means that a MDB (or pool of MDBs) will start listening for messages as soon as the application is started, and will continue to listen for messages until the application is shut down. There is no way to listen for messages for a shorter period that this, or to allow objects that are not MDBs to listen for messages.

### In Java EE 8, we're now proposing that any CDI managed bean may listen for JMS messages

To address the restrictions in the MDB lifecycle **it is now proposed to allow any CDI managed bean in a Java EE application to listen for JMS messages**. The bean will start listening for messages as soon as a bean instance is created, and it will continue to listen for messages until it is destroyed. All that will be necessary is to define a suitable callback method on the bean and add method annotations in the same way as is proposed for JMS MDBs. Here's an example of such a bean:

```
@Dependent
public class MyCDIBean21 {
 
  @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
  @JMSConnectionFactory("java:global/MyCF")
  @MessageSelector("ticker='ORCL'")
  public void processNewsItem(String newsItem) {
    ...
  }
}
```
As can be seen, this looks very similar to the example  of a JMS 2.1 MDB. This is deliberate: all the new annotations that can be used on a callback method of a JMS MDB (`@JMSListener` etc) can also be used on a CDI managed bean.  (Note also that these annotations are still being designed: comments are invited.)

However note that this object is not a MDB. It does not have the `MessageDriven` annotation. Instead it is a CDI managed bean which can have any CDI scope and can be injected into Java EE code just like any other CDI managed bean. When a CDI bean is injected, the lifecycle of the bean instance is managed by the CDI container. When the bean instance is created, if it is annotated with `JMSListener` then it will start listening for messages, and when the bean instance is destroyed (such as when its scope ends) it will stop listening for messages.

For more about the lifecycle of a CDI JMS listener bean, see [Listener lifecycles](/jms-spec/pages/CDIBeansAsJMSListeners#listener-lifecycles) below.

## JMS listener beans

This section describes JMS listener beans in more detail and how they relate to CDI managed beans in general, and to JMS MDBs.

The CDI 1.2 specification defines a managed bean in [section 3.1 "Managed beans"](http://docs.jboss.org/cdi/spec/1.2/cdi-spec.html#managed_beans). This states:

> A top-level Java class is a managed bean if it is defined to be a managed bean by any other Java EE specification, or if it meets all of the following conditions:
> * It is not a non-static inner class.
> * It is a concrete class, or is annotated @Decorator.
> * It is not annotated with an EJB component-defining annotation or declared as an EJB bean class in ejb-jar.xml.
> * It does not implement javax.enterprise.inject.spi.Extension.
> * It is not annotated @Vetoed or in a package annotated @Vetoed.
> * It has an appropriate constructor - either:
>   *  the class has a constructor with no parameters, or
>   *  the class declares a constructor annotated @Inject.
> 
> All Java classes that meet these conditions are managed beans and thus no special declaration is required to define a managed bean.

In addition, the managed bean instance must be obtained via dependency injection. This allows its lifecycle will be managed by the CDI container. The CDI container will take care of creating the instance when needed, and destroying it afterwards.  For the purposes of this proposal, the term "managed bean" refers to a managed bean instance that has been obtained via dependency injection.

It is now proposed that <b>JMS 2.1 will allow any managed bean to receive JMS messages asynchronously</b> so that whenever a message is received a specified callback method on the bean will be invoked. We will call such a managed bean a <b>JMS listener bean</b>. 

<b>This is still a managed bean</b>

* The application must obtain an instance of the JMS listener bean using dependency injection just like any other managed bean. It the application simply creates an instance directly by invoking its constructor then it will not be managed by the container and will not receive messages.

* The managed bean may have normal business methods as well as callback methods, which can be called by the application just like any other managed bean.

<b>Callback methods</b>

* A listener bean may have any number of application-defined callback methods. Each callback method will be treated as representing a separate consumer, and so may specify a different queue or topic, connection factory, durable subscription, message selector etc. 

* Each callback method must be specified using the annotation `@JMSListener`. The `@JMSListener` annotation may also be used to specify the queue or topic from where messages are to be received. Additional information about how the consumer is configured may be specified using the annotations `@JMSConnectionFactory`, `@Acknowledge`, `@SubscriptionDurability`, `@ClientId`, `@SubscriptionName` or `@MessageSelector`. These are exactly the same annotations as are proposed for improving JMS MDBs and are used in much the same way. See [the proposals for annotations to specify a callback method in a JMS MDB](jms-spec/pages/JMSListener2#Specifying_what_messages_will_be_received) for details.

* A callback method must return void and may have any number of parameters. Depending on the parameter type and any parameter annotations, each parameter will may be set to the message, the message body, a message header or a message property.  The same features will be available as are proposed for JMS MDBs. See [the proposals for application-defined callback methods in JMS MDBs](jms-spec/pages/JMSListener2#Flexible_method_signature) for details.

* Both the callback method and the `JMSMessageDrivenBean` interface may be inherited.

<b>These are similar to JMS MDBs in some ways</b>

* A bean may have multiple callback methods, as is proposed for JMS MDBs

* Only one callback method will be called at a time on a given listener instance, as is proposed for JMS MDBs

<b>But these are not MDBs</b>

* There will always be a single listener instance. This is the instance that is injected into the application. This is quite different from MDBs which are designed to allow a pool of MDB instances to share the work of processing messages.

* The application will have access to the listener instance and can invoke other methods on it. This is quite different from MDBs.

* Although the container will ensure that only one callback method will be called at a time on the listener instance, there are no restrictions on the use of ordinary business methods by multiple application threads. 

* Whilst a MDB may be configured using activation properties as well as by annotations on the callback methods, a JMS listener bean is configured only using annotations on the callabck method.

* Container-managed transactions are configured differently for JMS listener beans than for MDBs. See the next section for details.

<b>Container-managed transactions</b>

Both JMS MDBs and JMS listener beans can be configured so that each message is received from the JMS provider and delivered to the callback method in a single container-managed transaction which encompasses both the consumption of the message and the invocation of the callback method. 

In a MDB the class-level annotation `@javax.ejb.TransactionManagement(TransactionManagementType.CONTAINER)` is used to specify that a container-managed transaction must be used, with `@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)` used to specify that a container-managed transaction must not be used. If no such annotation is provided then a container-managed transaction will be used.

With JMS listener beans it is proposed to follow the existing existing mechanism for specifying whether a business method on a managed bean is executed in a container-managed transaction. This is to use the method-level annotation `@javax.transaction.Transactional(Transactional.TxType.REQUIRED)` to specify that a container-managed transaction must be used. If no such annotation is provided then a container-managed transaction will _not_ be used. Note that this default behaviour is the opposite of that of MDBs.

So in the following example, each message will be received, and the callback method invoked, without the container using a transaction:

```
 public class MYListenerBean{
 
   @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
   public void giveMeAMessage(Message message) {
     ...
   }
 }
```

However in the following example, each message will be received, and the callback method invoked, in the same container-managed transaction:

```
 public class MYListenerBean{
 
   @Transactional 
   @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
   public void giveMeAMessage(Message message) {
     ...
   }
 }
```

The `@Transactional` annotation has an optional attribute which allows the "transaction type" to be specified. This can be set to one of the six possible values. Not all are appropriate for listener beans:
* `@Transactional(Transactional.TxType.MANDATORY)`. This is not appropriate for a MDB and will be treated as an error.
* `@Transactional(Transactional.TxType.NEVER)`. A container-managed transaction will not be used.
* `@Transactional(Transactional.TxType.NOT_SUPPORTED)`. A container-managed transaction will not be used.
* `@Transactional(Transactional.TxType.REQUIRED)`. A container-managed transaction will be used. This is the default.
* `@Transactional(Transactional.TxType.REQUIRES_NEW)`. This is not appropriate for a MDB and will be treated as an error.
* `@Transactional(Transactional.TxType.SUPPORTS)`. This is not appropriate for a MDB and will be treated as an error.

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue 1:</b> Should a CDI managed bean be allowed to have multiple callback methods or should it be restricted to have just one? 
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue 2:</b> Do we need an additional annotation to allow arbitrary properties to be specifed (analogous to activation properties)? 
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue 3:</b> Do we need an additional annotation (either at class or method level) to allow a resource adapter to be specified?? 
</td></tr></table>

## Listener lifecycles

The main reason for using JMS listener beans rather than MDBs is that they give the application finer-grained control over the lifecycle of the listener: when it starts listening, and when it stops listening. The lifecycle of the listener bean is controlled by CDI and depends on its _scope_ and how it is injected into the application. 

### JMS listener bean with dependent scope
By default a CDI managed bean has _dependent scope_. This can be denoted by adding the `@Dependent` class annotation, though since this is the default it is not required. A bean with dependent scope will follow the lifecycle of whatever bean it is injected into. 

So if we define a CDI managed bean as follows:

```
 @Dependent
 public class MyDepScopeListenerBean{
 
   @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
   public void deliver(Message message) {
     ...
   }
 }
```
And inject it into, say, a Servlet as follows:
```
 @WebServlet("/myjmsservlet1")
 public class MyJMSServlet1 extends HttpServlet {
 
   @Inject MyDepScopeListenerBean myDepScopeListenerBean;
    
   public void service(ServletRequest req, ServletResponse res) throws IOException, ServletException {
      ...
   }
 }
```
Then whenever an instance of the servlet is created (by the container) an instance of `MyDepScopeListenerBean` will be automatically created which will start listening for messages from the specified. Each time a message is delivered the callback method will be invoked. When the servlet instance is destroyed (by the container) the instance will cease listening for messages.

Each instance of the servlet will have its own instance of `MyDepScopeListenerBean`, and can access its other methods directly using the `myDepScopeListenerBean` field. 

The use of a servlet here is just an example. The `MyDepScopeListenerBean` could be injected into any other class that supports CDI injection. SInce it has dependent scope it would follow the same lifecycle as the object into which it was injected.

### JMS listener bean with dependent scope and explicit lifecycle management

If required the application can control the lifecycle of a dependent-scoped JMS listener bean explicitly. In this case it must inject an `Instance` object of the required type. This will function as a factory (or "provider") of listener bean instances:
```
@Inject Instance&lt;MyDepScopeListenerBean&gt; listenerProvider;
``` 
The application then needs to call the `get` method to create an instance of the listener and start it listening for messages. It can create multiple instances of required.
```
MyDepScopeJMSListener jmsListener1 = listenerProvider.get();
``` 

Each listener will continue to listen for messages until the application explicitly destroys it by calling

```
listenerProvider.destroy(jmsListener1 );
```
 
### CDI managed listener bean with request scope

A JMS listener bean doesn't have to have dependent scope. It can have any scope supported by CDI, including scopes which are application-defined. 

For example, the `@RequestScoped` class annotation can be used to specify that the listener bean should have "request" scope. This means that each request will use a separate instance of the listener. The listener will be created, and will start listening for messages,  the first time that the injected object is used within the request. It will then continue to listen for messages until the request ends.

So if we define a CDI managed bean as follows:

```
 @RequestScoped
 public class MyReqScopeListenerBean{
 
   @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
   public void deliver(Message message) {
     ...
   }
 }
```
And inject it into, a Servlet as follows:
```
 @WebServlet("/myjmsservlet1")
 public class MyJMSServlet1 extends HttpServlet {
 
   @Inject MyReqScopeListenerBean listener;
    
   public void service(ServletRequest req, ServletResponse res) throws IOException, ServletException {
     listener.toString(); // trigger instantiation of the listener
      ...
     // listener will be destroyed when scope ends
   }
 }
```
Then each time a request is received, and the service method invoked, the call to `listener.toString()` will cause a new listener to be created and will start listening for messages. It will then continue to listen for messages until the request ends.

The call to `listener.toString()` is required because of the way that CDI works with non-dependent scopes (so-called "normal" scopes). The object that is injected, and saved in the `listener` field is just a proxy. The actual listener will only be created when a method is called.  In this case we call `listener.toString()`, but any other method supported by the bean may be called instead.

Normally an application developer does not need to be aware of this, since there is no need to instantiate an injected bean before one of its methods is called. However if the injected bean is a JMS listener, and the listener has "normal" scope. then the developer needs to be aware that they need to call a method to force it to start listening.

## How will this be implemented?

As with MDBs, it must be possible for application servers to implement JMS listener beans using a resource adapter. This imposes responsibilities on the application server and on the resource adapter which will be described in this section. 

The application server will be required to provide
* A CDI portable extension 
* Suitable implementations of `MessageEndpointFactory`
* Suitable implementations of `MessageEndpoint`

The resource adapter will be required to provide
* A suitable implementation of the `ResourceAdapter` method `endpointActivation` 
* A suitable implementation of the `ResourceAdapter` method `endpointDeactivation` 

The application server must support the use of a resource adapter if one is explicitly specified by the application. If a resource adapter is not specified then the application server must either use a default resource adapter provided by the application server or a mechanism which does not use a resource adapter.

### Responsibilities of the application server

#### CDI portable extension

The application server will provide a CDI "portable extension" which extends the behaviour of all CDI managed beans that have one or more methods annotated with `JMSListener`, as follows:

* It will extend the `postConstruct` behaviour of such beans to:
  * Determine which resource adapter to use
  * Call the `ResourceAdapter` method `endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec spec)`

* This will lookup the connection factory and destination specified in the callback annotations and use these and the other annotations to create a consumer which will deliver messages, one at a time, to the specified callback method, adding whatever transactional behaviour is specified. 

* It will extend the `postConstruct` behaviour of such beans to:
  * Call the `ResourceAdapter` method `endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec spec)`

* This will close the consumer so that no further messages are delivered to the bean, and clean up any other state created during the `postConstruct` stage.

Note that the term "portable extension" used here is CDI terminology for an extension to an application server's CDI container that interacts with the CDI container using CDI's portable extension integration SPI. Although it is expected that this SPI will be sufficient, the application server may use any API that is required.

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue 4:</b> It is currently proposed to re-use the existing JCA methods `endpointActivation` and `endpointActivation` so that they are used for both MDBs and for JMS listener beans, and require the resource adapter to make use of the existing `MessageEndpointClass` interface to obtain the listener instead. This avoids the need to extend the JCA specification as well as the controversy of doing so for a JMS-specific feature. 
<br/><br/>
However using this existing API for JMS listener beans is an awkward fit, since for each activation there can only ever be one instance of the listener bean. This means that the mechanism of a `MessageEndpointFactory` which can return multiple instances of `MessageEndpoint` is unnecessarily complicated, with the `MessageEndpointFactory` having to behave as if it were managing a pool containing just one instance. To avoid the resource adapter unsuccessfully trying to deliver messages concurrently in multiple threads, all but one of which get blocked whilst they contend for the same instance, the call to `endpointActivation` will need to somehow specify  which type of activation is being performed (MDB or listener bean).
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue 5:</b>How does the application server determine what resource adapter to use? Is this a per-bean or per-method setting? In these proposals a single call to `endpointActivation` is responsible for handling all the callback methods on a listener bean, which means that all callback methods must use the same resource adapter (and so probably the same JMS provider). Is this desirable, or do we want each callback method to be able to specify a different resource adapter?
</td></tr></table>

#### MessageEndpointFactory

The application server will be responsible for providing a suitable `MessageEndpointFactory` class which will be passed to the resource adapter in the calls to `endpointActivation` and `endpointDeactivation`. 

* The methods  `createEndpoint(XAResource xaResource)` and `createEndpoint(XAResource xaResource, long timeout)` will return a `MessageEndpoint` which is a proxy to the JMS listener bean. The behaviour of the  `MessageEndpoint` is described in the next section. Since there is only one JMS listener bean there is only one `MessageEndpoint`, so once a call to one of these methods has returned a `MessageEndpoint` then until `MessageEndpoint#release` has been called any other call to one of these methods will block.

* The method `getActivationName()` will need to return a name unique to the JMS listener bean being activated. 

* The method `getEndpointClass` will return the class of the JMS listener bean.

* The method `isDeliveryTransacted(Method method)` will return whether message deliveries to the specified method are transacted or not. The application server will determine this by checking whether or not the specified method has a `@javax.transaction.Transactional` annotation.

#### MessageEndpoint

The application server will be responsible for returning a suitable `MessageEndpoint` object when  `MessageEndpointFactory#createEndpoint(XAResource xaResource)` and `MessageEndpointFactory#createEndpoint(XAResource xaResource, long timeout)` are called.

This is a proxy to the JMS listener bean object injected into the application which will implement all the JMS listener bean's callback methods as well as the methods defined by the `MessageEndpoint` interface. 

Note that a CDI bean is itself either a proxy (for normal-scoped beans) or a wrapper subclass (for dependent-scoped beans), and the `MessageEndpoint` is a proxy to that. 

* The method `beforeDelivery(Method method)` When this method is called by the resource adapter, the application server will check to see whether the callback method has been annotated with the `@javax.transaction.Transactional` annotation. If it has then this method will start a transaction.

* The method `afterDelivery()` When this method is called by the resource adapter, the application server will complete any transaction started by the `beforeDelivery(Method method)`. 

* The method `release()` may be called by the  by the resource adapter to indicate that it no longer needs a proxy endpoint instance. After this method has been called then the instance becomes available to be returned by a subsequent call to `MessageEndpointFactory#createEndpoint`.

  * Important note:** Since the CDI listener bean is itself a proxy or wrapper and when the resource adapter calls the callback method then the CDI runtime will intercept this call and add transactional behaviour just like with any other method on a CDI bean. This means we are liable to have two mechanisms for starting and completing the transaction: the `beforeCompletion`/`afterCompletion` methods of the `MessageEndpoint`, and the normal CDI interceptors that wrap any bean business method. The application server will need to ensure that the normal CDI interceptors do not attempt to start or complete a transaction if the `beforeCompletion` method has already started one.  

The `@javax.transaction.Transactional` annotation can be used to specify which methods, when thrown by the bean method, will cause the transdaction to be marked for rollback. The application server will need to ensure that any such exception is caught by the `MessageEndpoint` wrapper code and used to determine whether the subsequent call to `afterCompletion` will commit or rollback the transaction.


<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue 6:</b> The interaction between the `beforeCompletion`/`afterCompletion` methods of the `MessageEndpoint`, and the normal CDI interceptors that wrap any bean business method, needs to be considered carefully.
</td></tr></table>

### Responsibilities of the resource adapter

The main responsibility of the resource adapter is to implement the  `ResourceAdapter` methods `endpointActivation` and `endpointDeactivation` so that they handle JMS listener beans as follows:

#### endpointActivation

The resource adapter must implement  the `ResourceAdapter` method `endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec spec)` as follows:

* Call `getEndpointClass()` on the supplied `MessageEndpointFactory` to obtain the class of the JMS listener bean.
* Scan the methods of the JMS listener bean class, looking for callback methods which are annotated with `@JMSListener` 
* For each callback method:
  * Using the annotations on the callback method, obtain a connection to the JMS provider and create a consumer on the specified destination
  * When a message is delivered from a particular consumer, call `createEndpoint` on the specified `MessageEndpointFactory` to obtain a `MessageEndpoint` corresponding to the JMS listener bean.
  *  Call `MessageEndpoint#beforeDelivery`
  *  Use reflection to invoke the callback method for this consumer, setting each method parameter to the message, to the message body, or to a specified message header or property, as defined in the [/jms-spec/pages/proposals for more flexible JMS MDBs(JMSListener2#Summary_of_callback_method_parameters)
  *  Acknowledge the message in the current transaction
  *  Call `MessageEndpoint#afterDelivery`
  *  Call `MessageEndpoint#release`

Note that this is very similar behaviour to that required for activating JMS MDBs. The main difference is that there will only ever be a single instance of  `MessageEndpoint`. There is therefore no point in the resource adapter attempting to deliver messages from a given consumer in more than as single thread.

#### endpointDeactivation

The resource adapter must implement  the `ResourceAdapter` method `endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec spec)` as follows:

* For each callback method
  *  Close the consumer created by `endpointActivation` and clean up any other state created by that method
