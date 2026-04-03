# CLIAR — pronounced “clear” (/klɪər/)

![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)
![Java Version](https://img.shields.io/badge/Java-17+-orange)
![Status](https://img.shields.io/badge/Project-Single--File%20Utility-green)

A simple drop-in Java class for parsing command-line arguments.

## Features

- Single‑class parser, easy to include in any Java project  
- Supports:
  - Single‑character short flags (`-x`)
  - Grouped short options (`-xyz`)
  - Long options (`--verbose`)
  - Long options with values (`--output=file.txt`)
- Normalizes all option names to lower‑case
- Provides validation with helpful exceptions
- Lightweight alternative to full command‑line parsing frameworks

## Usage Example

Include `Cliar.java` in your project and use it in your own `main` class:

```java
public class Main {
    public static void main(String[] args) {
        try {
            Cliar arguments = Cliar.from(args);

            // Access flags:
            // arguments.hasFlag("v");

            // Access long options:
            // arguments.getOption("output");

        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage()); // and/or display help message
            System.exit(-1);
        }

        // ...
    }
}
```

## Supported Syntax

| Type                   | Example             | Meaning                                |
|------------------------|---------------------|----------------------------------------|
| Short flag             | `-v`                | Boolean flag `v = true`                |
| Grouped short flags    | `-abc`              | Flags `a`, `b`, `c` all set to `true`  |
| Long flag              | `--verbose`         | Boolean option `verbose = true`        |
| Long key–value option  | `--output=file.txt` | Option `output = "file.txt"`           |

Invalid formats (e.g., `-9`, `--=value`, `---weird`) throw an exception.

## Article

Read my article on DEV.to:  
Building CLIAR — A simple drop-in Java class for parsing command-line arguments  
[Part 1](https://dev.to/onebitwonder/building-cliar-a-simple-drop-in-java-class-for-parsing-command-line-arguments-part-1-jle)  
Part 2 ... comming soon