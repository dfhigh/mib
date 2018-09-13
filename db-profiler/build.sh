#!/usr/bin/env bash
cd `dirname $0`/..
mvn clean package -pl db-profiler -am && cd db-profiler/target
mkdir -p profiler/conf && cp ../src/main/resources/*.xml profiler/conf/ && cp ../src/main/resources/*.properties profiler/conf/ && cp -fr ../src/main/resources/bin profiler && mv lib profiler && cp db-profiler*.jar profiler/lib/ && tar zcf profiler.tar.gz profiler