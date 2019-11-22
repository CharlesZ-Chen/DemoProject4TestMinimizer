# DemoProject4TestMinimizer

A complex demo for [delta debugger for CF and CFI](https://github.com/opprop/do-like-javac/pull/4).

## What it demos

This project includes the java source code which is annotated with nullness checker.
During the type-checking, the program will crash due to two errors in `AsSuperVisitor`.
Errors can be reproduced by a specify version of checker-framework: [aa6b5eea60](https://github.com/xingweitian/checker-framework/tree/aa6b5eea601369bcfc58c1cfa1e66ee8a834f4e0).

Using TestMinimizer, we get a minimum test case in which LOC is reduced from 3566 to 299.

**Notice that this demo is just to show how to use TestMinimizer with big project,
errors introduced in this demo may not be able to reproduce in the future.**

## How to run the demo

First, clone this repo:

```bash
git clone https://github.com/opprop/DemoProject4TestMinimizer.git
```

Then, under `DemoProject4TestMinimizer/complex_demo` directory, just simply running the script `run_debugging.sh`:

```bash
cd DemoProject4TestMinimizer/complex_demo
./run_debugging.sh
```
