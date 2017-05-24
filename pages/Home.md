# Java Message Service Specification

Welcome to the website for the development of the Java Message Service (JMS) specification.

## Latest news (Oct 2015)

The JMS 2.1 Early Draft is now out! 
* Download it [here](https://jcp.org/aboutJava/communityprocess/edr/jsr368/index.html).
* See the [latest news on JMS 2.1](JMS21#Latest_news_on_JMS_2.1 JMS 2.1)
* Find out all about [JMS 2.1](JMS21).

## JMS 2.1

[JSR 368](https://jcp.org/en/jsr/detail?id=368) will be the next version of JMS. 
For more information see the [main JMS 2.1 page](https://java.net/projects/jms-spec/pages/JMS21).

## JMS 2.0 errata

An errata for JMS 2.0 was released in March 2015. For more information see the [JMS 2.0 errata page](JMS20RevA).

## JMS 2.0

JMS 2.0 is the latest released version of JMS and  was released on 21 May 2013. 
See the [JMS 2.0 Final Release page](JMS20FinalRelease) for information on where to find the specification and API documentation. 
That page also contains information on the new features in JMS 2.0, where to download the reference implementation, 
and provides links to various useful articles about JMS 2.0.


## About the development of the JMS specification

The Java Message Service (JMS) specification was first released in 1998, at the dawn of Java EE, with a major update in 2002. It then remained unchanged during the decade that followed. Meanwhile, technology evolved and many vendors pressed ahead with enhancements outside the specification. During that time, the rest of Java EE evolved significantly, with improvements and new features added. Despite the lack of development of the specification, JMS continued to be a popular and successful standard, with numerous competing implementations, both standalone and as part of complete Java EE stacks, and  open and closed source. 

On 1st March 2011 a Java Specification Request (JSR) was submitted to the [http://jcp.org/en/home/index Java Community Process] which proposed the development of JMS 2.0. This is [JSR 343](http://jcp.org/en/jsr/detail?id=343).  

On 21 May 2013 JMS 2.0 was released. This major update introduces a new simplified API and adds support for the injection of JMS connections into Java EE applications. It forms part of Java EE 7. Now that JMS 2.0 is released, JSR 343 moves into maintenance mode.

In August 2014 a new Java Specification Request (JSR) was submitted to the [Java Community Process](http://jcp.org/en/home/index) which proposed the development of JMS 2.1.  For more information see the [main JMS 2.1 page](https://java.net/projects/jms-spec/pages/JMS21).

The purpose of this website is to support the continuing development of the JMS specification. 
Currently it is focussed on JSR 343 (JMS 2.0) and  JSR 368  (JMS 2.1) 
by hosting their mailing lists, issue tracker, wiki and any other resources that are required. 
It runs in conjunction with the  [official JSR 343 page on the JCP website](http://jcp.org/en/jsr/detail?id=343)
and the  [official JSR 368 page on the JCP website](http://jcp.org/en/jsr/detail?id=368)
which are used mainly to conduct the formal aspects of these JSRs.

This website is a project hosted by java.net. To get the best out of this website, to join the mailing lists and use the issue tracker, you need to register with java.net and log in. 

The maintenance lead for JSR 343 (JMS 2.0) and the prospective specification lead for JSR 368 (JMS 2.1) is [Nigel Deakin](mailto:nigel.deakin@oracle.com) (Oracle). 

## Getting involved

Here are some ways to get involved in the development of the JMS specification:

*  **Read** the [main JMS 2.1 page](https://java.net/projects/jms-spec/pages/JMS21).

* If you'd like to **contribute** to the development of the JMS specification, or simply **observe** what is going on in the expert group, join the [mailing list](mailto:users@jms-spec.java.net users@jms-spec.java.net). See below for instructions on how to join.

* If you'd like to **submit proposals** for consideration by the expert group, log them as issues in the JIRA  [issue tracker](http://java.net/jira/browse/JMS_SPEC). Please read [How to use the issue tracker](http://java.net/projects/jms-spec/pages/Home#How_to_create_an_issue). We're interested in comments on JMS 2.0 as well as proposals for JMS 2.1.

* A new expert group is being formed for JSR 368 (JMS 2.1). 
Applications to **join the JSR 368 expert group** are now invited. 
See [Joining the JMS expert group](JMSExperts#Joining_the_JMS_expert_group).

* JSR 343 (JMS 2.0)  is now in maintenance mode and is administered directly by the maintenance lead. 

## JMS community mailing lists

###  Mailing lists open to everyone

First Header | Second Header
------------ | -------------
Content from cell 1 | Content from cell 2
Content in the first column | Content in the second column

Email address | Purpose | Access
------------- | ------- | ------
**users@jms-spec.java.net** | Mailing list for general discussion about the JMS specification, including JMS 2.0 and JMS 2.1. Everyone is welcome to join. 
This list can also be used to raise issues with, or participate in discussions with, the spec lead and expert group. All members of the expert group are expected to subscribe to this list.
Discussions on the **jsr-368-experts@jms-spec.java.net** mailing list are forwarded to this list to allow other members of the community to monitor the discussions of the expert group. Members of the community are welcome to join in on these discussions, but they'll need to send their replies to this list rather than directly to the expert group. |
Anyone may send email to this list. Messages from non-subscribers are moderated (to prevent spam) so there may be a delay. 

To receive messages from this list, you need to "join" the [jms-spec.java.net](http://jms-spec.java.net) project
and then subscribe to this list.  ([How to do this](https://java.net/projects/jms-spec/pages/Home#Mailing_lists_FAQ).  Messages from subscribers are not moderated.
[Online archive of past messages](http://java.net/projects/jms-spec/lists/users/archive).  

|-
| **issues@jms-spec.java.net**
| Mailing list for JIRA issue notifications. Everyone is welcome to join. <br /> <br />
Additions and updates to the [JIRA issue tracker](http://java.net/jira/browse/JMS_SPEC) are automatically sent to this list. Please don't use this list for discussions.<br />
| Before you can receive messages from or post messages to this list, you need to join the [ http://jms-spec.java.net  jms-spec.java.net] project as an observer and then subscribe to this list. ([https://java.net/projects/jms-spec/pages/Home#Mailing_lists_FAQ How to do this])<br /><br />
[http://java.net/projects/jms-spec/lists/issues/archive Online archive of past messages]
|-
| **commits@jms-spec.java.net**
| Mailing list for source code repository update notifications. <br /> <br />
Anyone by join, but there's no need to unless you are updating the source code repository yourself. Whenever there's anything new of interest an email will be sent to the user mailing list.  Please don't use this list for discussions.<br />
| Before you can receive messages from or post messages to this list, you need to join the [ http://jms-spec.java.net  jms-spec.java.net] project as an observer and then subscribe to this list. ([How to do this](https://java.net/projects/jms-spec/pages/Home#Mailing_lists_FAQ))<br /><br />
[Online archive of past messages](http://java.net/projects/jms-spec/lists/commits/archive)
|}

###  Mailing lists FAQ
;How do I join the [jms-spec.java.net](http://jms-spec.java.net) project? 
:Simply click on [Join this project](https://java.net/projects/jms-spec/watch) on the left, and you'll be instantly become a member. 
This is sometimes referred to as an "observer role". You'll need to be logged into [java.net](http://java.net). 
This will also add this project to your [My Projects](http://java.net/mypage) page.

;How to I subscribe to a mailing list?
:After joining the project, navigate to (http://java.net/projects/jms-spec/lists java.net/projects/jms-spec/lists), find the list you want to subscribe to, and press the Subscribe button.  

;I tried to reply to an email but it was rejected. Why?
: If you subscribe to users@jms-spec.java.net you'll automatically receive a copy of all emails sent to the expert group.  You're welcome to reply to any email with a comment. However if you are not a member of the expert group you'll need to change the recipient to users@jms-spec.java.net before replying, otherwise your email will be rejected. The expert group will see all emails sent to users@jms-spec.java.net.

###  Observer mailing list (not used) 
{|- border="1"
! Email address
! Purpose
! Access
|-
| **jsr-368-observers@jcp.org**
| Legacy mailing list for observers of JSR 368. <br /><br />
Being an "observer" of a JSR is an obsolete concept and although this mailing list does exist it is not used. <br /><br />
If you wish to observe what is going on with JSR 368, join the join the **users@jms-spec.java.net** mailing list using the instructions above.
| Registered observers of [http://jcp.org/en/jsr/summary?id=368 JSR 368] are automatically subscribed to this list.  However being an "observer" of a JSR is an obsolete concept and applications to be observers will not be accepted. 
<br /><br />
There is currently no online archive of past messages.<br />
|}

## JMS expert group mailing lists

There are two mailing lists that only JMS expert group members may subscribe. These are described in [[JMSExperts#JMS_expert_group_mailing_lists| JMS expert group mailing lists]].


## Issue tracker

Comments and changes to the JMS specification will be managed using  [http://java.net/jira/browse/JMS_SPEC the JIRA issue tracker].

If you would like to propose a change to the JMS specification, please [http://java.net/jira/secure/CreateIssue!default.jspa create an issue] containing a description of the proposed change, or [http://java.net/jira/secure/IssueNavigator.jspa?mode=show&createNew=true search for an existing issue] and add a comment.  You need to sign up for a java.net account and login first. 

### How to create an issue

Here are some guidelines to follow when creating a new issue:
* Please don't combine unrelated subjects in the same issue. Create a separate issue for each. 
* **Issue Type**: .Choose one of Bug, New Feature, Task or Improvement from the pop-up menu. 
** Bug: There is a problem or error in the spec, API or javadocs
** New Feature: Use this to suggest a new feature which is not in the existing spec, API or javadocs
** Task: Please don't use this  git push origin gh-pages
** Improvement:  An improvement or enhancement to an existing feature of the spec, API or javadocs
* **Summary** Please enter a meaningful one-line summary of your issue
* Priority: Please don't change this
* **Component/s** We will use this in the future to allow you to categorise your issue
* **Affects version/s** Please specify which version of the JMS specification your comment relates to. Normally this will be 1.1, the latest released version.
* Environment: Please leave blank
* **Description**: Please describe your issue in as much detail as you wish. You can add comments later.
* Original Estimate: Please leave blank
* **Attachment**: There shouldn't be much need to use this, but if necessary you can upload files here
* Issuezilla Id: Please leave blank
* Status Whiteboard: Please leave blank. We may start using this field in the future.
* Tags: Please leave blank. These should only be set by the project administrators, so if you set these yourself you'll confuse things. 

## Procedures and policies

The [http://java.net/projects/javaee-spec/pages/CompatibilityRequirements Java EE Backwards Compatibility Requirements] (on the Java EE WIki) describe the requirements to make releases of JMS 2.0  backwards compatible with previous versions.

## Important legal statement

[https://java.net/projects/jms-spec/pages/SafeHarborStatement Safe harbor statement].