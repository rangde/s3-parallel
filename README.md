## s3-parallel
===========
S3 client that can parallel process tasks (PUT/GET/DELETE)

Support for PUT is now available.

```PUT -C corePoolSize -M maxPoolSize -T keepAliveSeconds -A awsAccessKey -S awsSecretKey -B awsBucket [-R <region>] -D sourceDirectory -L loggingMode[NONE|CONSOLE|FILE|FILE_CONSOLE]```

eg. ```PUT -C 10 -M 10 -T 3600 -A AJKJHKJH232HJ2 -S jkhh4gh2jed%%^&RTYFHGsffsss -B mysamplebuckethatis282232unique -R ap-south-1 -D /Users/someUser/someDirectory -L FILE_CONSOLE```

ToDo : http://central.sonatype.org/pages/ossrh-guide.html
[mvn compile assembly:single]
