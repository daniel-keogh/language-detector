# Language Detector

Y3S1 OOP Assignment

## Description

Java programme that compares some query text against an [n-gram](https://en.wikipedia.org/wiki/N-gram) collection of subject text and attempts to determine the natural language of the query.

## Features

- The query and dataset are both parsed in separate threads, with each line of the dataset file parsed in its own worker thread using an `ExecutorService`.

- The query may be a text file or a string. However, you could also potentially implement the `ie.gmit.sw.Query` interface and design your own queries to be parsed by `ie.gmit.sw.QueryParser`.

## Build

### Requirements

- JavaSE-11, or higher.

Download the project and run the following from inside the `bin/` directory to create a JAR file.

```sh
$ jar –cf oop.jar ie/gmit/sw/*.class
```

## Run

```
$ java –cp ./oop.jar ie.gmit.sw.Runner
```

## Example Output

<!-- TODO -->

## Limitations

My implementation does not appear to be very accurate and rarely correctly determines the language of the query.

## Authors

Daniel Keogh
