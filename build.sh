#!/bin/bash
mvn clean package -Pwin,deb,rpm
mkdir -p target/release
cp packages/generic/target/nedetlesmaki-installer*.jar target/release/
cp packages/win/target/nedetlesmaki-installer*.exe target/release/
cp packages/deb/target/nedetlesmaki*.deb target/release/
cp packages/rpm/target/rpm/nedetlesmaki/RPMS/*/nedetlesmaki-*.rpm target/release/
