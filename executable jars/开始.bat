@echo off
start "server's output" java -jar BuffServer-1.0-SNAPSHOT-jar-with-dependencies.jar
start javaw -jar BuffClient-1.0-SNAPSHOT-jar-with-dependencies.jar