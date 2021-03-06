#!/bin/bash

jar=target/paradox-1.0-SNAPSHOT.jar
expected=2016-01-01T12:00:00.000Z
command="java -javaagent:$jar=fixed=$expected -jar $jar -Dparadox.mode=fixed"
actual=`$command 2>/dev/null | tail -1`
if [ $actual == $expected ]; then
echo Set fixed value: passed.
else
echo Set fixed value: failed. Expected $expected but was $actual
exit 1
fi
