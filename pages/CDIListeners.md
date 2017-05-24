# Some notes about CDI listeners (DRAFT)</h1>

==Listener created automatically ==

===Normal-scoped listener===

{|- border="1"
! Listener class declaration
! Listener method declaration
! Listener instance declaration
! What happens 
|-
| <tt>@ApplicationScoped<br/>public class MyListener {<br/></tt>
| <tt>public void listen(<br/>@Observes<br/>HelloEvent helloEvent){</tt>
| None
| CDI creates instance of listener which continues to receive events until the scope ends 
|-
| <tt>@ApplicationScoped<br/>public class MyListener {<br/></tt>
| <tt>public void listen(<br/>@Observes<br/>HelloEvent helloEvent){</tt>
| <tt>@Inject MyListener myListener1;</tt>
| <tt>myListener1</tt> initialised to proxy, Listener object instantiated when event fired. Callback invoked. Listener continues to receive events until its scope ends.
|-
| <tt>@ApplicationScoped<br/>public class MyListener {<br/></tt>
| <tt>public void listen(<br/>@Observes<br/>HelloEvent helloEvent){</tt>
| <tt>@Inject MyListener myListener1;</tt><br/><br/>
Prior to event being fired, forces instance creation by calling<br/><br/>
<tt>myListener.toString();</tt> 
| <tt>myListener1</tt> initialised to proxy, Listener object instantiated when <tt>toString</tt> called. Callback invoked. Listener continues to receive events until its scope ends.
|-
|}

===Dependent-scoped listener===

{|- border="1"
! Listener class declaration
! Listener method declaration
! Listener instance declaration
! What happens 
|-
| <tt>@Dependent<br/>public class MyListener {<br/></tt>
| <tt>public void listen(<br/>@Observes<br/>HelloEvent helloEvent){</tt>
| None
| CDI creates a new instance of MyListener for each event, and sends it that one event.
|-
| <tt>@Dependent<br/>public class MyListener {<br/></tt>
| <tt>public void listen(<br/>@Observes<br/>HelloEvent helloEvent){</tt>
| <tt>@Inject MyListener myListener1;</tt>
| CDI creates a new, separate, instance of MyListener for each event, and sends it that one event.<br/><br/><tt>myListener1</tt> is initialised to an instance of MyListener which remains until the scope ends, but it never receives any events. 
|-
|}

==Listener not created automatically ==

===Normal-scoped listener===

{|- border="1"
! Listener class declaration
! Listener method declaration
! Listener instance declaration
! What happens 
|-
| <tt>@ApplicationScoped<br/>public class MyListener {<br/></tt>
| <tt>public void listen(<br/>@Observes(notifyObserver=Reception.IF_EXISTS)<br/>HelloEvent helloEvent){</tt>
| None
| Listener object never instantiated. Callback never invoked.
|-
| <tt>@ApplicationScoped<br/>public class MyListener {<br/></tt>
| <tt>public void listen(<br/>@Observes(notifyObserver=Reception.IF_EXISTS)<br/>HelloEvent helloEvent){</tt>
| <tt>@Inject MyListener myListener1;</tt>
| <tt>myListener1</tt> initialised to proxy, but Listener object never instantiated. Callback never invoked.
|-
| <tt>@ApplicationScoped<br/>public class MyListener {<br/></tt>
| <tt>public void listen(<br/>@Observes(notifyObserver=Reception.IF_EXISTS)<br/>HelloEvent helloEvent){</tt>
| <tt>@Inject MyListener myListener1;</tt><br/><br/>
Prior to event being fired, forces instance creation by calling<br/><br/>
<tt>myListener.toString();</tt> 
| <tt>myListener1</tt> initialised to proxy, Listener object instantiated. Callback invoked. Listener continues to receive events until its scope ends.
|-
|}

===Dependent-scoped listener===

This is not possible, since <tt>@Observes(notifyObserver=Reception.IF_EXISTS)</tt> is not allowed with dependent scoped listeners) 

==Other aspects==

Can the same listener instance be called concurrently for multiple events?

