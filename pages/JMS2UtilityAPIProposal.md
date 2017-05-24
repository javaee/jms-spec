# JMS 2 Utility API Proposal
This is a basic proposal to add a utility API to simplify JMS synchronous operations such as sending and receiving messages. The proposal is based largely on analyzing the [http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jms/core/JmsTemplate.html Spring JmsTemplate] API. The code/API for the proposal is [http://rahmannet.net/projects/jmsutil/src/javax/jms/util/JmsUtility.java here]. For convenience, a corresponding Javadoc is [http://rahmannet.net/projects/jmsutil/doc/javax/jms/util/JmsUtility.html here]. The actual API code is more useful in this case than the Javadocs since it contains embedded comments important to the overall analysis/proposal.
__TOC__
## The Basics 
As we know, the raw JMS 1.x API is quite verbose and results in a lot of boilerplate code. This issue can be solved in JMS 2.x by creating a higher level abstraction that hides most of the boilerplate code and focuses on the actual task at hand (for example sending an object to a queue).

One approach to such an abstraction is allowing for direct injection of JMS API objects like messages, producers, consumers and queue browsers, hiding (but still making available for injection) all "intermediate" objects like connections and sessions (it is already possible to inject destinations and connection factories in Java EE 6). The essence of this approach is to create proxies of the injected JMS objects behind-the-scenes and managing their life-cycle/inter-dependencies transparently within the scope of a transaction (a model similar to the JPA transaction scoped persistence context). The very basics of this abstraction is demonstrated in the JMS integration modules available in [http://docs.jboss.org/seam/2.2.2.Final/reference/en-US/html_single/#d0e21465 Seam 2] and [http://docs.jboss.org/seam/3/jms/latest/reference/en-US/html/resource-injection.html Seam 3]. A separate proposal outlines this approach for JMS 2.

Another approach to the issue is creating a more traditional abstraction that acts as a high level utility for common JMS tasks. The approach is inspired by Spring JmsTemplate and is detailed here. In this model, one helper class contains a set of methods corresponding to each logical JMS task. It is possible to set connection, session, producer and consumer level options through properties of this class in addition to being able to set the connection factory and destination (if desired). Each method hides JMS boilerplate code and focuses on the core business domain. This model can be augmented to expose some of the underlying mechanics by allowing access to the intermediate JMS objects through call-backs/delegates at specific extension points. I did not include these extension points in the initial API but only included simple operations for sending messages, receiving messages, browsing queues and subscribing to/unsubscribing from topics. The code comments discuss where these delegation points could be added if desired.

In the proposed API, all JMS objects are created and destroyed per method call. Unless used with pooled JMS resources, this would cause serious scalability issues. Other than relying on connection pooling, the API could support a mode where JMS objects are cached for the lifetime of the API object instance or for the duration of a transaction. Although the default mode for Spring JmsTemplate is to do no caching, in a Java EE environment, it might be most sensible to cache object for the lifetime of a transaction by default.

To facilitate a focus on the core business domain, the helper class automatically converts objects to the most appropriate JMS message type and vice-versa. Byte arrays are converted to BytesMessage, maps are converted to MapMessage, strings are converted to TextMessage and serializable objects are converted to ObjectMessage. StreamMessage types are not handled.
## Usage Patterns 
Since the utility class is a very basic abstraction with a minimum number of runtime dependencies, it could be used as-is in Java SE, even without any dependency injection support. For example, it could be programmatically instantiated like this:
 JmsUtility jmsUtility = new JmsUtility(connectionFactory); 
In a Java EE environment, the API could take advantage of resource injection like this:
 private JmsUtility jmsUtility = null;
 ... 
 @Resource(name = "jms/DefaultConnectionFactory")
 public void setConnectionFactory(ConnectionFactory connectionFactory) {
     jmsUtility = new JmsUtility(connectionFactory);
 }
The API instance could also be configured entirely in XML as below (the example uses [http://docs.jboss.org/seam/3/config/latest/reference/en-US/html/ Seam XML] OO-style syntax, but any bean wiring syntax could be used):
 &lt;javaee:JmsUtility>
     &lt;connectionFactory>
         &lt;Resource name="jms/DefaultConnectionFactory" />
     &lt;/connectionFactory>
     &lt;defaultDestination>
         &lt;Resource name="jms/DefaultQueue" />
     &lt;/defaultDestination>
     &lt;disableMessageID>true&lt;/disableMessageID>
     &lt;receiveTimeout>0&lt;/receiveTimeout>
 &lt;/javaee:JmsUtility>
The XML wired API instance can then be injected where desired:
 @Inject
 private JmsUtility jmsUtility;

Once properly constructed/configured, the API instance can then be used for JMS tasks:

  jmsUtility.send(queue, "Hello World!");

Each logical operation in the API comes in two variants, one that takes the JMS destination as an argument and one that uses a default destination that can be configured. For example, the send call above could have used a default queue like this:
 private JmsUtility jmsUtility = new JmsUtility();
 ...
 @Resource(name = "jms/DefaultConnectionFactory")
 public void setConnectionFactory(ConnectionFactory connectionFactory) {
     jmsUtility.setConnectionFactory(connectionFactory);
 }
 
 @Resource(name = "jms/DefaultQueue")
 public void setDefaultQueue(Queue queue) {
     jmsUtility.setDefaultDestination(queue);
 }
 ...
 jmsUtility.send("Hello World!");
The receive and browse operations also come with variants that take message selectors as arguments.
##  Notes
Some notes on the initial version of the API:
* Spring JmsTemplate wisely wraps existing checked JMS exceptions into unchecked Spring JMS specific exceptions. I used JMS exceptions in my proposal in the hopes that we will make it unchecked in JMS 2.
* In addition to operation variants that support a JMS destination parameter and default destination setting, Spring JmsTemplate also supports a variant with String destination names. I did not add this variant to reduce clutter and because look-ups seem a little anachronistic with injection, particularly the type-safe injection that CDI supports. If desired we could add that variant here as well. Under the hood, these String names would be used for JNDI look-ups.
* For some reason, Spring JmsTemplate currently has no support for durable subscriptions. I added that here.
* The message QoS settings (delivery mode, priority, time-to-live) would only be used if at least one is set explicitly by the user. Otherwise, these values are assumed to be set at the resource level by the JMS resource provider/application server.
## Strengths 
The following are the benefits of this model as I see it:
* <b><i>It is a simple-to-implement API that has minimal external dependencies.</i></b> In fact, it is very similar to something most developers would implement in-house themselves (indeed, I implemented something very similar myself before JmsTemplate existed).
* <b><i>It is a familiar/popular mental model.</i></b> I will be the first to admit I do not give much weight to anything based solely on its popularity - it seems a lot like <i>argumentum ad populum</i> to me. One need not look much farther than pop music icons like Britney Spears or the Spice Girls or most Bollywood movies with mass market appeal to see the obvious weaknesses in adopting something just because "everyone is doing it". That being said, Spring JmsTemplate along with Spring does enjoy a wide install base. Adopting this model would mean leveraging existing developer familiarity with a workable approach.

## Weaknesses 
In my opinion, this approach has the following weaknesses:
* <b><i>It is an extraneous layer that duplicates JMS APIs.</i></b> As opposed to the Seam JMS based approach of leveraging the existing JMS API as much as possible (and perhaps fixing some parts of the basic JMS API), this approach adds a new API altogether that duplicates parts of the existing JMS API. Besides adding to the learning curve, this also means that functionality must be maintained in both places going forward.
* <b><i>The API lacks flexibility and has very leaky abstractions</i></b>. The basic API I proposed above is very rigid compared to basic JMS. For example, there is no good way of handling message headers, properties, stream messages, custom object-to-message mapping, batches of messages, sending and receiving messages in the same session, reply-to queues, temporary queues/topics, correlation IDs, using connection meta-data and so on. The solution to this problem is adding delegation points that  provide access to underlying JMS objects or exposing more JMS objects at the API level (such as methods that take JMS messages as arguments/return types, factory methods for creating messages, sessions, message producers and so on). This is in fact exactly what JmsTemplate does (as did my own home brewed JMS abstraction). Adding these features basically amounts to adding a lot of leaks in the abstraction, reducing the value of the abstraction in the first place. My personal experience with JmsTemplate has been that for all but very trivial JMS clients, you end up needing these abstraction leaks, which raises the question of whether this is the wrong paradigm altogether. The Seam JMS style approach does not have these problems because it does not attempt to place a concrete abstraction barrier at all while still removing boilerplate code.
* <b><i>The abstraction itself is not necessarily simple.</i></b> The basic JMS API consists of a set of fine-grained, hierarchical, specialized interfaces. While this approach adds flexibility and keeps each interface digestible, it also introduces the problem of boilerplate code. The JmsTemplate approach takes the exact opposite road. It is a flat, general purpose API with little boilerplate code. However, it is debatable whether the API is really that simple. In my initial API alone, there are more than twenty-five methods that look very similar. As we add more features like String based destinations and delegation points, the possible permutations for methods increases even more dramatically to the point where the abstraction itself becomes fairly cumbersome. The Seam JMS style approach effectively avoids this problem as well.

## Possible Improvements to the Basic JMS API 
In course of developing this proposal, a few ideas for improving the basic JMS API occurred to me. We may  want to consider these regardless of the abstraction approach we take:
* As we discussed earlier in the EG, requiring a session to create messages does seem cumbersome. We could investigate a way of simplifying this. Ideally, it should have been possible to simply instantiate message types directly. Short of that, I am not so sure how to effectively simplify the process of creating a message. However, the ability to inject messages might bypass this issue altogether.
* One of the places JmsTemplate really shines is when you are simply interested in the message payload and not the JMS message type, headers or properties. We may want to consider adding convenience methods to the basic JMS producer and consumer that simply handle the payload as a Java Object, bypassing the JMS Message interface altogether.
* The factory methods for creating messages is a little inconsistent. While the methods to create object and text messages can use payload values, the methods to create map and byte array messages do not. This could be easily fixed I think. 
* In some places, the JMS API really does not follow well-established Java naming practices like camel-casing. It is now probably too late to fix this for object and method names, however, it would be easy to fix the parameter names. This is understandable given the age of some of these APIs as well as the time-to-market.
* The syntax for the message consumer receive methods is a bit strange. To see why, compare these methods to what I have for the receive timeout in the proposed API (it is the same way in Spring JmsTemplate). It might be too late to fix these now, however.

## Recommendation 
The way I see it, the weaknesses of this approach outweighs its benefits. <i>I believe we should not consider this approach further and focus on the Seam JMS style injection/context management based abstraction going forward.</i> In either case, developers can continue to use the Spring JmsTemplate with JMS 2/Java EE 7. Others can similarly use the JMS-CDI Event bridge that I also do no believe should be standardized right now.

However, I do think that we should discuss the merits of these approaches not just in the EG but also with the broader community given the broad impact and longevity of these changes.