# Injection of JMSContext objects - Use Cases A - E (version 4)

## Summary 

This page contains a number of use cases which demonstrate how the scope proposed in [[JMSContextScopeProposalsv4p1|Injection of JMSContext objects - Proposals (version 4)]] would appear to users. Each use case is followed by an analysis. 

After reading these, now read [[JMSContextScopeProposalsv4p3|Injection of JMSContext objects - Use Cases F-K (version 4)]]

Note that these examples do **not** use the proposed new <tt>JMSContext</tt> API for sending messages described in [[JMSContextScopeProposalsv4p4|Proposed changes to JMSContext to support injection (Option 4)]]

__TOC__

## Use cases 

### Use case A: Two methods on the same bean within separate transactions

Consider a stateless session bean configured to use container-managed transactions with two business methods. Each method is configured to require a transaction. The bean has an injected <tt>JMSContext</tt>. Each method uses the context to send a message.  

A remote client obtains a reference to <tt>Bean1</tt> and calls the methods <tt>method1a</tt> and <tt>method1b</tt> in turn. 
<br/><br/>
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

#### =Case A: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they  use the same variable declaration.
|- valign="top"
| What scope do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> have?
| Both  calls to <tt>context.send()</tt> take place within a transaction, so they both have transaction scope.
|- valign="top"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> in the same scope?
| No, since the two calls to <tt>context.send()</tt> take place in different transactions
|- valign="top"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| No. Although they are injected using identical annotatons they are not in the same transaction and so have separate transaction scopes.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| No, since they are sent using different <tt>MessageProducer</tt> objects.
|} 

**Important note:** Note that there is no guarantee that the same bean instance is used for both method invocations. Stateless session bean instances might be pooled, or new stateless session bean instances might be created, but in either case there is no guarantee that the same instance is reused for a client. However, even if the method invocations are serviced by different stateless session bean instances this does not affect whether or not the same <tt>JMSContext</tt> object is used. 

#### =Case A: JMSContext lifecycle=

The <tt>JMSContext</tt> object used by <tt>method1a</tt> will be created when <tt>method1a</tt> uses <tt>context</tt> for the first time, and destroyed when the first transaction is committed.

The <tt>JMSContext</tt> object used by <tt>method1b</tt> will be created when <tt>method1b</tt> uses <tt>context</tt> for the first time, and destroyed when the second transaction is committed.

### Use case B: Two methods on the same bean within the same transaction

Consider two stateless session beans, <tt>Bean1</tt> and <tt>Bean2</tt>. 

<tt>Bean1</tt> is configured to use container managed transactions and has one business method <tt>method1</tt>, which is configured to require a transaction. This invokes <tt>method2a</tt> and <tt>method2b</tt> on <tt>Bean2</tt> in turn.

<tt>Bean2</tt> is also configured to use container managed transactions and has two business methods  <tt>method2a</tt> and <tt>method2b</tt> which are configured to require a transaction. The bean has an injected <tt>JMSContext</tt>. Each method uses the context to send a message.

A remote client obtains a reference to <tt>Bean1</tt> and calls <tt>method1</tt>

<br/>
This is <tt>Bean1</tt>:

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
This is <tt>Bean2</tt>:

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

#### =Case B: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they  use the same variable declaration.
|- valign="top"
| What scope do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> have?
| Both  calls to <tt>context.send()</tt> take place within a transaction, so they both have transaction scope.
|- valign="top"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> in the same scope?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction
|- valign="top"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes. They are injected using identical annotatons and have same transaction scope.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case B: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method2a</tt> uses the <tt>context</tt> variable for the first time, and destroyed when when the transaction is committed, which will occur after <tt>method1</tt> returns.


### Use case C. One bean which calls another within the same transaction

Consider two stateless session beans, <tt>Bean1</tt> and <tt>Bean2</tt>

<tt>Bean1</tt> is configured to use container-managed transactions and has a business method <tt>method1</tt>, which is configured to require a transaction. The bean has an injected <tt>JMSContext</tt>. <tt>method1</tt> uses this context to send a message and then invokes <tt>method2</tt> on <tt>Bean2</tt>.

<tt>Bean2</tt> is also configured to use container-managed transactions and has a business method <tt>method2</tt>, which is also configured to require a transaction. The bean also has an injected <tt>JMSContext</tt> with identical annotations to <tt>Bean1</tt>. <tt>method2</tt> simply uses this context to send a second message.

A remote client obtains a reference to <tt>Bean1</tt> and calls <tt>method1</tt>

<br/>
This is <tt>Bean1</tt>

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
This is <tt>Bean2</tt>

 
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

#### =Case C: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes. Although they use separate variable declarations, both declarations use identical annotations.
|- valign="top"
| What scope do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> have?
| Both  calls to <tt>context.send()</tt> take place within a transaction, so they both have transaction scope.
|- valign="top"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> in the same scope?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction
|- valign="top"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes. They are injected using identical annotations and have the same transaction scope.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case C: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when  <tt>method1</tt> uses <tt>context</tt> for the first time, and destroyed when the container commits the transaction, which will be after <tt>method1</tt> returns.

### Use case D. One bean which sends two messages within the same transaction

Consider a stateless session bean <tt>Bean1</tt>. This is configured to use container-managed transactions and has one business method, <tt>method1</tt>, which is configured to require a transaction. The bean has an injected <tt>JMSContext</tt> . <tt>method1</tt> uses the context to send two messages.

A remote client obtains a reference to <tt>Bean1</tt> and calls <tt>method1</tt>.
<br/><br/>
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

#### =Case D: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they they use the same variable declaration.
|- valign="top"
| What scope do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> have?
| Both  calls to <tt>context.send()</tt> take place within a transaction, so they both have transaction scope.
|- valign="top"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> in the same scope?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction
|- valign="top"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes. They are injected using identical annotations and have the same transaction scope.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case D: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses the <tt>context</tt> variable for the first time, to send the first message. It will be destroyed when the transaction is committed.

### Use case E. One bean which sends two messages when there is no transaction

Consider a stateless session bean <tt>Bean1</tt>. This is configured to use **bean**-managed transactions and has one business method, <tt>method1</tt>. The bean has an injected <tt>JMSContext</tt>.  <tt>method1</tt> does not start a transaction and uses the context to send two messages.

A remote client obtains a reference to <tt>Bean1</tt> and calls <tt>method1</tt>.
<br/><br/>
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

#### =Case E: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they they use the same variable declaration.
|- valign="top"
| What scope do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> have?
| Both  calls to <tt>context.send()</tt> take place when there is no transaction, so they both have **request** scope.
|- valign="top"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> in the same scope?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same request
|- valign="top"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes. They are injected using identical annotations and have the same request scope.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case E: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses the <tt>context</tt> variable for the first time, to send the first message. It will be destroyed when <tt>method1</tt> returns.

Note that it is entirely valid to create and use a <tt>JMSContext</tt> object when there is no transaction. The context is non-transacted. JMS message ordering rules apply irrespective of whether the session is non-transacted, local-transacted or uses a JTA transaction.

The same behaviour would apply when the bean is configured to use container-managed transactions but the transaction attribute type is <tt>NEVER</tt> or <tt>NOT_SUPPORTED</tt>.

After reading these, now read [[JMSContextScopeProposalsv4p3|Injection of JMSContext objects - Use Cases F-K (version 4)]]
