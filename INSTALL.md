Requirements
============

Java JDK, [openjdk](http://openjdk.java.net/) or [Oracle](http://java.com): 1.7+
[Maven](http://www.maven.org) 3+

Compile
=======

Go to the source folder and run maven with the following command:

	mvn package

This will generate an executable jar in target subfolder named nedetlesmaki-game-${project.version}.jar

Install
=======

Manual installation
-------------------

- Create a directory, for example /opt/nedetlesmaki-game
- Copy the game/target/nedetlesmaki-game-${project.version}.jar, the game/target/natives, game/target/lib game/data folders into it.

Create packages and installers
------------------------------

Using maven 3 plugins, rpm and deb can be generated and also a generic [izpack](www.izpack.org) based installer.

	mvn clean package -Pizpack,deb,rpm,rpm32

Run
===


The game can be run using the following command:

	java -jar nedetlesmaki-game-${project.version}.jar

It will try to load the game data files in

	./data

or

	../data

NB: lwjgl shared libraries must be loadable, check the [lwjgl](www.lwjgl.org) documentation for more information.