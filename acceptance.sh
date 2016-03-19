#!/bin/bash

command="java -javaagent:target/paradox.jar -jar target/paradox-test.jar -Dparadox.mode=fixed -Dparadox.fixed='2016-01-01T12:00:00.000Z'"
expected=2016-01-01T12:00:00.000Z
echo -n was:
$command
echo expected: $expected
