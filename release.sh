#!/bin/bash
nedetlesmaki_version=`mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep -Ev '(^\[|Download\w+:)'`
case "$nedetlesmaki_version" in 
  *SNAPSHOT*)
    echo "Cannot release snapshot"
    exit
    ;;
esac
trash-put release
[ -e game/nedetlesmaki-${nedetlesmaki_version}-installer.exe ] && rm game/nedetlesmaki-${nedetlesmaki_version}-installer.exe
[ -e game/nedetlesmaki-${nedetlesmaki_version}-installer.jar ] && rm game/nedetlesmaki-${nedetlesmaki_version}-installer.jar
mvn clean package -Pizpack,rpm,rpm32
mkdir release
cp game/target/nedetlesmaki-${nedetlesmaki_version}-installer.jar release/
cp game/target/rpm/nedetlesmaki/RPMS/i686/nedetlesmaki-${nedetlesmaki_version}-1.i686.rpm release/
cp game/target/nedetlesmaki-${nedetlesmaki_version}-installer.jar ./game/
mvn install -Pwin
cp game/nedetlesmaki-${nedetlesmaki_version}-installer.exe release/
mvn clean package -Prpm,rpm64
cp game/target/rpm/nedetlesmaki/RPMS/x86_64/nedetlesmaki-${nedetlesmaki_version}-1.x86_64.rpm release/
mvn clean package -Pdeb
cp game/target/nedetlesmaki_${nedetlesmaki_version}.deb release/
