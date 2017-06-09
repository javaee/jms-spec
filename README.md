# JMS specification source repository

This repository has two main branches.

The _master_ branch contains

* The source of the JMS specification (various versions)
* The source code for the JMS API. This is used  to generate the official Javadocs that are sent to the JCP when a new version of the JMS specification is released.
    However it is not used to directly generate the JMS API jar published in Maven Central. Instead, when a new version of the JMS specification is released, the API classes are copied to [Open Message Queue](https://github.com/javaee/openmq) and the JMS API jar built from there.

The _gh-pages_ branch contains
* The source of the JMS specification website [https://javaee.github.io/jms-spec/](https://javaee.github.io/jms-spec/)

This repository is used to generate the official Javadocs that are sent to the JCP when a new version of the JMS specification is released. 


*   This is a list item with two paragraphs. Lorem ipsum dolor
    sit amet, consectetuer adipiscing elit. Aliquam hendrerit
    mi posuere lectus.

    Vestibulum enim wisi, viverra nec, fringilla in, laoreet
    vitae, risus. Donec sit amet nisl. Aliquam semper ipsum
    sit amet velit.

*   Suspendisse id sem consectetuer libero luctus adipiscing.


## Repository structure (master branch)

Directory or file | Notes
:--- | :---
`FinalRelease-1.0.1` | Framemaker source of the JMS 1.0.1 specification
`FinalRelease-1.0.2` | Framemaker source of the JMS 1.0.2 specification
`jms1.1` | Source of the JMS 1.1 specification
`jms1.1/doc` | JMS 1.1 javadocs (HTML), as published
`jms1.1/specification/jms-1_1-fr-spec.pdf` | JMS 1.1 specification (PDF), as published
`jms1.1/specification/src` | JMS 1.1 specification source (FrameMaker .<br/> This was reconstituted manually from the 1.0.2 source (the original 1.1 source has been lost) and is not guaranteed correct.
`jms1.1/src` | JMS 1.1 API (java), as published
`jms1.1` | Source of the JMS 1.1 specification
`jms1.1/doc` | JMS 1.1 javadocs (HTML), as published
`jms2.0/specification/jms.pdf` | JMS 2.0 specification (PDF), latest working draft
`jms2.0/specification/src` | JMS 2,0 specification source (FrameMaker), latest working draft. 
`jms2.0/src` | JMS 2.0 API (java), latest working draft
`jms2.0/target/jms-2.0.jar/src` | JMS 2.0 API (jar), latest working draft<br/>To rebuild, navigate to  `jms2.0` and type `mvn package`
 `jms2.0/target/jms-2.0-javadoc.jar` | JMS 2.0 javadocs (HTML in a jar), latest working draft<br/>To rebuild, navigate to  `jms2.0` and type `mvn javadoc:jar`
 
