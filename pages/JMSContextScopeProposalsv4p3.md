# Injection of JMSContext objects - Use Cases F - K  (version 4)</h1>

==Summary==

This page contains a number of use cases which demonstrate how the scope proposed in [[JMSContextScopeProposalsv4p1|Injection of JMSContext objects - Proposals (version 4)]] would appear to users. Each use case is followed by an analysis. 

Before reading these, read [[JMSContextScopeProposalsv4p2|Injection of JMSContext objects - Use Cases A-E (version 4)]]

Note that these examples do **not** use the proposed new <tt>JMSContext</tt> API for sending messages described in [[JMSContextScopeProposalsv4p4|Proposed changes to JMSContext to support injection (Option 4)]]

__TOC__

==Use cases==

===Use case F: One bean which calls another when there is no transaction===

Consider two stateless session beans, <tt>Bean1</tt> and <tt>Bean2</tt>.

<tt>Bean1</tt> is configured to use bean-managed transactions and has a business method <tt>method1</tt>. The bean has an injected <tt>JMSContext</tt>. <tt>method1</tt> does not start a transaction, uses this context to send a message, and then invoke <tt>method2</tt> on <tt>Bean2</tt>.

<tt>Bean2</tt> is also configured to use bean-managed transactions and has a business method <tt>method2</tt>. The bean has an injected <tt>JMSContext</tt>. <tt>method2</tt> does not start a transaction and simply uses this context to send a second message.

A remote client obtains a reference to <tt>Bean1</tt> and calls <tt>method1</tt>.

<br/>
This is <tt>Bean1</tt>

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

<br/>
This is <tt>Bean2</tt>
 
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

=====Case F: Analysis=====

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes. Although they  use separate variable declarations, both declarations use identical annotations.
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

=====Case F: JMSContext lifecycle=====

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses <tt>context</tt> for the first time, and destroyed when method1 returns.

The same behaviour should apply when the bean is configured to use container-managed transactions but the transaction attribute type is <tt>NEVER</tt> or <tt>NOT_SUPPORTED</tt>. 

=== Use case G: One bean method which uses two transactions===

Consider a stateless session bean <tt>Bean1</tt>. This is configured to use **bean**-managed transactions and has one business method, <tt>method1</tt>. The bean has an injected <tt>JMSContext</tt>. <tt>method1</tt> starts a transaction and uses the context to send two messages. It then commits the transaction and starts a second transaction. It then uses the context to send two further messages and finally commits the second transaction.

A remote client obtains a reference to <tt>Bean1</tt> and calls <tt>method1</tt>.
<br/><br/>
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

=====Case G: Analysis=====

{|- border="1"
| Are the <tt>context</tt> variables in the four calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they  use the same variable declaration.
|- valign="top"
| What scope do the <tt>context</tt> variables in the four calls to <tt>context.send()</tt> have?
| All four calls to <tt>context.send()</tt> take place within a transaction, so the <tt>context</tt> variables all have transaction scope.
|- valign="top"
| Are the <tt>context</tt> variables in the four calls to <tt>context.send()</tt> in the same scope?
| The first and second calls to <tt>context.send()</tt> take place in one transaction and the third and fourth calls  to <tt>context.send()</tt> take place in another transaction. This means the <tt>context</tt> variables in the first and second calls are in one transaction scope, and the <tt>context</tt> variables in the third and fourth calls are in a different transaction scope.
|- valign="top"
| Do the <tt>context</tt> variables in the four calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| All four  <tt>context</tt> variables  are injected using identical annotatons. The <tt>context</tt> variables in the first and second calls are in the same transaction scope and so share the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) object.  The <tt>context</tt> variables in the third and fourth calls are in a different transaction scope and so share a different <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) object. 
|- valign="top"
| Are the four messages guaranteed to be delivered in the order in which they are sent?
| The first and second messages share one <tt>MessageProducer</tt> object and are delivered in order, and the third and fourth messages  share a different <tt>MessageProducer</tt> object and are delivered in order. However there is no guarantee that the first and second messages will be delivered before the third and fourth messages.
|} 

=====Case G: JMSContext lifecycle=====

The <tt>JMSContext</tt> object used by the first and second calls to  <tt>context.send()</tt> will be created when the first call to <tt>context.send()</tt> takes place, and will be destroyed when the first transaction is committed.

