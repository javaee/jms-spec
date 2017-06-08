# JMS 2.1

In August 2014, Oracle proposed the development of JMS 2.1, and submitted [JSR 368](https://jcp.org/en/jsr/detail?id=368) in accordance with the Java Community Process. It was intended that JMS 2.1 would form part of java EE 8.

An expert group was formed which worked for most of 2015. The specification lead was Nigel Deakin. 

In October 2015 an Early Draft was published for public review. This included a new chapter on flexible JMS message-driven beans. Work continued and the proposed improvements to JMS message-driven beans were further refined and improved. 

In early 2016 Oracle announced that it was re-assessing its priorities and work on developing JMS 2.1 halted. In November 2016 Oracle formally proposed that the JSR be withdrawn. 

Oracle confirmed that JMS would remain part of Java EE 8, but the existing version JMS 2.0 would be used rather than a new version 2.1. 

This page contains historical information about JMS 2.1, including the most recent version of the proposals for improving JMS MDBs. 

## Historical information about JMS 2.1 

* The initial proposals for JMS 2.1 are summarised in [JSR 368](https://jcp.org/en/jsr/detail?id=368)
* The [JMS 2.1 early draft](https://jcp.org/aboutJava/communityprocess/edr/jsr368/index.html) was released in October 2015. This included a new chapter on flexible JMS message-driven beans.
* JMS spec lead Nigel Deakin gave a presentation at JavaOne in October 2015 "What's coming in JMS 2.1". [Download the slides](/jms-spec/downloads/JMS%202.1/CON3942_WhatsNewInJMS21.pdf). [Watch the video](https://youtu.be/6exFuFJhfcA?t=27336). This included a summary of what was in the early draft, together with some other ideas.
* After the early draft was released, the expert group continued work on improving and refining its proposals for flexible JMS MDBs. 
  * The latest thinking is summarised in [Flexible JMS MDBs: method annotations (version 5)](/jms-spec/pages/JMSListener5). The corresponding API interfaces and annotations can be seen in the source code repository [here](https://github.com/javaee/jms-spec/tree/master/jms2.1/src/main/java/javax/jms). 
  * Older versions of these proposals are [version 1](/jms-spec/pages/JMSListener), [version 2](/jms-spec/pages/JMSListener2), [version 3](/jms-spec/pages/JMSListener3) and [version 4](/jms-spec/pages/JMSListener4)_.
* The [JMS 2.1 plan](/jms-spec/pages/JMS21Plan) contains a list of the features that were planned for JMS 2.1. 
* The [JMS 2.1 Planning Long List](/jms-spec/pages/JMS21LongList) contains a classified list of all the open issues that were in the JMS issue tracker at the time that JMS 2.1 was being developed. This includes many issues that were not expected make it into JMS 2.1.
* All these issues are also recorded in the [JMS spec issue tracker](https://github.com/javaee/jms-spec/issues).

##  JMS 2.1 schedule

_This is a historical record of the JMS 2.1 schedule before work on JMS 2.1 ceased and JSR 386 was withdrawn_

Stage | Initial plan<br/>(Sep 2014) | Current plan<br/> (updated  11 Jun 2015) | Actual
:--- | :--- | :--- | :---
JSR approval | Sep 2014 |   | Sep 2014
Expert group formation | Q3 (Sep) 2014 |   | Dec 2014
Early draft review 1 |  |  |  Oct 2015
Early draft review 2 | Q1 (Mar) 2015 | Q4 (Dec) 2015 |  
Public Review | Q3 (Sep) 2015 | Q1 (Mar) 2016 | 
Proposed Final Draft | Q4 (Dec) 2015 | Q3 (Sep) 2016 |  
Final release | Q3 (Sep) 2016 | H1 (Jun) 2017 |  

This was based in the [schedule for the Java EE platform](https://www.jcp.org/en/jsr/detail?id=366) at the time.
