This folder contains a NetBeans project which demonstrates some features of JMS 2.0

How to get this project
-----------------------

This folder be be checked out from the jms-spec.java.net source code repository at
https://svn.java.net/svn/jms-spec~repository/jms2.0/demos/JMS20Demo
(Use your java.net user name and password. 
You need to be a member of the jms-spec.java.net project.)

Which version of GlassFish to use
---------------------------------

You will need to download and unzip an appropriate version of GlassFish 4.

You can download GlassFish 4 nightly builds from 
http://dlc.sun.com.edgesuite.net/glassfish/4.0/nightly/

This version was tested with glassfish-4.0-b78-02_21_2013

How to run it
-------------

* Start NetBeans (7.1 or later).
* Open the "services" view (Ctrl+5)
* Select the "Services" note
* Open the context menu and select "Add server".
* In the Wizard, choose a "GlassFish 3+ server" and follow the steps to add the GlassFish 4 
  installation that you should have already downloaded and unzipped.

* Now open the "Projects" view (Ctrl+1)
* Open the context menu and choose "Open project"
* In the wizard, navigate to the JMS20Demo project and open it

* In the "Projects" view, select the new "JMS20Demo" note
* Open the context view and choose "Resolve missing server problem"
* Follow the wizard to specify the version of GlassFish you just added

* In the "Projects" view, select the new "JMS20Demo" note
* Open the context menu and choose "Run"
* A web page opens showing various things you can do. 
  Each link corresponds to a different session bean
