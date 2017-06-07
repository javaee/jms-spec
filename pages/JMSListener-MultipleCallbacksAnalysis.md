# Flexible JMS MDBs: analysis of how to implement multiple callbacks

This page discusses the method annotations on flexible JMS MDBs

## Contents

* auto-gen TOC:
{:toc}

##  Using resource adapter 

##  Using ConnectionConsumer 

##  Using synchronous loop 

### Two separate MDBs listening on Q1 and Q2

<img src="/jms-spec/pages/images/Slide1.JPG">

### A single MDB with twice as many threads and instances, listening on both Q1 and Q2

<img src="/jms-spec/pages/images/Slide2.JPG">
