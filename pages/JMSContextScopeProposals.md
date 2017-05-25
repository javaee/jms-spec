# Injection of JMSContext objects - Proposals (version 3)

## Summary 

This page discusses that part of the JMS 2.0 Early Draft which defines how `javax.jms.JMSContext` objects may be injected.   In particular it discusses the scope and lifecycle of injected `JMSContext` objects. 

The JMS 2.0 Early Draft proposed that injected `JMSContext` objects will "have request scope and will be automatically closed when the request ends. However, unlike a normal CDI request-scoped object, a separate JMSContext instance will be injected for every injection point." This proposal will be referred to here as "Option 1".

This page describes some drawbacks to that proposal and offers two alternatives, [Option 2](/jms-spec/pages/JMSContextScopeProposals#option-2) and  [Option 3](/jms-spec/pages/JMSContextScopeProposals#option-3). 

After reading this, please read  [Injection of JMSContext objects - Use Cases A-E (version 3)](/jms-spec/pages/JMSContextScopeProposals2) and [Injection of JMSContext objects - Use Cases F-K (version 3)](/jms-spec/pages/JMSContextScopeProposals3).

## Contents

* auto-gen TOC:
{:toc}

## Proposal in JMS 2.0 Early Draft (Option 1) 

The JMS 2.0 early draft (section 11.3) specified that applications may declare a field of type `javax.jms.JMSContext` and annotate it with the `javax.inject.Inject` annotation as follows:
```
@Inject JMSContext context;
```
This would cause the container to inject a `JMSContext`. 

Injection would only be supported if the following three conditions were all satisfied:
* the must be running in the Java EE web, EJB or application client containers  
* the type of application was defined as supporting injection in table EE.5-1 "Component classes supporting injection" of the Java EE specification  
* the deployed archive contained a `META-INF/beans.xml` descriptor to enable CDI support

The injected `JMSContext` would have a scope and lifecycle as follows:
* The injected `JMSContext` would have request scope and be automatically closed when the request ends. 
* However, unlike a normal CDI request-scoped object, a separate JMSContext instance would be injected for every injection point.

The connection factory, user, password, session mode and autoStart behaviour, where needed, could  be specified using annotations as follows:
```
@Inject
@JMSConnectionFactory("jms/connectionFactory") 
@JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
@JMSAutoStart(false)
@JMSPasswordCredential(userName="admin",password="mypassword")
private JMSContext context;
```
## Problems with the JMS 2.0 Early Draft proposal 

Since the Early Draft was published there has been a great deal of discussion in the JSR 343 expert group and elsewhere about how `JMSContext` objects should be injected. The following conclusions were drawn:

* The requirement that a separate JMSContext instance would be injected for every injection point was endorsed as being less "surprising"  to users than if the injected object were reused at all injection points within the defined scope. 

* However, although the requirement that the scope of the injected `JMSContext` be linked to the CDI definition of "request" would give reasonable behaviour in most cases, it would prevent the same injected `JMSContext` being used in the case where a transaction spans multiple requests. This would prevent prevent messages sent in those requests being guaranteed to be delivered in order, even though they were being sent in the same transaction.

* In addition, the same request can cross multiple transaction boundaries in case of CMT. As a result, the resource shared across the request can reach inconsistent state if it is used by multiple active and suspended transactions (such as the resource receiving a roll-back request from the currently active transaction and subsequently receiving a commit request from a resumed transactions or vice-versa). It has been suggested that the scope of the injected  `JMSContext` be linked to the transaction instead. Since CDI does not define a "transaction" scope, we should work with the CDI expert group to define one for CDI 1.1. 

There has also been discussion as to whether the annotations for specifying the connection factory, user, password, session mode and autoStart behaviour should be changed. However this amounts to an issue of style more than anything. This document makes no proposals to change this.

## New proposals for JMS 2.0 Public Draft (Options 2 and 3) 

To address the problems with the Early Draft proposal, two possible alternatives are proposed. We call these **option 2** and **option 3**.

###  Option 2 

This option proposes that the JMS 2.0 public draft (section 11.3) should specify that the injected `JMSContext` would have a scope and lifecycle as follows:

* The injected `JMSContext` would have a new CDI scope called "transaction". This scope would end, and the injected `JMSContext` automatically closed, when the transaction ends, or the method ends, whichever comes last. 
* In addition to the functionality offered through the new scope, JMS will ensure that a separate `JMSContext` instance would be injected for every injection point.

A full definition of this new "transaction" scope will be defined in CDI 1.1. 

###  Option 3 

This option is a variant of option 2. Like that option, the injected `JMSContext` would have a new CDI scope called "transaction". This scope would end, and the injected `JMSContext` automatically closed, when the transaction ends, or the method ends, whichever comes last. 

However this option would drop the requirement that a separate `JMSContext` instance be injected for every injection point. Instead, normal CDI scoping behaviour would be used. This means that the same injected `JMSContext` object will be used in all places where a  `JMSContext` is injected within the same transaction, even if this is in different beans, so long as the annotations which defined the injected `JMSContext` object are identical.

This change addresses a limitation of option 2 which is illustrated in [Use case C](/jms-spec/pages/JMSContextScopeProposals2#use-case-c-one-bean-which-calls-another-within-the-same-transaction). Option 2 specifies that, if two separate beans are used to send messages within the same transaction, different `JMSContext` objects will be used in each bean. This means that the messages sent during the transaction may not be delivered in order.  If option 3 is adopted then the same `JMSContext` object would be used, meaning that messages sent using them will be delivered in order.

However by using the same injected `JMSContext` object in different beans there is a possibility that this is confusing to the developer. To the casual reader the injected objects are fields of different beans and are completely separate. It would be counter-intuitive and confusing if, for example calling the JMSContext method `setPriority` (which sets the `JMSContext`'s default priority] in one bean affected the default priority used in another bean.

To avoid possible confusion whilst retaining the advantaged of CD scoping it is therefore proposed that different parts of the `JMSContext`'s state be given different priority:

* The `JMSContext`'s underlying `Connection` object will have transaction scope. This means that the same underlying `Connection` object  will be used in all places where a  `JMSContext` is injected within the same transaction, even if this is in different beans, so long as the annotations which defined the injected `JMSContext` object are identical. Using the same underlying `Connection` object will minimise the number of connections in use at any time.

* The `JMSContext`'s underlying `Session` object will also have transaction scope. This means that the same underlying `Session` objects  will be used in all places where a  `JMSContext` is injected within the same transaction, even if this is in different beans, so long as the annotations which defined the injected `JMSContext` object are identical. Using the same underlying `Session` object will mean that different beans within the same transaction will use the same `XAResource` object, thereby avoiding the need to perform two-phase commits or depend on `XAResource.isSameRM()` returning `true`. 

*  The `JMSContext`'s underlying `MessageProducer` will also have transaction scope, with the exception of its six JavaBean properties (see next). This means that the same underlying `MessageProducer` object  will be used in all places where a  `JMSContext` is injected within the same transaction, even if this is in different beans, so long as the annotations which defined the injected `JMSContext` object are identical. Using the same  `MessageProducer` object when sending messages means that messages will be delivered in the order in which they sent, irrespective of which bean is used to send them.

* However it is proposed that the  six JavaBean properties of the `JMSContext`'s underlying `MessageProducer` will have <i>dependent</i> scope. These are the `deliveryMode`, `priority`, `timeToLive`, `deliveryDelay`, `disableMessageID` and `disableMessageTimestamp` properties. Setting these properties in a particular bean will only affect the  `JMSContext` when it is used in that bean. This means that from the user's perspective the injected `JMSContext` behaves much more like an ordinary field of the bean. 

Since the user doesn't have direct access to the `JMSContext`'s underlying `Connection` and `Session`  objects the fact that these are transaction scoped is just an internal detail which improves performance and scalability and guarantees message ordering without appearing to violate the normal isolation of one bean from another.

## Proposed definition of TransactionScoped for CDI 1.1

Here's Reza's draft proposal to the CDI developer alias (also available in the cdi-dev archives  [here](http://lists.jboss.org/pipermail/cdi-dev/2012-May/001856.html):

<blockquote>
The transaction context is provided by a context object for the built-in scope type @TransactionScoped. When a JTA transaction is available, the transaction scope is active when TransactionManager.begin is called (when the status goes to active), and ends when TransactionManager.commit or TransactionManager.rollback is called. In the absence of a currently active transaction or in case of very short-lived transactions that are begun and committed within a single managed bean method call, the transaction context is active during the managed bean method call and ends when the managed bean method returns. Note that each managed bean method call begins and ends its own separate local context. Note also that most Container-Managed Transactions (CMT) span one or more manage bean method calls. Conversely, most Bean-Manage Transactions (BMT) are begun and committed during a single method call (for the purposes of defining this context, it is assumed that JTA is the underlying transaction API used by both CMT and BMT).
<br/><br/>
If a contextual object is placed into the transaction context while a transaction is active, the object will remain available until the transaction is committed or rolled-back. Such an object will also be propagated throughout the managed bean method invocation context and will remain active even after the transaction is committed if the transaction is committed before the method invocation is finished. If a contextual object is placed into the transaction context without an active transaction, the object will be propagated throughout the method call even if a transaction becomes active. However, if a transaction becomes active, the object will then be propagated throughout the transaction and possibly beyond the method call.
<br/><br/>
The transaction context is shared between all contextual objects involved in the transaction if the context comes into effect when a transaction is active. Transactional contexts that come into effect in complete absence of a transaction are shared between all contextual objects in a single managed bean method call and not beyond. Contextual objects may outlive a transaction if a transaction ends before a managed beam method invocation finishes. Similarly, contextual objects can outlive method invocation boundaries if a transaction spans multiple method calls.
<br/><br/>
If a transaction is suspended, transactional scoped objects are not impacted. It is only the transaction active status or its final state of committed or rolledback that impacts the lifecycle of the transaction context. Similarly, managed bean method call boundaries affect the transaction scope.
</blockquote>

## Use cases 

After reading this, please read  [Injection of JMSContext objects - Use Cases A-E (version 3)](/jms-spec/pages/JMSContextScopeProposals2) and [Injection of JMSContext objects - Use Cases F-K (version 3)](/jms-spec/pages/JMSContextScopeProposals3).
