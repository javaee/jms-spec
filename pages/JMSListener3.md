# More flexible JMS MDBs (Updates to version 2)
{: .no_toc}

_This page contains some proposals for JMS 2.1 that were being considered by the expert group before work was halted and JSR 368 withdrawn. It is retained here as a historical record and in case it proves useful to a future JMS expert group. See also the main [JMS 2.1 page](JMS21)_

This page contains version 1 of proposals to simplify the configuration of JMS MDBs in JMS 2.1 and Java EE 8. 

These proposals supersede [version 1](JMSListener) and [version 2](JMSListener2).

These proposals were superseded by [version 4](JMSListener4) and [version 5](JMSListener5).

## Contents
{: .no_toc}

* auto-gen TOC:
{:toc}

## Changes from version 2 

The major issues which still need to be decided are:

**Issue I17: Should multiple callback methods be permitted?**

 As issue I18 describes, there is an argument that allowing multiple callback methods may be confusing for developers, who may not realise the concurrency implications (i.e. that defining multiple callbacks reduces the number of MDB instances available to process each callback unless the MDB poolsize is increased.). It may also make implementation more complex for application servers that automatically calculate the size of the MDB pool. It may also require an excessive amount of extra work for vendors which offer monitoring and management features for JMS MDBs, since they might need to be extended to allow each callback to be managed separately. Finally, it introduces an ambiguity as to how old-style activation properties relate to multiple callback methods. For example, what is the effect of setting the activation property clientId? when there are multiple callback methods, each using a separate connection?  

**Whether we should allow the new annotations to be combined with old-style activation properties.**

The current proposals state that activation properties may be used to override new-style annotations. However this introduces additional test scenarios. It also introduces potential ambiguity if there are multiple callback methods. However these are still MDBs, and activation properties are an intrinsic feature of both MDBs and the JCA API for message endpoints. One possible clarification is to state that activation properties *defined in the EJB spec* will override those implied by new-style annotations, and that the effect of setting any non-standard activation properties (e.g. other ways to specify the destination) is not defined by the spec - just like the way that the current spec does not define how spec-defined annotations interact with non-standard annotations.

### Relationship between the old and new ways to define a JMS MDB

Should these new annotations be allowed for MDBs that implement the `javax.jms.MessageListener` interface? 

There are two possible cases we need to consider:

