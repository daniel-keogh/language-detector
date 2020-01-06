# Language Detector

Y3S1 OOP Assignment

## Description

Java programme that compares some query text against an [n-gram](https://en.wikipedia.org/wiki/N-gram) collection of subject text and attempts to determine the 
natural language of the query.

## Features

- The query and dataset are both parsed in separate threads, with each line of the dataset file parsed in its own worker thread using an `ExecutorService`.

- The query may be a text file, or it may be a string typed directly from the console menu. You could also potentially implement the `ie.gmit.sw.query.Query` 
interface and parse other objects too.

- I probably shouldn't have included this since it's incomplete, but it may be possible to significantly reduce the time needed to parse the dataset file if 
you can first determine what script the query is written in. For instance, if the query is found to be written in Greek, then there would be no need to parse 
the dataset entries for languages that don't use the Greek script. This can be done by checking if the Unicode value of each 1-gram of the query is within a certain range. 
I wrote a method inside `ie.gmit.sw.Script` that [seems to be able to determine the script of a string of text](https://user-images.githubusercontent.com/37158241/71834878-2b75c980-30a8-11ea-9ee4-f2759bda204b.png), 
but I haven't fully implemented this feature into the parser.

## Build

### Requirements

- JavaSE-11, or higher.

Download the project and run the following from inside the `bin/` directory to create a JAR file.

```sh
$ jar –cf ./oop.jar *
```

## Run

```sh
$ java –cp ./oop.jar ie.gmit.sw.Runner
```

## Limitations

My implementation does not appear to be very accurate and rarely correctly determines the language of the query.
