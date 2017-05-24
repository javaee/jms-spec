# JMS 2.0 errata release (Rev a)

This page describes the JMS 2.0 errata release (JMS 2.0 revision a)

__TOC__

## Introduction 

A JMS 2.0 errata was needed to correct a number of errors in JMS 2.0 which couldn't wait for JMS 2.1 because they affected the ability of implementers to implement JMS 2.0 and conform to the TCK.

An "errata" is a simple kind of maintenance release intended purely to correct errors in the spec, and which does not define a new version of the spec or define new features. This errata consists of a new JMS 2.0 spec document and a new set of JMS 2.0 javadocs, but there were be no new compatibility tests and it was not necessary to make any changes to the reference implementation or any existing JMS 2.0 implementations.

One of the errors was a bug in the JMS API jar (see JMS_SPEC-161 [[JMS20RevA#Proposed_content | below]]). To fix that we needed to release a new version of that jar. This jar is actually part of the RI, not the spec, but the bug was included here for convenience.

The following page explains what an "errata" is
https://java.net/projects/javaee-spec/pages/JCPProcesses

The JCP process for a maintenance release is
https://jcp.org/en/procedures/jcp2#5

This errata was proposed by [mailto:nigel.deakin@oracle.com Nigel Deakin] (Oracle), JSR 343 (JMS 2.0) maintenance lead. Errata releases (and maintenance releases in general) don't have an expert group. Everyone in the JMS community was invited to comment and contribute, including members of the new JSR 368 (JMS 2.1) expert group, former former members of the old JSR 343 (JMS 2.0) expert group, and subscribers to users@jms-spec.java.net.

The errata was released on 16th March 2015 and may be downloaded [https://jcp.org/aboutJava/communityprocess/mrel/jsr343/index.html here].

<a name="review1IssueList"/>

## Issue list 

This table lists all the error reports and requests for clarification that have been received subsequent to the original release of JMS 2.0, and shows the disposition of each. 

This list does not include proposals for new features in JMS 2.1.

[http://java.net/jira/secure/IssueNavigator.jspa?reset=true&customfield_10002=jms20-errata Query]  (<tt>tag=jms20-errata</tt>)

{|- border="1"
! ID
! Description
! Nature
! Status
|-
| [http://java.net/jira/browse/JMS_SPEC-167 JMS_SPEC-167]
| Session javadoc should mention consumer.close is allowed outside thread of control
| Document error
| Accepted for JMS 2.0 errata.
|-
| [http://java.net/jira/browse/JMS_SPEC-165 JMS_SPEC-165]
| Error in javadoc for Connection#stop and JMSContext#stop
| Document error
| Accepted for JMS 2.0 errata.
|-
| [http://java.net/jira/browse/JMS_SPEC-164 JMS_SPEC-164]
| Typos in section 7.3.5 and 12.2: Change Session to MessageProducer
| Document error
| Accepted for JMS 2.0 errata.
|-  
| [http://java.net/jira/browse/JMS_SPEC-163 JMS_SPEC-163]
| Javadoc for JMSContext#setClientID contains obsolete MessageContext reference
| Document error
| Accepted for JMS 2.0 errata.
|-  
| [http://java.net/jira/browse/JMS_SPEC-162 JMS_SPEC-162]
| Typos in section 7.3 "Asynchronous send"
| Document error
| Accepted for JMS 2.0 errata.
|-  
| [http://java.net/jira/browse/JMS_SPEC-161 JMS_SPEC-161]
| serialVersionUID of JMSException has changed from JMS 1.1 to JMS 2.0
| Compatibility error
| Accepted for JMS 2.0 errata.
|-  
| [http://java.net/jira/browse/JMS_SPEC-160 JMS_SPEC-160]
| JMS API source contains self-closing HTML tags
| Typo
| Accepted for JMS 2.0 errata.]
|-  
| [http://java.net/jira/browse/JMS_SPEC-158 JMS_SPEC-158]
| JMS 2.0 introduced incompatible changes to Connection.stop and close and Session.close
| Compatibility error
| Accepted for JMS 2.0 errata.
|-  
| [http://java.net/jira/browse/JMS_SPEC-157 JMS_SPEC-157]
| JMS 2.0 introduced an incompatible restriction on creating two sessions per connection in Java EE
| Compatibility error
| Accepted for JMS 2.0 errata.
|-  
| [http://java.net/jira/browse/JMS_SPEC-156 JMS_SPEC-156]
| JMS does not adequately define the behaviour of getAcknowledgeMode, getTransacted and getSessionMode in Java EE
| Request for clarification
| Deferred until JMS 2.1
|-  
| [http://java.net/jira/browse/JMS_SPEC-155 JMS_SPEC-155]
| JMS 2.0 introduced incompatible changes to createSession(bool,int)
| Compatibility error
| Accepted for JMS 2.0 errata.
|-
| Added to  [http://java.net/jira/browse/JMS_SPEC-138 JMS_SPEC-138]
| Clarify behaviour of createContext when used on a QueueConnectionFactory or TopicConnectionFactory 
| 
| Deferred until JMS 2.1.<br/>This is similar to existing issue  [http://java.net/jira/browse/JMS_SPEC-138 JMS_SPEC-138] , which requests clarification of the behaviour of createConnection with these objects. 
|-  
| [http://java.net/jira/browse/JMS_SPEC-133 JMS_SPEC-133]
| Update javadoc comments for QueueConnection#createQueueSession and TopicConnection#createTopicSession
| Document error
| Accepted for JMS 2.0 errata.
|-    
| [http://java.net/jira/browse/JMS_SPEC-128 JMS_SPEC-128]
| Typo in section 4.14 "Queue"
| Document error
| Accepted for JMS 2.0 errata.
|-    
| [http://java.net/jira/browse/JMS_SPEC-127 JMS_SPEC-127]
| Incorrect HTML in API documentation
| Document error. 
| These were corrected by the spec lead soon after the original JMS 2.0 release <br/>
[https://java.net/jira/browse/JMS_SPEC-127?focusedCommentId=364714 Diffs of javadoc for Message]<br/>
[https://java.net/jira/browse/JMS_SPEC-127?focusedCommentId=364716 Diffs of javadoc for JMSProducer]<br/>
[https://java.net/jira/browse/JMS_SPEC-127?focusedCommentId=364717 Diffs of javadoc for MapMessage]<br/>
[https://java.net/jira/browse/JMS_SPEC-127?focusedCommentId=364718 Diffs of javadoc for StreamMessage]
|-  
| [http://java.net/jira/browse/JMS_SPEC-125 JMS_SPEC-125]
| Define whether a JMS provider should call reset after sending a BytesMessage asynchronously
| Request for clarification.
| Accepted for JMS 2.0 errata.
|-   
| [http://java.net/jira/browse/JMS_SPEC-122 JMS_SPEC-122]
| Typos in javadocs for ConnectionFactory.createContext 
| Document error
| Accepted for JMS 2.0 errata.
|-  
| [http://java.net/jira/browse/JMS_SPEC-120 JMS_SPEC-120]
| Typo: in example, change .class() to .class
| Document error
| Accepted for JMS 2.0 errata.
|-  
| [http://java.net/jira/browse/JMS_SPEC-119 JMS_SPEC-119]
| Remove reference to password alias
| Document error
| Accepted for JMS 2.0 errata.
|-  
|}


<a name="review1ProposedChanges"/>
## Change log 

This table lists all the changes in JMS 2.0 revision a.

[https://java.net/jira/browse/JMS_SPEC-167?jql=fixVersion%20%3D%20%22JMS%202.0%20rev%20A%22%20AND%20project%20%3D%20JMS_SPEC Query]  

{|- border="1"
! ID
! Description
! Nature
! Status
|-
| [http://java.net/jira/browse/JMS_SPEC-167 JMS_SPEC-167]
| Session javadoc should mention consumer.close is allowed outside thread of control
| Document error
| [https://java.net/jira/browse/JMS_SPEC-167?focusedCommentId=383844 Changes to javadoc for Session].
|-
| [http://java.net/jira/browse/JMS_SPEC-165 JMS_SPEC-165]
| Error in javadoc for Connection#stop and JMSContext#stop
| Document error
| [https://java.net/jira/browse/JMS_SPEC-165?focusedCommentId=382826 Changes to specification].
|-
| [http://java.net/jira/browse/JMS_SPEC-164 JMS_SPEC-164]
| Typos in section 7.3.5 and 12.2: Change Session to MessageProducer
| Document error
| [https://java.net/jira/browse/JMS_SPEC-164 Changes to specification].
|-  
| [http://java.net/jira/browse/JMS_SPEC-163 JMS_SPEC-163]
| Javadoc for JMSContext#setClientID contains obsolete MessageContext reference
| Document error
| [https://java.net/jira/browse/JMS_SPEC-163?focusedCommentId=382062 Changes to javadoc for JMSContext#setClientID].
|-  
| [http://java.net/jira/browse/JMS_SPEC-162 JMS_SPEC-162]
| Typos in section 7.3 "Asynchronous send"
| Document error
| [https://java.net/jira/browse/JMS_SPEC-162?focusedCommentId=382061 Changes to specification]
|-  
| [http://java.net/jira/browse/JMS_SPEC-161 JMS_SPEC-161]
| serialVersionUID of JMSException has changed from JMS 1.1 to JMS 2.0
| Compatibility error
| [https://java.net/jira/browse/MQ-359?focusedCommentId=381985 Fix details]
|-  
| [http://java.net/jira/browse/JMS_SPEC-160 JMS_SPEC-160]
| JMS API source contains self-closing HTML tags
| Typo
| [https://java.net/jira/browse/JMS_SPEC-160?focusedCommentId=382069 Changes to javadocs for Message and JMSConsumer]
|-  
| [http://java.net/jira/browse/JMS_SPEC-158 JMS_SPEC-158]
| JMS 2.0 introduced incompatible changes to Connection.stop and close and Session.close
| Compatibility error
| [https://java.net/jira/browse/JMS_SPEC-158?focusedCommentId=382772&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-382772 Changes to specification and javadocs for Connection, Session and JMSContext].
|-  
| [http://java.net/jira/browse/JMS_SPEC-157 JMS_SPEC-157]
| JMS 2.0 introduced an incompatible restriction on creating two sessions per connection in Java EE
| Compatibility error
| [https://java.net/jira/browse/JMS_SPEC-157?focusedCommentId=382448 Changes to specification and javadocs for Connection].
|-  
| [http://java.net/jira/browse/JMS_SPEC-155 JMS_SPEC-155]
| JMS 2.0 introduced incompatible changes to createSession(bool,int)
| Compatibility error
| [https://java.net/jira/browse/JMS_SPEC-155?focusedCommentId=382430 Changes to specification (Connection only)]<br/>
[https://java.net/jira/browse/JMS_SPEC-155?focusedCommentId=382142 Changes for Connection.html#createSession(boolean, int)]<br/>
[https://java.net/jira/browse/JMS_SPEC-155?focusedCommentId=382143 Changes for Connection.html#createSession(int)]
|-
| [http://java.net/jira/browse/JMS_SPEC-133 JMS_SPEC-133]
| Update javadoc comments for QueueConnection#createQueueSession and TopicConnection#createTopicSession
| Document error
| [https://java.net/jira/browse/JMS_SPEC-133?focusedCommentId=382510 Changes to QueueConnection#createQueueSession and TopicConnection#createTopicSession]
|-    
| [http://java.net/jira/browse/JMS_SPEC-128 JMS_SPEC-128]
| Typo in section 4.14 "Queue"
| Document error
| [https://java.net/jira/browse/JMS_SPEC-128?focusedCommentId=382467& Changes to specification]
|-    
| [http://java.net/jira/browse/JMS_SPEC-125 JMS_SPEC-125]
| Define whether a JMS provider should call reset after sending a BytesMessage asynchronously
| Request for clarification.
| [https://java.net/jira/browse/JMS_SPEC-125?focusedCommentId=382468 Changes to javadoc for BytesMessage]
|-   
| [http://java.net/jira/browse/JMS_SPEC-122 JMS_SPEC-122]
| Typos in javadocs for ConnectionFactory.createContext 
| Document error
| [http://java.net/jira/browse/JMS_SPEC-122 Changes to ConnectionFactory.createContext]
|-  
| [http://java.net/jira/browse/JMS_SPEC-120 JMS_SPEC-120]
| Typo: in example, change .class() to .class
| Document error
| [https://java.net/jira/browse/JMS_SPEC-120?focusedCommentId=382097 Change to specification]
|-  
| [http://java.net/jira/browse/JMS_SPEC-119 JMS_SPEC-119]
| Remove reference to password alias
| Document error
| [https://java.net/jira/browse/JMS_SPEC-119?focusedCommentId=382100 Change to specification]
|-  
|}

## Feedback 

Comments are still invited on any of these issues. These can be made either directly on the JIRA issue or using the [mailto:users@jms-spec.java.net users@jms-spec.java.net] mailing list. Instructions on how to join the mailing list may be found [/jms-spec/pages/Home#Mailing_lists_connected_to_the_jms-spec.java.net_project here].

## Timescales 

{|- border="1"                                                                                          
! Stage                                                                                                    
! Current plan<br/>(updated 27 Jan 2015)
! Actual                                                                                                 
|-                                                                                                      
| Start of Informal discussions                              
|                           
| Mon 24 Nov 2014  
|- 
| Informal discussions                                 
|                           
|  Mon 24 Nov 2014 - Fri 16 Dec 2014                    
|- 
| Maintenance lead sends list of proposed changes to JCP
|   
|  Fri 23 Jan 2015          
|- 
| Formal public maintenance review period                               
|                                  
|  Mon 21 Jan 2015 - Sun 22 Feb 2015 
|- 
| Start of EC ballot:                
| Mon 23 Feb 2015                                                          
| Mon 23 Feb 2015            
|- 
| End of EC ballot: 
| Mon 2 Mar 2015                                                                 
| Mon 2 Mar 2015       
|- 
| Maintenance lead sends final materials sent to PMO     
| Fri 6 March 2015                        
| Fri 6 March 2015       
|- 
| Errata release                                     
|  Mon 9 March 2015     
|  
|- 
|}

## What is the version number of this errata release? 

A Maintenance Release that includes only errata does not define a new version of the spec.  An errata update to a specification document is indicated by including a "Rev level" after the specification version number. In this case, the specification version number is "JMS 2.0 Rev a".

The fix to [http://java.net/jira/browse/JMS_SPEC-161 JMS_SPEC-161]  required a new version of the JMS API jar. This again does not define a new version of the spec. However it does reflect a new version of the implementation, so its version number was 2.0.1.

[[SafeHarborStatement|Safe Harbor Statement]]
