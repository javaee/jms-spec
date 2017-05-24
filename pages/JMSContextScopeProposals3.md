# Injection of JMSContext objects - Use Cases F - K  (version 3)

## Summary 

This page contains a number of use cases which demonstrate how the proposals in [[JMSContextScopeProposals|Injection of JMSContext objects - Proposals (version 3)]] would appear to users. Each use case is followed by an analysis for both [[JMSContextScopeProposals#Option_2|Option 2]] and [[JMSContextScopeProposals#Option_3|Option 3]]. 

Note that these use cases are not intended to demonstrate how <tt>@TransactionScoped</tt> beans behave in general. They are intended only to demonstrate how injected <tt>JMSContext</tt> objects behave.

Before reading these, read [[JMSContextScopeProposals2|Injection of JMSContext objects - Use Cases A-E (version 3)]]

* auto-gen TOC:
{:toc}

## Use cases 

### Use case F: One bean which calls another when there is no transaction

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
 
    public void method2() {
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

#### =Case F, option 2: Analysis=

{|- border="1"
| Do the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  use the same injection point?
| No, since they are in different beans and so use different <tt>context</tt> variables.
|- valign="top"
| Are the <tt>context</tt> variables in each call to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| No. There is no transaction, and the two calls to <tt>context.send()</tt> take place in different methods.
|- valign="top"
| Do the <tt>context</tt> variables in each call to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| No, since they use different injection points and are not in the same <tt>@TransactionScope</tt>.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| No, since they are sent using different <tt>MessageProducer</tt> objects.
|} 

#### =Case F, option 2: JMSContext lifecycle=

The <tt>JMSContext</tt> object used by <tt>method1</tt> will be created when <tt>method1</tt> uses <tt>context</tt> for the first time, and destroyed when that method returns.

The <tt>JMSContext</tt> object used by <tt>method2</tt> will be created when <tt>method2</tt> uses <tt>context</tt> for the first time, and destroyed when that method returns.

The same behaviour should apply when the bean is configured to use container-managed transactions but the transaction attribute type is <tt>NEVER</tt> or <tt>NOT_SUPPORTED</tt>. 

#### =Case F, option 3: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes. Although they are in different beans and so use different <tt>context</tt> variables, they are injected using identical annotations
|- valign="top"
| Are the <tt>context</tt> variables in each call to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| No. There is no transaction, and the two calls to <tt>context.send()</tt> take place in different methods.
|- valign="top"
| Do the <tt>context</tt> variables in each call to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| No. Although they are injected using identical annotations they are not in the same <tt>@TransactionScope</tt>.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| No, since they are sent using different <tt>MessageProducer</tt> objects.
|} 

#### =Case F, option 3: JMSContext lifecycle=

The <tt>JMSContext</tt> object used by <tt>method1</tt> will be created when <tt>method1</tt> uses <tt>context</tt> for the first time, and destroyed when that method returns.

The <tt>JMSContext</tt> object used by <tt>method2</tt> will be created when <tt>method2</tt> uses <tt>context</tt> for the first time, and destroyed when that method returns.

The same behaviour should apply when the bean is configured to use container-managed transactions but the transaction attribute type is <tt>NEVER</tt> or <tt>NOT_SUPPORTED</tt>. 

###  Use case G: One bean method which uses two transactions

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

#### =Case G, option 2: Analysis=

{|- border="1"
| Do the <tt>context</tt> variables in the four calls to <tt>context.send()</tt>  use the same injection point?
| Yes, since they  all use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in the four calls to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes. Although the first two and second two calls to <tt>context.send()</tt> take place in different transactions, all four calls take place in the same method. <br/>
(When the first transaction is committed the context remains in scope since the method has not yet completed. When the third and fourth message sends are performed it uses the context that is already in scope.)
|- valign="top"
| Do the <tt>context</tt> variables in the four calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they use the same injection point and are in the same <tt>@TransactionScope</tt>
|- valign="top"
| Are the four messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case G, option 2: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses the <tt>context</tt> variable for the first time, and destroyed when <tt>method1</tt> returns.

#### =Case G, option 3: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the four calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they  all use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in the four calls to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes. Although the first two and second two calls to <tt>context.send()</tt> take place in different transactions, all four calls take place in the same method. <br/>
(When the first transaction is committed the context remains in scope since the method has not yet completed. When the third and fourth message sends are performed it uses the context that is already in scope.)
|- valign="top"
| Do the <tt>context</tt> variables in the four calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they are injected using identical annotations and are in the same <tt>@TransactionScope</tt>
|- valign="top"
| Are the four messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case G, option 3: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses the <tt>context</tt> variable for the first time, and destroyed when <tt>method1</tt> returns.

### Use case H. A bean which uses a context both outside and within a transaction

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

#### =Case H, option 2: Analysis=

{|- border="1"
| Do the <tt>context</tt> variables in the five calls to <tt>context.send()</tt>  use the same injection point?
| Yes, since they  all use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in the five calls to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes. Although the first two and last two calls to <tt>context.send()</tt> take place in different transactions, and the third call takes place without a transaction, all five calls take place in the same method.
|- valign="top"
| Do the <tt>context</tt> variables in the five calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they use the same injection point and are in the same <tt>@TransactionScope</tt>
|- valign="top"
| Are the five messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case H, option 2: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses the <tt>context</tt> variable for the first time, and destroyed when <tt>method1</tt> returns.

#### =Case H, option 3: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the five calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes, since they  all use the same <tt>context</tt> variable.
|- valign="top"
| Are the <tt>context</tt> variables in the five calls to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes. Although the first two and last two calls to <tt>context.send()</tt> take place in different transactions, and the third call takes place without a transaction, all five calls take place in the same method.
|- valign="top"
| Do the <tt>context</tt> variables in the five calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| Yes, since they are injected using identical annotations and are in the same <tt>@TransactionScope</tt>
|- valign="top"
| Are the five messages guaranteed to be delivered in the order in which they are sent?
| Yes, since they are sent using the same <tt>MessageProducer</tt> object.
|} 

#### =Case H, option 3: JMSContext lifecycle=

The <tt>JMSContext</tt> object will be created when <tt>method1</tt> uses the <tt>context</tt> variable for the first time, and destroyed when <tt>method1</tt> returns.

''Important note (for both options)'': Although the scope of the injected <tt>JMSContext</tt> object is completely defined in this example, the JMS behaviour of the  <tt>JMSContext</tt> object itself is not, since JMS does not define what happens if a <tt>JMSContext</tt> object is created outside a transaction and then used with one. It is probably desirable to clarify the JMS behaviour in this case. However, exactly the same question would arise of the <tt>JMSContext</tt> was instantiated from a connection factory rather than injected, so this is a JMS issue, not a CDI or injection-related issue.

### Use case J. Two separate container-managed transactions on the same thread, one suspended before the second is started 

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

#### =Case J, option 2: Analysis=

{|- border="1"
| Do the <tt>context</tt> variables in the three calls to <tt>context.send()</tt>  use the same injection point?
| The first and third calls use the same injection point, since they use the same <tt>context</tt> variable in <tt>Bean1</tt>. The second call uses a different injection point since it uses a different variable in <tt>Bean2</tt>.
|- valign="top"
| Are the <tt>context</tt> variables in the three calls to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| The first and third calls to <tt>context.send()</tt> take place in the same transaction, so are in the same <tt>@TransactionScope</tt>. However the third call takes place in a different transaction, so is in a different <tt>@TransactionScope</tt>.
|- valign="top"
| Do the <tt>context</tt> variables in the three calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| The first and third calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> since they use the same same injection point and are in the same <tt>@TransactionScope</tt>.
<br/>The second call to <tt>context.send()</tt> use a different <tt>JMSContext</tt> since it uses both a different injection point and is in a different <tt>@TransactionScope</tt>.
|- valign="top"
| Are the three messages guaranteed to be delivered in the order in which they are sent?
| The first and third messages are guaranteed to be delivered in the order in which they are sent, since they are sent using the same <tt>MessageProducer</tt> object, but the second message is not, since it is sent using a different <tt>MessageProducer</tt> object.
|} 

#### =Case J, option 2: JMSContext lifecycle=

The <tt>JMSContext</tt>object used by <tt>method1</tt> will be created when <tt>method1</tt> uses context for the first time, and destroyed when that method returns.

The <tt>JMSContext</tt>object used by <tt>method2</tt> will be created when <tt>method2</tt> uses context for the first time, and destroyed when that method returns. 

#### =Case J, option 3: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the three calls to <tt>context.send()</tt>  injected using identical annotations?
| Yes. The first and third calls use the same <tt>context</tt> variable in <tt>Bean1</tt>. The second call uses a different variable in <tt>Bean2</tt>, but it is injected using identical annotations to the variable used in <tt>Bean1</tt>.
|- valign="top"
| Are the <tt>context</tt> variables in the three calls to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| The first and third calls to <tt>context.send()</tt> take place in the same transaction, so are in the same <tt>@TransactionScope</tt>. However the third call takes place in a different transaction, so is in a different <tt>@TransactionScope</tt>.
|- valign="top"
| Do the <tt>context</tt> variables in the three calls to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| The first and third calls to <tt>context.send()</tt> (in <tt>Bean1</tt>) use the same <tt>JMSContext</tt> since they are injected using identical annotations and are in the same <tt>@TransactionScope</tt>.
<br/>The second call to <tt>context.send()</tt> use a different <tt>JMSContext</tt> because although it is injected using identical annotations it is in a different <tt>@TransactionScope</tt>.
|- valign="top"
| Are the three messages guaranteed to be delivered in the order in which they are sent?
| The first and third messages are guaranteed to be delivered in the order in which they are sent, since they are sent using the same <tt>MessageProducer</tt> object, but the second message is not, since it is sent using a different <tt>MessageProducer</tt> object.
|} 

#### =Case J, option 3: JMSContext lifecycle=

The <tt>JMSContext</tt>object used by <tt>method1</tt> will be created when <tt>method1</tt> uses context for the first time, and destroyed when that method returns.

The <tt>JMSContext</tt>object used by <tt>method2</tt> will be created when <tt>method2</tt> uses context for the first time, and destroyed when that method returns. 

### Use case K. One bean which calls another within the same transaction, but using different connection factories

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

#### =Case K, option 2: Analysis=

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

#### =Case K, option 2: JMSContext lifecycle=

The <tt>JMSContext</tt> object used by <tt>method1</tt> will be created when  <tt>method1</tt> uses <tt>context</tt> for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

The <tt>JMSContext</tt> object used by <tt>method2</tt> will be created when  <tt>method2</tt> uses <tt>context</tt> for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

#### =Case K, option 3: Analysis=

{|- border="1"
| Are the <tt>context</tt> variables in the two calls to <tt>context.send()</tt>  injected using identical annotations?
| No. They are in different beans and so use different <tt>context</tt> variables, which are injected using different connection factories.
|- valign="top"
| Are the <tt>context</tt> variables in each call to <tt>context.send()</tt> in the same <tt>@TransactionScope</tt>?
| Yes, since the two calls to <tt>context.send()</tt> take place in the same transaction.
|- valign="top"
| Do the <tt>context</tt> variables in each call to <tt>context.send()</tt> use the same <tt>JMSContext</tt> (and therefore <tt>MessageProducer</tt>) objects?
| No, although they are in the same <tt>@TransactionScope</tt> they are injected using different annotations.
|- valign="top"
| Are the two messages guaranteed to be delivered in the order in which they are sent?
| No, since they are sent using different <tt>MessageProducer</tt> objects.
|} 

#### =Case K, option 3: JMSContext lifecycle=

The <tt>JMSContext</tt> object used by <tt>method1</tt> will be created when  <tt>method1</tt> uses <tt>context</tt> for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

The <tt>JMSContext</tt> object used by <tt>method2</tt> will be created when  <tt>method2</tt> uses <tt>context</tt> for the first time, and destroyed when the container commits the transaction, which will be after both methods have returned.

''Note how specifying different connection factories means that the same injected <tt>JMSContext</tt> object cannot be used.