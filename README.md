# CLIAR — pronounced “clear” (/klɪər/)

A simple drop-in Java class for parsing command-line arguments.

## Features
- Single-class parser, easy to include in any Java project
- Supports single character flags
- Provides validation with helpful exceptions

## Usage Example

Include `Arguments.java` in your project and use it in your own `main` class:

```java
public class Main {
    public static void main(String[] args) {
        try {
            Arguments arguments = Arguments.from(args);

            // ...
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage()); // and/or display help message
            System.exit(-1);
        }

        // ...
    }
}