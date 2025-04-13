# java-gpuslave

This project is a Java application built with Gradle that demonstrates various programming concepts including generics, mathematical expression parsing, CSV data processing, and dependency injection using reflection.

## Table of Contents

- [java-gpuslave](#java-gpuslave)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [Prerequisites](#prerequisites)
  - [Getting Started](#getting-started)
    - [Cloning the Repository](#cloning-the-repository)
    - [Building the Project](#building-the-project)
  - [Core Components](#core-components)
    - [Container](#container)
    - [ExpressionEvaluator](#expressionevaluator)
    - [CSV Parser](#csv-parser)
    - [Dependency Injection](#dependency-injection)
  - [Running the Application](#running-the-application)
  - [Running Tests](#running-tests)
  - [Generating Javadoc](#generating-javadoc)
  - [Project Structure](#project-structure)
  - [Continuous Integration (CI)](#continuous-integration-ci)

## Introduction

This project serves as a showcase for Java programming concepts and patterns. It includes implementations of:
- A generic container class similar to ArrayList
- A mathematical expression evaluator supporting variables and operations
- A CSV parser with support for structured data types
- A custom dependency injection framework using annotations and reflection

## Prerequisites

*   **Java Development Kit (JDK):** Version 21 or higher is required.
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

## Core Components

### Container

The `Container<T>` class is a generic data structure that can store elements of any type. Key features include:

- Dynamic resizing when capacity is reached
- Element access by index
- Element removal with automatic shifting
- Console output for displaying contents

Example usage:
```java
Container<Integer> container = new Container<>();
container.add(1);
container.add(2);
container.add(3);
container.print(); // Outputs: [1, 2, 3]
```

### ExpressionEvaluator

The `ExpressionEvaluator` class evaluates mathematical expressions provided as strings. Features:

- Support for basic arithmetic operations (+, -, *, /)
- Parentheses for controlling operation order
- Variable support with interactive value input
- Conversion between infix and postfix notation

Example usage:
```java
ExpressionEvaluator evaluator = new ExpressionEvaluator();
double result = evaluator.evaluate("(2 + 3) * 4");  // Returns 20.0
double resultWithVariables = evaluator.evaluate("x + y");  // Prompts for x and y values
```

### CSV Parser

The `CSV` class provides functionality for reading and processing CSV files, with specific support for employee data. Features:

- Reading CSV files with custom delimiters
- Parsing structured data into strongly-typed objects
- Object caching to reduce memory usage
- Error handling for malformed data

Example usage:
```java
List<CSV.Person> people = CSV.parseCSV("data.csv", ';');
for (CSV.Person person : people) {
    System.out.println(person.getName() + " works in " + person.getDivision().getTitle());
}
```

### Dependency Injection

The project includes a custom dependency injection framework using the `@AutoInjectable` annotation and `Injector` class. Features:

- Annotation-based field injection
- Configuration via properties files
- Support for interface-implementation mappings
- Runtime resolution of dependencies

Example usage:
```java
// In properties file: com.example.SomeInterface=com.example.SomeImplementation

public class MyBean {
    @AutoInjectable
    private SomeInterface dependency;
    
    public void doSomething() {
        dependency.method();
    }
}

// Inject dependencies
MyBean bean = new Injector("config.properties").inject(new MyBean());
bean.doSomething();
```

## Running the Application

To run the application, execute the following Gradle command:

```bash
./gradlew run
```

The application demonstrates all four core components in sequence.

## Running Tests

To test the application, execute the following Gradle command:

```bash
./gradlew test
```

The project includes extensive unit tests for all components. Test reports can be found in `app/build/reports/tests/test/index.html`.

## Generating Javadoc

To generate the Javadoc documentation, execute the following Gradle task:

```bash
./gradlew javadoc
```

This will generate the Javadoc documentation in the `app/build/docs/javadoc` directory. You can then open `app/build/docs/javadoc/index.html` in your browser to view the documentation.

## Project Structure

- `src/main/java`: Contains the source code for the application.
  - `app.gpuslave.first`: Main package containing all components.
- `src/test/java`: Contains the unit tests.
- `build.gradle.kts`: The main Gradle build file for the entire project.
- `settings.gradle.kts`: Specifies the project name and included subprojects.
- `run-java.yaml`: CI Workflow file for GitHub Actions.
- `injection.properties`: Configuration file for dependency injection.

## Continuous Integration (CI)

This project uses GitHub Actions for Continuous Integration. The workflow is defined in `run-java.yaml` and includes:
- Building the project
- Running all tests
- Generating the Javadoc documentation