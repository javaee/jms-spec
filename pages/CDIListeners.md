# Some notes about CDI listeners (DRAFT)

## Listener created automatically

### Normal-scoped listener

{|- border="1"
! Listener class declaration
! Listener method declaration
! Listener instance declaration
! What happens 
|-
| `@ApplicationScoped<br/>public class MyListener {<br/>`
| `public void listen(<br/>@Observes<br/>HelloEvent helloEvent){`
| None
| CDI creates instance of listener which continues to receive events until the scope ends 
|-
| `@ApplicationScoped<br/>public class MyListener {<br/>`
| `public void listen(<br/>@Observes<br/>HelloEvent helloEvent){`
| `@Inject MyListener myListener1;`
| `myListener1` initialised to proxy, Listener object instantiated when event fired. Callback invoked. Listener continues to receive events until its scope ends.
|-
| `@ApplicationScoped<br/>public class MyListener {<br/>`
| `public void listen(<br/>@Observes<br/>HelloEvent helloEvent){`
| `@Inject MyListener myListener1;`<br/><br/>
Prior to event being fired, forces instance creation by calling<br/><br/>
`myListener.toString();` 
| `myListener1` initialised to proxy, Listener object instantiated when `toString` called. Callback invoked. Listener continues to receive events until its scope ends.
|-
|}

### Dependent-scoped listener

{|- border="1"
! Listener class declaration
! Listener method declaration
! Listener instance declaration
! What happens 
|-
| `@Dependent<br/>public class MyListener {<br/>`
| `public void listen(<br/>@Observes<br/>HelloEvent helloEvent){`
| None
| CDI creates a new instance of MyListener for each event, and sends it that one event.
|-
| `@Dependent<br/>public class MyListener {<br/>`
| `public void listen(<br/>@Observes<br/>HelloEvent helloEvent){`
| `@Inject MyListener myListener1;`
| CDI creates a new, separate, instance of MyListener for each event, and sends it that one event.<br/><br/>`myListener1` is initialised to an instance of MyListener which remains until the scope ends, but it never receives any events. 
|-
|}

## Listener not created automatically

### Normal-scoped listener

{|- border="1"
! Listener class declaration
! Listener method declaration
! Listener instance declaration
! What happens 
|-
| `@ApplicationScoped<br/>public class MyListener {<br/>`
| `public void listen(<br/>@Observes(notifyObserver=Reception.IF_EXISTS)<br/>HelloEvent helloEvent){`
| None
| Listener object never instantiated. Callback never invoked.
|-
| `@ApplicationScoped<br/>public class MyListener {<br/>`
| `public void listen(<br/>@Observes(notifyObserver=Reception.IF_EXISTS)<br/>HelloEvent helloEvent){`
| `@Inject MyListener myListener1;`
| `myListener1` initialised to proxy, but Listener object never instantiated. Callback never invoked.
|-
| `@ApplicationScoped<br/>public class MyListener {<br/>`
| `public void listen(<br/>@Observes(notifyObserver=Reception.IF_EXISTS)<br/>HelloEvent helloEvent){`
| `@Inject MyListener myListener1;`<br/><br/>
Prior to event being fired, forces instance creation by calling<br/><br/>
`myListener.toString();` 
| `myListener1` initialised to proxy, Listener object instantiated. Callback invoked. Listener continues to receive events until its scope ends.
|-
|}

### Dependent-scoped listener

This is not possible, since `@Observes(notifyObserver=Reception.IF_EXISTS)` is not allowed with dependent scoped listeners) 

## Other aspects 

Can the same listener instance be called concurrently for multiple events?

