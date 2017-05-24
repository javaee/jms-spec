# JMS specification source repository

This page contains some notes on the [http://java.net/projects/jms-spec/sources/repository/show JMS specification source repository].

{|- border="1"
! Directory or file
! Notes
|-
| `FinalRelease-1.0.1`
| Framemaker source of the JMS 1.0.1 specification
|-
| `FinalRelease-1.0.2`
| Framemaker source of the JMS 1.0.2 specification
|-
| `jms1.1` 
| Source of the JMS 1.1 specification
|- 
| `jms1.1/doc` 
| JMS 1.1 javadocs (HTML), as published
|- 
| `jms1.1/specification/jms-1_1-fr-spec.pdf` 
| JMS 1.1 specification (PDF), as published
|- 
| `jms1.1/specification/src` 
| JMS 1.1 specification source (FrameMaker .<br/> This was reconstituted manually from the 1.0.2 source (the original 1.1 source has been lost) and is not guaranteed correct.
|- 
| `jms1.1/src` 
| JMS 1.1 API (java), as published
|-
| `jms1.1` 
| Source of the JMS 1.1 specification
|- 
| `jms1.1/doc` 
| JMS 1.1 javadocs (HTML), as published
|- 
| `jms2.0/specification/jms.pdf` 
| JMS 2.0 specification (PDF), latest working draft
|- 
| `jms2.0/specification/src` 
| JMS 2,0 specification source (FrameMaker), latest working draft. 
|- 
| `jms2.0/src` 
| JMS 2.0 API (java), latest working draft
|- 
| `jms2.0/target/jms-2.0.jar/src` 
| JMS 2.0 API (jar), latest working draft<br/>
To rebuild, navigate to  `jms2.0` and type `mvn package`
|- 
| `jms2.0/target/jms-2.0-javadoc.jar` 
| JMS 2.0 javadocs (HTML in a jar), latest working draft<br/>
To rebuild, navigate to  `jms2.0` and type `mvn javadoc:jar`
|}

Please note that any version of JMS 2.0 spec,  API or javadocs found in the repository is a working draft only and is subject to constant change (and at the time of writing is unchanged from 1.1) .  
 