# Injection of JMSContext objects - Use Cases F - K  (version 4)
{: .no_toc}

_This page relates to JSR 343 (JMS 2.0) which has been released. It is retained here as a historical record and in case it proves useful to a future JMS expert group._

This page contains a number of use cases which demonstrate how the scope proposed in [Injection of JMSContext objects - Proposals (version 4)](/jms-spec/pages/JMSContextScopeProposalsv4p1) would appear to users. Each use case is followed by an analysis. 

Before reading these, read [Injection of JMSContext objects - Use Cases A-E (version 4)](/jms-spec/pages/JMSContextScopeProposalsv4p2).

Note that these examples do **not** use the proposed new `JMSContext` API for sending messages described in [Proposed changes to JMSContext to support injection (Option 4)](/jms-spec/pages/JMSContextScopeProposalsv4p4).

* auto-gen TOC:
{:toc}

## Use case F: One bean which calls another when there is no transaction

Consider two stateless session beans, `Bean1` and `Bean2`.

`Bean1` is configured to use bean-managed transactions and has a business method `method1`. The bean has an injected `JMSContext`. `method1` does not start a transaction, uses this context to send a message, and then invoke `method2` on `Bean2`.

`Bean2` is also configured to use bean-managed transactions and has a business method `method2`. The bean has an injected `JMSContext`. `method2` does not start a transaction and simply uses this context to send a second message.

A remote client obtains a reference to `Bean1` and calls `method1`.

This is `Bean1`
```
@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
public class Bean1 {
 
  @Resource(lookup="jms/inboundQueue") Queue queue;
 
  @Inject
  @JMSConnectionFactory("jms/connectionFactory")
  JMSContext context;
 
  @EJB Bean2 bean2;
 
  public void method1() {
    context.send(queue,"Message 1");
    bean2.method2();
  }
}
```

This is `Bean2`
``` 
@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
public class Bean2 {
 
  @Resource(lookup="jms/inboundQueue") Queue queue;

  @Inject
  @JMSConnectionFactory("jms/connectionFactory")
  JMSContext context;
 
  public void method2() {
    context.send(queue,"Message 2");
  }
}
```

### Case F: Analysis

Q | A
:--- | :---
Are the `context` variables in the two calls to `context.send()`  injected using identical annotations? | Yes. Although they  use separate variable declarations, both declarations use identical annotations.
What scope do the `context` variables in the two calls to `context.send()` have? | Both  calls to `context.send()` take place when there is no transaction, so they both have **request** scope.
Are the `context` variables in the two calls to `context.send()` in the same scope? | Yes, since the two calls to `context.send()` take place in the same request
Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes. They are injected using identical annotations and have the same request scope.
Are the two messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

### Case F: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses `context` for the first time, and destroyed when method1 returns.

The same behaviour should apply when the bean is configured to use container-managed transactions but the transaction attribute type is `NEVER` or `NOT_SUPPORTED`. 

##  Use case G: One bean method which uses two transactions

Consider a stateless session bean `Bean1`. This is configured to use **bean**-managed transactions and has one business method, `method1`. The bean has an injected `JMSContext`. `method1` starts a transaction and uses the context to send two messages. It then commits the transaction and starts a second transaction. It then uses the context to send two further messages and finally commits the second transaction.

A remote client obtains a reference to `Bean1` and calls `method1`.
```
@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
public class Bean1 {
 
  @Resource(lookup="jms/inboundQueue") Queue queue;
 
  @Inject
  @JMSConnectionFactory("jms/connectionFactory")
  JMSContext context;
 
  @Inject UserTransaction ut;
 
  public void method1() {
    ut.begin();
    context.send(queue,"Message 1");
    context.send(queue,"Message 2");
    ut.commit();
    ut.begin();
    context.send(queue,"Message 3");
    context.send(queue,"Message 4");
    ut.commit();
  }
 
}
```

### Case G: Analysis

