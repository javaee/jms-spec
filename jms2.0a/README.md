# JMS specification source repository
*   The source code for the JMS API. This is used  to generate the official Javadocs that are sent to the JCP when a new version of the JMS specification is released.

To generate the JMS API Javadocs, run
mvn javadoc:jar

The Javadocs are generated under 'target/apidocs' directory

To generate the API jar, run
mvn package

The jar is generated under 'target/jms-2.0.jar'


