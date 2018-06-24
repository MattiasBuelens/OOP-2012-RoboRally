# RoboRally

The 2012 project for the course Object Oriented Programming in the second phase of the Bachelor of Engineering
at KU Leuven, Belgium.

The original project was built using Eclipse. It has been converted to an IntelliJ project and now uses Maven for building.
However, the source files are unchanged, and the resulting program runs exactly the same as the original.

![Screenshot](https://mattiasbuelens.github.io/OOP-2012-RoboRally/assets/screenshot.png)

## Authors
* [Mattias Buelens](https://github.com/MattiasBuelens)
* [Thomas Goossens](https://github.com/tgoossens)

## Usage

### Setup
To set up the project and install its dependencies:
```
$ mvn install
```

### Running
To compile and run the main program:
```
$ mvn compile exec:exec
```

To run the unit tests:
```
$ mvn test
```

To create a distributable JAR:
```
$ mvn compile assemble:single
```
