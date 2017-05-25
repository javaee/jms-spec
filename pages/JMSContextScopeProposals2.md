# Injection of JMSContext objects - Use Cases A - E (version 3)

## Summary 

This page contains a number of use cases which demonstrate how the proposals in [Injection of JMSContext objects - Proposals (version 3)](/jms-spec/pages/JMSContextScopeProposals) would appear to users. Each use case is followed by an analysis for both [Option 2](/jms-spec/pages/JMSContextScopeProposals#Option_2) and [Option 3](/jms-spec/pages/JMSContextScopeProposals#Option_3). 

If you're looking for a use case which demonstrates the differences between [Option 2](/jms-spec/pages/JMSContextScopeProposals#Option_2) and [Option 3](/jms-spec/pages/JMSContextScopeProposals#Option_3), please look at [use case C](/jms-spec/pages/JMSContextScopeProposals2#Use_case_C._One_bean_which_calls_another_within_the_same_transaction).

Note that these use cases are not intended to demonstrate how `@TransactionScoped` beans behave in general. They are intended only to demonstrate how injected `JMSContext` objects behave.

After reading these, now read [Injection of JMSContext objects - Use Cases F-K (version 3)](/jms-spec/pages/JMSContextScopeProposals3).

* auto-gen TOC:
{:toc}

## Use cases 

### Use case A: Two methods on the same bean within separate transactions

Consider a stateless session bean configured to use container-managed transactions with two business methods. Each method is configured to require a transaction. The bean has an injected `JMSContext`. Each method uses the context to send a message.  

A remote client obtains a reference to `Bean1` and calls the methods `method1a` and `method1b` in turn. 
```
 @TransactionManagement(TransactionManagementType.CONTAINER) 
 @Stateless
 public class Bean1{
 
    @Resource(lookup="jms/inboundQueue") Queue queue;
 
    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    JMSContext context;
 
    @TransactionAttribute(REQUIRED)
    public void method1a() {
       context.send(queue,"Message 1);
    }
 
    @TransactionAttribute(REQUIRED)
    public void method1b() {
       context.send(queue,"Message 2);
    }
 }
```
#### Case A, option 2: Analysis

:--- | :---
Do the `context` variables in the two calls to `context.send()`  use the same injection point? | Yes, since they  use the same `context` variable.
Are the `context` variables in the two calls to `context.send()` in the same `@TransactionScope`? | No, since the two calls to `context.send()` take place in different transactions
Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | No. Although they use the same injection point they are not in the same `@TransactionScope`
Are the two messages guaranteed to be delivered in the order in which they are sent? | No, since they are sent using different `MessageProducer` objects. 

**Important note:** Note however that there is no guarantee that the same bean instance is used for both method invocations. Stateless session bean instances might be pooled, or new stateless session bean instances might be created, but in either case there is no guarantee that the same instance is reused for a client. If the method invocations are serviced by different stateless session bean instances, the answer to the first question above is not necessarily 'Yes'.

#### Case A, option 2: JMSContext lifecycle

The `JMSContext` object used by `method1a` will be created when `method1a` uses `context` for the first time, and destroyed when that method returns.

The `JMSContext` object used by `method1b` will be created when `method1b` uses `context` for the first time, and destroyed when that method returns.

#### Case A, option 3: Analysis

:--- | :---
Are the `context` variables in the two calls to `context.send()`  injected using identical annotations? | Yes, since they  use the same `context` variable.
| Are the `context` variables in the two calls to `context.send()` in the same `@TransactionScope`? | No, since the two calls to `context.send()` take place in different transactions
 Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | No. Although they are injected using identical annotatons they are not in the same `@TransactionScope`
Are the two messages guaranteed to be delivered in the order in which they are sent? | No, since they are sent using different `MessageProducer` objects.

#### Case A, option 3: JMSContext lifecycle

The `JMSContext` object used by `method1a` will be created when `method1a` uses `context` for the first time, and destroyed when that method returns.

The `JMSContext` object used by `method1b` will be created when `method1b` uses `context` for the first time, and destroyed when that method returns.

### Use case B: Two methods on the same bean within the same transaction

Consider two stateless session beans, `Bean1` and `Bean2`. 

`Bean1` is configured to use container managed transactions and has one business method `method1`, which is configured to require a transaction. This invokes `method2a` and `method2b` on `Bean2` in turn.

`Bean2` is also configured to use container managed transactions and has two business methods  `method2a` and `method2b` which are configured to require a transaction. The bean has an injected `JMSContext`. Each method uses the context to send a message.

A remote client obtains a reference to `Bean1` and calls `method1`

<br/>
This is `Bean1`:

 @TransactionManagement(TransactionManagementType.CONTAINER) 
 @Stateless
 public class Bean1{
 
   @EJB Bean2 bean2;
 
    @TransactionAttribute(REQUIRED)
    public void method1() {
       bean2.method2a();
       bean2.method2b();
    }
 }

<br/>
This is `Bean2`:

 @TransactionManagement(TransactionManagementType.CONTAINER) 
 @Stateless
 public class Bean2{
 
    @Resource(lookup="jms/inboundQueue") Queue queue;
 
    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    JMSContext context;
 
    @TransactionAttribute(REQUIRED)
    public void method2a() {
       context.send(queue,"Message 1);
     }
 
    @TransactionAttribute(REQUIRED)
    public void method2b() {
       context.send(queue,"Message 2);
     }
 }

#### Case B, option 2: Analysis

Q | A
:--- | :---
Do the `context` variables in the two calls to `context.send()`  use the same injection point? | Yes, since they  use the same `context` variable.
Are the `context` variables in the two calls to `context.send()` in the same `@TransactionScope`? | Yes, since the two calls to `context.send()` take place in the same transaction.
Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they use the same injection point and are in the same `@TransactionScope`.
Are the two messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

**Important note:** Note however that there is no guarantee that the same bean instance is used for both method invocations. Stateless session bean instances might be pooled, or new stateless session bean instances might be created, but in either case there is no guarantee that the same instance is reused for a client. If the method invocations are serviced by different stateless session bean instances, the answer to the first question above is not necessarily 'Yes'.

#### Case B, option 2: JMSContext lifecycle

The `JMSContext` object will be created when `method2a` uses the `context` variable for the first time, and destroyed when when the transaction is committed, which will occur after `method1` returns.

#### Case B, option 3: Analysis

:--- | :---
Are the `context` variables in the two calls to `context.send()`  injected using identical annotations? | Yes, since they  use the same `context` variable.
Are the `context` variables in the two calls to `context.send()` in the same `@TransactionScope`? | Yes, since the two calls to `context.send()` take place in the same transaction.
Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they are injected using identical annotations and are in the same `@TransactionScope`.
Are the two messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

#### Case B, option 2: JMSContext lifecycle

The `JMSContext` object will be created when `method2a` uses the `context` variable for the first time, and destroyed when when the transaction is committed, which will occur after `method1` returns.


### Use case C. One bean which calls another within the same transaction

Consider two stateless session beans, `Bean1` and `Bean2`

`Bean1` is configured to use container-managed transactions and has a business method `method1`, which is configured to require a transaction. The bean has an injected `JMSContext`. `method1` uses this context to send a message and then invokes `method2` on `Bean2`.

`Bean2` is also configured to use container-managed transactions and has a business method `method2`, which is also configured to require a transaction. The bean also has an injected `JMSContext` with identical annotations to `Bean1`. `method2` simply uses this context to send a second message.

A remote client obtains a reference to `Bean1` and calls `method1`

<br/>
This is `Bean1`

 @TransactionManagement(TransactionManagementType.CONTAINER) 
 @Stateless
 public class Bean1 {
 
    @Resource(lookup="jms/inboundQueue") Queue queue;
 
    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    JMSContext context;
 
    @EJB Bean2 bean2;
 
    @TransactionAttribute(REQUIRED)
    public void method1() {
        context.send(queue,"Message 1");
        bean2.method2();
    } 
 }

<br/>
This is `Bean2`

 
 @TransactionManagement(TransactionManagementType.CONTAINER) 
 @Stateless
 public class Bean2 {
 
    @Resource(lookup="jms/inboundQueue") Queue queue;
 
    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    JMSContext context;
 
    @TransactionAttribute(REQUIRED)
    public void method2() {
        context.send(queue,"Message 2");
    }
 }

#### Case C, option 2: Analysis

:--- | :---
Do the `context` variables in the two calls to `context.send()`  use the same injection point? | No, since they are in different beans and so use different `context` variables.
Are the `context` variables in each call to `context.send()` in the same `@TransactionScope`? | Yes, since the two calls to `context.send()` take place in the same transaction.
Do the `context` variables in each call to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | No. Although they are in the same `@TransactionScope`  they use different injection points. 
Are the two messages guaranteed to be delivered in the order in which they are sent? | No, since they are sent using different `MessageProducer` objects.

#### Case C, option 2: JMSContext lifecycle

The `JMSContext` object used by `method1` will be created when  `method1` uses `context` for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

The `JMSContext` object used by `method2` will be created when  `method2` uses `context` for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

#### Case C, option 3: Analysis

:--- | :---
Are the `context` variables in the two calls to `context.send()`  injected using identical annotations? | Yes. Although they are in different beans and so use different `context` variables, both variables are injected using identical annotations.
Are the `context` variables in each call to `context.send()` in the same `@TransactionScope`? | Yes, since the two calls to `context.send()` take place in the same transaction.
Do the `context` variables in each call to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they are injected using identical annotations and are in the same `@TransactionScope`. 
 Are the two messages guaranteed to be delivered in the order in which they are sent? | Yes, since they use the same `MessageProducer` object.

#### Case C, option 3: JMSContext lifecycle

The `JMSContext` obj_ect will be created when  `method1` uses `context` for the first time, and destroyed when the container commits the transaction, which will be after `method1` returns.

_Note how option 3 uses one `JMSContext` object whilst option 2 uses two._

### Use case D. One bean which sends two messages within the same transaction

Consider a stateless session bean `Bean1`. This is configured to use container-managed transactions and has one business method, `method1`, which is configured to require a transaction. The bean has an injected `JMSContext` . `method1` uses the context to send two messages.

A remote client obtains a reference to `Bean1` and calls `method1`.

```
 @TransactionManagement(TransactionManagementType.CONTAINER)
 @Stateless
 public class Bean1 {
 
    @Resource(lookup="jms/inboundQueue") Queue queue;
 
    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    JMSContext context;
 
    @TransactionAttribute(REQUIRED)
    public void method1() {
        context.send(queue,"Message 1");
        context.send(queue,"Message 2");
    }
 }
```

#### Case D, option 2: Analysis

:--- | :---
Do the `context` variables in the two calls to `context.send()`  use the same injection point? | Yes, since they use the same `context` variable.
Are the `context` variables in each call to `context.send()` in the same `@TransactionScope`? | Yes, since the two calls to `context.send()` take place in the same transaction and in the same method. Either of these would be sufficient to place them in the same scope.
Do the `context` variables in each call to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they use the same injection point and are in the same `@TransactionScope`.
Are the two messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

#### Case D, option 2: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses the `context` variable for the first time, to send the first message. It will be destroyed after `method1` returns.

#### Case D, option 3: Analysis

:--- | :---
Are the `context` variables in the two calls to `context.send()`  injected using identical annotations? | Yes, since they use the same `context` variable.
Are the `context` variables in each call to `context.send()` in the same `@TransactionScope`? | Yes, since the two calls to `context.send()` take place in the same transaction and in the same method. Either of these would be sufficient to place them in the same scope.
Do the `context` variables in each call to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they are injected using identical annotations and are in the same `@TransactionScope`.
Are the two messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

#### Case D, option 3: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses the `context` variable for the first time, to send the first message. It will be destroyed after `method1` returns.

### Use case E. One bean which sends two messages when there is no transaction

Consider a stateless session bean `Bean1`. This is configured to use **bean**-managed transactions and has one business method, `method1`. The bean has an injected `JMSContext`.  `method1` does not start a transaction and uses the context to send two messages.

```
 @TransactionManagement(TransactionManagementType.BEAN)
 @Stateless
 public class Bean1 {
 
    @Resource(lookup="jms/inboundQueue") Queue queue;
 
    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    JMSContext context;
  
    public void method1() {
        context.send(queue,"Message 1");
        context.send(queue,"Message 2");
    }
 }
```

#### Case E, option 2: Analysis

:--- | :---
Do the `context` variables in the two calls to `context.send()`  use the same injection point? | Yes, since they use the same `context` variable.
Are the `context` variables in each call to `context.send()` in the same `@TransactionScope`? | Yes. Although there is no transaction, the two calls to `context.send()` take place in the same method.
Do the `context` variables in each call to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they use the same injection point and are in the same `@TransactionScope`.
Are the two messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

_Case E, option 2: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses the `context` variable for the first time, to send the first message. It will be destroyed when `method1` returns.

Note that it is entirely valid to create and use a `JMSContext` object when there is no transaction. The context is non-transacted. JMS message ordering rules apply irrespective of whether the session is non-transacted, local-transacted or uses a JTA transaction.

The same behaviour would apply when the bean is configured to use container-managed transactions but the transaction attribute type is `NEVER` or `NOT_SUPPORTED`.

#### Case E, option 3: Analysis

:--- | :---
Are the `context` variables in the two calls to `context.send()`  injected using identical annotations? | Yes, since they use the same `context` variable.
Are the `context` variables in each call to `context.send()` in the same `@TransactionScope`? | Yes. Although there is no transaction, the two calls to `context.send()` take place in the same method.
Do the `context` variables in each call to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they are injected using identical annotations and are in the same `@TransactionScope`.
Are the two messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

#### Case E, option 3: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses the `context` variable for the first time, to send the first message. It will be destroyed when `method1` returns.

Note that it is entirely valid to create and use a `JMSContext` object when there is no transaction. The context is non-transacted. JMS message ordering rules apply irrespective of whether the session is non-transacted, local-transacted or uses a JTA transaction.

The same behaviour would apply when the bean is configured to use container-managed transactions but the transaction attribute type is `NEVER` or `NOT_SUPPORTED`.

After reading these, now read [[JMSContextScopeProposals3|Injection of JMSContext objects - Use Cases F-K (version 3)]]
