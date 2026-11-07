# CNC Cutting Simulator — Java Desktop Application

A desktop application developed in Java as part of a collaborative software engineering project at Université Laval.

The application models CNC cutting operations and provides a graphical interface for configuring and visualizing cutting scenarios.

## Features

- Graphical desktop interface built with Java Swing
- CNC cutting and geometric modeling
- Support for different cutting operations
- G-code generation and export
- Management of restricted cutting zones
- Undo/redo functionality through operation history
- Interactive visualization of cutting configurations

## Technologies

- **Language:** Java
- **GUI:** Java Swing
- **Build tool:** Maven
- **Concepts:** Object-Oriented Programming, MVC-style separation of responsibilities, geometric modeling, event-driven programming

## Project Structure

The main application is located in `CNC/`.

- `domain/` — Business logic, cutting operations, and CNC models
- `dto/` — Data transfer objects
- `gui/` — Graphical user interface components
- `gui/drawing/` — Drawing and visualization utilities
- `Utils/` — Supporting utility classes

## Getting Started

### Requirements

- Java Development Kit (JDK)
- Apache Maven

### Build

From the `CNC` directory:

```bash
mvn clean package
```

The project also includes a prebuilt JAR file in the repository root.

## Academic Context

This project was developed collaboratively for a software engineering course at Université Laval.

It demonstrates practical experience with Java desktop development, object-oriented design, modular architecture, and collaborative software development.

## Author

**Alou Coulibaly** — Computer Science Graduate, Université Laval

GitHub: [AlouCoul](https://github.com/AlouCoul)