# JMS 2.0 errata release (Rev a)

This page describes the JMS 2.0 errata release (JMS 2.0 revision a)

* auto-gen TOC:
{:toc}

## Introduction 

A JMS 2.0 errata was needed to correct a number of errors in JMS 2.0 which couldn't wait for JMS 2.1 because they affected the ability of implementers to implement JMS 2.0 and conform to the TCK.

An "errata" is a simple kind of maintenance release intended purely to correct errors in the spec, and which does not define a new version of the spec or define new features. This errata consists of a new JMS 2.0 spec document and a new set of JMS 2.0 javadocs, but there were be no new compatibility tests and it was not necessary to make any changes to the reference implementation or any existing JMS 2.0 implementations.

One of the errors was a bug in the JMS API jar ([JMS spec issue 161](https://github.com/javaee/jms-spec/issues/161)). To fix that we needed to release a new version of that jar. This jar is actually part of the RI, not the spec, but the bug was included here for convenience.

The following page explains what an "errata" is: [https://java.net/projects/javaee-spec/pages/JCPProcesses](https://java.net/projects/javaee-spec/pages/JCPProcesses).

The JCP process for a maintenance release is [https://jcp.org/en/procedures/jcp2#5](https://jcp.org/en/procedures/jcp2#5
)

This errata was proposed by [Nigel Deakin](mailto:nigel.deakin@oracle.com) (Oracle), JSR 343 (JMS 2.0) maintenance lead. Errata releases (and maintenance releases in general) don't have an expert group. Everyone in the JMS community was invited to comment and contribute, including members of the new JSR 368 (JMS 2.1) expert group, former former members of the old JSR 343 (JMS 2.0) expert group, and subscribers to users@jms-spec.java.net.

The errata was released on 16th March 2015 and may be downloaded [here](https://jcp.org/aboutJava/communityprocess/mrel/jsr343/index.html).

<a name="review1IssueList"/>

## Issue list 

This table lists all the error reports and requests for clarification that have been received subsequent to the original release of JMS 2.0, and shows the disposition of each. 

This list does not include proposals for new features in JMS 2.1.

[Query](https://github.com/javaee/jms-spec/issues?utf8=%E2%9C%93&q=is%3Aissue%20label%3Ajms20-errata%20) (`tag=jms20-errata`)


ID | Description | Nature | Status
:--- | :--- | :--- | :---
[JMS_SPEC-167](https://github.com/javaee/jms-spec/issues/167) | Session javadoc should mention consumer.close is allowed outside thread of control | Document error | Accepted for JMS 2.0 errata.
[JMS_SPEC-165](https://github.com/javaee/jms-spec/issues/165) | Error in javadoc for Connection#stop and JMSContext#stop | Document error | Accepted for JMS 2.0 errata.
[JMS_SPEC-164](https://github.com/javaee/jms-spec/issues/164) | Typos in section 7.3.5 and 12.2: Change Session to MessageProducer | Document error | Accepted for JMS 2.0 errata.
[JMS_SPEC-163](https://github.com/javaee/jms-spec/issues/163) | Javadoc for JMSContext#setClientID contains obsolete MessageContext reference | Document error | Accepted for JMS 2.0 errata.
[JMS_SPEC-162](https://github.com/javaee/jms-spec/issues/162) | Typos in section 7.3 "Asynchronous send" | Document error | Accepted for JMS 2.0 errata.
[JMS_SPEC-161](https://github.com/javaee/jms-spec/issues/161) | serialVersionUID of JMSException has changed from JMS 1.1 to JMS 2.0 | Compatibility error | Accepted for JMS 2.0 errata.
[JMS_SPEC-160](https://github.com/javaee/jms-spec/issues/160) | JMS API source contains self-closing HTML tags | Typo | Accepted for JMS 2.0 errata
[JMS_SPEC-158](https://github.com/javaee/jms-spec/issues/158) | JMS 2.0 introduced incompatible changes to Connection.stop and close and Session.close | Compatibility error | Accepted for JMS 2.0 errata.
[JMS_SPEC-157](https://github.com/javaee/jms-spec/issues/157) | JMS 2.0 introduced an incompatible restriction on creating two sessions per connection in Java EE | Compatibility error | Accepted for JMS 2.0 errata.
[JMS_SPEC-156](https://github.com/javaee/jms-spec/issues/156) | JMS does not adequately define the behaviour of getAcknowledgeMode, getTransacted and getSessionMode in Java EE | Request for clarification | Deferred until JMS 2.1
[JMS_SPEC-155](https://github.com/javaee/jms-spec/issues/155) | JMS 2.0 introduced incompatible changes to createSession(bool,int) | Compatibility error | Accepted for JMS 2.0 errata.
Added to  [JMS_SPEC-138](https://github.com/javaee/jms-spec/issues/138) | Clarify behaviour of createContext when used on a QueueConnectionFactory or TopicConnectionFactory  |  | Deferred until JMS 2.1.<br/>This is similar to existing issue  [JMS_SPEC-138](https://github.com/javaee/jms-spec/issues/138), which requests clarification of the behaviour of createConnection with these objects. 
[JMS_SPEC-133](https://github.com/javaee/jms-spec/issues/133) | Update javadoc comments for QueueConnection#createQueueSession and TopicConnection#createTopicSession | Document error | Accepted for JMS 2.0 errata.
[JMS_SPEC-128](https://github.com/javaee/jms-spec/issues/128) | Typo in section 4.14 "Queue" | Document error | Accepted for JMS 2.0 errata.
[JMS_SPEC-127](https://github.com/javaee/jms-spec/issues/127) | Incorrect HTML in API documentation | Document error.  | These were corrected by the spec lead soon after the original JMS 2.0 release <br/>[Diffs of javadoc for Message](https://github.com/javaee/jms-spec/issues/127#issuecomment-298589718)<br/>[Diffs of javadoc for JMSProducer](https://github.com/javaee/jms-spec/issues/127#issuecomment-298589719)<br/>[Diffs of javadoc for MapMessage](https://github.com/javaee/jms-spec/issues/127#issuecomment-298589720)<br/>[Diffs of javadoc for StreamMessage](https://github.com/javaee/jms-spec/issues/127#issuecomment-298589721)
[JMS_SPEC-125](https://github.com/javaee/jms-spec/issues/125) | Define whether a JMS provider should call reset after sending a BytesMessage asynchronously | Request for clarification. | Accepted for JMS 2.0 errata.
[JMS_SPEC-122](https://github.com/javaee/jms-spec/issues/122) | Typos in javadocs for ConnectionFactory.createContext  | Document error | Accepted for JMS 2.0 errata.
[JMS_SPEC-120](https://github.com/javaee/jms-spec/issues/120) | Typo: in example, change .class() to .class | Document error | Accepted for JMS 2.0 errata.
[JMS_SPEC-119](https://github.com/javaee/jms-spec/issues/119) | Remove reference to password alias | Document error | Accepted for JMS 2.0 errata.

<a name="review1ProposedChanges"/>
## Change log 

This table lists all the changes in JMS 2.0 revision a.

[Query](https://github.com/javaee/jms-spec/milestone/6?closed=1)

ID | Description | Nature | Status
:--- | :--- | :--- | :---
[JMS_SPEC-167](https://github.com/javaee/jms-spec/issues/167) | Session javadoc should mention consumer.close is allowed outside thread of control | Document error | [Changes to javadoc for Session](https://github.com/javaee/jms-spec/issues/167#issuecomment-298590579).
[JMS_SPEC-165](https://github.com/javaee/jms-spec/issues/165) | Error in javadoc for Connection#stop and JMSContext#stop | Document error | [Changes to javadoc](https://github.com/javaee/jms-spec/issues/165#issuecomment-298590536).
[JMS_SPEC-164](https://github.com/javaee/jms-spec/issues/164) | Typos in section 7.3.5 and 12.2: Change Session to MessageProducer | Document error | [Changes to specification](https://github.com/javaee/jms-spec/issues/164#issue-225650287).
[JMS_SPEC-163](https://github.com/javaee/jms-spec/issues/163) | Javadoc for JMSContext#setClientID contains obsolete MessageContext reference | Document error | [Changes to javadoc for JMSContext#setClientID](https://github.com/javaee/jms-spec/issues/163#issuecomment-298590494)].
[JMS_SPEC-162](https://github.com/javaee/jms-spec/issues/162) | Typos in section 7.3 "Asynchronous send" | Document error | [Changes to specification](https://github.com/javaee/jms-spec/issues/162#issuecomment-298590457)
[JMS_SPEC-161](https://github.com/javaee/jms-spec/issues/161) | serialVersionUID of JMSException has changed from JMS 1.1 to JMS 2.0 | Compatibility error | [Open MQ Fix details](https://github.com/javaee/openmq/issues/359#issuecomment-300257172)
[JMS_SPEC-160](https://github.com/javaee/jms-spec/issues/160) | JMS API source contains self-closing HTML tags | Typo | [Changes to javadocs for Message and JMSConsumer](https://github.com/javaee/jms-spec/issues/160#issuecomment-298590418)
[JMS_SPEC-158](https://github.com/javaee/jms-spec/issues/158) | JMS 2.0 introduced incompatible changes to Connection.stop and close and Session.close | Compatibility error | [Changes to specification and javadocs for Connection, Session and JMSContext](https://github.com/javaee/jms-spec/issues/158#issuecomment-298590370).
[JMS_SPEC-157](https://github.com/javaee/jms-spec/issues/157) | JMS 2.0 introduced an incompatible restriction on creating two sessions per connection in Java EE | Compatibility error | [Changes to specification and javadocs for Connection](https://github.com/javaee/jms-spec/issues/157#issuecomment-298590343).
[JMS_SPEC-155](https://github.com/javaee/jms-spec/issues/155) | JMS 2.0 introduced incompatible changes to createSession(bool,int) | Compatibility error | [Changes to specification (Connection only)](https://github.com/javaee/jms-spec/issues/155#issuecomment-298590302)<br/>[Changes for Connection.html#createSession(boolean, int)](https://github.com/javaee/jms-spec/issues/155#issuecomment-298590293)<br/>[Changes for Connection.html#createSession(int)](https://github.com/javaee/jms-spec/issues/155#issuecomment-298590295)
[JMS_SPEC-133](https://github.com/javaee/jms-spec/issues/133) | Update javadoc comments for QueueConnection#createQueueSession and TopicConnection#createTopicSession | Document error | [Changes to QueueConnection#createQueueSession and TopicConnection#createTopicSession](https://github.com/javaee/jms-spec/issues/133#issuecomment-298589848)
[JMS_SPEC-128](https://github.com/javaee/jms-spec/issues/128) | Typo in section 4.14 "Queue" | Document error | [Changes to specification](https://github.com/javaee/jms-spec/issues/128#issuecomment-298589741)
[JMS_SPEC-125](https://github.com/javaee/jms-spec/issues/125) | Define whether a JMS provider should call reset after sending a BytesMessage asynchronously | Request for clarification. | [Changes to javadoc for BytesMessage](https://github.com/javaee/jms-spec/issues/125#issuecomment-298589671)
[JMS_SPEC-122](https://github.com/javaee/jms-spec/issues/122) | Typos in javadocs for ConnectionFactory.createContext | Document error | [Changes to ConnectionFactory.createContext](https://github.com/javaee/jms-spec/issues/122#issuecomment-298589629)
[JMS_SPEC-120](https://github.com/javaee/jms-spec/issues/120) | Typo: in example, change .class() to .class | Document error | [Change to specification](https://github.com/javaee/jms-spec/issues/120#issuecomment-298589567)
[JMS_SPEC-119](https://github.com/javaee/jms-spec/issues/119) | Remove reference to password alias | Document error | [Change to specification](https://github.com/javaee/jms-spec/issues/119#issuecomment-298589545)

## Feedback 

Comments are still invited on any of these issues. These can be made either directly on the JIRA issue or using the [users@jms-spec.java.net](mailto:users@jms-spec.java.net) mailing list. Instructions on how to join the mailing list may be found [here](https://javaee.github.io/jms-spec/#jms-community-mailing-lists).

## Timescales 

**Stage** | **Current plan**<br/>(updated 27 Jan 2015) | **Actual**
:--- | :--- | :---
Start of Informal discussions |  | Mon 24 Nov 2014  
Informal discussions          |  |  Mon 24 Nov 2014 - Fri 16 Dec 2014                    
Maintenance lead sends list of proposed changes to JCP |  |  Fri 23 Jan 2015          
Formal public maintenance review period |  |  Mon 21 Jan 2015 - Sun 22 Feb 2015 
Start of EC ballot: | Mon 23 Feb 2015 | Mon 23 Feb 2015            
End of EC ballot: | Mon 2 Mar 2015 | Mon 2 Mar 2015 
Maintenance lead sends final materials sent to PMO | Fri 6 March 2015 | Fri 6 March 2015       
Errata release |  Mon 9 March 2015 | 

## What is the version number of this errata release? 

A Maintenance Release that includes only errata does not define a new version of the spec.  An errata update to a specification document is indicated by including a "Rev level" after the specification version number. In this case, the specification version number is "JMS 2.0 Rev a".

The fix to [JMS_SPEC-161](https://github.com/javaee/jms-spec/issues/161)  required a new version of the JMS API jar.
This again does not define a new version of the spec. 
However it does reflect a new version of the implementation, so its version number was 2.0.1.

[Safe Harbor Statement](/jms-spec/pages/SafeHarborStatement)
