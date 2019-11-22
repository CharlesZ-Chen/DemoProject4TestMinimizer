# DemoProject4TestMinimizer

Foo Project for demo [delta debugger for CF and CFI](https://github.com/opprop/do-like-javac/pull/4).

## What it demos

In src/C.java, it contains below code that is obviously a dereference null-pointer bug:

```java
Object foo = null;
foo.toString();
```

When running nullness checker on this Project, nullness checker would issue a de-reference error, and the output of nullness checker would be:

```
src/C.java:19: error: [dereference.of.nullable] dereference of possibly-null reference foo
        foo.toString();
```

For demo purpose, suppose the code segment that cause nullness checker produce a de-reference error on `foo` is the interesting test case we want to minimized from this project.

By running script `run_debugging.sh`, it will set the expect output from nullness checker should contain text "dereference of possibly-null reference foo", and then try to produce a minimized test case extracted from this project that still cause nullness checker output something contains the given expected matching text "dereference of possibly-null reference foo". This minimized test case can be found under `deltaTestCase` directory after running `run_debugging.sh`.

You may notice that the produced minimized test case still contains some un-related code to the interesting code segment, an can be further minimized. Generally this would also happens when running our tool on real project, as essentially delta debugging would only gives you a local minimized test case, not the global minimized one. Also, since we mutated files on line-level instead of on AST level, which cause the local minimized test case becomes even more coarse.

However, this tool still can help you find the one or two files that contains the interesting segment from hundreds source files in a given project, and then try to reduce the size of those interesting files (my own experience of running it on real projects  would generally reduce a 1000+ LoC file to around 100+,sometimes just 10+ lines).

## How to run the demo

First, clone this repo:

```bash
git clone https://github.com/opprop/DemoProject4TestMinimizer.git
```

Then, under `DemoProject4TestMinimizer/simple_demo` directory, just simply running the script `run_debugging.sh`:

```bash
cd DemoProject4TestMinimizer/simple_demo
./run_debugging.sh
```
