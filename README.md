# Language Detector

Y3S1 Object Oriented Programming Assignment

## Description

A Java program that compares some query text against an [*n*-gram](https://en.wikipedia.org/wiki/N-gram) collection of subject text and attempts to determine the natural language of the query.

## Features

- The query and dataset are both parsed in separate threads, with each line of the dataset file parsed in its own worker thread using an `ExecutorService`.

- The query may be a text file, or it may be a string typed directly from the console menu.

- To reduce the time & space needed to parse the dataset file, the program will first try to determine what script the query is written in. For instance, if the query was found to be written in Cyrillic, then there would be no need to parse the dataset entries for languages that don't use the Cyrillic script. Determining the script was done by finding the most common `Character.UnicodeScript` seen in the query.

## Build

### Requirements

- JavaSE-11, or higher.

Download the project and run the following from inside the `bin/` directory to create a JAR file.

```sh
$ jar -cf ./oop.jar *
```

## Run

```sh
$ java -cp ./oop.jar ie.gmit.sw.Runner
```

## Example Output

While the application's accuracy is limited, it is still often able to accurately detect the language of a query. For instance, testing with the Japanese (`jp.txt`) and French (`fr.txt`) samples provided in the `samples/` directory produces the following output.

### Japanese

```
$ Enter WiLi data location (or press enter to use the default): datasets/wili-2018-Large-117500-Edited.txt
$ Enter the query text/file: samples/jp.txt
Processing query...
Building subject database...
Finished processing query.
Finished building subject database.

The text appears to be written in Japanese.

Time: 2.643 (s)
```

### French

```
$ Enter WiLi data location (or press enter to use the default):
$ Enter the query text/file: samples/fr.txt
Processing query...
Building subject database...
Finished processing query.
Finished building subject database.

The text appears to be written in French.

Time: 35.619 (s)
```
