HOME_PATH=.
CLASS_PATH=./target/hellometrics-1.0-SNAPSHOT-jar-with-dependencies.jar
MAIN_CLASS=com.github.walterfan.hellometrics.OomDemo
startTime=$(date +%Y-%m-%d_%H_%M_%S)

JAVA_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$HOME_PATH/heapdump_$startTime.hprof -XX:+UseParallelGC -Xms128m -Xmx256m -XX:MaxMetaspaceSize=64m -XX:StringTableSize=100003 -Dsun.net.inetaddr.ttl=15 -Djava.security.egd=file:/dev/./urandom -Duser.timezone=GMT -Dfile.encoding=UTF8"

JMX_OPS="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=8091 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=true -Dcom.sun.management.jmxremote.password.file=./jmxremote.password -Dcom.sun.management.jmxremote.access.file=./jmxremote.access"

CMD_TYPE=multimap
CMD_COUNT=100000000

if [ $# -eq 1 ]
  then
    CMD_TYPE=$1
fi
if [ $# -eq 2 ]
  then
    CMD_TYPE=$1
    CMD_COUNT=$2
fi

APP_OPTS="-t $CMD_TYPE -c $CMD_COUNT"

cmd="java $JAVA_OPTS $JMX_OPS -cp $CLASS_PATH $MAIN_CLASS $APP_OPTS"
echo $cmd
$cmd