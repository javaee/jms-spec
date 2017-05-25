# Injection of JMSContext objects - Use Cases A - E (version 4)

## Summary 

This page contains a number of use cases which demonstrate how the scope proposed in [Injection of JMSContext objects - Proposals (version 4)](/jms-spec/pages/JMSContextScopeProposalsv4p1) would appear to users. Each use case is followed by an analysis. 

After reading these, now read [Injection of JMSContext objects - Use Cases F - K (version 4)](/jms-spec/pages/JMSContextScopeProposalsv4p3).

Note that these examples do **not** use the proposed new `JMSContext` API for sending messages described in [Proposed changes to JMSContext to support injection (Option 4)](/jms-spec/pages/JMSContextScopeProposalsv4p4).

## Contents

* auto-gen TOC:
{:toc}

## Use case A: Two methods on the same bean within separate transactions

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

### Case A: Analysis

Q | A
:--- | :---
Are the `context` variables in the two calls to `context.send()`  injected using identical annotations? | Yes, since they  use the same variable declaration.
What scope do the `context` variables in the two calls to `context.send()` have? | Both  calls to `context.send()` take place within a transaction, so they both have transaction scope.
Are the `context` variables in the two calls to `context.send()` in the same scope? | No, since the two calls to `context.send()` take place in different transactions
Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | No. Although they are injected using identical annotatons they are not in the same transaction and so have separate transaction scopes.
 Are the two messages guaranteed to be delivered in the order in which they are sent? | No, since they are sent using different `MessageProducer` objects.

  * Important note:** Note that there is no guarantee that the same bean instance is used for both method invocations. Stateless session bean instances might be pooled, or new stateless session bean instances might be created, but in either case there is no guarantee that the same instance is reused for a client. However, even if the method invocations are serviced by different stateless session bean instances this does not affect whether or not the same `JMSContext` object is used. 

### Case A: JMSContext lifecycle

The `JMSContext` object used by `method1a` will be created when `method1a` uses `context` for the first time, and destroyed when the first transaction is committed.

The `JMSContext` object used by `method1b` will be created when `method1b` uses `context` for the first time, and destroyed when the second transaction is committed.

### Use case B: Two methods on the same bean within the same transaction

Consider two stateless session beans, `Bean1` and `Bean2`. 

`Bean1` is configured to use container managed transactions and has one business method `method1`, which is configured to require a transaction. This invokes `method2a` and `method2b` on `Bean2` in turn.

`Bean2` is also configured to use container managed transactions and has two business methods  `method2a` and `method2b` which are configured to require a transaction. The bean has an injected `JMSContext`. Each method uses the context to send a message.

A remote client obtains a reference to `Bean1` and calls `method1`

This is `Bean1`:
```
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
```

This is `Bean2`:
```
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
```

#### Case B: Analysis

Q | A
:--- | :---
| Are the `context` variables in the two calls to `context.send()`  injected using identical annotations?
| Yes, since they  use the same variable declaration.
|- valign="top"
| What scope do the `context` variables in the two calls to `context.send()` have?
| Both  calls to `context.send()` take place within a transaction, so they both have transaction scope.
|- valign="top"
| Are the `context` variables in the two calls to `context.send()` in the same scope?
| Yes, since the two calls to `context.send()` take place in the same transaction
|- valign="top"
| Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects?
| Yes. They are injected using identical annotatons and have same transaction scope.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same `MessageProducer` object.
|} 

#### Case B: JMSContext lifecycle

The `JMSContext` object will be created when `method2a` uses the `context` variable for the first time, and destroyed when when the transaction is committed, which will occur after `method1` returns.

### Use case C. One bean which calls another within the same transaction

Consider two stateless session beans, `Bean1` and `Bean2`

`Bean1` is configured to use container-managed transactions and has a business method `method1`, which is configured to require a transaction. The bean has an injected `JMSContext`. `method1` uses this context to send a message and then invokes `method2` on `Bean2`.

