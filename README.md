# Architecture builder

[![Build Status](https://travis-ci.org/dvoraka/architecture-builder.svg?branch=master)](https://travis-ci.org/dvoraka/architecture-builder)
[![codecov](https://codecov.io/gh/dvoraka/architecture-builder/branch/master/graph/badge.svg)](https://codecov.io/gh/dvoraka/architecture-builder)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/87c3fd4174c74af7b7a35f717d6c9afe)](https://www.codacy.com/app/dvoraka/architecture-builder?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dvoraka/architecture-builder&amp;utm_campaign=Badge_Grade)

It's an application/project builder from prebuilt structures. You can use smaller blocks to build your application
dynamically (generate source code and all other stuff). There are high-level Java templates for building the whole
project. For instance, micro-service template is for building new micro-service with known architecture. It possible
to use your own abstract types with the template or build your own template.

Prototype currently works with Java 8 only.

## Building blocks

Description of main building blocks you can use.

### Directory

Data structure for defining a part of a project. You can describe filesystem directory, abstract type implementation,
build configuration and many other situations.

### Architecture template

Architecture template uses **Directory** tree and encapsulates it. It's used for defining a concrete architecture and
making it configurable, like name of the base package, main abstract class, etc.

### Source template

For Java classes it's possible to use configurable source templates. For instance, Spring Boot application class
is usually very similar and you only need to choose the name of the application. The templates are used in Directories.

### Configuration template

It's a template for any configuration, like build configuration. The templates are used in Directories.

### Text template

Template for any text based data. It could be used so far simple text builder to build dynamic text templates.
Used with Directories.

## Examples

### Micro-service template

It creates the whole micro-service skeleton based on known abstraction.

Starting abstraction is (from sample package in the project):
```text
microservice/
├── data
│   ├── BaseException.java
│   ├── message
│   │   ├── BaseMessage.java
│   │   ├── Message.java
│   │   ├── RequestMessage.java
│   │   ├── ResponseMessage.java
│   │   └── ResultMessage.java
│   └── ResultData.java
├── Management.java
├── net
│   ├── Acknowledgment.java
│   ├── BaseNetComponent.java
│   ├── GenericNetComponent.java
│   ├── NetMessageListener.java
│   ├── receive
│   │   ├── BaseNetReceiver.java
│   │   └── NetReceiver.java
│   ├── send
│   │   ├── AbstractNetSender.java
│   │   └── NetSender.java
│   └── ServiceNetComponent.java
├── server
│   └── AbstractServer.java
└── service
    └── BaseService.java
```

And generated project structure:

```text
budget-service/
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    └── main
        ├── java
        │   └── test
        │       └── microservice
        │           ├── BudgetApp.java
        │           ├── configuration
        │           │   └── BudgetConfig.java
        │           ├── data
        │           │   ├── BudgetData.java
        │           │   └── message
        │           │       ├── BudgetMessage.java
        │           │       └── BudgetResponseMessage.java
        │           ├── exception
        │           │   └── BudgetException.java
        │           ├── net
        │           │   ├── BudgetNetAdapter.java
        │           │   ├── BudgetNetComponent.java
        │           │   ├── BudgetNetReceiver.java
        │           │   └── BudgetNetResponseReceiver.java
        │           ├── server
        │           │   └── BudgetServer.java
        │           └── service
        │               ├── BudgetService.java
        │               └── DefaultBudgetService.java
        └── resources
            └── application.properties
```

And then you can insert your logic easily into prepared structure.