Q | A
:--- | :---
Are the `context` variables in the four calls to `context.send()`  injected using identical annotations? | Yes, since they  use the same variable declaration.
What scope do the `context` variables in the four calls to `context.send()` have? | All four calls to `context.send()` take place within a transaction, so the `context` variables all have transaction scope.
Are the `context` variables in the four calls to `context.send()` in the same scope? | The first and second calls to `context.send()` take place in one transaction and the third and fourth calls  to `context.send()` take place in another transaction. This means the `context` variables in the first and second calls are in one transaction scope, and the `context` variables in the third and fourth calls are in a different transaction scope.
Do the `context` variables in the four calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | All four  `context` variables  are injected using identical annotatons. The `context` variables in the first and second calls are in the same transaction scope and so share the same `JMSContext` (and therefore `MessageProducer`) object.  The `context` variables in the third and fourth calls are in a different transaction scope and so share a different `JMSContext` (and therefore `MessageProducer`) object.
Are the four messages guaranteed to be delivered in the order in which they are sent? | The first and second messages share one `MessageProducer` object and are delivered in order, and the third and fourth messages  share a different `MessageProducer` object and are delivered in order. However there is no guarantee that the first and second messages will be delivered before the third and fourth messages.

### Case G: JMSContext lifecycle

The `JMSContext` object used by the first and second calls to  `context.send()` will be created when the first call to `context.send()` takes place, and will be destroyed when the first transaction is committed.

The `JMSContext` object used by the third and fourth calls to  `context.send()` will be created when the third call to `context.send()` takes place, and will be destroyed when the second transaction is committed.

## Use case H. A bean which uses a context both outside and within a transaction

Consider a stateless session bean `Bean1`. This is configured to use **bean**-managed transactions and has one business method, `method1`. The bean has an injected `JMSContext`. `method1` does not start a transaction and uses the `context` variable to send two messages. It then starts a transaction and uses the `context` variable to send a third message. It then commits the transaction and uses the `context` variable  to send a fourth and fifth more messages.

A remote client obtains a reference to `Bean1` and calls `method1`.
```
@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
public class Bean1 {
 
  @Resource(lookup="jms/inboundQueue") Queue queue;
 
  @Inject
  @JMSConnectionFactory("jms/connectionFactory")
  JMSContext context;
 
  @Inject UserTransaction ut;
 
  public void method1() {
    context.send(queue,"Message 1");
    context.send(queue,"Message 2");
    ut.begin();
    context.send(queue,"Message 3");
    ut.commit();
    context.send(queue,"Message 4");
    context.send(queue,"Message 5");
  }
}
```

### Case H: Analysis

Q | A
:--- | :---
Are the `context` variables in the five calls to `context.send()`  injected using identical annotations? | Yes, since they  use the same variable declaration.
What scope do the `context` variables in the five calls to `context.send()` have? | The first, second, fourth and fifth calls to `context.send()` take place without a transaction, so these `context` variables have **request** scope. The third call to `context.send()` take place within a transaction, and so this `context` variable has **transaction** scope.
Are the `context` variables in the five calls to `context.send()` in the same scope? | The first, second, fourth and fifth calls to `context.send()` take place in the same request and so these `context` variables have the same request scope. In addition the  `context` variable used in the third call to to `context.send()` has a completely separate transaction scope.
Do the `context` variables in the five calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | All five `context` variables  are injected using identical annotatons. The `context` variables in the first, second, fourth and fifth calls are in the same request scope and so share the same `JMSContext` (and therefore `MessageProducer`) object.  The `context` variable in the third call is in a separate transaction scope and so uses a different `JMSContext` (and therefore `MessageProducer`) object. 
Are the five messages guaranteed to be delivered in the order in which they are sent? | The first, second, fourth and fifth messages share one `MessageProducer` object and are delivered in order. However the third message uses a different `MessageProducer` object and so there is no guarantee that the third message will be delivered between the second and fourth messages.

### Case H: JMSContext lifecycle

The `JMSContext` object used by the first, second, fourth and fifth calls to   `context.send()` will be created when the first call to  `context.send()` takes place, and will be destroyed when the method returns. 

The  `JMSContext` object used by the third call to   `context.send()` will be created when the third call to  `context.send()` takes place, and will be destroyed when the transaction is committed

_Important note_: Although JMS does not define what happens when the same `JMSContext` object is used both within and outside a transaction, this ambiguity is avoided when the `JMSContext` object is injected because since the two cases have separate scopes and so different `JMSContext` objects are always used.

## Use case J. Two separate container-managed transactions on the same thread, one suspended before the second is started 

Consider two stateless session beans, `Bean1` and `Bean2`. 

`Bean1` is configured to use container-managed transactions and has a business method `method1`, which is configured to require a transaction. The bean has an injected `JMSContext`. `method1` uses this context to send a message and then invokes `method2` on `bean1`. It then sends a further message.

`Bean2` is also configured to use container-managed transactions and has a business method `method2`, which is configured to require a **new** transaction. The bean has an injected `JMSContext`. `method2` simply uses this context to send a message.

