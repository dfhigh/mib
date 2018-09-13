#!/usr/bin/env bash
cur=$(cd `dirname $0`/..; pwd)

CMD="java -Dconfig.location=$cur/conf/config.properties -cp lib/*"

if [ "$1" = "--init" ]; then
    CMD = "$CMD org.mib.db.entry.DBInitializer"
elif [ "$1" = "--profile" ]; then
    CMD = "$CMD org.mib.db.entry.DBProfiler"
else
    echo "unknown phase $1, exiting..."
    exit 99
fi

echo $CMD
$CMD