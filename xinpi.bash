#!/bin/bash

echo "welcome to xinpi system"
if [ $1x = "oldx" ] 
then
echo "old version"
java -Dpara="old" -jar xinpi-1.0-SNAPSHOT.jar
else 
echo "new version"
java -jar xinpi-1.0-SNAPSHOT.jar
fi
 
