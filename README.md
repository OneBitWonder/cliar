# CLIAR — pronounced “clear” (/klɪər/)

![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)
![Java Version](https://img.shields.io/badge/Java-17+-orange)
![Status](https://img.shields.io/badge/Project-Single--File%20Utility-green)

A lightweight, drop-in Java class for parsing command-line arguments.
It is designed as a simple alternative to full-featured CLI parsing frameworks, focusing on readability, transparency, and minimal dependencies.
This project is also part of a blog series where CLIAR is built step-by-step from a minimal parser to a more structured implementation.

---

## Features

- Single‑class parser, easy to include in any Java project
- Supports:
  - UNIX‑style short options (`-a`, `-abc`)
  - GNU‑style long options (`--verbose`, `--output=file.txt`)
- Required options
- Options with values (`--color=red`)
- Positional arguments
- Automatically generated help text with aligned formatting
- No external dependencies
- Provides validation with helpful exceptions

---

## Example

Include `Cliar.java` in your project and use it in your own `main` class:

```java
public class Main {

    private final Option verbose = new Option("v", "verbose", "Enable verbose output", false, false);

    private final Option color = new Option(null, "color", "Set output color", false, true);

    private final Option input = new Option(null, "input", "Input file", true, true);
    
    private void init(String[] args) {

        Cliar cliar = null;

        try {
            cliar = Cliar.from(args, new Cliar.Option[] {
                verbose,
                color,
                input
            });

            // ...

            if (cliar.getBoolean(verbose)) {
                // ...
            }

            // ...
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());

            if (null != cliar) System.err.println(cliar.help());

            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        (new Main()).init(args);
    }
}
```

## Usage

```bash
java MyApp -v --color=red input.txt
```

## Example Help Output

When an error occurs or when you explicitly print cliar.help(), CLIAR generates aligned, human‑readable help text:

```java
-v --verbose Enable verbose output
   --color   Set output color
   --input   Input file
```

This output is automatically formatted based on the declared options, with long option names padded to a uniform width for readability.

## Supported Syntax

| Type                   | Example             | Meaning                                |
|------------------------|---------------------|----------------------------------------|
| Short flag             | `-v`                | Boolean flag `v = true`                |
| Grouped short flags    | `-abc`              | Flags `a`, `b`, `c` all set to `true`  |
| Long flag              | `--verbose`         | Boolean option `verbose = true`        |
| Long key–value option  | `--output=file.txt` | Option `output = "file.txt"`           |

Invalid formats (e.g., `-9`, `--=value`, `---weird`) throw an exception.

## Motivation

Most Java CLI libraries are powerful but can feel heavy for small tools or learning purposes.
CLIAR explores how far you can go with a small, transparent implementation that you fully control and understand.

## Article

Read my article on DEV.to:  
Building CLIAR — A simple drop-in Java class for parsing command-line arguments  
[Part 1 - Introduction](https://dev.to/onebitwonder/building-cliar-a-simple-drop-in-java-class-for-parsing-command-line-arguments-part-1-jle)  
[Part 2 - Command line argument parsing](https://dev.to/onebitwonder/building-cliar-a-simple-drop-in-java-class-for-parsing-command-line-arguments-part-2-25ja)  
[Part 3 - Option registration and validation](https://dev.to/onebitwonder/building-cliar-a-simple-drop-in-java-class-for-parsing-command-line-arguments-part-3-2pd1)
[Part 4 - Accessing parsed options and generating help messages](https://dev.to/onebitwonder/building-cliar-a-simple-drop-in-java-class-for-parsing-command-line-arguments-final-part-of-4-2oe3)