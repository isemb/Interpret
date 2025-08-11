# ASPInterp
Asp is a lightweight interpreter for a Python-inspired language, implemented in Java using Apache Ant.
It supports most of Python’s syntax, keywords, and built-in functions, while omitting object-oriented constructs and certain advanced features.

## Overview
- Python-like syntax and semantics

- Variables, functions, conditionals, and loops (including nested loops and nested if statements)

- Common keywords such as pass, global, if, else, for, while, etc.

- Most built-in operations and data types (numbers, strings, booleans, lists, dictionaries, etc.)

- Built-in functions such as print(), input(), len(), range(), str(), int(), and others

- No classes, inheritance, or nested function definitions inside functions

## How to run

Usage: java -jar asp.jar [-log{E|P|S|Y}] [-test{expr|parser|scanner}] file
