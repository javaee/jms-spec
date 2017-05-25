# JMS 2.1 Plan

Here's the high-level list of features planned for JMS 2.1, and an approximate order in which they will be considered. 

See also the [/jms-spec/pages/JMS21#JMS_2.1_schedule JMS 2.1 schedule]

For a longer list of all the open issues (many of which won't make it into JMS 2.1), see the [[JMS21LongList|JMS 2.1 planning long list]]

* auto-gen TOC:
{:toc}

## Major changes 

Minor changes, which could be considered in parallel to this, are listed under [/jms-spec/pages/JMS21Plan#Minor_changes Minor changes] below.

* Flexible JMS MDBs ([https://github.com/javaee/jms-spec/issues/116 JMS_SPEC-116])
  * Latest proposals are in the [https://jcp.org/aboutJava/communityprocess/edr/jsr368/index.html JMS 2.1 Early Draft 1]

* CDI beans as message listeners ([https://github.com/javaee/jms-spec/issues/134 JMS_SPEC-134])
  * (Under discussion)

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

## Minor changes 

We need have a separate "issue triage" session to discuss minor proposals in turn. The following minor issues are being actively promoted by members of the community (e.g. by recent updated to JIRA).

* StreamMessage#getBody(Class&lt;T&gt;) ([https://java.net/jira/browse/JMS_SPEC-144 JMS_SPEC-144])

*  Add generics to methods currently returning raw types  ([https://java.net/jira/browse/JMS_SPEC-108 JMS_SPEC-108])

* Clarify the difference (if any) between JMSException.getLinkedException() and JMSException.getCause()  ([https://java.net/jira/browse/JMS_SPEC-113 JMS_SPEC-113])

## Anything else 

There's a complete list of all currently-proposed features (too many to do all of them) on the on the [/jms-spec/pages/JMS21Planning JMS 2.1 planning page].

Note the following two requests for changes to the MDB specification (by definition this is not within our direct control).

* Allow listener method to always be determined at runtime by resource adapter ([http://java.net/jira/browse/EJB_SPEC-126 EJB_SPEC-126])

* Define a standard way to specify the resource adapter used by a JMS MDB ([http://java.net/jira/browse/EJB_SPEC-127 EJB_SPEC-127])