The <tt>JMSContext</tt> object used by the third and fourth calls to  <tt>context.send()</tt> will be created when the third call to <tt>context.send()</tt> takes place, and will be destroyed when the second transaction is committed.

===Use case H. A bean which uses a context both outside and within a transaction===

Consider a stateless session bean <tt>Bean1</tt>. This is configured to use **bean**-managed transactions and has one business method, <tt>method1</tt>. The bean has an injected <tt>JMSContext</tt>. <tt>method1</tt> does not start a transaction and uses the <tt>context</tt> variable to send two messages. It then starts a transaction and uses the <tt>context</tt> variable to send a third message. It then commits the transaction and uses the <tt>context</tt> variable  to send a fourth and fifth more messages.

A remote client obtains a reference to <tt>Bean1</tt> and calls <tt>method1</tt>.
<br/><br/>
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

=====Case H: Analysis=====

{|- border="1"
| Are the <tt>context</tt> variables in the five calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they  use the same variable declaration.
|- valign="top"
| What scope do the <tt>context</tt> variables in the five calls to <tt>context.send()</tt> have?
| The first, second, fourth and fifth calls to <tt>context.send()</tt> take place without a transaction, so these <tt>context</tt> variables have **request** scope. The third call to <tt>context.send()</tt> take place within a transaction, and so this <tt>context</tt> variable has **transaction** scope.
|- valign="top"
| Are the <tt>context</tt> variables in the five calls to <tt>context.send()</tt> in the same scope?
| The first, second, fourth and fifth calls to <tt>context.send()</tt> take place in the same request and so these <tt>context</tt> variables have the same request scope.
In addition the  <tt>context</tt> variable used in the third call to to <tt>context.send()</tt> has a completely separate transaction scope.
|- valign="top"
| Do the <tt>context</tt> variables in the five calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| All five <tt>context</tt> variables  are injected using identical annotatons. The <tt>context</tt> variables in the first, second, fourth and fifth calls are in the same request scope and so share the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) object.  The <tt>context</tt> variable in the third call is in a separate transaction scope and so uses a different <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) object. 
|- valign="top"
| Are the five messages guaranteed to be delivered in the order in which they are sent?
| The first, second, fourth and fifth messages share one <tt>MessageProducer</tt> object and are delivered in order. However the third message uses a different <tt>MessageProducer</tt> object and so there is no guarantee that the third message will be delivered between the second and fourth messages.
|} 

=====Case H: JMSContext lifecycle=====

The <tt>JMSContext</tt> object used by the first, second, fourth and fifth calls to   <tt>context.send()</tt> will be created when the first call to  <tt>context.send()</tt> takes place, and will be destroyed when the method returns. 

The  <tt>JMSContext</tt> object used by the third call to   <tt>context.send()</tt> will be created when the third call to  <tt>context.send()</tt> takes place, and will be destroyed when the transaction is committed

''Important note'': Although JMS does not define what happens when the same <tt>JMSContext</tt> object is used both within and outside a transaction, this ambiguity is avoided when the <tt>JMSContext</tt> object is injected because since the two cases have separate scopes and so different <tt>JMSContext</tt> objects are always used.

===Use case J. Two separate container-managed transactions on the same thread, one suspended before the second is started ===

Consider two stateless session beans, <tt>Bean1</tt> and <tt>Bean2</tt>. 

<tt>Bean1</tt> is configured to use container-managed transactions and has a business method <tt>method1</tt>, which is configured to require a transaction. The bean has an injected <tt>JMSContext</tt>. <tt>method1</tt> uses this context to send a message and then invokes <tt>method2</tt> on <tt>bean1</tt>. It then sends a further message.

<tt>Bean2</tt> is also configured to use container-managed transactions and has a business method <tt>method2</tt>, which is configured to require a **new** transaction. The bean has an injected <tt>JMSContext</tt>. <tt>method2</tt> simply uses this context to send a message.

A remote client obtains a reference to <tt>Bean1</tt> and calls <tt>method1</tt>.

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
         context.send(queue,"Message 3");
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
 
     @TransactionAttribute(REQUIRES_NEW)
     public void method2() {
         context.send(queue,"Message 2");
     }
 }

=====Case J: Analysis=====

