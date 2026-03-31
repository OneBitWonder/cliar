# CLIAR — pronounced “clear” (/klɪər/)

A simple drop-in Java class for parsing command-line arguments.

## Features
- Single-class parser, easy to include in any Java project
- Supports single character flags
- Provides validation with helpful exceptions

## Usage Example

Include `Cliar.java` in your project and use it in your own `main` class:

```java
public class Main {
    public static void main(String[] args) {
        try {
            Cliar arguments = Cliar.from(args);

            // ...
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage()); // and/or display help message
            System.exit(-1);
        }

        // ...
    }
}
```

## Article

Read my article on DEV.to:
Building CLIAR — A simple drop-in Java class for parsing command-line arguments
[Part 1](https://dev.to/onebitwonder/building-cliar-a-simple-drop-in-java-class-for-parsing-command-line-arguments-part-1-jle)
[Part 2] ... comming soon