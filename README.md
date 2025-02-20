# java-gpuslave

This project is a simple Java application built with Gradle, demonstrating basic project structure, unit testing with JUnit, and documentation with Javadoc.

## Table of Contents

- [java-gpuslave](#java-gpuslave)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [Prerequisites](#prerequisites)
  - [Getting Started](#getting-started)
    - [Cloning the Repository](#cloning-the-repository)
    - [Building the Project](#building-the-project)
  - [Running the Application](#running-the-application)
  - [Running Tests](#running-tests)
  - [Generating Javadoc](#generating-javadoc)
  - [Project structure](#project-structure)
  - [Continuous Integration (CI)](#continuous-integration-ci)

## Introduction

This project serves as a basic template for Java projects using Gradle as the build automation tool. The project is set up to easily generate documentation using Javadoc.

## Prerequisites

*   **Java Development Kit (JDK):** Version 21 or higher is recommended.
*   **Gradle:** This project uses the Gradle Wrapper, so you don't need to install Gradle separately. The wrapper ensures that the correct Gradle version is used.

## Getting Started

### Cloning the Repository

Clone the repository to your local machine using Git:

```bash
git clone <repository_url>
cd java-gpuslave
```

### Building the Project

Use the Gradle Wrapper to build the project:

```bash
./gradlew build
```

## Running the Application

To run the application, execute the following Gradle command:

```bash
./gradlew run
```

## Running Tests

To test the application, execute the following Gradle command:

```bash
./gradlew test
```
Test report you can find in `app/build/reports/tests/test/index.html`

## Generating Javadoc

To generate the Javadoc documentation, execute the following Gradle task:

```bash
./gradlew javadoc
```

This will generate the Javadoc documentation in the `build/docs/javadoc` directory. You can then open `build/docs/javadoc/index.html` in your browser to view the documentation.

## Project structure

`src/main/java`: Contains the source code for the application.
`src/test/java`: Contains the unit tests.
`build.gradle.kts`: The main Gradle build file for the entire project.
`settings.gradle.kts`: Specifies the project name and included subprojects.
`run-java.yaml`: CI Workflow file

## Continuous Integration (CI)
This project uses GitHub Actions for Continuous Integration. The workflow is defined in `run-java.yaml`.