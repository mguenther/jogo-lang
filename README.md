# JOGO - A Java-based implementation of a subset of LOGO

[![Build Status](https://travis-ci.org/mguenther/jogo-lang.svg?branch=master)](https://travis-ci.org/mguenther/jogo-lang.svg)

After reading [Turtle Geometry](https://books.google.de/books/about/Turtle_geometry.html?id=3geYp44hJVcC&hl=en) 
by Abelson and diSessa I wanted to be able to test some of the ideas and programs that the authors talk about.
Of course, there are already plenty of LOGO interpreters in the wild, but since I like parsers and interpreters 
I decided to get my hands dirty and implement my own subset of a LOGO-like language to follow through these examples. 
Hence, JOGO was created.

The interpretation of your program basically emits commands for a so called `Turtle`. The `Turtle` reacts on those
commands, leaving a graphical trace of its movements behind. The book mentioned above uses these turtle graphics
in order to explore mathematical concepts in a playful and engaging way.

JOGO comes with a very minimalistic IDE so that you can run your programs and see the effect they have on the 
`Turtle`. The IDE is not capable of much, but you can save your programs to disk and load them again and of course
you can execute your program within the IDE. The screenshot underneath shows the IDE running the example program
`flower.jogo`.

![JOGO IDE with sample program](https://dl.dropboxusercontent.com/u/8084425/jogo.jpg)

## Language Features

JOGO implements a subset of a LOGO-like language. It currently features

* Built-in functions to emit commands to the `Turtle`
* User-supplied functions
* Control flow structures to support conditionals and iteration
* Expressions
* Lexical scoping

This feature set is indeed enough to get something useful out of JOGO programs. But there are still some basic 
features missing that would increase the capabilities of JOGO tremendously. As this project was just a playful way for
me to hack on a small parser and interpreter library, I don't know if I'll continue working on it. There is currently
no roadmap for future releases. But I do welcome feedback and PRs!

One of the things missing is recursion. Having the ability to define recursive functions would greatly enhance the
capabilities of JOGO and open up new ways to draw Turtle graphics.

To learn more about JOGO, its execution model and sample programs, please consult the projects Wiki.

## Module Overview

JOGO is comprised of three modules. These are:

* `jogo-lang-parser`: This module implements the lexer and parser for the JOGO language and contains the AST.
* `jogo-lang-interpreter`: This module implements a tree-based interpreter that is able to run your program by traversing the AST that is emitted from `jogo-lang-parser`.
* `jogo-lang-ide`: This module contains a JavaFX-based IDE that is able to save/load/run your programs. It uses `jogo-lang-interpreter` to execute your programs.

## Building JOGO

From the root directory of the project, issue a `mvn install` to compile all modules and install them into your
local Maven repository. Packaging the JavaFX-based IDE as an executable JAR can be done using another build target
that makes use of the `javafx-maven-plugin`.

Change into the directory of module `jogo-lang-ide`. Then

    $ mvn jfx:jar
    
will compile the IDE and package it into a JAR file. The resulting JAR file along with all external dependencies
will be located at `jogo-lang-ide/target/jfx/app`. To execute the JAR type in

    $ java -jar jogo-lang-ide-0.2.0-SNAPSHOT-jfx.jar -cp lib/
    
from within that directory.

## Examples

Examples that show the language features of JOGO are located in `src/main/jogo`.

## License

This software is released under the terms of the MIT license