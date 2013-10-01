#!/bin/sh
nedetlesmaki_version=`mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep -Ev '(^\[|Download\w+:)'`
rm nedetlesmaki-${nedetlesmaki_version}-installer.exe
rm nedetlesmaki-${nedetlesmaki_version}-installer.jar
mvn clean package -Pizpack,rpm,rpm32,src
cp target/nedetlesmaki_${nedetlesmaki_version}_sources.zip ../www/downloads/
cp target/nedetlesmaki-${nedetlesmaki_version}-installer.jar ../www/downloads/
cp target/rpm/nedetlesmaki/RPMS/i686/nedetlesmaki-${nedetlesmaki_version}-1.i686.rpm ../www/downloads/
cp target/nedetlesmaki-${nedetlesmaki_version}-installer.jar ./
mvn install -Pwin
cp nedetlesmaki-${nedetlesmaki_version}-installer.exe  ../www/downloads/
mvn package -Prpm,rpm64
cp target/rpm/nedetlesmaki/RPMS/x86_64/nedetlesmaki-${nedetlesmaki_version}-1.x86_64.rpm ../www/downloads/
mvn package -Pdeb
cp target/nedetlesmaki_${nedetlesmaki_version}.deb ../www/downloads/
