# JMS specification source repository

This repository has two main branches.

The _master_ branch contains

*   The source of the JMS specification (various versions)

*   The source code for the JMS API. This is used  to generate the official Javadocs that are sent to the JCP when a new version of the JMS specification is released.

    However it is not used to directly generate the JMS API jar published in Maven Central. Instead, when a new version of the JMS specification is released, the API classes are copied to [Open Message Queue](https://github.com/javaee/openmq) and the JMS API jar built from there.    

The _gh-pages_ branch contains

* The source of the JMS specification website [https://javaee.github.io/jms-spec/](https://javaee.github.io/jms-spec/)

## Repository structure (master branch)

Directory or file | Notes
:--- | :---
`FinalRelease-1.0.1` | JMS 1.0.1 specification source (Framemaker)
`FinalRelease-1.0.2` | JMS 1.0.2 specification source (Framemaker)
`jms1.0.1a/doc` | JMS 1.0.1a API javadocs (html)
`jms1.0.1a/src` | JMS 1.0.1a API source (java)
`jms1.1/doc` | JMS 1.1 javadocs (html)
`jms1.1/jar` | JMS 1.1 API jar 
`jms1.1/specification/jms-1_1-fr-spec.pdf` | JMS 1.1 specification (PDF)
`jms1.1/specification/src` | JMS 1.1 specification source (FrameMaker .<br/> This was reconstituted manually from the 1.0.2 source (the original 1.1 source has been lost) and is not guaranteed correct.
`jms1.1/src` | JMS 1.1 API source (java)
`jms2.0/demos` |
`jms2.0/docs` |
`jms2.0/specification/word` | JMS 2.0 specification source (Word)
`jms2.0/src` | JMS 2.0 API source (java)
`jms2.0/target/jms-2.0.jar` | JMS 2.0 API jar (jar)<br>To rebuild, navigate to  `jms2.0` and type `mvn package`
`jms2.0/target/jms-2.0-javadoc.jar` | JMS 2.0 javadocs (html in a jar)<br/>To rebuild, navigate to  `jms2.0` and type `mvn javadoc:jar`
 `jms2.0a` |
 `jms2.1` |
 
