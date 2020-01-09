# CinemaSpace

## Description
Task 3 project for the Large-Scale and Multi-Structured Databases course of the Master of Science in Computer Engineering, University of Pisa.  
It is an extension of the previous Task 2 [(CinemaSpace)](https://github.com/diegocasu/Cinemaspace), with the addition of a recommendation system based on Neo4j.  

For more details, please refer to the specification document.

## Installation

The project can be configured to run either on a MongoDB replica set or on a MongoDB single server.  If you want to use the single server mode, 
please refer to the code inside the `single_server_version` branch.  
The Neo4j database is configured to run in single server mode.

| :warning: ATTENTION: the dataset is large and the replication process can take a long time. </br> It is suggested to configure MongoDB in single server mode. |
|:--- |

- Import the Eclipse Maven project
- Set up a MongoDB replica set or a MongoDB single server
- Set up a Neo4j single server
- Update `configuration.xml` according to the configurations of the databases (name of the set, addresses and ports of the nodes)
- Unzip the MongoDB dump file and restore the MongoDB database using `mongorestore`
- Unzip the Neo4j dump file and restore the Neo4j database using `neo4j-admin load`
- Execute from `cinemaspace.controller.CinemaSpaceLauncher`

The Neo4j dump includes all the users and films, but only a subset of 1M ratings to avoid excessive RAM occupancy.  

## Test credentials

**Normal user**  
Username: soni<span>@ponsufpo.gi  
Password: motew  

**Administrator**  
Username: admin<span>@unipi.it  
Password: admin
  
## Dataset credits

[CinemaSpace](https://github.com/diegocasu/Cinemaspace#dataset-credits)

## Contributors
[Marie Giannoni](https://github.com/mariegiannoni)  
[Shirley Caillere](https://github.com/shca10766)  
[Francisco Payés Erroa](https://github.com/fxisco) 