* Allowing these new annotations to be used on the legacy `onMessage` method of a `javax.jms.MessageListener`

  * [Version 2](JMSListener2#specifying-the-callback-method) proposed any of the new annotations could be specified. However the requirement that the `@JMSListener` always be specified could not apply since that would break existing MDBs.

* Allowing these new annotations to be used to define additional callback methods (i.e. in addition to the `onMessage` method).

  * [Version 2](JMSListener2#specifying-the-callback-method) proposed that this be allowed so long as the MDB also implemented the `javax.jms.JMSMessageDrivenBean` marker interface. 

On reflection, this is probably introducing unnecessary complexity. It means that lots of additional use cases need to be defined in the spec, implemented, and tested, whilst bringing little benefits to users who can convert to a new-style JMS MDB easily enough.

In addition, since we're seeking EJB spec changes to remove the need for the `javax.jms.JMSMessageDrivenBean` marker interface completely, it doesn't make any sense to introduce a requirement to use it here.

It is therefore proposed to make a clear distinction between "legacy" JMS MDBs and "new-style" JMS 2.1 MDBs:

* Legacy JMS MDBs will implement `javax.jms.MessageListener` and will be configured as they are now. 

* New-style MDBs will not implement `javax.jms.MessageListener`.  They will use the new annotations to specify the callback methods. They will also implement the `javax.jms.JMSMessageDrivenBean` marker interface, though we're seeking EJB spec changes to remove the need for this.

* If the new annotations are used on a MDB which implements `javax.jms.MessageListener` then deployment must fail.

### New JMSListenerProperty annotation

We need to define an additional annotation to allow proprietary activation properties to be specified on the callback method. Many application servers (and resource adapters) use these to offer additional non-standard features. Examples of such properties are the Glassfish-specific activation properties `reconnectAttempts` and `reconnectInterval`, though just about every other application server or resource adapter defines its own set of proprietary activation properties.

Without such an annotation, applications would have to continue defining these properties in the same way as now, thereby forcing applications to mix the new JMS-specific method annotations on the callback method with the old generic class annotations:
```
 @MessageDriven(activationConfig = {
   @ActivationConfigProperty(propertyName = "foo1", propertyValue = "bar1"),
   @ActivationConfigProperty(propertyName = "foo2", propertyValue = "bar2")
 })
 public class MyMessageBean implements JMSMessageDrivenBean {
 
  @JMSListener(lookup="java:global/Trades", type=JMSListener.Type.QUEUE)
   public void processTrade(TextMessage tradeMessage){
     ...
   }
 
 }
```
It is therefore proposed that a new method annotation `@JMSListenerProperty` be defined which the application can use to specify arbitrary activation properties. This annotation would be a "repeatable annotation" so that it could be used multiple times to set multiple properties.
```
 @MessageDriven
 public class MyMessageBean implements JMSMessageDrivenBean {
 
  <b>@JMSListenerProperty(name="foo1", value="bar2")</b>
  <b>@JMSListenerProperty(name="foo2", value="bar2")</b>
  @JMSListener(lookup="java:global/Trades", type=JMSListener.Type.QUEUE)
   public void processTrade(TextMessage tradeMessage){
     ...
   }
 
 }
```
Since this annotation is a repeatable annotation, a composite annotation needs to be defined as well which the compiler will insert automatically when it encounters a repeatable annotation. This will be called `@JMSListenerProperties`.  


New or modified? | Interface or annotation? | Name | Link to javadocs
:--- | :--- | :--- | :---
New | Method annotation | `javax.jms.JMSListenerProperty` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSListenerProperty.html)
New | Method annotation | `javax.jms.JMSListenerProperties` | [javadocs](https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSListenerProperties.html)

Since this gives us yet another way to define activation properties we need to define some override rules:

* A property set using the existing activation property annotations or XML elements will override any value set using the `@JMSListenerProperty` annotation. This allows values to be overridden (for the MDB as a whole) by defining activation properties in the deployment descriptor.

* If `@JMSListenerProperty`  is used to specify one of the JMS standard activation properties then this will override any value set using the corresponding new JMS-specific annotation. This order is chosen to be consistent with the previous rule.

### Callback methods that throw exceptions

Version 2 [proposed](JMSListener2#specifying-the-callback-method) that callback methods will be allowed to throw exceptions as follows:

* Callback methods will be allowed to declare and throw exceptions. Checked exceptions and `RuntimeException` thrown by the callback method (including those thrown by the `onMessage` method of a `MessageListener`) will be handled by the EJB container as defined in the EJB 3.2 specification section 9.3.4 "Exceptions thrown from Message-Driven Bean Message Listener methods".  This defines whether or not any transaction in progress is committed or rolled back, depending on whether or not the exception is a "system exception" or an "application exception", whether or not the application exception is specified as causing rollback, and whether or not the application has called `setRollbackOnly`. It also defines whether or not the MDB instance is discarded. If the transaction is rolled back, or a transaction is not being used, then the message will be redelivered. 

* The JMS provider should detect repeated attempts to redeliver the same message to a MDB. Such messages may either be discarded or delivered to a provider-specific dead message queue. (Note that this not completely new to JMS: JMS 2.1 section 8.7 refers to a JMS provider "giving up" after a message has been redelivered a certain number of times).

Version 3 expands on this by reviewing how exceptions thrown by old-style MDBs should be handled now, and uses this as the basis for proposals on how exceptions thrown by new-style MDBs should be handled.

#### A review of how `RuntimeException`s thrown by old-style MDBs are handled

With old-style JMS MDBs (those that implement `javax.jms.MessageListener`) the `onMessage` callback method is prevented by the compiler from declaring or throwing checked exceptions. However the compiler does allow them to throw unchecked exceptions and the existing EJB and JMS specifications do define how `RuntimeException`s should be handled.

(Recap: an _unchecked exception_ is a class which extends `RuntimeException` or `Error`. A _checked exception_ is any other class which extends `Throwable`. The existing JMS and EJB specifications don't define the behaviour for `Error` exceptions such as `ThreadDeath` and `NoClassDefFoundError`, no doubt because these are by definition "serious problems that a reasonable application should not try to catch". This is probably why the specifications refer to `RuntimeException`s rather than "unchecked exceptions"; JMS 2.1 will take the same approach.)

In deciding how old-style MDBs should handle `RuntimeException`s there are several places we need to look:

* JMS 2.0 section 8.7 "Receiving messages asynchronously" (reproduced below) defines how a JMS provider should handle a `RuntimeException` thrown by the `onMessage` method  of a `MessageListener`. However this section only considers Java SE acknowledgement modes. That means that it is not relevant to MDBs which consume messages in a container-managed transaction. However it _is_ relevant for MDBs which consume messages in auto-acknowledge or dups-ok-acknowledge modes - which is the case when bean-managed transactions are specified. It says that in this case the message will be "immediately redelivered", where "the number of times a JMS provider will redeliver the same message before giving up is provider-dependent".

>8.7 Receiving messages asynchronously
>
>A client can register an object that implements the JMS MessageListener interface with a consumer. As messages arrive for the consumer, the provider delivers them by calling the listener’s onMessage method.
>
>It is possible for a listener to throw a RuntimeException; however, this is considered a client programming error. Well behaved listeners should catch such exceptions and attempt to divert messages causing them to some form of application-specific ‘unprocessable message’ destination.
>
>The result of a listener throwing a RuntimeException depends on the session’s acknowledgment mode.
>
><ul>
><li>AUTO_ACKNOWLEDGE or DUPS_OK_ACKNOWLEDGE - the message will be immediately redelivered. The number of times a JMS provider will redeliver the same message before giving up is provider-dependent. The JMSRedelivered message header field will be set, and the JMSXDeliveryCount message property incremented, for a message redelivered under these circumstances.</li>
><li>CLIENT_ACKNOWLEDGE - the next message for the listener is delivered. If a client wishes to have the previous unacknowledged message redelivered, it must manually recover the session.</li>
><li>Transacted Session - the next message for the listener is delivered. The client can either commit or roll back the session (in other words, a RuntimeException does not automatically rollback the session).</li>
></ul>
>
>JMS providers should flag clients with message listeners that are throwing RuntimeException as possibly malfunctioning.
>
>See Section 6.2.13 “Serial execution of client code” for information about how onMessage calls are serialized by a session.

* EJB 3.2 specification section 9.3.4 "Exceptions thrown from Message-Driven Bean Message Listener methods" describes how the EJB container should handle exceptions thrown by the MDB's callback method. It specifies whether any transaction is committed or rolled back, whether the MDB is discarded, and what exception is rethrown to the resource adapter (or JMS provider). However it does not define how the resource adapter (or JMS provider) should handle any such exception. For that we need to look at the JMS specification.

* In the case where a container-managed transaction is being used, the EJB specification defines whether or not the transaction will be committed by the container or rolled back. If the transaction is committed then it is clear that the message being received will be considered to have been successfully delivered and will not be delivered again. If the transaction is rolled back then the message is clearly eligible for redelivery. JMS 2.0 section 6.2.7 "Transactions" (which mentions Java EE transactions as well as Java SE local transactions) states that  "if a transaction rollback is done, its produced messages are destroyed and its consumed messages are automatically recovered."

Considering the EJB 3.2 specification and JMS 2.0 specifications together, here is a summary the existing requirements for handling a `RuntimeException` thrown by the `onMessage` method of a JMS MDB:

**Existing rules for handling a `RuntimeException` thrown by old-style `onMessage`**

<table border="1">
<tr><th>Transactional mode</th><th>Type of RuntimeException</th><th>Container's action<br/> (as defined by EJB 3.2 section 9.3.4)</th><th>Resource adapter's action (as defined by JMS 2.0 specification)</th></tr>

<tr>
<td rowspan="4">Container-managed transaction demarcation configured (which is the default) and callback method configured with `Required` attribute (which is also the default)<br/><br/>  (Message is received in a transaction, and callback method is called in the same transaction)</td>

<td style="text-align:left;">`RuntimeException` is annotated with `@ApplicationException(rollback="true")`</td>
<td style="text-align:left;">Rollback transaction.<br/>Rethrow exception to resource adapter.</td>
<td style="text-align:left;">Message is "automatically recovered" (JMS 2.0 section 6.2.7 applies)</td>

</tr>

<tr>
<td style="text-align:left;">`RuntimeException` is annotated with `@ApplicationException(rollback="false")<br/> (default) and MDB calls `setRollbackOnly``</td>
<td style="text-align:left;">Rollback transaction.<br/>Rethrow exception to resource adapter.</td>
<td style="text-align:left;">Message is "automatically recovered" (JMS 2.0 section 6.2.7 applies)</td>
</tr>

<tr>
<td style="text-align:left;">`RuntimeException` is annotated with `@ApplicationException(rollback="false")`<br/>(default) and MDB does not call `setRollbackOnly`</td>
<td style="text-align:left;">Commit transaction <br/>Rethrow exception to resource adapter.</td>
<td style="text-align:left;">Continue with next message.</td>
</tr>

<tr>
<td style="text-align:left;">Any other `RuntimeException`</td><td style="text-align:left;">Rollback transaction.<br/>
Discard MDB instance<br/>
Wrap exception in `EJBException` and rethrow to resource adapter.</td><td style="text-align:left;">Message is "automatically recovered" (JMS 2.0 section 6.2.7 applies)</td>
</tr>

<tr><td rowspan="2">Container-managed transaction demarcation configured (which is the default) and callback method configured with `NotSupported` attribute <br/><br/>(This case is not well defined in EJB 3.2, but this is assumed to mean that no transaction is used either to receive the message or to execute the callback method. Instead  the message will be received in auto-acknowledge or dups-ok-acknowledge mode.)</td><td style="text-align:left;">`RuntimeException` is annotated with `@ApplicationException`</td><td style="text-align:left;">`Rethrow exception to resource adapter.`</td><td style="text-align:left;">Redeliver message immediately. May "give up" after repeated attempts. (JMS 2.0 section 8.7 applies)</td></tr>
<tr><td style="text-align:left;">Any other `RuntimeException`</td><td style="text-align:left;">Discard MDB instance<br/>
Wrap exception in `EJBException` and rethrow to resource adapter.</td><td style="text-align:left;">Redeliver message immediately. May "give up" after repeated attempts. (JMS 2.0 section 8.7 applies)</td></tr>

<tr>
<td rowspan="2">Bean-managed transaction<br/><br/>(Message is received in auto-ack or dups-ok-ack mode, not in a transaction. However `onMessage` may itself start and commit a transaction.)</td><td style="text-align:left;">`RuntimeException` is annotated with `@ApplicationException`</td><td style="text-align:left;">Rethrow exception to resource adapter.</td><td style="text-align:left;">Redeliver message immediately. May "give up" after repeated attempts. (JMS 2.0 section 8.7 applies)</td>
</tr>

<tr><td style="text-align:left;">Any other `RuntimeException`</td><td style="text-align:left;">Rollback transaction if there is one in progress (this does not affect the message being delivered)<br/>
Discard MDB instance<br/>
Wrap exception in `EJBException` and rethrow to resource adapter.</td><td style="text-align:left;">Redeliver message immediately. May "give up" after repeated attempts. (JMS 2.0 section 8.7 applies)</td></tr>

</table>

(EJB 3.2 defines that the behaviour of the container depends on whether the exception is an "application exception" or a "system exception".  If the exception is a `RuntimeException` then it is assumed to be a system exception unless it is explicitly annotated as an application exception.)

These existing rules leave scope for clarification:

* JMS 2.0 section 8.7 really applies only to the auto-acknowledge and dups-ok-acknowledge cases when using a Java SE message listener. The specification does not explicitly state that it applies also to the auto-acknowledge and dups-ok-acknowledge cases when using a MDB. 

* The statement that the JMS provider may "give up" after repeated attempts to deliver a message applies only to the auto-acknowledge and dups-ok-acknowledge cases. The specification does not explicitly state whether the same option applies to a message that is repeatedly rolled back.

* The term "give up" is not defined. The specification does not state whether this means the message is permanently deleted or whether it might be redelivered at some future time.  It also does not mention the possibility of diverting the message to a dead message queue.

#### Discussion of how exceptions thrown by new-style MDBs should be handled

In a new-style JMS MDB,  it will be possible for a callback method to throw a `RuntimeException`. This is identical to the existing case of an old-style MDB whose `onMessage` method throws a  `RuntimeException`, and the existing rules, summarised in the previous section, can apply. We need to decide whether to state that throwing a `RuntimeException` is considered a programming error.

In addition, it is proposed that callback methods on new-style MDBs be allowed to declare and throw checked exceptions. It will be up to the MDB provider to decide what checked exceptions may be thrown; this will _not_ be considered a programming error. 

The EJB 3.2 specification already defines how the EJB container should handle checked exceptions thrown by a MDB's callback method (although this has never previously been possible with JMS MDBs). Here's a summary of what the existing EJB and JMS specifications define when the callback method of a new-style MDB throws a checked exception.:

**Existing rules for handling a checked exception thrown by callback method in new-style MDB**

<table border="1">
<tr><th>Transactional mode</th><th>Type of checked exception</th><th>Container's action<br/> (as defined by EJB 3.2 section 9.3.4)</th><th>Resource adapter's action (as defined by JMS 2.0 specification)</th></tr>
<tr><td rowspan="3">Container-managed transaction demarcation configured (which is the default) and callback method configured with `Required` attribute (which is also the default)<br/><br/>  (Message is received in a transaction, and callback method is called in the same transaction)</td>
<td style="text-align:left;">Checked exception is annotated with `@ApplicationException(rollback="true")`</td>
<td style="text-align:left;">Rollback transaction.<br/>Rethrow exception to resource adapter.</td>
<td style="text-align:left;">Message is "automatically recovered" (JMS 2.0 section 6.2.7 applies)</td>
</tr>
<tr><td style="text-align:left;">Any other checked exception and <br/>MDB called `setRollbackOnly`
</td>
<td style="text-align:left;">Rollback transaction.<br/>Rethrow exception to resource adapter.</td>
<td style="text-align:left;">Message is "automatically recovered" (JMS 2.0 section 6.2.7 applies)</td>
</tr>

<tr><td style="text-align:left;">Any other checked exception and <br/>MDB did not call `setRollbackOnly`</td>
<td style="text-align:left;">Commit transaction.<br/>Rethrow exception to resource adapter.</td>
<td style="text-align:left;">Transaction was committed so continue with next message</td>
</tr>

<tr>
<td rowspan="1">Container-managed transaction demarcation configured (which is the default) and callback method configured with `NotSupported` attribute <br/><br/>(This case is not well defined in EJB 3.2, but thies is assumed to mean that no transaction is used either to receive the message or to execute the callback method. Instead  the message will be received in auto-acknowledge or dups-ok-acknowledge mode.)</td><td style="text-align:left;">Any checked exception</td><td style="text-align:left;">`Rethrow exception to resource adapter.`</td>
<td style="text-align:left;">Not defined in JMS 2.0</td>
</tr>

<tr>
<td rowspan="1">Bean-managed transaction<br/><br/>(The message is received in auto-ack or dups-ok-ack mode, not in a transaction. However `onMessage` may itself start and commit a transaction.)</td><td style="text-align:left;">Any checked exception</td><td style="text-align:left;">Rethrow checked exception to resource adapter.</td>
<td style="text-align:left;">Not defined in JMS 2.0</td>
</tr>
</table>

(In EJB 3.2 parlance, any checked exception is considered an "application exception", irrespective of whether it is explicitly annotated with `@ApplicationException`. By default an application exception does not prevent the transaction being committed. The annotation `@ApplicationException(rollback="true")` may be used to specify transaction rollback.)

As the table above shows, the existing specifications already cover the case where the message is being received in a container-managed-transaction, but not the cases where the message is being received in auto-ack or dups-ok-ack mode. So what do we need to do for JMS 2.1? 

The minimum we need to do for JMS 2.1 is:

* Say nothing about the case where the message is being received in a container-managed-transaction and the callback method throws a checked exception. The EJB 3.2 specification already defines when the transaction is rolled back and when it is committed. Automatic recovery after a rollback is not a new case so doesn't need to be defined further. Note that this would mean there is no mention of the case where the same message repeatedly causes an exception to be thrown. 

* Define the required behaviour for when the message is being received in auto-ack or dups-ok-ack mode and the callback method throws a checked exception. We could simply define that the result is the same as when the callback throws a `RuntimeException`, which is already defined for the Java SE case in JMS 2.0 section 8.7. This would say that the message would be immediately redelivered, that the redelivered flag should be set, and that the JMS provider may "give up" if a message is repeatedly redelivered. For completeness we could also extend this to cover a `RuntimeException` thrown by a MDB.

See [Proposed minimum new wording for JMS 2.1 specification](JMSListener3#proposed-minimum-new-wording-for-jms-21-specification) below.

However we may decide that we want to do more than the minimum:

* We could say more about how a message is "automatically recovered" (which are existing words) after a Java EE transaction rollback. We could mention that recovery might not be immediate and so might occur out of order (though since the spec doesn't describe MDB message order this may not be necessary). We could mention whether or not the redelivered flag should be set on a recovered message. We could mention what the resource adapter (if used) or JMS provider is allowed to do if the same message is repeatedly recovered. 

* We could say more about how a message is "immediately redelivered" after an exception is thrown when auto-ack or dups-ok-ack is being used. The wording in JMS 2.0 8.7 doesn't allow for the possibility that there may be more than one consumer on the queue (or JMS 2.0 shared subscription) , or that there may be more than one MDB instance.

* We could say more about what it means to "give up" on a message which is being repeatedly redelivered/recovered. Does this mean deleting the message? Should we mention the possibility of diverting the message to a dead message queue?

See [/jms-spec/pages/JMSListener3#Proposed_extended_new_wording_for_JMS_2.1_specification Proposed extended new wording for JMS 2.1 specification] below.

#### Proposed minimum new wording for JMS 2.1 specification

Here is a proposed minimum wording. This would be a new section (arbitrarily numbered 16.5 here) in a new chapter 16 defining JMS MDBs in more detail. It will follow a number of previous sections which define how JMS MDBs are configured.

>**16. JMS message-driven beans**
> 
>**16.5 Exceptions thrown by message callback methods**
>
>An application-defined callback method of a JMS MDB may throw checked exceptions (where allowed by the method signature) or `RuntimeException`s. 
>
>The `onMessage` method of a JMS MDB that implements `MessageListener` may throw `RuntimeException`s.
>
>All exceptions thrown by message callback methods must be handled by the container as defined in the EJB 3.2 specification section 9.3.4 "Exceptions thrown from Message-Driven Bean Message Listener methods". This defines whether or not any container-managed transaction is committed or rolled back by the container. It also defines whether or not the MDB instance is discarded, whether or not the exception is required to be logged, and what exception is re-thrown to the resource adapter (if a resource adapter is being used).
>
>If a resource adapter is being used it must catch any checked exceptions or `RuntimeException`s thrown by the callback method. 
>
>If a message is being delivered to the callback method of a MDB using container-managed transaction demarcation, and the resource adapter had called the `beforeDelivery` method on the `javax.resource.spi.endpoint.MessageEndpoint` prior to invoking the callback method then it must call the `afterDelivery` method afterwards even if the callback method threw a checked exception or `RuntimeException`. This ensures that the container-managed transaction is rolled back or committed by the container as required by the EJB specification.
>
>If a message is being delivered to the callback method of a MDB, and auto-acknowledge or dups-ok-acknowledge mode is being used, and the callback method throws a checked exception or a `RuntimeException` then the message will be automatically redelivered.  The number of times a JMS provider will redeliver the same message before giving up is provider-dependent. The JMSRedelivered message header field will be set, and the JMSXDeliveryCount message property incremented, for a message redelivered under these circumstances.

The proposed minimum wording above essentially extends the wording already used in the JMS specification for Java SE message listeners to cover JMS MDBs:

* There's a reminder that EJB 3.2 defines whether or not any container-managed transaction is committed or rolled back, and a requirement that the appropriate `afterDelivery` method must be called even if an exception is thrown. 

* If auto-acknowledge or dups-ok-acknowledge mode is being used then the behaviour of the resource adapter or JMS provider is similar to that defined in JMS 2.0 section 8.7.

* The proposed wording above not repeat the statement in JMS 2.0 section 8.7 that "it is possible for a listener to throw a `RuntimeException`; however, this is considered a client programming error.". That statement remains for Java SE message listeners for reasons of backward compatibility. However we're not repeating it here for MDBs since we're explicitly allowing MDB callback methods to throw checked exceptions, and it seems arbitrary to allow checked exceptions but to discourage `RuntimeException`s. Especially as methods in the JMS 2.0 simplified API throw `RuntimeException`s.

#### Proposed extended new wording for JMS 2.1 specification

Here is a proposed extended wording. 

The JMS 2.1 specification will contain a new chapter 16 defining JMS MDBs in more detail. This chapter will have a section (arbitrarily numbered 16.5 here) devoted to exceptions thrown by callback methods and a section following it (arbitrarily numbered 16.6 here)  about message redelivery in general. Here is a suggested wording. This section will follow a number of previous sections which define how JMS MDBs are configured.

>**16. JMS message-driven beans**
> 
>**16.5 Exceptions thrown by message callback methods**
>
>An application-defined callback method of a JMS MDB may throw checked exceptions (where allowed by the method signature) or `RuntimeException`s. 
>
>The `onMessage` method of a JMS MDB that implements `MessageListener` may throw `RuntimeException`s.
>
>All exceptions thrown by message callback methods must be handled by the container as defined in the EJB 3.2 specification section 9.3.4 "Exceptions thrown from Message-Driven Bean Message Listener methods". This defines whether or not any container-managed transaction is committed or rolled back by the container. It also defines whether or not the MDB instance is discarded, whether or not the exception is required to be logged, and what exception is re-thrown to the resource adapter (if a resource adapter is being used).
>
>If a resource adapter is being used it must catch any checked exceptions or `RuntimeException`s thrown by the callback method. 
>
>If a message is being delivered to the callback method of a MDB using container-managed transaction demarcation, and the resource adapter had called the `beforeDelivery` method on the `javax.resource.spi.endpoint.MessageEndpoint` prior to invoking the callback method then it must call the `afterDelivery` method afterwards even if the callback method threw a checked exception or `RuntimeException`. This ensures that the container-managed transaction is rolled back or committed by the container as required by the EJB specification.
>
>If a message is being delivered to the callback method of a MDB using container-managed transaction demarcation, and the transaction is rolled back, then the message will be automatically recovered. Message delivery after transaction recovery is described in more detail in section 16.6.1 "Message redelivery after transaction rollback".
>
>If a message is being delivered to the callback method of a MDB, and auto-acknowledge or dups-ok-acknowledge mode is being used, and the callback method throws a checked exception or a `RuntimeException`, then the message will be automatically redelivered.  The message will not be acknowledged and will be immediately redelivered. Message redelivery after an exception in auto-acknowledge or dups-ok-acknowledge mode is described in more detail in section 16.6.2 "Message redelivery after an exception, in auto-acknowledge or dups-ok-acknowledge mode".
>
>**16.6 Message redelivery to MDBs**
>
>**16.6.1 Message redelivery after transaction rollback**
>
>If a message is being delivered to the callback method of a MDB, and container-managed transaction demarcation is being used, then if the transaction is rolled back for any reason the message will be redelivered. Redelivery may not be immediate. The number of times a JMS provider will redeliver the same message before giving up is provider-dependent. The JMSRedelivered message header field will be set, and the JMSXDeliveryCount message property incremented, for a message redelivered under these circumstances. 
>
>**16.6.2 Message redelivery after an exception, in auto-acknowledge or dups-ok-acknowledge mode**
> 
>If a message is being delivered to the callback method of a MDB, and auto-acknowledge or dups-ok-acknowledge mode is being used, and the callback method throws a checked exception or a RuntimeException then the message will be immediately redelivered. The number of times a JMS provider will redeliver the same message before giving up is provider-dependent. The JMSRedelivered message header field will be set, and the JMSXDeliveryCount message property incremented, for a message redelivered under these circumstances. 

The proposed extended wording above extends the minimum wording as follows:

* There's a new section which defines how messages are redelivered after rollback. This states that redelivery may not be immediate. It also states that the redelivery flag should be set , that the redelivery count should be incremented and that the resource adapter may "give up" after repeated redelivery.

#### Redelivery delays, redelivery limits and dead-letter queues

The proposals above could be extended to include a fuller set of features which allow the user to configure

* A minimum time delay between redelivery attempts (which will inevitably change message order)

* The maximum number of times a message can be redelivered before some other action is taken

* The action to be taken when the redelivery limit has been reached. Options could be
  * delete the message
  * forward the message to a specified queue or topic

The proposals above will define the behaviour when none of these are specified. 

Adding these will be considered separately. (See also [https://github.com/javaee/jms-spec/issues/117 JMS_SPEC-117]).

## Simplifying the method annotations: some options 

No changes are proposed to the method annotations (apart from the addition of `JMSListenerProperty`) at this stage.  

(The term "method annotations" here refers to the annotations that are placed on each callback method rather than the annotations that are placed on method arguments)

However the current proposals still need to be reviewed in detail. Aspects needing review include
* Exactly what annotations we require, and what attributes each annotation has
* Exactly what name should be used for each annotation (e.g. should they start with "JMS"), and what attributes they should have
* Whether attributes are optional (e.g. have a default) or mandatory
* The enumerated types used

### Recap of current proposal (Option A) 

The following example illustrates the annotations currently proposed:
```
 @JMSListener(lookup="java:global/java:global/Trades",type=JMSListener.Type.TOPIC )
 @JMSConnectionFactory("java:global/MyCF")
 @SubscriptionDurability(SubscriptionDurability.Mode.DURABLE)
 @ClientId("myClientID1")
 @SubscriptionName("mySubName1")
 @MessageSelector("ticker='ORCL'")
 @JMSListenerProperty(name="foo1", value="bar1")
 @JMSListenerProperty(name="foo2", value="bar2")
 @Acknowledge(Acknowledge.Mode.DUPS_OK_ACKNOWLEDGE)
 public void giveMeAMessage(Message message) {
   ...
 }
```
Obviously, this is an extreme example which demonstrates the use of every annotation. 

The most important annotation here is `@JMSListener`. This has two attributes, "`lookup`" and "`type`"

* `@JMSListener` is the annotation that signifies that this is a callback method, and is always required. That's why the name of this annotation is `@JMSListener` rather than, say,  `@JMSDestination`, despite its role being to specify the queue or topic.

* The `lookup` attribute is used to set the `destinationLookup` activation property and can be omitted. This allows the MDB to use legacy or non-standard ways to define the queue or topic, such as  using a non-standard activation property (specified using a `@JMSListenerProperty` annotation). 

* The "`type`" attribute is used to set the `destinationType` activation property, which specifies whether the destination is a queue or topic. This is proposed as a mandatory attribute which must be set (or the code won't compile). See the side discussion below.

* We may want to extend `@JMSListener` in the future to allow the queue or topic's "non-portable" name to be specified. That's not proposed currently (it's a separate issue) but we did we could define an additional attribute "`name`", as in `@JMSListener(name="tradesTopic",type=JMSListener.Type.TOPIC )`. That's why the attribute used to specify JNDI name should be called "`lookup`" rather than "`value`".

The remaining annotations have a single attribute "`value`", with each annotation corresponding to a single activation property.

There are a number of things we should review here. Probably the main thing we need to consider is which is better:

1. to have lots of separate annotations, almost all of which have a single attribute, or

2. to have a smaller number of annotations, each of which set multiple attributes.


<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Side discussion:</b> Should "destination type" be mandatory or have a default?

We need to decide whether the application should be required to specify whether the destination is a queue or a topic.
 Currently EJB 3.2 is problematic: it defines an activation property `destinationType` but  does not define what should happen if it is not set: it's not described as mandatory but there's no default value. Are application server/resource adapters expected to define their own default, or are they expected to be able to work when this property is not set? If the latter, then what is the point of having this property?

For new-style MDBs we have four options:
* Make it mandatory to set the destination type. This is the current proposal: the  `@JMSListener` attribute "`type`" is mandatory
* Make it optional, but define a default value (queue or topic)
* Make it optional, and leave it to application servers to choose a default (which will be non-portable)
* Make it optional, and require application servers to be able to handle the case where it is omitted (which may be non-portable)
</td></tr></table>

### An alternative proposal (Option B)

If we went for  a smaller number of annotations, each of which set multiple attributes, what might they look like? Here's a suggestion.

This proposals would replace the eight annotations of Option A with three new annotations:

* `@JMSQueueListener`

* `@JMSDurableTopicListener`

* `@JMSNonDurableTopicListener`

plus zero or more occurrences of the newly-proposed annotation:

* `@JMSListenerProperty`

This would remove the need for the user to specify destinationType or subscriptionDurability as attributes.

Here's an example of a MDB which listens for messages from a queue:
```
 @JMSQueueListener(destinationLookup="java:global/java:global/Trades",
    connectionFactoryLookup="java:global/MyCF",
    messageSelector=""ticker='ORCL'",
    acknowledge=JMSQueueListener.Mode.DUPS_OK_ACKNOWLEDGE)
 @JMSListenerProperty(name="foo1", value="bar1")
 @JMSListenerProperty(name="foo2", value="bar2")
 public void giveMeAMessage(Message message) {
    ...
 }
```
Here's an example of a MDB which listens for messages from a durable topic subscription:
```
 @JMSDurableTopicListener(destinationLookup="java:global/java:global/Trades",
    connectionFactoryLookup="java:global/MyCF",
    clientId="myClientID1",
    subscriptionName="mySubName1",
    messageSelector=""ticker='ORCL'",
    acknowledge=JMSQueueListener.Mode.DUPS_OK_ACKNOWLEDGE)
 @JMSListenerProperty(name="foo1", value="bar1")
 @JMSListenerProperty(name="foo2", value="bar2")
 public void giveMeAMessage(Message message) {
    ...
 }
```
Here's an example of a MDB which listens for messages from a non-durable topic subscription:
```
 @JMSNonDurableTopicListener(destinationLookup="java:global/java:global/Trades",
    connectionFactoryLookup="java:global/MyCF",
    messageSelector=""ticker='ORCL'",
    acknowledge=JMSQueueListener.Mode.DUPS_OK_ACKNOWLEDGE)
 @JMSListenerProperty(name="foo1", value="bar1")
 @JMSListenerProperty(name="foo2", value="bar2")
 public void giveMeAMessage(Message message) {
    ...
 }
```
Some of these attributes would need to be specified, others would be optional.

The main benefits of this option are:

* Fewer annotations for the user to remember, and more scope for the IDE to provide guidance. The user simply needs to choose between `@JMSQueueListener`, `@JMSDurableTopicListener` and `@JMSNonDurableTopicListener` and the IDE would then remind them which standard attributes that they may need to set.

* No need to explicitly specify destination type or subscription durability via verbose enumerated types

###  Option C

A third option might be to combine option B with an additional, completely generic annotation.

In addition to `@JMSQueueListener`, `@JMSDurableTopicListener` and `@JMSNonDurableTopicListener`, there would also be an alternative, generic annotation  `@JMSDestinationListener`. This would strictly be unnecessary, but some users may prefer it:
```
 @JMSDestinationListener(destinationLookup="java:global/java:global/Trades",
    type=JMSListener.DestinationType.TOPIC,
    connectionFactoryLookup="java:global/MyCF",
    clientId="myClientID1",
    subscriptionDurability=JMSListener.SubscriptionDurability.DURABLE
    subscriptionName="mySubName1",
    messageSelector=""ticker='ORCL'",
    acknowledge=JMSQueueListener.Mode.DUPS_OK_ACKNOWLEDGE)
 @JMSListenerProperty(name="foo1", value="bar1")
 @JMSListenerProperty(name="foo2", value="bar2")
 public void giveMeAMessage(Message message) {
    ...
 }
```
This generic annotation would need  to allow the attributes `destinationType` and `subscriptionDurability` to be specified.

###  Option D

A fourth option would be to offer just the generic annotation `@JMSDestinationListener`, plus `@JMSListenerProperty`

