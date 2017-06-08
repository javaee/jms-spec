# JSR 368 Meeting 1 at JavaOne 2015

## Meeting details

0900-1100 Thursday 29th October

Hilton San Francisco Union Square, 333 O'Farrell Street, San Francisco

Room "Union Square 22". (JavaOne badge not required)

The minutes of this meeting are [here](https://java.net/projects/jms-spec/lists/users/archive/2015-10/message/47).

## Agenda 

### 1. Introductions

Please introduce yourself. Are you a JMS developer or a JMS user? What do you use JMS for? What would you like to see in JMS2.1?

### 2. Improving collaboration

How can the spec lead make it easier for people to contribute to JMS 2.1? Should we hold meetings, regular conference calls or IM sessions? What is the role of the expert group? Should we be more formal in planning the work on JMS 2.1?

Email lists. Draft spec and javadocs. Reference implementation. Compatibility tests. CTS assertions in the spec.

### 3. Proposed high-level plan for JMS 2.1

See proposed plan below

What additional features would you like to see?

What proposed features would you like to see dropped?

Show of hands?

### 4. Review of EDR1: Flexible JMS MDBs

Review of the detailed proposals for "Flexible JMS MDBs" as described in the JMS 2.1 Early Draft. 

Particular issues to cover
* Relationship with EJB spec
* Requirement for no-methods marker interface
* Annotations on callback methods
* Should we allow multiple callback methods?
* Parameter options
* Handling conversion errors
* Handling exceptions

### 5. CDI beans as JMS message listeners

Particular issues to cover
* Why we need this. Whether we need this.
* Relationship to CDI events
* Dependent scope beans
* Application scoped beans
* Other scopes
* Should we make the callback method single-threaded?

## Proposed high-level plan 

[JMS 2.1 schedule](/jms-spec/pages/JMS21#jms-21-schedule)

Here's a suggested high-level list of features for JMS 2.1, and an approximate order in which they will be considered. 

Minor changes are not listed here: these could be considered in parallel to this: we need to decide on a parallel list of minor changes. We could have a separate "issue triage" session to discuss minor proposals in turn.

* Flexible JMS MDBs ([JMS_SPEC-116](https://github.com/javaee/jms-spec/issues/116))
  * (See above)

* CDI beans as message listeners ([JMS_SPEC-134](https://github.com/javaee/jms-spec/issues/134))
  * (See above)

* Batch delivery to MDBs ([JMS_SPEC-36](https://github.com/javaee/jms-spec/issues/36))

* New and custom acknowledgement modes
  * No acknowledge mode ([JMS_SPEC-21](https://github.com/javaee/jms-spec/issues/21), [JMS_SPEC-168(https://github.com/javaee/jms-spec/issues/168)])
  * Individual acknowledge mode ([JMS_SPEC-95](https://github.com/javaee/jms-spec/issues/95) and related [JMS_SPEC-176](https://github.com/javaee/jms-spec/issues/176))
  * Allowing custom acknowledgement modes ([JMS_SPEC-169](https://github.com/javaee/jms-spec/issues/169))

* Allowing setMessageListener in a Java EE web or EJB application

* API to create a connection factory (without using JNDI) in a Java SE application ([JMS_SPEC-89](https://github.com/javaee/jms-spec/issues/89))

* API to create a queue or topic (without using JNDI) in a Java SE application ([JMS_SPEC-90](https://github.com/javaee/jms-spec/issues/90))

* Repeatable annotations for resource definitions ([JMS_SPEC-151](https://github.com/javaee/jms-spec/issues/151))

* Configuring message redelivery and dead message queues for MDBs ([JMS_SPEC-117](https://github.com/javaee/jms-spec/issues/117))
  * redeliveryInterval
  * redeliveryLimit
  * deadMessageLookup

* Missing method createXAJMSContext() on XAJMSContext (to allow multiple XAJMSContexts to share the same connection) ([JMS_SPEC-152](https://github.com/javaee/jms-spec/issues/152))

* JMS in a Java EE application: adding clarifications and removing restrictions
  * Defining the behavior of a JMS session that is created outside a JTA transaction but used to send or receive a message within a JTA transaction, and vice versa. ([JMS_SPEC-129](https://github.com/javaee/jms-spec/issues/129))
  * Defining an API to allow a JMS connection factory, connection or session to opt-out of a JTA transaction ([JMS_SPEC-130](https://github.com/javaee/jms-spec/issues/130))
  * Clarifying the existing restrictions on using client-acknowledgement and local transactions in a Java EE environment and removing these restrictions where possible ([JMS_SPEC-131](https://github.com/javaee/jms-spec/issues/131))

* Anything else?

There's a complete list of all currently-proposed features (too many to do all of them) on the on the [JMS planning long list](/jms-spec/pages/JMSLongList).

Note the following two requests for changes to the MDB specification (by definition this is not within our direct control).

* Allow listener method to always be determined at runtime by resource adapter ([EJB_SPEC-126](https://github.com/javaee/ejb-spec/issues/126))

* Define a standard way to specify the resource adapter used by a JMS MDB ([EJB_SPEC-127](https://github.com/javaee/ejb-spec/issues/127))


