HOME_PATH=.
CLASS_PATH=./target/hellometrics-1.0-SNAPSHOT-jar-with-dependencies.jar
MAIN_CLASS=com.github.walterfan.hellometrics.OomDemo
startTime=$(date +%Y-%m-%d_%H_%M_%S)

HEAP_OPTS="-Xms128m -Xmx256m -XX:MaxMetaspaceSize=64m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$HOME_PATH/heapdump_$startTime.hprof"

GC_OPTS="-verbosegc -XX:+UseParallelGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -XX:+PrintGCDateStamps -Xloggc:./gc.log"

JAVA_OPTS="-server $HEAP_OPTS $GC_OPTS -XX:StringTableSize=100003 -Dsun.net.inetaddr.ttl=15 -Djava.security.egd=file:/dev/./urandom -Duser.timezone=GMT -Dfile.encoding=UTF8"

JMX_OPS="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=8091 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=true -Dcom.sun.management.jmxremote.password.file=./jmxremote.password -Dcom.sun.management.jmxremote.access.file=./jmxremote.access"

APP_OPTS="-t multimap -c 100000000"

if [ $# -eq 1 ]
  then
    APP_OPTS="-t $1 -c 1"
elif [ $# -eq 2 ]
  then
    APP_OPTS="-t $1 -c $2"
else
    APP_OPTS="-h"
fi

cmd="java $JAVA_OPTS $JMX_OPS -cp $CLASS_PATH $MAIN_CLASS $APP_OPTS"
echo $cmd
$cmd