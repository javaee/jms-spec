# Injection of JMSContext objects - Use Cases F - K  (version 3)

## Summary 

This page contains a number of use cases which demonstrate how the proposals in [Injection of JMSContext objects - Proposals (version 3)](/jms-spec/pages/JMSContextScopeProposals) would appear to users. Each use case is followed by an analysis for both [Option 2](/jms-spec/pages/JMSContextScopeProposals#option-2) and [Option 3](/jms-spec/pages/JMSContextScopeProposals#option-3). 

Note that these use cases are not intended to demonstrate how `@TransactionScoped` beans behave in general. They are intended only to demonstrate how injected `JMSContext` objects behave.

Before reading these, read [Injection of JMSContext objects - Use Cases A-E (version 3)](/jms-spec/pages/JMSContextScopeProposals2)

## Contents

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
 
  public void method2() {
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

### Case F, option 2: Analysis

Q | A
:--- | :---
Do the `context` variables in the two calls to `context.send()`  use the same injection point? | No, since they are in different beans and so use different `context` variables.
Are the `context` variables in each call to `context.send()` in the same `@TransactionScope`? | No. There is no transaction, and the two calls to `context.send()` take place in different methods.
Do the `context` variables in each call to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | No, since they use different injection points and are not in the same `@TransactionScope`.
 Are the two messages guaranteed to be delivered in the order in which they are sent? | No, since they are sent using different `MessageProducer` objects.

### Case F, option 2: JMSContext lifecycle

The `JMSContext` object used by `method1` will be created when `method1` uses `context` for the first time, and destroyed when that method returns.

The `JMSContext` object used by `method2` will be created when `method2` uses `context` for the first time, and destroyed when that method returns.

The same behaviour should apply when the bean is configured to use container-managed transactions but the transaction attribute type is `NEVER` or `NOT_SUPPORTED`. 

### Case F, option 3: Analysis

Q | A
:--- | :---
Are the `context` variables in the two calls to `context.send()`  injected using identical annotations? | Yes. Although they are in different beans and so use different `context` variables, they are injected using identical annotations
 Are the `context` variables in each call to `context.send()` in the same `@TransactionScope`? | No. There is no transaction, and the two calls to `context.send()` take place in different methods.
 Do the `context` variables in each call to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | No. Although they are injected using identical annotations they are not in the same `@TransactionScope`.
Are the two messages guaranteed to be delivered in the order in which they are sent? | No, since they are sent using different `MessageProducer` objects.

### Case F, option 3: JMSContext lifecycle

The `JMSContext` object used by `method1` will be created when `method1` uses `context` for the first time, and destroyed when that method returns.

The `JMSContext` object used by `method2` will be created when `method2` uses `context` for the first time, and destroyed when that method returns.

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
    ut.commit()
  }
 
}
```

### Case G, option 2: Analysis

Q | A
:--- | :---
Do the `context` variables in the four calls to `context.send()`  use the same injection point? | Yes, since they  all use the same `context` variable.
Are the `context` variables in the four calls to `context.send()` in the same `@TransactionScope`? | Yes. Although the first two and second two calls to `context.send()` take place in different transactions, all four calls take place in the same method. (When the first transaction is committed the context remains in scope since the method has not yet completed. When the third and fourth message sends are performed it uses the context that is already in scope.)
Do the `context` variables in the four calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they use the same injection point and are in the same `@TransactionScope`
Are the four messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

### Case G, option 2: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses the `context` variable for the first time, and destroyed when `method1` returns.

### Case G, option 3: Analysis

Q | A
:--- | :---
Are the `context` variables in the four calls to `context.send()`  injected using identical annotations? | Yes, since they  all use the same `context` variable.
Are the `context` variables in the four calls to `context.send()` in the same `@TransactionScope`? | Yes. Although the first two and second two calls to `context.send()` take place in different transactions, all four calls take place in the same method. (When the first transaction is committed the context remains in scope since the method has not yet completed. When the third and fourth message sends are performed it uses the context that is already in scope.)
Do the `context` variables in the four calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they are injected using identical annotations and are in the same `@TransactionScope`
Are the four messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

### Case G, option 3: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses the `context` variable for the first time, and destroyed when `method1` returns.

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

### Case H, option 2: Analysis

Q | A
:--- | :---
Do the `context` variables in the five calls to `context.send()`  use the same injection point? | Yes, since they  all use the same `context` variable.
Are the `context` variables in the five calls to `context.send()` in the same `@TransactionScope`? | Yes. Although the first two and last two calls to `context.send()` take place in different transactions, and the third call takes place without a transaction, all five calls take place in the same method.
Do the `context` variables in the five calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they use the same injection point and are in the same `@TransactionScope`
Are the five messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

### Case H, option 2: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses the `context` variable for the first time, and destroyed when `method1` returns.

### Case H, option 3: Analysis

Q | A
:--- | :---
Are the `context` variables in the five calls to `context.send()`  injected using identical annotations? | Yes, since they  all use the same `context` variable.
Are the `context` variables in the five calls to `context.send()` in the same `@TransactionScope`? | Yes. Although the first two and last two calls to `context.send()` take place in different transactions, and the third call takes place without a transaction, all five calls take place in the same method.
Do the `context` variables in the five calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | Yes, since they are injected using identical annotations and are in the same `@TransactionScope`
Are the five messages guaranteed to be delivered in the order in which they are sent? | Yes, since they are sent using the same `MessageProducer` object.

### Case H, option 3: JMSContext lifecycle

The `JMSContext` object will be created when `method1` uses the `context` variable for the first time, and destroyed when `method1` returns.

_Important note (for both options)_: Although the scope of the injected `JMSContext` object is completely defined in this example, the JMS behaviour of the  `JMSContext` object itself is not, since JMS does not define what happens if a `JMSContext` object is created outside a transaction and then used with one. It is probably desirable to clarify the JMS behaviour in this case. However, exactly the same question would arise of the `JMSContext` was instantiated from a connection factory rather than injected, so this is a JMS issue, not a CDI or injection-related issue.

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

### Case J, option 2: Analysis

Q | A
:--- | :---
Do the `context` variables in the three calls to `context.send()`  use the same injection point?
| The first and third calls use the same injection point, since they use the same `context` variable in `Bean1`. The second call uses a different injection point since it uses a different variable in `Bean2`.
Are the `context` variables in the three calls to `context.send()` in the same `@TransactionScope`? | The first and third calls to `context.send()` take place in the same transaction, so are in the same `@TransactionScope`. However the third call takes place in a different transaction, so is in a different `@TransactionScope`.
Do the `context` variables in the three calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | The first and third calls to `context.send()` use the same `JMSContext` since they use the same same injection point and are in the same `@TransactionScope`. The second call to `context.send()` use a different `JMSContext` since it uses both a different injection point and is in a different `@TransactionScope`.
Are the three messages guaranteed to be delivered in the order in which they are sent? | The first and third messages are guaranteed to be delivered in the order in which they are sent, since they are sent using the same `MessageProducer` object, but the second message is not, since it is sent using a different `MessageProducer` object.

### Case J, option 2: JMSContext lifecycle

The `JMSContext`object used by `method1` will be created when `method1` uses context for the first time, and destroyed when that method returns.

The `JMSContext`object used by `method2` will be created when `method2` uses context for the first time, and destroyed when that method returns. 

### Case J, option 3: Analysis

Q | A
:--- | :---
Are the `context` variables in the three calls to `context.send()`  injected using identical annotations? | Yes. The first and third calls use the same `context` variable in `Bean1`. The second call uses a different variable in `Bean2`, but it is injected using identical annotations to the variable used in `Bean1`.
Are the `context` variables in the three calls to `context.send()` in the same `@TransactionScope`? | The first and third calls to `context.send()` take place in the same transaction, so are in the same `@TransactionScope`. However the third call takes place in a different transaction, so is in a different `@TransactionScope`.
Do the `context` variables in the three calls to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | The first and third calls to `context.send()` (in `Bean1`) use the same `JMSContext` since they are injected using identical annotations and are in the same `@TransactionScope`. <br/>The second call to `context.send()` use a different `JMSContext` because although it is injected using identical annotations it is in a different `@TransactionScope`.
Are the three messages guaranteed to be delivered in the order in which they are sent? | The first and third messages are guaranteed to be delivered in the order in which they are sent, since they are sent using the same `MessageProducer` object, but the second message is not, since it is sent using a different `MessageProducer` object.

### Case J, option 3: JMSContext lifecycle 

The `JMSContext`object used by `method1` will be created when `method1` uses context for the first time, and destroyed when that method returns.

The `JMSContext`object used by `method2` will be created when `method2` uses context for the first time, and destroyed when that method returns. 

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

### Case K, option 2: Analysis

Q | A
:--- | :---
Do the `context` variables in the two calls to `context.send()`  use the same injection point?
| No, since they are in different beans and so use different `context` variables.
Are the `context` variables in each call to `context.send()` in the same `@TransactionScope`? | Yes, since the two calls to `context.send()` take place in the same transaction.
Do the `context` variables in each call to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | No. Although they are in the same `@TransactionScope`  they use different injection points. 
Are the two messages guaranteed to be delivered in the order in which they are sent? | No, since they are sent using different `MessageProducer` objects.

### Case K, option 2: JMSContext lifecycle

The `JMSContext` object used by `method1` will be created when  `method1` uses `context` for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

The `JMSContext` object used by `method2` will be created when  `method2` uses `context` for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

### Case K, option 3: Analysis

Q | A
:--- | :---
Are the `context` variables in the two calls to `context.send()`  injected using identical annotations? | No. They are in different beans and so use different `context` variables, which are injected using different connection factories.
Are the `context` variables in each call to `context.send()` in the same `@TransactionScope`? | Yes, since the two calls to `context.send()` take place in the same transaction.
Do the `context` variables in each call to `context.send()` use the same `JMSContext` (and therefore `MessageProducer`) objects? | No, although they are in the same `@TransactionScope` they are injected using different annotations.
Are the two messages guaranteed to be delivered in the order in which they are sent? | No, since they are sent using different `MessageProducer` objects.

### Case K, option 3: JMSContext lifecycle

The `JMSContext` object used by `method1` will be created when  `method1` uses `context` for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

The `JMSContext` object used by `method2` will be created when  `method2` uses `context` for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

_Note how specifying different connection factories means that the same injected `JMSContext` object cannot be used.