A remote client obtains a reference to `Bean1` and calls `method1`.

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
    context.send(queue,"Message 3");
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
 
  @TransactionAttribute(REQUIRES_NEW)
    public void method2() {
    context.send(queue,"Message 2");
  }
}
```

### Case J: Analysis

Q | A
:--- | :---
Are the `context` variables in the three calls to `context.send()`  injected using identical annotations? | Yes. Although the `context` variable used for the first and third calls has a different declaration from the `context` variable used for the second call, both declarations have identical annotations.
What scope do the `context` variables in the three calls to `context.send()` have? | All three calls to `context.send()` take place within transactions, so both `context` variables have **transaction** scope.
Are the `context` variables in the three calls to `context.send()` in the same scope? | The first and third calls to `context.send()` take place in one transaction whereas the second call to to `context.send()` take place in a separate transaction. This means the `context` variables used in the first and third calls are in one transaction scope, and the `context` variable used in the second call is in a separate transaction scope.
Do the `context` variables in the three calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | All three `context` variables  are injected using identical annotatons. The `context` variables in the first and third calls are in the same transaction scope and so share the same  `JMSContext` (and therefore `MessageProducer`) object. The `context` variable in the second call is in a different transaction scope uses a different  `JMSContext` (and therefore `MessageProducer`) object. 
Are the three messages guaranteed to be delivered in the order in which they are sent? | The first and third messages are sent using the same `MessageProducer` objects and are guaranteed to be delivered in order. However the second message is sent using a different `MessageProducer` object and so there is no guarantee that it will be delivered in between the first and third messages.

### Case J: JMSContext lifecycle

The `JMSContext` object used by `method1` will be created when `method1` uses `context` for the first time, and destroyed when the first transaction is committed, which will be when `method1` returns.

The `JMSContext` object used by `method2` will be created when `method2` uses `context` for the first time, and destroyed when the second transaction is committed, which will be when `method2` returns.

## Use case K. One bean which calls another within the same transaction, but using different connection factories

(Note that this use case is identical to [http://java.net/projects/jms-spec/pages/JMSContextScopeProposals2#Use_case_C._One_bean_which_calls_another_within_the_same_transaction use case C] except that the two beans specify different connection factories).

Consider two stateless session beans, `Bean1` and `Bean2`

`Bean1` is configured to use container-managed transactions and has a business method `method1`, which is configured to require a transaction. The bean has an injected `JMSContext`. `method1` uses this context to send a message and then invokes `method2` on `Bean2`.

`Bean2` is also configured to use container-managed transactions and has a business method `method2`, which is also configured to require a transaction. The bean also has an injected `JMSContext` to `Bean1`, but specifies a different connection factory. `method2` simply uses this context to send a second message.

A remote client obtains a reference to `Bean1` and calls `method1`

This is `Bean1`
```
 @TransactionManagement(TransactionManagementType.CONTAINER) 
 @Stateless
 public class Bean1 {
 
    @Resource(lookup="jms/inboundQueue") Queue queue;
 
    @Inject
    @JMSConnectionFactory("jms/connectionFactory1")
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
  @JMSConnectionFactory("jms/connectionFactory2")
  JMSContext context;
 
  @TransactionAttribute(REQUIRED)
  public void method2() {
    context.send(queue,"Message 2");
  }
}
```

### Case K: Analysis

Q | A
:--- | :---
Are the `context` variables in the two calls to `context.send()`  injected using identical annotations? | No. The two  `context` variables have different declarations which use different annotations: the declaration in `Bean1` uses the annotation `@JMSConnectionFactory("jms/connectionFactory1")` whilst the declaration in `Bean2` uses the annotation `@JMSConnectionFactory("jms/connectionFactory2")`, which is different
What scope do the `context` variables in the two calls to `context.send()` have? | Both  calls to `context.send()` take place within a transaction, so they both have transaction scope.
Are the `context` variables in the two calls to `context.send()` in the same scope? | Yes, since the two calls to `context.send()` take place in the same transaction
Do the `context` variables in the two calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | No. Although they are in the same transaction scope they are injected using different annotations.
Are the two messages guaranteed to be delivered in the order in which they are sent? | No, since they are sent using different `MessageProducer` objects.

### Case K: JMSContext lifecycle

The `JMSContext` object used by `method1` will be created when  `method1` uses `context` for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

The `JMSContext` object used by `method2` will be created when  `method2` uses `context` for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

_Note how specifying different connection factories means that the same injected `JMSContext` object cannot be used._