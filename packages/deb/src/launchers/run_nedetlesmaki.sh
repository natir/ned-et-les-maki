#!/usr/bin/env sh
if [ -e /usr/lib/jvm/java-7-openjdk-`/usr/share/jarwrapper/java-arch.sh`/bin/java ]
then
    /usr/lib/jvm/java-7-openjdk-`/usr/share/jarwrapper/java-arch.sh`/bin/java -jar /opt/nedetlesmaki/nedetlesmaki-game-${project.version}.jar $*
else
    /opt/nedetlesmaki/nedetlesmaki-game-${project.version}.jar $*
fi