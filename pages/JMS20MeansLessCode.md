<h1>Ten ways in which JMS 2.0 means writing less code</h1>

<p>Here are ten simple examples that demonstrate how JMS 2.0 requires less code than JMS 1.1.</p>

__TOC__

==Single JMSContext instead of separate Connection and Session objects==

The JMS 2.0 simplified API introduces a new object, <tt>JMSContext</tt>, which provides the same functionality as the separate <tt>Connection</tt> and
<tt>Session</tt> objects in the JMS 1.1 API:

'''JMS 1.1'''

 Connection connection = connectionFactory.createConnection();
 Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

'''JMS 2.0'''

 JMSContext context = connectionFactory.createContext(JMSContext.SESSION_TRANSACTED);

==Use of try-with-resources block means no need to call close==

Failing to close a <tt>Connection</tt> after use may cause your application to run out of resources.

'''JMS 1.1'''

In JMS 1.1 the best way to make sure your connection gets closed after use is to call <tt>close()</tt> in a <tt>finally</tt> block:

 try {
    Connection connection = connectionFactory.createConnection();
    try {
       Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
       ... etc ...
    } finally {
       connection.close();
    }
 } catch (JMSException ex) {
    ex.printStackTrace();
 }

This is rather verbose. To make things worse, if you get an exception in the body of the <tt>try</tt> block, followed by an exception in <tt>close()</tt>,
the first exception will be lost, even though the first exception is probably the root cause of the failure.

'''JMS 2.0'''

In JMS 2.0 the <tt>Connection</tt> object implements the <tt>java.lang.AutoCloseable</tt> interface. This means that if you create the <tt>Connection</tt>  object in a try-with-resources block the <tt>close</tt> method will be called automatically at the end of the block. 

 try (Connection connection = connectionFactory.createConnection();){
    ... etc ...
 } catch (JMSException ex) {
    ex.printStackTrace();
 }

This requires less code. In addition it allows exceptions to be handled better: if you get an exception in the body of the try block, followed by an exception in <tt>close()</tt>, the
first exception will be nicely nested within the first, so you can easily identify the root cause.

The same syntax may be used when creating a <tt>JMSContext</tt>.

==No need to pass in two parameters when creating a session in Java SE==

...one is all you need:

'''JMS 1.1'''

 Session session = connection.createSession(true,Session.SESSION_TRANSACTED);

'''JMS 2.0'''

 Session session = connection.createSession(Session.SESSION_TRANSACTED);

There are similar methods for creating a <tt>JMSContext</tt>

==No need to pass in any parameters when creating a session in a Java EE transaction==

If you create a <tt>Session</tt> in a Java EE transaction, the arguments to <tt>createSession</tt> are ignored. 
It says so in the EJB 3.1 spec.

'''JMS 1.1'''

But JMS 1.1 still required you to pass in two parameters even though they will be ignored.

 // both parameters will be ignored; transactional behaviour is determined by the container
 Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);

'''JMS 2.0'''

JMS 2.0 provides a method with no parameters:

 // transactional behaviour is determined by the container
 Session session = connection.createSession();

==New JMSProducer object supports method chaining==

The new <tt>JMSProducer</tt> object allows message headers, message properties and delivery options to be specified in a single line of code using method chaining

'''JMS 1.1'''

 MessageProducer messageProducer = session.createProducer(demoQueue);
 messageProducer.setPriority(1);
 TextMessage textMessage = session.createTextMessage(body);
 textMessage.setStringProperty("foo", "bar");
 messageProducer.send(textMessage);

'''JMS 2.0'''

 TextMessage textMessage = context.createTextMessage(body);
 context.createProducer().setPriority(1).setProperty("foo", "bar").send(demoQueue, textMessage);

==No need to save a JMSProducer in a variable; simply instantiate on the fly==

The new <tt>JMSProducer</tt> object is a lightweight object so there's no need to save it in a variable; simply instantiate one on the fly when needed

'''JMS 1.1'''

<tt>MessageProducer</tt> is expensive so need to reuse it.

 MessageProducer messageProducer = session.createProducer(demoQueue);
 messageProducer.send(message1);
 messageProducer.send(message2);

'''JMS 2.0'''

<tt>JMSProducer</tt> is cheap, no need to save in variable

 context.createProducer().send(demoQueue,message1);
 context.createProducer().send(demoQueue,message2);

==In Java EE, injecting a JMSContext means you don't need to create or close it==

'''JMS 1.1'''

 try {
    Connection connection = connectionFactory.createConnection();
    try {
       Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
       MessageProducer messageProducer = session.createProducer(demoQueue);
       TextMessage textMessage = session.createTextMessage(body);
       messageProducer.send(textMessage);
    } finally {
       connection.close();
     }
 } catch (JMSException ex) {
    ex.printStackTrace();
 }

'''JMS 2.0'''

 try {
    context.createProducer().send(inboundQueue, body);
 } catch (JMSRuntimeException ex){
    ex.printStackTrace();
 }

==When sending, no need to instantiate a Message object==

'''JMS 1.1'''

Need to create a message object of the appropriate type and then set its body:

   MessageProducer messageProducer = session.createProducer(demoQueue);
   TextMessage textMessage = session.createTextMessage("Hello world");
   messageProducer.send(textMessage);

'''JMS 2.0'''

In JMS 2.0, simply pass the message body into the <tt>send</tt> method:

 context.createProducer().send(demoQueue,"Hello world");

Note that you can do this even when you want to set message properties since these can be set on the <tt>JMSProducer</tt>.

==Receiving synchronously, can receive mesage payload directly==

'''JMS 1.1'''

When receiving synchronously, so you are given a <tt>Message</tt> object and need to cast it to the appropriate subtype before you can extract its body:

 MessageConsumer messageConsumer = session.createConsumer(demoQueue);
 TextMessage textMessage = (TextMessage) messageConsumer.receive(1000);
 if (textMessage==null){
    return "Received null";
 } else {
    return "Received "+textMessage.getText();
 }

'''JMS 2.0'''

JMS 2.0 allows you to receive the message body directly.

 JMSConsumer consumer = context.createConsumer(demoQueue);
 return "Received " + consumer.receiveBody(String.class, 1000);

Note the lack of casting, or special null handling.

==Receiving asynchronously: no need for a cast before extracting message body.==

'''JMS 1.1'''

When receiving a message asynchronously, the message passed to the <tt>onMessage</tt> method is a <tt>javax.jms.Message</tt> You need to cast it to the expected subtype before you can extract the body. If the message is an <tt>ObjectMessage</tt> this gives you a <tt>Serializable</tt> body which you have to cast a second time to the expected body type. 

 public void onMessage(Message m){
    MyObject myObj = (MyObject)((ObjectMessage)m).getObject();
    ...

'''JMS 2.0'''

A new method <tt>getBody</tt> allows you to extract the body from the <tt>Message</tt> without the need to cast it to the appropriate subtype first. This is particularly handy for ObjectMessages, as it avoids a double cast to extract the object payload

 public void onMessage(Message m){
    MyObject myObj = m.getBody(MyObject.class);
    ...
