#!/bin/bash
nedetlesmaki_version=`mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep -Ev '(^\[|Download\w+:)'`
case "$nedetlesmaki_version" in 
  *SNAPSHOT*)
    echo "Cannot release snapshot"
    exit
    ;;
esac
mkdir -p target
[ -e game/nedetlesmaki-${nedetlesmaki_version}-installer.exe ] && rm game/nedetlesmaki-${nedetlesmaki_version}-installer.exe
[ -e game/nedetlesmaki-${nedetlesmaki_version}-installer.jar ] && rm game/nedetlesmaki-${nedetlesmaki_version}-installer.jar
mvn clean package -Pizpack,rpm,rpm32,src
cp game/target/nedetlesmaki-game-${nedetlesmaki_version}-sources.zip target/
cp game/target/nedetlesmaki-game-${nedetlesmaki_version}-installer.jar target/
cp game/target/rpm/nedetlesmaki-game/RPMS/i686/nedetlesmaki-game-${nedetlesmaki_version}-1.i686.rpm target/
cp game/target/nedetlesmaki-game-${nedetlesmaki_version}-installer.jar ./game/
mvn install -Pwin
cp game/nedetlesmaki-game-${nedetlesmaki_version}-installer.exe target/
mvn package -Prpm,rpm64
cp game/target/rpm/nedetlesmaki-game/RPMS/x86_64/nedetlesmaki-game-${nedetlesmaki_version}-1.x86_64.rpm target/
mvn package -Pdeb
cp game/target/nedetlesmaki-game_${nedetlesmaki_version}.deb target/
