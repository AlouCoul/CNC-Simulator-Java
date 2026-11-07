# CNC Cutting Simulator — Java Desktop Application

A Java desktop application developed as part of a collaborative software engineering project at **Université Laval**.

The application simulates CNC cutting operations through an interactive graphical interface, allowing users to configure cutting scenarios, visualize geometric operations, manage restricted cutting zones, and generate G-code.

## Key Features

- **Interactive Desktop Interface:** Graphical user interface developed with Java Swing.
- **CNC Cutting Simulation:** Model and visualize different cutting operations and geometric configurations.
- **G-code Generation:** Generate and export G-code for CNC cutting operations.
- **Restricted Zones:** Define and manage areas where cutting operations are prohibited.
- **Undo/Redo Functionality:** Manage operation history to reverse or restore changes.
- **Geometric Visualization:** Display cutting paths and configurations through graphical components.

## Technologies & Concepts

| Category | Technologies |
|---|---|
| Programming Language | Java |
| Desktop GUI | Java Swing |
| Build & Dependency Management | Apache Maven |
| Software Design | Object-Oriented Programming, modular architecture, MVC-style separation |
| Core Concepts | Geometric modeling, event-driven programming, operation history |

## Project Architecture

The application source code is organized under `CNC/src/main/java/ca/ulaval/glo2004/`.

```text
CNC/
├── pom.xml
└── src/main/java/ca/ulaval/glo2004/
    ├── Main.java
    ├── domain/
    │   └── dto/
    ├── gui/
    │   └── drawing/
    └── Utils/
```

**Main components:**

- `domain/` — Core CNC models, cutting operations, geometric logic, and operation history.
- `domain/dto/` — Data transfer objects used between application components.
- `gui/` — Java Swing interface and user interaction.
- `gui/drawing/` — Drawing, geometric rendering, and visualization.
- `Utils/` — Supporting utility classes.

## Getting Started

### Prerequisites

- Java Development Kit (JDK), compatible with the project's Maven configuration.
- Apache Maven, if building from source.

### Build from Source

Clone the repository:

```bash
git clone https://github.com/AlouCoul/CNC-Simulator-Java.git
cd CNC-Simulator-Java/CNC
```

Build the application:

```bash
mvn clean package
```

### Run the Application

The repository includes a prebuilt executable JAR file named `equipe10.jar`.

If Java is installed and the JAR is compatible with your environment, launch it from the repository root:

```bash
java -jar equipe10.jar
```

Alternatively, double-click the JAR file if your operating system is configured to open executable JAR files with Java.

## Screenshots

Screenshots of the application's graphical interface and CNC cutting simulations will be added here.

## Academic Project

Developed collaboratively as part of a software engineering course at **Université Laval, Canada**.

This project demonstrates practical experience in:

- Java desktop application development
- Object-oriented software design
- Modular application architecture
- Graphical user interface development
- Geometric modeling and simulation
- Collaborative software development using Git

## Contributor

**Alou Coulibaly** — Computer Science Graduate, Université Laval

[GitHub Profile](https://github.com/AlouCoul)

*This repository is a portfolio copy of an academic team project. The application was developed collaboratively; contributions are not claimed as individual work.*