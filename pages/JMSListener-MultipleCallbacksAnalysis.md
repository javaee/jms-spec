# Flexible JMS MDBs: analysis of how to implement multiple callbacks

This page discusses the method annotations on flexible JMS MDBs

__TOC__

##  Using resource adapter 

##  Using ConnectionConsumer 

##  Using synchronous loop 

### Two separate MDBs listening on Q1 and Q2

[[image:  Slide1.JPG]]

### A single MDB with twice as many threads and instances, listening on both Q1 and Q2

[[image:  Slide2.JPG]]