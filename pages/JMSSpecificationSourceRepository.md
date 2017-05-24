# JMS specification source repository

This page contains some notes on the [http://java.net/projects/jms-spec/sources/repository/show JMS specification source repository].

{|- border="1"
! Directory or file
! Notes
|-
| <tt>FinalRelease-1.0.1</tt>
| Framemaker source of the JMS 1.0.1 specification
|-
| <tt>FinalRelease-1.0.2</tt>
| Framemaker source of the JMS 1.0.2 specification
|-
| <tt>jms1.1</tt> 
| Source of the JMS 1.1 specification
|- 
| <tt>jms1.1/doc</tt> 
| JMS 1.1 javadocs (HTML), as published
|- 
| <tt>jms1.1/specification/jms-1_1-fr-spec.pdf</tt> 
| JMS 1.1 specification (PDF), as published
|- 
| <tt>jms1.1/specification/src</tt> 
| JMS 1.1 specification source (FrameMaker .<br/> This was reconstituted manually from the 1.0.2 source (the original 1.1 source has been lost) and is not guaranteed correct.
|- 
| <tt>jms1.1/src</tt> 
| JMS 1.1 API (java), as published
|-
| <tt>jms1.1</tt> 
| Source of the JMS 1.1 specification
|- 
| <tt>jms1.1/doc</tt> 
| JMS 1.1 javadocs (HTML), as published
|- 
| <tt>jms2.0/specification/jms.pdf</tt> 
| JMS 2.0 specification (PDF), latest working draft
|- 
| <tt>jms2.0/specification/src</tt> 
| JMS 2,0 specification source (FrameMaker), latest working draft. 
|- 
| <tt>jms2.0/src</tt> 
| JMS 2.0 API (java), latest working draft
|- 
| <tt>jms2.0/target/jms-2.0.jar/src</tt> 
| JMS 2.0 API (jar), latest working draft<br/>
To rebuild, navigate to  <tt>jms2.0</tt> and type <tt>mvn package</tt>
|- 
| <tt>jms2.0/target/jms-2.0-javadoc.jar</tt> 
| JMS 2.0 javadocs (HTML in a jar), latest working draft<br/>
To rebuild, navigate to  <tt>jms2.0</tt> and type <tt>mvn javadoc:jar</tt>
|}

Please note that any version of JMS 2.0 spec,  API or javadocs found in the repository is a working draft only and is subject to constant change (and at the time of writing is unchanged from 1.1) .  
 