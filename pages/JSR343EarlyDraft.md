# JMS 2.0 Early Draft
{: .no_toc}

_JMS 2.0 has now been released. This page is retained here as a historical record and in case they prove useful to a future JMS expert group. See also the [JMS 2.0 Final Release](/jms-spec/pages/JMS20FinalRelease) page._

The JMS 2.0 Early Draft is now published and may be downloaded [here](http://jcp.org/aboutJava/communityprocess/edr/jsr343/index.html). The formal review period ended on 29 March 2012. 

**Comments are still welcome**. You can either send email to the community mailing list ([how to  subscribe](/jms-spec/#jms-community-mailing-lists)) or you can add a comment to the specific issue ([how](/jms-spec/#issue-tracker)).

## Contents
{: .no_toc}

* auto-gen TOC:
{:toc}

## What's new in JMS 2.0? 

The following new messaging features have been added in JMS 2.0:

* Batch delivery: new API has been added to allow a JMS provider to deliver messages to an asynchronous consumer in batches.
* Delivery delay: a message producer can now specify that a message must not be delivered until after a specified time interval.
* New send methods have been added to allow an application to send messages asynchronously.
* JMS providers must now set the `JMSXDeliveryCount` message property.

Several changes have been made to the JMS API to make it simpler and easier to use:

* `Connection`, `Session` and other objects with a `close()` method now implement the `java.jang.AutoCloseable` interface to allow them to be used in a Java SE 7 try-with-resources statement.
* A new "simplified API" has been added which offers a simpler alternative to the standard API, especially in Java EE applications.
* New methods have been added to create a session without the need to supply redundant arguments.
* Client ID is now optional when creating a durable subscription

A new chapter has been added which describes some additional restrictions and behaviour which apply when using the JMS API in the Java EE web or EJB container. This information was previously only available in the EJB and Java EE platform specifications.

New methods have been added to `Session` which return a `MessageConsumer` on a durable topic subscription. Applications could previously only obtain a domain-specific `TopicSubscriber`, even though its use was discouraged.

The JMS 2.0 specification now requires JMS providers to implement both P2P and Pub-Sub.

The specification has been clarified in various places.

A full list of the new features, changes and clarifications introduced in JMS 2.0 is given in section B.5 "Version 2.0" of the draft specification. 

##  Unresolved issues

The JCP process encourages the release of the Early Draft when the specification still has some unresolved issues. Although comments and suggestions are welcome on all parts of the specification, the following issues in particular are known to be unresolved:

* Simplified JMS API: Should the new `MessagingContext` object allow multiple consumers to be created on the same session, which will require separate consumer objects to be created, or should the API restrict a session to a single consumer in order to avoid the need to create a separate object?
* Simplified API: what is the most appropriate API, scope and behaviour for the injection of `>MessagingContext` objects?
* Batch message delivery: Is the proposed API appropriate or is it getting too complicated?

A more detailed discussion of these unresolved issues is given in section A.2 "Unresolved issues in the JMS 2.0 Early Draft" in the draft specification.

##  Issues related to the Early Draft

The following query will list all the "issues" included in the Early Draft: <br/>

[Issues included in the JMS 2.0 Early Draft](/javaee/jms-spec/issues?utf8=✓&q=label%3Aed20-added)

##  What's planned for after the early draft? 

Quite a few issues didn't make it into the Early Draft and have been deferred until the next draft (the Public Draft): These can be listed using the query below.

[Issues which are not included in the JMS 2.0 Early Draft but will be considered for inclusion in the JMS 2.0 Public Draft](https://github.com/javaee/jms-spec/issues?utf8=%E2%9C%93&q=label%3Apd20-underreview)

##  Related specifications

Several JMS-related issues affect other specifications and have been submitted to the appropriate expert group: <br/>

[Issues which have been passed to the EJB 3.2 expert group for inclusion in that specification](/jms-spec/issues?utf8=✓&q=label%3Ajms20-jsr345)
[Issues which have been passed to the Java EE 7  expert group for inclusion in that specification](/jms-spec/issues?utf8=✓&q=label%3Ajms20-jsr342)


