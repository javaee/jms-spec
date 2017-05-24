# Injection of JMSContext objects - Use Cases A - E (version 3)</h1>

## Summary=### 

This page contains a number of use cases which demonstrate how the proposals in [[JMSContextScopeProposals|Injection of JMSContext objects - Proposals (version 3)]] would appear to users. Each use case is followed by an analysis for both [[JMSContextScopeProposals#Option_2|Option 2]] and [[JMSContextScopeProposals#Option_3|Option 3]]. 

If you're looking for a use case which demonstrates the differences between [[JMSContextScopeProposals#Option_2|Option 2]] and [[JMSContextScopeProposals#Option_3|Option 3]], please look at [http://java.net/projects/jms-spec/pages/JMSContextScopeProposals2#Use_case_C._One_bean_which_calls_another_within_the_same_transaction use case C].

Note that these use cases are not intended to demonstrate how <tt>@TransactionScoped</tt> beans behave in general. They are intended only to demonstrate how injected <tt>JMSContext</tt> objects behave.

After reading these, now read [[JMSContextScopeProposals3|Injection of JMSContext objects - Use Cases F-K (version 3)]]

__TOC__

## Use cases=### 

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

#### =Case A, option 2: Analysis=

{|- border="1"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  use the same injection point?
| Yes, since they  use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| No, since the two calls to <tt>context.send()</tt> take place in different transactions
|- valign="top"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| No. Although they use the same injection point they are not in the same <tt>@TransactionScope</tt>
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| No, since they are sent using different <tt>MessageProducer</tt> objects.
|} 

**Important note:** Note however that there is no guarantee that the same bean instance is used for both method invocations. Stateless session bean instances might be pooled, or new stateless session bean instances might be created, but in either case there is no guarantee that the same instance is reused for a client. If the method invocations are serviced by different stateless session bean instances, the answer to the first question above is not necessarily 'Yes'.

#### =Case A, option 2: JMSContext lifecycle=

The <tt>JMSContext</tt> object used by <tt>method1a</tt> will be created when <tt>method1a</tt> uses <tt>context</tt> for the first time, and destroyed when that method returns.

The <tt>JMSContext</tt> object used by <tt>method1b</tt> will be created when <tt>method1b</tt> uses <tt>context</tt> for the first time, and destroyed when that method returns.

#### =Case A, option 3: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they  use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| No, since the two calls to <tt>context.send()</tt> take place in different transactions
|- valign="top"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| No. Although they are injected using identical annotatons they are not in the same <tt>@TransactionScope</tt>
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| No, since they are sent using different <tt>MessageProducer</tt> objects.
|} 

#### =Case A, option 3: JMSContext lifecycle=

The <tt>JMSContext</tt> object used by <tt>method1a</tt> will be created when <tt>method1a</tt> uses <tt>context</tt> for the first time, and destroyed when that method returns.

The <tt>JMSContext</tt> object used by <tt>method1b</tt> will be created when <tt>method1b</tt> uses <tt>context</tt> for the first time, and destroyed when that method returns.

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

#### =Case B, option 2: Analysis=

{|- border="1"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  use the same injection point?
| Yes, since they  use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction.
|- valign="top"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they use the same injection point and are in the same <tt>@TransactionScope</tt>.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

**Important note:** Note however that there is no guarantee that the same bean instance is used for both method invocations. Stateless session bean instances might be pooled, or new stateless session bean instances might be created, but in either case there is no guarantee that the same instance is reused for a client. If the method invocations are serviced by different stateless session bean instances, the answer to the first question above is not necessarily 'Yes'.

#### =Case B, option 2: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method2a</tt> uses the <tt>context</tt> variable for the first time, and destroyed when when the transaction is committed, which will occur after <tt>method1</tt> returns.

#### =Case B, option 3: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they  use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction.
|- valign="top"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they are injected using identical annotations and are in the same <tt>@TransactionScope</tt>.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case B, option 2: JMSContext lifecycle=

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

#### =Case C, option 2: Analysis=

{|- border="1"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  use the same injection point?
| No, since they are in different beans and so use different <tt>context</tt> variables.
|- valign="top"
| Are the <tt>context</tt> variables in each call to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction.
|- valign="top"
| Do the <tt>context</tt> variables in each call to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| No. Although they are in the same <tt>@TransactionScope</tt>  they use different injection points. 
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| No, since they are sent using different <tt>MessageProducer</tt> objects.
|} 

#### =Case C, option 2: JMSContext lifecycle=

The <tt>JMSContext</tt> object used by <tt>method1</tt> will be created when  <tt>method1</tt> uses <tt>context</tt> for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

The <tt>JMSContext</tt> object used by <tt>method2</tt> will be created when  <tt>method2</tt> uses <tt>context</tt> for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

#### =Case C, option 3: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes. Although they are in different beans and so use different <tt>context</tt> variables, both variables are injected using identical annotations.
|- valign="top"
| Are the <tt>context</tt> variables in each call to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction.
|- valign="top"
| Do the <tt>context</tt> variables in each call to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they are injected using identical annotations and are in the same <tt>@TransactionScope</tt>. 
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they use the same <tt>MessageProducer</tt> object.
|} 

#### =Case C, option 3: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when  <tt>method1</tt> uses <tt>context</tt> for the first time, and destroyed when the container commits the transaction, which will be after <tt>method1</tt> returns.

''Note how option 3 uses one <tt>JMSContext</tt> object whilst option 2 uses two.''

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

#### =Case D, option 2: Analysis=

{|- border="1"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  use the same injection point?
| Yes, since they use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in each call to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction and in the same method. Either of these would be sufficient to place them in the same scope.
|- valign="top"
| Do the <tt>context</tt> variables in each call to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they use the same injection point and are in the same <tt>@TransactionScope</tt>.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case D, option 2: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses the <tt>context</tt> variable for the first time, to send the first message. It will be destroyed after <tt>method1</tt> returns.

#### =Case D, option 3: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in each call to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction and in the same method. Either of these would be sufficient to place them in the same scope.
|- valign="top"
| Do the <tt>context</tt> variables in each call to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they are injected using identical annotations and are in the same <tt>@TransactionScope</tt>.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case D, option 3: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses the <tt>context</tt> variable for the first time, to send the first message. It will be destroyed after <tt>method1</tt> returns.

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

#### =Case E, option 2: Analysis=

{|- border="1"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  use the same injection point?
| Yes, since they use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in each call to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes. Although there is no transaction, the two calls to <tt>context.send()</tt> take place in the same method.
|- valign="top"
| Do the <tt>context</tt> variables in each call to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they use the same injection point and are in the same <tt>@TransactionScope</tt>.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case E, option 2: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses the <tt>context</tt> variable for the first time, to send the first message. It will be destroyed when <tt>method1</tt> returns.

Note that it is entirely valid to create and use a <tt>JMSContext</tt> object when there is no transaction. The context is non-transacted. JMS message ordering rules apply irrespective of whether the session is non-transacted, local-transacted or uses a JTA transaction.

The same behaviour would apply when the bean is configured to use container-managed transactions but the transaction attribute type is <tt>NEVER</tt> or <tt>NOT_SUPPORTED</tt>.

#### =Case E, option 3: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in each call to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes. Although there is no transaction, the two calls to <tt>context.send()</tt> take place in the same method.
|- valign="top"
| Do the <tt>context</tt> variables in each call to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they are injected using identical annotations and are in the same <tt>@TransactionScope</tt>.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case E, option 3: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses the <tt>context</tt> variable for the first time, to send the first message. It will be destroyed when <tt>method1</tt> returns.

Note that it is entirely valid to create and use a <tt>JMSContext</tt> object when there is no transaction. The context is non-transacted. JMS message ordering rules apply irrespective of whether the session is non-transacted, local-transacted or uses a JTA transaction.

The same behaviour would apply when the bean is configured to use container-managed transactions but the transaction attribute type is <tt>NEVER</tt> or <tt>NOT_SUPPORTED</tt>.

After reading these, now read [[JMSContextScopeProposals3|Injection of JMSContext objects - Use Cases F-K (version 3)]]
