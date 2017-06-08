# Injection of JMSContext objects - Proposals (version 4)
{: .no_toc}

This page discusses that part of the JMS 2.0 Early Draft which defines how `javax.jms.JMSContext` objects may be injected.   In particular it discusses the scope and lifecycle of injected `JMSContext` objects. 

  * The JMS 2.0 Early Draft** proposed that injected `JMSContext` objects will "have request scope and will be automatically closed when the request ends. However, unlike a normal CDI request-scoped object, a separate JMSContext instance will be injected for every injection point." This proposal will be referred to here as **Option 1**.

  * Two further proposals** were made which both envisaged a new scope called `@TransactionScoped` which would have the scope of a transaction, but extended to the start and end of the method. In [Option 2](/jms-spec/pages/JMSContextScopeProposals#option-2) it was proposed that a separate JMSContext instance would still be injected for every injection point. [Option 3](/jms-spec/pages/JMSContextScopeProposals#option-3) offered a variant of this: the requirement that a separate JMSContext instance would be injected for every injection point was dropped but instead a requirement that the injected JMSContext's six state properties would be given dependent scope.

  * This page now describes a fourth option**, Option 4. This proposals new a new definition of the scope of an injected `JMSContext`.  It should be read in conjunction with some new  [Proposed changes to JMSContext to support injection (Option 4)](/jms-spec/pages/JMSContextScopeProposalsv4p4).

The scope described here is illustrated in a series of use cases:  [Injection of JMSContext objects - Use Cases A - E (version 4)](/jms-spec/pages/JMSContextScopeProposalsv4p2) and [Injection of JMSContext objects - Use Cases F - K (version 4)](/jms-spec/pages/JMSContextScopeProposalsv4p3).

## Contents
{: .no_toc}

* auto-gen TOC:
{:toc}

## Proposal in JMS 2.0 Early Draft (Option 1) 

The JMS 2.0 early draft (section 11.3) specified that applications may declare a field of type `javax.jms.JMSContext` and annotate it with the `javax.inject.Inject` annotation as follows:

 @Inject JMSContext context;

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

## New proposals for JMS 2.0 Public Draft (Option 4) 

### Scope of injected JMSContext objects
In this proposal, the scope of an injected JMSContext object will depend on whether it is used in a transaction. Whereas  [Option 2](/jms-spec/pages/JMSContextScopeProposals#option-2) and  [Option 3](/jms-spec/pages/JMSContextScopeProposals#option-3) proposed a single scope which was used irrespective of whether there was a transaction or not, this option proposes that the scope used depends on whether the injected JMSContext is used in a transaction or not.

* If an injected JMSContext is used in a JTA transaction (both bean-managed and container-managed), its scope will be that of the transaction. This means that:
  * The JMSContext object will be automatically created the first time it is used within the transaction.
  * The JMSContext object will be automatically closed when the transaction is committed.
  * If, within the same JTA transaction, different beans, or different methods within the same bean, use an injected JMSContext which is injected using identical annotations then they will all share the same JMSContext object.
* If an injected JMSContext is used when there is no JTA transaction then its scope will be the existing CDI scope @RequestScoped. This means that:
  * The JMSContext object will be created the first time it is used within a request.
  * The JMSContext object will be closed when the request ends.
  * If, within the same request, different beans, or different methods within the same bean, use an injected JMSContext which is injected using identical annotations then they will all share the same JMSContext object.
* If injected JMSContext is used both in a JTA transaction and outside a JTA transaction then separate JMSContext objects will be used, with a separate JMSContext object being used for each JTA transaction as described above.

### Modified JMSContext API

If, within the same scope, different beans, or different methods within the same bean, use an injected JMSContext which is injected using identical annotations then they will all share the same JMSContext object.

Although this is normal CDI behaviour this is potentially confusing to developers because it means that changing a property of a JMSContext in one bean would change that property in a JMSContext in a different bean, despite different variables being used.  

To avoid this potential confusion,  some major changes are proposed to the `JMSContext` API for sending messages. These are described in  [Proposed changes to JMSContext to support injection (Option 4)](/jms-spec/pages/JMSContextScopeProposalsv4p4).

## Use cases 

After reading this, please read  [Injection of JMSContext objects - Use Cases A - E (version 4)](/jms-spec/pages/JMSContextScopeProposalsv4p2) and [Injection of JMSContext objects - Use Cases F - K (version 4)](/jms-spec/pages/JMSContextScopeProposalsv4p3).

