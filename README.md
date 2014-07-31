## s3-parallel
===========
s3 client that can parallel process tasks (PUT/GET/DELETE)

Support for PUT is now available.

PUT -C corePoolSize -M maxPoolSize -T keepAliceSeconds -A awsAccessKey -S awsSecretKey -B awsBucket -D sourceDirectory -L loggingMode[NONE|CONSOLE|FILE|FILE_CONSOLE]

eg. PUT -C 10 -M 10 -T 3600 -A AJKJHKJH232HJ2 -S jkhh4gh2jed%%^&RTYFHGsffsss -B mysamplebuckethatis282232unique -D /Users/someUser/someDirectory -L FILE_CONSOLE

ToDo : http://central.sonatype.org/pages/ossrh-guide.html