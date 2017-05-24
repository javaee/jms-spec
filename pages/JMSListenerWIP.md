# More flexible JMS MDBs (Version 3) DRAFT DRAFT DRAFT DRAFT

This page contains version 3 of proposals to simplify the configuration of JMS MDBs in JMS 2.1 and Java EE 8. 

Comments are invited, especially to the various issues mentioned. See [/jms-spec/pages/JMS21#How_to_get_involved_in_JMS_2.1 How to get involved in JMS 2.1].

See the [[#Changes_from_version_2|summary of changes]] compared to [[JMSListener2|Version 2 of these proposals]].

* Issues I1 to I16 were listed in version 1
* Issues I17 to I23 were added in version 2
* Issues I24 to ? were added in version 3

* auto-gen TOC:
{:toc}

## Changes from version 2 

The major issues which still need to be decided are:

* Issue I17: Should multiple callback methods be permitted? As issue I18 describes, there is an argument that allowing multiple callback methods may be confusing for developers, who may not realise the concurrency implications (i.e. that defining multiple callbacks reduces the number of MDB instances available to process each callback unless the MDB poolsize is increased.). It may also make implementation more complex for application servers that automatically calculate the size of the MDB pool. It may also require an excessive amount of extra work for vendors which offer monitoring and management features for JMS MDBs, since they might need to be extended to allow each callback to be managed separately. Finally, it introduces an ambiguity as to how old-style activation properties relate to multiple callback methods. For example, what is the effect of setting the activation property clientId? when there are multiple callback methods, each using a separate connection?  

## List of issues 

### Issues added in version 3

### Issues added in version 2

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I17:</b> Should multiple callback methods be permitted or should MDBs be restricted to a single callback method as in the previous version?. There is an argument that allowing multiple callback methods may be confusing for developers, who may not realise the concurrency implications. It may also make implementation more complex for application servers that automatically calculate the size of the MDB pool. Comments are invited.
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I18:</b> Should we relax the requirement for each callback method (other than  the `javax.jms.MessageListener` method `onMessage(Message m)`) to be annotated with `@JMSListener`, and allow  the presence of <i>any</i> of the annotations `@JMSConnectionFactory`, `@JMSListener`, `@SubscriptionDurability`, `@ClientId`, `@SubscriptionName`, `@MessageSelector` or `@Acknowledge` to be sufficient to designate a callback method?
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I19:</b> This requirement may be a cause of unexpected errors, since the resource adapter has no way to verify at deployment  time whether the MDB has been configured to specify that its listener interface is `JMSMessageDrivenBean`. The `endpointActivation` method has no access to this information. This means that the resource adapter will only discover when it tries to deliver a message that the message endpoint does not implement the callback method. Note that  although `endpointActivation` has access to an instance of `MessageEndpointFactory` this cannot be used to examine what methods are implemented by the endpoint class since  it may not be valid to call `createEndpoint` until after deployment has completed. Note that  [https://java.net/jira/browse/EJB_SPEC-126 EJB_SPEC-126]  would remove this issue.
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I20:</b> Is this an adequate definition of the required behaviour when parameters of the callback method cannot be set?
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I21:</b> Is this an adequate definition of the required behaviour when a callback method throws an exception?
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I22:</b> How about replacing `@JMSListener` with separate `@JMSQueueListener` and `@JMSTopicListener` annotations? This would remove the need for a separate "type" attribute. 
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I23:</b> Currently the `acknowledgeMode` activation property is rather confusing, as it is ignored when the bean is configured to use container-managed transactions. It is only used when the MDB is configured to use bean-managed transactions, such as with the class-level annotation `@TransactionManagement(TransactionManagementType.BEAN)`.  The same confusion will apply if we define the new `@Acknowledge` annotation to work the same way as `acknowledgeMode`. 

In fact if you want the MDB to consume messages without a transaction and using automatic-acknowledgement then all you need to do is to set `@TransactionManagement(TransactionManagementType.BEAN)`. You don't actually need to set the `acknowledgeMode` activation property, since it defaults to auto-ack anyway. The only reason you ever need to use the `acknowledgeMode` activation property is if you wanted to specify `DUPS_OK`.  

We can't change the behaviour of `acknowledgeMode`, but it would be better if we could replace the existing `@TransactionManagement` annotation and the proposed `@AcknowledgeMode` annotation with a single annotation which could define both at the same time. 
</td></tr></table>

### Issues added in version 1

To help track comments and changes, I've listed all the known issues that have been raised below. 

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I1:</b> Any alternative proposals for `JMSMessageDrivenBean`?
Deleted in version 2
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I2:</b>Any alternative proposals for `JMSListener`? 
Deleted in version 2
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I3:</b> The EJB specification does not define a standard way to associate a MDB with a resource adapter. JMS MDBs that require the use of a resource adapter will continue to need to specify the resource adapter in a non-portable way, either using the app-server-specific deployment descriptor (e.g. `glassfish-ejb-jar.xml`) or by using a default resource adapter provided by the application server.  (Note that it is hoped that the EJB specification can be updated to define a standard way to associate a MDB with a resource adapter. See [https://java.net/jira/browse/EJB_SPEC-127 EJB_SPEC-127])
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I4:</b> The current proposal is that a JMS MDB has a single listener method. However EJB 3.2 section 5.4.2 and JCA 1.7 does allow a MDB to have more than one listener method, with the resource adapter deciding which method is invoked. Is this something we would want to allow? Would these be alternative callback methods for the same consumer, with the container choosing which one to call depending on the message and the method signature, or would these represent two completely different consumers, on different destinations? 
Deleted (superseded by New Issue I17)
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I5:</b> It would be desirable to avoid the need to implement  `javax.jms.JMSMessageDrivenBean` since this is needed purely to satisfy EJB 3.2.   [https://java.net/jira/browse/EJB_SPEC-115 EJB_SPEC-115]  and  [https://java.net/jira/browse/EJB_SPEC-126 EJB_SPEC-126] propose removal of this requirement from the next version of EJB.  
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I6:</b> The reason why these annotations cannot be applied to the `onMessage` method of a `MessageListener` is that `MessageListener` is not a no-method interface, which means the resource adapter cannot access the methods of the MDB implementation class. It may be possible to change the EJB specification to allow this restriction to be removed.
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I7:</b> Any alternative proposals for the `@JMSConnectionFactory`, `@Acknowledge`, `@SubscriptionDurability`, `@ClientId`, `@SubscriptionName` or `@MessageSelector` annotations?
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I8:</b> Should JMS define a set of deployment descriptor which correspond to these annotations, and which can be used by a deployer to override them? This would require a major change to the EJB and JCA specs since a resource adapter cannot currently access the deployment descriptor. A slightly simpler alternative might be to require the EJB container to convert these deployment descriptor elements to activation properties and pass them to the resource adapter in the activation spec.
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I9:</b> What happens if the same attribute is specified using an activation property and using one of these new annotations? It is proposed that a value defined using an activation property always overrides the same property defined using one of these new annotations, since this would provide a way to override these new annotations in the deployment descriptor.
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I10:</b> Is an additional annotation required to allow non-standard properties to be passed to the resource adapter or container? Or are activation properties adequate for this purpose?
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I11:</b> Allowing the queue or topic to be specified by destination name rather than by JNDI name is a new feature. Since it is not portable, is this actually desirable? 
Deleted in version 2
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I12:</b> The EJB and Java EE specifications currently define a number of other ways of  defining the destination used by the MDB, such as by setting the `mappedName` attribute of the `@MessageDriven` annotation. The specification will need to clarify the override order used if the destination is specified in multiple ways.
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I13:</b> Is it right that the  `@JMSListener` attribute `"type"` is mandatory,? The existing EJB 3.2 activation property `destinationType` does not define a default value. Should it remain optional, in which case should the specification designate a default value when  `@JMSListener` is used?
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I14:</b> Should the `@SubscriptionDurability`, `@SubscriptionName` and `@ClientId` annotations (or perhaps the first two) be combined into a single annotation?
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I15:</b> Any alternative proposals for the `@MessageProperty` or `MessageHeader` annotations? 
Deleted in version 2
</td></tr></table>

<table> <tr style="background-color:#f8f8f8;"> <td style="text-align:left;">
<b>Issue I16:</b> The table above is based on the principle that if a parameter cannot be set to the required value due to it having an unsuitable type then it should simple be set to null (or a default primitive value) rather than throwing an exception and triggering message delivery. This is because there is no point in redelivering the message since it will simply fail again (and JMS does not define any dead message functionality). Is this the correct approach? 
Deleted in version 2
</td></tr></table>

## Summary and links to javadocs 

The draft javadocs can be found [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/index.html?javax/jms/package-summary.html here]. Direct links to the javadocs for each class are given in the table below.

{|- border="1"
! New or modified?
! Interface or annotation?
! Name
| Link to javadocs
|-
| New
| Marker interface
| `javax.jms.JMSMessageDrivenBean`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSMessageDrivenBean.html javadocs]
|-
| New
| Method annotation
| `javax.jms.JMSListener`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSListener.html javadocs]
|-
| Modified
| Method or field annotation
| `javax.jms.JMSConnectionFactory`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSConnectionFactory.html javadocs]
|-
| New
| Method annotation
| `javax.jms.Acknowledge`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/Acknowledge.html javadocs]
|-|-
| New
| Method annotation
| `javax.jms.SubscriptionDurability`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/SubscriptionDurability.html javadocs]
|-
| New
| Method annotation
| `javax.jms.SubscriptionName`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/SubscriptionName.html javadocs]
|-
| New
| Method annotation
| `javax.jms.ClientId`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/ClientId.html javadocs]
|-
| New
| Method annotation
| `javax.jms.JMSListenerProperty`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSListenerProperty.html javadocs]
|-
| New
| Method annotation
| `javax.jms.JMSListenerProperties`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/JMSListenerProperties.html javadocs]
|-
| New
| Method annotation
| `javax.jms.MessageSelector`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageSelector.html javadocs]
|-
| New
| Parameter annotation
| `javax.jms.MessageHeader`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageHeader.html javadocs]
|-
| New
| Parameter annotation
| `javax.jms.MessageProperty`
| [https://jms-spec.java.net/2.1-SNAPSHOT/apidocs/javax/jms/MessageProperty.html javadocs]
|} 