`Bean2` is also configured to use container-managed transactions and has a business method `method2`, which is also configured to require a transaction. The bean also has an injected `JMSContext` with identical annotations to `Bean1`. `method2` simply uses this context to send a second message.

A remote client obtains a reference to `Bean1` and calls `method1`

This is `Bean1`
```
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
```

This is `Bean2`
```
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
```

#### Case C: Analysis

Q | A
:--- | :---
| Are the `context` variables in the two calls to `context.send()`  injected using identical annotations?
| Yes. Although they use separate variable declarations, both declarations use identical annotations.
|- valign="top"
| What scope do the `context` variables in the two calls to `context.send()` have?
| Both  calls to `context.send()` take place within a transaction, so they both have transaction scope.
|- valign="top"
| Are the `context` variables in the two calls to `context.send()` in the same scope?
| Yes, since the two calls to `context.send()` take place in the same transaction
|- valign="top"
| Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects?
| Yes. They are injected using identical annotations and have the same transaction scope.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same `MessageProducer` object.
|} 

#### Case C: JMSContext lifecycle

The `JMSContext` object will be created when  `method1` uses `context` for the first time, and destroyed when the container commits the transaction, which will be after `method1` returns.

### Use case D. One bean which sends two messages within the same transaction

Consider a stateless session bean `Bean1`. This is configured to use container-managed transactions and has one business method, `method1`, which is configured to require a transaction. The bean has an injected `JMSContext` . `method1` uses the context to send two messages.

A remote client obtains a reference to `Bean1` and calls `method1`.
````
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
````

# Case D: Analysis

Q | A
:--- | :---
| Are the `context` variables in the two calls to `context.send()`  injected using identical annotations?
| Yes, since they they use the same variable declaration.
|- valign="top"
| What scope do the `context` variables in the two calls to `context.send()` have?
| Both  calls to `context.send()` take place within a transaction, so they both have transaction scope.
|- valign="top"
| Are the `context` variables in the two calls to `context.send()` in the same scope?
| Yes, since the two calls to `context.send()` take place in the same transaction
|- valign="top"
| Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects?
| Yes. They are injected using identical annotations and have the same transaction scope.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same `MessageProducer` object.
|} 

# Case D: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses the `context` variable for the first time, to send the first message. It will be destroyed when the transaction is committed.

### Use case E. One bean which sends two messages when there is no transaction

Consider a stateless session bean `Bean1`. This is configured to use **bean**-managed transactions and has one business method, `method1`. The bean has an injected `JMSContext`.  `method1` does not start a transaction and uses the context to send two messages.

A remote client obtains a reference to `Bean1` and calls `method1`.
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

#### Case E: Analysis

Q | A
:--- | :---
| Are the `context` variables in the two calls to `context.send()`  injected using identical annotations?
| Yes, since they they use the same variable declaration.
|- valign="top"
| What scope do the `context` variables in the two calls to `context.send()` have?
| Both  calls to `context.send()` take place when there is no transaction, so they both have **request** scope.
|- valign="top"
| Are the `context` variables in the two calls to `context.send()` in the same scope?
| Yes, since the two calls to `context.send()` take place in the same request
|- valign="top"
| Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects?
| Yes. They are injected using identical annotations and have the same request scope.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same `MessageProducer` object.
|} 

#### Case E: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses the `context` variable for the first time, to send the first message. It will be destroyed when `method1` returns.

Note that it is entirely valid to create and use a `JMSContext` object when there is no transaction. The context is non-transacted. JMS message ordering rules apply irrespective of whether the session is non-transacted, local-transacted or uses a JTA transaction.

The same behaviour would apply when the bean is configured to use container-managed transactions but the transaction attribute type is `NEVER` or `NOT_SUPPORTED`.

After reading these, now read [[JMSContextScopeProposalsv4p3|Injection of JMSContext objects - Use Cases F-K (version 4)]]