{|- border="1"
| Are the <tt>context</tt> variables in the three calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes. Although the <tt>context</tt> variable used for the first and third calls has a different declaration from the <tt>context</tt> variable used for the second call, both declarations have identical annotations.
|- valign="top"
| What scope do the <tt>context</tt> variables in the three calls to <tt>context.send()</tt> have?
| All three calls to <tt>context.send()</tt> take place within transactions, so both <tt>context</tt> variables have **transaction** scope.
|- valign="top"
| Are the <tt>context</tt> variables in the three calls to <tt>context.send()</tt> in the same scope?
| The first and third calls to <tt>context.send()</tt> take place in one transaction whereas the second call to to <tt>context.send()</tt> take place in a separate transaction. This means the <tt>context</tt> variables used in the first and third calls are in one transaction scope, and the <tt>context</tt> variable used in the second call is in a separate transaction scope.
|- valign="top"
| Do the <tt>context</tt> variables in the three calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| All three <tt>context</tt> variables  are injected using identical annotatons. The <tt>context</tt> variables in the first and third calls are in the same transaction scope and so share the same  <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) object. The <tt>context</tt> variable in the second call is in a different transaction scope uses a different  <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) object. 
|- valign="top"
| Are the three messages guaranteed to be delivered in the order in which they are sent?
| The first and third messages are sent using the same <tt>MessageProducer</tt> objects and are guaranteed to be delivered in order. However the second message is sent using a different <tt>MessageProducer</tt> object and so there is no guarantee that it will be delivered in between the first and third messages.
|} 

=====Case J: JMSContext lifecycle=====

The <tt>JMSContext</tt> object used by <tt>method1</tt> will be created when <tt>method1</tt> uses <tt>context</tt> for the first time, and destroyed when the first transaction is committed, which will be when <tt>method1</tt> returns.

The <tt>JMSContext</tt> object used by <tt>method2</tt> will be created when <tt>method2</tt> uses <tt>context</tt> for the first time, and destroyed when the second transaction is committed, which will be when <tt>method2</tt> returns.

===Use case K. One bean which calls another within the same transaction, but using different connection factories===

(Note that this use case is identical to [http://java.net/projects/jms-spec/pages/JMSContextScopeProposals2#Use_case_C._One_bean_which_calls_another_within_the_same_transaction use case C] except that the two beans specify different connection factories).

Consider two stateless session beans, <tt>Bean1</tt> and <tt>Bean2</tt>

<tt>Bean1</tt> is configured to use container-managed transactions and has a business method <tt>method1</tt>, which is configured to require a transaction. The bean has an injected <tt>JMSContext</tt>. <tt>method1</tt> uses this context to send a message and then invokes <tt>method2</tt> on <tt>Bean2</tt>.

<tt>Bean2</tt> is also configured to use container-managed transactions and has a business method <tt>method2</tt>, which is also configured to require a transaction. The bean also has an injected <tt>JMSContext</tt> to <tt>Bean1</tt>, but specifies a different connection factory. <tt>method2</tt> simply uses this context to send a second message.

A remote client obtains a reference to <tt>Bean1</tt> and calls <tt>method1</tt>

<br/>
This is <tt>Bean1</tt>

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

<br/>
This is <tt>Bean2</tt>

 
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

=====Case K: Analysis=====

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| No. The two  <tt>context</tt> variables have different declarations which use different annotations: the declaration in <tt>Bean1</tt> uses the annotation <tt>@JMSConnectionFactory("jms/connectionFactory1")</tt> whilst the declaration in <tt>Bean2</tt> uses the annotation <tt>@JMSConnectionFactory("jms/connectionFactory2")</tt>, which is different
|- valign="top"
| What scope do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> have?
| Both  calls to <tt>context.send()</tt> take place within a transaction, so they both have transaction scope.
|- valign="top"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> in the same scope?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction
|- valign="top"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| No. Although they are in the same transaction scope they are injected using different annotations.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| No, since they are sent using different <tt>MessageProducer</tt> objects.
|} 

=====Case K: JMSContext lifecycle=====

The <tt>JMSContext</tt> object used by <tt>method1</tt> will be created when  <tt>method1</tt> uses <tt>context</tt> for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

The <tt>JMSContext</tt> object used by <tt>method2</tt> will be created when  <tt>method2</tt> uses <tt>context</tt> for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

''Note how specifying different connection factories means that the same injected <tt>JMSContext</tt> object cannot be used.