# JMS 2.1 Plan

_This is a historical record of the JMS 2.1 plan before work on JMS 2.1 ceased and JSR 386 was withdrawn_

Here's the high-level list of features planned for JMS 2.1, and an approximate order in which they will be considered. 

See also the [JMS 2.1 schedule](/jms-spec/pages/JMS21#jms-21-schedule).

For a longer list of all the open issues (many of which won't make it into JMS 2.1), see the [JMS 2.1 planning long list](/jms-spec/pages/JMS21LongList).

* auto-gen TOC:
{:toc}

## Major changes 

Minor changes, which could be considered in parallel to this, are listed under [Minor changes](/jms-spec/pages/JMS21Plan#minor-changes) below.

* Flexible JMS MDBs [JMS_SPEC-116](https://github.com/javaee/jms-spec/issues/116)
  * Latest proposals are in the [JMS 2.1 Early Draft 1](https://jcp.org/aboutJava/communityprocess/edr/jsr368/index.html)
* CDI beans as message listeners [JMS_SPEC-134](https://github.com/javaee/jms-spec/issues/134)
  * (Under discussion)
* Batch delivery to MDBs [JMS_SPEC-36](https://github.com/javaee/jms-spec/issues/36)
* New and custom acknowledgement modes
  * No acknowledge mode [JMS_SPEC-21](https://github.com/javaee/jms-spec/issues/21), [JMS_SPEC-168](https://github.com/javaee/jms-spec/issues/168)
  * Individual acknowledge mode [JMS_SPEC-95](https://github.com/javaee/jms-spec/issues/95) and related [JMS_SPEC-176](https://github.com/javaee/jms-spec/issues/176)
  * Allowing custom acknowledgement modes [JMS_SPEC-169](https://github.com/javaee/jms-spec/issues/169)
* Allowing setMessageListener in a Java EE web or EJB application
* API to create a connection factory (without using JNDI) in a Java SE application [JMS_SPEC-89](https://github.com/javaee/jms-spec/issues/89)
* API to create a queue or topic (without using JNDI) in a Java SE application [JMS_SPEC-90](https://github.com/javaee/jms-spec/issues/90)
* Repeatable annotations for resource definitions [JMS_SPEC-151](https://github.com/javaee/jms-spec/issues/151)
* Configuring message redelivery and dead message queues for MDBs [JMS_SPEC-117](https://github.com/javaee/jms-spec/issues/117)
  * redeliveryInterval
  * redeliveryLimit
  * deadMessageLookup
* Missing method createXAJMSContext() on XAJMSContext (to allow multiple XAJMSContexts to share the same connection) [JMS_SPEC-152](https://github.com/javaee/jms-spec/issues/152)
* JMS in a Java EE application: adding clarifications and removing restrictions
  * Defining the behavior of a JMS session that is created outside a JTA transaction but used to send or receive a message within a JTA transaction, and vice versa. [JMS_SPEC-129](https://github.com/javaee/jms-spec/issues/129)
  * Defining an API to allow a JMS connection factory, connection or session to opt-out of a JTA transaction [JMS_SPEC-130](https://github.com/javaee/jms-spec/issues/130)
  * Clarifying the existing restrictions on using client-acknowledgement and local transactions in a Java EE environment and removing these restrictions where possible [JMS_SPEC-131](https://github.com/javaee/jms-spec/issues/131)

## Minor changes 

We need have a separate "issue triage" session to discuss minor proposals in turn. The following minor issues are being actively promoted by members of the community (e.g. by recent updated to JIRA).

* `StreamMessage.getBody(Class<T>)` [JMS_SPEC-144](https://github.com/javaee/jms-spec/issues/144)

*  Add generics to methods currently returning raw types  [JMS_SPEC-108](https://github.com/javaee/jms-spec/issues/108)

* Clarify the difference (if any) between `JMSException.getLinkedException()` and `JMSException.getCause()`  [JMS_SPEC-113](https://github.com/javaee/jms-spec/issues/113)

## Anything else 

There's a complete list of all currently-proposed features (too many to do all of them) on the on the [JMS planning long list](/jms-spec/pages/JMS21LongList).

Note the following two requests for changes to the MDB specification (by definition this is not within our direct control).

* Allow listener method to always be determined at runtime by resource adapter [EJB_SPEC-126](https://github.com/javaee/ejb-spec/issues/126)

* Define a standard way to specify the resource adapter used by a JMS MDB [EJB_SPEC-127](https://github.com/javaee/ejb-spec/issues/127)
