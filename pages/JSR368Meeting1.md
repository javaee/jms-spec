# JSR 368 Meeting 1 at JavaOne 2015

0900-1100 Thursday 29th October<br/>
Hilton San Francisco Union Square, 333 O'Farrell Street, San Francisco<br/>
Room "Union Square 22". (JavaOne badge not required)

* auto-gen TOC:
{:toc}

Up to the [/jms-spec/pages/JMS21#Meetings Meetings index].

The minutes of this meeting are [https://java.net/projects/jms-spec/lists/users/archive/2015-10/message/47 here].

## Agenda 

<h3>1. Introductions

Please introduce yourself. Are you a JMS developer or a JMS user? What do you use JMS for? What would you like to see in JMS2.1?

<h3>2. Improving collaboration

How can the spec lead make it easier for people to contribute to JMS 2.1? Should we hold meetings, regular conference calls or IM sessions? What is the role of the expert group? Should we be more formal in planning the work on JMS 2.1?

Email lists. Draft spec and javadocs. Reference implementation. Compatibility tests. CTS assertions in the spec.

<h3>3. Proposed high-level plan for JMS 2.1

See proposed plan below

What additional features would you like to see?

What proposed features would you like to see dropped?

Show of hands?

<h3>4. Review of EDR1: Flexible JMS MDBs

Review of the detailed proposals for "Flexible JMS MDBs" as described in the JMS 2.1 Early Draft. 

Particular issues to cover
* Relationship with EJB spec
* Requirement for no-methods marker interface
* Annotations on callback methods
* Should we allow multiple callback methods?
* Parameter options
* Handling conversion errors
* Handling exceptions

<h3>5. CDI beans as JMS message listeners

Particular issues to cover
* Why we need this. Whether we need this.
* Relationship to CDI events
* Dependent scope beans
* Application scoped beans
* Other scopes
* Should we make the callback method single-threaded?

## Proposed high-level plan 

[/jms-spec/pages/JMS21#JMS_2.1_schedule JMS 2.1 schedule]

Here's a suggested high-level list of features for JMS 2.1, and an approximate order in which they will be considered. 

Minor changes are not listed here: these could be considered in parallel to this: we need to decide on a parallel list of minor changes. We could have a separate "issue triage" session to discuss minor proposals in turn.

* Flexible JMS MDBs ([https://github.com/javaee/jms-spec/issues/116 JMS_SPEC-116])
  * (See above)

* CDI beans as message listeners ([https://github.com/javaee/jms-spec/issues/134 JMS_SPEC-134])
  * (See above)

* Batch delivery to MDBs ([https://github.com/javaee/jms-spec/issues/36 JMS_SPEC-36])

* New and custom acknowledgement modes
  * No acknowledge mode ([https://github.com/javaee/jms-spec/issues/21 JMS_SPEC-21], [https://java.net/jira/browse/JMS_SPEC-168 JMS_SPEC-168])
  * Individual acknowledge mode ([https://github.com/javaee/jms-spec/issues/95 JMS_SPEC-95] and related [https://java.net/jira/browse/JMS_SPEC-176 JMS_SPEC-176])
  * Allowing custom acknowledgement modes ([https://java.net/jira/browse/JMS_SPEC-169 JMS_SPEC-169])

* Allowing setMessageListener in a Java EE web or EJB application

* API to create a connection factory (without using JNDI) in a Java SE application ([https://java.net/jira/browse/JMS_SPEC-89 JMS_SPEC-89])

* API to create a queue or topic (without using JNDI) in a Java SE application ([https://java.net/jira/browse/JMS_SPEC-90 JMS_SPEC-90])

* Repeatable annotations for resource definitions ([https://java.net/jira/browse/JMS_SPEC-151 JMS_SPEC-151])

* Configuring message redelivery and dead message queues for MDBs ([https://java.net/jira/browse/JMS_SPEC-117 JMS_SPEC-117])
  * redeliveryInterval
  * redeliveryLimit
  * deadMessageLookup

* Missing method createXAJMSContext() on XAJMSContext (to allow multiple XAJMSContexts to share the same connection) ([https://github.com/javaee/jms-spec/issues/152 JMS_SPEC-152])

* JMS in a Java EE application: adding clarifications and removing restrictions
  * Defining the behavior of a JMS session that is created outside a JTA transaction but used to send or receive a message within a JTA transaction, and vice versa. ([https://java.net/jira/browse/JMS_SPEC-129 JMS_SPEC-129])
  * Defining an API to allow a JMS connection factory, connection or session to opt-out of a JTA transaction ([https://github.com/javaee/jms-spec/issues/130 JMS_SPEC-130])
  * Clarifying the existing restrictions on using client-acknowledgement and local transactions in a Java EE environment and removing these restrictions where possible ([https://java.net/jira/browse/JMS_SPEC-131 JMS_SPEC-131])

* Anything else?

There's a complete list of all currently-proposed features (too many to do all of them) on the on the [/jms-spec/pages/JMS21Planning JMS 2.1 planning page].

Note the following two requests for changes to the MDB specification (by definition this is not within our direct control).

* Allow listener method to always be determined at runtime by resource adapter ([http://java.net/jira/browse/EJB_SPEC-126 EJB_SPEC-126])

* Define a standard way to specify the resource adapter used by a JMS MDB ([http://java.net/jira/browse/EJB_SPEC-127 EJB_SPEC-127])


